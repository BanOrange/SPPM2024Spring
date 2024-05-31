package IntervalSet;

import java.io.IOException;
import java.util.Set;

public interface IntervalSet<L> {
    /**
     * 创造一个空的时间段集合
     * <p>
     * @param <L> 时间段集合中标签的数据类型，必须是不可变类型
     * @return 一个新的空的时间段集合
     */
    public static <L> IntervalSet<L> empty(){
        return new CommonIntervalSet<L>();
    };

    /**
     * 向当前时间段集合中插入一组新的时间段和对应标签
     *
     * @param start 时间段的开始时间
     * @param end 时间段的结束时间，不能早于开始时间
     * @param label 时间段对应的标签，必须是不可变类型
     * @throws IOException 如果开始时间小于0;结束时间小于开始时间;标签重复（即该标签已经有一个时间段与之对应）
     */
    public void insert(long start,long end,L label) throws IOException;

    /**
     * 获得当前时间段集合中所有时间段的标签
     *
     * @return 一个由所有时间段的标签组成的Set集合
     */
    public Set<L> labels();

    /**
     * 移除在时间段集合之中该标签对应的时间段
     *
     * @param label 时间段的标签，必须已经存在于时间段集合之中
     * @return 如果移除成功则返回true；如果没有找到标签对应的时间段或者移除失败则返回false
     */
    public boolean remove(L label);

    /**
     * 返回标签对应的时间段的开始时间
     *
     * @param label 时间段的标签
     * @return 如果成功找到该标签对应的时间段则返回对应的开始时间；如果没有找到该标签对应的时间段或者查找开始时间失败则返回-1
     */
    public long start(L label);

    /**
     * 返回标签对应的时间段的结束时间
     *
     * @param label 时间段的标签
     * @return 如果成功找到该该标签对应的时间段则返回对应的开始时间；如狗没有找到该标签对应的时间段或者查找结束时间失败则返回-1
     */
    public long end(L label);
}
