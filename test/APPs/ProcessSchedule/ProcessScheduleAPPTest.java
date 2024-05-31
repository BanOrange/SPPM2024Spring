package APPs.ProcessSchedule;

import entity.Process;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ProcessScheduleAPPTest {
    /**
     * Testing Strategy
     * Testing addProcess():
     *   process:重复ID;ID不重复
     * Testing random():
     *  process:没有进程；存在进程
     * Testing optimize():
     *  process:没有进程；存在进程
     * Testing showProcess()
     *  process:没有进程；存在进程
     * Testing display():
     *  time:超过最晚的进程；最早结束的进程和最晚结束的进程之间；所有进程之前；<0
     *  process：存在进程；不存在进程
     *  排序：未排序；已经按优化排序完
     * Testing toString()
     *  process：存在进程；不存在进程
     *  排序：未排序；排序完成
     *
     * 测试策略：每种可能至少测试一次
     */

    @Test
    public void addProcessTest() throws IOException {
        ProcessScheduleAPP intervals = new ProcessScheduleAPP();
        Process p1 = new Process(1,"h1",12,14);
        Process p2 = new Process(2,"h2",4,6);
        Process p3 = new Process(3,"h3",1,3);
        Process p4 = new Process(2,"h4",7,8);

        intervals.addProcess(p1);  //正常加入
        intervals.addProcess(p2);
        intervals.addProcess(p3);

        assertThrows(IOException.class, () -> {
            intervals.addProcess(p4); //ID重复
        });
    }

    @Test
    public void randomTest() throws IOException {
        ProcessScheduleAPP intervals = new ProcessScheduleAPP();
        Process p1 = new Process(1,"h1",12,14);
        Process p2 = new Process(2,"h2",4,6);
        Process p3 = new Process(3,"h3",1,3);

        intervals.addProcess(p1);  //正常加入
        intervals.addProcess(p2);
        intervals.addProcess(p3);
        assertEquals(true,intervals.random());
    }

    @Test
    public void optimize() throws IOException {
        ProcessScheduleAPP intervals1 = new ProcessScheduleAPP();
        assertEquals("ID\t进程名称\t开始时间\t结束时间\t\n",intervals1.toString());

        Process p1 = new Process(1,"h1",12,14);
        Process p2 = new Process(2,"h2",4,6);
        Process p3 = new Process(3,"h3",1,3);
        Process p5 = new Process(4,"h4",20,21);

        intervals1.addProcess(p1);  //正常加入
        intervals1.addProcess(p2);
        intervals1.addProcess(p3);
        intervals1.addProcess(p5);
        intervals1.optimize();
        assertEquals("ID\t进程名称\t开始时间\t结束时间\t\n" +
                "1\th1\t5\t17\n" +
                "2\th2\t1\t5\n" +
                "3\th3\t0\t1\n" +
                "4\th4\t17\t37\n",intervals1.toString());
    }

    @Test
    public void showProcessTest() throws IOException {
        ProcessScheduleAPP intervals = new ProcessScheduleAPP();
        assertEquals("ID\t名称\t最短执行时间\t最长执行时间\t\n",intervals.showProcess());

        Process p1 = new Process(1,"h1",12,14);
        Process p2 = new Process(2,"h2",4,6);
        Process p3 = new Process(3,"h3",1,3);
        Process p4 = new Process(4,"h4",20,21);

        intervals.addProcess(p1);  //正常加入
        intervals.addProcess(p2);
        intervals.addProcess(p3);
        intervals.addProcess(p4);

        assertEquals("ID\t名称\t最短执行时间\t最长执行时间\t\n" +
                "1\th1\t12\t14\n" +
                "2\th2\t4\t6\n" +
                "3\th3\t1\t3\n" +
                "4\th4\t20\t21\n",intervals.showProcess());

    }

    @Test
    public void displayTest() throws IOException {
        ProcessScheduleAPP intervals = new ProcessScheduleAPP();
        assertEquals("ID\t进程名称\t开始时间\t结束时间\t\n" +
                "当前执行的进程\n",intervals.display(20));

        Process p1 = new Process(1,"h1",12,14);
        Process p2 = new Process(2,"h2",4,6);
        Process p3 = new Process(3,"h3",1,3);
        Process p4 = new Process(4,"h4",20,21);

        intervals.addProcess(p1);  //正常加入
        intervals.addProcess(p2);
        intervals.addProcess(p3);
        intervals.addProcess(p4);

        assertEquals("ID\t进程名称\t开始时间\t结束时间\t\n" +
                "当前执行的进程\n",intervals.display(20));         //未进行排序

        intervals.optimize();
        assertEquals("ID\t进程名称\t开始时间\t结束时间\t\n" +
                "2\th2\t1\t5\n" +
                 "3\th3\t0\t1\n"+
                "当前执行的进程\n" +
                "1\th1\t5\t17\n",intervals.display(15));


        assertEquals("ID\t进程名称\t开始时间\t结束时间\t\n" +
                "3\th3\t0\t1\n" +
                "当前执行的进程\n" +
                "2\th2\t1\t5\n",intervals.display(4));
    }
}
