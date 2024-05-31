package entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CourseTest {
    /**
     * Testing Strategy
     * Testing getXXX():
     *  构造之后进行测试
     *  同时测试所有get方法
     *
     *  测试策略:每种get方法都至少要测试一次
     */

    @Test
    public void getXXXTest(){
        Course course = new Course(14,"软件构造","Mr.Wang","ZhengXin");

        assertEquals(14,course.getID());
        assertEquals("软件构造",course.getCourseName());
        assertEquals("ZhengXin",course.getLocation());
        assertEquals("Mr.Wang",course.getTeacherName());
    }
}
