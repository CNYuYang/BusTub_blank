package run.yuyang.db.recovery;

/**
 * @author YuYang
 */
public class LogManger {

    /**
     * 设置enable_logging = true
     * 启动一个单独的线程以定期执行刷新到磁盘操作
     * 超时或日志缓冲区已满或缓冲时可以触发刷新
     * 池管理器要强制刷新（仅在刷新的页面具有
     * 比持久LSN更大的LSN）
     *
     * 这个线程永远运行直到系统关闭/ StopFlushThread
     */
    public void runFlushThread() {}

    /**
     * 停止并加入刷新线程，设置enable_logging = false
     */
    public void stopFlushThread() {}

}
