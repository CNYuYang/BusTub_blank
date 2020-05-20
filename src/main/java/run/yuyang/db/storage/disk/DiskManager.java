package run.yuyang.db.storage.disk;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

import static run.yuyang.db.util.Config.PAGE_SIZE;

/**
 * @author YuYang
 */
@Slf4j
public class DiskManager {


    private RandomAccessFile dbFile;
    private FileOutputStream logWriter;
    private FileInputStream logReader;
    private String logName, fileName;
    private int numWrites = 0, numFlushes = 0, nextPageId = 0;
    private boolean flushLogFlag = false;

    /**
     * 构造器 ： 打开/创建单个数据库文件和日志文件
     *
     * @param db 数据库文件名
     */
    public DiskManager(String db) {
        fileName = db;
        if (!db.contains(".")) {
            log.debug("wrong file format");
            return;
        }
        logName = fileName.split("\\.")[0] + ".log";
        File file = new File(logName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            logWriter = new FileOutputStream(file, true);
            logReader = new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dbFile = new RandomAccessFile(fileName, "rw");
        } catch (IOException e) {
            try {
                File newFile = new File(fileName);
                newFile.createNewFile();
                dbFile = new RandomAccessFile(fileName, "rw");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 关闭所有文件
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        logReader.close();
        logWriter.close();
        dbFile.close();
        super.finalize();
    }

    /**
     * 将指定页面的内容写入磁盘文件
     */
    public void writePage(int pageId, byte[] data) {
        try {
            dbFile.seek(pageId * PAGE_SIZE);
            dbFile.write(data);
            numWrites++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将指定页面的内容读入给定的存储区
     */
    public void readPage(int pageId, byte[] data) {
        int offset = pageId * PAGE_SIZE;
        if (offset >= getFileSize(fileName)) {
            log.debug("I/O error while reading");
        } else {
            try {
                dbFile.seek(offset);
                dbFile.readFully(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将日志内容写入磁盘文件
     * 仅在同步完成后返回，并且仅执行序列写入
     */
    public void writeLog(byte[] logData, int size) {

        if (size == 0) {
            return;
        }
        flushLogFlag = true;
        numFlushes++;
        try {
            logWriter.write(logData, 0, size);
            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        flushLogFlag = false;
    }


    /**
     * 将日志内容读入给定的存储区
     * 始终从头开始读取并执行顺序读取
     *
     * @return: false表示已经结束
     */
    public boolean readLog(byte[] logData, int size, int offset) {
        if (offset >= getFileSize(logName)) {
            return false;
        }
        try {
            logReader.skip(offset);
            logReader.read(logData, 0, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 分配新页面（诸如创建索引/表之类的操作）
     * 保证page id增长
     */
    public int allocatePage() {
        return nextPageId++;
    }

    /**
     * 返回到目前为止进行的Flush次数
     */
    public int getNumFlushes() {
        return numFlushes;
    }

    /**
     * 返回到目前为止进行的Write次数
     */
    public int getNumWrites() {
        return numWrites;
    }

    /**
     * 如果当前正在刷新日志，则返回true
     */
    public boolean getFlushState(){
        return flushLogFlag;
    }


    /**
     * 获取文件字节数
     */
    private long getFileSize(String fileName) {
        File file = new File(fileName);
        return file.length();
    }


    public void deallocatePage(int pageId) {

    }
}
