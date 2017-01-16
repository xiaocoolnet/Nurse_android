package chinanurse.cn.nurse.new_Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.ErrorQuestion_bean;
import chinanurse.cn.nurse.bean.Question_hashmap_data;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.popWindow.Pop_Answer_Sheet;
import chinanurse.cn.nurse.popWindow.Pop_Aswer;
import chinanurse.cn.nurse.publicall.ListViewForScrollView;

public class ExaminationSubmitAdapter extends PagerAdapter implements OnClickListener {

    private static final int CHECKHADFAVORITE = 5;
    Question_Activity mContext;
    // 传递过来的页面view的集合
    List<View> viewItems;
    // 每个item的页面view
    View convertView;
    // 传递过来的所有数据
    List<Answer_option_Bean.DataEntity> answerItems;
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


//	DBManager dbManager;

    String isCorrect = ConstantUtil.isCorrect;//1对，0错

    private int errortopicNum = 0;
    private int RightPosition;
    private UserBean user;
    String questionType;
    String questionNum;
    private static int gradview_position;


    String resultA = "";
    String resultB = "";
    String resultC = "";
    String resultD = "";
    String resultE = "";

    int currentNumber;
    private String type;

    private String Timekey;
    private String collectTag;
    int number = 0;


    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SETCOLLECT:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String data = obj.getString("data");
                            if ("success".equals(status)) {
                                Toast.makeText(mContext, R.string.question_collect_succcess, Toast.LENGTH_SHORT).show();
                                holder.rbtn_collect.setBackgroundResource(R.mipmap.btn_collect_sel);
//                                /**保存是否添加收藏*/
//                                    SharedPreferences sp=getSharedPreferences("SAVECOLLECTION", Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor=sp.edit();
//                                    editor.putBoolean("isCollection", isCollection);
//                                    editor.commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case CANCELCOLLECT:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String data = obj.getString("data");
                            if ("success".equals(status)) {
                                holder.rbtn_collect.setBackgroundResource(R.mipmap.btn_collet);
                                Toast.makeText(mContext, R.string.question_cancel_collect, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CHECKHADFAVORITE:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                if (HttpConnect.isConnnected(mContext)) {
                                    new StudyRequest(mContext, handler).DELLCOLLEXT(user.getUserid(), answerItems.get(gradview_position).getId(),
                                    "2", CANCELCOLLECT);
                                } else {
                                      Toast.makeText(mContext, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                if (HttpConnect.isConnnected(mContext)) {
                                    new StudyRequest(mContext, handler).COLLEXT(user.getUserid(), answerItems.get(gradview_position).getId(), "2",
                                    answerItems.get(gradview_position).getPost_title(), answerItems.get(gradview_position).getPost_description().toString(),
                                    SETCOLLECT);
                        } else {
                            Toast.makeText(mContext, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                        }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

            }
        }
    };

    public ExaminationSubmitAdapter(Question_Activity context, List<View> viewItems, List<Answer_option_Bean.DataEntity> dataItems, String questionType, String questionNum, int mposition, String type) {
        mContext = context;
        this.viewItems = viewItems;
        this.answerItems = dataItems;
        this.questionType = questionType;
        this.questionNum = questionNum;
        this.gradview_position = mposition;
        this.type = type;
        Log.e("size", "ExaminationSubmitAdapter-----ExaminationSubmitAdapter");

        user = new UserBean(context);
        pop_aswer = new Pop_Aswer(mContext);
        pop_aswer_sheet = new Pop_Answer_Sheet(mContext);


        allpageNum = Integer.valueOf(questionNum);
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
//        gradview_position = Question_Sheet_Gridview_Adapter.mposition;
        if (gradview_position != -1) {
            currentNumber = gradview_position;
            convertView = viewItems.get(gradview_position);
            //显示选项列表
            holder.listview = (ListViewForScrollView) convertView.findViewById(R.id.question_answer_list);
            //显示单选还是多选
            holder.question_title = (TextView) convertView.findViewById(R.id.question_title_score_text);
            //显示当前页面和总页面
            holder.present_topic = (TextView) convertView.findViewById(R.id.quedtion_at_present_topic);
//            holder.question_test = (TextView) convertView.findViewById(R.id.quedtion_at_present_topic_page);
            //考试时间
            holder.question_time = (TextView) convertView.findViewById(R.id.question_time);

            if ("1".equals(type)){
                holder.question_time.setText(position + 1 + "/" + answerItems.size());
            }else if ("11".equals(type)){
                holder.question_time.setText("15:00");
                holder.present_topic.setText(position + 1 + "/" + answerItems.size());
                holder.present_topic.setVisibility(View.VISIBLE);
            }
//        //得到正确答案
//        for ( i = 0; i < answerItems.get(position).getAnswerlist().size(); i++) {
//            if ("1".equals(answerItems.get(position).getAnswerlist().get(i).getIsanswer())) {
//                holder.correct_answer = holder.option[i];
//            }
//        }
            //本题分数

//            if ("1".equals(type)){

//            }else if("11".equals(type)){
//                holder.question_test.setText(position + 1 + "/" + answerItems.size());
//                holder.question_test.setVisibility(View.VISIBLE);
//                holder.present_topic.setText(String.valueOf(minute)+":"+String.valueOf(second));
//                startTime();
//            }
            holder.question_score = (TextView) convertView.findViewById(R.id.question_score);
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
            holder.question_title.setText(answerItems.get(position).getPost_title());
            holder.adapter = new Study_question_answer_anapdter(mContext, answerItems, position);

            holder.listview.setAdapter(holder.adapter);
            holder.listview.setDivider(null);
            mContext.setListViewHeightBasedOnChildren(holder.listview);
            //我把这几个点击事件拿到里面来，因为，这些viewpager是同时创建的，currentNumber会从0加到9，
            // currentNumber=9之后不会在变化，传输currentNumber的值无意义
            holder.rbtn_collect.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    holder.rbtn_collect.setSelected(true);
//                    holder.rbtn_collect.setTextColor(Color.parseColor("#90006B"));
                    if (null != user.getUserid() && user.getUserid().length() > 0) {
                        if (HttpConnect.isConnnected(mContext)) {
                            new StudyRequest(mContext, handler).CheckHadFavorite(user.getUserid(), answerItems.get(gradview_position).getId(), "2", CHECKHADFAVORITE);
                        } else {
                            Toast.makeText(mContext, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                    }
                }
            });


            holder.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int lv_position, long id) {
                    //得到正确答案
                    for (int i = 0; i < answerItems.get(gradview_position).getAnswerlist().size(); i++) {
                        if ("1".equals(answerItems.get(gradview_position).getAnswerlist().get(i).getIsanswer())) {
                            holder.correct_answer = holder.option[i];
                            RightPosition = i;
                            //得到正确答案的描述
                            answer_description = answerItems.get(gradview_position).getPost_description().toString();
                            answer_difficulty = Integer.valueOf(answerItems.get(gradview_position).getPost_difficulty().toString());

                        }
                    }
                    View v = parent.getChildAt(RightPosition);
                    if(v!=null) {
                        v.findViewById(R.id.ril_choose_option).setBackgroundResource(R.drawable.button_boder_green);
                        holder.answer_list = (TextView) v.findViewById(R.id.tv_answer_list);
                        holder.question_option.setTextColor(mContext.getResources().getColor(R.color.whilte));
                        holder.answer_list.setTextColor(mContext.getResources().getColor(R.color.whilte));
                    }
                    //我选择的答案
                    holder.answer_mine = holder.option[lv_position];
                    holder.answer_score = holder.question_score.getText().toString();
                    holder.answer_history = (LinearLayout) view.findViewById(R.id.ril_choose_option);
                    holder.question_option = (TextView) view.findViewById(R.id.question_option);
                    holder.answer_list = (TextView) view.findViewById(R.id.tv_answer_list);

                    //画外框
                    if (((ListView) parent).getTag() != null) {
                        ((View) ((ListView) parent).getTag()).findViewById(R.id.ril_choose_option).setBackgroundResource(R.drawable.button_boder);
                    }
                    ((ListView) parent).setTag(view);
                    //判断题目的对错
                    if ("1".equals(answerItems.get(position).getAnswerlist().get(lv_position).getIsanswer())) {
                        holder.answer_mine = ((TextView) view.findViewById(R.id.question_option)).getText().toString();
                        holder.answer_history.setBackgroundResource(R.drawable.button_boder_green);
                        holder.question_option.setTextColor(mContext.getResources().getColor(R.color.whilte));
                        holder.answer_list.setTextColor(mContext.getResources().getColor(R.color.whilte));
                        //正确的时候将当前自定义页面添加到第一页的viewpager中
//                      mContext.setCurrentView(position + 1);
                        //正确
                        isCorrect = ConstantUtil.isCorrect;
                        //答案正确的状态
                        holder.answer_type = "1";
                    } else if ("0".equals(answerItems.get(gradview_position).getAnswerlist().get(lv_position).getIsanswer().toString())) {
                        holder.answer_history.setBackgroundResource(R.drawable.button_boder_red);
                        holder.question_option.setTextColor(mContext.getResources().getColor(R.color.whilte));
                        holder.answer_list.setTextColor(mContext.getResources().getColor(R.color.whilte));
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
                        answer_list.setAnswer_title(answerItems.get(gradview_position).getAnswerlist().get(lv_position).getAnswer_title() + "");
                        answer_list.setIsanswer(answerItems.get(gradview_position).getAnswerlist().get(lv_position).getIsanswer() + "");
                    }
                    //传值到答案中；
                    mapMultiSelect.put("historyanswer", lv_position + "");
                    mapMultiSelect.put("currentanswer", RightPosition + "");
                    listmap.add(mapMultiSelect);
//                //保存数据
                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                    questionInfo.setQuestionId(answerItems.get(gradview_position).getId());
                    questionInfo.setRealAnswer(lv_position + "");
                    questionInfo.setScore(holder.answer_score);
                    questionInfo.setIs_correct(isCorrect);
//                    questionInfo.setPresentanswer(errorQuestionInfo.getCurrent_answer());
                    mContext.questionInfos.add(questionInfo);
                    if (!map.containsKey(gradview_position)) {
//						return;
//					}
                        Log.e("currentNumber", position + "?????currentNumbercurrentNumbercurrentNumber");
                        map.put(gradview_position, true);
                    }

//                view.setEnabled(false);
                    parent.setEnabled(false);
//                holder.listview.setEnabled(false);

                }
            });
            holder.btn_answer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("position", "---------");

                    if (!map.containsKey(gradview_position)){

                    }
                    if (rbtnoneNum_answer == 1) {
                        pop_aswer.showAsDropDown(holder.btn_answer, barNumber, holder.correct_answer, holder.answer_mine, answer_description, answer_difficulty);
                        holder.btn_answer.setSelected(true);
                        holder.btn_answer_sheet.setSelected(false);
                        pop_aswer_sheet.dissmiss();

                        rbtnoneNum_answer = 0;
                    } else {
//                          setBackgroundBlack(all_choice, 1);
                        holder.btn_answer.setSelected(false);
                        holder.btn_answer_sheet.setSelected(false);
                        rbtnoneNum_answer = 1;
                    }


                }
            });
            holder.btn_answer_sheet.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop_aswer_sheet.showAsDropDown(mContext, holder.btn_answer_sheet, barNumber, gradview_position + 1, allpageNum, questionType, questionNum);
                    holder.btn_answer_sheet.setSelected(true);
                    holder.btn_answer.setSelected(false);

                    holder.btn_answer_sheet.setTextColor(Color.parseColor("#90006B"));

                }
            });
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#2b89e9"));
            Log.e("currentNumber", position + "-----currentNumber");
            holder.ril_last_question.setOnClickListener(new LinearOnClickListener(gradview_position - 1, false, gradview_position, holder, listmap));
            holder.ril_next_question.setOnClickListener(new LinearOnClickListener(gradview_position + 1, true, gradview_position, holder, listmap));
            container.addView(viewItems.get(position));

            Log.e("currentNumber", currentNumber + "=======" + position);
            return viewItems.get(gradview_position);


        } else {

            currentNumber = position;
            convertView = viewItems.get(position);
            //显示选项列表
            holder.listview = (ListViewForScrollView) convertView.findViewById(R.id.question_answer_list);
            //显示单选还是多选
            holder.question_title = (TextView) convertView.findViewById(R.id.question_title_score_text);
            //显示当前页面和总页面
            holder.present_topic = (TextView) convertView.findViewById(R.id.quedtion_at_present_topic);
//            holder.question_test = (TextView) convertView.findViewById(R.id.quedtion_at_present_topic_page);
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
            //本题分数
            holder.question_score = (TextView) convertView.findViewById(R.id.question_score);
            //考试时间或者是当前页页数占总页数的几分之几
            holder.question_time = (TextView) convertView.findViewById(R.id.question_time);
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
            holder.question_title.setText(answerItems.get(position).getPost_title());
            holder.adapter = new Study_question_answer_anapdter(mContext, answerItems, position);
            collectTag = "collectTag"+currentNumber;
            holder.rbtn_collect.setTag(collectTag);
            Timekey = "Timekey"+currentNumber;
            holder.question_time.setTag(Timekey);
            holder.ril_next_question.setTag("NextQ"+currentNumber);
            if ("1".equals(type)){//每日一练
                holder.question_time.setText(position + 1 + "/" + answerItems.size());
                holder.present_topic.setVisibility(View.GONE);
            }else if ("11".equals(type)){
                holder.present_topic.setText(position + 1 + "/" + answerItems.size());
                holder.present_topic.setVisibility(View.VISIBLE);
            }
            holder.listview.setAdapter(holder.adapter);
            holder.listview.setDivider(null);
            mContext.setListViewHeightBasedOnChildren(holder.listview);
            //我把这几个点击事件拿到里面来，因为，这些viewpager是同时创建的，currentNumber会从0加到9，
            // currentNumber=9之后不会在变化，传输currentNumber的值无意义

//            holder.rbtn_collect.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (collectNum == 1) {
//                        holder.rbtn_collect.setSelected(true);
//                        holder.rbtn_collect.setTextColor(Color.parseColor("#90006B"));
//                        if (HttpConnect.isConnnected(mContext)) {
//                            new StudyRequest(mContext, handler).COLLEXT(user.getUserid(), answerItems.get(position).getId(), questionType,
//                                    answerItems.get(position).getPost_title(), answerItems.get(position).getPost_description().toString(),
//                                    SETCOLLECT);
//                        } else {
//                            Toast.makeText(mContext, R.string.net_erroy, Toast.LENGTH_SHORT).show();
//                        }
//                        collectNum = 0;
//                    } else {
//                        holder.rbtn_collect.setSelected(false);
//                        holder.rbtn_collect.setTextColor(Color.parseColor("#808080"));
//                        if (HttpConnect.isConnnected(mContext)) {
//                            new StudyRequest(mContext, handler).DELLCOLLEXT(user.getUserid(), answerItems.get(position).getId(),
//                                    questionType, CANCELCOLLECT);
//                        } else {
//                            Toast.makeText(mContext, R.string.net_erroy, Toast.LENGTH_SHORT).show();
//                        }
//                        collectNum = 1;
//                    }
//                }
//            });


            holder.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int lv_position, long id) {
                    //得到正确答案
                    for (int i = 0; i < answerItems.get(position).getAnswerlist().size(); i++) {
                        if ("1".equals(answerItems.get(position).getAnswerlist().get(i).getIsanswer())) {
                            holder.correct_answer = holder.option[i];
                            RightPosition = i;
                            //得到正确答案的描述
                            answer_description = answerItems.get(position).getPost_description().toString();
                            answer_difficulty = Integer.valueOf(answerItems.get(position).getPost_difficulty().toString());

                        }
                    }
                    View v = parent.getChildAt(RightPosition);
                    if(v!=null) {
                        v.findViewById(R.id.ril_choose_option).setBackgroundResource(R.drawable.button_boder_green);
                        holder.question_option = (TextView) v.findViewById(R.id.question_option);
                        holder.answer_list = (TextView) v.findViewById(R.id.tv_answer_list);
                        holder.question_option.setTextColor(mContext.getResources().getColor(R.color.whilte));
                        holder.answer_list.setTextColor(mContext.getResources().getColor(R.color.whilte));

                    }
                    //我选择的答案
                    holder.answer_mine = holder.option[lv_position];
                    Question_hashmap_data.answerList.add((lv_position+1)+"");//添加答案到静态类
                    Question_hashmap_data.questionList.add(answerItems.get(position).getId()+"");
                    holder.answer_score = holder.question_score.getText().toString();
                    holder.answer_history = (LinearLayout) view.findViewById(R.id.ril_choose_option);
                    holder.question_option = (TextView) view.findViewById(R.id.question_option);
                    holder.answer_list = (TextView) view.findViewById(R.id.tv_answer_list);
                    //画外框
                    if (((ListView) parent).getTag() != null) {
                        ((View) ((ListView) parent).getTag()).findViewById(R.id.ril_choose_option).setBackgroundResource(R.drawable.button_boder);
                    }
                    ((ListView) parent).setTag(view);
                    //判断题目的对错
                    if ("1".equals(answerItems.get(position).getAnswerlist().get(lv_position).getIsanswer())) {
                        holder.answer_mine = ((TextView) view.findViewById(R.id.question_option)).getText().toString();
                        holder.answer_history.setBackgroundResource(R.drawable.button_boder_green);
                        holder.question_option.setTextColor(mContext.getResources().getColor(R.color.whilte));
                        holder.answer_list.setTextColor(mContext.getResources().getColor(R.color.whilte));

                        //正确的时候将当前自定义页面添加到第一页的viewpager中
//                      mContext.setCurrentView(position + 1);
                        //正确
                        isCorrect = ConstantUtil.isCorrect;
                        //答案正确的状态
                        holder.answer_type = "1";

                        //宝宝写的静态类 填写题目的正确或错误状态
                        if (!Question_hashmap_data.sheetmap.containsKey(position)) {
                            Question_hashmap_data.sheetmap.put(position, "1");
                        }
                    } else if ("0".equals(answerItems.get(position).getAnswerlist().get(lv_position).getIsanswer().toString())) {
                        holder.answer_history.setBackgroundResource(R.drawable.button_boder_red);
                        holder.question_option.setTextColor(mContext.getResources().getColor(R.color.whilte));
                        holder.answer_list.setTextColor(mContext.getResources().getColor(R.color.whilte));
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
                        answer_list.setAnswer_title(answerItems.get(position).getAnswerlist().get(lv_position).getAnswer_title() + "");
                        answer_list.setIsanswer(answerItems.get(position).getAnswerlist().get(lv_position).getIsanswer() + "");

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
                    questionInfo.setScore(holder.answer_score);
                    questionInfo.setIs_correct(isCorrect);
//                questionInfo.setPresentanswer(errorQuestionInfo.getCurrent_answer());
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
                    Log.e("position", "---------");
                    PopuwindowAnswer();
                }
            });
            holder.btn_answer_sheet.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop_aswer_sheet.showAsDropDown(mContext, holder.btn_answer_sheet, barNumber, position + 1, allpageNum, questionType, questionNum);
                    holder.btn_answer_sheet.setSelected(true);
                    holder.btn_answer.setSelected(false);

                    holder.btn_answer_sheet.setTextColor(Color.parseColor("#90006B"));

                }
            });
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#2b89e9"));
            Log.e("currentNumber", position + "-----currentNumber");
            holder.ril_last_question.setOnClickListener(new LinearOnClickListener(position - 1, false, position, holder, listmap));
            holder.ril_next_question.setOnClickListener(new LinearOnClickListener(position + 1, true, position, holder, listmap));
            container.addView(viewItems.get(position));

            Log.e("currentNumber", currentNumber + "=======" + position);
            return viewItems.get(position);
        }
    }

    public void PopuwindowAnswer(){
        if (!map.containsKey(number)){
            return;
        }
        pop_aswer.showAsDropDown(holder.btn_answer, barNumber, holder.correct_answer, holder.answer_mine, answer_description, answer_difficulty);
        holder.btn_answer.setSelected(true);
        holder.btn_answer_sheet.setSelected(false);
        pop_aswer_sheet.dissmiss();
    }
    public void GoneNext(){
        holder.ril_next_question.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
    }
    /*
解决scrollview下listview显示不全
*/
//    public void setListViewHeightBasedOnChildren(ListView listView) {
//        // 获取ListView对应的Adapter
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            return;
//        }
//
//        int totalHeight = 0;
//        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
//            // listAdapter.getCount()返回数据项的数目
//            View listItem = listAdapter.getView(i, null, listView);
//            // 计算子项View 的宽高
//            listItem.measure(0, 0);
//            // 统计所有子项的总高度
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        // listView.getDividerHeight()获取子项间分隔符占用的高度
//        // params.height最后得到整个ListView完整显示需要的高度
//        listView.setLayoutParams(params);
//    }
    //    /**
//     * @author 设置上一步和下一步按钮监听
//     */
    class LinearOnClickListener implements OnClickListener {

        private int mPosition;
        private int mPosition1;
        private boolean mIsNext;
        private ViewHolder viewHolder;
        private String ssStr;
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
                    Toast.makeText(mContext, "请选择选项", Toast.LENGTH_SHORT).show();

                    return;
                }
                //点击答案弹出提交
//                mContext.uploadExamination(errortopicNum);
                map.put(mPosition1, true);
            } else {//点击上一页按钮
                if (mPosition == -1) {
                    Toast.makeText(mContext, "已经是第一页", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (mIsNext) {//点击下一页按钮
                        Log.e("currentNumber", mPosition1 + "mPosition1");
                        if (!map.containsKey(mPosition1)) {//下一页
                            Toast.makeText(mContext, "请选择选项", Toast.LENGTH_SHORT).show();
                            Log.e("currentNumber", "currentNumbercurrentNumbercurrentNumber");
                            return;
                        }
                        isNext = mIsNext;
                        mContext.setCurrentView(mPosition);
                        number+=1;
                    } else {
                        Log.e("currentNumber", mPosition1 + "mPosition1_____++");
                        //点击上一页按钮
                        mContext.setCurrentView(mPosition);
                        number-=1;
                    }
                }
            }
        }
    }

    @Override
    public int getCount() {
        if (viewItems == null)
            return 0;
        return viewItems.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    //错题数
    public int errorTopicNum() {
        if (errortopicNum != 0) {
            return errortopicNum;
        }
        return 0;

    }


    public static int getGradview_position(int position) {

        gradview_position = position;
        Log.e("position_4", gradview_position + "");
        return gradview_position;
    }


    public class ViewHolder {
        private ListViewForScrollView listview;
        private Study_question_answer_anapdter adapter;
        private TextView present_topic, question_title, question_score,question_test,question_time,question_option,answer_list;
        private RelativeLayout ril_last_question, ril_next_question;
        private Button rbtn_collect, btn_answer_sheet, btn_answer;
        private String answer_mine, correct_answer, answer_type, answer_score;
        private String[] option = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        private LinearLayout answer_history;
    }
    //从监听中获取listview中获取item的位置上的layout
//    public View getViewByPosition(int pos, ListView listView) {
//        final int firstListItemPosition = listView.getFirstVisiblePosition();
//        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
//        if (pos < firstListItemPosition || pos > lastListItemPosition) {
//            return listView.getAdapter().getView(pos, null, listView);
//        } else
//    }

}
