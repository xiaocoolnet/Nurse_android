package chinanurse.cn.nurse.Syudyfragment;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.adapter.study_adapter.DailyPractice_adapter;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.bean.study_main_bean.Daily_Practice_bean;
import chinanurse.cn.nurse.list.WaveSwipeRefreshLayout;

/**
 * Created by Administrator on 2016/7/23.
 */
public class DailyPractice extends AppCompatActivity implements View.OnClickListener,WaveSwipeRefreshLayout.OnRefreshListener{
    private static final int GETTESTLISTTITLE = 1;
    private ExpandableListView main_list;

    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private RelativeLayout btn_back;
    private TextView top_title;
    private Activity mactivity;
    private UserBean user;
    private String type;
    private Intent intent;
    private Daily_Practice_bean dailybean;
    private List<Daily_Practice_bean.DataEntity> dailylist = new ArrayList<Daily_Practice_bean.DataEntity>();
    private DailyPractice_adapter dailyadapter;
    private List<List<Daily_Practice_bean.DataEntity>> dailyshow = new ArrayList<List<Daily_Practice_bean.DataEntity>>();
    private List<Map<String,String>> daily = new ArrayList<Map<String,String>>();
//    private List<Map<String,String>> dailymin = new ArrayList<Map<String,String>>();
    private List<List<Map<String, String>>> childs = new ArrayList<List<Map<String,String>>>();
    private RelativeLayout ril_shibai,ril_list;
    private TextView shuaxin_button;
    private ProgressDialog dialogpgd;

    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETTESTLISTTITLE:
                    if (msg.obj != null) {
                        dailylist.clear();
                        String result = (String) msg.obj;
                        if (result != null){
                            ril_shibai.setVisibility(View.GONE);
                            ril_list.setVisibility(View.VISIBLE);
                            Gson gson = new Gson();
                            dailybean = gson.fromJson(result, Daily_Practice_bean.class);
                            dailylist.addAll(dailybean.getData());
                            dailyadapter = new DailyPractice_adapter(mactivity,dailylist,type,user.getUserid());
                            main_list.setAdapter(dailyadapter);
                            for(int i = 0; i <dailyadapter.getGroupCount() ; i++){
                                main_list.expandGroup(i);
                            }
                            dailyadapter.notifyDataSetChanged();
                            dialogpgd.dismiss();
                        }else{
                            dialogpgd.dismiss();
                            ril_shibai.setVisibility(View.VISIBLE);
                            ril_list.setVisibility(View.GONE);
                            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    getindata();
                                }
                            });
                        }

                    }
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailypractice);
        mactivity = this;
        user = new UserBean(mactivity);
        intent = getIntent();
        type = intent.getStringExtra("type");
        initView();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
    }
    private void initView() {
        shuaxin_button = (TextView) findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout)findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        dialogpgd=new ProgressDialog(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setWaveColor(0xFF90006b);
        //mWaveSwipeRefreshLayout.setMaxDropHeight(1500);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        top_title = (TextView) findViewById(R.id.top_title);
        if("1".equals(type)){
            top_title.setText(R.string.stu_every_relation_title_text);
        }else if ("11".equals(type)){
            top_title.setText(R.string.stu_online_test_text);
        }
        main_list = (ExpandableListView) findViewById(R.id.main_list);//
        main_list.setGroupIndicator(null);
        main_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
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

    @Override
    protected void onResume() {

        refresh();
        super.onResume();
        getindata();
    }

    private void getindata() {
        mWaveSwipeRefreshLayout.setRefreshing(false);
        if (!"".equals(type)&&null != type){
            if (HttpConnect.isConnnected(mactivity)) {
                dialogpgd.setMessage("正在加载...");
                dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialogpgd.show();
                new StudyRequest(mactivity, handler).getstudyTitle(user.getUserid(),type, GETTESTLISTTITLE);
            } else {
                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                Log.i("onResume", "initData2");
                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                ril_shibai.setVisibility(View.VISIBLE);
                ril_list.setVisibility(View.GONE);
                shuaxin_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getindata();
                    }
                });
            }
        }
    }

    @Override
    public void onRefresh() {
        getindata();
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mWaveSwipeRefreshLayout.setRefreshing(true);
//            dailyadapter.notifyDataSetChanged();
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        }
    }
}
