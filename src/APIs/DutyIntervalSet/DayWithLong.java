package APIs.DutyIntervalSet;

import java.text.ParseException;
import java.time.DateTimeException;

public interface DayWithLong {
    /**
     * 计算两个日期之间的日期差距
     *
     * @param day1 时间段的开始时间 格式为xxxx-xx-xx，年月日
     * @param day2 时间段的结束时间，必须要比day1更晚
     * @return 两个日期之间相差的日数，如果day2比day1更早或者其他情况则返回-1
     * @throws DateTimeException 如果字符串格式不正确则抛出异常
     */
    public long getInterval(String day1,String day2) throws DateTimeException;

    /**
     * 计算一个日期加上几天以后的日期
     *
     * @param day 当前日期
     * @param length 需要向后延期多少天，必须要大于等于0
     * @return 当前日期过了length天以后的字符串形式的新日期，如果length小于0或其他异常情况则返回空字符串
     * @throws DateTimeException 如果字符串格式不正确则抛出异常
     */
    public String addDays(String day,long length) throws DateTimeException;
}
