package APIs.CourseIntervalSet;

import IntervalSet.MultiIntervalSet;

public interface ICourseIntervalSet<L> extends MultiIntervalSet<L> {
    /**
     * 为一个时间段设置重复周期
     *
     * @param label 时间段对应的标签
     * @param count 时间段是该标签的第几个时间段
     * @param period 重复周期的长度,如果为-1则视为删除该时间段的重复周期
     * @return 如果设置成功则返回true，反之返回false
     */
    public boolean setPeriod(L label,int count,long period);

    /**
     * 得到一个时间段的重复周期
     *
     * @param label 时间段的标签
     * @param count 时间段是该标签的第几个时间段
     * @return 如果找到了时间周期则返回时间周期的大小;如果未能找到时间周期或者时间周期是默认的-1，那么返回-1
     */
    public long getPeriod(L label,int count);

}
