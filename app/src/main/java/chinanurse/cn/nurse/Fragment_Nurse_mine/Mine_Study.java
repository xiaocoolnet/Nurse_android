package chinanurse.cn.nurse.Fragment_Nurse_mine;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Nurse_mine.my_collect_study.Mine_study_collect;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.MinePage.ChartView;
import chinanurse.cn.nurse.Fragment_Nurse_mine.my_collect_study.Question_Collect;
import chinanurse.cn.nurse.Fragment_Nurse_mine.my_collect_study.Question_Error;
import chinanurse.cn.nurse.Fragment_Nurse_mine.my_collect_study.Question_Over;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.bean.Mine_study_zhexian_bean;
import chinanurse.cn.nurse.bean.MyCollectQuestion_Bean;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.Question_hashmap_data;
import chinanurse.cn.nurse.bean.UserBean;

public class Mine_Study extends AppCompatActivity implements View.OnClickListener {

    private static final int GETSYN = 1;
    private static final int GETECAMPAPER = 2;
    private static final int GETECAMPAPERONE = 3;
    private static final int GETMYRECIVERESUMELIST = 4;
    private static final int GETlINECHARDATA = 5;
    private String[] xlabel, ylabel;
    private int[] datas;
    private int height_, width_, width, height;
    private String title = "", type;
    ChartView chartView;
    private RelativeLayout rela_qusetion_record, rela_error_question, rela_collect_question, rela_other_collect;
    private Intent intent;
    private Activity activity;
    private UserBean user;

    private LinearLayout mystudy_back;
    private int rates_averagel;
    private TextView tv_num_01, tv_num_02, tv_num_04, tv_num_03;
    private String result;
    private List<MyCollectQuestion_Bean.DataBean> list_first = new ArrayList<>();
    private FirstPageNews fndbean;
    private int numone, numtwo;
    private String[] xlist;
    private int[] ylist;
    private Mine_study_zhexian_bean.DataBean zhexianData;
    private Mine_study_zhexian_bean zhexianbean;
    private Context context;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETSYN:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                JSONObject jsonobj = new JSONObject(json.getString("data"));
                                rates_averagel = jsonobj.getInt("rates_average");
                                tv_num_01.setText(rates_averagel + "");
                                tv_num_02.setText(String.valueOf(rates_averagel * rates_averagel) + "");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        tv_num_01.setText("0");
                        tv_num_02.setText("0");
                    }
                    break;
                case GETECAMPAPER:
                    list_first.clear();
                    result = (String) msg.obj;
                    if (result != null) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            if ("success".equals(status)) {
                                Question_hashmap_data.MyCollectList.clear();
                                Gson gson = new Gson();
                                MyCollectQuestion_Bean quesbean = gson.fromJson(result, MyCollectQuestion_Bean.class);
                                Question_hashmap_data.MyCollectList.addAll(quesbean.getData());
                                list_first.addAll(quesbean.getData());
                                if (list_first != null && list_first.size() > 0) {
                                    numone = list_first.size();
                                } else {
                                    numone = 0;
                                }
                                if (HttpConnect.isConnnected(activity)) {
                                    new StudyRequest(activity, handler).GetExampaper(user.getUserid(), "2", GETECAMPAPERONE);//获取我做过的考试题(2在线考试)
                                } else {
                                    Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                numone = 0;
                                if (HttpConnect.isConnnected(activity)) {
                                    new StudyRequest(activity, handler).GetExampaper(user.getUserid(), "2", GETECAMPAPERONE);//获取我做过的考试题(2在线考试)
                                } else {
                                    Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        numone = 0;
                        if (HttpConnect.isConnnected(activity)) {
                            new StudyRequest(activity, handler).GetExampaper(user.getUserid(), "2", GETECAMPAPERONE);//获取我做过的考试题(2在线考试)
                        } else {
                            Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case GETECAMPAPERONE:
                    list_first.clear();
                    result = (String) msg.obj;
                    if (result != null){
                    JSONObject jsonObjectone;
                    try {
                        jsonObjectone = new JSONObject(result);
                        String status = jsonObjectone.getString("status");
                        if ("success".equals(status)) {
                            Question_hashmap_data.MyCollectList.clear();
                            Gson gson = new Gson();
                            MyCollectQuestion_Bean quesbean = gson.fromJson(result, MyCollectQuestion_Bean.class);
                            Question_hashmap_data.MyCollectList.addAll(quesbean.getData());
                            list_first.addAll(quesbean.getData());
                            if (list_first != null && list_first.size() > 0) {
                                numtwo = list_first.size();
                            } else {
                                numtwo = 0;
                            }
                            tv_num_03.setText(String.valueOf(numone + numtwo));
                        }else{
                            numtwo = 0;
                            tv_num_03.setText(String.valueOf(numone + numtwo));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}else{
                        numtwo = 0;
                        tv_num_03.setText(String.valueOf(numone + numtwo));
                    }
                    break;
                case GETMYRECIVERESUMELIST:
                    result = (String) msg.obj;
                    if (result != null){
                    list_first.clear();
                    JSONObject jsonObjectteo;
                    try {
                        jsonObjectteo = new JSONObject(result);
                        String status = jsonObjectteo.getString("status");
                        if ("success".equals(status)) {
                            Question_hashmap_data.MyCollectList.clear();
                            Gson gson = new Gson();
                            MyCollectQuestion_Bean quesbean = gson.fromJson(result, MyCollectQuestion_Bean.class);
                            Question_hashmap_data.MyCollectList.addAll(quesbean.getData());
                            list_first.addAll(quesbean.getData());
                            if (list_first != null && list_first.size() > 0) {
                                tv_num_04.setText(list_first.size() + "");
                            } else {
                                tv_num_04.setText("0");
                            }
                        }else{
                            tv_num_04.setText("0");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}else{
                        tv_num_04.setText("0");
                    }
                    break;
                case GETlINECHARDATA:
                    String result = (String) msg.obj;
                    if (result != null){
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))){
                                Gson gson = new Gson();
                                Log.e("1213",result);
                                zhexianbean = gson.fromJson(result,Mine_study_zhexian_bean.class);//Mine_study_zhexian_bean
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
                                long time1 = Long.parseLong(String.valueOf(zhexianbean.getData().getCreate_time_1()));
                                long time2 = Long.parseLong(String.valueOf(zhexianbean.getData().getCreate_time_2()));
                                long time3 = Long.parseLong(String.valueOf(zhexianbean.getData().getCreate_time_3()));
                                long time4 = Long.parseLong(String.valueOf(zhexianbean.getData().getCreate_time_4()));
                                long time5 = Long.parseLong(String.valueOf(zhexianbean.getData().getCreate_time_5()));
                                long time6 = Long.parseLong(String.valueOf(zhexianbean.getData().getCreate_time_6()));
                                long time7 = Long.parseLong(String.valueOf(zhexianbean.getData().getCreate_time_7()));
                                String timeone = simpleDateFormat.format(new Date(time1 * 1000));
                                String timetwo = simpleDateFormat.format(new Date(time2 * 1000));
                                String timeothree = simpleDateFormat.format(new Date(time3 * 1000));
                                String timefour = simpleDateFormat.format(new Date(time4 * 1000));
                                String timefive = simpleDateFormat.format(new Date(time5 * 1000));
                                String timesix = simpleDateFormat.format(new Date(time6 * 1000));
                                String timeseven = simpleDateFormat.format(new Date(time7 * 1000));
//                        获取屏幕宽高
                                WindowManager wm = activity.getWindowManager();
                                width = wm.getDefaultDisplay().getWidth();
                                height = wm.getDefaultDisplay().getHeight();
                                chartView = (ChartView) findViewById(R.id.chartview_zhexian);
                                height_ = (height / 4) / 8;
                                width_ = width / 8;
                                xlist = new String[]{timeone + "", timetwo + "", timeothree + "", timefour + "", timefive + "", timesix + "", timeseven + ""};
                                ylabel = new String[]{"0", "20", "40", "60", "80", "100"};
                                ylist = new int[]{zhexianbean.getData().getRate_1(), zhexianbean.getData().getRate_2(), zhexianbean.getData().getRate_3(), zhexianbean.getData().getRate_4(),
                                        zhexianbean.getData().getRate_5(), zhexianbean.getData().getRate_6(), zhexianbean.getData().getRate_7()};
//
                                Log.e("height_", "===========" + height + "--------" + width);
                                chartView.init_View(activity, xlist, ylabel, ylist, "", width_, height_);
                                chartView.setPoint(60, height / 4 - 20);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        //无值
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
                        xlabel = new String[getWeekDay().length];
                        for (int i = 0; i < getWeekDay().length; i++) {
                            Date[] date = getWeekDay();

                            xlabel[i] = dateFormat.format(date[i]);
                            Log.e("data", xlabel[i] + "-------------------");
                        }
                        height_ = (height / 4) / 8;
                        width_ = width / 8;
                        datas = new int[]{0, 0, 0,0, 0, 0, 0};
//        xlabel = new String[]{"7-11", "7-12", "7-13", "7-14", "7-15", "7-16", "7-17"};
                        ylabel = new String[]{"0", "20", "40", "60", "80", "100"};
                        Log.e("height_", "===========" + height + "--------" + width);
                        chartView.init_View(activity, xlabel, ylabel, datas, "", width_, height_);
                        chartView.setPoint(60, height / 4 - 20);
                    }
                    break;

            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine__study);
        activity = this;
        user = new UserBean(activity);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
        if (HttpConnect.isConnnected(activity)) {
            new StudyRequest(activity, handler).GetLineChartData(user.getUserid(), GETlINECHARDATA);//获取折线图数据
        } else {
            Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
        intent = getIntent();
        zhexianData = (Mine_study_zhexian_bean.DataBean) intent.getSerializableExtra("zhexian");
        type = intent.getStringExtra("pagetype");
        initData();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initData() {

        mystudy_back = (LinearLayout) findViewById(R.id.mystudy_back);
        mystudy_back.setOnClickListener(this);
        rela_qusetion_record = (RelativeLayout) findViewById(R.id.rela_qusetion_record);
        rela_qusetion_record.setOnClickListener(this);
        rela_error_question = (RelativeLayout) findViewById(R.id.rela_error_question);
        rela_error_question.setOnClickListener(this);
        rela_collect_question = (RelativeLayout) findViewById(R.id.rela_collect_question);
        rela_collect_question.setOnClickListener(this);
        rela_other_collect = (RelativeLayout) findViewById(R.id.rela_other_collect);
        rela_other_collect.setOnClickListener(this);
        tv_num_01 = (TextView) findViewById(R.id.tv_num_01);
        tv_num_02 = (TextView) findViewById(R.id.tv_num_02);
        tv_num_04 = (TextView) findViewById(R.id.tv_num_04);
        tv_num_03 = (TextView) findViewById(R.id.tv_num_03);
        if ("0".equals(type)) {//有值
//        获取屏幕宽高
        WindowManager wm = activity.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        chartView = (ChartView) findViewById(R.id.chartview_zhexian);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long time1 = Long.parseLong(String.valueOf(zhexianData.getCreate_time_1()));
        long time2 = Long.parseLong(String.valueOf(zhexianData.getCreate_time_2()));
        long time3 = Long.parseLong(String.valueOf(zhexianData.getCreate_time_3()));
        long time4 = Long.parseLong(String.valueOf(zhexianData.getCreate_time_4()));
        long time5 = Long.parseLong(String.valueOf(zhexianData.getCreate_time_5()));
        long time6 = Long.parseLong(String.valueOf(zhexianData.getCreate_time_6()));
        long time7 = Long.parseLong(String.valueOf(zhexianData.getCreate_time_7()));
        String timeone = simpleDateFormat.format(new Date(time1 * 1000)).substring(5);
        String timetwo = simpleDateFormat.format(new Date(time2 * 1000)).substring(5);
        String timeothree = simpleDateFormat.format(new Date(time3 * 1000)).substring(5);
        String timefour = simpleDateFormat.format(new Date(time4 * 1000)).substring(5);
        String timefive = simpleDateFormat.format(new Date(time5 * 1000)).substring(5);
        String timesix = simpleDateFormat.format(new Date(time6 * 1000)).substring(5);
        String timeseven = simpleDateFormat.format(new Date(time7 * 1000)).substring(5);
        xlist = new String[]{timeone + "", timetwo + "", timeothree + "", timefour + "", timefive + "", timesix + "", timeseven + ""};
        ylabel = new String[]{"0", "20", "40", "60", "80", "100"};
        ylist = new int[]{zhexianData.getRate_1(), zhexianData.getRate_2(), zhexianData.getRate_3(), zhexianData.getRate_4(),
                zhexianData.getRate_5(), zhexianData.getRate_6(), zhexianData.getRate_7()};
            height_ = (height / 4) / 8;
            width_ = width / 8;
            Log.e("height_", "===========" + height + "--------" + width);
            chartView.init_View(this, xlist, ylabel, ylist, "", width_, height_);
            chartView.setPoint(60, height / 4 - 20);
        } else if ("1".equals(type)) {
            //        获取屏幕宽高
            WindowManager wm = activity.getWindowManager();
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight();
            chartView = (ChartView) findViewById(R.id.chartview_zhexian);
            //无值
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
            xlabel = new String[getWeekDay().length];
            for (int i = 0; i < getWeekDay().length; i++) {
                Date[] date = getWeekDay();

                xlabel[i] = dateFormat.format(date[i]);
                Log.e("data", xlabel[i] + "-------------------");
            }
            height_ = (height / 4) / 8;
            width_ = width / 8;
            datas = new int[]{0, 0, 0,0, 0, 0, 0};
//        xlabel = new String[]{"7-11", "7-12", "7-13", "7-14", "7-15", "7-16", "7-17"};
            ylabel = new String[]{"0", "20", "40", "60", "80", "100"};
            Log.e("height_", "===========" + height + "--------" + width);
            chartView.init_View(this, xlabel, ylabel, datas, "", width_, height_);
            chartView.setPoint(60, height / 4 - 20);

        }
    }

    //  从本周一开始获取   本周 的 日期；
    public static Date[] getWeekDay() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        Date[] dates = new Date[7];
        for (int i = 0; i < 7; i++) {
            dates[i] = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mystudy_back:
                unregisterReceiver(receiver);
                finish();
                break;

            case R.id.rela_qusetion_record:
                intent = new Intent(this, Question_Over.class);
                startActivity(intent);
                break;

            case R.id.rela_error_question:
                intent = new Intent(this, Question_Error.class);
                startActivity(intent);
                break;


            case R.id.rela_collect_question:
                intent = new Intent(this, Question_Collect.class);
                startActivity(intent);
                break;

            case R.id.rela_other_collect:
                intent = new Intent(this, Mine_study_collect.class);
                startActivity(intent);
                break;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        inidata();
    }

    private void inidata() {
        if (HttpConnect.isConnnected(activity)) {
            new StudyRequest(activity, handler).getSynAccuracy(user.getUserid(), GETSYN);
            new StudyRequest(activity, handler).GetExampaper(user.getUserid(), "1", GETECAMPAPER);//获取我做过的考试题(1每日一练)
            new StudyRequest(activity, handler).SETCOLLEXT(user.getUserid(), "2", GETMYRECIVERESUMELIST);//收藏
            new StudyRequest(activity, handler).GetLineChartData(user.getUserid(), GETlINECHARDATA);//获取折线图数据

        } else {
            Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }
//   private void test(){
//
//
//    LineCharView lcv=(LineCharView) findViewById(R.id.test);
//    List<String> x_coords=new ArrayList<String>();
//    x_coords.add("1");
//    x_coords.add("2");
//    x_coords.add("3");
//    x_coords.add("4");
//    x_coords.add("5");
//    x_coords.add("6");
//    x_coords.add("7");
//    x_coords.add("8");
//    x_coords.add("9");
//    x_coords.add("10");
//    x_coords.add("11");
//    x_coords.add("12");
//    x_coords.add("13");
//    x_coords.add("14");
//    x_coords.add("15");
//    x_coords.add("16");
//    x_coords.add("17");
//    x_coords.add("18");
//
//    List<String> x_coord_values=new ArrayList<String>();
//    x_coord_values.add("A");
//    x_coord_values.add("B");
//    x_coord_values.add("C");
//    x_coord_values.add("D");
//    x_coord_values.add("C");
//    x_coord_values.add("A");
//    x_coord_values.add("B");
//    x_coord_values.add("A");
//    x_coord_values.add("D");
//    x_coord_values.add("A");
//    x_coord_values.add("C");
//    x_coord_values.add("C");
//    x_coord_values.add("A");
//    x_coord_values.add("B");
//    x_coord_values.add("A");
//    x_coord_values.add("D");
//    x_coord_values.add("A");
//    x_coord_values.add("A");
//    lcv.setBgColor(Color.BLACK);
//    lcv.setValue(x_coords, x_coord_values);
//   }
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        newstypebean = (News_list_type.DataBean) intent.getSerializableExtra("fndinfo");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("是否点击查看")
                .setCancelable(false)
                .setPositiveButton("立即查看", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("fndinfo", newstypebean);
                        Intent intent = new Intent(activity, News_WebView_url.class);
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
        context.unregisterReceiver(this);
//        AlertDialog alert = builder.create();
//        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        alert.show();
    }
}
}
