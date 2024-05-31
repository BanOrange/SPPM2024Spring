package SpecialSet.inter;

public interface NonPeriodicIntervalSet<L> {
    /**
     * 检查时间段集合是否有重复周期
     *
     * @param period 时间段集合的重复周期字段（不是所有时间段集合都含有此字段）
     * @return 如果重复周期是0则返回true，重复周期不为0则返回false
     */
    public boolean checkNoPeriodic(long period);


}
