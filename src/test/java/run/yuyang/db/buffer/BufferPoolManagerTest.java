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

        // �����������Ϊ�ա�����Ӧ���ܹ�����һ����ҳ�档
        Page page0 = bufferPoolManager.newPageImpl();
        assertNotNull(page0);
        assertEquals(page0.getPageId(), 0);

        // �����������������
        byte[] randomBinaryData = new byte[PAGE_SIZE];
        Random random = new Random();
        random.nextBytes(randomBinaryData);

        // ���м�ͽ�β�����ն��ַ�
        randomBinaryData[PAGE_SIZE / 2] = '\0';
        randomBinaryData[PAGE_SIZE - 1] = '\0';

        // ����������ҳ�������Ӧ���ܹ���д���ݡ�
        page0.setData(randomBinaryData);
        for (int i = 0; i < PAGE_SIZE; i++) {
            assertEquals(page0.getData()[i], randomBinaryData[i]);
        }
        // ����������仺���֮ǰ������Ӧ���ܹ�������ҳ�档
        for (int i = 1; i < bufferpoolSize; ++i) {
            assertNotNull(bufferPoolManager.newPageImpl());
        }

        // ������һ����������������ǾͲ��ܴ����κ���ҳ�档
        for (int i = bufferpoolSize; i < bufferpoolSize * 2; ++i) {
            assertNull(bufferPoolManager.newPageImpl());
        }

        // ������ȡ���̶�ҳ��{0��1��2��3��4}���̶�����5����ҳ���
        // ��Ȼ��һ������֡���Զ�ȡ��0ҳ��
        for (int i = 0; i < 5; ++i) {
            assertEquals(true, bufferPoolManager.unpinPageImpl(i, true));
            bufferPoolManager.flushPageImpl(i);
        }

        for (int i = 0; i < 5; ++i) {
            Page page = bufferPoolManager.newPageImpl();
            assertNotNull(page);
            bufferPoolManager.unpinPageImpl(page.getPageId(), false);
        }

        //����������Ӧ���ܹ���ȡ����֮ǰ��д�����ݡ�
        page0 = bufferPoolManager.fetchPageImpl(0);
        for (int i = 0; i < PAGE_SIZE; i++) {
            assertEquals(page0.getData()[i], randomBinaryData[i]);
        }
        File file = new File("test.db");

        file.delete();
    }

}