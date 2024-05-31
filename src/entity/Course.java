package entity;

public class Course {
    private final int ID;
    private final String courseName;
    private final String teacherName;
    private final String location;

    //AF
    //AF(ID) = 课程ID
    //AF(courseName) = 课程名称
    //AF(TeacherName) = 教师名称
    //AF(location) = 地点

    //RI
    //不能为空

    // safety from rep exposure
    // use private and final to modify fields
    // 使用不可变的数据类型

    /**
     * 构造方法
     *
     * @param ID 课程ID
     * @param courseName 课程名称
     * @param teacherName 教师名称
     * @param location 地点
     */
    public Course(int ID, String courseName, String teacherName, String location) {
        this.ID = ID;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.location = location;
        checkRep();
    }

    /**
     * 检查RI是否得到满足
     */
    public void checkRep(){
        assert courseName!=null;
        assert teacherName!=null;
        assert location!=null;
    }

    /**
     * 得到课程ID
     * @return 课程ID
     */
    public int getID() {
        checkRep();
        return ID;
    }

    /**
     * 得到课程名称
     * @return 课程名称
     */
    public String getCourseName() {
        checkRep();
        return courseName;
    }

    /**
     * 得到教师名称
     * @return 教师名称
     */
    public String getTeacherName() {
        checkRep();
        return teacherName;
    }

    /**
     * 得到地点
     * @return 地点
     */
    public String getLocation() {
        checkRep();
        return location;
    }

    @Override
    public String toString(){
        String res = "";
        res = res + getCourseName()+ "  " + getID() + "  " + getTeacherName()+"  "+getTeacherName();
        return res;
    }
}
