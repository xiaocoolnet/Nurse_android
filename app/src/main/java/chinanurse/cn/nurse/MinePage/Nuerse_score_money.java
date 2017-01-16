package chinanurse.cn.nurse.MinePage;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

import chinanurse.cn.nurse.Fragment_Mine.Myinfo;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.GoAbroad_chest_WebView;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.adapter.Score_adapter;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.Scroe_bean;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import chinanurse.cn.nurse.popWindow.Pop_shared_activity_Activity;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class Nuerse_score_money extends AppCompatActivity implements View.OnClickListener{
    private static final int GETSCORE = 1;
    private static final int GETSCORESHARE = 2;
    private static final int MYGRTUSERINFO = 3;
    private PullToRefreshListView pulllist;
    private Button shared_score;
    private RelativeLayout btn_back,community_write_release;
    private TextView title_top,textview,score_tv_btn;
    private Activity mactivity;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private UserBean user;
    private Scroe_bean scorebean;
    private List<Scroe_bean.DataBean> scorelist;
    private Score_adapter scoreadapter;
    private ImageView score_image;
    private Pop_shared_activity_Activity popshared;
    private String all_info,alltv;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;
    private StringBuilder actionText;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GETSCORE:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        Gson gson = new Gson();
                        scorebean = gson.fromJson(result,Scroe_bean.class);
                        scorelist = new ArrayList<>();
                        scorelist.addAll(scorebean.getData());
                        scoreadapter = new Score_adapter(scorelist,mactivity,0);
                        lv_view.setAdapter(scoreadapter);
                    }
                    break;
                case MYGRTUSERINFO:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                JSONObject obj = new JSONObject(data);
                                user.setUserid(obj.getString("id"));
                                Log.i("userid", "--------->" + user.getUserid());
                                user.setName(obj.getString("name"));
                                user.setPhone(obj.getString("phone"));
                                user.setCity(obj.getString("city"));
                                user.setEmail(obj.getString("email"));
                                user.setQq(obj.getString("qq"));
                                user.setWeixin(obj.getString("weixin"));
                                user.setPhoto(obj.getString("photo"));
                                user.setSchool(obj.getString("school"));
                                user.setAddress(obj.getString("address"));
                                user.setMajor(obj.getString("major"));
                                user.setEducation(obj.getString("education"));
                                user.setFanscount(obj.getString("fanscount"));
                                user.setMoney(obj.getString("money"));
                                user.setSex(obj.getString("sex"));
                                user.setRealname(obj.getString("realname"));
                                user.setBirthday(obj.getString("birthday"));
                                all_info = obj.getString("all_information");
                                if (user.getRealname().length() <= 0||user.getBirthday().length() <= 0||user.getAddress().length() <= 0||user.getMajor().length() <= 0||user.getEducation().length() <= 0
                                        ||user.getRealname() == null||user.getBirthday() == null||user.getAddress() == null||user.getMajor() == null||user.getEducation() == null){
                                    new AlertDialog.Builder(mactivity).setMessage("请完善个人资料后重试")
                                            .setPositiveButton("取消",new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).setNegativeButton("现在就去", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(mactivity, Myinfo.class);
                                            startActivity(intent);
                                        }
                                    }).create().show();
                                }else {
                                    popshared.showAsDropDown(textview);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_score_money);
        mactivity = this;
        user = new UserBean(mactivity);
        popshared = new Pop_shared_activity_Activity(mactivity);
        scoreiew();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
    }

    private void scoreiew() {
        score_tv_btn = (TextView) findViewById(R.id.score_tv_btn);
        textview = (TextView) findViewById(R.id.textview);
        community_write_release = (RelativeLayout) findViewById(R.id.community_write_release);
        community_write_release.setOnClickListener(this);
        score_image = (ImageView) findViewById(R.id.question_answer_submit_image);
        score_image.setBackgroundResource(R.mipmap.score_image);
        pulllist = (PullToRefreshListView) findViewById(R.id.lv_comprehensive);
        shared_score = (Button) findViewById(R.id.shared_score);
        shared_score.setOnClickListener(this);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        title_top  = (TextView) findViewById(R.id.top_title);
        title_top.setText("个人积分");
        alltv = "\t\t成为中国护士网会员，会享受100余项积分功能和政策。我们会有积分兑换商城，定期会有积分活动，让您真正享受到您的每一份支持都将获得我们的真情回报。点击查看>>";
        actionText = new StringBuilder();
        actionText.append("<a style=\"text-decoration:none;\" href='username'>"
                        + "\t\t成为中国护士网会员，会享受100余项积分功能和政策。我们会有积分兑换商城，定期会有积分活动，让您真正享受到您的每一份支持都将获得我们的真情回报。" + "</a>");
        actionText.append("<a style=\"color:white;text-decoration:none;\" href='点击查看>>'>"
                        + "点击查看>>" + "</a>");
        score_tv_btn.setText(Html.fromHtml(actionText.toString()));
        score_tv_btn.setMovementMethod(LinkMovementMethod
                .getInstance());
        CharSequence text = score_tv_btn.getText();
        int ends = text.length();
        Spannable spannable = (Spannable) score_tv_btn.getText();
        URLSpan[] urlspan = spannable.getSpans(alltv.length()-6, ends, URLSpan.class);
        SpannableStringBuilder stylesBuilder = new SpannableStringBuilder(text);
        stylesBuilder.clearSpans(); // should clear old spans
        for (URLSpan url : urlspan) {
            TextViewURLSpan myURLSpan = new TextViewURLSpan(url.getURL());
            stylesBuilder.setSpan(myURLSpan, spannable.getSpanStart(url),
                    spannable.getSpanEnd(url), spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        score_tv_btn.setText(stylesBuilder);

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
        //获取当前时间
        setLastData();
//        pulllist.doPullRefreshing(true, 500);
        lv_view = pulllist.getRefreshableView();
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
               mactivity.finish();
               break;
           case R.id.community_write_release:
               Intent intent = new Intent(mactivity,Nurse_score_sort.class);
               startActivity(intent);
               break;
           case R.id.shared_score:
               if (HttpConnect.isConnnected(mactivity)) {
                   new StudyRequest(mactivity, handler).getuserinfo(user.getUserid(), MYGRTUSERINFO);
               } else {
                   Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
               }

               break;
       }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "我的积分");
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "我的积分");
    }
    private void initData() {
        if (HttpConnect.isConnnected(mactivity)){
            new StudyRequest(mactivity, handler).getRanking_User(user.getUserid(), GETSCORE);
        }else{
            Toast.makeText(mactivity,R.string.net_erroy,Toast.LENGTH_SHORT).show();
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
    private class TextViewURLSpan extends ClickableSpan {
        private String clickString;

        public TextViewURLSpan(String clickString) {
            this.clickString = clickString;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mactivity.getResources().getColor(R.color.white));
            ds.setUnderlineText(true); //添加下划线
            ds.clearShadowLayer();
        }

        @Override
        public void onClick(View widget) {
            avoidHintColor(widget);
            if (clickString.equals("点击查看>>")) {
                Intent itnent = new Intent(mactivity,GoAbroad_chest_WebView.class);
                itnent.putExtra("chesttype","11");
                startActivity(itnent);
            }
        }
    }
    private void avoidHintColor(View view){
        if(view instanceof TextView)
            ((TextView)view).setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

}
