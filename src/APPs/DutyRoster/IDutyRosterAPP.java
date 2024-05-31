package APPs.DutyRoster;

import entity.Employee;

import java.io.IOException;
import java.text.ParseException;

public interface IDutyRosterAPP{
    /**
     * 为排班表设置起止时间
     *
     * @param startTime 排班表的开始时间，符合格式yyyy-MM-dd
     * @param endTime 排班表的结束时间，必须要晚于开始时间
     * @return 如果设置成功则返回true，设置失败则返回false
     * @throws ParseException 如果不符合格式则抛出异常
     */
    public boolean setTime(String startTime,String endTime) throws ParseException;
    /**
     * 向系统中加入员工信息
     *
     * @param name 员工的姓名,不能和已经存在的员工重名
     * @param job 员工的职位
     * @param telephone 员工的电话号码，需要符合电话号码的11位格式
     * @return 如果添加成功返回true，添加失败返回false，
     *         如果员工的姓名和已经存在于的员工姓名相同则视为更新，删除旧的员工信息改为现在的信息
     */
    public boolean addEmployee(String name,String job,String telephone);

    /**
     * 向排班表中加入排班记录
     *
     * @param start 排班记录的开始时间
     * @param end 排班记录的结束时间
     * @param name 排班记录对应的员工的姓名
     * @return 成功添加则返回true，反之返回false
     */
    public boolean addDuty(String start,String end,String name) throws IOException;

    /**
     * 向系统中添加一个员工
     *
     * @param e 该员工对应的对象
     * @return 如果添加成功则返回true，反之返回false
     */
    public boolean addEmployee(Employee e);

    /**
     * 移除一个员工，如果他有排班记录，那么将排班记录也一起移除掉
     *
     * @param name 员工的姓名
     */
    public void remove(String name);

    /**
     * 检验排班表是否已经装满
     * @return 如果已经装满，那么返回true，反之返回false
     */
    public boolean full();

    /**
     * 从文件中读取数据并构建一个排班表
     * @param filePath 文件地址
     */
    public void readFile(String filePath) throws ParseException, IOException;

    /**
     * 通过员工的姓名得到员工的职位
     *
     * @param name 员工的姓名
     * @return 字符串形式的员工的职位
     */
    public String getJob(String name);

    /**
     * 通过员工的姓名获得员工的电话号码
     *
     * @param name 员工的姓名
     * @return 员工的电话号码
     */
    public String telephone(String name);

    /**
     * 展示所有的员工信息
     * @return 字符串形式的员工信息
     */
    public String showEmployee();

    /**
     * 得到当前排班表的空闲时间比例
     * @return 介于0-1之间的小数形式的空闲时间比例 如果表中未填入任何数据或者表的大小为0则返回-1
     */
    public double getFreeTimeRatio();

    /**
     * 获取当前还空闲的日期
     * @return 以字符串形式返回当前还空闲的日期
     */
    public String getFreeTime();

    /**
     * 系统随机完成排班操作
     */
    public void random();
}
