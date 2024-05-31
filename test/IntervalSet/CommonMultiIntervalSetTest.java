package IntervalSet;

import IntervalSet.MultiIntervalSet;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CommonMultiIntervalSetTest {
    /**
     * Testing Strategy
     * Testing toString():
     *  集合：空集合;非空集合
     *  label：对应一个时间段;对应多个时间段
     *
     *  测试策略：每种可能至少测试一次
     */

    @Test
    public void toStringTest() throws IOException {
        MultiIntervalSet<String> intervals = MultiIntervalSet.empty();

        assertEquals("",intervals.toString()); //空集合

        intervals.insert(0,5,"A");
        intervals.insert(5,10,"B");
        intervals.insert(10,15,"B");
        assertEquals("Label:A  startTime:0  endTime:5\n" +
                "Label:B  startTime:5  endTime:10\n" +
                "Label:B  startTime:10  endTime:15\n",intervals.toString());
    }
}
