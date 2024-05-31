package APPs;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaeserForDuty {
    //利用正则表达式将文本分离

    /**
     * 将文本切割成三部分
     *
     * @param allStr 由一整个文本构成的字符串
     * @return 被分割为三个字符串的文本组合成的List
     */
    public List<String> threeKind(String allStr){
        List<String> kindList = new ArrayList<>();
        //根据三个部分的不同次序，一共有六种可能，全部写出来
        String kind1 = "^Period\\{(.*)}Roster\\{(.*)}Employee\\{(.*)}";
        String kind2 = "^Period\\{(.*)}Employee\\{(.*)}Roster\\{(.*)}";
        String kind3 = "^Roster\\{(.*)}Employee\\{(.*)}Period\\{(.*)}";
        String kind4 = "^Roster\\{(.*)}Period\\{(.*)}Employee\\{(.*)}";
        String kind5 = "^Employee\\{(.*)}Roster\\{(.*)}Period\\{(.*)}";
        String kind6 = "^Employee\\{(.*)}Period\\{(.*)}Roster\\{(.*)}";

        //用不同的matcher来执行这些正则表达式
        Pattern pattern1 = Pattern.compile(kind1);
        Pattern pattern2 = Pattern.compile(kind2);
        Pattern pattern3 = Pattern.compile(kind3);
        Pattern pattern4 = Pattern.compile(kind4);
        Pattern pattern5 = Pattern.compile(kind5);
        Pattern pattern6 = Pattern.compile(kind6);

        Matcher matcher1 = pattern1.matcher(allStr);
        Matcher matcher2 = pattern2.matcher(allStr);
        Matcher matcher3 = pattern3.matcher(allStr);
        Matcher matcher4 = pattern4.matcher(allStr);
        Matcher matcher5 = pattern5.matcher(allStr);
        Matcher matcher6 = pattern6.matcher(allStr);
        //将能够与文本匹配的matcher分割出来的三部分分别放入group之中方便后续使用
        //并且第一个位置放period，第二个位置放Employee，第三个位置放Roster
        if(matcher1.find()){
            kindList.add(matcher1.group(1));
            kindList.add(matcher1.group(3));
            kindList.add(matcher1.group(2));
            return kindList;
        }else if(matcher2.find()){
            kindList.add(matcher2.group(1));
            kindList.add(matcher2.group(2));
            kindList.add(matcher2.group(3));
            return kindList;
        }else if(matcher3.find()){
            kindList.add(matcher3.group(3));
            kindList.add(matcher3.group(2));
            kindList.add(matcher3.group(1));
            return kindList;
        }else if(matcher4.find()){
            kindList.add(matcher4.group(2));
            kindList.add(matcher4.group(3));
            kindList.add(matcher4.group(1));
            return kindList;
        }else if(matcher5.find()){
            kindList.add(matcher5.group(3));
            kindList.add(matcher5.group(1));
            kindList.add(matcher5.group(2));
            return kindList;
        }else if(matcher6.find()){
            kindList.add(matcher6.group(2));
            kindList.add(matcher6.group(1));
            kindList.add(matcher6.group(3));
            return kindList;
        }

        return kindList;
    }
}
