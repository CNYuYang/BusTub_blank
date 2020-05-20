package run.yuyang.db.buffer;

import junit.framework.TestCase;
import org.junit.Test;
import run.yuyang.db.storage.disk.DiskManager;
import run.yuyang.db.storage.page.Page;

import java.io.File;
import java.util.Random;

import static run.yuyang.db.util.Config.PAGE_SIZE;

public class BufferPoolManagerTest extends TestCase {

    @Test
    public void test() {

        String dbName = "test.db";
        int bufferpoolSize = 10;
        DiskManager diskManager = new DiskManager(dbName);
        BufferPoolManager bufferPoolManager = new BufferPoolManager(diskManager, null, bufferpoolSize);

        // 场景：缓冲池为空。我们应该能够创建一个新页面。
        Page page0 = bufferPoolManager.newPageImpl();
        assertNotNull(page0);
        assertEquals(page0.getPageId(), 0);

        // 生成随机二进制数据
        byte[] randomBinaryData = new byte[PAGE_SIZE];
        Random random = new Random();
        random.nextBytes(randomBinaryData);

        // 在中间和结尾插入终端字符
        randomBinaryData[PAGE_SIZE / 2] = '\0';
        randomBinaryData[PAGE_SIZE - 1] = '\0';

        // 场景：有了页面后，我们应该能够读写内容。
        page0.setData(randomBinaryData);
        for (int i = 0; i < PAGE_SIZE; i++) {
            assertEquals(page0.getData()[i], randomBinaryData[i]);
        }
        // 场景：在填充缓冲池之前，我们应该能够创建新页面。
        for (int i = 1; i < bufferpoolSize; ++i) {
            assertNotNull(bufferPoolManager.newPageImpl());
        }

        // 场景：一旦缓冲池已满，我们就不能创建任何新页面。
        for (int i = bufferpoolSize; i < bufferpoolSize * 2; ++i) {
            assertNull(bufferPoolManager.newPageImpl());
        }

        // 场景：取消固定页面{0，1，2，3，4}并固定另外5个新页面后，
        // 仍然有一个缓存帧可以读取第0页。
        for (int i = 0; i < 5; ++i) {
            assertEquals(true, bufferPoolManager.unpinPageImpl(i, true));
            bufferPoolManager.flushPageImpl(i);
        }

        for (int i = 0; i < 5; ++i) {
            Page page = bufferPoolManager.newPageImpl();
            assertNotNull(page);
            bufferPoolManager.unpinPageImpl(page.getPageId(), false);
        }

        //场景：我们应该能够获取我们之前编写的数据。
        page0 = bufferPoolManager.fetchPageImpl(0);
        for (int i = 0; i < PAGE_SIZE; i++) {
            assertEquals(page0.getData()[i], randomBinaryData[i]);
        }
        File file = new File("test.db");

        file.delete();
    }

}