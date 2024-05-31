package APIs;

import IntervalSet.IntervalSet;
import IntervalSet.MultiIntervalSet;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class calcAPIsTest {
    /**
     * Testing Strategy
     * Testing Similarity():
     *  集合：空集；非空集合，但没有相似时间段;非空集合有多个相似时间段
     * Testing calcConflictRatio():
     *  IntervalSet: 空集;非空集合，但不存在冲突时间段;非空集合,存在冲突时间段，同时冲突的段数最多为2;
     *               非空集合，存在冲突时间段，冲突的段数可以大于2
     *  IntervalSet.MultiIntervalSet: 空集；非空集合，不存在冲突时间段；非空集合，存在最大冲突段数<=2的冲突时间段；
     *                    非空集合，存在最大冲突段数>2的冲突时间段
     * Testing calcFreeTimeRatio():
     *  IntervalSet:空集合；非空集合，其中不存在空闲时间；非空集合，存在空闲时间
     *  IntervalSet.MultiIntervalSet:空集合；非空集合，其中不存在空闲时间；非空集合，存在空闲时间
     *
     */

    @Test
    public void SimilarityTest() throws IOException {
        MultiIntervalSet<String> intervals1 = MultiIntervalSet.empty();
        MultiIntervalSet<String> intervals2 = MultiIntervalSet.empty();

        calcAPIs calc = new calcAPIs();
        assertEquals(0,calc.Similarity(intervals1,intervals2),0.000001);  //空集合应该返回没有相似度

        intervals1.insert(3,5,"B");
        intervals2.insert(2,4,"C");
        assertEquals(0,calc.Similarity(intervals1,intervals2),0.000001);//非空集合，但是不存在相似时间段

        intervals1.insert(1,5,"A");
        intervals2.insert(2,3,"A");
        assertEquals(0.25,calc.Similarity(intervals1,intervals2),0.00001); //非空集合，有单独的相似时间段

        intervals1.insert(5,7,"D");
        intervals2.insert(5,7,"D");
        assertEquals(0.5,calc.Similarity(intervals1,intervals2),0.0000001); //非空集合，存在多个相似时间段
    }

    @Test
    public void calcConflictRatio() throws IOException {
        IntervalSet<String> intervals1 = IntervalSet.empty();
        MultiIntervalSet<String> intervals2 = MultiIntervalSet.empty();

        calcAPIs calc = new calcAPIs();
        assertEquals(0,calc.calcConflictRatio(intervals1),0.00001); //空集合情况
        assertEquals(0,calc.calcConflictRatio(intervals2),0.00001); //空集合情况

        intervals1.insert(1,5,"A");
        intervals1.insert(7,9,"B");
        intervals2.insert(1,5,"A");
        intervals2.insert(7,9,"B");
        assertEquals(0,calc.calcConflictRatio(intervals1),0.00001); //非空集合，但是不存在冲突时间段
        assertEquals(0,calc.calcConflictRatio(intervals2),0.00001); //非空集合，但是不存在冲突时间段

        intervals1.insert(2,4,"C");
        intervals2.insert(2,4,"C");
        assertEquals(0.25,calc.calcConflictRatio(intervals1),0.000001); //非空集合，存在最大冲突段数等于2的冲突时间段
        assertEquals(0.25,calc.calcConflictRatio(intervals2),0.000001); //非空集合，存在最大冲突段数等于2的冲突时间段

        intervals1.insert(3,5,"D");
        intervals2.insert(3,5,"B");
        assertEquals(0.375,calc.calcConflictRatio(intervals1),0.000001);//非空集合，存在最大冲突段数大于2的冲突时间段
        assertEquals(0.375,calc.calcConflictRatio(intervals2),0.000001);//非空集合，存在最大冲突段数大于2的冲突时间段
    }

    @Test
    public void calcFreeTimeRatio() throws IOException {
        IntervalSet<String> intervals1 = IntervalSet.empty();
        MultiIntervalSet<String> intervals2 = MultiIntervalSet.empty();

        calcAPIs calc = new calcAPIs();
//        assertEquals(0,calc.calcFreeTimeRatio(intervals1,-1),0.00001); //空集合
//        assertEquals(0,calc.calcFreeTimeRatio(intervals2,-1),0.00001); //空集合

        intervals1.insert(1,5,"A");
        intervals1.insert(5,9,"B");
        intervals2.insert(1,5,"A");
        intervals2.insert(5,9,"A");
//        assertEquals(0,calc.calcFreeTimeRatio(intervals1,-1),0.00001); //非空集合，但是不存在空闲时间
//        assertEquals(0,calc.calcFreeTimeRatio(intervals2,9),0.00001); //非空集合，但是不存在非空集合

        intervals1.insert(15,21,"C");
        intervals2.insert(15,21,"A");
        assertEquals(-1,calc.calcFreeTimeRatio(intervals1,20),0.00001); //非空集合，但是存在空闲时间
        assertEquals(-1,calc.calcFreeTimeRatio(intervals2,20),0.00001); //非空集合，但是存在空闲时间
        assertEquals(0.23809,calc.calcFreeTimeRatio(intervals1,21),0.001); //非空集合，但是存在空闲时间
        assertEquals(0.23809,calc.calcFreeTimeRatio(intervals2,21),0.001); //非空集合，但是存在空闲时间
    }
}
