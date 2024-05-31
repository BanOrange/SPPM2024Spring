package APIs;

import IntervalSet.IntervalSet;
import IntervalSet.MultiIntervalSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class calcAPIs<L> implements IcalcAPIs<L> {

    @Override
    public double Similarity(MultiIntervalSet<L> intervals1, MultiIntervalSet<L> intervals2) {
        if (intervals1 == null || intervals2 == null) return 0;
        double similarity = 0;
        Set<L> labels1 = intervals1.labels();
        Set<L> labels2 = intervals2.labels();
        if (labels1.size() == 0 || labels2.size() == 0) return 0;
        long min = Integer.MAX_VALUE; //记录所有时间段的最早开始时间
        long max = 0; //记录所有时间段的最晚结束时间
        for (L label1 : labels1) {          //计算相似的时间段长度
            for (L label2 : labels2) {
                if (label1.equals(label2)) {
                    IntervalSet<Integer> interval1 = intervals1.intervals(label1);
                    IntervalSet<Integer> interval2 = intervals2.intervals(label2);
                    int count1 = 0;
                    while (interval1.start(count1) != -1) {
                        int count2 = 0;
                        while (interval2.start(count2) != -1) {   //分为四种可能性
                            long start1 = interval1.start(count1);
                            long end1 = interval1.end(count1);
                            long start2 = interval2.start(count2);
                            long end2 = interval2.end(count2);

                            if (end1 > end2 && end1 > max) max = end1;
                            if (end2 >= end1 && end2 > max) max = end2;
                            if (start1 < start2 && start1 > min) min = start1;
                            if (start2 <= start1 && start2 < min) min = start2;

                            if (start1 >= start2 && end1 <= end2) {
                                similarity = similarity + end1 - start1;
                            } else if (start2 >= start1 && end2 <= end1) {
                                similarity = similarity + end2 - start2;
                            } else if (start1 < start2 && end1 < end2) {
                                similarity = similarity + end1 - start2;
                            } else if (start1 > start2 && end1 > end2) {
                                similarity = similarity + end2 - start1;
                            }
                            count2++;
                        }
                        count1++;
                    }
                }

            }
        }

        //得到总时长
        IntervalSet<Integer> allInterval = IntervalSet.empty(); //用来装载集合1所有的时间段

        int number = 0;
        for (L label : labels1) {   //先把集合1的所有的时间段都加入到allInterval当中
            IntervalSet<Integer> interval = intervals1.intervals(label);
            int count = 0;
            while (interval.start(count) != -1) {
                try {
                    allInterval.insert(interval.start(count), interval.end(count), number);
                } catch (
                        IOException e) {
                    throw new RuntimeException(e);
                }
                count++;
                number++;
            }
        }
        //装入集合2的所有时间段
        for (L label : labels2) {
            IntervalSet<Integer> interval = intervals1.intervals(label);
            int count = 0;
            while (interval.start(count) != -1) {
                try {
                    allInterval.insert(interval.start(count), interval.end(count), number);
                } catch (
                        IOException e) {
                    throw new RuntimeException(e);
                }
                count++;
                number++;
            }
        }

        //计算总长度
        int count = 0;
        while (allInterval.start(count) != -1) {
            long start = allInterval.start(count);
            long end = allInterval.end(count);
            if (start < min) min = start;
            if (end > max) max = end;
            count++;
        }
        long length = max - min;

        double res = (similarity / length);
        return res;

    }

    @Override
    public double calcConflictRatio(IntervalSet<L> intervals) {
        Set<L> labels = intervals.labels();
        List<Integer> allTime = new ArrayList<>();
        if (labels.size() == 0) return 0;
        long max = 0;
        long min = Integer.MAX_VALUE;

        for (L label : labels) {          //得到总长度
            long start = intervals.start(label);
            long end = intervals.end(label);
            if (start < min) min = start;
            if (end > max) max = end;
        }
        long length = max - min;

        for (int i = 0; i < length; i++) {
            allTime.add(0);
        }

        //因为逻辑有些复杂，尝试了很多方法也未能实现，只好进行穷举法了
        int count = 0;
        for (L label : labels) {
            long start = intervals.start(label);
            long end = intervals.end(label);
            for (long i = start - min; i < end - min; i++) {
                if (allTime.get((int) i) == 1) {
                    allTime.set((int) i, 2);
                    count++;
                    continue;
                }
                allTime.set((int) i, 1);

            }
        }

        double conflict = (double) count / length;
        return conflict;

    }

    @Override
    public double calcFreeTimeRatio(MultiIntervalSet<L> intervals,long length) {
        Set<L> labels1 = intervals.labels();
        if (labels1.size() == 0) return 0;
        long max = 0;
        long min = Integer.MAX_VALUE;
        IntervalSet<Integer> allInterval = IntervalSet.empty();
        int number = 0;

        for (L label : labels1) {   //先把所有的时间段都加入到一个集合当中
            IntervalSet<Integer> interval = intervals.intervals(label);
            int count = 0;
            while (interval.start(count) != -1) {
                try {
                    allInterval.insert(interval.start(count), interval.end(count), number);
                } catch (
                        IOException e) {
                    throw new RuntimeException(e);
                }
                count++;
                number++;
            }
        }


        //之后按照正常的IntervalSet集合来进行空闲时间的计算
        Set<Integer> labels = allInterval.labels();
        for (int label : labels) {          //得到总长度
            long start = allInterval.start(label);
            long end = allInterval.end(label);
            if (start <= min) min = start;
            if (end >= max) max = end;
        }
        long length1 = max - min +1;

        List<Integer> allTime = new ArrayList<>();

        for (int i = 0; i < length1; i++) {
            allTime.add(0);
        }

        //因为逻辑有些复杂，尝试了很多方法也未能实现，只好进行穷举法了
        long count = 0;
        for (Integer label : labels) {
            long start = allInterval.start(label);
            long end = allInterval.end(label);
            for (long i = start - min; i <= end - min; i++) {
                allTime.set((int) i, 1);
            }
        }
        for(int i=0;i<allTime.size();i++){
            if(allTime.get(i) == 0){
                count++;
            }
        }
        if(length >= length1){
            count = count + length-length1;
            length1 = length;
        }else if(length<length1 && length>0){
            return -1;
        }else if(length<0){
            length1 = length1;
        }

        double freeTime = (double)count / length1;
        return freeTime;
    }

    @Override
    public double calcFreeTimeRatio(IntervalSet<L> intervals,long length) {
        Set<L> labels = intervals.labels();
        List<Integer> allTime = new ArrayList<>();
        if (labels.size() == 0) return 0;
        long max = 0;
        long min = Integer.MAX_VALUE;

        for (L label : labels) {          //得到总长度
            long start = intervals.start(label);
            long end = intervals.end(label);
            if (start <= min) min = start;
            if (end >= max) max = end;
        }
        long length1 = max - min + 1;

        for (int i = 0; i < length1; i++) {
            allTime.add(0);
        }

        //因为逻辑有些复杂，尝试了很多方法也未能实现，只好进行穷举法了
        long count = 0;
        for (L label : labels) {
            long start = intervals.start(label);
            long end = intervals.end(label);
            for (long i = start - min; i <= end - min; i++) {
                allTime.set((int) i, 1);
            }

        }
        for(int i=0;i<allTime.size();i++){
            if(allTime.get(i) == 0){
                count++;
            }
        }
        if(length >= length1){
            count = count + length-length1;
            length1 = length;
        }else if(length<length1 && length>0){
            return -1;
        }else if(length<0){
            length1 = length1;
        }
        double conflict = (double) count /length1;
        return conflict;
    }

    @Override
    public double calcConflictRatio(MultiIntervalSet<L> intervals) {
        Set<L> labels = intervals.labels();
        if (labels.size() == 0) return 0;
        long max = 0;
        long min = Integer.MAX_VALUE;
        int number = 0;
        IntervalSet<Integer> allInterval = IntervalSet.empty(); //用来装载所有的时间段
        List<Integer> allTime = new ArrayList<>();

        for (L label : labels) {   //先把所有的时间段都加入到一个集合当中
            IntervalSet<Integer> interval = intervals.intervals(label);
            int count = 0;
            while (interval.start(count) != -1) {
                try {
                    allInterval.insert(interval.start(count), interval.end(count), number);
                } catch (
                        IOException e) {
                    throw new RuntimeException(e);
                }
                count++;
                number++;
            }
        }

        //计算总长度
        int count = 0;
        while (allInterval.start(count) != -1) {
            long start = allInterval.start(count);
            long end = allInterval.end(count);
            if (start < min) min = start;
            if (end > max) max = end;
            count++;
        }
        long length = max - min;

        for (int i = 0; i < length; i++) {
            allTime.add(0);
        }

        //因为逻辑有些复杂，尝试了很多方法也未能实现，只好进行穷举法了
        int countTime = 0;
        Set<Integer> numberSet = allInterval.labels();
        for (int label : numberSet) {
            long start = allInterval.start(label);
            long end = allInterval.end(label);
            for (long i = start - min; i < end - min; i++) {
                if (allTime.get((int) i) == 1) {
                    allTime.set((int) i, 2);
                    countTime++;
                    continue;
                }
                allTime.set((int) i, 1);

            }
        }

        double conflict = (double) countTime / length;
        return conflict;

    }
}
