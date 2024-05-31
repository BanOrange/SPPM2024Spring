package SpecialSet;

import IntervalSet.IntervalSet;
import IntervalSet.MultiIntervalSet;
import SpecialSet.inter.NoBlankIntervalSet;
import SpecialSet.inter.impl.NoBlankIntervalSetImpl;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NoBlankIntervalSetTest {
    /**
     * Testing Strategy
     * Testing for checkNoBlank():
     * IntervalSet:空集合;没有空白区间的集合;有空白区间的集合
     * IntervalSet.MultiIntervalSet: 空集合; 没有空白区间的集合;有空白区间的集合
     *                   所有标签都对应多个时间段;不是所有标签都对应多个时间段
     *
     * 测试策略：每种情况至少覆盖一次
     */
    
    @Test
    public void checkNoBlankIntervalSetTest() throws IOException {
        IntervalSet<String> intervals = IntervalSet.empty();
        NoBlankIntervalSet<String> noBlank = new NoBlankIntervalSetImpl<>(intervals);

        assertEquals(false,noBlank.checkNoBlank(0,10)); //空集合

        intervals.insert(1,5,"A");
        intervals.insert(5,8,"B");
        assertEquals(true,noBlank.checkNoBlank(1,8));  //覆盖了时间区间的集合

        intervals.insert(9,11,"C");
        assertEquals(true,noBlank.checkNoBlank(1,11)); //覆盖整个时间区间的集合
        assertEquals(false,noBlank.checkNoBlank(1,14));

    }

    @Test
    public void checkNoBlankMultiIntervalSetTest() throws IOException {
        MultiIntervalSet<String> intervals = MultiIntervalSet.empty();
        NoBlankIntervalSet<String> noBlank = new NoBlankIntervalSetImpl<>(intervals);

        assertEquals(false,noBlank.checkNoBlank(1,10));  //空集合

        intervals.insert(1,5,"A");
        intervals.insert(5,9,"A");
        intervals.insert(9,11,"B");
        assertEquals(true,noBlank.checkNoBlank(1,11)); //存在有标签不对应多个时间段，且为无空白集合

        intervals.insert(12,15,"B");
        assertEquals(false,noBlank.checkNoBlank(1,15)); //所有标签都对应多个时间段，且为有空白集合
    }
}
