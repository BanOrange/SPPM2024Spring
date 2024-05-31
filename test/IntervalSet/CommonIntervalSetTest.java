package IntervalSet;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CommonIntervalSetTest {
    /**
     * Testing Strategy for CommonIntervalSet
     * Testing toString():
     * Set:空集合;非空集合
     *
     * 测试策略：每一种情况至少覆盖一次
     */

    @Test
    public void toStringSetTest() throws IOException {
        IntervalSet<String> set = new CommonIntervalSet<String>();
        set.insert(0,1,"B"); //start = 0
        set.insert(1,2,"C"); //正常情况 start>0 end>=start
        set.insert(2,6,"D");

        assertEquals("Label:B  startTime:0  endTime:1\n" +
                "Label:C  startTime:1  endTime:2\n" +
                "Label:D  startTime:2  endTime:6\n",set.toString());
        System.out.println(set.toString());
    }

    /**
     * Testing Strategy for interval
     * Testing interval():
     * start:<end
     *
     * Testing getXXX()
     * Testing toString():
     */

    //测试构造方法和getter
    @Test
    public void intervalTest(){
        interval<String> interval = new interval<String>("A",10,15);

        assertEquals(10,interval.getStart());
        assertEquals(15,interval.getEnd());
        assertEquals("A",interval.getLabel());

    }

    //测试toString方法
    @Test
    public void toStringTest(){
        interval<String> interval = new interval<String>("A",10,15);

        assertEquals("Label:A  startTime:10  endTime:15\n",interval.toString());
    }
}
