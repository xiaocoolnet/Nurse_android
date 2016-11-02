package chinanurse.cn.nurse.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/7/5.
 */
public class Question_hashmap_data {
    //题目的id
    public static int question_answer_id = 0;
    //保存已打完的数据
    public static ArrayList<HashMap<String,Object>> hashmap_question = new ArrayList<HashMap<String,Object>>();
    public static Map<Integer,String> sheetmap = new HashMap<>();
    public static List<String> questionList = new ArrayList<>();
    public static List<String> answerList = new ArrayList<>();
    public static List<MyCollectQuestion_Bean.DataBean> MyCollectList = new ArrayList<>();
}
