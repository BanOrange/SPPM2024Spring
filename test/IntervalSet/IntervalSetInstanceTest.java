package IntervalSet;

import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class IntervalSetInstanceTest {
    /**
     * Testing Strategy
     * <p>
     * Testing insert():
     *   start:<0;=0;>0;
     *   end:<start;>=start;
     *   label:重复；非重复
     *
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
     * 测试策略：每一种情况至少覆盖一次
     */

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    //测试正常情况下的insert()方法
    @Test
    public void insertTest() throws IOException {
        IntervalSet<String> set = new CommonIntervalSet<String>();
        assertThrows(IOException.class, () -> {
            set.insert(-1,1,"D");
        });   //异常情况，应该抛出异常

        assertThrows(IOException.class, () -> {
            set.insert(3,1,"D");
        });   //异常情况，应该抛出异常

        set.insert(0,1,"B"); //start = 0
        set.insert(1,2,"C"); //正常情况 start>0 end>=start
        set.insert(2,6,"D");

    }

    @Test(expected = IOException.class)
    public void insertExTest() throws IOException {
        IntervalSet<String> set = new CommonIntervalSet<String>();
        set.insert((long) -1.1,0,"A");   //start<0的情况
        set.insert(4,-1,"E"); //end<start
        set.insert(1,4,"C"); //标签重复的情况
    }

    //测试labels()方法
    @Test
    public void labelsTest() throws IOException {
        IntervalSet<String> set = new CommonIntervalSet<String>();
        Set labels1 = set.labels();
        assertEquals(true,labels1.isEmpty());  //空集合情况

        set.insert(1,2,"A");   //集合不为空
        set.insert(3,4,"B");
        set.insert(5,7,"C");
        Set labels2 = set.labels();
        assertEquals(3,labels2.size());
        assertEquals(true,labels2.contains("A"));
        assertEquals(true,labels2.contains("B"));
        assertEquals(true,labels2.contains("C"));
    }

    //测试remove（）方法
    @Test
    public void removeTest() throws IOException {
        IntervalSet<String> set = new CommonIntervalSet<String>();
        set.insert(1,2,"A");   //集合不为空
        set.insert(3,4,"B");
        set.insert(5,7,"C");

        assertEquals(true,set.remove("A"));         //删除一个集合中存在的标签及其对应的时间段
        Set labels1 = set.labels();
        assertEquals(2,labels1.size());
        assertEquals(true,labels1.contains("B"));
        assertEquals(true,labels1.contains("C"));

        assertEquals(false,set.remove("D"));     //删除一个不存在于集合当中的标签及其对应的时间段
        Set labels2 = set.labels();
        assertEquals(2,labels2.size());
        assertEquals(true,labels2.contains("B"));
        assertEquals(true,labels2.contains("C"));
    }

    //测试start（）方法
    @Test
    public void start() throws IOException {
        IntervalSet<String> set = new CommonIntervalSet<String>();
        set.insert(1,2,"A");   //集合不为空
        set.insert(3,4,"B");
        set.insert(5,7,"C");

        long number1 = 1;    //labels存在于集合之中
        long number2 = 3;
        long number3 = 5;
        assertEquals(number1,set.start("A"));
        assertEquals(number2,set.start("B"));
        assertEquals(number3,set.start("C"));

        long number =-1;  //labels不存在于集合之中
        assertEquals(number,set.start("D"));
        assertEquals(number,set.start("E"));
        assertEquals(number,set.start("F"));
    }

    //测试end（）方法
    @Test
    public void end() throws IOException {
        IntervalSet<String> set = new CommonIntervalSet<String>();
        set.insert(1,2,"A");   //集合不为空
        set.insert(3,4,"B");
        set.insert(5,7,"C");

        long number1 = 2;    //labels存在于集合之中
        long number2 = 4;
        long number3 = 7;
        assertEquals(number1,set.end("A"));
        assertEquals(number2,set.end("B"));
        assertEquals(number3,set.end("C"));

        long number =-1;  //labels不存在于集合之中
        assertEquals(number,set.end("D"));
        assertEquals(number,set.end("E"));
        assertEquals(number,set.end("F"));
    }
}
