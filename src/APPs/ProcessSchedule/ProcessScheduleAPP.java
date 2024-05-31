package APPs.ProcessSchedule;


import APIs.ProcessIntervalSet.ProcessIntervalSet;
import IntervalSet.IntervalSet;
import entity.Process;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessScheduleAPP {
    private final ProcessIntervalSet<Process> intervals = new ProcessIntervalSet<Process>();
    private final List<Process> allProcess = new ArrayList<>();
    private final Map<Process, Boolean> processMap = new HashMap<>();

    //AF
    //AF(intervals) = 进程的安排表
    //AF(allProcess) = 所有的进程组成的集合
    //AF(processMap) = 进程的状态（是否已经运行完毕）

    //RI
    //进程安排表中的所有进程必须存在于进程集合之中
    //进程的ID不能重复
    //进程安排表初始化后，满足进程安排表的RI

    //rep safety form exposure
    //使用private和final修饰字段
    //必要时使用防御式编程

    /**
     * 检查RI是否得到满足
     */
    public void checkRep() {
        for(Process p1:allProcess){
            for(Process p2:allProcess){
                if(!p1.equals(p2)){
                    assert p1.getID()!=p2.getID();
                }
            }
        }
        for (Process p : intervals.labels()) {
            assert allProcess.contains(p);
        }
        intervals.checkRep();
    }

    /**
     * 向系统中添加进程
     *
     * @param p 添加的进程，不能和已有进程重复
     * @throws IOException 如果ID重复则抛出异常
     */
    public void addProcess(Process p) throws IOException{
        for(Process p1 : allProcess){
            if(p1.getID()==p.getID()){
                throw new IOException();
            }
        }
        allProcess.add(p);
        processMap.put(p, false);
        checkRep();
    }

    /**
     * 随机安排进程
     *
     * @return 如果安排成功了则返回true，反之返回false
     */
    public boolean random() {
        long start = 0;

        while (true) {     //循环随机选取元素，直到所有进程都已经执行完毕
            int flag = 0;  //标识是否所有的进程都执行完毕
            int index = (int) (Math.random() * allProcess.size());
            Process p = allProcess.get(index);
            if (processMap.get(p) == false) {
                long minTime = p.getStartTime();
                long maxTime = p.getEndTime();
                long OverTime = 0;
                IntervalSet<Integer> OverTimes = intervals.intervals(p);
                int count = 0;
                while (OverTimes.start(count) != -1) {
                    OverTime = OverTime + OverTimes.end(count) - OverTimes.start(count);
                    count++;
                }
                int time = (int) (Math.random() * (maxTime - OverTime));
                if(time == 0){    //如果正好抽中时间为0，那么变为1，避免瞬间结束的进程
                    time =1;
                }
                if(time+OverTime<=maxTime) {      //要小于等于最长执行时间
                    if (time + OverTime >= minTime) {
                        processMap.put(p, true);
                    }
                    try {
                        intervals.insert(start, (long) time + start, p);
                    } catch (IOException e) {
                        System.out.println("输入的进程时间有误");
                    }
                    start = start + time;
                }
            }
            for (Process p1 : processMap.keySet()) {
                if (processMap.get(p1) == false) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                return true;
            }
        }
    }

    /**
     * 优化策略安排进程
     */
    public void optimize(){
        long start = 0;
        int flag = 0;
        while(flag == 0){
            int flag1 = 0;
            Process P = null;
            for(Process p1 : this.allProcess){
                if(processMap.get(p1)==false){
                    P = p1;
                }
            }
            for(Process p : this.allProcess){
                if(processMap.get(p) == false){
                    long OverTimeP = 0;
                    IntervalSet<Integer> POverTimes = intervals.intervals(P);
                    int countP = 0;
                    while (POverTimes.start(countP) != -1) {
                        OverTimeP = POverTimes.end(countP) - POverTimes.start(countP);
                    }
                    long OverTimep = 0;
                    IntervalSet<Integer> pOverTimes = intervals.intervals(p);
                    int countp = 0;
                    while (pOverTimes.start(countp) != -1) {
                        OverTimep = pOverTimes.end(countp) - pOverTimes.start(countp);
                    }
                    if(P.getStartTime()-OverTimeP > p.getStartTime() - OverTimep){   //选取需要最少时间就能完成的进程
                        P = p;
                    }
                }
            }

            long OverTimeP = 0;
            IntervalSet<Integer> POverTimes = intervals.intervals(P);
            int countP = 0;
            while (POverTimes.start(countP) != -1) {
                OverTimeP = POverTimes.end(countP) - POverTimes.start(countP);
            }
            long length = P.getStartTime()-OverTimeP;  //完成P进程需要的时间
            try {
                intervals.insert(start,start+length,P);
            } catch (IOException e) {
                System.out.println("输入的进程时间有误");
            }
            processMap.put(P,true);
            start = length+start;
            for(Process p :processMap.keySet()){
                if(processMap.get(p) == false){
                    flag1 = 1;
                }
            }
            if(flag1 == 0){
                flag = 1;
            }
        }
    }

    /**
     *
     */
    @Override
    public String toString(){
        StringBuilder res =new StringBuilder();
        res.append("ID\t进程名称\t开始时间\t结束时间\t\n");
        Set<Process> process = intervals.labels();
        for(Process p : allProcess){
            if(process.contains(p)) {
                IntervalSet<Integer> interval = intervals.intervals(p);
                int count = 0;
                while (interval.start(count) != -1) {
                    res.append(p.getID()).append("\t").append(p.getName()).append("\t").append(interval.start(count)).append("\t")
                            .append(interval.end(count)).append("\n");
                    count++;
                }
            }

        }
        return res.toString();
    }

    /**
     * 展示当前系统中包含的所有进程信息
     * @return 以字符串的形式返回所有的进程信息
     */
    public String showProcess(){
        StringBuilder res = new StringBuilder();
        res.append("ID\t").append("名称\t").append("最短执行时间\t").append("最长执行时间\t\n");
        for(Process p : allProcess){
            res.append(p.getID()).append("\t").append(p.getName()).append("\t").append(p.getStartTime()).append("\t").append(p.getEndTime()).append("\n");
        }
        return res.toString();
    }

    /**
     * 返回某一时刻之前的进程调度结果和该时刻正在执行的进程
     *
     * @param time 某一时刻，不能小于0
     * @return 一个字符串，包含该时刻之前的进程调度结果和当前时刻正在执行的进程
     */
    public String display(long time){
        StringBuilder res =new StringBuilder();
        res.append("ID\t进程名称\t开始时间\t结束时间\t\n");
        for(Process p : allProcess){
            if(intervals.labels().contains(p)) {
                IntervalSet<Integer> interval = intervals.intervals(p);
                int count = 0;
                while (interval.start(count) != -1) {
                    if (interval.end(count) < time) {
                        res.append(p.getID()).append("\t").append(p.getName()).append("\t")
                                .append(interval.start(count)).append("\t").append(interval.end(count)).append("\n");
                    }
                    count++;
                }
            }
        }
        res.append("当前执行的进程\n");
        for(Process p : allProcess){
            if(intervals.labels().contains(p)) {
                IntervalSet<Integer> interval = intervals.intervals(p);
                int count = 0;
                while (interval.start(count) != -1) {
                    if (interval.end(count) > time && interval.start(count) <= time) {
                        res.append(p.getID()).append("\t").append(p.getName()).append("\t")
                                .append(interval.start(count)).append("\t").append(interval.end(count)).append("\n");
                    }
                    count++;
                }
            }
        }
        return res.toString();
    }

    public static void main(String args[]) throws IOException {
        ProcessScheduleAPP app = new ProcessScheduleAPP();
        Scanner scanner = new Scanner(System.in);
        boolean inputFlag = true;  // 用于表示进程信息输入是否结束
        while(inputFlag){
            System.out.println("请输入进程信息（ID，名称，最短执行时间，最长执行时间）:");
            String lineStr = scanner.next();
            Pattern p = Pattern.compile("(.*)[,](.*)[,](.*)[,](.*)");
            Matcher m = p.matcher(lineStr);
            if(m.find()){
                int ID = Integer.parseInt(m.group(1));
                String name = m.group(2);
                long startTime = Long.parseLong(m.group(3));
                long endTime = Long.parseLong(m.group(4));
                try {
                    if(name!=null && startTime>0 && endTime>0 && endTime>startTime){
                        Process process = new Process(ID,name,startTime,endTime);
                        app.addProcess(process);
                    }else{
                        System.out.println("您输入的进程信息存在问题，请重新输入");
                        continue;
                    }
                } catch (IOException e) {
                    System.out.println("您输入的进程信息有误，请检查格式或内容");
                }
            }else{
                inputFlag = false; //进程写入完毕
            }
        }
        System.out.println("需要执行的进程信息如下");
        System.out.println(app.showProcess());
        System.out.println("是否需要优化进程安排表(Y/N):");
        if(scanner.next().equals("Y")){
            app.optimize();
            System.out.println(app.toString());
        }else{
            app.random();
            System.out.println(app.toString());
        }
        System.out.println("请输入您想查看的时间（展示此时间及之前的所有操作执行过程）:");
        long end = Long.valueOf(scanner.next());
        System.out.println(app.display(end));

    }
}
