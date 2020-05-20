package run.yuyang.db.buffer;

import lombok.extern.slf4j.Slf4j;
import run.yuyang.db.recovery.LogManger;
import run.yuyang.db.storage.disk.DiskManager;
import run.yuyang.db.storage.page.Page;

import java.util.*;

import static run.yuyang.db.util.Config.PAGE_SIZE;

/**
 * @author YuYang
 */
@Slf4j
public class BufferPoolManager {

    private final DiskManager diskManager;
    private final LogManger logManger;
    private final int poolSize;
    private final ClockReplacer clockReplacer;
    private final Page[] pages;
    private final LinkedList<Integer> freeList = new LinkedList<>();
    private final Object latch = new Object();

    public BufferPoolManager(DiskManager diskManager, LogManger logManger, int poolSize) {
        this.diskManager = diskManager;
        this.logManger = logManger;
        this.poolSize = poolSize;
        clockReplacer = new ClockReplacer(poolSize);
        pages = new Page[poolSize];
        for (int i = 0; i < poolSize; i++) {
            freeList.add(i);
        }
    }

    /**
     * �ӻ�����л�ȡ�����ҳ��
     *
     * @param pageId ҳ��id
     * @return ��Ӧ��page
     */
    public Page fetchPageImpl(int pageId) {
        return null;
    }


    /**
     * ȡ��ָ��ҳ��page�Ĺ̶�
     *
     * @param pageId  page_idҪȡ���̶���ҳ���ID
     * @param isDirty is_dirty���ҳ��Ӧ���Ϊ�࣬��Ϊtrue������Ϊfalse
     * @return ����ڴ˵���֮ǰҳ��̶�����<= 0 �� �򷵻�false �� ���򷵻�true
     */
    public boolean unpinPageImpl(int pageId, boolean isDirty) {

        return false;
    }

    /**
     * ��Ŀ��ҳ��ˢ�µ����̡�
     *
     * @param pageId page_idҪˢ�µ�ҳ���ID������ΪINVALID_PAGE_ID
     * @return �����ҳ������Ҳ�����ҳ�棬�򷵻�false�����򷵻�true
     */
    public boolean flushPageImpl(int pageId) {

        return false;
    }

    /**
     * ��buffer pool�д���һ���µ�page
     *
     * @return �µ�page
     */
    public Page newPageImpl() {

        return null;
    }

    /**
     * �ӻ������ɾ��ҳ��
     *
     * @param pageId page_idҪɾ����ҳ���ID
     * @return �����ҳ����ڵ��޷�ɾ�����򷵻�false�������ҳ�治���ڻ�ɾ���ɹ����򷵻�true
     */
    public boolean deletePageImpl(int pageId) {

        return true;
    }

    /**
     * ��������е�����ҳ��ˢ�µ����̡�
     */
    public void flushAllPagesImpl() {
    }
}
