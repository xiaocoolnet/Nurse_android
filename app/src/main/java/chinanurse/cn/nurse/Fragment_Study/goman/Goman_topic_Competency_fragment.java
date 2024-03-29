package chinanurse.cn.nurse.Fragment_Study.goman;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.publicall.adapter.News_Down_Adapter;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class Goman_topic_Competency_fragment extends Activity implements View.OnClickListener{

    private static final int FIRSTPAGELIST = 4;
    public static final int BTNCHECKHADFAVORITE = 7;
    public static final int CHECKHADFAVORITE = 8;
    private static final int CANCELCOLLECT = 9;
    private static final int SETCOLLECT = 10;
    private static final int RESETLIKE = 11;
    private static final int SETLIKE = 12;
    private News_Down_Adapter lv_Adapter;
    private String channelid,pagetype,result,pagetypename;
    private ArrayList<FirstPageNews.DataBean> fndlist = new ArrayList<>();

    private PullToRefreshListView pulllist;
    private TextView detail_loading,news_first_title;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private Gson gson;
    private FirstPageNews fndbean;
    private int position;
    private UserBean user;
    private RelativeLayout ril_shibai, ril_list,btn_back,ril_rirlw;
    private TextView shuaxin_button,tv_title;
    private ProgressDialog dialogpgd;
    private Dialog dialog;
    private int page = 1;
    private Activity mactivity;
    private Intent intent;
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
                                    lv_Adapter = new News_Down_Adapter(mactivity, fndlist, 4, handler);
                                    lv_view.setAdapter(lv_Adapter);
                                }else {
                                    if (page == 1) {
                                        lv_Adapter = new News_Down_Adapter(mactivity, fndlist, 4, handler);
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
                                gomandata();
                            }
                        });
                    }
                    break;
                case 5:
                    position = (int) msg.obj;
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).CheckHadLike(user.getUserid(), fndlist.get(position).getObject_id(), "1", BTNCHECKHADFAVORITE);
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
//                    initData();
//                    flv_Adapter.notifyDataSetChanged();
                    break;
                case 6:
                    position = (int) msg.obj;
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).CheckHadFavorite(user.getUserid(), fndlist.get(position).getObject_id(), "3", CHECKHADFAVORITE);
                        Log.i("collect1", "-------------------->" + fndlist.get(position).getTerm_id());
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
//                    initData();
//                    flv_Adapter.notifyDataSetChanged();
                    break;
                case BTNCHECKHADFAVORITE:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
//                                webview_sc.setBackgroundResource(R.mipmap.ic_collect_sel);

                                if (HttpConnect.isConnnected(mactivity)) {
                                    new StudyRequest(mactivity, handler).resetLike(user.getUserid(), fndlist.get(position).getObject_id().toString(), "1", RESETLIKE);
                                } else {
                                    Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                webview_sc.setBackgroundResource(R.mipmap.ic_collect_nor);
                                if (HttpConnect.isConnnected(mactivity)) {
                                    //还有个time
                                    new StudyRequest(mactivity, handler).setLike(user.getUserid(), fndlist.get(position).getObject_id().toString(), "1", SETLIKE);
                                } else {
                                    Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
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
                                if (HttpConnect.isConnnected(mactivity)) {
                                    new StudyRequest(mactivity, handler).DELLCOLLEXT(user.getUserid(), fndlist.get(position).getObject_id().toString(),
                                            "3", CANCELCOLLECT);
                                } else {
                                    Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                if (HttpConnect.isConnnected(mactivity)) {
                                    new StudyRequest(mactivity, handler).COLLEXT(user.getUserid(), fndlist.get(position).getObject_id().toString(),
                                            "3", fndlist.get(position).getPost_title().toString(), fndlist.get(position).getPost_excerpt().toString(),
                                            SETCOLLECT);
                                } else {
                                    Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case RESETLIKE://取消赞图标
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                View v = getViewByPosition(position, lv_view);
                                TextView tv = (TextView) v.findViewById(R.id.tv_collect);
                                ImageView iv = (ImageView) v.findViewById(R.id.image_3);
                                iv.setBackgroundResource(R.mipmap.ic_like_gray);
                                if ("0".equals(tv.getText().toString())||tv.getText().toString().length() < 0){
                                    tv.setText("0");
                                }else{
                                    tv.setText(Integer.valueOf(tv.getText().toString()) - 1 + "");
                                }

                                Toast.makeText(mactivity, "取消点赞", Toast.LENGTH_SHORT).show();
                                gomandata();
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
                                View v = getViewByPosition(position, lv_view);
                                TextView tv = (TextView) v.findViewById(R.id.tv_tv_collect);
                                ImageView iv = (ImageView) v.findViewById(R.id.image_collect);
                                iv.setBackgroundResource(R.mipmap.ic_collect_nor);
                                if ("0".equals(tv.getText().toString())||tv.getText().toString().length() < 0){
                                    tv.setText("0");
                                }else{
                                    tv.setText(Integer.valueOf(tv.getText().toString()) - 1 + "");
                                }
                                Toast.makeText(mactivity, "取消收藏", Toast.LENGTH_SHORT).show();
                                gomandata();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case SETLIKE://点赞变化图标
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                JSONObject jsonObject = new JSONObject(data);
                                View v = getViewByPosition(position,lv_view);
                                TextView tv = (TextView) v.findViewById(R.id.tv_collect);
                                ImageView iv = (ImageView) v.findViewById(R.id.image_3);
                                iv.setBackgroundResource(R.mipmap.ic_like_sel);
                                tv.setText(Integer.valueOf(tv.getText().toString()) + 1 + "");
                                Toast.makeText(mactivity, "成功点赞", Toast.LENGTH_SHORT).show();
                                gomandata();
                                if (jsonObject.getString("score") != null &&jsonObject.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("+"+jsonObject.getString("score"));
                                    TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                    tv_score_name.setText(jsonObject.getString("event"));
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(3000);
                                                dialog.dismiss();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case SETCOLLECT:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String data = obj.getString("data");
                            if ("success".equals(status)) {
                                View v = getViewByPosition(position,lv_view);
                                TextView tv = (TextView) v.findViewById(R.id.tv_tv_collect);
                                ImageView iv = (ImageView) v.findViewById(R.id.image_collect);
                                iv.setBackgroundResource(R.mipmap.ic_collect_sel);
                                tv.setText(Integer.valueOf(tv.getText().toString()) + 1 + "");
                                Toast.makeText(mactivity,"成功收藏", Toast.LENGTH_SHORT).show();
                                gomandata();
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
        setContentView(R.layout.competnecy_fragment_layout);
        mactivity = this;
        user = new UserBean(mactivity);
        intent = getIntent();
        channelid = intent.getStringExtra("pagetypeid");
        pagetypename = intent.getStringExtra("pagetypename");
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
        competency();
    }

    private void competency() {
        ril_rirlw = (RelativeLayout) findViewById(R.id.ril_rirlw);
        ril_rirlw.setVisibility(View.VISIBLE);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(pagetypename);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        shuaxin_button = (TextView) findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout)findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(mactivity, AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        detail_loading = (TextView) findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView)findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(true);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                gomandata();
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
        StatService.onPageStart(this, "Goman_topic_Competency_fragment");
        gomandata();
    }
    private void getnewslistother() {
        if (HttpConnect.isConnnected(mactivity)){
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            if (user.getUserid() != null && user.getUserid().length() > 0) {
                new StudyRequest(mactivity, handler).getNewsListtwo(channelid, String.valueOf(page),user.getUserid(),"1", FIRSTPAGELIST);
            }else{
                new StudyRequest(mactivity, handler).getNewsListtwo(channelid, String.valueOf(page),"","", FIRSTPAGELIST);
            }
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
    private void gomandata() {
        if (HttpConnect.isConnnected(mactivity)){
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            page = 1;
            if (user.getUserid() != null && user.getUserid().length() > 0) {
                new StudyRequest(mactivity, handler).getNewsListtwo(channelid, String.valueOf(page),user.getUserid(),"1", FIRSTPAGELIST);
            }else{
                new StudyRequest(mactivity, handler).getNewsListtwo(channelid, String.valueOf(page),"","", FIRSTPAGELIST);
            }
        }else{
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            ril_shibai.setVisibility(View.VISIBLE);
            ril_list.setVisibility(View.GONE);
            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gomandata();
                }
            });
        }
    }
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
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

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
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
        StatService.onPageEnd(this, "Goman_topic_Competency_fragment");
    }
}
