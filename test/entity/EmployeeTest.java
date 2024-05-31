package entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmployeeTest {
    /**
     * Testing Strategy
     * Testing getXXX():
     *  构造之后进行测试
     *  同时测试所有get方法
     *
     *  测试策略：每种get方法都要测试至少一次
     */

    @Test
    public void getXXXTest(){
        Employee employee = new Employee("Biden","president","186-1145-1455");

        assertEquals("Biden",employee.getName());
        assertEquals("president",employee.getJob());
        assertEquals("186-1145-1455",employee.getTelephone());
    }
}
