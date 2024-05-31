package entity;

public class Employee {
    private final String name;
    private final String job;
    private final String telephone;

    // AF
    // AF(name) = 职员的名字
    // AF(job) = 职员的职务
    // AF(telephone)

    // RI
    // 如果电话号码不为空，电话号码必须要按照11位的格式

    // safety from rep exposure
    // use private and final to modify fields
    // 使用不可变数据类型

    /**
     * 构造一个职员类
     *
     * @param name 职员的姓名
     * @param job 职员的职务
     * @param telephone 职员的电话号，按照11位的数字格式
     */
    public Employee(String name, String job, String telephone) {
        this.name = name;
        this.job = job;
        this.telephone = telephone;
        checkRep();
    }

    public void checkRep(){
        assert name!=null;
        assert job!=null;
        if (!telephone.equals("")) {
            assert telephone.length()==13;
            for(int i=0;i<13;i++){
                if(i==3 || i==8){
                    assert telephone.charAt(i) == '-';
                }else{
                    assert telephone.charAt(i)>='0';
                    assert telephone.charAt(i)<='9';
                }

            }
        }

    }
    /**
     *获取职员的姓名
     *
     * @return 职员的姓名
     */
    public String getName() {
        checkRep();
        return name;
    }

    /**
     * 获取职员的职务
     *
     * @return 职员的职务
     */
    public String getJob() {
        checkRep();
        return job;
    }

    /**
     * 获取职员的电话号码
     *
     * @return 获取职员的电话号码
     */
    public String getTelephone() {
        checkRep();
        return telephone;
    }

    @Override
    public String toString(){
        String res  = "";
        res = res + getName() + "  "+ getJob()+"  "+getTelephone();
        return res;
    }
}
