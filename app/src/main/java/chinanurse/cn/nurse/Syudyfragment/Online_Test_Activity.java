package chinanurse.cn.nurse.Syudyfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.adapter.study_adapter.Study_question_answer_anapdter;
import chinanurse.cn.nurse.bean.Answer_option_Bean;
import chinanurse.cn.nurse.bean.Question_hashmap_data;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.popWindow.Pop_Answer_Sheet;
import chinanurse.cn.nurse.popWindow.Pop_Aswer;
import chinanurse.cn.nurse.question_viewpager.Lv_question_answer;
import chinanurse.cn.nurse.question_viewpager.ViewFlipperSelfe;

/**
 * Created by Administrator on 2016/6/26.
 */
public class Online_Test_Activity extends Activity implements View.OnClickListener, ViewFlipperSelfe.OnViewFlipperListener {
    private static final int GETTESTTEXT = 1;
    private static final int SETCOLLECT = 2;
    private static final int CANCELCOLLECT = 3;
    private TextView top_title, title_radiobtn, present_topic, showpop, questionTitle;
    private RelativeLayout rlBack, last_question, next_question;
    private LinearLayout all_choice;
    private Intent intent;
    private String questionType, questionNum, answer_mine, correct_answer, answer_description;
    private RadioGroup answer_function;
    private ViewFlipperSelfe vp_topic;
    /**
     * 是否添加收藏
     */
    private static boolean isCollection = true;
    private CirclePageIndicator cpid;
    private Activity mActivity;
    private UserBean user;
    private Pop_Aswer pop_aswer;
    private Pop_Answer_Sheet pop_aswer_sheet;
    private int[] position = new int[2];
    private Button question_collect;
    private int currentNumber, allpageNum, right, answer_difficulty;
    private int collectNum = 1;
    private int rbtnoneNum = 1;
    private Study_question_answer_anapdter answer_list_adapter;
    private Lv_question_answer answerlist;
    private Answer_option_Bean answer_option;
    private int rbtnNum = 1;
    private RadioButton rb_sheet, rb_answer;
    private String answer_type;
    private List<Answer_option_Bean.DataBean> answerlistbean = new ArrayList<>();
    private SharedPreferences sp;
    private String[] option = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    //定义难度
    private int barNumber = 4;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETTESTTEXT:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        Gson gson = new Gson();
                        answer_option = gson.fromJson(result, Answer_option_Bean.class);
                        if ("success".equals(answer_option.getStatus())) {
                            answerlistbean.addAll(answer_option.getData());
                            present_topic.setText(currentNumber + "/" + answer_option.getData().size());

//                            answer_list_adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Online_Test_Activity.this, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                        }
                        vp_topic.addView(creatView(currentNumber, 1));
//
                    } else {
                        Log.i("<=======>", "是他是他就是他");
                    }
                    break;
                case SETCOLLECT:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String data = obj.getString("data");
                            if ("success".equals(status)) {
                                Toast.makeText(mActivity, R.string.question_collect_succcess, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(mActivity, R.string.question_cancel_collect, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online__test_);
        mActivity = this;
        user = new UserBean(mActivity);
        intent = getIntent();
        questionType = intent.getStringExtra("questionNUM");
        questionNum = intent.getStringExtra("questioncount");
        allpageNum = Integer.valueOf(questionNum);
        intView();
        pop_aswer = new Pop_Aswer(this);
        pop_aswer_sheet = new Pop_Answer_Sheet(this);
    }

    private void intView() {
        currentNumber = 1;
        top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText(R.string.stu_question_details);
        rlBack = (RelativeLayout) findViewById(R.id.btn_back);
        rlBack.setOnClickListener(this);
        //头部标题单选多选
        title_radiobtn = (TextView) findViewById(R.id.question_title_score_text);
//        showpop = (TextView) findViewById(R.id.question_pop_position);
        last_question = (RelativeLayout) findViewById(R.id.ril_last_question);
        last_question.setOnClickListener(this);
        next_question = (RelativeLayout) findViewById(R.id.ril_next_question);
        next_question.setOnClickListener(this);
        answer_function = (RadioGroup) findViewById(R.id.rg_answer_function);
        vp_topic = (ViewFlipperSelfe) findViewById(R.id.question_vp_topic);
        vp_topic.setOnViewFlipperListener(this);
        rb_sheet = (RadioButton) findViewById(R.id.rbtn_answer_sheet);
        rb_answer = (RadioButton) findViewById(R.id.rbtn_answer);
        sp = mActivity.getSharedPreferences("question", Context.MODE_PRIVATE);

        cpid = (CirclePageIndicator) findViewById(R.id.question_topic_indicator);
        present_topic = (TextView) findViewById(R.id.quedtion_at_present_topic);

        all_choice = (LinearLayout) findViewById(R.id.question_all_choice_layout);
        question_collect = (Button) findViewById(R.id.rbtn_collect);
        question_collect.setOnClickListener(this);
        answer_function.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_answer_sheet:
                        if (rbtnNum == 1) {
//                            setBackgroundBlack(all_choice, 0);
                            answer_function.getLocationOnScreen(position);
                            Log.i("position", "----------->" + answer_function);
                            //暂时注了
//                            pop_aswer_sheet.showAsDropDown(answer_function, barNumber, currentNumber, allpageNum, questionType, questionNum);
                            rbtnNum = 0;
                        } else {
//                            setBackgroundBlack(all_choice, 1);
                            rb_sheet.setChecked(false);
                            rbtnNum = 1;
                        }


                        break;
                    case R.id.rbtn_answer:
                        if (rbtnoneNum == 1) {
//                            setBackgroundBlack(all_choice, 0);
                            answer_function.getLocationOnScreen(position);
                            Log.i("position", "----------->" + answer_function);
                            pop_aswer.showAsDropDown(answer_function, barNumber, correct_answer, answer_mine, answer_description, answer_difficulty);
                            rbtnoneNum = 0;
                        } else {
//                          setBackgroundBlack(all_choice, 1);
                            rb_answer.setChecked(false);
                            rbtnoneNum = 1;
                        }
                        break;
                }
            }
        });

        if ("39".equals(questionType)) {
            if (HttpConnect.isConnnected(mActivity)) {
                new StudyRequest(mActivity, handler).TEXTLIST(user.getUserid(), questionType, questionNum, GETTESTTEXT);
            } else {
                Toast.makeText(mActivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            }
        } else if (questionType.equals("34")) {
            if (HttpConnect.isConnnected(mActivity)) {
                new StudyRequest(mActivity, handler).TEXTLIST(user.getUserid(), questionType, questionNum, GETTESTTEXT);
            } else {
                Toast.makeText(mActivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            }
        } else if (questionType.equals("35")) {
            if (HttpConnect.isConnnected(mActivity)) {
                new StudyRequest(mActivity, handler).TEXTLIST(user.getUserid(), questionType, questionNum, GETTESTTEXT);
            } else {
                Toast.makeText(mActivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            }
        } else if (questionType.equals("36")) {
            if (HttpConnect.isConnnected(mActivity)) {
                new StudyRequest(mActivity, handler).TEXTLIST(user.getUserid(), questionType, questionNum, GETTESTTEXT);
            } else {
                Toast.makeText(mActivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            }
        } else if (questionType.equals("37")) {
            if (HttpConnect.isConnnected(mActivity)) {
                new StudyRequest(mActivity, handler).TEXTLIST(user.getUserid(), questionType, questionNum, GETTESTTEXT);
            } else {
                Toast.makeText(mActivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            }
        } else if (questionType.equals("38")) {
            if (HttpConnect.isConnnected(mActivity)) {
                new StudyRequest(mActivity, handler).TEXTLIST(user.getUserid(), questionType, questionNum, GETTESTTEXT);
            } else {
                Toast.makeText(mActivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.rbtn_collect:
                if (collectNum == 1) {
                    question_collect.setSelected(true);
                    if (HttpConnect.isConnnected(mActivity)) {
                        new StudyRequest(mActivity, handler).COLLEXT(user.getUserid(), answer_option.getData().get(currentNumber - 1).getId(),
                                questionType, answer_option.getData().get(currentNumber - 1).getPost_title(), answer_option.getData().get(currentNumber - 1).getPost_description(),
                                SETCOLLECT);
                    } else {
                        Toast.makeText(mActivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    collectNum = 0;
                    Log.i("collectNum", "--------->" + collectNum);
                } else {
                    question_collect.setSelected(false);
                    if (HttpConnect.isConnnected(mActivity)) {
                        new StudyRequest(mActivity, handler).DELLCOLLEXT(user.getUserid(), answer_option.getData().get(currentNumber - 1).getId(),
                                questionType, CANCELCOLLECT);
                    } else {
                        Toast.makeText(mActivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    collectNum = 1;
                    Log.i("collectNum", "--------->" + collectNum);
                }
                break;
            case R.id.ril_last_question:
                if (currentNumber == 1) {
                    Log.i("---------", "----------");
                } else {
                    vp_topic.flingToPrevious();
                }
                break;
            case R.id.ril_next_question:
                if (currentNumber == answer_option.getData().size()) {
                    Log.i("---------", "----------");
                } else {
                    vp_topic.flingToNext();
                }
                break;
        }
    }

    /**
     * 控制背景变暗 0变暗 1变亮
     */
    public void setBackgroundBlack(View view, int what) {
        switch (what) {
            case 0:
                view.setVisibility(View.VISIBLE);
                break;
            case 1:
                view.setVisibility(View.GONE);
                break;
        }
    }


    //num=0表示上一题，=1 表示下一题
    private View creatView(final int currentNumber, int num) {
//        final SharedPreferences.Editor editor = sp.edit();

        SharedPreferences sp = mActivity.getSharedPreferences("SAVE_ANSWER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();


        int i = sp.getInt("ArrayCart_size", 0);
        Log.e("ArrayCart_size", i + "");
        i++;

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        ScrollView resultView = (ScrollView) layoutInflater.inflate(R.layout.flipper_view, null);
        questionTitle = ((TextView) resultView.findViewById(R.id.question_title));
        answerlist = (Lv_question_answer) resultView.findViewById(R.id.question_answer_list);
        if (answer_option != null) {
            questionTitle.setText(answer_option.getData().get(currentNumber - 1).getPost_title() + "");
        }
        answer_list_adapter = new Study_question_answer_anapdter(mActivity, answerlistbean, currentNumber - 1);
        answerlist.setAdapter(answer_list_adapter);


        if (num == 1) {
            answerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    answer_mine = option[position];
                    answer_description = answerlistbean.get(currentNumber - 1).getPost_description().toString();
                    answer_difficulty = Integer.valueOf(answerlistbean.get(currentNumber - 1).getPost_difficulty().toString());
                    if (((ListView) parent).getTag() != null) {
                        ((View) ((ListView) parent).getTag()).findViewById(R.id.ril_choose_option).setBackgroundResource(R.drawable.button_boder);
                    }
                    ((ListView) parent).setTag(view);
                    if ("1".equals(answerlistbean.get(currentNumber - 1).getAnswerlist().get(position).getIsanswer().toString())) {
                        view.findViewById(R.id.ril_choose_option).setBackgroundResource(R.drawable.button_boder_green);
                        answer_mine = ((TextView) view.findViewById(R.id.question_option)).getText().toString();
                        answer_type = "1";
                    } else if ("0".equals(answerlistbean.get(currentNumber - 1).getAnswerlist().get(position).getIsanswer().toString())) {
                        view.findViewById(R.id.ril_choose_option).setBackgroundResource(R.drawable.button_boder_red);
                        answer_type = "0";
//                    for (int i = 0;i < answer_option.getData().get(currentNumber-1).getAnswerlist().size();i++) {
//                        if ("1".equals(answer_option.getData().get(currentNumber-1).getAnswerlist().get(i).getIsanswer()))
//                        {
//                            correct_answer = option[i];
//                        }
                    } else if ("".equals(answerlistbean.get(currentNumber - 1).getAnswerlist().get(position).getIsanswer().toString())) {
                        view.findViewById(R.id.ril_choose_option).setBackgroundResource(R.drawable.button_boder);
                        answer_type = "";
//                    for (int i = 0;i < answer_option.getData().get(currentNumber-1).getAnswerlist().size();i++) {
//                        if ("1".equals(answer_option.getData().get(currentNumber-1).getAnswerlist().get(i).getIsanswer()))
//                        {
//                            correct_answer = option[i];
//                        }
                    }
                    for (int i = 0; i < answer_option.getData().get(currentNumber - 1).getAnswerlist().size(); i++) {
                        if ("1".equals(answer_option.getData().get(currentNumber - 1).getAnswerlist().get(i).getIsanswer())) {
                            correct_answer = option[i];
                        }
                    }
                    answerlist.setEnabled(false);
                    HashMap<String, Object> allHashMap = new HashMap<String, Object>();
                    allHashMap.put("answer_id", answerlistbean.get(currentNumber - 1).getId());
                    allHashMap.put("answer_type", answer_type);
                    allHashMap.put("currentNumber", currentNumber);
                    allHashMap.put("allQuestionNum", answer_option.getData().size());
                    allHashMap.put("answer_mine", answer_mine);
                    allHashMap.put("id", Question_hashmap_data.question_answer_id + 1);
                    Question_hashmap_data.hashmap_question.add(allHashMap);
                    setsavedData();


                    Log.i("answer_mine", ((TextView) view.findViewById(R.id.question_option)).getText().toString());
                    Log.i("answer_correct", answerlistbean.get(currentNumber - 1).getAnswerlist().get(position).toString());
                    Log.i("answer_description", answerlistbean.get(currentNumber - 1).getPost_description().toString());
                    Log.i("answer_id", answerlistbean.get(currentNumber - 1).getId());
                    Log.i("answer_type", answer_type);

                    Question_hashmap_data.sheetmap.put(currentNumber,
                            answerlistbean.get(currentNumber - 1).getAnswerlist().get(position).getIsanswer().toString());
                }

            });
        } else {


        }

        return resultView;
    }

    private void setsavedData() {
        SharedPreferences sp = mActivity.getSharedPreferences("SAVE_ANSWER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("ArrayCart_size", Question_hashmap_data.hashmap_question.size());

        Log.e("size", Question_hashmap_data.hashmap_question.size() + "");
        Log.e("size1", Question_hashmap_data.hashmap_question.get(0).size() + "");

        int i = Question_hashmap_data.hashmap_question.size();
//        for (int i = 0; i < Question_hashmap_data.hashmap_question.size(); i++) {
//            editor.remove("answer_id" + i);
//            editor.remove("answer_type" + i);
//            editor.remove("currentNumber" + i);
//            editor.remove("allQuestionNum" + i);
//            editor.remove("answer_mine" + i);
//            editor.remove("correct_answer" + i);

        editor.putString("answer_id" + i, answerlistbean.get(currentNumber - 1).getId());
        editor.putString("answer_type" + i, answer_type);
        editor.putInt("currentNumber" + i, currentNumber);
        editor.putInt("allQuestionNum" + i, answer_option.getData().size());
        editor.putString("answer_mine" + i, answer_mine);
        editor.putString("correct_answer" + i, correct_answer);
        editor.commit();
//        }
    }

    @Override
    public View getNextView() {
        SharedPreferences sp = mActivity.getSharedPreferences("SAVE_ANSWER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
//        int i = Question_hashmap_data.hashmap_question.size();
        int i = sp.getInt("ArrayCart_size", 0);
        Log.e("ArrayCart_size", i + "");
        i++;
//        if (i > currentNumber) {
        currentNumber = currentNumber == answer_option.getData().size() ? 1 : currentNumber + 1;
        present_topic.setText(currentNumber + "/" + answer_option.getData().size());
        answerlist.setEnabled(false);
        Log.e("size2", i + "---" + currentNumber);
        return creatView(currentNumber, 1);

//        } else {
//            return creatView(i, 1);
//        }

    }

    @Override
    public View getPreviousView() {

        SharedPreferences sp = mActivity.getSharedPreferences("SAVE_ANSWER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
//        int i = Question_hashmap_data.hashmap_question.size();
        int i = sp.getInt("ArrayCart_size", 0);


        //让控件失去焦点
//        vp_topic.setFocusable(false);
//        vp_topic.setFocusableInTouchMode(false);
//        vp_topic.requestFocus();
//        vp_topic.requestFocus();
//
//        answerlist.setFocusable(false);
//        answerlist.setFocusableInTouchMode(false);
//        answerlist.requestFocus();
//        answerlist.requestFocus();
//        answerlist.setItemsCanFocus(false);


        currentNumber = currentNumber == 1 ? answer_option.getData().size() : currentNumber - 1;
        present_topic.setText(currentNumber + "/" + answer_option.getData().size());


        answerlist.setEnabled(true);
        return creatView(currentNumber, 0);
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sp = mActivity.getSharedPreferences("SAVE_ANSWER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        answer_type = "";
        int i = sp.getInt("ArrayCart_size", 0);
        Log.e("des  ArrayCart_size", i + "");

        editor.remove("ArrayCart_size");

        editor.clear();
        editor.commit();
        int i2 = sp.getInt("ArrayCart_size", 0);
        Log.e("des  ArrayCart_size", i2 + "");

    }


}
