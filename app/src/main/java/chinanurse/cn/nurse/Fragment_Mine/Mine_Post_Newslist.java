package chinanurse.cn.nurse.Fragment_Mine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Mine.adapter.Mine_news_Adapter;
import chinanurse.cn.nurse.HttpConn.NetUtil;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.Mine_news_bean;
import chinanurse.cn.nurse.bean.News_bean;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class Mine_Post_Newslist extends Activity implements View.OnClickListener{
    private static final int KEY = 1;
    private PullToRefreshListView pulllist;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private ImageButton imagebutton_bi;
    private Activity mactivity;
    private RelativeLayout ril_shibai,ril_list,btn_back;
    private TextView shuaxin_button,top_title;
    private ProgressDialog dialogpgd;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;
    private News_bean newlist;
    private UserBean user;
    private Mine_news_bean.DataBean minenewsbean;
    private Mine_news_bean.DataBean.ReplyBean newreplay;
    private List<Mine_news_bean.DataBean> minenewslist = new ArrayList<>();
    private List<Mine_news_bean.DataBean.ReplyBean> newsreplay;
    private Mine_news_Adapter mineadapter;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case KEY:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        minenewslist.clear();
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))){
                                JSONArray jsonarray = json.getJSONArray("data");
                                if (jsonarray != null && jsonarray.length() > 0){
                                    for (int i = 0;i < jsonarray.length();i++) {
                                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                                        minenewsbean = new Mine_news_bean.DataBean();
                                        minenewsbean.setId(jsonobj.getString("id"));
                                        minenewsbean.setUserid(jsonobj.getString("userid"));
                                        minenewsbean.setContent(jsonobj.getString("content"));
                                        minenewsbean.setCreate_time(jsonobj.getString("create_time"));
                                        minenewsbean.setStatus(jsonobj.getString("status"));
                                        minenewsbean.setDevicestate(jsonobj.getString("devicestate"));
                                        newsreplay = new ArrayList<>();
                                        newsreplay.clear();
                                        JSONArray jsonreplay = jsonobj.getJSONArray("reply");
                                        for (int j = 0;j < jsonreplay.length();j++){
                                            JSONObject jsonrepplay = jsonreplay.getJSONObject(j);
                                            newreplay = new Mine_news_bean.DataBean.ReplyBean();
                                            newreplay.setId(jsonrepplay.getString("id"));
                                            newreplay.setUserid(jsonrepplay.getString("userid"));
                                            newreplay.setTitle(jsonrepplay.getString("title"));
                                            newreplay.setFid(jsonrepplay.getString("fid"));
                                            newreplay.setContent(jsonrepplay.getString("content"));
                                            newreplay.setCreate_time(jsonrepplay.getString("create_time"));
                                            newreplay.setStatus(jsonrepplay.getString("status"));
                                            newsreplay.add(newreplay);
                                        }
                                        minenewsbean.setReply(newsreplay);
                                        minenewslist.add(minenewsbean);
                                    }
                                }
                                dialogpgd.dismiss();
                                mineadapter = new Mine_news_Adapter(minenewslist,mactivity,0);
                                lv_view.setAdapter(mineadapter);
                                mineadapter.notifyDataSetChanged();
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
                               inidata();
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
        setContentView(R.layout.activity_my__news);
        mactivity = this;
        user = new UserBean(mactivity);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
        getView();
    }

    private void getView() {
        dialogpgd = new ProgressDialog(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT);
//        dialogpgd.setCancelable(false);
        shuaxin_button = (TextView) findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText("我的反馈信息");
        imagebutton_bi = (ImageButton) findViewById(R.id.imagebutton_bi);
        imagebutton_bi.setVisibility(View.VISIBLE);
        imagebutton_bi.setOnClickListener(this);
        pulllist = (PullToRefreshListView) findViewById(R.id.lv_comprehensive);
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
                stopRefresh();
            }
        });
        lv_view = pulllist.getRefreshableView();
        lv_view.setDivider(null);
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
    //停止刷新
    private void stopRefreshnomore() {
        pulllist.postDelayed(new Runnable() {
            @Override
            public void run() {
                pulllist.onPullDownRefreshComplete();
                pulllist.setHasMoreData(false);
                setLastData();
            }
        }, 2000);
    }
    private String formatdatatime(long time) {
        if (0 == time) {
            return "";
        }
        return mdata.format(new Date(time));
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "反馈信息列表");
        inidata();
    }

    private void inidata() {
        if (NetUtil.isConnnected(mactivity)) {
            //user.getUserid()报错 数据的状态 error
            //接口暂时  采用  获取本公司收到简历列表
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            Log.i("onResume", "initData1");
            new StudyRequest(mactivity, handler).getfeedbackList(user.getUserid(), KEY);
        } else {
            Log.i("onResume", "initData2");
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            ril_shibai.setVisibility(View.VISIBLE);
            ril_list.setVisibility(View.GONE);
            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inidata();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "反馈信息列表");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imagebutton_bi:
                Intent intent = new Intent(mactivity,Mine_Post_News.class);
                startActivity(intent);
                break;
            case R.id.btn_back:
                unregisterReceiver(receiver);
                finish();
                break;
        }
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            newstypebean = (News_list_type.DataBean) intent.getSerializableExtra("fndinfo");

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("新通知")
                    .setMessage(newstypebean.getPost_title())
                    .setCancelable(false)
                    .setPositiveButton("立即查看", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("fndinfo", newstypebean);
                            Intent intent = new Intent(Mine_Post_Newslist.this, News_WebView_url.class);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Mine_Post_Newslist.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
            context.unregisterReceiver(this);
        }
    }
}
