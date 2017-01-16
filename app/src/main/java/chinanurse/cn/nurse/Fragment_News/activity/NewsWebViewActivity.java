package chinanurse.cn.nurse.Fragment_News.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.WebView.webview_comments_bean.Webview_comments_bean;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.publicall.SecondPage;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.adapter.Pop_Adapter_Choice;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.popWindow.Pop_EditText;
import chinanurse.cn.nurse.popWindow.Pop_shared_Activity;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.weibo.AccessTokenKeeper;
import chinanurse.cn.nurse.weibo.Constants;
import cz.msebera.android.httpclient.Header;


/**
 * 新闻界面列表详情
 */
public class NewsWebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "NewsWebViewActivity";
    private static final int SETLIKE = 3;
    private static final int RESETLIKE = 4;
    private static final int SETCOLLECT = 5;
    private static final int CANCELCOLLECT = 6;
    private static final int GETLIKE = 7;
    private static final int GETTITLENAME = 8;
    private static final int CHECKHADFAVORITE = 9;
    private static final int GETSTHECURRENT = 10;
    private static final int BTNCHECKHADFAVORITE = 11;
    private static final int GETNEWSLIKSE = 12;
    private static final int REQUEST_CODE = 2;
    private static final int ADDSCORE = 13;
    private static final int GETREFCOMMENTS = 14;
    private static final int MAX_DECODE_PICTURE_SIZE = 15;
    private Pop_shared_Activity popshared;
    //微信
    private IWXAPI iwxapi;
    private String IWXAPI_ID = "wxdd50558e711439e8";
    //微博
    /**
     * 微博分享的接口实例
     */
    private IWeiboShareAPI mWeiboShareAPI;
    private boolean isChecked = true;

    private Pop_Adapter_Choice popchoice;
    private int weblike = 1, like;
    private Intent intentuser;
    private static final int FIRSTPAGELIST_ABOUT = 1;
    private Activity mactivity;
    private WebView webView;
    private ListView web_list;
    private ScrollView news_scroll;
    private Pop_EditText popedittext;
    private LinearLayout shared_three_dot;
    private ImageView webview_like, webview_ff, webview_sc;
    private RelativeLayout web_back, wechat, friend, weibo, image_choice;
    private RelativeLayout rl_webview;
    private TextView web_title, web_from, web_read, web_time, about_read, web_like, adbout_read_view, textview, tv_choice_num, et_iamge_choice, linear_list;
    private Intent intent;
    private String likesize, titletype, Titletype, position, type;
    private String refid = "1";
    private String title, time, read, source, webId, webview, new_webview, webtitleId, result;
    private FirstPageNews.DataBean fndData;
    private UserBean user;
    private String pagertype, description, pageid, path;
    private TextView title_main;
    private int collectNum = 1;
    private Webview_comments_bean comments;
    private List<Webview_comments_bean.DataBean> commentslist = new ArrayList<>();
    private Dialog dialog;
    private int positionlist;
    private Bitmap bitmap;
    private Long commentCount = 0L;
    public int pager = 1;
    private TextView tv_more;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;
    private Bitmap thumbBmp;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SETLIKE://点赞变化图标
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject jsonobj = new JSONObject(result);
                            String status = jsonobj.getString("status");
                            String data = jsonobj.getString("data");
                            //bt
                            if (status.equals("success") && !"已经点赞！".equals(data)) {
                                JSONObject json = new JSONObject(data);
                                webview_like.setBackgroundResource(R.mipmap.img_like);
                                web_like.setText(String.valueOf(Integer.valueOf(web_like.getText().toString()) + 1));
                                ToastUtils.ToastShort(mactivity, "成功点赞");
                                if (json.getString("score") != null && json.getString("score").length() > 0) {
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    dialog.getWindow().clearFlags(
                                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("+" + json.getString("score"));
                                    TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                    tv_score_name.setText(json.getString("event"));
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
                case RESETLIKE://取消赞图标
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            if (status.equals("success")) {
                                webview_like.setBackgroundResource(R.mipmap.img_like_nol);
                                if (0 != Integer.valueOf(web_like.getText().toString())) {
                                    web_like.setText(String.valueOf(Integer.valueOf(web_like.getText().toString()) - 1));
                                }
                                ToastUtils.ToastShort(mactivity, "取消点赞");
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
                            if ("success".equals(status)) {
                                webview_sc.setBackgroundResource(R.mipmap.ic_collect_sel);
                                ToastUtils.ToastShort(mactivity, getString(R.string.question_collect_succcess));

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
                                webview_sc.setBackgroundResource(R.mipmap.btn_collect_sel);
                                ToastUtils.ToastShort(mactivity, getString(R.string.question_cancel_collect));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case GETTITLENAME:
                    if (msg.obj != null) {
                        String mTitle = (String) msg.obj;
                        LogUtils.i("newfragment", "------------------>" + mTitle);
                        //解析JSON数据
                        JSONObject mJson;
                        try {
                            //解析JSON数据
                            mJson = new JSONObject(mTitle);
                            //。。获取data数组
                            JSONArray mJarray = mJson.getJSONArray("data");
                            if (mJarray != null || mJarray.length() > 0) {
                                for (int i = 0; i < mJarray.length(); i++) {
                                    JSONObject temp = (JSONObject) mJarray.get(i);
                                    String termid = temp.getString("term_id");
                                    String termname = temp.getString("name");
                                    Intent intent = new Intent(mactivity, SecondPage.class);
                                    intent.putExtra("termid", termid);
                                    intent.putExtra("termname", termname);
                                    mactivity.startActivity(intent);
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
                                    new StudyRequest(mactivity, handler).DELLCOLLEXT(user.getUserid(), webId,
                                            "1", CANCELCOLLECT);
                                } else {
                                    ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                                }

                            } else {
                                if (HttpConnect.isConnnected(mactivity)) {
                                    new StudyRequest(mactivity, handler).COLLEXT(user.getUserid(), webId,
                                            "1", web_title.getText().toString(), description,
                                            SETCOLLECT);
                                } else {
                                    ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case GETSTHECURRENT://判断是否收藏过
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                webview_sc.setBackgroundResource(R.mipmap.ic_collect_sel);
                            } else {
                                webview_sc.setBackgroundResource(R.mipmap.btn_collect_sel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case GETLIKE://判断是否点过赞
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                webview_like.setBackgroundResource(R.mipmap.img_like);
                            } else {
                                webview_like.setBackgroundResource(R.mipmap.img_like_nol);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case BTNCHECKHADFAVORITE://检测是否点赞
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                if (HttpConnect.isConnnected(mactivity)) {
                                    new StudyRequest(mactivity, handler).resetLike(user.getUserid(), webId, "1", RESETLIKE);
                                } else {
                                    ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                                }
                            } else {
                                if (HttpConnect.isConnnected(mactivity)) {
                                    //还有个time
                                    new StudyRequest(mactivity, handler).setLike(user.getUserid(), webId, "1", SETLIKE);
                                } else {
                                    ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                                }
                            }
                            collect();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case ADDSCORE:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
                                JSONObject json = new JSONObject(obj.getString("data"));
                                if (json.getString("score") != null && json.getString("score").length() > 0) {
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("积分\t\t+" + json.getString("score"));
                                    TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                    tv_score_name.setText(json.getString("event"));
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(1000);
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
                case GETREFCOMMENTS:
                    String result = (String) msg.obj;
                    if (result != null) {
                        if (pager == 1) {
                            commentslist.clear();
                        }
                        LogUtils.e(TAG, "-result-->" + result.toString());
                        Gson gson = new Gson();
                        comments = gson.fromJson(result, Webview_comments_bean.class);
                        commentslist.addAll(comments.getData());
                        if (commentslist != null && commentslist.size() > 0) {
                            if (popchoice == null) {
                                popchoice = new Pop_Adapter_Choice(commentslist, mactivity, 0, handler);
                                web_list.setAdapter(popchoice);
                            }
                            popchoice.setCommentCount(commentCount);
                            popchoice.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(web_list);
                            tv_choice_num.setText("" + commentCount);
                            linear_list.setVisibility(View.GONE);
                            web_list.setVisibility(View.VISIBLE);
                        } else {
                            tv_choice_num.setText("0");
                            linear_list.setVisibility(View.VISIBLE);
                            web_list.setVisibility(View.GONE);
                        }
                        if (commentslist != null && commentslist.size() >= commentCount) {
                            tv_more.setVisibility(View.GONE);
                        } else {
                            tv_more.setVisibility(View.VISIBLE);//TODO
                        }
                    } else {
                        tv_choice_num.setText("0");
                        linear_list.setVisibility(View.VISIBLE);
                        web_list.setVisibility(View.GONE);
                    }
                    break;
                case 15:
                    positionlist = (int) msg.obj;
                    String cid = commentslist.get(positionlist).getCid().toString();
                    if (user.getUserid() != null && user.getUserid().length() > 0) {
                        type = "2";
                        popedittext.showAsDropDown(textview, cid, type);
                    } else {
                        Intent intent = new Intent(mactivity, LoginActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news__web_view);
        mactivity = this;
        user = new UserBean(mactivity);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.USER_ACTION");
        filter.addAction("shareSuccess");
        registerReceiver(receiver, filter);

        //初始化intent，提取其中传递的值
        intent = getIntent();
        fndData = (FirstPageNews.DataBean) intent.getSerializableExtra("fndinfo");
        LogUtils.e(TAG, "--->" + fndData.toString());
        pagertype = fndData.getTerm_name();
        titletype = fndData.getTerm_id();
        likesize = intent.getStringExtra("likesize");
        Titletype = intent.getStringExtra("titletype");
        position = intent.getStringExtra("position");
        popshared = new Pop_shared_Activity(mactivity);
        popedittext = new Pop_EditText(NewsWebViewActivity.this);
        //点击收藏
        webview_sc = (ImageView) findViewById(R.id.webview_sc);
        webview_sc.setOnClickListener(this);
        news_scroll = (ScrollView) findViewById(R.id.news_scroll);
        et_iamge_choice = (TextView) findViewById(R.id.et_iamge_choice);//评论输入框
        et_iamge_choice.setOnClickListener(this);
        image_choice = (RelativeLayout) findViewById(R.id.image_choice);//评论定位按钮
        image_choice.setOnClickListener(this);
        linear_list = (TextView) findViewById(R.id.linear_list);

        tv_choice_num = (TextView) findViewById(R.id.tv_choice_num);//评论数字
        //头部标题
        title_main = (TextView) findViewById(R.id.goabroad_title_main);
        shared_three_dot = (LinearLayout) findViewById(R.id.shared_three_dot);
        shared_three_dot.setOnClickListener(this);
        webview_ff = (ImageView) findViewById(R.id.webview_ff);
        webview_ff.setOnClickListener(this);
        if (null != pagertype && pagertype.length() > 0) {
            title_main.setText(pagertype);
        } else {
            title_main.setText("");
        }
        //微信
        //获取API
        iwxapi = WXAPIFactory.createWXAPI(mactivity, IWXAPI_ID);
        //注册API
        iwxapi.registerApp(IWXAPI_ID);
        //微博
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
        wechat = (RelativeLayout) findViewById(R.id.wechat);
        wechat.setOnClickListener(this);
        friend = (RelativeLayout) findViewById(R.id.friend);
        friend.setOnClickListener(this);
        textview = (TextView) findViewById(R.id.textview);
        rl_webview = (RelativeLayout) findViewById(R.id.rl_webview);
        //微博
        weibo = (RelativeLayout) findViewById(R.id.weibo);
        weibo.setOnClickListener(this);
        //点赞
        webview_like = (ImageView) findViewById(R.id.news_give_like);
        webview_like.setOnClickListener(this);

        webView = (WebView) findViewById(R.id.web_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        web_list = (ListView) findViewById(R.id.web_lv);
        //初始化返回按钮
        web_back = (RelativeLayout) findViewById(R.id.web_back);
        web_back.setOnClickListener(this);
        //初始化文章来源地
        web_from = (TextView) findViewById(R.id.web_from);
        //初始化文章阅读量
        web_read = (TextView) findViewById(R.id.web_read);
        //初始化文章标题
        web_title = (TextView) findViewById(R.id.web_title);
        //初始化文章时间
        web_time = (TextView) findViewById(R.id.web_time);
        //初始化文章点赞量
        web_like = (TextView) findViewById(R.id.web_ok);
        if (fndData != null) {
            web_title.setText(fndData.getPost_title().toString() + "");//设置文章标题
            web_from.setText(fndData.getPost_source().toString() + "");//设置文章来源
            web_read.setText(fndData.getPost_hits().toString() + "");//设置文章阅读量
            web_time.setText(fndData.getPost_modified().toString().substring(0, 10) + "");//设置文章时间
//            if (null != fndData.getLikes_count()) {
//                web_like.setText(fndData.getLikes_count());
//            } else {
//                web_like.setText("0");
//            }
            // 解析出来的格式就是2016-06-14 09:45:14
            LogUtils.e("aaaa", fndData.getPost_modified().toString());
            description = fndData.getPost_excerpt().toString();//文章的内容a
            webId = fndData.getObject_id().toString();//文章的id\
            webtitleId = fndData.getTerm_id().toString();//获取新闻的id
            webview = NetBaseConstant.NET_WEB_VIEW + fndData.getObject_id().toString();//获取网址
            title = fndData.getPost_title().toString();//题目
        } else {
//            Toast.makeText(mactivity, "没有新内容", Toast.LENGTH_SHORT).show();
        }
        adbout_read_view = (TextView) findViewById(R.id.adbout_read_view);
        about_read = (TextView) findViewById(R.id.about_read);
        tv_more = (TextView) findViewById(R.id.tv_forum_details_comment_more);
        tv_more.setOnClickListener(this);
        tv_more.setVisibility(View.GONE);
        gotoweb();

    }

    private void getscore() {
        if (user.getUserid().length() > 0) {
            if (HttpConnect.isConnnected(mactivity)) {
                LogUtils.i("onResume", "initData1");
                new StudyRequest(mactivity, handler).ADDSCORE(user.getUserid(), ADDSCORE);
            } else {
                LogUtils.i("onResume", "initData2");
                ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
            }


        }
    }

    private void getCommentsCount() {
//      id(文章id/帖子id),type(1文章，2帖子)
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", webId);
        requestParams.put("type", "1");
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_COMMENTS_COUNT, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getCommentsCount->" + response);
//                    {
//                        "status":"success",
//                            "data":"1"
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            String data = response.getString("data");
                            if (data != null && !data.equals("")) {
                                commentCount = Long.valueOf(data);
                                if (popchoice != null) {
                                    popchoice.setCommentCount(commentCount);
                                    popchoice.notifyDataSetChanged();
                                    setListViewHeightBasedOnChildren(web_list);
                                    tv_choice_num.setText("" + commentCount);
                                }
                                if (commentslist != null && commentslist.size() >= commentCount) {
                                    tv_more.setVisibility(View.GONE);
                                } else {
                                    tv_more.setVisibility(View.VISIBLE);//TODO
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void getLikesCount() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", webId);
        requestParams.put("type", "1");
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_LIKE_COUNT, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        String data = response.getString("data");
                        if (data != null && !data.equals("")) {
                            web_like.setText(data);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /*
    开启子线程，获取网络数据
     */
    /*
   内嵌浏览器
     */
    private void gotoweb() {
        //WebView加载web资源
        webView.loadUrl(NetBaseConstant.NET_WEB_VIEW + fndData.getObject_id().toString());
        LogUtils.i("webview111", "-------------->" + fndData.getSmeta());
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     *
     * @param hasWebpage 分享的内容是否有网页
     */
    private void sendMultiMessage(boolean hasWebpage) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = fndData.getPost_title();
        mediaObject.description = fndData.getPost_excerpt();
        if (compressBitmapToData(checkImageSize(thumbBmp), 32) != null && compressBitmapToData(checkImageSize(thumbBmp), 32).length > 0) {
            mediaObject.thumbData = compressBitmapToData(checkImageSize(thumbBmp), 32);
        } else {
            try {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);

                // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
                mediaObject.setThumbImage(bitmap);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        mediaObject.actionUrl = NetBaseConstant.NET_WEB_VIEW + fndData.getObject_id().toString() + "&type=1";
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        // 1. 初始化微博的分享消息
        if (hasWebpage) {
            weiboMessage.mediaObject = mediaObject;
        }
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        AuthInfo authInfo = new AuthInfo(this, chinanurse.cn.nurse.weibo.Constants.APP_KEY, chinanurse.cn.nurse.weibo.Constants.REDIRECT_URL, chinanurse.cn.nurse.weibo.Constants.SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(this, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
            }

            @Override
            public void onComplete(Bundle bundle) {
                // TODO Auto-generated method stub
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(getApplicationContext(), newToken);
                getscore();
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = NetBaseConstant.NET_WEB_VIEW + fndData.getObject_id().toString() + "&type=1";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = fndData.getPost_title();
        msg.description = fndData.getPost_excerpt();
        if (compressBitmapToData(checkImageSize(thumbBmp), 32) != null && compressBitmapToData(checkImageSize(thumbBmp), 32).length > 0) {
            msg.thumbData = compressBitmapToData(checkImageSize(thumbBmp), 32);
        } else {
            try {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
                msg.setThumbImage(bitmap);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }

    /*
    返回按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击返回按钮,finish()掉本页面，显示上一页
            case R.id.web_back:
                if (bitmap != null && !bitmap.isRecycled()) {
                    // 回收并且置为null
                    bitmap.recycle();
                    bitmap = null;
                }
                finish();
                break;
            case R.id.tv_forum_details_comment_more:
                pager = pager + 1;
                new StudyRequest(mactivity, handler).GETrefcomments(webId, GETREFCOMMENTS, pager);
                break;
            case R.id.wechat:
                if (user.getUserid().length() <= 0) {
                    intentuser = new Intent(mactivity, LoginActivity.class);
                    intentuser.putExtra("now_btn", "0");
                    startActivity(intentuser);
                } else {
                    getBitmap(0);
                    //分享到微信好友
                }
                break;
            case R.id.friend:
                if (user.getUserid().length() <= 0) {
                    intentuser = new Intent(mactivity, LoginActivity.class);
                    startActivity(intentuser);
                } else {
                    getBitmap(1);
                }
                break;
            case R.id.news_give_like://点赞按钮
                LogUtils.e("id", "======================" + fndData.getTerm_id() + "---------------->" + fndData.getObject_id());
                if (null != user.getUserid() && user.getUserid().length() > 0) {
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).CheckHadLike(user.getUserid(), webId, "1", BTNCHECKHADFAVORITE);
                    } else {
                        ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                    }
                } else {
                    Intent intent = new Intent(mactivity, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            //微博
            case R.id.weibo:
                if (user.getUserid().length() <= 0) {
                    intentuser = new Intent(mactivity, LoginActivity.class);
                    startActivity(intentuser);
                } else {
                    //分享到朋友圈
                    getBitmapweibo(isChecked);
                }
                break;

            case R.id.webview_ff:
                if (user.getUserid().length() <= 0) {
                    intentuser = new Intent(mactivity, LoginActivity.class);
                    startActivity(intentuser);
                } else {
                    path = NetBaseConstant.NET_PIC_PREFIX_THUMB + fndData.getThumb().get(0).getUrl().toString();
                    //分享到朋友圈
                    popshared.showAsDropDown(textview, description, webview, title, path);
                }
                break;
            case R.id.webview_sc:
                LogUtils.e("id", "======================" + fndData.getTerm_id() + "---------------->" + fndData.getObject_id());
                if (null != user.getUserid() && user.getUserid().length() > 0) {
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).CheckHadFavorite(user.getUserid(), webId, "1", CHECKHADFAVORITE);
                    } else {
                        ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                    }
                } else {
                    Intent intent = new Intent(mactivity, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.et_iamge_choice:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    type = "1";
                    popedittext.showAsDropDown(rl_webview, fndData.getObject_id(), type);
                } else {
                    Intent intent = new Intent(mactivity, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.shared_three_dot:
                if (user.getUserid().length() <= 0) {
                    intentuser = new Intent(mactivity, LoginActivity.class);
                    startActivity(intentuser);
                } else {
                    path = NetBaseConstant.NET_PIC_PREFIX_THUMB + fndData.getThumb().get(0).getUrl().toString();
                    //分享到朋友圈
                    popshared.showAsDropDown(rl_webview, description, webview, title, path);
                }
                break;

            case R.id.image_choice://点击滑动到评论界面

                news_scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        int[] location = new int[2];
                        about_read.getLocationOnScreen(location);
                        int offset = location[1] - news_scroll.getMeasuredHeight();
                        if (offset < 0) {
                            offset = 0;
                        }
                        news_scroll.smoothScrollTo(0, offset);
                    }
                });
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (resultCode) {
                case 0:
                    if (bitmap != null && !bitmap.isRecycled()) {
                        // 回收并且置为null
                        bitmap.recycle();
                        bitmap = null;
                    }
                    finish();
                    break;

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("onResume", "==============>+onResume");
        StatService.onPageStart(this, "新闻网页");
        getCommentsCount();
        getLikesCount();
        collect();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "新闻网页");
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
    }

    public void collect() {
        if (null != user.getUserid() && user.getUserid().length() > 0) {
            if (HttpConnect.isConnnected(mactivity)) {
                new StudyRequest(mactivity, handler).CheckHadFavorite(user.getUserid(), webId, "1", GETSTHECURRENT);
                new StudyRequest(mactivity, handler).CheckHadLike(user.getUserid(), webId, "1", GETLIKE);
            } else {
                ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
            }
        } else {
            webview_sc.setBackgroundResource(R.mipmap.btn_collect_sel);
            webview_like.setBackgroundResource(R.mipmap.img_like_nol);
        }
        new StudyRequest(mactivity, handler).GETrefcomments(webId, GETREFCOMMENTS, pager);
    }

    /*
      解决scrollview下listview显示不全
    */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals("shareSuccess")) {
                getscore();
            } else {
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
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    //网络下载图片
    private void getBitmap(final int num) {
        path = NetBaseConstant.NET_PIC_PREFIX_THUMB + fndData.getThumb().get(0).getUrl().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //这里下载数据
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();
                    thumbBmp = BitmapFactory.decodeStream(inputStream);
                    wechatShare(num);
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //网络下载图片
    private void getBitmapweibo(final boolean isChecked) {
        path = NetBaseConstant.NET_PIC_PREFIX_THUMB + fndData.getThumb().get(0).getUrl().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //这里下载数据
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();
                    thumbBmp = BitmapFactory.decodeStream(inputStream);
                    sendMultiMessage(isChecked);
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //剪裁图片
    public static Bitmap checkImageSize(Bitmap bitmap) {
        //防止超长图文件大小超过微信限制，需要进行截取，暂定比例上限为5
        final int MAX_RATIO = 5;
        Bitmap result = bitmap;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float ratio = width > height ? width * 1.0f / height : height * 1.0f / width;
            if (ratio > MAX_RATIO) {
                int size = Math.min(width, height);
                result = Bitmap.createBitmap(bitmap, 0, 0, size, size);
            }
        }
        return result;
    }

    //压缩图片到32KB
    public static byte[] compressBitmapToData(Bitmap bmp, float size) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] result;
        try {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (output.toByteArray().length / 1024 >= size) {  //循环判断如果压缩后图片是否大于size kb,大于继续压缩
                output.reset();//重置baos即清空baos
                bmp.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到baos中
                if (options == 1) {
                    break;
                }
                options -= 10;//每次都减少20
                if (options <= 0) {
                    options = 1;
                }
            }

            result = output.toByteArray();

            LogUtils.i("", "compressBitmap return length = " + result.length);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
