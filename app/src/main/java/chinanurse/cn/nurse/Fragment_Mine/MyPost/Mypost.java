package chinanurse.cn.nurse.Fragment_Mine.MyPost;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.adapter.main_adapter.Mine_Post_Adapter;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.bean.mine_main_bean.Mine_Post_bean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

public class Mypost extends AppCompatActivity implements View.OnClickListener {
    private ListView lv_view;
    private LinearLayout back;
    private List<Mine_Post_bean.DataBean> list_post;
    private Mine_Post_Adapter post_adapter;
    private static final int GETCOMMUNITYLIST = 1;
    private Mine_Post_bean mine_post_bean;
    private Activity activity;
    private UserBean user;
    private String type = "1";
    private String ishot = "0";
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private PullToRefreshListView pulllist;
    private TextView detail_loading;
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
                case GETCOMMUNITYLIST:
                    if (msg.obj != null) {
                        list_post.clear();
                        String result = (String) msg.obj;
                        Gson gson = new Gson();
                        mine_post_bean = gson.fromJson(result, Mine_Post_bean.class);
                        if ("success".equals(mine_post_bean.getStatus())) {
                            list_post.addAll(mine_post_bean.getData());
                            post_adapter = new Mine_Post_Adapter(activity, list_post);
                            lv_view.setAdapter(post_adapter);
//                            clalistviewadapter.addList(contentlist);
                            post_adapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);
        back = (LinearLayout) findViewById(R.id.mypost_back);
        activity = this;
        user = new UserBean(activity);
        list_post = new ArrayList<>();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);

//        listView.setAdapter(post_adapter);
        back.setOnClickListener(this);


        postiniview();

    }

    private void postiniview() {
        detail_loading = (TextView) findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView) findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(true);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                fansfragment();
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
    }


//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mypost_back:
                unregisterReceiver(receiver);
                finish();
                break;
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
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this, "Mypost");
        Log.i("onResume", "---------->onResume");
        fansfragment();
    }

    private void fansfragment() {
        if (HttpConnect.isConnnected(this)) {
            new StudyRequest(activity, handler).GETCOMMUNITYLIST(type, ishot, GETCOMMUNITYLIST);
        } else {
            Toast.makeText(this, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            newstypebean = (News_list_type.DataBean) intent.getSerializableExtra("fndinfo");
            String title = intent.getStringExtra("title");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("新通知")
                    .setMessage(title)
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
//            AlertDialog alert = builder.create();
//            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            alert.show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "Mypost");
    }
}
