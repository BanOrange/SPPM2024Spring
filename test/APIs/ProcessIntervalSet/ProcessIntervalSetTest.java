package APIs.ProcessIntervalSet;

import IntervalSet.IntervalSet;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ProcessIntervalSetTest {
    /**
     * Testing Strategy
     * <p>
     * Testing insert():
     *   start:<0;=0;>0;
     *   end:<start;>=start;
     *   label:没有多个时间段;有多个时间段
     *   集合：重叠；不重叠
     *
     * Testing labels():
     *   IntervalSet:空集合;非空集合
     *
     * Testing remove():
     *   label:有一个时间段与之对应；有多个时间段与之对应；不存在于集合
     *
     * Testing intervals():
     *  label:有对应多个时间段;没有时间段;不存在于集合当中
     *
     * Testing checkNoPeriod():
     *  集合：有重复时间；无重复时间
     *
     * Testing toString()：
     *  集合：空集合；非空集合
     *
     * 测试策略：每种情况至少测试一次
     */

    //测试正常情况下的insert方法
    @Test
    public void insertNormalTest() throws IOException {
        ProcessIntervalSet<String> intervals = new ProcessIntervalSet<>();
        IntervalSet<Integer> set = IntervalSet.empty();

        intervals.insert(0,15,"A"); //正常情况
        set.insert(0,15,0);
        assertEquals(set.toString(),intervals.intervals("A").toString());

        intervals.insert(16,17,"A"); //A已经有时间段与之绑定
        set.insert(16,17,1);
        assertEquals(set.toString(),intervals.intervals("A").toString());

        intervals.insert(19,20,"A");
        set.insert(19,20,2);
        assertEquals(set.toString(),intervals.intervals("A").toString());


    }

    //测试应该抛出异常情况下的insert方法
    @Test(expected = IOException.class)
    public void insertExceptionTest() throws IOException {
        ProcessIntervalSet<String> intervals = new ProcessIntervalSet<>();

        intervals.insert(-1,2,"A"); //start<0
        assertEquals(0,intervals.labels().size());
        intervals.insert(7,5,"A");  //end<start
        assertEquals(0,intervals.labels().size());
        intervals.insert(1,5,"B");
        assertEquals(1,intervals.labels().size());
        intervals.insert(2,3,"B");  //和B已有的时间段重叠
        assertEquals(1,intervals.labels().size());
        intervals.insert(2,6,"C");
        assertEquals(1,intervals.labels().size());
        assertEquals(true,intervals.labels().contains("B"));

    }

    //测试labels方法
    @Test
    public void labelsTest() throws IOException {
        ProcessIntervalSet<String> intervals = new ProcessIntervalSet<>();

        Set<String> labels = new HashSet<>();
        assertEquals(labels,intervals.labels());  //空集合情况

        intervals.insert(0,5,"A");
        intervals.insert(5,10,"B");
        intervals.insert(10,15,"C");
        labels.add("A");
        labels.add("B");
        labels.add("C");
        assertEquals(labels,intervals.labels());      //非空集合情况
    }

    //测试remove方法
    @Test
    public void removeTest() throws IOException {
        //由于本方法是变值方法，因此需要检测每一步中集合的值是否正确变化
        ProcessIntervalSet<String> intervals = new ProcessIntervalSet<>();
        assertEquals(true,intervals.labels().isEmpty());
        assertEquals(false,intervals.remove("A"));  //集合中不包含该标签

        intervals.insert(0,5,"A");
        assertEquals(1,intervals.labels().size());
        assertEquals(true,intervals.labels().contains("A"));
        assertEquals(true,intervals.remove("A")); //集合中该标签包括一个与之对应的时间段
        assertEquals(true,intervals.labels().isEmpty());

        intervals.insert(5,10,"B");
        intervals.insert(10,15,"B");
        assertEquals(1,intervals.labels().size());
        assertEquals(true,intervals.labels().contains("B"));
        IntervalSet<Integer> inter = IntervalSet.empty();
        inter.insert(5,10,0);
        inter.insert(10,15,1);
        assertEquals(inter.toString(),intervals.intervals("B").toString());
        assertEquals(true,intervals.remove("B")); //标签与多个时间段对应
        assertEquals(true,intervals.labels().isEmpty());

    }

    //测试inters方法
    @Test
    public void intervalsTest() throws IOException {
        ProcessIntervalSet<String> intervals = new ProcessIntervalSet<>();
        IntervalSet<Integer> inter = IntervalSet.empty();

        assertEquals(inter.toString(),intervals.intervals("A").toString()); //不存在于集合之中

        intervals.insert(0,5,"A");
        inter.insert(0,5,0);
        assertEquals(inter.toString(),intervals.intervals("A").toString()); //集合中该标签包括一个与之对应的时间段
        inter.remove(0);

        intervals.insert(5,10,"B");
        intervals.insert(10,15,"B");
        inter.insert(5,10,0);
        inter.insert(10,15,1);
        assertEquals(inter.toString(),intervals.intervals("B").toString());  //集合中该标签有多个时间段与之对应
    }

    //测试toString方法
    @Test
    public void toStringTest() throws IOException {
        ProcessIntervalSet<String> intervals = new ProcessIntervalSet<>();

        assertEquals("",intervals.toString()); //空集合

        intervals.insert(0,5,"A");
        intervals.insert(5,10,"B");
        intervals.insert(10,15,"B");
        assertEquals("Label:A  startTime:0  endTime:5\n" +
                "Label:B  startTime:5  endTime:10\n" +
                "Label:B  startTime:10  endTime:15\n",intervals.toString());
    }

    //测试checkNoPeriod方法
    @Test
    public void NoPeriodTest(){
        ProcessIntervalSet<String> intervals = new ProcessIntervalSet<>();

        assertEquals(true,intervals.checkNoPeriodic(-1));
        assertEquals(false,intervals.checkNoPeriodic(1));

    }
}
