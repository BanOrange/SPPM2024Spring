package APIs.DutyIntervalSet;

import SpecialSet.inter.NoBlankIntervalSet;
import SpecialSet.inter.NonOverlapIntervalSet;
import SpecialSet.inter.NonPeriodicIntervalSet;

import java.text.ParseException;
import java.time.DateTimeException;
import java.util.List;

public interface IDutyIntervalSet<L> extends NonOverlapIntervalSet<L>, NoBlankIntervalSet<L>, NonPeriodicIntervalSet<L>{
    /**
     * 得到当前排班表的空闲时间比例
     *
     * @return 以0-1之间的小数表示当前排班表的空闲时间比例
     */
    public double getFreeTimeRatio();

    /**
     * 获取并展示当前排班表还有哪些日期没有安排
     *
     * @return 返回一条包含所有为安排日期的字符串
     */
    public String getFreeTime();

    /**
     * 随机安排当前存在的员工来排班
     *
     * @param employees 员工列表，不能为空
     * @return 如果安排成功则返回true，安排失败则返回false
     * @throws DateTimeException 当前集合的时间格式存在问题，需要修正
     */
    public boolean random(List<L> employees) throws DateTimeException;
}
