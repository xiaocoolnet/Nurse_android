package chinanurse.cn.nurse.Fragment_Nurse_Abroad.Abroad_Firstpage;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.WebView.GoAbroad_chest_WebView;
import chinanurse.cn.nurse.WebView.News_WebView;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.publicall.adapter.News_Down_Adapter;
import chinanurse.cn.nurse.adapter.News_Title_Adapter;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * Created by Administrator on 2016/7/24.
 */
public class Abroad_first_abroad extends AppCompatActivity implements View.OnClickListener{
    private static final int FIRTPAGETITLE = 1;
    private static final int FIRSTPAGELIST = 2;
    private static final int ADDSCORE = 3;
    private Activity mactivity;
    private String title_image_name,title_slide,abroadtype,result;
    //解析图片第三方初始化
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private News_Title_Adapter vp_Adapter;

    private Intent intent;
    private ArrayList<FirstPageNews.DataBean> fndlist = new ArrayList<>();
    private ArrayList<FirstPageNews.DataBean> fndlistlist = new ArrayList<>();

    private News_Down_Adapter lv_Adapter;
    private String country;
    private TextView top_title,detail_loading,question_answer_submit;
    private RelativeLayout btn_back;
    private SharedPreferences sp;
    private PullToRefreshListView pulllist;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private View viewH;
    private FirstPageNews fndbean;
    private FirstPageNews.DataBean fnd;
    private Banner banner;
    private Gson gson;
    private String[] images, imageTitle;
    private UserBean user;
    private RelativeLayout ril_shibai,ril_list;
    private TextView shuaxin_button;
    private ProgressDialog dialogpgd;
    private int page = 1;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;


    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FIRSTPAGELIST:
                    result = (String) msg.obj;
                    if (result != null) {
                        ril_shibai.setVisibility(View.GONE);
                        ril_list.setVisibility(View.VISIBLE);
                        if (page == 1) {
                            fndlist.clear();
                        }
                        try {
                            JSONObject json = new JSONObject(result);
                            String data = json.getString("data");
                            if ("success".equals(json.optString("status"))){
                                gson = new Gson();
                                fndbean = gson.fromJson(result, FirstPageNews.class);
                                fndlist.addAll(fndbean.getData());
                                if (lv_Adapter==null){
                                    lv_Adapter = new News_Down_Adapter(mactivity, fndlist, 0, handler);
                                    lv_view.setAdapter(lv_Adapter);
                                }else {
                                    if (page == 1) {
                                        lv_Adapter = new News_Down_Adapter(mactivity, fndlist, 0, handler);
                                        lv_view.setAdapter(lv_Adapter);
                                    }else {
                                        lv_Adapter.notifyDataSetChanged();
                                    }
                                }
                                stopRefresh();
                            }else{
                                if ("end".equals(data)){
                                    Toast.makeText(mactivity,"无更新数据",Toast.LENGTH_SHORT).show();
                                    stopRefresh();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialogpgd.dismiss();
                    } else {
                        stopRefresh();
                        dialogpgd.dismiss();
                        ril_shibai.setVisibility(View.VISIBLE);
                        ril_list.setVisibility(View.GONE);
                        shuaxin_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initDataview();
                            }
                        });
                    }
                    break;
                case FIRTPAGETITLE:
                    result = (String) msg.obj;
                    if (result != null) {
                        ril_shibai.setVisibility(View.GONE);
                        ril_list.setVisibility(View.VISIBLE);
                        fndlistlist.clear();
                        gson = new Gson();
                        fndbean = gson.fromJson(result, FirstPageNews.class);
                        fndlistlist.addAll(fndbean.getData());
                        showImage();
                    }else{
                        dialogpgd.dismiss();
                        ril_shibai.setVisibility(View.VISIBLE);
                        ril_list.setVisibility(View.GONE);
                        shuaxin_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                initDataview();
                            }
                        });
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
//                banner.setBannerTitle(titles);
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
        banner.setImages(images,"");//可以选择设置图片网址，或者资源文件，默认用Glide加载
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {
            @Override
            public void OnBannerClick(View view, int position) {
                fnd = fndlistlist.get(position-1);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fndinfo", fnd);
                Intent intent = new Intent(mactivity, News_WebView.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        mactivity = this;
        user = new UserBean(mactivity);
        intent = getIntent();
        abroadtype = intent.getStringExtra("abroadtype");
        country = intent.getStringExtra("country");
        title_slide = intent.getStringExtra("title_slide");
        iniviewabroad();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
    }

    private void iniviewabroad() {
        shuaxin_button = (TextView) findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        dialogpgd=new ProgressDialog(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT);
        question_answer_submit = (TextView) findViewById(R.id.question_answer_submit);
        question_answer_submit.setText("报名");
        question_answer_submit.setOnClickListener(this);
        detail_loading = (TextView) findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView) findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(true);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initDataview();
                stopRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (fndlist.size()%20 != 0){
                    stopRefresh();
                    Toast.makeText(mactivity,"无更新数据",Toast.LENGTH_SHORT).show();
                    return;
                }
                page = page+1;
                try{
                    getnewslistother();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        setLastData();
//        pulllist.doPullRefreshing(true, 500);
        lv_view = pulllist.getRefreshableView();
        viewH = LayoutInflater.from(this).inflate(R.layout.firstpagenew, null);
        lv_view.addHeaderView(viewH);
        lv_view.setDivider(null);
        sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        top_title = (TextView) findViewById(R.id.top_title);
        if (null != country&&country.length() > 0){
            top_title.setText(country);
        }else{
            top_title.setText("");
        }
        banner = (Banner) viewH.findViewById(R.id.banner1);//新 图片轮播
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FirstPageNews.DataBean fndData = fndlist.get(position-1);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fndinfo", fndData);
                    bundle.putString("position", String.valueOf(position));
                    Intent intent = new Intent(mactivity, News_WebView.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                }
        });
    }
    private void getnewslistother() {
        if (null != abroadtype&&abroadtype.length() > 0){
            if (HttpConnect.isConnnected(mactivity)) {
                dialogpgd.setMessage("正在加载...");
                dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialogpgd.show();
                Log.i("onResume", "initData3");
                new StudyRequest(mactivity, handler).getNewsListother(abroadtype,String.valueOf(page), FIRSTPAGELIST);
            } else {
                Log.i("onResume", "initData4");
                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                ril_shibai.setVisibility(View.VISIBLE);
                ril_list.setVisibility(View.GONE);
                shuaxin_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getnewslistother();
                    }
                });
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("1111", "---------->onResume");
        initDataview();
    }
    private void initDataview() {
        if (null != abroadtype&&abroadtype.length() > 0){
            if (HttpConnect.isConnnected(mactivity)) {
                dialogpgd.setMessage("正在加载...");
                dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialogpgd.show();
                Log.i("onResume", "initData3");
                page = 1;
                new StudyRequest(mactivity, handler).getNewsListother(abroadtype,String.valueOf(page), FIRSTPAGELIST);
                new StudyRequest(mactivity, handler).getNewsList(title_slide, FIRTPAGETITLE);

            } else {
                Log.i("onResume", "initData4");
                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                ril_shibai.setVisibility(View.VISIBLE);
                ril_list.setVisibility(View.GONE);
                shuaxin_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initDataview();
                    }
                });
            }
        }
    }
    //获取当前时间
    private void setLastData() {
        String text = formatdatatime(System.currentTimeMillis());
        pulllist.setLastUpdatedLabel(text);
        Log.i("time", "-------->" + text);
    }
    //停止刷新
    private void stopRefresh() {
        pulllist.postDelayed(new Runnable() {
            @Override
            public void run() {
                pulllist.onPullUpRefreshComplete();
                pulllist.onPullDownRefreshComplete();
                setLastData();
            }
        }, 2000);
    }
    private String formatdatatime(long time){
        if (0==time){
            return "";
        }
        return mdata.format(new Date(time));
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.btn_back:
               unregisterReceiver(receiver);
               finish();
               break;
           case R.id.question_answer_submit:
               Intent intent = new Intent(mactivity,GoAbroad_chest_WebView.class);
               intent.putExtra("chesttype","10");
               startActivity(intent);
               break;
       }
    }
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
                            Intent intent = new Intent(mactivity, News_WebView_url.class);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mactivity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
            context.unregisterReceiver(this);
//            AlertDialog alert = builder.create();
//            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            alert.show();
        }
    }
}
