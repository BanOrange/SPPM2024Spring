package APIs.DutyIntervalSet;

import APIs.DutyIntervalSet.DutyIntervalSet;
import entity.Employee;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DutyIntervalSetTest {
    /**
     * Testing Strategy
     * <p>
     * Testing insert():
     *   start:<0;=0;>0;
     *   end:<start;>=start;
     *   label:重复；非重复
     *   时间段：重叠；非重叠
     * Testing labels():
     *   IntervalSet:空集合;非空集合
     *
     * Testing remove():
     *   label:存在于集合中；不存在于集合
     *
     * Testing start():
     *   label:存在于集合之中;不存在于集合之中
     *
     * Testing end():
     *   label:存在于集合之中;不存在于集合之中
     *
     * Testing toString():
     *   集合:空集合;非空集合
     *
     * Testing checkNoBlank():
     *  集合：有空白时间，无空白时间
     *
     * Testing checkNoPeriod():
     *  集合：有重复时间；无重复时间
     *
     * Testing getStartTime(),getEndTime()
     *  常规数据
     * Testing getFreeTimeRatio():
     *  集合状态:空集合；非空集合，其中不存在空闲时间；非空集合，存在空闲时间
     * Testing getFreeTime()
     *  集合状态:空集合；非空集合，其中不存在空闲时间；非空集合，存在空闲时间
     * Testing random():
     *  员工列表：为空;不为空
     *  集合：为空，不为空
     *
     * 测试策略：每一种情况至少覆盖一次
     */

    //测试insert方法
    @Test
    public void insertTest() throws IOException {
        //由于是变值方法，因此每一步都需要检验是否正确
        DutyIntervalSet<String> intervals = new DutyIntervalSet<String>("2024-05-01","2024-05-20");
        intervals.insert(1,5,"A");
        assertEquals(1,intervals.labels().size());
        assertEquals(1,intervals.start("A"));
        assertEquals(5,intervals.end("A"));

        assertThrows(IOException.class, () -> {
            intervals.insert(3,5,"A");  //产生重叠，因此插入失败
        });

        assertEquals(1,intervals.start("A"));
        assertEquals(5,intervals.end("A"));


        assertThrows(IOException.class, () -> {
            intervals.insert(5,9,"A");  //标签重复 插入失败
        });
        assertEquals(1,intervals.labels().size());
        assertEquals(1,intervals.start("A"));
        assertEquals(5,intervals.end("A"));

        assertThrows(IOException.class, () -> {
            intervals.insert(9,5,"A");  //end<start 插入失败
        });

        assertEquals(1,intervals.labels().size());
        assertEquals(1,intervals.start("A"));
        assertEquals(5,intervals.end("A"));

        assertThrows(IOException.class, () -> {
            intervals.insert(-1,2,"A");  //start<0,插入失败
        });

        assertEquals(1,intervals.labels().size());
        assertEquals(1,intervals.start("A"));
        assertEquals(5,intervals.end("A"));


        intervals.insert(6,9,"B");  //正常插入且无重叠
        assertEquals(2,intervals.labels().size());
        assertEquals(1,intervals.start("A"));
        assertEquals(5,intervals.end("A"));
        assertEquals(6,intervals.start("B"));
        assertEquals(9,intervals.end("B"));
    }

    //测试labels方法
    @Test
    public void labelsTest() throws IOException {
        DutyIntervalSet<String> intervals = new DutyIntervalSet<String>("2024-05-01","2024-05-20");
        assertEquals(0,intervals.labels().size()); //空集合

        intervals.insert(1,5,"A");
        assertEquals(1,intervals.labels().size());
        assertEquals(true,intervals.labels().contains("A"));
    }

    //测试remove方法
    @Test
    public void removeTest() throws IOException {
        DutyIntervalSet<String> intervals = new DutyIntervalSet<String>("2024-05-01","2024-05-20");
        assertEquals(false,intervals.remove("A")); //空集合

        intervals.insert(1,5,"A");
        assertEquals(1,intervals.labels().size());
        assertEquals(true,intervals.remove("A"));  //存在于集合之中
        assertEquals(0,intervals.labels().size());
    }

    //测试start方法
    @Test
    public void startTest() throws IOException {
        DutyIntervalSet<String> intervals = new DutyIntervalSet<String>("2024-05-01","2024-05-20");
        assertEquals(-1,intervals.start("A")); //空集合

        intervals.insert(1,5,"A");
        assertEquals(1,intervals.labels().size());
        assertEquals(1,intervals.start("A"));  //存在于集合之中
        assertEquals(1,intervals.labels().size());
    }

    //测试end方法
    @Test
    public void endTest() throws IOException {
        DutyIntervalSet<String> intervals = new DutyIntervalSet<String>("2024-05-01","2024-05-20");
        assertEquals(-1,intervals.end("A")); //空集合

        intervals.insert(1,5,"A");
        assertEquals(1,intervals.labels().size());
        assertEquals(5,intervals.end("A"));  //存在于集合之中
        assertEquals(1,intervals.labels().size());
    }

    //测试toString方法
    @Test
    public void toStringTest() throws IOException {
        DutyIntervalSet<String> intervals = new DutyIntervalSet<String>("2024-05-01","2024-05-20");

        assertEquals("排班表的起止时间为2024-05-01---2024-05-20\n"+
                        "以下是已经安排的时间段\n",intervals.toString()); //空集合

        intervals.insert(0,1,"B"); //start = 0
        intervals.insert(2,2,"C"); //正常情况 start>0 end>=start
        intervals.insert(3,6,"D");

        assertEquals("排班表的起止时间为2024-05-01---2024-05-20\n" +
                "以下是已经安排的时间段\n" +
                "2024-05-01  B\n" +
                "2024-05-02  B\n" +
                "2024-05-03  C\n" +
                "2024-05-04  D\n" +
                "2024-05-05  D\n" +
                "2024-05-06  D\n" +
                "2024-05-07  D\n",intervals.toString());  //非空集合
    }

    //测试checkNoBlank方法
    @Test
    public void checkNoBlankTest() throws IOException {
        DutyIntervalSet<String> intervals = new DutyIntervalSet<String>("2024-05-01","2024-05-20");

        intervals.insert(0,1,"B"); //start = 0
        intervals.insert(2,2,"C"); //正常情况 start>0 end>=start
        intervals.insert(3,6,"D");

        assertEquals(true,intervals.checkNoBlank(0,6));
        assertEquals(false,intervals.checkNoBlank(0,9));
    }

    //测试checkNoPeriod方法
    @Test
    public void checkNoPeriodTest() throws IOException {
        DutyIntervalSet<String> intervals = new DutyIntervalSet<String>("2024-05-01","2024-05-20");

        intervals.insert(0,1,"B"); //start = 0
        intervals.insert(2,2,"C"); //正常情况 start>0 end>=start
        intervals.insert(3,6,"D");
        assertEquals(true,intervals.checkNoPeriodic(0));
    }

    @Test
    public void getStartOrEndTimeTest(){
        DutyIntervalSet<String> intervals = new DutyIntervalSet<String>("2024-05-01","2024-05-20");
        assertEquals("2024-05-01",intervals.getStartTime());
        assertEquals("2024-05-20",intervals.getEndTime());
    }

    @Test
    public void getFreeTimeRatioTest() throws IOException {
        DutyIntervalSet<String> intervals = new DutyIntervalSet<String>("2024-05-01","2024-05-21");
        assertEquals(0,intervals.getFreeTimeRatio(),0.00001);

        intervals.insert(0,1,"B"); //start = 0
        intervals.insert(2,2,"C"); //正常情况 start>0 end>=start
        intervals.insert(3,6,"D");
        assertEquals(0.666,intervals.getFreeTimeRatio(),0.01);   //存在空闲时间

        intervals.insert(7,20,"E");
        assertEquals(0,intervals.getFreeTimeRatio(),0.00001); //不存在空闲时间
    }

    @Test
    public void getFreeTimeTest() throws IOException {
        DutyIntervalSet<String> intervals = new DutyIntervalSet<String>("2024-05-01","2024-05-21");
        assertEquals("以下时间还是空闲的哦:"+"\n"+
                "2024-05-01\n" +
                "2024-05-02\n" +
                "2024-05-03\n" +
                "2024-05-04\n" +
                "2024-05-05\n" +
                "2024-05-06\n" +
                "2024-05-07\n" +
                "2024-05-08\n" +
                "2024-05-09\n" +
                "2024-05-10\n" +
                "2024-05-11\n" +
                "2024-05-12\n" +
                "2024-05-13\n" +
                "2024-05-14\n" +
                "2024-05-15\n" +
                "2024-05-16\n" +
                "2024-05-17\n" +
                "2024-05-18\n" +
                "2024-05-19\n" +
                "2024-05-20\n" +
                "2024-05-21\n",intervals.getFreeTime());

        intervals.insert(0,1,"B"); //start = 0
        intervals.insert(2,2,"C"); //正常情况 start>0 end>=start
        intervals.insert(3,6,"D");
        assertEquals("以下时间还是空闲的哦:\n" +
                "2024-05-08\n" +
                "2024-05-09\n" +
                "2024-05-10\n" +
                "2024-05-11\n" +
                "2024-05-12\n" +
                "2024-05-13\n" +
                "2024-05-14\n" +
                "2024-05-15\n" +
                "2024-05-16\n" +
                "2024-05-17\n" +
                "2024-05-18\n" +
                "2024-05-19\n" +
                "2024-05-20\n" +
                "2024-05-21\n",intervals.getFreeTime());

        intervals.insert(7,20,"E");
        assertEquals("所有时间都已经安排完毕!",intervals.getFreeTime()); //不存在空闲时间

    }

    @Test
    public void randomTest() throws ParseException {
        DutyIntervalSet<Employee> intervals = new DutyIntervalSet<Employee>("2024-05-01","2024-05-05");
        List<Employee> employees = new ArrayList<>();

        assertEquals(false,intervals.random(employees));

        Employee e1 = new Employee("zhangsan","jingli","133-2222-2222");
        Employee e2 = new Employee("lisi","yuangong","123-2234-5667");
        Employee e3 = new Employee("wangwu","dongshizhang","123-2344-5667");
        employees.add(e1);
        employees.add(e2);
        employees.add(e3);

        assertEquals(true,intervals.random(employees));
        System.out.println(intervals.toString());
        assertEquals(0,intervals.getFreeTimeRatio(),0.001);


    }

}
