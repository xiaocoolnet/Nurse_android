package chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext.adapter;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext.MyCollect_Question_Activity;
import chinanurse.cn.nurse.MinePage.MyCollect.MyCollect_question_answer_anapdter;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.ErrorQuestion_bean;
import chinanurse.cn.nurse.bean.MyCollectQuestion_Bean;
import chinanurse.cn.nurse.bean.Question_hashmap_data;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.new_Activity.ConstantUtil;
import chinanurse.cn.nurse.new_Activity.SaveQuestionInfo;
import chinanurse.cn.nurse.popWindow.Pop_Answer_Sheet;
import chinanurse.cn.nurse.popWindow.Pop_Aswer;

/**
 * 我的收藏---试题适配器
 */
public class MyCollectQuestion_Adapter extends PagerAdapter implements OnClickListener {

    MyCollect_Question_Activity mContext;
    // 传递过来的页面view的集合
    List<View> viewItems;
    // 每个item的页面view
    View convertView;
    // 传递过来的所有数据
    List<MyCollectQuestion_Bean.DataBean> answerItems;
    //错题容器
    ErrorQuestion_bean.DataEntity errorQuestionInfo;
    ErrorQuestion_bean.DataEntity.AnswerlistEntity answer_list;

    private Pop_Aswer pop_aswer;
    private Pop_Answer_Sheet pop_aswer_sheet;
    private int barNumber = 4;
    Integer allpageNum;
    String answer_description;
    int answer_difficulty;

    String imgServerUrl = "";

    private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> mapClick = new HashMap<Integer, Boolean>();
    //    private Map<Integer, String> mapMultiSelect = new HashMap<Integer, String>();
    private Map<String, Object> mapMultiSelect = new HashMap<String, Object>();
    private List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();

    boolean isNext = false;

    StringBuffer answer = new StringBuffer();
    StringBuffer answerLast = new StringBuffer();
    StringBuffer answer1 = new StringBuffer();

    private int collectNum = 1;
    private static final int GETTESTTEXT = 1;
    private static final int SETCOLLECT = 2;
    private static final int CANCELCOLLECT = 3;
    final ViewHolder holder = new ViewHolder();

    int rbtnoneNum_answer = 1, rbtnoneNum_sheet = 1;
    String isCorrect = ConstantUtil.isCorrect;//1对，0错

    private int errortopicNum = 0;
    private int RightPosition;
    private UserBean user;
    String questionType;
    String questionNum;
    private  int gradview_position;

    String resultA = "";
    String resultB = "";
    String resultC = "";
    String resultD = "";
    String resultE = "";

    int currentNumber;
    private String collectTag;


    public MyCollectQuestion_Adapter(MyCollect_Question_Activity context, List<View> viewItems, List<MyCollectQuestion_Bean.DataBean> dataItems, String questionType, String questionNum, int mposition) {
        mContext = context;
        this.viewItems = viewItems;
        this.answerItems = dataItems;
        this.questionType = questionType;
        this.questionNum = questionNum;
        this.gradview_position = mposition;//获取listview点击的值

        user = new UserBean(context);
        pop_aswer = new Pop_Aswer(mContext);
        pop_aswer_sheet = new Pop_Answer_Sheet(mContext);
        allpageNum = Integer.valueOf(questionNum);
    }

    @Override
    public int getCount() {
        if (viewItems == null)
            return 0;
        return viewItems.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewItems.get(position));
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
            currentNumber = position;
            convertView = viewItems.get(position);
            //显示选项列表
            holder.listview = (ListView) convertView.findViewById(R.id.question_answer_list);
            //显示单选还是多选
            holder.question_title = (TextView) convertView.findViewById(R.id.question_title_score_text);
            //显示当前页面和总页面
            holder.present_topic = (TextView) convertView.findViewById(R.id.quedtion_at_present_topic);
//        //得到正确答案
//        for ( i = 0; i < answerItems.get(position).getAnswerlist().size(); i++) {
//            if ("1".equals(answerItems.get(position).getAnswerlist().get(i).getIsanswer())) {
//                holder.correct_answer = holder.option[i];
//            }
//        }
//            if ("1".equals(type)){
                holder.present_topic.setText(position + 1 + "/" + answerItems.size());
//            }else if("11".equals(type)){
//                holder.question_test.setText(position + 1 + "/" + answerItems.size());
//                holder.question_test.setVisibility(View.VISIBLE);
//                holder.present_topic.setText(minute+":"+second);
//                startTime();
//            }

            //上一页按钮
            holder.ril_last_question = (RelativeLayout) convertView.findViewById(R.id.ril_last_question);
            //下一页按钮
            holder.ril_next_question = (RelativeLayout) convertView.findViewById(R.id.ril_next_question);
            //收藏按钮
            holder.rbtn_collect = (Button) convertView.findViewById(R.id.rbtn_collect);
            //答案详情
            holder.btn_answer = (Button) convertView.findViewById(R.id.btn_answer);
            //答题卡
            holder.btn_answer_sheet = (Button) convertView.findViewById(R.id.btn_answer_sheet);
            //答题题目
            holder.question_title = (TextView) convertView.findViewById(R.id.question_title);
//            if(answerItems.get(position).getTitle().toString()==null){
//                holder.question_title.setText(answerItems.get(position).getTitle().toString()+"");
//            }else{
                holder.question_title.setText(answerItems.get(position).getTitle().toString());
//            }
            holder.adapter = new MyCollect_question_answer_anapdter(mContext, answerItems, position);
            collectTag = "collectTag"+currentNumber;
            holder.rbtn_collect.setTag(collectTag);
            holder.listview.setAdapter(holder.adapter);
            setListViewHeightBasedOnChildren(holder.listview);
            if(position==1){
                mContext.isCollect();
            }
            holder.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int lv_position, long id) {
                    //得到正确答案
                    for (int i = 0; i < answerItems.get(position).getAnswerslist().size(); i++) {
                        if ("1".equals(answerItems.get(position).getAnswerslist().get(i).getIsanswer())) {
                            holder.correct_answer = holder.option[i];
                            RightPosition = i;
                            //得到正确答案的描述
                            answer_description = answerItems.get(position).getDescription().toString();
                            //是个null，需要接口正确了再改回来
//                            answer_difficulty = Integer.valueOf(answerItems.get(position).getPost_difficulty().toString());
                            answer_difficulty =1;
                        }
                    }
                    View v = parent.getChildAt(RightPosition);
                    if (v != null){
                        v.findViewById(R.id.ril_choose_option).setBackgroundResource(R.drawable.button_boder_green);
                    }

                    //我选择的答案
                    holder.answer_mine = holder.option[lv_position];
                    holder.answer_history = (LinearLayout) view.findViewById(R.id.ril_choose_option);
                    //画外框
                    if (((ListView) parent).getTag() != null) {
                        ((View) ((ListView) parent).getTag()).findViewById(R.id.ril_choose_option).setBackgroundResource(R.drawable.button_boder);
                    }
                    ((ListView) parent).setTag(view);
                    //判断题目的对错
                    if ("1".equals(answerItems.get(position).getAnswerslist().get(lv_position).getIsanswer())) {
                        holder.answer_mine = ((TextView) view.findViewById(R.id.question_option)).getText().toString();
                        holder.answer_history.setBackgroundResource(R.drawable.button_boder_green);
                        //正确
                        isCorrect = ConstantUtil.isCorrect;
                        //答案正确的状态
                        holder.answer_type = "1";

                        //宝宝写的静态类 填写题目的正确或错误状态
                        if (!Question_hashmap_data.sheetmap.containsKey(position)) {
                            Question_hashmap_data.sheetmap.put(position, "1");
                        }
                    } else if ("0".equals(answerItems.get(position).getAnswerslist().get(lv_position).getIsanswer().toString())) {
                        holder.answer_history.setBackgroundResource(R.drawable.button_boder_red);
                        //错误
                        isCorrect = ConstantUtil.isError;
                        //错误状态
                        holder.answer_type = "0";
                        errortopicNum += 1;
                        errorQuestionInfo = new ErrorQuestion_bean.DataEntity();
                        answer_list = new ErrorQuestion_bean.DataEntity.AnswerlistEntity();
                        errorQuestionInfo.setPost_title(answerItems.get(position).getPost_title() + "");
                        errorQuestionInfo.setCurrent_answer(RightPosition + "");//正确选项
                        errorQuestionInfo.setPost_isright(isCorrect);
                        errorQuestionInfo.setQuestion_select(holder.option[lv_position]);
                        answer_list.setAnswer_title(answerItems.get(position).getAnswerslist().get(lv_position).getTitle() + "");
                        answer_list.setIsanswer(answerItems.get(position).getAnswerslist().get(lv_position).getIsanswer() + "");

                        //宝宝写的静态类 填写题目的正确或错误状态
                        if (!Question_hashmap_data.sheetmap.containsKey(position)) {
                            Question_hashmap_data.sheetmap.put(position, "0");
                        }
                    }

                    //传值到答案中；
                    mapMultiSelect.put("historyanswer", lv_position + "");
                    mapMultiSelect.put("currentanswer", RightPosition + "");
                    listmap.add(mapMultiSelect);
                    //保存数据
                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                    questionInfo.setQuestionId(answerItems.get(position).getId());
                    questionInfo.setRealAnswer(lv_position + "");
                    questionInfo.setIs_correct(isCorrect);
//                  questionInfo.setPresentanswer(errorQuestionInfo.getCurrent_answer());
                    mContext.questionInfos.add(questionInfo);
                    if (!map.containsKey(position)) {
//						return;
//					}
                        map.put(position, true);
                    }

//                view.setEnabled(false);
                    parent.setEnabled(false);
//                holder.listview.setEnabled(false);

                }
            });
            holder.btn_answer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!map.containsKey(position)){
                        return;
                    }
                    pop_aswer.showAsDropDown(holder.btn_answer, barNumber, holder.correct_answer, holder.answer_mine, answer_description, answer_difficulty);
                    holder.btn_answer.setSelected(true);
                    holder.btn_answer_sheet.setSelected(false);
                    pop_aswer_sheet.dissmiss();
                }
            });

//            holder.btn_answer_sheet.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    pop_aswer_sheet.showAsDropDown(mContext, holder.btn_answer_sheet, barNumber, position + 1, allpageNum, questionType, questionNum);
//                    holder.btn_answer_sheet.setSelected(true);
//                    holder.btn_answer.setSelected(false);
//
//                    holder.btn_answer_sheet.setTextColor(Color.parseColor("#90006B"));
//
//                }
//            });
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#2b89e9"));
            Log.e("currentNumber", position + "-----currentNumber");
            holder.ril_last_question.setOnClickListener(new LinearOnClickListener(position - 1, false, position, holder, listmap));
            holder.ril_next_question.setOnClickListener(new LinearOnClickListener(position + 1, true, position, holder, listmap));
            container.addView(viewItems.get(position));
            return viewItems.get(position);
        }
    /*
        解决scrollview下listview显示不全
      */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    @Override
    public void onClick(View v) {
    }

    /**
     * @author 设置上一步和下一步按钮监听
     */
    class LinearOnClickListener implements OnClickListener {

        private int mPosition;
        private int mPosition1;
        private boolean mIsNext;
        private ViewHolder viewHolder;
        private String currentanswer;
        SaveQuestionInfo questionInfo;
        private LinearLayout linear;
        private List<Map<String, Object>> listmap;
        private String[] option = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        public LinearOnClickListener(int position, boolean mIsNext, int position1, ViewHolder viewHolder, List<Map<String, Object>> listmap) {
            mPosition = position;//当前页面标号，相当于不全的总页数
            mPosition1 = position1;//listview下标
            this.viewHolder = viewHolder;//view
            this.mIsNext = mIsNext;//是否获取监听
            this.listmap = listmap;//hashmaplist
        }

        @Override
        public void onClick(View v) {
            if (mPosition == viewItems.size()) {//当前页面是最后一页
                if (!map.containsKey(mPosition1)) {
                    Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                    return;
                }
                map.put(mPosition1, true);
            } else {//点击上一页按钮
                if (mPosition == -1) {
                    Toast.makeText(mContext, "已经是第一页", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (mIsNext) {//点击下一页按钮
                        isNext = mIsNext;
                        mContext.setCurrentView(mPosition);
                    } else {
                        //点击上一页按钮
                        mContext.setCurrentView(mPosition);

                    }
                }
            }
        }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public class ViewHolder {
        private ListView listview;
        private MyCollect_question_answer_anapdter adapter;
        private TextView present_topic, question_title, question_score,question_test;
        private RelativeLayout ril_last_question, ril_next_question;
        private Button rbtn_collect, btn_answer_sheet, btn_answer;
        private String answer_mine, correct_answer, answer_type;
        private String[] option = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        private LinearLayout answer_history;
    }

}
