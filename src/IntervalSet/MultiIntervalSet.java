package IntervalSet;

import IntervalSet.IntervalSet;

import java.io.IOException;
import java.util.Set;

public interface MultiIntervalSet<L> {
    /**
     * 创造一个空地允许多绑定的时间段集合
     * <p>
     *
     * @param <L> 时间段集合中标签的数据类型，必须是不可变类型
     * @return 一个空地允许多绑定的时间段集合
     */
    public static <L> CommonMultiIntervalSet<L> empty(){
        return new CommonMultiIntervalSet<L>();
    };

    /**
     * 向当前时间段集合中插入一组新的时间段和对应标签
     *
     * @param start 时间段的开始时间
     * @param end 时间段的结束时间，不能早于开始时间
     * @param label 时间段对应的标签，必须是不可变类型
     * @throws IOException 如果开始时间小于0;结束时间小于等于开始时间;输入的时间段与集合内已存入的相同标签的时间段存在重叠
     */
    public void insert(long start,long end,L label) throws IOException;

    /**
     * 获得当前时间段集合中所有时间段的标签
     *
     * @return 一个由所有时间段的标签组成的Set集合
     */
    public Set<L> labels();

    /**
     * 移除在时间段集合之中该标签对应的所有时间段
     *
     * @param label 时间段的标签，必须已经存在于时间段集合之中
     * @return 如果将该标签的所有对应时间段全部移除成功则返回true；如果没有找到标签对应的时间段或者移除失败则返回false
     */
    public boolean remove(L label);

    /**
     * 得到该标签对应的所有时间段
     *
     * @param label 时间段的标签
     * @return 返回一个Interval集合，其中这个集合label为我们传入参数label所对应的时间段的次序，而对应时间段则是我们传入参数
     *          label所对应的时间段。例如 { "A"=[[0,10],[20,30]],"B"=[[10,20]]}，intervals("A") 返回的结果是
     *          {0=[0,10],1=[20,30]}。
     *          如果在集合中没有找到这个标签，那么返回一个空集
     */
    public IntervalSet<Integer> intervals(L label);
}
