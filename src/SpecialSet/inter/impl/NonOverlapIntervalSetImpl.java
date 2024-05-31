package SpecialSet.inter.impl;

import IntervalSet.IntervalSet;
import IntervalSet.MultiIntervalSet;
import SpecialSet.inter.NonOverlapIntervalSet;

import java.io.IOException;
import java.util.Set;

public class NonOverlapIntervalSetImpl<L> implements NonOverlapIntervalSet<L> {
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
    public NonOverlapIntervalSetImpl(IntervalSet<L> intervalSet){
        this.intervals = intervalSet;
        flag = 0;
        checkRep();
    }

    /**
     * 构造方法
     * @param multiIntervalSet 传入的时间段集合
     */
    public NonOverlapIntervalSetImpl(MultiIntervalSet<L> multiIntervalSet){
        this.Multiintervals = multiIntervalSet;
        flag = 1;
        checkRep();
    }

    /**
     * 检查RI是否得到满足
     */
    public void checkRep(){
        assert intervals != null;
        assert Multiintervals != null;
        assert flag==1 || flag==0;
    }
    @Override
    public void insert(long start, long end, L label) throws IOException {
        if(flag == 0){
            if(start<0 || end<start){
                checkRep();
                throw new IOException("输入的参数不满足条件");
            }
            Set<L> labels = intervals.labels();
            if(labels.contains(label)){
                checkRep();
                throw new IOException("普通时间段集合不能输入重复的标签");
            }
            for(L labelNow : labels){
                if(start>=intervals.start(labelNow) && start<=intervals.end(labelNow)){
                    checkRep();
                    throw new IOException("存在重叠现象");
                }
                if(end>=intervals.start(labelNow) && end<=intervals.end(labelNow)){
                    checkRep();
                    throw new IOException("存在重叠现象");
                }
                if(start<=intervals.start(labelNow) && end>=intervals.end(labelNow)){
                    throw new IOException("存在重叠现象");
                }
            }
            intervals.insert(start,end,label);
            checkRep();
            return;

        }




        if(flag == 1){
            if(start<0 || end<start){
                checkRep();
                throw new IOException();
            }
            Set<L> labels = Multiintervals.labels();
            for(L labelNow : labels){
                IntervalSet<Integer> interval = Multiintervals.intervals(labelNow);
                int count = 0;
                while(interval.start(count)!=-1){
                    if(start>interval.start(count) && start<interval.end(count)){
                        checkRep();
                        throw new IOException();

                    }
                    if(end>interval.start(count) && end<interval.end(count)){
                        checkRep();
                        throw new IOException();

                    }
                    count++;
                }
            }

            Multiintervals.insert(start,end,label);
            checkRep();

        }
    }

}
