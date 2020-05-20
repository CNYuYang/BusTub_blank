package run.yuyang.db.buffer;

/**
 * @author YuYang
 * <p>
 * Buffer pool �滻��
 */
public interface Replacer {


    /**
     * Ѱ�ҿ����Ƴ�������frame id
     *
     * @return �����Ƴ���frame id����û���򷵻� -1
     */
    int victim();


    /**
     * �̶�һ��frame��ȷ�������ᱻ�Ƴ�
     *
     * @param frameId �̶�frame��id
     */
    void pin(int frameId);


    /**
     * ����̶�frame��ʹ�����Ա��Ƴ�
     *
     * @param frameId ����̶�frame��id
     */
    void unpin(int frameId);


    /**
     * �����滻frame������
     *
     * @return �����滻frame������
     */
    int size();

}
