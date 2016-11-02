package chinanurse.cn.nurse.Fragment_Main;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.Fragment_Nurse_Study_News.Clinic_Nurse;
import chinanurse.cn.nurse.Syudyfragment.DailyPractice;
import chinanurse.cn.nurse.Fragment_Nurse_Study_News.Exam_Canon;
import chinanurse.cn.nurse.Fragment_Nurse_Study_News.gobroad.GoAbroad_main;
import chinanurse.cn.nurse.Fragment_Nurse_Study_News.goman.Goman_Topic_Bank;
import chinanurse.cn.nurse.Fragment_Nurse_Study_News.Nursing_Operation;
import chinanurse.cn.nurse.Fragment_Nurse_Study_News.Paper_Protect;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.WebView.News_WebView;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.list.WaveSwipeRefreshLayout;

/**
 * 学习界面
 * Created by Administrator on 2016/6/2.
 */
public class StudyFragment extends Fragment implements View.OnClickListener ,WaveSwipeRefreshLayout.OnRefreshListener{
    private static final int STUDYPAGETITLE = 1;
    private static final int FOURTHPAGELIST = 2;
    private static final int FIRSTPAGELIST = 3;
    private static final int GETNUM = 444;
    //    public ViewPager study_vp;
    private Banner banner;
    private RelativeLayout stu_every, stu_Goman, stu_online, stu_goabroad, stu_clinic, stu_fifty, stu_book, stu_paper;
    private Context mcontext;
    private Activity mactivity;
    private String title_slide = "6",result;
    private Intent intent;
    //  新建的容器 但是容器的内容暂时延用新闻页的
    private ArrayList<FirstPageNews.DataBean> fndlistlist = new ArrayList<>();
    private ArrayList<FirstPageNews.DataBean> fndlist = new ArrayList<>();
    //解析图片第三方初始化
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private FirstPageNews fndbean;
    private FirstPageNews.DataBean fnd;
    private Gson gson;
    private String[] images,imageTitle;
    private TextView stu_paper_num,tv_jiaziashibai;
    private SharedPreferences preferences;
    private ProgressDialog dialogpgd;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private SharedPreferences prefences;
    private String isopen;
    private UserBean user;
    private long timecurrentTimeMillis;


    //    111
    public Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FOURTHPAGELIST:
                    result = (String) msg.obj;
                    if (result != null){
                        tv_jiaziashibai.setVisibility(View.GONE);
                        banner.setVisibility(View.VISIBLE);
                        fndlistlist.clear();
                        gson = new Gson();
                        fndbean = gson.fromJson(result, FirstPageNews.class);
                        fndlistlist.addAll(fndbean.getData());
                        showImage();
                    }else{
                        tv_jiaziashibai.setVisibility(View.VISIBLE);
                        banner.setVisibility(View.GONE);
                    }
                    break;
                case FIRSTPAGELIST:
                    result = (String) msg.obj;
                    if (result != null){
                        fndlist.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if ("success".equals(jsonObject.optString("status"))){
                                gson = new Gson();
                                fndbean = gson.fromJson(result, FirstPageNews.class);
                                fndlist.addAll(fndbean.getData());
                                if (fndlist != null&&fndlist.size()>0){
                                    preferences=mactivity.getSharedPreferences("nursenum", Context.MODE_PRIVATE);
                                    String num=preferences.getString("fndlist",null);
                                    if (num != null) {
                                        if (fndlist.size() > Integer.valueOf(num)) {
                                            stu_paper_num.setVisibility(View.VISIBLE);
                                            stu_paper_num.setText(String.valueOf(fndlist.size() - Integer.valueOf(num)) + "");
                                        } else {
                                            stu_paper_num.setVisibility(View.GONE);
                                        }
                                    }else{
                                        stu_paper_num.setVisibility(View.VISIBLE);
                                        stu_paper_num.setText(String.valueOf(fndlist.size())+"");
                                    }
                                }else{
                                    stu_paper_num.setVisibility(View.GONE);
                                }
                            }else{
                                stu_paper_num.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        stu_paper_num.setVisibility(View.GONE);
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case GETNUM:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))){
                                String data = json.optString("data");
                                if (data != null &&!"0".equals(data)){
                                    stu_paper_num.setVisibility(View.VISIBLE);
                                    stu_paper_num.setText(String.valueOf(data)+"");
                                }else{
                                    stu_paper_num.setVisibility(View.GONE);
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

    //新写的图片轮播
    private void showImage(){

        if (fndlistlist.size() > 0 && fndlistlist.size() <= 5) {
            images = new String[fndlistlist.size()];
            imageTitle = new String[fndlistlist.size()];
            for (int i = 0; i < fndlistlist.size(); i++) {
                Log.e("hello-----", fndlistlist.get(i).getThumb().get(0).getUrl());
                images[i] = NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlistlist.get(i).getThumb().get(0).getUrl();
            }
            for (int i = 0; i < fndlistlist.size(); i++) {
                imageTitle[i] = fndlistlist.get(i).getPost_title();
            }
        } else if (fndlistlist.size() > 5) {
            images = new String[5];
            imageTitle = new String[5];
            for (int i = 0; i < 5; i++) {
                Log.e("hello-----", fndlistlist.get(i).getThumb().get(0).getUrl());
                images[i] = NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlistlist.get(i).getThumb().get(0).getUrl();
            }
            for (int i = 0; i < 5; i++) {
                imageTitle[i] = fndlistlist.get(i).getPost_title();
            }
        }
        switch (4){
            case 0:
                break;
            case 1:
                //设置样式 显示圆形指示器
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                break;
            case 2:
                //显示数字指示器
                banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
                break;
            case 3:
                //显示数字指示器和标题
                banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
                //记得设置标题列表哦
//                banner.setBannerTitle(imageTitle);
                break;
            case 4:
                //显示圆形指示器和标题
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                //记得设置标题列表哦
                banner.setBannerTitle(imageTitle);
                break;
            case 5:
                //设置指示器居中（CIRCLE_INDICATOR或者CIRCLE_INDICATOR_TITLE模式下）
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                banner.setIndicatorGravity(BannerConfig.CENTER);
                break;
        }

        isopen=prefences.getString("isopen",null);
        banner.setImages(images,isopen);//可以选择设置图片网址，或者资源文件，默认用Glide加载
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {
            @Override
            public void OnBannerClick(View view, int position) {
                fnd = fndlistlist.get(position-1);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fndinfo", fnd);
                Intent intent = new Intent(getActivity(), News_WebView.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = View.inflate(getContext(), R.layout.studyfragment, null);
        mcontext = getActivity();
        mactivity = getActivity();
        user = new UserBean(mactivity);
        banner = (Banner) mView.findViewById(R.id.banner1);
        prefences=getActivity().getSharedPreferences("nursenum", Context.MODE_PRIVATE);
        stu_every = (RelativeLayout) mView.findViewById(R.id.stu_every_relation_title);
        stu_every.setOnClickListener(this);
        stu_Goman = (RelativeLayout) mView.findViewById(R.id.stu_Goman_topic_bank);
        stu_Goman.setOnClickListener(this);
        stu_online = (RelativeLayout) mView.findViewById(R.id.stu_online_test);
        stu_online.setOnClickListener(this);
        stu_goabroad = (RelativeLayout) mView.findViewById(R.id.stu_goabroad_test);
        stu_goabroad.setOnClickListener(this);
        stu_clinic = (RelativeLayout) mView.findViewById(R.id.stu_clinic_nurse);
        stu_clinic.setOnClickListener(this);
        stu_fifty = (RelativeLayout) mView.findViewById(R.id.stu_fifty_nurse_operation);
        stu_fifty.setOnClickListener(this);
        stu_book = (RelativeLayout) mView.findViewById(R.id.stu_test_book);
        stu_book.setOnClickListener(this);
        stu_paper = (RelativeLayout) mView.findViewById(R.id.stu_paper_nurse);
        stu_paper.setOnClickListener(this);
        stu_paper_num = (TextView) mView.findViewById(R.id.stu_paper_num);
        tv_jiaziashibai = (TextView) mView.findViewById(R.id.tv_jiaziashibai);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) mView.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setWaveColor(0xFF90006b);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取数据，网络获取数据并保存
        Log.i("UserData", "------------->+onResume");
        getData();
        //数据显示
        displayData();
        gettime();
    }
    private void gettime() {
//        if (user.getUserid() != null&&user.getUserid().length()>0){
//            timecurrentTimeMillis = System.currentTimeMillis()/1000;
//            Log.e("time","-=---------->"+timecurrentTimeMillis);
//            if (HttpConnect.isConnnected(mactivity)){
//                new StudyRequest(mactivity, handler).getNewslist_count(user.getUserid(),String.valueOf(timecurrentTimeMillis),GETNUM);
//            }else{
//                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
//            }
//        }else{
            if (HttpConnect.isConnnected(mactivity)) {
                new StudyRequest(mactivity, handler).getNewsList("95", FIRSTPAGELIST);
            } else {
                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
//            }
        }
    }
    private void displayData() {

    }

    private void getData() {
        if (HttpConnect.isConnnected(mactivity)) {
            new StudyRequest(mactivity,handler).getNewsList("111",FOURTHPAGELIST);
        } else {
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //每日一练
            case R.id.stu_every_relation_title:
                intent = new Intent(mactivity, DailyPractice.class);
                intent.putExtra("type","1");
                startActivity(intent);
                break;
            //五万道题库
            case R.id.stu_Goman_topic_bank:
                intent = new Intent(mactivity, Goman_Topic_Bank.class);
                intent.putExtra("top_title", "5万道题库");
                startActivity(intent);
                break;
            //在线考试
            case R.id.stu_online_test:
                intent = new Intent(mactivity, DailyPractice.class);
                intent.putExtra("type","11");
                startActivity(intent);

                break;
            //出国考试
            case R.id.stu_goabroad_test:
                intent = new Intent(mactivity, GoAbroad_main.class);
                intent.putExtra("top_title", "出国考试");
                startActivity(intent);
                break;
            //临床护理
            case R.id.stu_clinic_nurse:
                intent = new Intent(mactivity, Clinic_Nurse.class);
                intent.putExtra("top_title", "临床护理");
                startActivity(intent);
                break;
            //50项护理操作
            case R.id.stu_fifty_nurse_operation:
                intent = new Intent(mactivity, Nursing_Operation.class);
                intent.putExtra("top_title", "50项护理操作");
                startActivity(intent);
                break;
            //考试宝典
            case R.id.stu_test_book:
                intent = new Intent(mactivity, Exam_Canon.class);
                intent.putExtra("top_title", "考试宝典");
                startActivity(intent);
                break;
            //护理部
            case R.id.stu_paper_nurse:
                intent = new Intent(mactivity, Paper_Protect.class);
                intent.putExtra("top_title", "护理部");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        getData();
        gettime();
        refresh();
    }
    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 更新が終了したらインジケータ非表示
//                dailylist.clear();
//                dailyadapter.notifyDataSetChanged();

                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);

    }
}
