package entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProcessTest {
    /**
     * Testing Strategy
     * Testing getXXX():
     *  构造之后进行测试
     *  同时测试所有get方法
     * Testing toString()：
     *  构造之后进行测试
     * 测试策略：每种情况至少测试一次
     */

    @Test
    public void getXXXTest(){
        Process process = new Process(13,"开机",2,5);

        assertEquals(13,process.getID());
        assertEquals("开机",process.getName());
        assertEquals(2,process.getStartTime());
        assertEquals(5,process.getEndTime());
    }

    @Test
    public void toStringTest(){
        Process process = new Process(13,"开机",2,5);
        assertEquals("开机  13  2  5",process.toString());
    }
}
