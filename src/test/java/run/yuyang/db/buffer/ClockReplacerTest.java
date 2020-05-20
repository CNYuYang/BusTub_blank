package run.yuyang.db.buffer;

import junit.framework.TestCase;
import org.junit.Test;

public class ClockReplacerTest extends TestCase {

    @Test
    public void test() {
        ClockReplacer clock_replacer = new ClockReplacer(7);

        // Scenario: unpin six elements, i.e. add them to the replacer.
        clock_replacer.unpin(1);
        clock_replacer.unpin(2);
        clock_replacer.unpin(3);
        clock_replacer.unpin(4);
        clock_replacer.unpin(5);
        clock_replacer.unpin(6);
        clock_replacer.unpin(1);
        assertEquals(6, clock_replacer.size());

        // Scenario: get three victims from the clock.
        int value;
        value = clock_replacer.victim();
        assertEquals(1, value);
        value = clock_replacer.victim();
        assertEquals(2, value);
        value = clock_replacer.victim();
        assertEquals(3, value);

        // Scenario: pin elements in the replacer.
        // Note that 3 has already been victimized, so pinning 3 should have no effect.
        clock_replacer.pin(3);
        clock_replacer.pin(4);
        assertEquals(2, clock_replacer.size());

        // Scenario: unpin 4. We expect that the reference bit of 4 will be set to 1.
        clock_replacer.unpin(4);

        // Scenario: continue looking for victims. We expect these victims.
        value = clock_replacer.victim();
        assertEquals(5, value);
        value = clock_replacer.victim();
        assertEquals(6, value);
        value = clock_replacer.victim();
        assertEquals(4, value);
    }
}