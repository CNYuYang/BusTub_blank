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
     * ������ �� ��/�����������ݿ��ļ�����־�ļ�
     *
     * @param db ���ݿ��ļ���
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
     * �ر������ļ�
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
     * ��ָ��ҳ�������д������ļ�
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
     * ��ָ��ҳ������ݶ�������Ĵ洢��
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
     * ����־����д������ļ�
     * ����ͬ����ɺ󷵻أ����ҽ�ִ������д��
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
     * ����־���ݶ�������Ĵ洢��
     * ʼ�մ�ͷ��ʼ��ȡ��ִ��˳���ȡ
     *
     * @return: false��ʾ�Ѿ�����
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
     * ������ҳ�棨���紴������/��֮��Ĳ�����
     * ��֤page id����
     */
    public int allocatePage() {
        return nextPageId++;
    }

    /**
     * ���ص�ĿǰΪֹ���е�Flush����
     */
    public int getNumFlushes() {
        return numFlushes;
    }

    /**
     * ���ص�ĿǰΪֹ���е�Write����
     */
    public int getNumWrites() {
        return numWrites;
    }

    /**
     * �����ǰ����ˢ����־���򷵻�true
     */
    public boolean getFlushState(){
        return flushLogFlag;
    }


    /**
     * ��ȡ�ļ��ֽ���
     */
    private long getFileSize(String fileName) {
        File file = new File(fileName);
        return file.length();
    }


    public void deallocatePage(int pageId) {

    }
}
