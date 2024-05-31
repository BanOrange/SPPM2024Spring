package IntervalSet;

import java.io.IOException;
import java.util.*;

public class CommonMultiIntervalSet<L> implements MultiIntervalSet<L> {
    private List<IntervalSet<L>> intervals = new ArrayList<IntervalSet<L>>();

    // AF
    // AF(intervals) = 允许标签多绑定的时间段集合

    // RI
    // 同一个标签段的时间不能重叠

    // Safety from rep exposure
    // use private to modify the field
    // use defensive copy

    /**
     * 构造一个MultiIntervalSet
     */
    public CommonMultiIntervalSet(){checkRep(); return;}

    /**
     * 检查RI是否得到满足
     */
//    List<Long> start = interval.getStarts();
//    List<Long> end = interval.getEnds();
//            for(int i=0;i<start.size();i++){
//        for(int j=0;j< end.size();j++){
//            assert i == j || !(start.get(i) > start.get(j) && start.get(i) < end.get(j));
//            assert i == j || !(end.get(i) > start.get(j) && end.get(i) < end.get(j));
//        }
//    }
    public void checkRep(){
        //检查同一个标签下的时间段是否存在重叠，注：首尾相接例如段二的开始时间等于段一的结束时间，不算重叠
        for (int i=0;i<intervals.size();i++) {
            for(int j=0;j< intervals.size();j++){
              if(j!=i){
                  for(L label : intervals.get(i).labels()){          //如果另一个集合中也含有相同的时间段，那么就去检查时间段是否重叠
                      if(intervals.get(j).labels().contains(label)){
                            assert !(intervals.get(i).start(label) > intervals.get(j).start(label)
                                    && intervals.get(i).start(label) < intervals.get(j).end(label));
                            assert !(intervals.get(i).end(label) > intervals.get(j).start(label)
                                    && intervals.get(i).end(label) < intervals.get(j).end(label));
                      }
                  }
              }
            }
        }
    }

    @Override
    public void insert(long start, long end, L label) throws IOException {
        //检查开始时间和结束时间是否符合条件
        if(start<0 || end < start){
            throw new IOException();
        }
        //检查一下如果加入是否存在重叠
        for (IntervalSet<L> interval : intervals) {
            if(interval.labels().contains(label)){
                if(start>interval.start(label) && start<interval.end(label))
                    throw new IOException();
                if(end>interval.start(label) && end<interval.end(label))
                    throw new IOException();
            }
        }

        if(intervals.isEmpty()){
            IntervalSet<L> interval = IntervalSet.empty();
            interval.insert(start,end,label);
            intervals.add(interval);
            return;
        }

        //检查label是否已经存在于第一个IntervalSet集合当中，因为第一个集合中包含所有的label，如果存在那么将它添加到距离第一个集合最近的集合当中
        if(intervals.getFirst().labels().contains(label)){
            for (IntervalSet<L> lIntervalSet : intervals) {
                if (!lIntervalSet.labels().contains(label)) {        //不包括label那么就可以进行添加
                    lIntervalSet.insert(start, end, label);          //如果现存的所有集合都已经包括label，那就需要新建集合来添加该时间段了
                    checkRep();
                    return;
                }
            }
            IntervalSet<L> interval = IntervalSet.empty();          //现存的集合都装满了，那么就新建一个新集合来装时间段并放入intervals中
            interval.insert(start ,end,label);
            intervals.add(interval);
            checkRep();
            return;
        };

        //如果label不存在于集合之中，那么就加入intervals中的第一个interval集合
        if(!intervals.getFirst().labels().contains(label)){
            intervals.getFirst().insert(start,end,label);
            checkRep();
        }

    }

    @Override
    public Set<L> labels() {
        Set<L> label = new HashSet<>();
        if(intervals.isEmpty()){         //如果intervals是空的话直接返回空集合
            checkRep();
            return label;
        }
        label = intervals.getFirst().labels();
        checkRep();
        return label;
    }

    @Override
    public boolean remove(L label) {
        if(intervals.isEmpty()){     //集合为空直接return false
            return false;
        }

        if(intervals.getFirst().labels().contains(label)){    //如果集合中包含label标签的时间段，那么可以进行删除
            for(IntervalSet<L> interval : intervals){
                if(interval.labels().contains(label))
                    interval.remove(label);                   //删除对应的标签及其对应的所有时间段
            }
            return true;
        }

        return false;         //没找到或者删除失败都要返回false
    }

    @Override
    public IntervalSet<Integer> intervals(L label) {
        IntervalSet<Integer> Inter = IntervalSet.empty();
        int count = 0;
        for(IntervalSet<L> interval : intervals){              //将所有的与标签label有关的时间段加入到Inter集合当中
            if(interval.labels().contains(label)){
                try {
                    Inter.insert(interval.start(label),interval.end(label),count);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                count++;
            }
        }
        checkRep();
        return Inter;          //如果没有任何时间段的标签是label，那么应该返回一个空集
    }

    /**
     * 将一个标签的对应多时间段的时间段集合生成一个人类可读的字符串
     *
     * @return 返回一个人类可读的字符串，如果集合中没有时间段，那么返回空串，否则返回一个人类可读的字符串,其中将输出每一个label的第一个时间段，然后输出每个标签的第二个时间段，以此类推
     */
    @Override
    public String toString() {

        StringBuilder res = new StringBuilder();
        for(IntervalSet<L> interval : intervals){
            res.append(interval.toString());
        }
        return res.toString();
    }
}


//为了完成实验中对于复用IntervalSet<L>的要求，以下代码只好放弃

//class MultiInterval<L>{
//    private L label;
//    private final List<Long> starts = new ArrayList<Long>();
//    private final List<Long> ends = new ArrayList<Long>();
//
//    // AF
//    // AF(label) = 时间段的标签
//    // AF(starts) = 标签所对应的多个时间段的开始时间
//    // AF(ends) = 标签所对应的多个时间段的结束时间
//
//    // RI
//    // 时间段不可以有重叠,一个时间段的开始时间和另一个时间段的结束时间相重合不算重叠
//
//    // safety from rep exposure
//    // use private and final to modify　fields
//    // use defensive copy
//
//    /**
//     * 构造一个标签所对应的时间段
//     *
//     * @param label 这些时间段所对应的标签
//     */
//    public MultiInterval(L label) {
//        this.label = label;
//        checkRep();
//    }
//
//    /**
//     * 检查RI是否得到满足
//     */
//    public void checkRep(){
//        for(int i=0;i<starts.size();i++){
//            for(int j=0;j<starts.size();j++){
//                assert !(starts.get(i) >= starts.get(j) && starts.get(i) <= ends.get(j));
//                assert !(ends.get(i) >= starts.get(j) && ends.get(i) <= ends.get(j));
//            }
//        }
//    }
//
//    /**
//     * 得到这些时间段所对应的标签
//     *
//     * @return 这些时间段所对应的标签
//     */
//    public L getLabel() {
//        L labelCopy = label;
//        return labelCopy;   //防御式拷贝
//    }
//
//    /**
//     * 得到这个标签所对应的所有时间段的开始时间
//     *
//     * @return 以List的形式返回这个标签所对应的所有时间段的开始时间，如果没有时间段与这个标签对应，那么返回空集
//     */
//    public List<Long> getStarts() {
//        List<Long> start = this.starts;
//        checkRep();
//        return start;          //防御式拷贝
//    }
//
//    /**
//     * 得到这个标签所对应的所有时间段的结束时间
//     * @return 以List的形式返回这个标签所对应的所有时间段的结束时间，如果没有时间段与这个标签对应，那么返回空集
//     */
//    public List<Long> getEnds() {
//        List<Long> end = this.ends;
//        checkRep();
//        return end;            //防御式拷贝
//    }
//
//    /**
//     * 向该标签添加新的绑定的时间段
//     *
//     * @param start 新时间段的开始时间
//     * @param end   新时间段的结束时间
//     * @return   如果添加成功则返回true，如果添加失败则返回false
//     */
//    public boolean addInterval(long start,long end){
//        for(int i =0;i<starts.size();i++){
//            if (start >= starts.get(i) && start <= ends.get(i)){
//                return false;
//            }
//            if (end >= starts.get(i) && end <= ends.get(i)){
//                return false;
//            }
//        }
//        starts.add(start);
//        ends.add(end);
//        checkRep();
//        return true;
//    }
//}

