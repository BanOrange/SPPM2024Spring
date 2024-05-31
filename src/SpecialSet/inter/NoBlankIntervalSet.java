package SpecialSet.inter;

public interface NoBlankIntervalSet<L> {
    /**
     * 检查时间段是否覆盖了对应的区间
     *
     * @param begin 时间区间的开始时间，必须要>=0
     * @param end   时间区间的结束时间，必须>=begin
     * @return 如果时间段覆盖了整个区间，即没有空白，那么就返回true，否则返回false
     */
    public boolean checkNoBlank(long begin,long end);


}
