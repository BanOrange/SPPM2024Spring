package APIs.ProcessIntervalSet;

import IntervalSet.IntervalSet;
import IntervalSet.MultiIntervalSet;
import SpecialSet.inter.impl.NonOverlapIntervalSetImpl;
import SpecialSet.inter.impl.NonPeriodicIntervalSetImpl;

import java.io.IOException;
import java.util.Set;

public class ProcessIntervalSet<L> implements IProcessIntervalSet<L>, MultiIntervalSet<L> {
    private NonOverlapIntervalSetImpl<L> nois;
    private NonPeriodicIntervalSetImpl<L> npis;
    private MultiIntervalSet<L> intervals = MultiIntervalSet.empty();

    // AF
    // AF（NonOverlapIntervalSetImpl，NonPeriodicIntervalSetImpl,inter,intervals） = 操作系统进程调度记录

    // RI
    // 与三个字段的RI相同
    // 也即三个字段中自带的rep已经实现了RI的检查

    //Safety from rep exposure
    // use private to modify field


    /**
     * 构造方法，构造一个ProcessIntervalSet
     */
    public ProcessIntervalSet() {
        nois = new NonOverlapIntervalSetImpl<>(this.intervals);
        npis = new NonPeriodicIntervalSetImpl<>();
        checkRep();
    }

    public void checkRep(){
        assert intervals != null;
        assert npis!=null;
        assert nois!=null;
    }

    @Override
    public void insert(long start, long end, L label) throws IOException {
        nois.insert(start,end,label);
        checkRep();
    }

    @Override
    public Set<L> labels() {
        return intervals.labels();
    }

    @Override
    public boolean remove(L label) {
        return intervals.remove(label);
    }

    @Override
    public IntervalSet<Integer> intervals(L label) {
        return intervals.intervals(label);
    }

    @Override
    public boolean checkNoPeriodic(long period) {
        return npis.checkNoPeriodic(period);
    }

    @Override
    public String toString(){
        return intervals.toString();
    }
}
