package IntervalSet;

import java.io.IOException;
import java.util.*;

public class CommonIntervalSet<L> implements IntervalSet<L> {
    private final List<interval<L>> intervals = new ArrayList<interval<L>>();

    // AF
    // AF(intervals) = 时间段集合

    // RI
    // two labels cant be same
    // there is no overlap between two intervals of one label

    // Safety from rep exposure
    // use private and final to modify field

    /**
     * 构造一个intervalSet类
     */
    public CommonIntervalSet() {
         checkRep();
    }

    /**
     * 检查Rep是否得到满足
     */
    public void checkRep() {
        if(intervals.isEmpty()){
            return;
        }
        for (interval<L> interval1 : intervals) {
            for (interval<L> interval2 : intervals) {
                if(!interval1.equals(interval2)) {
                    assert !(interval1.getLabel().equals(interval2.getLabel()));
                }
            }
        }
    }

    @Override
    public void insert(long start, long end, L label) throws IOException {
        if(start<0 || end<start){
            throw new IOException();
        }
        interval<L> interval = new interval<L>(label, start, end);
        intervals.add(interval);
        checkRep();
    }

    @Override
    public Set<L> labels() {
        Set<L> labels = new HashSet<>();
        for (interval<L> interval : intervals) {
            labels.add(interval.getLabel());
        }
        checkRep();
        return labels;
    }

    @Override
    public boolean remove(L label) {
        for (interval<L> interval : intervals) {
            if (label.equals(interval.getLabel())) {
                intervals.remove(interval);
                checkRep();
                return true;
            }
        }
        checkRep();
        return false;
    }

    @Override
    public long start(L label) {
        long start;
        for (interval<L> interval : intervals) {
            if (label.equals(interval.getLabel())) {
                start = interval.getStart();
                checkRep();
                return start;
            }
        }
        checkRep();
        return -1;
    }

    @Override
    public long end(L label) {
        long end;
        for (interval<L> interval : intervals) {
            if (label.equals(interval.getLabel())) {
                end = interval.getEnd();
                checkRep();
                return end;
            }
        }
        checkRep();
        return -1;
    }

    /**
     * 将时间段集合展示为人类可读的字符串形式
     *
     * @return 一个人类可读的字符串
     */
    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        //按照开始时间进行排序
        Map<L,Long> startTime = new HashMap<>();
        for(interval<L> interval : intervals){
            startTime.put(interval.getLabel(),interval.getStart());
        }
        while(startTime.size()!=0){
            long start = Integer.MAX_VALUE;
            L labelNow = null;
            for(L label : startTime.keySet()){       //获得最早开始的时间段的标签
                if(start>=startTime.get(label)){
                    start = startTime.get(label);
                    labelNow = label;
                }
            }
            for(interval<L> interval : intervals){
                if(interval.getLabel().equals(labelNow)){
                    res.append(interval.toString());
                }
            }
            startTime.remove(labelNow);
        }
        checkRep();
        return res.toString();
    }
}
class interval<L>{
    private final L label;
    private final long start;
    private final long end;

    // AF
    // AF(label) = 时间段的标签
    // AF(start) = 时间段的开始时间
    // AF(end) = 时间段的结束时间

    // RI
    // start<=end;

    // Safety from rep exposure
    // use private and final to modify fields
    // defensive copy
    // 不可变数据类型

    /**
     * 构造一个时间段
     *
     * @param label 时间段的标签
     * @param start 时间段的开始时间
     * @param end   时间段的结束时间，必须要大于开始时间
     */
    public interval(L label,long start,long end) {
        this.label = label;
        this.start = start;
        this.end = end;
        checkRep();
    }

    /**
     * 检查Rep是否得到满足
     */
    public void checkRep(){
        assert end>=start;
    }

    /**
     * 得到时间段的标签
     *
     * @return 时间段的标签的拷贝
     */
    public L getLabel() {
        L copy = this.label;
        return copy; //防御式编程
    }

    /**
     * 得到时间段的开始时间
     *
     * @return 时间段的开始时间
     */
    public long getStart() {
        return start;
    }

    /**
     * 得到时间段的结束时间
     *
     * @return 时间段的结束时间
     */
    public long getEnd() {
        return end;
    }

    /**
     * 将时间段转化为人类可读的字符串形式
     *
     * @return 一个人类可读的字符串
     */

    @Override
    public String toString(){
        String res = "";
        res = res + "Label:"+label.toString() + "  "+"startTime:"+start+"  "+"endTime:"+end;
        return res+"\n";
    }
}

