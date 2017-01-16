package chinanurse.cn.nurse.new_Activity;

import android.app.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.Question_hashmap_data;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.bean.study_main_bean.Daily_Practice_bean;

/**
 * Created by Administrator on 2016/7/6.
 */
public class Question_Activity extends Activity implements View.OnClickListener {
    private static final int GETTESTTEXT = 1;
    private static final int STARTtIME = 2;
    private static final int TIMERUN = 3;
    private static final int GETTESTTEXT_OVER = 222;
    private static final int GETTESTTEXT_ERROR = 333;
    private static final int SUBMITANSWER = 4;//提交答案
    private static final int SETCOLLECT = 5;//收藏
    private static final int CANCELCOLLECT = 6;//取消收藏
    private static final int ISCOLLECT = 7;//判断是否收藏过
    private static final int CHECKHADFAVORITE = 8;

    Activity mactivity;
    private Answer_option_Bean answer_option;
    private List<Answer_option_Bean.DataEntity> answerlistbean = new ArrayList<Answer_option_Bean.DataEntity>();
    private List<Answer_option_Bean.DataEntity> answerlistbean_over = new ArrayList<Answer_option_Bean.DataEntity>();
    private List<Answer_option_Bean.DataEntity> answerlistbean_error = new ArrayList<Answer_option_Bean.DataEntity>();
    private List<View> viewItems = new ArrayList<View>();
    public List<SaveQuestionInfo> questionInfos = new ArrayList<SaveQuestionInfo>();
    private CirclePageIndicator cpid;

    public static int gradview_position;
    private int ViewPosition = 0;

    private TextView question_answer_submit, top_title;
    private RelativeLayout btn_back,NextQTag;
    private VoteSubmitViewPager vote_submit_viewpager;
    private Button collectTag;
    private RelativeLayout ril_shibai, ril_list;
    private TextView shuaxin_button;
    private ProgressDialog dialogpgd;

    //考试时间
    Timer timer;
    TimerTask timerTask;
    //时间长度
    int hour;
    int minute;
    int second;
    int isFirst;

    //弹出框弹出内容
    private String errorMsg = "", type, exam_time;
    boolean isPause = false;
    private ExaminationSubmitAdapter pagerAdapter;

    private int pageScore, allpageNum, errortopicNums, errortopicNums1;//分数，页数（题数）、错题数

    private UserBean user;
    private Intent intent;
    private Daily_Practice_bean.DataEntity dailydata;
    private Daily_Practice_bean.DataEntity.ChildlistEntity dailydatatwo;
    private String questionType, questionNum;
    private ImageView submit_image;
    private Boolean isDaily;
    private int i = 0;
    private boolean ISc;
    private Dialog dialog,dialogNumber;
    private String submitAnswer,description;
    private List<String> questionlist;
    private List<String> answerlist;

    private Handler collecthandler = new Handler() {
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
                                Toast.makeText(mactivity, R.string.question_collect_succcess, Toast.LENGTH_SHORT).show();
                                if (collectTag != null) {
                                collectTag.setSelected(true);
                                collectTag.setTextColor(getResources().getColor(R.color.purple));
                                }
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
                                if (collectTag != null) {
                                    collectTag.setSelected(false);
                                    collectTag.setTextColor(getResources().getColor(R.color.gray6));
                                    Toast.makeText(mactivity, R.string.question_cancel_collect, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            super.handleMessage(msg);

        }
    };


    private Handler Timehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMERUN:
                    if ((int) msg.obj == 1) {
                        stopTime();
                    } else if ((int) msg.obj == 0) {
                        startTime();
                    }
                    break;

            }
            super.handleMessage(msg);
        }
    };


    private Handler hanlder = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETTESTTEXT://在线考试
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        Gson gson = new Gson();
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
                                answer_option = gson.fromJson(result, Answer_option_Bean.class);
                                Log.i("questioncount_39", answer_option.getData().toString() + "");
                                if ("success".equals(answer_option.getStatus())) {
                                    answerlistbean.addAll(answer_option.getData());
                                    for (int i = 0; i < answerlistbean.size(); i++) {
                                        viewItems.add(getLayoutInflater().inflate(
                                                R.layout.question_layout, null));
                                    }
                                    pagerAdapter = new ExaminationSubmitAdapter(
                                            Question_Activity.this, viewItems,
                                            answerlistbean, questionType, questionNum, gradview_position, type);
                                    vote_submit_viewpager.setAdapter(pagerAdapter);

                                    vote_submit_viewpager.getParent()
                                            .requestDisallowInterceptTouchEvent(false);
                                    //指示器和viewpager关联起来
                                    cpid.setViewPager(vote_submit_viewpager);
                                    //设置指示器快照模式
                                    cpid.setSnap(true);
                                    //当重新进入页面时，将小圆点重新归置为0
                                    cpid.onPageSelected(0);

                                    Message timemsg = new Message();
                                    timemsg.what = TIMERUN;
                                    timemsg.obj = 0;
                                    Timehandler.sendMessage(timemsg);

                                    vote_submit_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                        @Override
                                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                        }

                                        @Override
                                        public void onPageSelected(int position) {
                                            ViewPosition = position;
//                                            minute=3;
//                                            second=0;
//                                            if (timer != null) {
//                                                timer.cancel();
//                                                timer = null;
//                                            }
//                                            if (timerTask != null) {
//                                                timerTask = null;
//                                            }
//                                            Message timemsg = new Message();
//                                            timemsg.what=TIMERUN;
//                                            timemsg.obj=0;
//                                            Timehandler.sendMessage(timemsg);

                                            i = position;
                                            isCollect();


                                        }

                                        @Override
                                        public void onPageScrollStateChanged(int state) {

                                        }
                                    });
                                    dialogpgd.dismiss();
                                } else {
                                    dialogpgd.dismiss();
                                    Toast.makeText(Question_Activity.this, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isCollect();
                    }else{
                        dialogpgd.dismiss();
                        ril_shibai.setVisibility(View.VISIBLE);
                        ril_list.setVisibility(View.GONE);
                        shuaxin_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               iniviewdata();
                            }
                        });
                    }
                    break;
                case SUBMITANSWER://提交答案
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
                                JSONObject jsonObject = new JSONObject(obj.getString("data"));
                                if (!"".equals(jsonObject.optString("score"))){
                                    stopTime();
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    dialogNumber = new AlertDialog.Builder(mactivity).create();
                                    dialogNumber.show();
                                    dialogNumber.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("+"+jsonObject.getString("score"));
                                    TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                    tv_score_name.setText(jsonObject.getString("event"));
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(3000);
                                                dialogNumber.dismiss();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                    isShowDialog("提交成功，是否退出");
                                }else{
                                    stopTime();
                                    isShowDialog("提交成功，是否退出");
                                }
                            } else {
                                Toast.makeText(Question_Activity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                                question_answer_submit.setVisibility(View.VISIBLE);
                                submit_image.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(Question_Activity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                        question_answer_submit.setVisibility(View.VISIBLE);
                        submit_image.setVisibility(View.VISIBLE);
                }
                    break;

                case GETTESTTEXT_OVER://每日一练
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        answerlistbean_over.clear();
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
//                                Bundle bundle = intent.getBundleExtra("bundle");
//                                answerlistbean_over = (List<Answer_option_Bean.DataEntity>) bundle.getSerializable("answerlistbean");
                                Gson gson = new Gson();
                                answer_option = gson.fromJson(result, Answer_option_Bean.class);
                                answerlistbean_over.addAll(answer_option.getData());
                                for (int i = 0; i < answerlistbean_over.size(); i++) {
                                    viewItems.add(getLayoutInflater().inflate(
                                            R.layout.question_daily_layout, null));
                                }
                                pagerAdapter = new ExaminationSubmitAdapter(
                                        Question_Activity.this, viewItems,
                                        answerlistbean_over, questionType, questionNum, gradview_position, type);
                                vote_submit_viewpager.setAdapter(pagerAdapter);
                                vote_submit_viewpager.getParent()
                                        .requestDisallowInterceptTouchEvent(false);
                                //指示器和viewpager关联起来
                                cpid.setViewPager(vote_submit_viewpager);
                                //设置指示器快照模式
                                cpid.setSnap(true);
                                //当重新进入页面时，将小圆点重新归置为0
                                cpid.onPageSelected(0);
                                vote_submit_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        ViewPosition = position;
                                        i = position;
                                        isCollect();
                                        cpid.onPageSelected(position);
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {

                                    }
                                });
                                isCollect();
                                dialogpgd.dismiss();
                            }else{
                                dialogpgd.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else{
                        dialogpgd.dismiss();
                        ril_shibai.setVisibility(View.VISIBLE);
                        ril_list.setVisibility(View.GONE);
                        shuaxin_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                iniviewdata();
                            }
                            });
                        }
                    break;

                case GETTESTTEXT_ERROR:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        answerlistbean_error.clear();
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
                                Bundle bundle = intent.getBundleExtra("bundle");
                                answerlistbean_error = (List<Answer_option_Bean.DataEntity>) bundle.getSerializable("answerlistbean");

                                for (int i = 0; i < answerlistbean_error.size(); i++) {
                                    viewItems.add(getLayoutInflater().inflate(
                                            R.layout.question_layout, null));
                                }
                                pagerAdapter = new ExaminationSubmitAdapter(
                                        Question_Activity.this, viewItems,
                                        answerlistbean_error, questionType, questionNum, gradview_position, type);
                                vote_submit_viewpager.setAdapter(pagerAdapter);
                                vote_submit_viewpager.getParent()
                                        .requestDisallowInterceptTouchEvent(false);
                                //指示器和viewpager关联起来
                                cpid.setViewPager(vote_submit_viewpager);
                                //设置指示器快照模式
                                cpid.setSnap(true);
                                //当重新进入页面时，将小圆点重新归置为0
                                cpid.onPageSelected(0);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    break;
                case STARTtIME://时间定时
                    TextView timeTag = (TextView) vote_submit_viewpager.findViewWithTag("Timekey" + ViewPosition);
                    if (timeTag != null) {
                        // 判断时间快到前2分钟字体颜色改变
                        //当时间小于1分钟的时候变成和红色，否则是白色
                        if (hour == 0 && minute < 1) {
                            timeTag.setTextColor(Color.RED);
                        }
//                    else {
//                        timeTag.setTextColor(Color.GRAY);
//                    }
                        if (hour == 0) {
                            //当时间分钟等于0的时候
                            if (minute == 0) {
                                //秒等于0
                                if (second == 0) {
                                    //??
                                    isFirst += 1;
                                    // 时间到
                                    if (isFirst == 1) {
                                        //往showTimeOutDialog()方法中传个0
                                        //                                showTimeOutDialog(true, "0");
                                    }
                                    timeTag.setText("00:00:00");
                                    if (timer != null) {
                                        timer.cancel();
                                        timer = null;
                                    }
                                    if (timerTask != null) {
                                        timerTask = null;
                                    }
                                    submit();
                                    isShowDialog("时间到，是否退出！");
                                } else {//当秒还没有结束则自减
                                    second--;
                                    //当秒大于10秒的时候
                                    if (second >= 10) {
                                        //显示时间的textview显示00：剩余的藐视
                                        timeTag.setText("00:0" + minute + ":" + second);
                                    } else {
                                        //否则的话显示例如：00：01
                                        timeTag.setText("00:0" + minute + ":0" + second);
                                    }
                                }
                            } else {
                                //分钟不是0   判断如果秒是0
                                if (second == 0) {
                                    // 那么秒是59的时候
                                    second = 59;
                                    //分钟就得减掉
                                    minute--;
                                    //如果分钟大于等于10分钟
                                    if (minute >= 10) {
                                        timeTag.setText("00:" + minute + ":" + second);
                                    } else {
                                        timeTag.setText("00:0" + minute + ":" + second);
                                    }
                                } else {
                                    //分钟不是0，并且秒也不是0
                                    second--;
                                    //判断 如果秒大于10秒
                                    if (second >= 10) {
                                        //并且分钟也大于10
                                        if (minute >= 10) {
                                            //显示的是例如10:10
                                            timeTag.setText("00:" + minute + ":" + second);
                                        } else {
                                            //显示的是例如01:10
                                            timeTag.setText("00:0" + minute + ":" + second);
                                        }
                                    } else {
                                        //判断秒小于10分钟大于10
                                        if (minute >= 10) {
                                            //显示的是例如10：01
                                            timeTag.setText("00:" + minute + ":0" + second);
                                        } else {
                                            //判断秒小于10分钟小于10
                                            //显示的是例如01:01
                                            timeTag.setText("00:0" + minute + ":0" + second);
                                        }
                                    }
                                }
                            }
                        } else {//小时不等于零
                            //当时间分钟等于0的时候
                            if (minute == 0) {
                                minute = 59;
                                hour--;
                                //秒等于0
                                if (second == 0) {
                                    second = 59;
                                    //当秒大于10秒的时候
                                    if (hour >= 10) {
                                        if (minute >= 10) {
                                            //显示时间的textview显示00：剩余的藐视
                                            timeTag.setText(hour + ":" + minute + ":" + second);
                                        } else {
                                            timeTag.setText(hour + ":0" + minute + ":" + second);
                                        }
                                    } else {
                                        if (minute >= 10) {
                                            //显示时间的textview显示00：剩余的藐视
                                            timeTag.setText("0" + hour + ":" + minute + ":" + second);
                                        } else {
                                            //否则的话显示例如：00：01
                                            timeTag.setText("0" + hour + ":0" + minute + ":" + second);
                                        }
                                    }
                                } else {//当秒还没有结束则自减
                                    second--;
                                    //当秒大于10秒的时候
                                    if (hour >= 10) {
                                        if (second >= 10) {
                                            //显示时间的textview显示00：剩余的藐视
                                            timeTag.setText(hour + ":" + minute + ":" + second);
                                        } else {
                                            //否则的话显示例如：00：01
                                            timeTag.setText(hour + ":" + minute + ":0" + second);
                                        }

                                    } else {
                                        if (second >= 10) {
                                            //显示时间的textview显示00：剩余的藐视
                                            timeTag.setText("0" + hour + ":" + minute + ":" + second);
                                        } else {
                                            //否则的话显示例如：00：01
                                            timeTag.setText("0" + hour + ":" + minute + ":0" + second);
                                        }
                                    }
                                }
                            } else {
                                //分钟不是0   判断如果秒是0
                                if (second == 0) {
                                    // 那么秒是59的时候
                                    second = 59;
                                    //分钟就得减掉
                                    minute--;
                                    if (hour >= 10) {
                                        //如果分钟大于等于10分钟
                                        if (minute >= 10) {
                                            timeTag.setText(hour + ":" + minute + ":" + second);
                                        } else {
                                            timeTag.setText(hour + ":0" + minute + ":" + second);
                                        }
                                    } else {
                                        //如果分钟大于等于10分钟
                                        if (minute >= 10) {
                                            timeTag.setText("0" + hour + ":" + minute + ":" + second);
                                        } else {
                                            timeTag.setText("0" + hour + ":0" + minute + ":" + second);
                                        }
                                    }
                                } else {
                                    //分钟不是0，并且秒也不是0
                                    second--;
                                    if (hour >= 10) {
                                        //判断 如果秒大于10秒
                                        if (second >= 10) {
                                            //并且分钟也大于10
                                            if (minute >= 10) {
                                                //显示的是例如10:10
                                                timeTag.setText(hour + ":" + minute + ":" + second);
                                            } else {
                                                //显示的是例如01:10
                                                timeTag.setText(hour + ":0" + minute + ":" + second);
                                            }
                                        } else {
                                            //判断秒小于10分钟大于10
                                            if (minute >= 10) {
                                                //显示的是例如10：01
                                                timeTag.setText(hour + ":" + minute + ":0" + second);
                                            } else {
                                                //判断秒小于10分钟小于10
                                                //显示的是例如01:01
                                                timeTag.setText(hour + ":0" + minute + ":0" + second);
                                            }
                                        }
                                    } else {
                                        //判断 如果秒大于10秒
                                        if (second >= 10) {
                                            //并且分钟也大于10
                                            if (minute >= 10) {
                                                //显示的是例如10:10
                                                timeTag.setText("0" + hour + ":" + minute + ":" + second);
                                            } else {
                                                //显示的是例如01:10
                                                timeTag.setText("0" + hour + ":0" + minute + ":" + second);
                                            }
                                        } else {
                                            //判断秒小于10分钟大于10
                                            if (minute >= 10) {
                                                //显示的是例如10：01
                                                timeTag.setText("0" + hour + ":" + minute + ":0" + second);
                                            } else {
                                                //判断秒小于10分钟小于10
                                                //显示的是例如01:01
                                                timeTag.setText("0" + hour + ":0" + minute + ":0" + second);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case ISCOLLECT:

                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                if (collectTag != null) {
                                    collectTag.setSelected(true);
                                    collectTag.setTextColor(getResources().getColor(R.color.purple));
                                }
                            } else {
                                if (collectTag != null) {
                                    collectTag.setSelected(false);
                                    collectTag.setTextColor(getResources().getColor(R.color.gray6));
                                }
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
                                if (HttpConnect.isConnnected(Question_Activity.this)) {
                                    if ("1".equals(type)) {
                                        new StudyRequest(Question_Activity.this, collecthandler).DELLCOLLEXT(user.getUserid(), answerlistbean_over.get(ViewPosition).getId(),
                                                "2", CANCELCOLLECT);
                                    } else {
                                        new StudyRequest(Question_Activity.this, collecthandler).DELLCOLLEXT(user.getUserid(), answerlistbean.get(ViewPosition).getId(),
                                                "2", CANCELCOLLECT);
                                    }
                                } else {
                                    Toast.makeText(Question_Activity.this, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (HttpConnect.isConnnected(Question_Activity.this)) {
                                    if ("1".equals(type)) {
                                        if (answerlistbean_over.get(ViewPosition).getPost_description().toString() != null&&answerlistbean_over.get(ViewPosition).getPost_description().toString().length() > 0){
                                            description = answerlistbean_over.get(ViewPosition).getPost_description().toString();
                                        }else{
                                            description = "";
                                        }
                                        new StudyRequest(Question_Activity.this, collecthandler).COLLEXT(user.getUserid(), answerlistbean_over.get(ViewPosition).getId(), "2",
                                                answerlistbean_over.get(ViewPosition).getPost_title(),description,
                                                SETCOLLECT);
                                    } else {
                                        if (answerlistbean.get(ViewPosition).getPost_description().toString() != null&&answerlistbean.get(ViewPosition).getPost_description().toString().length() > 0){
                                            description = answerlistbean.get(ViewPosition).getPost_description().toString();
                                        }else{
                                            description = "";
                                        }
                                        new StudyRequest(Question_Activity.this, collecthandler).COLLEXT(user.getUserid(), answerlistbean.get(ViewPosition).getId(), "2",
                                                answerlistbean.get(ViewPosition).getPost_title(), description,
                                                SETCOLLECT);
                                    }
                                } else {
                                    Toast.makeText(Question_Activity.this, R.string.net_erroy, Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_mian);
        //先清除之前的记录
        Question_hashmap_data.questionList.clear();
        Question_hashmap_data.answerList.clear();
        mactivity = this;
        user = new UserBean(mactivity);
        intent = getIntent();
        dailydata = (Daily_Practice_bean.DataEntity) intent.getSerializableExtra("dailyData");
        dailydatatwo = (Daily_Practice_bean.DataEntity.ChildlistEntity) intent.getSerializableExtra("dailyDatatwo");
        type = intent.getStringExtra("type");
        exam_time = intent.getStringExtra("exam_time");//总时间
        //这里可以写时间的获取，然后分别把得到的小时分和秒赋值给下面这个三个值就行了。
        //hour；minute；second
        if (exam_time == null) {//如果时间为Null，则默认两小时
            hour = 2;
            minute = 0;
            second = 0;
        } else {
            hour = Integer.valueOf(exam_time) / 3600;
            minute = Integer.valueOf(exam_time) % 3600;
            second = Integer.valueOf(exam_time) % 60;
//            Date t = new Date(Long.valueOf(exam_time));
//            hour=t.getHours();
//            minute=t.getMinutes();
//            second=t.getSeconds();

        }

//        questionType = intent.getStringExtra("questionNUM");
//        questionNum = intent.getStringExtra("questioncount");
        gradview_position = intent.getIntExtra("mposition", -1);
        if ("1".equals(type)) {//每日一练
            if (dailydata != null && !"".equals(dailydata)) {
                questionType = dailydata.getTerm_id();
                questionNum = dailydata.getCount();
            } else if (dailydatatwo != null && !"".equals(dailydatatwo)) {
                questionType = dailydatatwo.getTerm_id();
                questionNum = dailydatatwo.getCount();
            }
        } else if ("11".equals(type)) {//在线考试
            if (dailydatatwo != null && !"".equals(dailydatatwo)) {
                questionType = dailydatatwo.getTerm_id();
                questionNum = dailydatatwo.getCount();
            } else if (dailydata != null && !"".equals(dailydata)) {
                questionType = dailydata.getTerm_id();
                questionNum = dailydata.getCount();
            }
        }
        //清除之前的缓存题目对错的缓存
        Question_hashmap_data.sheetmap.clear();
        questionlist = Question_hashmap_data.questionList;
        answerlist = Question_hashmap_data.answerList;
        iniview();
        iniviewdata();
    }

    private void iniview() {
        shuaxin_button = (TextView)findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        submit_image = (ImageView) findViewById(R.id.question_answer_submit_image);
        submit_image.setBackgroundResource(R.mipmap.ic_gou_nor);
        top_title = (TextView) findViewById(R.id.top_title);
        question_answer_submit = (TextView) findViewById(R.id.question_answer_submit);
        question_answer_submit.setText("提交");
        question_answer_submit.setOnClickListener(this);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        vote_submit_viewpager = (VoteSubmitViewPager) findViewById(R.id.vote_submit_viewpager);
        //viewpager
        vote_submit_viewpager.setOffscreenPageLimit(10);
        cpid = (CirclePageIndicator) findViewById(R.id.question_topic_indicator);
        if ("1".equals(type)) {
            top_title.setText("练习题");
            isDaily = true;
            submitAnswer = "1";
        } else if ("11".equals(type)) {
            top_title.setText("模拟考场");
            isDaily = false;
            cpid.setVisibility(View.GONE);
            submitAnswer = "2";
        }
        initViewPagerScroll();

    }


    /**
     * @param index 根据索引值切换页面
     */
    public void setCurrentView(int index) {
        vote_submit_viewpager.setCurrentItem(index);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
//        stopTime();
        minute = -1;
        second = -1;
        super.onDestroy();
    }

    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(vote_submit_viewpager.getContext());
            mScroller.set(vote_submit_viewpager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "试题");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "试题");

    }

    private void iniviewdata() {
        if (!"".equals(questionType) && null != questionType && null != questionNum) {
            if (HttpConnect.isConnnected(mactivity)) {
                if (isDaily) {//每日一练
                    new StudyRequest(mactivity, hanlder).TEXTLIST(user.getUserid(), questionType, questionNum, GETTESTTEXT_OVER);
                } else {//在线考试
                    new StudyRequest(mactivity, hanlder).TEXTLIST(user.getUserid(), questionType, questionNum, GETTESTTEXT);
                }
            } else {
                dialogpgd.dismiss();
                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                mactivity.finish();
                break;

            case R.id.question_answer_submit://提交答案
                question_answer_submit.setVisibility(View.GONE);
                submit_image.setVisibility(View.GONE);
                if (Question_hashmap_data.sheetmap.size() >= 1){
                    submit();
                }else{
                    Toast.makeText(mactivity,"您还没有答题，请答题！",Toast.LENGTH_SHORT).show();
                    question_answer_submit.setVisibility(View.VISIBLE);
                    submit_image.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    public void submit() {

        String questionUri = "{";
        for (int i = 0; i < questionlist.size(); i++) {
            if (i < questionlist.size() - 1) {
                questionUri += questionlist.get(i).toString() + ",";
            } else if (i == questionlist.size() - 1) {
                questionUri += questionlist.get(i).toString();
            }
        }
        questionUri += "}";

        String answerUri = "{";
        for (int i = 0; i < answerlist.size(); i++) {
            if (i < answerlist.size() - 1) {
                answerUri += answerlist.get(i).toString() + ",";
            } else if (i == answerlist.size() - 1) {
                answerUri += answerlist.get(i).toString();
            }
        }
        answerUri += "}";

        if (HttpConnect.isConnnected(mactivity)) {
            new StudyRequest(mactivity, hanlder).SUBMITANSWER(user.getUserid(), submitAnswer, String.valueOf(allpageNum), questionUri, answerUri, SUBMITANSWER);
        } else {
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }


    public static int getGradview_position(int position) {
        gradview_position = position;
        return gradview_position;
    }


    private void startTime() {
        if (timer == null) {
            timer = new Timer();

        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = STARTtIME;
                hanlder.sendMessage(msg);
            }
        };

    }
        if (timer != null && timerTask != null) {
            timer.schedule(timerTask,0,1000);
        }
    }

    private void stopTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    //每日一练和在线考试中的取消添加收藏
    private void isCollect() {
        collectTag = (Button) vote_submit_viewpager.findViewWithTag("collectTag" + ViewPosition);
        if (HttpConnect.isConnnected(mactivity)) {
            if (answer_option.getData().size() > 0) {
                new StudyRequest(mactivity, hanlder).CheckHadFavorite(user.getUserid(), answer_option.getData().get(i).getId()/**题目ID**/, "2", ISCOLLECT);
            }
        } else {
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
        if (collectTag != null) {
            collectTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != user.getUserid() && user.getUserid().length() > 0) {
                        if (HttpConnect.isConnnected(Question_Activity.this)) {
                            new StudyRequest(Question_Activity.this, hanlder).CheckHadFavorite(user.getUserid(), answer_option.getData().get(i).getId(), "2", CHECKHADFAVORITE);
                        } else {
                            Toast.makeText(Question_Activity.this, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(Question_Activity.this, LoginActivity.class);
                        Question_Activity.this.startActivity(intent);
                    }
                }
            });
        }
    }
    // 提交试卷
    public void uploadExamination(int errortopicNum) {
        // TODO Auto-generated method stub
//        String resultlist = "[";
        String resultlist = "";
        errortopicNums = errortopicNum;

        if (questionInfos.size() > 0) {
            //选择过题目
            //全部选中
            if (questionInfos.size() == answerlistbean.size()) {
                for (int i = 0; i < questionInfos.size(); i++) {
                    if (i == questionInfos.size() - 1) {
//                        resultlist += questionInfos.get(i).toString() + "]";
                        resultlist += questionInfos.get(i).toString() + "";
                    } else {
                        resultlist += questionInfos.get(i).toString() + ",";
                    }
                    if (questionInfos.size() == 0) {
//                        resultlist += "]";
                        resultlist += "";
                    }
                    if (questionInfos.get(i).getIs_correct()
                            .equals(ConstantUtil.isCorrect)) {
                        int score = Integer.parseInt(questionInfos.get(i).getScore());
                        pageScore += score;
                    }
                }
            } else {
                //部分选中
                for (int i = 0; i < answerlistbean.size(); i++) {
                    if (answerlistbean.get(i).getIsSelect() == null) {
                        errortopicNums1 += 1;
                        //保存数据
                        SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                        questionInfo.setQuestionId(answerlistbean.get(i).getId());
//                        questionInfo.setRealAnswer(answerlistbean.get(i).getCorrectAnswer());
//                        questionInfo.setScore(answerlistbean.get(i).getScore());
                        questionInfo.setIs_correct(ConstantUtil.isError);
                        questionInfos.add(questionInfo);
                    }
                }

                for (int i = 0; i < answerlistbean.size(); i++) {
                    if (i == answerlistbean.size() - 1) {
//                        resultlist += questionInfos.get(i).toString() + "]";
                        resultlist += questionInfos.get(i).toString() + "";
                    } else {
                        resultlist += questionInfos.get(i).toString() + ",";
                    }
                    if (answerlistbean.size() == 0) {
//                        resultlist += "]";
                        resultlist += "";
                    }
                    if (questionInfos.get(i).getIs_correct()
                            .equals(ConstantUtil.isCorrect)) {
                        int score = Integer.parseInt(questionInfos.get(i).getScore());
                        pageScore += score;
                    }
                }
            }
        } else {
            //没有选择题目
            for (int i = 0; i < answerlistbean.size(); i++) {
                if (answerlistbean.get(i).getIsSelect() == null) {
                    errortopicNums1 += 1;
                    //保存数据
                    SaveQuestionInfo questionInfo = new SaveQuestionInfo();
                    questionInfo.setQuestionId(answerlistbean.get(i).getId());
//                    questionInfo.setRealAnswer(dataItems.get(i).getCorrectAnswer());
//                    questionInfo.setScore(dataItems.get(i).getScore());
                    questionInfo.setIs_correct(ConstantUtil.isError);
                    questionInfos.add(questionInfo);
                }
            }

            for (int i = 0; i < answerlistbean.size(); i++) {
                if (i == answerlistbean.size() - 1) {
//                    resultlist += questionInfos.get(i).toString() + "]";
                    resultlist += questionInfos.get(i).toString() + "";
                } else {
                    resultlist += questionInfos.get(i).toString() + ",";
                }
                if (answerlistbean.size() == 0) {
//                    resultlist += "]";
                    resultlist += "";
                }
                if (questionInfos.get(i).getIs_correct()
                        .equals(ConstantUtil.isCorrect)) {
                    int score = Integer.parseInt(questionInfos.get(i).getScore());
                    pageScore += score;
                }
            }
        }
        System.out.println("提交的已经选择的题目数组给后台====" + resultlist);
    }

    private void isShowDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Question_Activity.this);
        builder.setMessage(title);
        builder.setTitle("提示");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(Question_hashmap_data.sheetmap.size()==(ViewPosition+1)){
                    //弹出正确答案
                    pagerAdapter.PopuwindowAnswer();
                }
                question_answer_submit.setVisibility(View.GONE);
                submit_image.setVisibility(View.GONE);
                if(!isDaily){
                    //下一题按钮隐藏
                    NextQTag = (RelativeLayout) vote_submit_viewpager.findViewWithTag("NextQ" + ViewPosition);
                    NextQTag.setVisibility(View.INVISIBLE);
                    pagerAdapter.GoneNext();
                }

            }
        });
        builder.create().show();
    }

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
}
