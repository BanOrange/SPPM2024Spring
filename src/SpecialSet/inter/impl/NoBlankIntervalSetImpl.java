package SpecialSet.inter.impl;

import IntervalSet.IntervalSet;
import IntervalSet.MultiIntervalSet;
import SpecialSet.inter.NoBlankIntervalSet;

import java.util.Set;

public class NoBlankIntervalSetImpl<L> implements NoBlankIntervalSet<L>{
    private IntervalSet<L> intervals = IntervalSet.empty();
    private MultiIntervalSet<L> Multiintervals = MultiIntervalSet.empty();
    private int flag;

    // AF
    // AF(intervals) = 不允许一个标签的对应多个时间段的时间段集合
    // AF（Multiintervals） = 允许一个标签对应多个时间段的时间段集合
    // AF (flag) = 该集合中采用的时间段集合类型，0为IntervalSet，1为MultiIntervalSet

    // RI
    // 要求与IntervalSet和MultiIntervalSet相同
    // flag要么等于1，要么等于0，不能为null

    // safety form rep exposure
    // use private to modify field

    /**
     * 构造函数
     * @param intervalSet 传入的时间段集合
     */
    public NoBlankIntervalSetImpl(IntervalSet<L> intervalSet){
        this.intervals = intervalSet;
        flag = 0;
        checkRep();
    }

    /**
     * 构造方法
     * @param multiIntervalSet 传入的时间段集合
     */
    public NoBlankIntervalSetImpl(MultiIntervalSet<L> multiIntervalSet){
        this.Multiintervals = multiIntervalSet;
        flag = 1;
        checkRep();
    }

    public void checkRep(){
        assert intervals != null;
        assert Multiintervals != null;
        assert flag==1 || flag==0;
    }
    @Override
    public boolean checkNoBlank(long begin, long end) {
        //flag == 0 ，用的是IntervalSet集合
        if(flag==0){
            if(begin<0 || end<begin) return false;
            Set<L> labels = intervals.labels();
            if(labels.isEmpty()) {
                return false;
            }
            long startTime = begin;
            while(startTime < end){            //每回找开始时间在startTime之前的时间段，然后将startTime更新为该时间段的结束时间
                int flag = 0;                  //通过判断最终startTime最终是否能够大于end来判断是否存在空白
                for(L label:labels){
                    if(intervals.start(label)<=startTime && intervals.end(label)>=startTime){
                        startTime = intervals.end(label)+1;
                        flag = 1;
                    }
                }
                if(flag == 0){
                    return false;
                }
            }
            checkRep();
            return true;
        }

        //flag ==1 的情况，用的是MultiIntervalSet
        if(flag == 1){
            if(begin<0 || end<begin) return false;
            Set<L> labels = Multiintervals.labels();
            if(labels.isEmpty()){
                checkRep();
                return false;
            }
            long startTime = begin;
            while(startTime < end){
                int time = 0;
                int flag = 0;
                for(L labelNow : labels){
                    int count = 0;
                    IntervalSet<Integer> interval = Multiintervals.intervals(labelNow);
                    while(interval.start(count) != -1){
                        if(interval.start(count)<=startTime && interval.end(count)>startTime){
                            startTime = interval.end(count);
                            flag = 1;
                        }
                        count++;
                    }

                }


                if(flag == 0){
                    checkRep();
                    return false;
                }
            }
            checkRep();
            return true;
        }
        //flag!=1 && flag!=0
        return false;
    }
}
