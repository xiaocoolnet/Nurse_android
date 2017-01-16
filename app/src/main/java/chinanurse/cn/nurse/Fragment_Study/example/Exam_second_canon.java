package chinanurse.cn.nurse.Fragment_Study.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_study;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import chinanurse.cn.nurse.publicall.adapter.News_Down_Adapter;

/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class Exam_second_canon extends Activity{
    private Bundle bundle;
    private String id,name;
    private Activity mactivity;
    private RelativeLayout btn_back;
    private Intent intent;
    private TextView top_title;
    private News_Down_Adapter lv_Adapter;
    private static final int FIRSTPAGELIST = 4;
    private String channelid = "15";
    //  新建的容器 但是容器的内容暂时延用新闻页的
    private ArrayList<FirstPageNews.DataBean> fndlist = new ArrayList<>();

    private PullToRefreshListView pulllist;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private TextView detail_loading;
    private FirstPageNews fndbean;
    private String result;
    private Gson gson;
    private UserBean user;
    private RelativeLayout ril_shibai,ril_list;
    private TextView shuaxin_button;
    private ProgressDialog dialogpgd;
    private int page = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
                                initData();
                            }
                        });
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstlistview);
        mactivity = this;
        intent = getIntent();
        bundle = intent.getExtras();
        id = bundle.getString("pageid");
        initData();
    }
    private void initData() {
        shuaxin_button = (TextView) findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout)findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout)findViewById(R.id.ril_list);
        dialogpgd=new ProgressDialog(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        detail_loading = (TextView) findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView)findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(true);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                inidata();
                stopRefresh();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (fndlist.size()%20 != 0){
                    stopRefresh();
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
        lv_view.setDivider(null);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FirstPageNews.DataBean fndData = fndlist.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fndinfo", fndData);
                bundle.putString("position", String.valueOf(position));
                Intent intent = new Intent(mactivity, News_WebView_study.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
        inidata();

    }

    private void getnewslistother() {
        if (HttpConnect.isConnnected(mactivity)){
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            new StudyRequest(mactivity, handler).getNewsListother(id,String.valueOf(page),FIRSTPAGELIST);
        }else{
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
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "考试宝典子页列表");
        inidata();
    }

    private void inidata() {
        if (HttpConnect.isConnnected(mactivity)){
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            page = 1;
            new StudyRequest(mactivity, handler).getNewsListother(id,String.valueOf(page),FIRSTPAGELIST);
        }else{
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            ril_shibai.setVisibility(View.VISIBLE);
            ril_list.setVisibility(View.GONE);
            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initData();
                }
            });
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "考试宝典子页列表");
    }
}
