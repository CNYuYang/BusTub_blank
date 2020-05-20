package run.yuyang.db.storage.disk;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

@Slf4j
public class DiskManagerTest extends TestCase {

    @Test
    public void testPage() {
        DiskManager diskManager = new DiskManager("1.db");
        byte[] tempData = new byte[1024 * 2];
        /*��page0 -2 д������*/
        Arrays.fill(tempData, (byte) 0);
        diskManager.writePage(diskManager.allocatePage(), tempData);
        Arrays.fill(tempData, (byte) 1);
        diskManager.writePage(diskManager.allocatePage(), "tempData".getBytes());
        Arrays.fill(tempData, (byte) 2);
        diskManager.writePage(diskManager.allocatePage(), tempData);

        /*��ȡpage 1�е�����*/
        diskManager.readPage(1, tempData);
        assertEquals(tempData.length, 1024 * 2);
        log.debug("{}", tempData);

        /*��page 5д������*/
        Arrays.fill(tempData, (byte) 5);
        diskManager.writePage(5, tempData);
        diskManager.readPage(5, tempData);
        assertEquals(tempData.length, 1024 * 2);
        log.debug("{}", tempData);

        /*��ȡpage 6����*/
        diskManager.readPage(6, tempData);
        log.debug("{}", tempData);
    }

    public void testLog() {
        DiskManager diskManager = new DiskManager("1.db");
        diskManager.writeLog("YuYang".getBytes(), 6);
        byte[] bytes = new byte[6];
        diskManager.readLog(bytes, 6, 0);
        log.debug(new String(bytes));

    }
}