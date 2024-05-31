package entity;

public class Process {
    private final int ID;
    private final String name;
    private final long startTime;
    private final long endTime;

    // AF
    // AF(ID) = 进程的ID；
    // AF(name) = 进程的名称
    // AF(startTime) = 进程的最短执行时间
    // AF(endTime） = 进程的最长执行时间

    //RI
    // 不能为空
    // startTime和endTime必须要大于0
    // endTime大于等于startTime

    // safety from rep exposure
    // use private and final to modify fields
    // 使用不可变的数据类型

    /**
     * 检查RI是否得到满足
     */
    public void checkRep(){
        assert name!=null;
        assert startTime>0 && endTime>0;
        assert endTime>=startTime;
    }

    /**
     * 构造一个系统进程
     *
     * @param ID 进程的ID
     * @param name 进程的名称
     * @param startTime 进程的最短执行时间
     * @param endTime 进程的最长执行时间
     */
    public Process(int ID, String name, long startTime, long endTime) {
        this.ID = ID;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        checkRep();
    }

    /**
     * 得到进程的ID
     * @return 进程的ID
     */
    public int getID() {
        checkRep();
        return ID;
    }

    /**
     * 得到进程的名称
     * @return 进程的名称
     */
    public String getName() {
        checkRep();
        return name;
    }

    /**
     * 得到进程的最短执行时间
     * @return 进程的最短执行时间
     */
    public long getStartTime() {
        checkRep();
        return startTime;
    }

    /**
     * 得到进程的最长执行时间
     * @return 进程的最长执行时间
     */
    public long getEndTime() {
        checkRep();
        return endTime;
    }

    @Override
    public String toString(){
        String res = "";
        res = res + getName()+"  "+getID()+"  "+getStartTime()+"  "+ getEndTime();
        return res;
    }
}
