package APIs;

import IntervalSet.IntervalSet;
import IntervalSet.MultiIntervalSet;

public interface IcalcAPIs<L> {
    /**
     * 计算两个MultiIntervalSet集合的相似度
     *
     * @param intervals1 第一个MultiIntervalSet集合
     * @param intervals2 第二个MultiIntervalSet集合
     * @return 计算成功的情况下返回两个时间段集合的整体相似度,介于0-1之间
     */
    public double Similarity(MultiIntervalSet<L> intervals1,MultiIntervalSet<L> intervals2);

    /**
     * 发现一个 IntervalSet<L>对象中的时间冲突比例
     *
     * @param intervals IntervalSet集合
     * @return 冲突指同一个时间段内安排了两个不同的 interval 对象。用
     *         发生冲突的时间段总长度除于总长度，得到冲突比例，是一个[0,1]之间的值
     *         计算成功时返回冲突比例。
     */
    public double calcConflictRatio(IntervalSet<L> intervals);

    /**
     * 发现 IntervalSet.MultiIntervalSet<L>对象中的时间冲突比例
     *
     * @param intervals MultiIntervalSet集合
     * @return 冲突指同一个时间段内安排了两个不同的 interval 对象。用
     *         发生冲突的时间段总长度除于总长度，得到冲突比例，是一个[0,1]之间的值
     *         计算成功时返回冲突比例。
     */
    public double calcConflictRatio(MultiIntervalSet<L> intervals);

    /**
     * 计算一个IntervalSet集合的空闲时间比例
     *
     * @param intervals IntervalSet集合
     * @param length 人为规定的时间段长度(必须>=当前事件段集合的长度)，如果设为<=0的数则视为取当前集合中最早时间和最晚时间的差作为时间段的长度
     * @return 计算成功的情况下返回空闲时间比例；失败情况返回-1，如果集合为空返回0
     */
    public double calcFreeTimeRatio(IntervalSet<L> intervals,long length);

    /**
     * 计算一个MultiIntervalSet集合的空闲时间比例
     *
     * @param intervals MultiIntervalSet集合
     * @param length 人为规定的时间段长度(必须>=当前时间集合的长度)，如果设为<=0的数则视为取当前集合中最早时间和最晚时间的差作为时间段的长度
     * @return 计算成功的情况下返回空闲时间比例，失败情况返回-1，如果集合为空返回0
     */
    public double calcFreeTimeRatio(MultiIntervalSet<L> intervals,long length);
}




