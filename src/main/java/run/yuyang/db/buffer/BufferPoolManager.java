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
     * 从缓冲池中获取请求的页面
     *
     * @param pageId 页面id
     * @return 对应的page
     */
    public Page fetchPageImpl(int pageId) {
        return null;
    }


    /**
     * 取消指定页面page的固定
     *
     * @param pageId  page_id要取消固定的页面的ID
     * @param isDirty is_dirty如果页面应标记为脏，则为true，否则为false
     * @return 如果在此调用之前页面固定计数<= 0 ， 则返回false ， 否则返回true
     */
    public boolean unpinPageImpl(int pageId, boolean isDirty) {

        return false;
    }

    /**
     * 将目标页面刷新到磁盘。
     *
     * @param pageId page_id要刷新的页面的ID，不能为INVALID_PAGE_ID
     * @return 如果在页面表中找不到该页面，则返回false，否则返回true
     */
    public boolean flushPageImpl(int pageId) {

        return false;
    }

    /**
     * 在buffer pool中创建一个新的page
     *
     * @return 新的page
     */
    public Page newPageImpl() {

        return null;
    }

    /**
     * 从缓冲池中删除页面
     *
     * @param pageId page_id要删除的页面的ID
     * @return 如果该页面存在但无法删除，则返回false；如果该页面不存在或删除成功，则返回true
     */
    public boolean deletePageImpl(int pageId) {

        return true;
    }

    /**
     * 将缓冲池中的所有页面刷新到磁盘。
     */
    public void flushAllPagesImpl() {
    }
}
