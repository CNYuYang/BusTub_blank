package run.yuyang.db.recovery;

/**
 * @author YuYang
 */
public class LogManger {

    /**
     * ����enable_logging = true
     * ����һ���������߳��Զ���ִ��ˢ�µ����̲���
     * ��ʱ����־�����������򻺳�ʱ���Դ���ˢ��
     * �ع�����Ҫǿ��ˢ�£�����ˢ�µ�ҳ�����
     * �ȳ־�LSN�����LSN��
     *
     * ����߳���Զ����ֱ��ϵͳ�ر�/ StopFlushThread
     */
    public void runFlushThread() {}

    /**
     * ֹͣ������ˢ���̣߳�����enable_logging = false
     */
    public void stopFlushThread() {}

}
