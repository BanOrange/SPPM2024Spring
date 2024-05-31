package APIs.CourseIntervalSet;

import IntervalSet.IntervalSet;
import IntervalSet.MultiIntervalSet;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class CourseIntervalSet<L> implements ICourseIntervalSet<L>, MultiIntervalSet<L> {
    private MultiIntervalSet<L> intervals = MultiIntervalSet.empty();
    private final Map<L,List<Long>> PeriodMap = new HashMap<>();

    // AF
    // AF(PeriodMap) = 所有时间段的重复周期
    // AF(intervals) = 课表

    // RI
    // PeriodMap中List的的数值要么为-1，要么>0

    // safety from rep exposure
    // use private and final to modify field

    /**
     * 构造函数
     */
    public CourseIntervalSet() {
        checkRep();
    }

    /**
     * 检查RI是否得到满足
     */
    public void checkRep(){
        assert intervals!=null;
        Set<L> labels = intervals.labels();

        for(L label : labels){
            List<Long> periods = PeriodMap.get(label);
            for(long period : periods){
                assert period>=0 || period==-1;
            }
        }
    }

    @Override
    public void insert(long start, long end, L label) throws IOException {
        intervals.insert(start,end,label);                //先尝试将该时间段加入到MultiIntervalSet中
        Set<L> labels = intervals.labels();
        for(L labelNow : labels){                         //如果MultiIntervalSet中产生了变化，那么就将对应的重复周期默认设置为-1
            IntervalSet<Integer> inter = intervals.intervals(labelNow);
            if(!PeriodMap.containsKey(labelNow)){
                List<Long> periods = new ArrayList<>();
                periods.add(-1L);
                PeriodMap.put(labelNow,periods);
            }
            int count = 0;
            while(inter.start(count) != -1){
                count++;
            }
            if(PeriodMap.get(labelNow).size()!=count){
                PeriodMap.get(labelNow).add(-1L);
            }
        }

    }

    @Override
    public Set<L> labels() {
        return intervals.labels();
    }

    @Override
    public boolean remove(L label) {
        if(PeriodMap.containsKey(label)){
            PeriodMap.remove(label);
        }
        return intervals.remove(label);
    }

    @Override
    public IntervalSet<Integer> intervals(L label) {
        return intervals.intervals(label);
    }

    @Override
    public boolean setPeriod(L label, int count, long period) {
        if(count<1) return false;
        if(period != -1 &&  period <0){
            return false;
        }
        if(PeriodMap.containsKey(label)){
            if(PeriodMap.get(label).size()>=count){
                PeriodMap.get(label).add(count-1,period);
                return true;
            }
        }
        return false;
    }

    @Override
    public long getPeriod(L label, int count) {
        if(count<1) return -1;
        if(PeriodMap.containsKey(label)){
            if(PeriodMap.get(label).size()>=count){
                return PeriodMap.get(label).get(count-1);
            }
            return -1;    //count的值比对应标签的
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        Set<L> labels = intervals.labels();
        for(L label : labels){
            IntervalSet<Integer> interval = intervals.intervals(label);
            int count = 0;
            while(interval.start(count)!=-1){
                res.append("Label:").append(label.toString()).append("  ").append("startTime:")
                        .append(interval.start(count)).append("  ")
                        .append("endTime:").append(interval.end(count)).append("  ")
                        .append("Period:").append(PeriodMap.get(label).get(count))
                        .append("\n");
                count++;
            }
        }
        return res.toString();
    }
}
