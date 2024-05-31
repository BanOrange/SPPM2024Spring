package APPs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PaeserForDutyTest {
    /**
     * Testing Strategy
     * Testing threeKind()
     * 字符串：不符合正则表达式；符合正则表达式的六种情况
     *
     * 测试策略：所有可能至少测试一次
     */

    @Test
    public void threeKindTest(){
        PaeserForDuty paeser = new PaeserForDuty();
        List<String> kindList = new ArrayList<>();

        kindList = paeser.threeKind("shdisadhashdjakhs");   //不符合格式
        assertEquals(0,kindList.size());

        String kind1 = "Period{period}Roster{roster}Employee{employee}";   //六种情况
        String kind2 = "Period{period}Employee{employee}Roster{roster}";
        String kind3 = "Roster{roster}Employee{employee}Period{period}";
        String kind4 = "Roster{roster}Period{period}Employee{employee}";
        String kind5 = "Employee{employee}Roster{roster}Period{period}";
        String kind6 = "Employee{employee}Period{period}Roster{roster}";

        kindList = paeser.threeKind(kind1);
        assertEquals("period",kindList.get(0));
        assertEquals("employee",kindList.get(1));
        assertEquals("roster",kindList.get(2));

        kindList = paeser.threeKind(kind2);
        assertEquals("period",kindList.get(0));
        assertEquals("employee",kindList.get(1));
        assertEquals("roster",kindList.get(2));

        kindList = paeser.threeKind(kind3);
        assertEquals("period",kindList.get(0));
        assertEquals("employee",kindList.get(1));
        assertEquals("roster",kindList.get(2));

        kindList = paeser.threeKind(kind4);
        assertEquals("period",kindList.get(0));
        assertEquals("employee",kindList.get(1));
        assertEquals("roster",kindList.get(2));

        kindList = paeser.threeKind(kind5);
        assertEquals("period",kindList.get(0));
        assertEquals("employee",kindList.get(1));
        assertEquals("roster",kindList.get(2));

        kindList = paeser.threeKind(kind6);
        assertEquals("period",kindList.get(0));
        assertEquals("employee",kindList.get(1));
        assertEquals("roster",kindList.get(2));
    }
}
