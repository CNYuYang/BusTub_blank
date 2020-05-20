package run.yuyang.db.buffer;

/**
 * @author YuYang
 * <p>
 * Buffer pool 替换器
 */
public interface Replacer {


    /**
     * 寻找可以移除的牺牲frame id
     *
     * @return 可以移除的frame id，若没有则返回 -1
     */
    int victim();


    /**
     * 固定一个frame，确定他不会被移除
     *
     * @param frameId 固定frame的id
     */
    void pin(int frameId);


    /**
     * 解除固定frame，使他可以被移除
     *
     * @param frameId 解除固定frame的id
     */
    void unpin(int frameId);


    /**
     * 可以替换frame的数量
     *
     * @return 可以替换frame的数量
     */
    int size();

}
