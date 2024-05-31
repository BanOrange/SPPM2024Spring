package APIs.DutyIntervalSet;

import org.junit.Test;

import java.text.ParseException;
import java.time.DateTimeException;

import static org.junit.Assert.assertEquals;

public class DayWithLongTest {
    /**
     * Testing Strategy
     * Tetsing getInterval():
     *  day1:不符合格式;符合格式
     *  day2：不符合格式;符合格式，但是要比day1更早
     *
     * Testing addDays():
     *  day:不符合格式;符合格式
     *  length:<0;=0;>0
     * 测试策略：每种情况至少覆盖一次
     */

    @Test
    public void getIntervalTest() throws DateTimeException {
        DayWithLong DT = new DayWithLongImpl();
        String day1 = "2022-02-23";
        String day2 = "2022-02-26";
        assertEquals(3,DT.getInterval(day1,day2));
        day2 = "2022-02-19";
        assertEquals(-1,DT.getInterval(day1,day2));
    }

    @Test(expected = DateTimeException.class)
    public void getIntervalThrowTest() throws DateTimeException{
        DayWithLong DT = new DayWithLongImpl();
        String day1 = "2022sd-02sd-23";
        String day2 = "20223sd-02-26";
        DT.getInterval(day1,day2);

    }

    @Test
    public void addDaysTest() throws DateTimeException {
        DayWithLong DT = new DayWithLongImpl();
        String day1 = "2022-02-23";
        long length = -1;
        assertEquals("",DT.addDays(day1,length));
        length = 0;
        assertEquals("2022-02-23",DT.addDays(day1,length));
        length = 3;
        assertEquals("2022-02-26",DT.addDays(day1,length));
    }

    @Test(expected = DateTimeException.class)
    public void addDaysThrowsTest() throws DateTimeException {
        DayWithLong DT = new DayWithLongImpl();
        String day1 = "2022sd-02-23";
        long length = 2;
        DT.addDays(day1,length);

    }
}
