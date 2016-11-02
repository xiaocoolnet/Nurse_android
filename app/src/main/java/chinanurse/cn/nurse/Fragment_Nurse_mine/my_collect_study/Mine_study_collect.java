package chinanurse.cn.nurse.Fragment_Nurse_mine.my_collect_study;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext.Mine_Collect_First_Adapter;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_study;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class Mine_study_collect extends AppCompatActivity {

    private static final int GETMYRECIVERESUMELIST = 6;
    private RelativeLayout btn_back;
    private Intent intent;
    private TextView top_title;
    private Mine_Collect_First_Adapter lv_Adapter;
    //  新建的容器 但是容器的内容暂时延用新闻页的
    private ArrayList<FirstPageNews.DataBean> fndlist = new ArrayList<>();

    private PullToRefreshListView pulllist;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private TextView detail_loading;
    private Activity mactivity;
    private FirstPageNews fndbean;
    private String result;
    private Gson gson;
    private UserBean user;
    private ProgressDialog dialogpgd;
    private RelativeLayout ril_shibai, ril_list;
    private TextView shuaxin_button;

    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETMYRECIVERESUMELIST:
                    if (msg.obj != null) {
                        result = (String) msg.obj;
                        fndlist.clear();
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                gson = new Gson();
                                fndbean = gson.fromJson(result, FirstPageNews.class);
                                fndlist.addAll(fndbean.getData());
                                lv_Adapter = new Mine_Collect_First_Adapter(mactivity, fndlist, 1);
                                lv_view.setAdapter(lv_Adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
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
        setContentView(R.layout.activity_paper__protect);
        mactivity = this;
        user = new UserBean(mactivity);
        intent = getIntent();
        initData();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
    }
    private void initData() {
        dialogpgd = new ProgressDialog(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT);
//        dialogpgd.setCancelable(false);
        ril_shibai = (RelativeLayout) findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        detail_loading = (TextView) findViewById(R.id.detail_loading);
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

        setLastData();
//        pulllist.doPullRefreshing(true, 500);
        lv_view = pulllist.getRefreshableView();

        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText("其他收藏");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterReceiver(receiver);
                finish();
            }
        });
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
        inidata();
    }

    private void inidata() {
        if (HttpConnect.isConnnected(mactivity)){
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            new StudyRequest(mactivity, handler).SETCOLLEXT(user.getUserid(), "3", GETMYRECIVERESUMELIST);
        }else{
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            Log.i("onResume", "initData2");
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
