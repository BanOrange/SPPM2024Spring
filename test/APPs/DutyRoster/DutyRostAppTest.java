package APPs.DutyRoster;

import APPs.DutyRoster.DutyRostAPP;
import entity.Employee;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DutyRostAppTest {
    /**
     * Testing Strategy
     * Testing setTime():
     * startTime:符合格式;不符合格式
     * endTime:不符合格式；符合格式，晚于或等于开始时间；符合格式，早于开始时间
     * <p>
     * Testing addEmployee():
     * name:重名;不重名
     * <p>
     * Testing addDuty():
     * start:早于startTime；等于startTime；晚于startTime
     * end：>=start;<start
     * name: 存在于员工集合;不存在于员工集合
     * <p>
     * Testing remove():
     * name:不存在于员工集合；存在于员工集合但是没有排班；已经排班
     * <p>
     * Testing full():
     * 排班表：空排班表；未装满的排班表；已经装满的排班表
     * <p>
     * Testing readFile();
     * 文件：空文件；正则语言不符合格式的文件；符合格式的小型文件（人数不超过5且时间不超过3个月）；符合格式的大型文件（人数超过5人且超过三个月）
     *      符合格式但是时间有冲突的文件；
     * <p>
     * Testing getJob(),telephone():
     * name:不存在于员工集合；存在于员工集合
     * <p>
     * Testing showEmployee():
     * 员工集合：空集合；非空集合
     * <p>
     * Testing getFreeTimeRatio():
     * 集合状态:空集合；非空集合，其中不存在空闲时间；非空集合，存在空闲时间
     * Testing getFreeTime()
     * 集合状态:空集合；非空集合，其中不存在空闲时间；非空集合，存在空闲时间
     * Testing random():
     * 员工列表：为空;不为空
     * 集合：为空，不为空
     * Testing toString():
     * 集合:空集合;非空集合
     * <p>
     * 测试策略：每种情况至少测试一次
     */

    //测试setTime
    @Test
    public void setTimeTest() throws DateTimeException {
        DutyRostAPP intervals = new DutyRostAPP();

        assertEquals(true,intervals.setTime("2024-05-01", "2024-05-20"));
        assertEquals(false,intervals.setTime("2024-05-20", "2024-05-01"));
        assertEquals(true,intervals.setTime("2024-05-01", "2024-05-01"));

        assertThrows(DateTimeException.class, () -> {
            assertEquals(false,intervals.setTime("2024-34-456","202d-03-02"));  //不符合格式
        });
    }

    @Test
    public void addEmployeeTest(){
        DutyRostAPP intervals = new DutyRostAPP("2024-05-01", "2024-05-20");
        Employee e1 = new Employee("jack","经理","123-2345-5667");
        Employee e2 = new Employee("jack","保洁","123-2345-5667");    //重名
//        Employee e3 = new Employee("lucy","董事长","123-2345-56d7");  //电话不符合格式,无法通过实体类的checkrep

        intervals.addEmployee(e1);
        assertEquals("jack  经理  123-2345-5667\n",intervals.showEmployee());
        intervals.addEmployee(e2);
        assertEquals("jack  保洁  123-2345-5667\n",intervals.showEmployee());
        intervals.addEmployee("lucy","董事长","123-2345-56d7");
        assertEquals("jack  保洁  123-2345-5667\n",intervals.showEmployee());

    }

    @Test
    public void addDUty() throws IOException {
        DutyRostAPP intervals = new DutyRostAPP("2024-05-01", "2024-05-20");

        intervals.addEmployee("B", "经理", "213-4567-8905");
        intervals.addEmployee("C", "董事长", "213-4567-5905");
        intervals.addEmployee("D", "保洁工", "333-4567-8905");
        intervals.addDuty("2024-05-01", "2024-05-02", "B"); //start = 0
        intervals.addDuty("2024-05-03", "2024-05-03", "C"); //正常情况 start>0 end>=start

        assertThrows(IOException.class, () -> {
            intervals.addDuty("2024-04-28", "2024-05-07", "D"); //start<startTime
        });
        assertThrows(IOException.class, () -> {
            intervals.addDuty("2024-05-07", "2024-05-05", "D"); //end<startTime
        });
        assertThrows(IOException.class, () -> {
            intervals.addDuty("2024-05-07", "2024-05-20", "NoName"); //不存在于员工集合
        });
    }
    @Test
    public void removeTest() throws IOException {
        DutyRostAPP intervals = new DutyRostAPP("2024-05-01", "2024-05-20");

        intervals.addEmployee("B", "经理", "213-4567-8905");
        intervals.addEmployee("C", "董事长", "213-4567-5905");
        intervals.addEmployee("D", "保洁工", "333-4567-8905");

        intervals.remove("E"); //不存在于员工集合
        assertEquals("B  经理  213-4567-8905\n" +
                "C  董事长  213-4567-5905\n" +
                "D  保洁工  333-4567-8905\n",intervals.showEmployee());

        intervals.remove("B");//存在且未排班
        assertEquals("C  董事长  213-4567-5905\n" +
                "D  保洁工  333-4567-8905\n",intervals.showEmployee());

        intervals.addDuty("2024-05-01","2024-05-05","C");
        intervals.addDuty("2024-05-06","2024-05-07","D");

        intervals.remove("D"); //存在且已经排班
        assertEquals("C  董事长  213-4567-5905\n",intervals.showEmployee());
        assertEquals("排班表的起止时间为2024-05-01---2024-05-20\n" +
                "以下是已经安排的时间段\n" +
                "2024-05-01  C  董事长  213-4567-5905\n" +
                "2024-05-02  C  董事长  213-4567-5905\n" +
                "2024-05-03  C  董事长  213-4567-5905\n" +
                "2024-05-04  C  董事长  213-4567-5905\n" +
                "2024-05-05  C  董事长  213-4567-5905\n",intervals.toString());
    }
    @Test
    public void toStringTest() throws IOException {
        DutyRostAPP intervals = new DutyRostAPP("2024-05-01", "2024-05-20");
        assertEquals("排班表的起止时间为2024-05-01---2024-05-20\n" +
                "以下是已经安排的时间段\n", intervals.toString()); //空集合

        intervals.addEmployee("B", "经理", "213-4567-8905");
        intervals.addEmployee("C", "董事长", "213-4567-5905");
        intervals.addEmployee("D", "保洁工", "333-4567-8905");
        intervals.addDuty("2024-05-01", "2024-05-02", "B"); //start = 0
        intervals.addDuty("2024-05-03", "2024-05-03", "C"); //正常情况 start>0 end>=start
        intervals.addDuty("2024-05-04", "2024-05-07", "D");

        assertEquals("排班表的起止时间为2024-05-01---2024-05-20\n" +
                "以下是已经安排的时间段\n" +
                "2024-05-01  B  经理  213-4567-8905\n" +
                "2024-05-02  B  经理  213-4567-8905\n" +
                "2024-05-03  C  董事长  213-4567-5905\n" +
                "2024-05-04  D  保洁工  333-4567-8905\n" +
                "2024-05-05  D  保洁工  333-4567-8905\n" +
                "2024-05-06  D  保洁工  333-4567-8905\n" +
                "2024-05-07  D  保洁工  333-4567-8905\n", intervals.toString());  //非空集合

    }
    @Test
    public void getFreeTimeRatioTest() throws IOException {
        DutyRostAPP intervals = new DutyRostAPP("2024-05-01", "2024-05-21");
        assertEquals(-1, intervals.getFreeTimeRatio(), 0.001);

        intervals.addEmployee("B", "经理", "213-4567-8905");
        intervals.addEmployee("C", "董事长", "213-4567-5905");
        intervals.addEmployee("D", "保洁工", "333-4567-8905");
        intervals.addDuty("2024-05-01", "2024-05-02", "B"); //start = 0
        intervals.addDuty("2024-05-03", "2024-05-03", "C"); //正常情况 start>0 end>=start
        intervals.addDuty("2024-05-04", "2024-05-07", "D");
        assertEquals(0.666, intervals.getFreeTimeRatio(), 0.01);   //存在空闲时间


        intervals.addEmployee("E", "程序员", "213-4237-8905");
        intervals.addDuty("2024-05-08", "2024-05-21", "E");
        assertEquals(0, intervals.getFreeTimeRatio(), 0.001); //不存在空闲时间
    }
    @Test
    public void getFreeTimeTest() throws IOException {
        DutyRostAPP intervals = new DutyRostAPP("2024-05-01", "2024-05-21");
        assertEquals("以下时间还是空闲的哦:" + "\n" +
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
                "2024-05-21\n", intervals.getFreeTime());

        intervals.addEmployee("B", "经理", "213-4567-8905");
        intervals.addEmployee("C", "董事长", "213-4567-5905");
        intervals.addEmployee("D", "保洁工", "333-4567-8905");
        intervals.addDuty("2024-05-01", "2024-05-02", "B"); //start = 0
        intervals.addDuty("2024-05-03", "2024-05-03", "C"); //正常情况 start>0 end>=start
        intervals.addDuty("2024-05-04", "2024-05-07", "D");
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
                "2024-05-21\n", intervals.getFreeTime());

        intervals.addEmployee("E", "程序员", "213-4237-8905");
        intervals.addDuty("2024-05-08", "2024-05-21", "E");
        assertEquals("所有时间都已经安排完毕!", intervals.getFreeTime()); //不存在空闲时间

    }
    @Test
    public void randomTest() throws ParseException {
        DutyRostAPP intervals = new DutyRostAPP("2024-05-01", "2024-05-05");
        List<Employee> employees = new ArrayList<>();

        intervals.random();
        assertEquals("排班表的起止时间为2024-05-01---2024-05-05\n" +
                "以下是已经安排的时间段\n", intervals.toString());

        Employee e1 = new Employee("zhangsan", "jingli", "133-2222-2222");
        Employee e2 = new Employee("lisi", "yuangong", "123-2234-5667");
        Employee e3 = new Employee("wangwu", "dongshizhang", "123-2344-5667");
        intervals.addEmployee(e1);
        intervals.addEmployee(e2);
        intervals.addEmployee(e3);

        intervals.random();
        System.out.println(intervals.toString());
        assertEquals(0, intervals.getFreeTimeRatio(), 0.001);


    }

    //getJob，telephone和showEmployee方法一起测试
    @Test
    public void getAndShowTest(){
        DutyRostAPP intervals = new DutyRostAPP("2024-05-01", "2024-05-20");
        assertEquals("",intervals.showEmployee());

        intervals.addEmployee("Jack", "经理", "213-4567-8905");
        intervals.addEmployee("luck", "董事长", "213-4567-5905");
        intervals.addEmployee("David", "保洁工", "333-4567-8905");

        assertEquals("Jack  经理  213-4567-8905\n" +
                "luck  董事长  213-4567-5905\n" +
                "David  保洁工  333-4567-8905\n",intervals.showEmployee());

        assertEquals("",intervals.getJob("NoName"));
        assertEquals("经理",intervals.getJob("Jack"));

        assertEquals("",intervals.telephone("NoName"));
        assertEquals("213-4567-8905",intervals.telephone("Jack"));

    }

    @Test
    public void fullTest() throws IOException {
        DutyRostAPP intervals = new DutyRostAPP("2024-05-01", "2024-05-21");
        assertEquals(false,intervals.full());

        intervals.addEmployee("B", "经理", "213-4567-8905");
        intervals.addEmployee("C", "董事长", "213-4567-5905");
        intervals.addEmployee("D", "保洁工", "333-4567-8905");
        intervals.addDuty("2024-05-01", "2024-05-02", "B"); //start = 0
        intervals.addDuty("2024-05-03", "2024-05-03", "C"); //正常情况 start>0 end>=start
        intervals.addDuty("2024-05-04", "2024-05-07", "D");
        assertEquals(false,intervals.full());


        intervals.addEmployee("E", "程序员", "213-4237-8905");
        intervals.addDuty("2024-05-08", "2024-05-21", "E");
        assertEquals(true,intervals.full());
    }

    //测试从文件中读取的功能
    @Test
    public void readFile() throws ParseException, IOException {
        DutyRostAPP intervals = new DutyRostAPP();
        DutyRostAPP intervalsForHuge = new DutyRostAPP();

        assertThrows(IOException.class, () -> {
            intervals.readFile("./empty.txt");
        });

        assertThrows(IOException.class, () -> {
            intervals.readFile("./notFormat.txt");
        });
        assertThrows(IOException.class, () -> {
            intervals.readFile("./wrongTime.txt");
        });

        intervals.readFile(".\\test\\APPs\\DutyRoster\\miniTest.txt");
        System.out.println(intervals.toString());
        assertEquals("排班表的起止时间为2021-01-10---2021-03-06\n" +
                "以下是已经安排的时间段\n" +
                "2021-01-10  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-11  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-12  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-13  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-14  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-15  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-16  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-17  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-18  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-19  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-20  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-21  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-22  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-23  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-24  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-25  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-26  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-27  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-28  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-29  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-30  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-31  ZhangSan  Manger  139-0451-0000\n" +
                "2021-02-01  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-02  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-03  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-04  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-05  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-06  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-07  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-08  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-09  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-10  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-11  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-12  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-13  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-14  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-15  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-16  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-17  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-18  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-19  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-20  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-21  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-22  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-23  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-24  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-25  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-26  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-27  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-28  LiSi  Secretary  151-0101-0000\n" +
                "2021-03-01  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-02  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-03  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-04  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-05  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-06  WangWu  Associate Dean  177-2021-0301\n",intervals.toString());

        intervalsForHuge.readFile(".\\test\\APPs\\DutyRoster\\HugeTest.txt");
        assertEquals("排班表的起止时间为2021-01-10---2022-01-09\n" +
                "以下是已经安排的时间段\n" +
                "2021-01-10  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-11  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-12  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-13  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-14  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-15  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-16  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-17  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-18  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-19  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-20  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-21  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-22  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-23  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-24  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-25  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-26  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-27  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-28  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-29  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-30  ZhangSan  Manger  139-0451-0000\n" +
                "2021-01-31  ZhangSan  Manger  139-0451-0000\n" +
                "2021-02-01  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-02  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-03  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-04  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-05  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-06  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-07  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-08  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-09  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-10  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-11  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-12  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-13  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-14  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-15  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-16  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-17  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-18  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-19  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-20  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-21  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-22  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-23  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-24  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-25  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-26  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-27  LiSi  Secretary  151-0101-0000\n" +
                "2021-02-28  LiSi  Secretary  151-0101-0000\n" +
                "2021-03-01  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-02  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-03  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-04  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-05  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-06  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-07  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-08  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-09  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-10  WangWu  Associate Dean  177-2021-0301\n" +
                "2021-03-11  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-12  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-13  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-14  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-15  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-16  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-17  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-18  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-19  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-20  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-21  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-22  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-23  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-24  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-25  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-26  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-27  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-28  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-29  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-30  WangZhongjie  Teacher  465-3545-3456\n" +
                "2021-03-31  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-01  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-02  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-03  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-04  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-05  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-06  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-07  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-08  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-09  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-10  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-11  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-12  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-13  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-14  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-15  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-16  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-17  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-18  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-19  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-20  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-21  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-22  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-23  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-24  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-25  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-26  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-27  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-28  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-29  QuanJunkang  student  123-4567-8975\n" +
                "2021-04-30  QuanJunkang  student  123-4567-8975\n" +
                "2021-05-01  QuanJunkang  student  123-4567-8975\n" +
                "2021-05-02  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-03  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-04  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-05  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-06  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-07  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-08  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-09  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-10  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-11  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-12  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-13  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-14  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-15  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-16  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-17  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-18  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-19  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-20  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-21  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-22  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-23  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-24  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-25  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-26  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-27  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-28  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-29  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-30  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-05-31  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-01  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-02  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-03  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-04  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-05  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-06  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-07  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-08  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-09  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-10  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-11  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-12  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-13  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-14  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-15  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-16  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-17  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-18  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-19  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-20  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-21  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-22  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-23  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-24  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-25  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-26  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-27  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-28  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-29  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-06-30  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-01  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-02  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-03  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-04  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-05  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-06  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-07  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-08  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-09  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-10  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-11  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-12  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-13  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-14  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-15  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-16  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-17  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-18  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-19  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-20  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-21  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-22  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-23  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-24  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-25  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-26  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-27  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-28  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-29  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-30  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-07-31  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-01  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-02  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-03  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-04  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-05  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-06  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-07  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-08  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-09  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-10  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-11  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-12  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-13  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-14  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-15  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-16  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-17  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-18  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-19  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-20  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-21  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-22  Todd  GameDeveloper  234-4567-3455\n" +
                "2021-08-23  LiuMingyi  TA  234-4556-3422\n" +
                "2021-08-24  LiuMingyi  TA  234-4556-3422\n" +
                "2021-08-25  LiuMingyi  TA  234-4556-3422\n" +
                "2021-08-26  LiuMingyi  TA  234-4556-3422\n" +
                "2021-08-27  LiuMingyi  TA  234-4556-3422\n" +
                "2021-08-28  LiuMingyi  TA  234-4556-3422\n" +
                "2021-08-29  LiuMingyi  TA  234-4556-3422\n" +
                "2021-08-30  LiuMingyi  TA  234-4556-3422\n" +
                "2021-08-31  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-01  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-02  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-03  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-04  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-05  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-06  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-07  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-08  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-09  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-10  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-11  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-12  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-13  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-14  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-15  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-16  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-17  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-18  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-19  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-20  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-21  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-22  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-23  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-24  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-25  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-26  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-27  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-28  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-29  LiuMingyi  TA  234-4556-3422\n" +
                "2021-09-30  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-01  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-02  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-03  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-04  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-05  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-06  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-07  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-08  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-09  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-10  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-11  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-12  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-13  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-14  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-15  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-16  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-17  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-18  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-19  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-20  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-21  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-22  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-23  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-24  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-25  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-26  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-27  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-28  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-29  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-30  LiuMingyi  TA  234-4556-3422\n" +
                "2021-10-31  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-01  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-02  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-03  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-04  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-05  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-06  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-07  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-08  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-09  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-10  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-11  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-12  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-13  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-14  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-15  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-16  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-17  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-18  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-19  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-20  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-21  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-22  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-23  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-24  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-25  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-26  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-27  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-28  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-29  LiuMingyi  TA  234-4556-3422\n" +
                "2021-11-30  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-01  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-02  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-03  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-04  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-05  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-06  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-07  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-08  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-09  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-10  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-11  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-12  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-13  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-14  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-15  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-16  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-17  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-18  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-19  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-20  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-21  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-22  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-23  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-24  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-25  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-26  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-27  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-28  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-29  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-30  LiuMingyi  TA  234-4556-3422\n" +
                "2021-12-31  LiuMingyi  TA  234-4556-3422\n" +
                "2022-01-01  LiuMingyi  TA  234-4556-3422\n" +
                "2022-01-02  LiuMingyi  TA  234-4556-3422\n" +
                "2022-01-03  LiuMingyi  TA  234-4556-3422\n" +
                "2022-01-04  LiuMingyi  TA  234-4556-3422\n" +
                "2022-01-05  LiuMingyi  TA  234-4556-3422\n" +
                "2022-01-06  LiuMingyi  TA  234-4556-3422\n" +
                "2022-01-07  LiuMingyi  TA  234-4556-3422\n" +
                "2022-01-08  LiuMingyi  TA  234-4556-3422\n" +
                "2022-01-09  LiuMingyi  TA  234-4556-3422\n",intervalsForHuge.toString());
    }
}
