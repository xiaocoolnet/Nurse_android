package chinanurse.cn.nurse.Fragment_Nurse_mine.my_collect_study.error.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chinanurse.cn.nurse.MinePage.MyStudy.MineStudt_question_error_Bean;
import chinanurse.cn.nurse.new_Activity.*;


/**
 * Created by Administrator on 2016/7/5.
 */
public class Question_hashmap_data_error {
    //题目的id
    public static int question_answer_id = 0;
    //保存已打完的数据
    public static ArrayList<HashMap<String,Object>> hashmap_question = new ArrayList<HashMap<String,Object>>();
    public static Map<Integer,String> sheetmap = new HashMap<>();
    public static List<String> questionList = new ArrayList<>();
    public static List<String> answerList = new ArrayList<>();
    public static List<MineStudt_question_error_Bean.DataBean> MyCollectList = new ArrayList<>();
}
