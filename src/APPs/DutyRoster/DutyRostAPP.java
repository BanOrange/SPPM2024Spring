package APPs.DutyRoster;

import APIs.DutyIntervalSet.DayWithLong;
import APIs.DutyIntervalSet.DayWithLongImpl;
import APIs.DutyIntervalSet.DutyIntervalSet;
import APPs.PaeserForDuty;
import entity.Employee;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.text.ParseException;
import java.time.DateTimeException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DutyRostAPP implements IDutyRosterAPP {
    private DutyIntervalSet<Employee> duty;
    private final List<Employee> Employees = new ArrayList<>();
    private String startTime;
    private String endTime;
//    private final List<Integer> arrange= new ArrayList<>();

    // AF
    // AF(Employees) = 所有员工信息
    // AF(startTime) = 排班表的开始时间
    // AF(endTime) = 排班表的结束时间
    // AF(duty) = 排班表
    // AF(arrange) = 用于标示每一个日期是否已经安排完毕

    // RI
    // 构造duty之后要满足duty的相关RI，已被duty自身的checkRep满足
    // 员工被删除之后，排班信息也要对应删除
    // 排班信息中的所有员工必须已经在集合之中
    // 员工的姓名不能重复

    // safety from rep exposure
    // 使用private和final修饰字段
    // 必要时采用防御式拷贝

    public void checkRep(){

        //duty为空说明还没开始构造，那么就不可能有排班记录，自然不会有非员工集合中的员工出现在员工集合之中
        if(duty!=null) {
            //确保排班记录中的所有记录对应的员工都在员工集合之中
            Set<Employee> labels = this.duty.labels();
            for (Employee e : labels) {
                assert Employees.contains(e);
            }
        }
        //员工姓名不能重复
        for(Employee e1 : Employees){
            for(Employee e2 :Employees){
                if(e1!=e2){
                    assert !e1.getName().equals(e2.getName());
                }
            }
        }

    }

    /**
     * 无参构造方法
     */
    public DutyRostAPP(){checkRep();};

    /**
     * 提供开始时间和结束时间的构造方法
     * @param startTime 排班表的开始时间
     * @param endTime 排班表的结束时间
     */
    public DutyRostAPP(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

        this.duty = new DutyIntervalSet<>(startTime,endTime);

        checkRep();
    }

    @Override
    public boolean setTime(String startTime,String endTime) throws DateTimeException {
        DayWithLong DT = new DayWithLongImpl();

        long length = DT.getInterval(startTime,endTime);
        System.out.println(length);
        if(length<0){
            return false;
        }

        this.startTime = startTime;
        this.endTime = endTime;
        duty = new DutyIntervalSet<>(startTime,endTime);
        checkRep();
        return true;
    }

    @Override
    public boolean addEmployee(String name, String job, String telephone) {
        String regEx = "[0-9]{3}-[0-9]{4}-[0-9]{4}";  //正则表达式，匹配电话号码的格式
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(telephone);
        if(matcher.matches()){
            Employee e = new Employee(name,job,telephone);
            Employee eRe = new Employee("","","");
            for(Employee e1 :  Employees){
                if(e1.getName().equals(e.getName())){
                    eRe = e1;
                }
            };
            Employees.remove(eRe);
            Employees.add(e);
            return true;
        }else{
            System.out.println("手机号格式错误");
            return false;
        }
    }

    @Override
    public boolean addDuty(String start, String end, String name) throws IOException {
        Employee E = new Employee("","","");
        for(Employee e: Employees){
            if(e.getName().equals(name)){
                E=e;
                break;
            }
        }
        if(E.getName().equals("")){
            throw new IOException();  //查无此人
        }

        DayWithLong DT = new DayWithLongImpl();
        long startL = DT.getInterval(this.startTime,start);
        long endL = DT.getInterval(this.startTime,end);
        duty.insert(startL,endL,E);
        checkRep();
        return true;

    }

    @Override
    public boolean addEmployee(Employee e) {
        this.addEmployee(e.getName(),e.getJob(),e.getTelephone());
        return true;
    }

    @Override
    public void remove(String name) {
        int flag = 0;
        Employee E = new Employee("","","");
        for(Employee e:Employees){
            if(e.getName().equals(name)){
                E = e;
                flag = 1;
                break;
            }
        }
        if(flag == 0){
            System.out.println("想要删除的人不存在");
            return;
        }
        duty.remove(E);
        Employees.remove(E);
        checkRep();
    }

    @Override
    public boolean full() {
        if(duty.getFreeTime().equals("所有时间都已经安排完毕!")){
            return true;
        }
        return false;
    }

    @Override
    public void readFile(String filePath) throws IOException, DateTimeException {
        //先把文件里面的所有文本全部整合到一个字符串中方便正则表达式
        String allStr = "";
        try {
            File f = new File(filePath);
            if(f.exists()){
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String line = "";
                StringBuilder sb = new StringBuilder();
                while((line = br.readLine()) !=null){
                    sb.append(line);
                }
                br.close();;
                fr.close();
                allStr = sb.toString();
            }
        } catch (FileNotFoundException e) {
            System.out.println("未找到该文件");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("缓冲流关闭失败");
            e.printStackTrace();
        }

        //下面使用正则表达式来将文本进行解析
        PaeserForDuty paeserForDuty = new PaeserForDuty();
        List<String> kindList = paeserForDuty.threeKind(allStr);
        if(kindList.size()<3){
            throw new IOException();
        }
        //首先解析Period
        Pattern periodP = Pattern.compile("(.*)[,](.*)");  //开始时间,结束时间
        Matcher periodM = periodP.matcher(kindList.get(0));
        if(periodM.find()){
            this.startTime = periodM.group(1);
            this.endTime = periodM.group(2);
            this.setTime(startTime,endTime);
        }
        //接下来添加员工信息
        String rex = "\\s*(\\w+)\\{([^,]+),(\\d{3})-(\\d{4})-(\\d{4})}";
        Pattern employeeP = Pattern.compile(rex);
        Matcher employeeM = employeeP.matcher(kindList.get(1));
        while(employeeM.find()){
            String telephone = employeeM.group(3)+'-'+employeeM.group(4)+'-'+employeeM.group(5);
            Employee employee = new Employee(employeeM.group(1),employeeM.group(2),telephone);
            Employees.add(employee);
        }
        //接下来添加排班信息
        String rex1="\\s*(\\w+)\\{(\\d{4})-(\\d{2})-(\\d{2}),(\\d{4})-(\\d{2})-(\\d{2})\\}";
        Pattern rosterP = Pattern.compile(rex1);
        Matcher rosterM = rosterP.matcher(kindList.get(2));
        while(rosterM.find()){
            String name = rosterM.group(1);
            String startTime = rosterM.group(2)+ '-'+rosterM.group(3)+'-'+rosterM.group(4);
            String endTime = rosterM.group(5)+ '-'+rosterM.group(6)+'-'+rosterM.group(7);
            this.addDuty(startTime,endTime,name);
        }
    }

    @Override
    public String getJob(String name) {
        String job = "";
        for(Employee e:Employees){
            if(e.getName().equals(name)){
                job = e.getJob();
                return job;
            }
        }
        return job;
    }

    @Override
    public String telephone(String name) {
        String telephone = "";
        for(Employee e:Employees){
            if(e.getName().equals(name)){
                telephone = e.getTelephone();
                return telephone;
            }
        }
        return telephone;
    }

    @Override
    public String showEmployee() {
        StringBuilder res = new StringBuilder();
        for(Employee e : Employees){
            res.append(e.toString()+"\n");
        }
        return res.toString();
    }

    @Override
    public double getFreeTimeRatio() {
        double freeTimeRatio = duty.getFreeTimeRatio();
        String res = ("排班表的起止时间为")+startTime+"---"+endTime+"\n";
        res = res + "以下是已经安排的时间段\n";
        if(duty.toString().equals(res)){
            System.out.println("还未填入排班记录");
            return -1;
        }
        return freeTimeRatio;
    }

    @Override
    public String getFreeTime() {
        String res = "";
        res = duty.getFreeTime();
        return res;
    }

    @Override
    public void random(){
        if(startTime!=null && endTime!=null){
            List<Employee> employees = new ArrayList<>();
            for(Employee e : Employees){
                employees.add(e);
                duty.remove(e);   //将duty中的段全部删掉
            }
            if (duty.random(employees) == false){            //为duty随机安排值班人员，如果失败的话提示用户
                System.out.println("安排失败，请检查员工列表不能为空或存在其他问题");
            }

        }else {
            System.out.println("您还没有设定起止时间");
        }
    }

    @Override
    public String toString(){
        String res = duty.toString();
        return res;
    }


    public static void main(String[] args) {
        DutyRostAPP app = new DutyRostAPP();
        Scanner scanner = new Scanner(System.in);
        boolean SetState = false;  //用来表示时间设置有没有成功
        while (!SetState) {

            System.out.println("请先输入排班表的开始时间（yyyy-MM-dd）:");
            String startTime = scanner.next();
            System.out.println("请输入排班表的结束时间（格式同上）：");
            String endTime = scanner.next();
            try {
                if (app.setTime(startTime, endTime) == true) {
                    SetState = true;
                }else{
                    System.out.println("您输入的起止日期先后顺序有问题，结束时间应该晚于开始时间");
                }
            } catch (DateTimeException e) {
                System.out.println("您输入的日期格式存在错误或者日期不正常，请重新输入");
            }
        }

//        int flag =1;   //用于表示是否输入完毕
//        System.out.println("请输入人员信息，注意不能重名，格式为姓名,职位,电话号码（xxx-xxxx-xxxx）:");
//        while(flag == 1){
//            String str = scanner.next();
//            Pattern employeeP = Pattern.compile("(.*)[,](.*)[,](\\d{3})-(\\d{4})-(\\d{4})");
//            Matcher employeeM = employeeP.matcher(str);
//            if(employeeM.find()){
//                String name = employeeM.group(1);
//                String job = employeeM.group(2);
//                String telephone = employeeM.group(3)+"-"+employeeM.group(4)+"-"+employeeM.group(5);
//
//                app.addEmployee(name,job,telephone);
//            }else{
//                flag=0;
//            }
//        }
//        System.out.println("当前员工信息如下:");
//        System.out.println(app.showEmployee());
//        System.out.println("请问是否需要删除某些员工？:Y/N");
//        if(scanner.next().equals("Y")){
//            int flag1 = 1;
//            while(flag1 == 1){
//                System.out.println("请输入要删除的员工名称(输入-1为停止删除)：");
//                String name = scanner.next();
//                app.remove(name);
//                if(name.equals("-1")) flag1 = 0;
//            }
//        }
//        System.out.println("当前排班表如下所示：");
//        System.out.println(app.toString());
//        System.out.println("本系统可帮您自动随机排班，是否需要：Y/N");
//        if(scanner.next().equals("Y")){
//            app.random();
//            System.out.println(app.toString());
//            return;
//        }
//        while(!app.full()){
//            System.out.println("请输入排班记录，格式为：姓名，开始时间，结束时间 日期的格式为yyyy-MM-dd");
//            String lineStr = scanner.next();
//            String rex = "(\\w+)[,](\\d{4})-(\\d{2})-(\\d{2})[,](\\d{4})-(\\d{2})-(\\d{2})";
//            Pattern rosterP = Pattern.compile(rex);
//            Matcher rosterM = rosterP.matcher(lineStr);
//            if(rosterM.find()){
//                String name = rosterM.group(1);
//                String start = rosterM.group(2)+rosterM.group(3)+rosterM.group(4);
//                String end = rosterM.group(5)+rosterM.group(6)+rosterM.group(7);
//                try {
//                    app.addDuty(start,end,name);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        System.out.println("当前的排班表的空闲比例为:"+app.getFreeTimeRatio());
//        System.out.println(app.toString());


        int op = 1;
        System.out.println("菜单" +
                "退出程序0\n" +
                "增加员工1\n" +
                "删除员工2\n" +
                "安排排班3\n" +
                "查看空闲时间比例和空闲时段4\n" +
                "自动排班5\n" +
                "展示当前排班表6\n" +
                "展示员工列表7\n" +
                "读取文件8\n");
        while(op!=0){
            System.out.print("请选择您想执行的指令:");
            op = Integer.parseInt(scanner.next());
            switch(op){
                case 0:
                    System.out.println("最后您的排班表为:");
                    if(app.getFreeTimeRatio()==-1){
                        return;
                    }else{
                        System.out.println("请注意您的排班表未满即退出");
                        return;
                    }
                case 1:
                    int flag1 =1;   //用于表示是否输入完毕
                    System.out.println("请输入人员信息，注意不能重名，格式为姓名,职位,电话号码（xxx-xxxx-xxxx）:");
                    while(flag1 == 1){
                        String str = scanner.next();
                        Pattern employeeP = Pattern.compile("(.*)[,](.*)[,](\\d{3})-(\\d{4})-(\\d{4})");
                        Matcher employeeM = employeeP.matcher(str);
                        if(employeeM.find()){
                            String name = employeeM.group(1);
                            String job = employeeM.group(2);
                            String telephone = employeeM.group(3)+"-"+employeeM.group(4)+"-"+employeeM.group(5);

                            app.addEmployee(name,job,telephone);
                        }else{
                            flag1=0;
                        }
                    }
                    break;
                case 2:
                    int flag2 = 1;
                    while(flag2 == 1){
                        System.out.println("请输入要删除的员工名称(输入-1为停止删除)：");
                        String name = scanner.next();
                        app.remove(name);
                        if(name.equals("-1")) flag2 = 0;
                    }
                    break;
                case 3:
                    int flag3 = 1;
                    while(flag3 != 0){
                        System.out.println("请输入排班记录，格式为：姓名，开始时间，结束时间 日期的格式为yyyy-MM-dd");
                        String lineStr = scanner.next();
                        String rex = "(\\w+)[,](\\d{4})-(\\d{2})-(\\d{2})[,](\\d{4})-(\\d{2})-(\\d{2})";
                        Pattern rosterP = Pattern.compile(rex);
                        Matcher rosterM = rosterP.matcher(lineStr);
                        if(rosterM.find()){
                            String name = rosterM.group(1);
                            String start = rosterM.group(2)+"-"+rosterM.group(3)+"-"+rosterM.group(4);
                            String end = rosterM.group(5)+"-"+rosterM.group(6)+"-"+rosterM.group(7);
                            try {
                                app.addDuty(start,end,name);
                            } catch (IOException e) {
                                System.out.println("您输入系统的排班时间有误");
                            }
                        }else{
                            flag3=0;
                        }
                    }
                    break;
                case 4:
                    System.out.println("空闲时间比例为："+app.getFreeTimeRatio());
                    System.out.println(app.getFreeTime());
                    break;
                case 5:
                    app.random();
                    System.out.println(app.toString());
                    break;
                case 6:
                    System.out.println(app.toString());
                    break;
                case 7:
                    System.out.println(app.showEmployee());
                    break;
                case 8:
                    System.out.println("请输入您想读取的文件（完整地址）:");
                    String file = scanner.next();
                    try {
                        app.readFile(file);
                    } catch (IOException | DateTimeException e) {
                        System.out.println("您输入的地址有误或者文件内部文本格式错误，无法读取该文件");
                    }
                    break;
                default:
                    System.out.println("不存在该指令");
                    break;

            }
        }
    }
}











































//System.out.println("欢迎使用本排班系统");
//        String menu = "操作菜单\n" +
//                "1.新增员工\n" +
//                "2.删除员工（不能存在于排班表中）\n" +
//                "3.增加排班记录\n" +
//                "4.删除排班记录" +
//                "5.自动排班\n" +
//                "6.展示当前排班表\n" +
//                "7.获取空闲时间段和空闲时间比例\n" +
//                "8.调取操作菜单\n" +
//                "9.退出系统\n"
//        System.out.println(menu);
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("请先设定排班表开始时间和结束时间：");
//        System.out.println("注：系统中所有时间的格式均为yyyy-MM-dd");
//        System.out.println("开始时间：");
//        String startTime = scanner.next();
//        System.out.println("结束时间：");
//        String endTime = scanner.next();
//        duty = new DutyIntervalSet<>(startTime,endTime);
//
//        int op = 0;  //操作码，允许用户选择想要执行的操作
//        while(op!=9 ) {
//            switch (op) {
//                case 0:
//                    System.out.println("请输入您想执行的操作");
//                    op = Integer.parseInt(scanner.next());
//                    break;
//                case :
//
//            }
//        }