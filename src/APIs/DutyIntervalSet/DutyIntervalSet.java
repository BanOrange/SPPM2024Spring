package APIs.DutyIntervalSet;

import APIs.calcAPIs;
import IntervalSet.IntervalSet;
import SpecialSet.inter.impl.NoBlankIntervalSetImpl;
import SpecialSet.inter.impl.NonOverlapIntervalSetImpl;
import SpecialSet.inter.impl.NonPeriodicIntervalSetImpl;

import java.io.IOException;
import java.text.ParseException;
import java.time.DateTimeException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class DutyIntervalSet<L> implements IDutyIntervalSet<L>, IntervalSet<L> {
    private NonOverlapIntervalSetImpl<L> nois;
    private NoBlankIntervalSetImpl<L> nbis;
    private NonPeriodicIntervalSetImpl<L> npis;
    private IntervalSet<L> inter = IntervalSet.empty();
    private String startTime;
    private String endTime;

    // AF
    // AF（NonOverlapIntervalSetImpl，NoBlankIntervalSetImpl，NonPeriodicIntervalSetImpl,inter） = 排班表
    // AF(startTime)  = 排班表的开始时间
    // AF(endTime） = 排班表的结束时间

    // RI
    // 与四个字段的RI相同
    // 也即四个字段中自带的rep已经实现了RI的检查
    //startTime和endTime必须符合“yyyy-MM-dd”的格式
    //endTime>=startTime

    //Safety from rep exposure
    // use private to modify field
    // use defensive copy



    /**
     * 构造方法,构造一个DutyIntervalSet类
     *
     */

//    NonOverlapIntervalSetImpl<L> nois,NoBlankIntervalSetImpl<L> nbis,NonPeriodicIntervalSetImpl<L> npis
    public DutyIntervalSet(String startTime,String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.nois = new NonOverlapIntervalSetImpl<L>(inter);
        this.nbis = new NoBlankIntervalSetImpl<L>(inter);
        this.npis =new NonPeriodicIntervalSetImpl<L>();
        checkRep();
    }

    public void checkRep() {
        assert startTime!=null;
        assert endTime!=null;
        DayWithLong DT = new DayWithLongImpl();
        try {
            assert DT.getInterval(startTime, endTime) >= 0;
        }catch(DateTimeException e){
            assert false;  //如果出现异常说明格式不对，那么不符合rep
        }
        assert inter != null;
        assert nois != null;
        assert npis != null;
        assert nbis != null;
    }
    @Override
    public boolean checkNoBlank(long begin, long end) {
        if(!nbis.checkNoBlank(begin, end)){
            System.out.println("排班表在该时段存在无人值守时间，请继续添加员工");
            return false;
        }else{
            System.out.println("值班表在该时段已经不存在无人值守时间！");
            return true;
        }
    }

    @Override
    public void insert(long start, long end, L label) throws IOException {
        nois.insert(start, end, label);

    }

    @Override
    public boolean checkNoPeriodic(long period) {
        return npis.checkNoPeriodic(period);
    }

    @Override
    public Set<L> labels() {
        return inter.labels();
    }

    @Override
    public boolean remove(L label) {
        return inter.remove(label);
    }

    @Override
    public long start(L label) {
        return inter.start(label);
    }

    @Override
    public long end(L label) {
        return inter.end(label);
    }

    @Override
    public String toString() {
        StringBuilder res= new StringBuilder();
        res.append("排班表的起止时间为").append(startTime).append("---").append(endTime).append("\n");
        res.append("以下是已经安排的时间段\n");
        DayWithLong DT = new DayWithLongImpl();
        String index = startTime;
        Set<L> labels = inter.labels();
        while(true){
            try {
                if (index.equals(DT.addDays(endTime, 1))) break;
            } catch (DateTimeException e) {
               e.printStackTrace();
            }
            try {
                int flag = 0;    //用来标识当前日期是否已经出现在某个时间段里面
                L labelNow=null;
                for(L label:labels){
                    long beginOfInterval = DT.getInterval(startTime,index);
                    if(beginOfInterval<=inter.end(label) && beginOfInterval>=inter.start(label)){
                        flag = 1;
                        labelNow=label;
                    }

                }
                if(flag==1){
                    res.append(index).append("  ").append(labelNow.toString()).append("\n");
                }
                index = DT.addDays(index,1);
            } catch (DateTimeException e) {
                e.printStackTrace();
            }
        }
        return res.toString();
    }

    /**
     * 获得当前排班表的开始时间
     *
     * @return 以长整型形式返回排班表的开始时间
     */
    public String getStartTime(){
        String start = startTime;
        return start;
    }

    /**
     * 获得当前排版表的结束时间
     *
     * @return 以长整型形式返回排班表的结束时间
     */
   public String getEndTime(){
        String end = endTime;
        return end;
   }

   @Override
   public double getFreeTimeRatio(){
        calcAPIs calc = new calcAPIs();
        DayWithLong DT = new DayWithLongImpl();
        double freeTimeRadio = 0;
        try {
           freeTimeRadio = calc.calcFreeTimeRatio(inter,DT.getInterval(startTime,endTime)+1);

        } catch (DateTimeException e) {
           e.printStackTrace();
           System.out.println("时间格式有误");
        }

        return freeTimeRadio;
   }

   @Override
   public String getFreeTime(){
       StringBuilder res = new StringBuilder();
        String index1 = startTime;
        DayWithLong DT = new DayWithLongImpl();
        Set<L> labels = inter.labels();
        res.append("以下时间还是空闲的哦:\n");
        int count = 0; //表示空闲时间段的日期长度
        while(true){
            try {
                if (index1.equals(DT.addDays(endTime, 1))) break;
            } catch (DateTimeException e) {
                e.printStackTrace();
            }
            try {
                int flag = 0;    //用来标识当前日期是否已经出现在某个时间段里面
                for(L label:labels){
                    long beginOfInterval = DT.getInterval(startTime,index1);
                    if(beginOfInterval<=inter.end(label) && beginOfInterval>=inter.start(label)){
                        flag = 1;
                    }
                }
                if(flag==0){
                    res.append(index1).append("\n");
                    count++;
                }
                index1 = DT.addDays(index1,1);
            } catch (DateTimeException e) {
                e.printStackTrace();
            }
        }
       if(count == 0){
           res.delete(0,res.lastIndexOf("\n")+1);
           res.append("所有时间都已经安排完毕!");
       }
       return res.toString();
   }

    @Override
    public boolean random(List<L> employees) {
       if(employees.isEmpty()){
           return false;
       }
        DayWithLong DT = new DayWithLongImpl();
        String index = startTime;
        Set<L> labels = inter.labels();
        for(L label : labels){
            inter.remove(label);   //全部删掉然后重新随机
        }
        while(true){
            try {
                int number = employees.size()-1;
                int ID = (int)(Math.random()*number);//随机取出一个员工
                long length = (int)(Math.random()*DT.getInterval(startTime,endTime));   //随机取长度
                if(number == 0){
                    inter.insert(DT.getInterval(startTime,index),DT.getInterval(startTime,endTime),employees.get(ID));
                    return true;
                }
                if(length<DT.getInterval(index,endTime)) {
                    inter.insert(DT.getInterval(startTime, index), length+DT.getInterval(startTime, index), employees.get(ID));
                    index = DT.addDays(index,length+1);
                }else if(length>=DT.getInterval(index,endTime) && number!=1){
                    inter.insert(DT.getInterval(startTime,index),DT.getInterval(startTime,endTime),employees.get(ID));
                    return true;
                }
                employees.remove(ID);

            } catch (IOException | DateTimeException e) {
                System.out.println("当前的时间段集合当中时间格式存在问题");
                System.out.println("请退出程序或者删除不正确的时间段");
            }
        }
    }
}
