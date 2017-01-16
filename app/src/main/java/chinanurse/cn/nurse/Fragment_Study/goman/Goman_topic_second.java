package chinanurse.cn.nurse.Fragment_Study.goman;

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
import android.widget.AdapterView;
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

import chinanurse.cn.nurse.Fragment_Study.adapter.Goman_topic_Second_Adapetr;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * Created by Administrator on 2016/11/5.
 */

public class Goman_topic_second extends Activity implements View.OnClickListener{
    private static final int FIRSTPAGELIST = 1;
    private RelativeLayout btn_back,ril_shibai,ril_list;
    private TextView tv_title,shuaxin_button;
    private PullToRefreshListView pulllist;
    private ListView lv_view;
    private ProgressDialog dialogpgd;
    private Activity mactivity;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;
    private Intent intent;
    private String firstpage,secondpage;
    private List<String> listpagename = new ArrayList<>();
    private List<String> listpageid = new ArrayList<>();
    private String pageid;
    private Goman_topic_Second_Adapetr secondadapter;
    private RelativeLayout title_ril;


    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FIRSTPAGELIST:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        listpagename.clear();
                        listpageid.clear();
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))){
                                JSONArray jsonarray = json.getJSONArray("data");
                                if (jsonarray != null &&jsonarray.length() > 0){
                                for (int i = 0;i < jsonarray.length();i++){
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    listpagename.add(jsonobject.getString("name"));
                                    listpageid.add(jsonobject.getString("term_id"));
                                }
                                }
                                secondadapter = new Goman_topic_Second_Adapetr(listpagename,mactivity);
                                lv_view.setAdapter(secondadapter);
                                secondadapter.notifyDataSetChanged();
                                dialogpgd.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        stopRefresh();
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
        setContentView(R.layout.goman_topic_second);
        mactivity = this;
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
        intent = getIntent();
        firstpage = intent.getStringExtra("firstpage");
        secondpage = intent.getStringExtra("secondpage");
        goman();
    }

    private void goman() {
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(secondpage);
        title_ril = (RelativeLayout) findViewById(R.id.title_ril);
        title_ril.setVisibility(View.VISIBLE);
        shuaxin_button = (TextView) findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        shuaxin_button = (TextView) findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT);
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
        //获取当前时间
        setLastData();
//        pulllist.doPullRefreshing(true, 500);
        lv_view = pulllist.getRefreshableView();
        lv_view.setDivider(null);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mactivity,Goman_topic_Competency_fragment.class);
                intent.putExtra("pagetypeid",listpageid.get(position));
                intent.putExtra("pagetypename",listpagename.get(position));
                startActivity(intent);
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
        StatService.onPageStart(this, "8万道题库——列表");
        inidata();
    }

    private void inidata() {
        if ("护士资格".equals(firstpage)&&"模拟考试".equals(secondpage)){
            pageid = "185";
        }else if ("护士资格".equals(firstpage)&&"历年真题".equals(secondpage)){
            pageid = "186";
        }else if ("护士资格".equals(firstpage)&&"考试冲刺".equals(secondpage)){
            pageid = "187";
        }else if ("护士资格".equals(firstpage)&&"辅导精华".equals(secondpage)){
            pageid = "188";
        }else if ("护士资格".equals(firstpage)&&"押题密卷".equals(secondpage)){
            pageid = "190";
        }else if ("护士资格".equals(firstpage)&&"核心考点".equals(secondpage)){
            pageid = "189";
        }else if ("初级护师".equals(firstpage)&&"模拟考试".equals(secondpage)){
            pageid = "191";
        }else if ("初级护师".equals(firstpage)&&"历年真题".equals(secondpage)){
            pageid = "192";
        }else if ("初级护师".equals(firstpage)&&"考试冲刺".equals(secondpage)){
            pageid = "193";
        }else if ("初级护师".equals(firstpage)&&"核心考点".equals(secondpage)){
            pageid = "195";
        }else if ("初级护师".equals(firstpage)&&"押题密卷".equals(secondpage)){
            pageid = "196";
        }else if ("初级护师".equals(firstpage)&&"辅导精华".equals(secondpage)){
            pageid = "194";
        }else if ("主管护师".equals(firstpage)&&"模拟考试".equals(secondpage)){
            pageid = "197";
        }else if ("主管护师".equals(firstpage)&&"历年真题".equals(secondpage)){
            pageid = "198";
        }else if ("主管护师".equals(firstpage)&&"考试冲刺".equals(secondpage)){
            pageid = "199";
        }else if ("主管护师".equals(firstpage)&&"核心考点".equals(secondpage)){
            pageid = "201";
        }else if ("主管护师".equals(firstpage)&&"押题密卷".equals(secondpage)){
            pageid = "202";
        }else if ("主管护师".equals(firstpage)&&"辅导精华".equals(secondpage)){
            pageid = "200";
        }
        if (HttpConnect.isConnnected(mactivity)){
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            new StudyRequest(mactivity, handler).getNewsTitleFirst(pageid,FIRSTPAGELIST);

        }else{
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
    public void onClick(View v) {
        switch (v.getId()){
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




    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "8万道题库——列表");
    }
}
