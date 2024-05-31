package SpecialSet.inter;

import java.io.IOException;

public interface NonOverlapIntervalSet<L>{
    /**
     * 在所有时间段都不允许重叠的情况进行插入
     * @param start 时间段的开始时间，需要>0
     * @param end 时间段的结束时间，需要>=start
     * @param label 时间段的标签,不能与已有标签重复
     * @throw IOException 如果不满足前置条件则抛出异常
     */
    public void insert(long start,long end,L label) throws IOException;

}
