package SpecialSet;

import IntervalSet.IntervalSet;
import IntervalSet.MultiIntervalSet;
import SpecialSet.inter.NonOverlapIntervalSet;
import SpecialSet.inter.impl.NonOverlapIntervalSetImpl;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NonOverlapIntervalSetTest {
    /**
     * Testing Strategy
     * Testing insertForI():
     *  intervals:空集合;非空集合
     *  start:<0;=0;>0
     *  end:>start;<=start
     *  label：重复；非重复
     * Testing insertForM():
     *  MultiIntervals:空集合;非空集合
     *  start:<0;=0;>0
     *  end:>start ; <=start
     *
     * 测试策略：每种可能至少覆盖一次
     */

    @Test
    public void insertForITest() throws IOException {
        //由于是变值函数，所以每一步都要检查是否正确
        IntervalSet<String> intervals = IntervalSet.empty();
        NonOverlapIntervalSet<String> noOverlap = new NonOverlapIntervalSetImpl<>(intervals);
        Set<String> labels;

        noOverlap.insert(0,4,"A"); //对空集合进行插入
        labels = intervals.labels();
        assertEquals(1,labels.size());
        assertEquals(0,intervals.start("A"));
        assertEquals(4,intervals.end("A"));


        intervals.insert(4,6,"B");
        labels = intervals.labels();
        assertEquals(2,labels.size());
        assertEquals(0,intervals.start("A"));
        assertEquals(4,intervals.end("A"));
        assertEquals(4,intervals.start("B"));
        assertEquals(6,intervals.end("B"));

        assertThrows(IOException.class, () -> {
            noOverlap.insert(3,5,"C"); //对非空集合进行插入,并且不符合非重叠的要求
        });

        labels = intervals.labels();
        assertEquals(2,labels.size());
        assertEquals(0,intervals.start("A"));
        assertEquals(4,intervals.end("A"));
        assertEquals(4,intervals.start("B"));
        assertEquals(6,intervals.end("B"));

        assertThrows(IOException.class, () -> {
            noOverlap.insert(-1,3,"D"); //start < 0
        });

        labels = intervals.labels();
        assertEquals(2,labels.size());
        assertEquals(0,intervals.start("A"));
        assertEquals(4,intervals.end("A"));
        assertEquals(4,intervals.start("B"));
        assertEquals(6,intervals.end("B"));

        assertThrows(IOException.class, () -> {
            noOverlap.insert(5,4,"E");  //end<start
        });

        labels = intervals.labels();
        assertEquals(2,labels.size());
        assertEquals(0,intervals.start("A"));
        assertEquals(4,intervals.end("A"));
        assertEquals(4,intervals.start("B"));
        assertEquals(6,intervals.end("B"));

        assertThrows(IOException.class, () -> {
            noOverlap.insert(9,14,"A"); //标签重复
        });

        labels = intervals.labels();
        assertEquals(2,labels.size());
        assertEquals(0,intervals.start("A"));
        assertEquals(4,intervals.end("A"));
        assertEquals(4,intervals.start("B"));
        assertEquals(6,intervals.end("B"));

        noOverlap.insert(9,14,"C"); //向非空集合插入并且符合非重叠性的要求
        labels = intervals.labels();
        assertEquals(3,labels.size());
        assertEquals(0,intervals.start("A"));
        assertEquals(4,intervals.end("A"));
        assertEquals(4,intervals.start("B"));
        assertEquals(6,intervals.end("B"));
        assertEquals(9,intervals.start("C"));
        assertEquals(14,intervals.end("C"));
    }

    @Test
    public void insertForMTest() throws IOException {
        MultiIntervalSet<String> intervals = MultiIntervalSet.empty();
        NonOverlapIntervalSet<String> noOverlap = new NonOverlapIntervalSetImpl<>(intervals);
        IntervalSet<Integer> interval = IntervalSet.empty();

        noOverlap.insert(0,4,"A"); //对空集合进行插入
        assertEquals(1,intervals.labels().size());
        interval.insert(0,4,0);
        assertEquals(interval.toString(),intervals.intervals("A").toString());

        noOverlap.insert(4,6,"A");     //向非空集合中插入
        assertEquals(1,intervals.labels().size());
        interval.insert(4,6,1);
        assertEquals(interval.toString(),intervals.intervals("A").toString());

        assertThrows(IOException.class, () -> {
            noOverlap.insert(-1,3,"A"); //start < 0
        });


        assertEquals(1,intervals.labels().size());
        assertEquals(interval.toString(),intervals.intervals("A").toString());

        assertThrows(IOException.class, () -> {
            noOverlap.insert(5,4,"A");  //end<start
        });


        assertEquals(1,intervals.labels().size());
        assertEquals(interval.toString(),intervals.intervals("A").toString());
    }
}
