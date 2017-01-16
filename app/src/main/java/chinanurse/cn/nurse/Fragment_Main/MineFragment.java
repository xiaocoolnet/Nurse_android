package chinanurse.cn.nurse.Fragment_Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Mine.Mine_Post_News;
import chinanurse.cn.nurse.Fragment_Mine.Mine_Post_Newslist;
import chinanurse.cn.nurse.Fragment_Mine.My_personal_recruit;
import chinanurse.cn.nurse.Fragment_Nurse.activity.MessageActivity;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.NetUtil;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.Fragment_Mine.Mine_Study;
import chinanurse.cn.nurse.Fragment_Mine.MyPost.Mypost;
import chinanurse.cn.nurse.Fragment_Mine.My_News;
import chinanurse.cn.nurse.Fragment_Mine.Mycollect;
import chinanurse.cn.nurse.Fragment_Mine.Myinfo;
import chinanurse.cn.nurse.Fragment_Mine.Myrecruit;
import chinanurse.cn.nurse.MainActivity;
import chinanurse.cn.nurse.MinePage.Nuerse_score_money;
import chinanurse.cn.nurse.Fragment_Mine.Fans;
import chinanurse.cn.nurse.Other.MyActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.Mine_news_bean;
import chinanurse.cn.nurse.bean.Mine_study_zhexian_bean;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.NurseEmployBean;
import chinanurse.cn.nurse.bean.NurseEmployTalentBean;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.list.WaveSwipeRefreshLayout;
import chinanurse.cn.nurse.picture.RoudImage;
import chinanurse.cn.nurse.ui.HeadPicture;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 我
 * Created by Administrator on 2016/6/2.
 */
public class MineFragment extends Fragment implements View.OnClickListener, WaveSwipeRefreshLayout.OnRefreshListener {

    private String TAG = "MineFragment";
    private static final int ISLOG = 2;
    private static final int RESULT_CANCELED = 0;
    private static final int KEY = 4;
    private static final int GETMYSIGNLOGONE = 6;
    private static final int GETlINECHARDATA = 7;
    private static final int GETMYRECIVERESUMELIST = 9;
    private static final int GETMYRECIVERESUME = 10;
    private static final int KEYNEWS = 11;
    public static final int KEYNEWSREAD = 12;
    private static final int POSTNEWS = 13;
    private static final int CHECKVERSON = 14;
    private static final int CHECKVERSON1 = 15;
    private RelativeLayout rela_mine_study;

    private int flag = 0;

    private static final int GETUSERINFOMINE = 1;

    private static final int GETMYSIGNLOG = 222;

    private static final int SIGNDAY = 333;

    private String time_stamp, head;

    int GETMYSIGNLOG_flag = 0;

    private ImageView setting;
    private RoudImage headimage;
    private LinearLayout fans, attention, money;
    private RelativeLayout post, myrecruit, sign_in, mine_collect, mine_news, mine_post_news, mine_version_information;
    private Button bt_outlogin;
    private Activity mactivity;
    private UserBean user;
    private TextView tv_fans, news, message_news, tv_attention, tv_nurse_money, tv_name, tv_level, mine_sign_in_text, mine_scroll_count, mine_recuit_num;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private News_list_type fndbean;

    // 保存的文件的路径
    @SuppressLint("SdCardPath")
    private String filepath = "/sdcard/myheader";
    private String picname = "newpic", woman, et_content;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁
    public static final int UPDATE_AVATAR_KEY = 5;
    private Dialog dialog;
    private Mine_study_zhexian_bean.DataBean zhexianbean;
    private List<NurseEmployBean.DataBean> talentlist = new ArrayList<>();
    private List<News_list_type.DataBean> newslist = new ArrayList<>();
    private List<String> newslistread = new ArrayList<>();
    private List<NurseEmployTalentBean.DataBean> list_mine_recruit;
    private NurseEmployBean ralentwork;
    private Switch is_show_image;
    private RelativeLayout rela_mine_clear;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private ProgressDialog dialogpgd;
    private Mine_news_bean.DataBean minenewsbean;
    private Mine_news_bean.DataBean.ReplyBean newreplay;
    private List<Mine_news_bean.DataBean> minenewslist = new ArrayList<>();
    private List<Mine_news_bean.DataBean.ReplyBean> newsreplay;
    private SharedPreferences sp;
    private Long lastTime = 0L;
    private Long ceartTime = 0L;

    private Handler handler = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETUSERINFOMINE:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                JSONObject obj = new JSONObject(data);
                                user.setUserid(obj.getString("id"));
                                LogUtils.i("userid", "--------->" + user.getUserid());
                                user.setName(obj.getString("name"));
                                user.setFanscount(obj.getString("fanscount"));
                                user.setMoney(obj.getString("money"));
                                user.setRealname(obj.getString("realname"));
                                user.setScore(obj.getString("score"));
                                user.setPhoto(obj.getString("photo"));
//                                user.setUsertype(obj.getString("usertype"));
                                user.setSex(obj.getString("sex"));
                                user.setLevel(obj.getString("level"));
                                user.setFollowcount(obj.getString("followcount"));
                                showfans();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case ISLOG:
                    user.removeUserid();

                    if (user.getUserid() == null || "".equals(user.getUserid())) {
                        tv_name.setText(R.string.mine_name_no);
                        ToastUtils.ToastShort(mactivity, "退出成功");
                        MainActivity main = (MainActivity) getActivity();
                        main.getCurrentTage();
                    } else {
                        ToastUtils.ToastShort(mactivity, "退出失败");
                    }
                    bt_outlogin.setText("登录");
                    break;


                case GETMYSIGNLOG:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            LogUtils.e("time_stamp", json.toString());
                            String status = json.getString("status");
                            LogUtils.e("time_stamp", status);
                            if (status.equals("success")) {
                                //已经签到了
//                                GETMYSIGNLOG_flag = 1;
                                mine_sign_in_text.setText("已签到");
                                LogUtils.e("time_stamp", "66666");
                            } else {
                                GETMYSIGNLOG_flag = 0;
                                LogUtils.e("time_stamp", "777777");
                                if (HttpConnect.isConnnected(mactivity)) {
                                    mine_sign_in_text.setText("每日签到");
                                    LogUtils.e("time_stamp", "888888");
                                    new StudyRequest(mactivity, handler).get_SignDay(user.getUserid(), time_stamp, SIGNDAY);
                                } else {
                                    LogUtils.e("time_stamp", "9999");
                                    ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case SIGNDAY:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            LogUtils.e("time_stamp", "888888______" + json.toString());
                            LogUtils.e("time_stamp", "888888______" + status);
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                JSONObject jsonone = new JSONObject(data);
                                LogUtils.e("time_stamp", "888888______888888");
                                ToastUtils.ToastShort(getActivity(), "签到成功");
                                mine_sign_in_text.setText("已签到");
                                if (jsonone.getString("score") != null && jsonone.getString("score").length() > 0) {
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("+" + jsonone.getString("score"));
                                    TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                    tv_score_name.setText(jsonone.getString("event"));
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
                                if (HttpConnect.isConnnected(mactivity)) {
                                    new StudyRequest(mactivity, handler).getuserinfo(user.getUserid(), GETUSERINFOMINE);
                                } else {
                                    ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case KEY://上传头像
                    String key = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(key);
                        String state = json.getString("status");
                        if ("success".equals(state)) {
//                            JSONObject js=json.getJSONObject("data");
                            String path = json.getString("data");
                            LogUtils.e("path", "-------------->" + path);
//                            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/"+path, iv_head, options);
                            if (HttpConnect.isConnnected(mactivity)) {
                                new StudyRequest(mactivity, handler).updateUserAvatar(user.getUserid(), path, UPDATE_AVATAR_KEY);
                            } else {
                                ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                            }
                        } else {
                            ToastUtils.ToastShort(mactivity, "上传头像失败，请重试！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
                case UPDATE_AVATAR_KEY://修改头像
                    String data = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(data);
                        String state = json.getString("status");
                        if ("success".equals(state)) {
                            JSONObject jsonone = new JSONObject(data);
                            ToastUtils.ToastShort(mactivity, "修改头像成功!");
                            if (jsonone.getString("score") != null && jsonone.getString("score").length() > 0) {
                                View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                dialog = new AlertDialog.Builder(mactivity).create();
                                dialog.show();
                                dialog.getWindow().setContentView(layout);
                                TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                tv_score.setText("+" + jsonone.getString("score"));
                                TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                tv_score_name.setText(jsonone.getString("event"));
                            }
                            getuseinfo();
                        } else {
                            ToastUtils.ToastShort(mactivity, "修改头像失败，请重试！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case GETMYSIGNLOGONE:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            LogUtils.e("time_stamp", json.toString());
                            String status = json.getString("status");
                            LogUtils.e("time_stamp", status);

                            String dataone = json.getString("data");
                            if (status.equals("success")) {
                                //已经签到了
//                                GETMYSIGNLOG_flag = 1;
                                mine_sign_in_text.setText("已签到");
                                LogUtils.e("time_stamp", "66666");
                            } else {
                                mine_sign_in_text.setText("每日签到");
                                GETMYSIGNLOG_flag = 0;
                                LogUtils.e("time_stamp", "777777");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case GETlINECHARDATA:
                    String result = (String) msg.obj;
                    if (result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if ("success".equals(jsonObject.optString("status"))) {
                                JSONObject json = new JSONObject(jsonObject.getString("data"));
                                zhexianbean = new Mine_study_zhexian_bean.DataBean();
                                zhexianbean.setCreate_time_1(json.getInt("create_time_1"));
                                zhexianbean.setCreate_time_2(json.getInt("create_time_2"));
                                zhexianbean.setCreate_time_3(json.getInt("create_time_3"));
                                zhexianbean.setCreate_time_4(json.getInt("create_time_4"));
                                zhexianbean.setCreate_time_5(json.getInt("create_time_5"));
                                zhexianbean.setCreate_time_6(json.getInt("create_time_6"));
                                zhexianbean.setCreate_time_7(json.getInt("create_time_7"));
                                zhexianbean.setRate_1(json.getInt("rate_1"));
                                zhexianbean.setRate_2(json.getInt("rate_2"));
                                zhexianbean.setRate_3(json.getInt("rate_3"));
                                zhexianbean.setRate_4(json.getInt("rate_4"));
                                zhexianbean.setRate_5(json.getInt("rate_5"));
                                zhexianbean.setRate_6(json.getInt("rate_6"));
                                zhexianbean.setRate_7(json.getInt("rate_7"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case GETMYRECIVERESUMELIST://个人获取面试邀请
                    String resultmine = (String) msg.obj;
                    if (resultmine != null) {
                        Gson gson = new Gson();
                        talentlist.clear();
                        ralentwork = gson.fromJson(resultmine, NurseEmployBean.class);
                        talentlist.addAll(ralentwork.getData());
                        if (talentlist != null && talentlist.size() > 0) {

                            String num = preferences.getString("talentlist", null);
                            if (num != null) {
                                if (talentlist.size() > Integer.valueOf(num)) {
                                    mine_recuit_num.setVisibility(View.VISIBLE);
                                    mine_recuit_num.setText(String.valueOf(talentlist.size() - Integer.valueOf(num)) + "");
                                } else {
                                    mine_recuit_num.setVisibility(View.GONE);
                                }
                            } else {
                                mine_recuit_num.setVisibility(View.VISIBLE);
                                mine_recuit_num.setText(String.valueOf(talentlist.size()) + "");
                            }
                        } else {
                            mine_recuit_num.setVisibility(View.GONE);
                        }
                    } else {
                        mine_recuit_num.setVisibility(View.GONE);
                        ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                    }
                    break;
                case GETMYRECIVERESUME://企业获取面试信息
                    list_mine_recruit.clear();
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    if (jsonObject != null) {
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                LogUtils.e("result_data", "66666");
                                int length = jsonArray.length();
                                JSONObject itemObject;
                                for (int i = 0; i < length; i++) {
                                    itemObject = (JSONObject) jsonArray.get(i);
                                    NurseEmployTalentBean.DataBean mine_recruit = new NurseEmployTalentBean.DataBean();
                                    mine_recruit.setName(itemObject.getString("name"));
                                    mine_recruit.setSex(itemObject.getString("sex"));
                                    mine_recruit.setAvatar(NetBaseConstant.NET_PIC_PREFIX + itemObject.getString("avatar"));
                                    mine_recruit.setBirthday(itemObject.getString("birthday"));
                                    mine_recruit.setExperience(itemObject.getString("experience"));
                                    mine_recruit.setEducation(itemObject.getString("education"));
                                    mine_recruit.setCertificate(itemObject.getString("certificate"));
                                    mine_recruit.setWantposition(itemObject.getString("wantposition"));
                                    mine_recruit.setTitle(itemObject.getString("title"));
                                    mine_recruit.setAddress(itemObject.getString("address"));
                                    mine_recruit.setCurrentsalary(itemObject.getString("currentsalary"));
                                    mine_recruit.setJobstate(itemObject.getString("jobstate"));
                                    mine_recruit.setDescription(itemObject.getString("description"));
                                    mine_recruit.setEmail(itemObject.getString("email"));
                                    mine_recruit.setPhone(itemObject.getString("phone"));
                                    mine_recruit.setHiredate(itemObject.getString("hiredate"));
                                    mine_recruit.setWantcity(itemObject.getString("wantcity"));
                                    mine_recruit.setWantsalary(itemObject.getString("wantsalary"));
                                    LogUtils.e("result_data", itemObject.getString("birthday"));
                                    list_mine_recruit.add(mine_recruit);
                                }
                                if (list_mine_recruit != null && list_mine_recruit.size() > 0) {
                                    String num = preferences.getString("list_mine_recruit", null);
                                    if (num != null) {
                                        if (list_mine_recruit.size() > Integer.valueOf(num)) {
                                            LogUtils.i("num", "555555555505++++++++++++" + num);
                                            LogUtils.i("num", "555555555505++++++++++++-----" + list_mine_recruit.size());
                                            mine_recuit_num.setVisibility(View.VISIBLE);
                                            mine_recuit_num.setText(String.valueOf(list_mine_recruit.size() - Integer.valueOf(num)) + "");
                                        } else {
                                            mine_recuit_num.setVisibility(View.GONE);
                                        }
                                    } else {
                                        mine_recuit_num.setVisibility(View.VISIBLE);
                                        mine_recuit_num.setText(String.valueOf(list_mine_recruit.size()) + "");
                                    }
                                } else {
                                    mine_recuit_num.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mine_recuit_num.setVisibility(View.GONE);
                        ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                    }
                    break;
                case KEYNEWS:
                    if (msg.obj != null) {
                        result = (String) msg.obj;
                        newslist.clear();
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                Gson gson = new Gson();
                                fndbean = gson.fromJson(result, News_list_type.class);
                                newslist.addAll(fndbean.getData());
                                if (newslist != null && newslist.size() > 0) {
                                    if (HttpConnect.isConnnected(mactivity)) {
                                        new StudyRequest(mactivity, handler).getMessagereadlist(user.getUserid(), KEYNEWSREAD);
                                    } else {
                                        ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                                    }
                                } else {
                                    mine_scroll_count.setVisibility(View.GONE);
                                }
                            } else {
                                mine_scroll_count.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mine_scroll_count.setVisibility(View.GONE);
                        ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                    }

                    break;
                case KEYNEWSREAD:
                    if (msg.obj != null) {
                        String resultread = (String) msg.obj;
                        newslistread.clear();
                        try {
                            JSONObject jaon = new JSONObject(resultread);
                            if ("success".equals(jaon.optString("status"))) {
                                JSONArray jsonArray = jaon.getJSONArray("data");
                                if (jsonArray != null && jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        newslistread.add(jsonObject1.getString("refid"));
                                    }
                                    mine_scroll_count.setVisibility(View.VISIBLE);
                                    String num = String.valueOf(newslist.size() - newslistread.size()) + "";
                                    if ("0".equals(num)) {
                                        mine_scroll_count.setVisibility(View.GONE);
                                    } else {
                                        mine_scroll_count.setText(num + "");
                                    }
                                } else {
                                    mine_scroll_count.setText(newslist.size() + "");
                                }

                            } else {
                                mine_scroll_count.setText(newslist.size() + "");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                        mine_scroll_count.setText(newslist.size() + "");
                    }
                    break;
                case POSTNEWS:
                    if (msg.obj != null) {
                        result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                JSONArray jsonarray = json.getJSONArray("data");
                                if (jsonarray != null && jsonarray.length() > 0) {
                                    for (int i = 0; i < jsonarray.length(); i++) {
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
                                        for (int j = 0; j < jsonreplay.length(); j++) {
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
                                    if (minenewslist != null && minenewslist.size() > 0) {
                                        mine_post_news.setClickable(true);
                                        Intent intent = new Intent(mactivity, Mine_Post_Newslist.class);
                                        startActivity(intent);
                                    } else {
                                        mine_post_news.setClickable(true);
                                        Intent intent = new Intent(mactivity, Mine_Post_News.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    mine_post_news.setClickable(true);
                                    Intent intent = new Intent(mactivity, Mine_Post_News.class);
                                    startActivity(intent);
                                }
                                mine_post_news.setClickable(true);
                                dialogpgd.dismiss();
                            } else {
                                mine_post_news.setClickable(true);
                                dialogpgd.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        dialogpgd.dismiss();
                        mine_post_news.setClickable(true);
                    }
                    break;
                case CHECKVERSON:
                    if (msg.obj != null) {
                        result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                JSONObject jsonobj = new JSONObject(json.getString("data"));
                                if (getResources().getString(R.string.versionname).equals(jsonobj.getString("version"))) {
                                    news.setVisibility(View.GONE);
                                } else {
                                    news.setVisibility(View.VISIBLE);
                                }
                            } else {
                                news.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                    }
                    break;
                case CHECKVERSON1:
                    if (msg.obj != null) {
                        result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                JSONObject jsonobj = new JSONObject(json.getString("data"));
                                String versionname = getResources().getString(R.string.versionname);
                                if (versionname.equals(jsonobj.getString("version"))) {
                                    LogUtils.e("versionname1", ";;;;;;;;;;;;;" + R.string.versionname);
                                    LogUtils.e("versionname2", ";;;;;;;;;;;;;" + versionname);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
                                    builder.setMessage("已是最新版本:" + getResources().getString(R.string.versionname));
                                    builder.setTitle("提示");
                                    builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    });
                                    builder.create().show();
                                } else {
                                    LogUtils.e("versionname3", ";;;;;;;;;;;;;" + R.string.versionname);
                                    LogUtils.e("versionname4", ";;;;;;;;;;;;;" + versionname);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
                                    builder.setMessage("当前版本:" + getResources().getString(R.string.versionname) + "\n" + "最新版本：" + jsonobj.getString("version"));
                                    builder.setTitle("提示");
                                    builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=chinanurse.cn.nurse");
                                            Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(it);
                                        }
                                    });
                                    builder.create().show();
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
                                builder.setMessage("已是最新版本:" + getResources().getString(R.string.versionname));
                                builder.setTitle("提示");
                                builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
                        builder.setMessage("已是最新版本:" + getResources().getString(R.string.versionname));
                        builder.setTitle("提示");
                        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                    break;
            }
        }
    };


    private void showfans() {
        if (user.getScore().length() > 0) {
            tv_nurse_money.setText(user.getScore());
        } else {
            tv_nurse_money.setText(0);
        }
        if (user.getLevel().length() > 0) {
            tv_level.setText(user.getLevel());
        } else {
            tv_level.setText(0);
        }
        if (user.getName().length() > 0) {
            tv_name.setText(user.getName());
        } else {
            tv_name.setText("");
        }
        if (user.getPhoto().length() > 0) {
            LogUtils.i("TAG", NetBaseConstant.NET_PIC_PREFIX + "/" + user.getPhoto());
            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + user.getPhoto(), headimage, options);
        } else {
            new HeadPicture().getHeadPicture(headimage);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = View.inflate(getContext(), R.layout.minefragment, null);
        mactivity = getActivity();
        user = new UserBean(mactivity);
        list_mine_recruit = new ArrayList<>();
        preferences = mactivity.getSharedPreferences("nursenum", Context.MODE_PRIVATE);
        sp = getActivity().getSharedPreferences("MESSAGE", getActivity().MODE_PRIVATE);

        editor = preferences.edit();
        news = (TextView) mView.findViewById(R.id.news);
        dialogpgd = new ProgressDialog(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT);
        mine_sign_in_text = (TextView) mView.findViewById(R.id.mine_sign_in_text);
        mine_version_information = (RelativeLayout) mView.findViewById(R.id.mine_version_information);
        mine_version_information.setOnClickListener(this);
        setting = (ImageView) mView.findViewById(R.id.mine_setting);
        fans = (LinearLayout) mView.findViewById(R.id.mine_fans);
        tv_fans = (TextView) mView.findViewById(R.id.tv_mine_fans);
        attention = (LinearLayout) mView.findViewById(R.id.mine_attention);
        tv_attention = (TextView) mView.findViewById(R.id.tv_mine_attention);
        money = (LinearLayout) mView.findViewById(R.id.mine_nurse_money);//护士币
        tv_nurse_money = (TextView) mView.findViewById(R.id.tv_mine_nurse_money);//护士币主页数量
        tv_name = (TextView) mView.findViewById(R.id.mine_name);
        tv_level = (TextView) mView.findViewById(R.id.tv_mine_level);
        headimage = (RoudImage) mView.findViewById(R.id.mine_headimg);
        headimage.setOnClickListener(this);
        bt_outlogin = (Button) mView.findViewById(R.id.bt_outlogin);//退出登录
        is_show_image = (Switch) mView.findViewById(R.id.is_show_image);//是否显示图片开关
        rela_mine_clear = (RelativeLayout) mView.findViewById(R.id.rela_mine_clear);//清除缓存
        rela_mine_clear.setOnClickListener(this);
        mine_post_news = (RelativeLayout) mView.findViewById(R.id.mine_post_news);//反馈信息
        mine_post_news.setClickable(true);
        mine_post_news.setOnClickListener(this);
        is_show_image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (4 == NetUtil.getNetWorkType(mactivity)) {
                        editor.putString("isopen", "true");
                        LogUtils.e("isopen", "-------------->true");
                        editor.commit();
                    } else {
                        editor.putString("isopen", "false");
                        LogUtils.e("isopen", "-------------->false");
                        editor.commit();
                    }
                } else {
                    editor.putString("isopen", "true");
                    LogUtils.e("isopen", "-------------->true");
                    editor.commit();
                }
            }
        });

        rela_mine_study = (RelativeLayout) mView.findViewById(R.id.rela_mine_study);
        mine_recuit_num = (TextView) mView.findViewById(R.id.mine_recuit_num);
        mine_scroll_count = (TextView) mView.findViewById(R.id.mine_scroll_count);
        message_news = (TextView) mView.findViewById(R.id.message_news);

        // 显示图片的配置
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_head_nor).showImageOnFail(R.mipmap.img_head_nor).cacheInMemory(true).cacheOnDisc(true).build();
        //签到
        sign_in = (RelativeLayout) mView.findViewById(R.id.mine_sign_in);
        sign_in.setOnClickListener(this);
        mine_collect = (RelativeLayout) mView.findViewById(R.id.ril_mine_collection);
        mine_collect.setOnClickListener(this);
        mine_news = (RelativeLayout) mView.findViewById(R.id.mine_news);
        mine_news.setOnClickListener(this);
        post = (RelativeLayout) mView.findViewById(R.id.mine_post);
        post.setVisibility(View.GONE);
        myrecruit = (RelativeLayout) mView.findViewById(R.id.mine_myrecruit);
        myrecruit.setOnClickListener(this);
        post.setOnClickListener(this);
        fans.setOnClickListener(this);
        attention.setOnClickListener(this);
        setting.setOnClickListener(this);
        rela_mine_study.setOnClickListener(this);
        bt_outlogin.setOnClickListener(this);
        money.setOnClickListener(this);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) mView.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setWaveColor(0xFF90006b);

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mine_setting:
                //调用自定义方法，跳转页面
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    MyActivity.getIntent(getActivity(), LoginActivity.class, "0");
                } else {
                    MyActivity.getIntent(getActivity(), Myinfo.class, "0");
                }

                break;
            case R.id.mine_fans:
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    MyActivity.getIntent(getActivity(), LoginActivity.class, "1");
                } else {
                    MyActivity.getIntent(getActivity(), Fans.class, "1");
                }


                break;
            case R.id.mine_attention:
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    MyActivity.getIntent(getActivity(), LoginActivity.class, "2");
                } else {
                    MyActivity.getIntent(getActivity(), Fans.class, "2");
                }
                break;
            case R.id.mine_post:
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    MyActivity.getIntent(getActivity(), LoginActivity.class, "0");
                } else {
                    MyActivity.getIntent(getActivity(), Mypost.class, "0");
                }

                break;
            case R.id.mine_myrecruit:
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    MyActivity.getIntent(getActivity(), LoginActivity.class, "0");
                } else {
                    if ("1".equals(user.getUsertype())) {
                        Intent intent = new Intent(getActivity(), My_personal_recruit.class);
                        startActivity(intent);
                    } else if ("2".equals(user.getUsertype())) {
                        MyActivity.getIntent(getActivity(), Myrecruit.class, "0");
                    }

                }

                break;
            //签到
            case R.id.mine_sign_in:
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    MyActivity.getIntent(getActivity(), LoginActivity.class, "0");
                } else {
                    //获取今天零点的时间戳
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Long time = calendar.getTime().getTime() / 1000;
                    time_stamp = time + "";
                    LogUtils.e("time_stamp", time_stamp + "     ----------------");
                    //获取我的今天是否签到
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).get_MySignLog(user.getUserid(), time_stamp, GETMYSIGNLOG);
                    } else {
                        ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                    }
                }
                break;
            case R.id.ril_mine_collection:
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    MyActivity.getIntent(getActivity(), LoginActivity.class, "0");
                } else {
                    MyActivity.getIntent(getActivity(), Mycollect.class, "0");
                }

                break;

            case R.id.rela_mine_study:
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    MyActivity.getIntent(getActivity(), LoginActivity.class, "0");
                } else {
                    MyActivity.getIntent(getActivity(), Mine_Study.class, "1");
                }

                break;
            //退出登录
            case R.id.bt_outlogin:

                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    tv_name.setText(R.string.mine_name_no);
                    bt_outlogin.setText("登录");
                    flag = 1;
                } else {
                    bt_outlogin.setText("退出登录");
                    flag = 0;

                }
                getlogin();

                break;


            case R.id.mine_news:
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    Intent intent = new Intent(mactivity, LoginActivity.class);
                    startActivity(intent);
                } else {
//                    MyActivity.getIntent(getActivity(), My_News.class, "0");
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putLong("lastTime", ceartTime);
                    editor.commit();
                    Intent intentMessge = new Intent();
                    intentMessge.setClass(getActivity(), MessageActivity.class);
                    getActivity().startActivity(intentMessge);
                }
                break;
            case R.id.mine_headimg:
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    Intent intent = new Intent(mactivity, LoginActivity.class);
                    startActivity(intent);
                } else {
                    getCheck();
                    ShowPickDialog();// 点击更换头像
                }
                break;
            case R.id.mine_nurse_money:
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    Intent intent = new Intent(mactivity, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mactivity, Nuerse_score_money.class);
                    startActivity(intent);
                }
                break;
            case R.id.rela_mine_clear:
                //清理缓存
                break;
            case R.id.mine_post_news:
                if (user.getUserid() == null || "".equals(user.getUserid())) {
                    Intent intent = new Intent(mactivity, LoginActivity.class);
                    startActivity(intent);
                } else {
                    mine_post_news.setClickable(false);
                    if (NetUtil.isConnnected(mactivity)) {
                        dialogpgd.setMessage("正在跳转...");
                        dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialogpgd.show();
                        new StudyRequest(mactivity, handler).getfeedbackList(user.getUserid(), POSTNEWS);
                    } else {
                        ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                    }
                }
                break;
            case R.id.mine_version_information:
                if (NetUtil.isConnnected(mactivity)) {
                    new StudyRequest(mactivity, handler).CHECKVERSON(CHECKVERSON1);
                } else {
                    LogUtils.i("onResume", "initData2");
                    ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                }
                break;

        }
    }

    private void getlogin() {

        if (flag == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
            builder.setMessage("是否退出");
            builder.setTitle("提示");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            msg.what = ISLOG;
                            handler.sendMessage(msg);
                        }
                    }).start();
                    flag = 1;
                    bt_outlogin.setText("登录");
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            LogUtils.e("qqqqq", "qqqq");
            Intent intent = new Intent(mactivity, LoginActivity.class);
            intent.putExtra("login_now", "mine");
            startActivity(intent);
            if ("".equals(user.getUserid()) || user.getUserid() == null) {
                flag = 1;
            } else {
                bt_outlogin.setText("退出登录");
                flag = 0;
            }
        }
    }


    private void getMessageListByPager() {
//        入参：userid,pager
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("pager", "1");
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_MY_MESSAGELIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getMessageListByPager->" + response);

                    try {
                        if ("success".equals(response.optString("status"))) {
                            JSONArray jsonArray = response.getJSONArray("data");
                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                ceartTime = jsonObject.getLong("create_time");
                                //TODO 判断最新信息的时间与lastTime 大小
                                lastTime = sp.getLong("lastTime", 0L);
                                //未登录不显示小红点
                                if (ceartTime > lastTime && user.getUserid() != null
                                        && user.getUserid().length() > 0) {
                                    message_news.setVisibility(View.VISIBLE);
                                } else {
                                    message_news.setVisibility(View.GONE);
                                }
                            } else {
                                message_news.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        });
    }


    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getActivity(), "我");
        getnews();
        if (user.getUserid() == null || "".equals(user.getUserid())) {
            tv_level.setText("0");
            tv_fans.setText("0");
            tv_attention.setText("0");
            tv_nurse_money.setText("0");
            headimage.setImageResource(R.mipmap.img_head_nor);
            tv_name.setText(R.string.mine_name_no);
            bt_outlogin.setText("登录");
//            flag = 1;
        } else {
            bt_outlogin.setText("退出登录");
//            flag = 0;
        }
        LogUtils.i("onResume", "---------->onResume");
        intselect();
        initzhexiantu();
        getuseinfo();
        getGet();
        getverson();
        getFanNum();
        getMessageListByPager();
    }

    private void getverson() {
        if (NetUtil.isConnnected(mactivity)) {
            new StudyRequest(mactivity, handler).CHECKVERSON(CHECKVERSON);
        } else {
            LogUtils.i("onResume", "initData2");
            ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
        }
    }

    private void getFanNum() {
        //入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_FOLLOW_FANS_NUM, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getFanNum->" + response);
//                    {
//                        "status": "success",
//                            "data": {
//                        "follows_count": "0",
//                                "fans_count": "1"
//                    }
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            JSONObject j = response.getJSONObject("data");
                            tv_attention.setText(j.getString("follows_count"));
                            tv_fans.setText(j.getString("fans_count"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "我");
    }

    private void getGet() {
        if (user.getUserid() == null || "".equals(user.getUserid())) {
            mine_recuit_num.setVisibility(View.GONE);
        } else {
            if ("1".equals(user.getUsertype())) {
                LogUtils.i("onResume111", "listlalala");
                if (NetUtil.isConnnected(mactivity)) {
                    new StudyRequest(mactivity, handler).UserGetInvite(user.getUserid(), GETMYRECIVERESUMELIST);
                } else {
                    LogUtils.i("onResume", "initData2");
                    ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                }
            } else if ("2".equals(user.getUsertype())) {
                LogUtils.i("onResume111", "listlalala");
                if (NetUtil.isConnnected(mactivity)) {
                    new StudyRequest(getActivity(), handler).getResumeList(user.getUserid(), GETMYRECIVERESUME);
                } else {
                    ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                }
            }
        }
    }

    private void initzhexiantu() {
        if (HttpConnect.isConnnected(mactivity)) {
            new StudyRequest(mactivity, handler).GetLineChartData(user.getUserid(), GETlINECHARDATA);//获取折线图数据
        } else {
            ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
        }
    }

    private void getnews() {
        if (HttpConnect.isConnnected(mactivity)) {
            new StudyRequest(mactivity, handler).get_systemmessage(user.getUserid(), KEYNEWS);
        } else {
            ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
        }
    }

    private void intselect() {
        //获取我的今天是否签到
        if (HttpConnect.isConnnected(mactivity)) {
            //获取今天零点的时间戳
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Long time = calendar.getTime().getTime() / 1000;
            time_stamp = time + "";
            new StudyRequest(mactivity, handler).get_MySignLog(user.getUserid(), time_stamp, GETMYSIGNLOGONE);
        } else {
            ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
        }
        getuseinfo();
    }

    private void getuseinfo() {
        if (HttpConnect.isConnnected(mactivity)) {
            new StudyRequest(mactivity, handler).getuserinfo(user.getUserid(), GETUSERINFOMINE);
        } else {
            ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
        }
    }

    //6.0申请动态权限
    private void getCheck() {
        if (ContextCompat.checkSelfPermission(mactivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                    100);
        }
        if (ContextCompat.checkSelfPermission(mactivity, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                    100);
        }
        if (ContextCompat.checkSelfPermission(mactivity, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE},
                    100);
        }
        if (ContextCompat.checkSelfPermission(mactivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }
        if (ContextCompat.checkSelfPermission(mactivity, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},
                    100);
        }
    }

    protected void ShowPickDialog() {
        new AlertDialog.Builder(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT).setNegativeButton("相册", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(intentFromGallery, PHOTO_REQUEST_ALBUM);

            }
        }).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File file = new File(path, "newpic.jpg");
                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                }

                startActivityForResult(intentFromCapture, PHOTO_REQUEST_CAMERA);
            }
        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:// 相册
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, "newpic.jpg");
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                    }
                    break;
                case PHOTO_REQUEST_ALBUM:// 图库
                    startPhotoZoom(data.getData());
                    break;

                case PHOTO_REQUEST_CUT: // 图片缩放完成后
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(this.getResources(), photo);
            headimage.setImageDrawable(drawable);
            picname = "avatar" + user.getUserid() + String.valueOf(new Date().getTime());
            storeImageToSDCARD(photo, picname, filepath);
            if (head != null && head.length() > 0) {
                if (HttpConnect.isConnnected(mactivity)) {
                    new StudyRequest(mactivity, handler).updateUserImg(head, KEY);
                } else {
                    ToastUtils.ToastShort(mactivity, getString(R.string.net_erroy));
                }
            } else {
                ToastUtils.ToastShort(mactivity, "请重新拍照");
            }
        }
    }

    /**
     * storeImageToSDCARD 将bitmap存放到sdcard中
     */
    public void storeImageToSDCARD(Bitmap colorImage, String ImageName, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        File imagefile = new File(file, ImageName + ".jpg");
        try {
            imagefile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imagefile);
            colorImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            head = imagefile.getPath();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        intselect();
        initzhexiantu();
        getuseinfo();
        getGet();
        getnews();
        refresh();
    }

    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);

    }
}
