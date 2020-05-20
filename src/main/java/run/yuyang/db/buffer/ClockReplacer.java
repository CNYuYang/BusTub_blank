package run.yuyang.db.buffer;

import java.util.Arrays;

/**
 * @author YuYang
 */
public class ClockReplacer implements Replacer {


    public ClockReplacer(int poolSize) {

    }

    @Override
    public int victim() {
        return 0;
    }

    @Override
    public void pin(int frameId) {

    }

    @Override
    public void unpin(int frameId) {

    }

    @Override
    public int size() {
        return 0;
    }
}
