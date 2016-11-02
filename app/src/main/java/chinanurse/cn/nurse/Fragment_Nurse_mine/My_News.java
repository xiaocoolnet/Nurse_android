package chinanurse.cn.nurse.Fragment_Nurse_mine;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.NetUtil;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.MinePage.MyNews.Mine_News_Adapter;
import chinanurse.cn.nurse.MinePage.MyNews.Mine_News_Bean;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.bean.News_bean;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView_delet;
import chinanurse.cn.nurse.publicall.adapter.News_Down_Adapter;

public class My_News extends AppCompatActivity implements View.OnClickListener {


    private RelativeLayout btn_back;
    private TextView title_top;
    private Activity activity;
    private UserBean user;
    private static final int KEY = 333;
    public static final int KEYSTART = 22;
    public static final int DELENEWS = 66;
    public static final int KEYSTARTTWO = 4;
    private ArrayList<News_list_type.DataBean> fndlist = new ArrayList<>();
    private ArrayList<String> fndlistone = new ArrayList<>();
    private ArrayList<News_list_type.DataBean.LikesBean> fndlistLike ;
    private ArrayList<News_list_type.DataBean.ThumbBean> fndlistthumb ;
    private ArrayList<News_list_type.DataBean.FavoritesBean> fndlistfavotites ;
    private News_list_type.DataBean fndbean;;
    private Mine_News_Adapter news_adapter;
    private ListView lv_view;

    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private TextView detail_loading, question_answer_submit;
    private PullToRefreshListView_delet pulllist;
    private RelativeLayout ril_shibai, ril_list;
    private TextView shuaxin_button;
    private ProgressDialog dialogpgd;
    private String result;
    private int position;

    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;
    private News_bean newlist;


    private Handler handler = new Handler(Looper.myLooper()) {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEY:
                    if (msg.obj != null) {
                        result = (String) msg.obj;
                        ril_shibai.setVisibility(View.GONE);
                        ril_list.setVisibility(View.VISIBLE);
                        fndlist.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if ("success".equals(jsonObject.optString("status"))){
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if (jsonArray != null &&jsonArray.length() > 0){
                                      JSONObject json;
                                    for (int i = 0;i<jsonArray.length();i++){
                                        json = jsonArray.getJSONObject(i);
                                        fndbean = new News_list_type.DataBean();;
                                        fndbean.setObject_id(json.getString("object_id"));
                                        fndbean.setTerm_id(json.getString("term_id"));
                                        fndbean.setTerm_name(json.getString("term_name"));
                                        fndbean.setPost_title(json.getString("post_title"));
                                        fndbean.setPost_excerpt(json.getString("post_excerpt"));
                                        fndbean.setPost_date(json.getString("post_date"));
                                        fndbean.setPost_modified(json.getString("post_modified"));
                                        fndbean.setPost_source(json.getString("post_source"));
                                        fndbean.setPost_like(json.getString("post_like"));
                                        fndbean.setPost_hits(json.getString("post_hits"));
                                        fndbean.setRecommended(json.getString("recommended"));
                                        fndbean.setSmeta(json.getString("smeta"));
                                        fndbean.setIstop(json.getString("istop"));
                                        fndbean.setMessage_id(json.getString("message_id"));
                                        fndlistLike = new ArrayList<>();
                                        fndlistLike.clear();
                                        JSONArray jsonArraylike = json.getJSONArray("likes");
                                        for (int j = 0;j < jsonArraylike.length();j++){
                                            JSONObject jsonObjectli9ke = jsonArraylike.getJSONObject(j);
                                            News_list_type.DataBean.LikesBean fndbeanlike = new News_list_type.DataBean.LikesBean();
                                            fndbeanlike.setUserid(jsonObjectli9ke.getString("userid"));
                                            fndbeanlike.setAvatar(jsonObjectli9ke.getString("avatar"));
                                            fndbeanlike.setUsername(jsonObjectli9ke.getString("username"));
                                            fndlistLike.add(fndbeanlike);
                                        }
                                        fndbean.setLikes(fndlistLike);
                                        fndlistthumb = new ArrayList<>();
                                        fndlistthumb.clear();
                                        JSONArray jsonArraythumb = json.getJSONArray("thumb");
                                        for (int j = 0;j < jsonArraythumb.length();j++){
                                            JSONObject jsonObjectthumb = jsonArraythumb.getJSONObject(j);
                                            News_list_type.DataBean.ThumbBean fndbeanthumb = new News_list_type.DataBean.ThumbBean();
                                            fndbeanthumb.setAlt(jsonObjectthumb.getString("alt"));
                                            fndbeanthumb.setUrl(jsonObjectthumb.getString("url"));
                                            fndlistthumb.add(fndbeanthumb);
                                        }
                                        fndbean.setThumb(fndlistthumb);
                                        fndlistfavotites = new ArrayList<>();
                                        fndlistfavotites.clear();
                                        JSONArray jsonArrayfavorite = json.getJSONArray("favorites");
                                        for (int j = 0;j < jsonArrayfavorite.length();j++){
                                            JSONObject jsonObjectfavorite = jsonArrayfavorite.getJSONObject(j);
                                            News_list_type.DataBean.FavoritesBean fndbeanfavorite = new News_list_type.DataBean.FavoritesBean();
                                            fndbeanfavorite.setUserid(jsonObjectfavorite.getString("userid"));
                                            fndlistfavotites.add(fndbeanfavorite);
                                        }
                                        fndbean.setFavorites(fndlistfavotites);
                                        fndlist.add(fndbean);

                                    }
                                }
                                if (NetUtil.isConnnected(activity)) {
                                    new StudyRequest(activity, handler).getMessagereadlist(user.getUserid(),KEYSTART);
                                } else {
                                    Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
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
                case 5:
                    position = (int) msg.obj;
                    Log.i("position","--------------->"+position);
                    if (NetUtil.isConnnected(activity)) {
                        new StudyRequest(activity, handler).delMySystemMessag(user.getUserid(),fndlist.get(position).getMessage_id(), DELENEWS);
                    } else {
                        Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DELENEWS:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if ("success".equals(jsonObject.getString("status"))){
                                Toast.makeText(activity, "成功删除消息", Toast.LENGTH_SHORT).show();
                                Message msgone = Message.obtain();
                                msgone.what = 6;
                                msgone.obj = 7;
                                handler.sendMessage(msgone);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case KEYSTART:
                    if (msg.obj != null) {
                        result = (String) msg.obj;
                        fndlistone.clear();
                        try {
                            JSONObject json = new JSONObject(result);
                            String data = json.getString("data");
                            if ("success".equals(json.optString("status"))){
                                JSONArray jsonArray = json.getJSONArray("data");
                                if (jsonArray != null &&jsonArray.length() > 0){
                                    for (int i = 0;i < jsonArray.length();i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        fndlistone.add(jsonObject.getString("refid"));
                                    }
                                    news_adapter = new Mine_News_Adapter(activity, fndlist,fndlistone,handler);
                                    lv_view.setAdapter(news_adapter);
                                    news_adapter.notifyDataSetChanged();
                                    dialogpgd.dismiss();
                                }
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case KEYSTARTTWO:

                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                Message msgone = Message.obtain();
                                msgone.what = 6;
                                msgone.obj = 7;
                                handler.sendMessage(msgone);
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 6:
                    if (msg.obj != null){
                        initData();
                        news_adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__news);
        activity = this;
        user = new UserBean(activity);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);

        Intent intent = getIntent();
// 通知标题
        String title = intent.getStringExtra("title");
// 通知内容
        String summary = intent.getStringExtra("summary");
// 通知额外参数
        String extraMap = intent.getStringExtra("extraMap");

        question_answer_submit = (TextView) findViewById(R.id.question_answer_submit);
        question_answer_submit.setOnClickListener(this);
        question_answer_submit.setText("标记已读");
        question_answer_submit.setTextSize(16);
        shuaxin_button = (TextView) findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(activity, android.app.AlertDialog.THEME_HOLO_LIGHT);
//        dialogpgd.setCancelable(false);
        detail_loading = (TextView) findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView_delet) findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(true);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
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
        ril_shibai = (RelativeLayout) findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        title_top = (TextView) findViewById(R.id.top_title);
        title_top.setText(R.string.main_my_news);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News_list_type.DataBean fndData = fndlist.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fndinfo", fndData);
                Intent intent = new Intent(activity, News_WebView_url.class);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                if (NetUtil.isConnnected(activity)) {
                    new StudyRequest(activity, handler).GETREAD(user.getUserid(), fndlist.get(position).getMessage_id(), KEYSTARTTWO);
                } else {
                    Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_back:
                unregisterReceiver(receiver);
                finish();
                break;
            case R.id.question_answer_submit:
                if (fndlist != null &&fndlist.size() > 0){
                    if (NetUtil.isConnnected(activity)) {
                        new StudyRequest(activity, handler).GETREAD(user.getUserid(), "", KEYSTARTTWO);
                    } else {
                        Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                }
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

    private String formatdatatime(long time) {
        if (0 == time) {
            return "";
        }
        return mdata.format(new Date(time));
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (NetUtil.isConnnected(activity)) {
            //user.getUserid()报错 数据的状态 error
            //接口暂时  采用  获取本公司收到简历列表
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            Log.i("onResume", "initData1");
            new StudyRequest(activity, handler).get_systemmessage(user.getUserid(), KEY);
        } else {
            Log.i("onResume", "initData2");
            Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
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
            builder.setMessage("是否点击查看")
                    .setCancelable(false)
                    .setPositiveButton("立即查看", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("fndinfo", newstypebean);
                            Intent intent = new Intent(My_News.this, News_WebView_url.class);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            My_News.this.startActivity(intent);
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

}
