package chinanurse.cn.nurse.NurseFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.adapter.community.Community_post_detail_show;
import chinanurse.cn.nurse.bean.Nuser_community;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Write_Community_Show extends Activity implements View.OnClickListener {


    private Nuser_community.DataEntity nurse_Data;
    private String pagertype;
    private static final int ADDCOMMENT = 2;
    private static final int GETCOMMUNITYLIST = 3;

    private static final int GETCOMMUNITYDETAILS = 1;
    private Activity mactivity;
    private String postdetails;
    private Intent intent;
    private UserBean user;
    private String id;
    private List<String> picurl = new ArrayList<>();
    private static final int SETCOLLECT = 5;
    private static final int CANCELCOLLECT = 6;
    private static final int GETCOLLECT = 4;
    private static final int SETLIKE = 7;
    private static final int RESETLIKE = 8;


    private ListView post_detail_show;
    private int collectNum = 1;
    private int weblike = 1;


    private RelativeLayout btn_back;//返回
    //标题,题目,发帖人,所在部门,发帖人等级,点赞,时间,内容,需要隐藏的部分,点赞数,评论数
    private TextView top_title, community_title, lv_name, lv_position, lv_level, lv_text_like, lv_time, lv_answ, show_detail_num, lv_thumb_up_Num, lv_invitation_num;
    private LinearLayout community_care, lv_collect, lv_thumb_up;//关注（点击）,点击收藏,点赞,帖子
    private ImageView lv_image, lv_shared, main_thumb_up;//头像,分享,已点赞
    private EditText et_write_detail;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETCOMMUNITYDETAILS://获取帖子详情
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        Gson gson = new Gson();

                    }
                    break;
                case ADDCOMMENT://提交评论
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            if ("success".equals(status)) {
                                Toast.makeText(Write_Community_Show.this, "评论成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Write_Community_Show.this, "评论失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case GETCOMMUNITYLIST://获取评论列表
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        Gson gson = new Gson();
//                        gson.fromJson(result);

//                        post_detail_show.setAdapter();


                    }
                    break;


                case SETCOLLECT:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            if ("success".equals(status)) {
                                Toast.makeText(Write_Community_Show.this, "成功收藏", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Write_Community_Show.this, "收藏失败", Toast.LENGTH_SHORT).show();
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
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            if ("success".equals(status)) {
                                Toast.makeText(Write_Community_Show.this, "取消收藏", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Write_Community_Show.this, "取消失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case SETLIKE:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            if ("success".equals(status)) {
                                Toast.makeText(Write_Community_Show.this, "点赞成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Write_Community_Show.this, "点赞失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case RESETLIKE:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            if ("success".equals(status)) {
                                Toast.makeText(Write_Community_Show.this, "取消点赞", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Write_Community_Show.this, "取消失败", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.write_community_show);
        mactivity = this;
        user = new UserBean(mactivity);
        intent = getIntent();
        nurse_Data = (Nuser_community.DataEntity) intent.getParcelableExtra("goabroad");
        pagertype = intent.getStringExtra("pagertype");
        postdetailshow();

    }

    private void postdetailshow() {
        //返回
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        //标题
        top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText("帖子详情");
        //关注
        community_care = (LinearLayout) findViewById(R.id.community_care);
        community_care.setOnClickListener(this);
        //文章标题
        community_title = (TextView) findViewById(R.id.community_title);
        if (!"".equals(nurse_Data.getTitle().toString()) && nurse_Data.getTitle() != null) {
            community_title.setText(nurse_Data.getTitle().toString() + "");
        }
        //头像
        lv_image = (ImageView) findViewById(R.id.lv_image);
        ImageLoader.getInstance().displayImage(NetBaseConstant.NET_HOST + "/" + nurse_Data.getPhoto(), lv_image);


        Log.e("bbbb", NetBaseConstant.NET_HOST + "/" + nurse_Data.getPhoto());
        //职位
        lv_position = (TextView) findViewById(R.id.lv_position);
//        lv_position.setText(nurse_Data.get);
        //等级
        lv_level = (TextView) findViewById(R.id.lv_level);
//        lv_level.setText(nurse_Data.get);

        //用户名
        lv_name = (TextView) findViewById(R.id.lv_name);
        if(nurse_Data.getName()!=null)
        {
            lv_name.setText(nurse_Data.getName().toString() + "");
        }
        //关注数
        lv_text_like = (TextView) findViewById(R.id.lv_text_like);
//        lv_text_like.setText(nurse_Data.getLike().size() + "");
        //发表时间
        lv_time = (TextView) findViewById(R.id.lv_time);
        if(nurse_Data.getWrite_time()!=null)
        {
            Date date = new Date();
            date.setTime(Long.parseLong(nurse_Data.getWrite_time()) * 1000);
            lv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(date));
            Log.e("bbbb", new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(date));
        }




        //发表内容
        lv_answ = (TextView) findViewById(R.id.lv_answ);
        lv_answ.setText(nurse_Data.getContent().toString() + "");
        //收藏
        lv_collect = (LinearLayout) findViewById(R.id.lv_collect);
        lv_collect.setOnClickListener(this);
        //点赞
        lv_thumb_up = (LinearLayout) findViewById(R.id.lv_thumb_up);
        lv_thumb_up.setOnClickListener(this);
        lv_thumb_up_Num = (TextView) findViewById(R.id.lv_thumb_up_Num);
//        lv_thumb_up_Num.setText("");
        //评论
        lv_invitation_num = (TextView) findViewById(R.id.lv_invitation_num);
//        lv_invitation_num.setText("");
        //展示评论
        post_detail_show = (ListView) findViewById(R.id.post_detail_show);

        //假数据
        List<String> list = new ArrayList<>();
        post_detail_show.setAdapter(new Community_post_detail_show(mactivity, list));
        setListViewHeightBasedOnChildren(post_detail_show);

        //分享
        lv_shared = (ImageView) findViewById(R.id.lv_shared);

        //点赞
        main_thumb_up = (ImageView) findViewById(R.id.show_detail_main_thumb_up);

        //评论输入框
        et_write_detail = (EditText) findViewById(R.id.et_community_write_detail);

        //显示已评论数量
        show_detail_num = (TextView) findViewById(R.id.show_detail_num);
        //实现修改enter键的功能
        et_write_detail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    if (HttpConnect.isConnnected(mactivity)) {
                        String urlstr = "{";
                        for (int i = 0; i < picurl.size(); i++) {
                            if (i <= picurl.size() - 1) {
                                urlstr += picurl.get(i).toString() + ",";
                            } else if (i == picurl.size()) {
                                urlstr += picurl.get(i).toString() + "";
                            }
                        }

                        urlstr += "}";
                        new StudyRequest(mactivity, handler).ADDCOMMENT(user.getUserid(), id, et_write_detail.getText().toString(), "2", urlstr, GETCOMMUNITYDETAILS);
                        et_write_detail.setText("");
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                }

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "==============onResume");
        communityshow();
    }

    private void communityshow() {
        if (HttpConnect.isConnnected(mactivity)) {
            new StudyRequest(mactivity, handler).GETCOMMUNITYDETAILS(postdetails, GETCOMMUNITYDETAILS);
            new StudyRequest(mactivity, handler).SETCOLLEXT(user.getUserid(), "1", GETCOLLECT);
        } else {
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.btn_back:
                finish();
                break;
            //关注
            case R.id.community_care:

//                if (HttpConnect.isConnnected(mactivity)) {
//
//
//                        new StudyRequest(mactivity, handler).COLLEXT(user.getUserid(), webId,
//                                "1", community_title.getText().toString(), lv_answ.getText().toString(),
//                                SETCOLLECT);
//                } else {
//                    Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
//                }


                break;
            //收藏
            case R.id.lv_collect:
                if (collectNum == 1) {
                    lv_collect.setSelected(true);
                    if (HttpConnect.isConnnected(mactivity)) {


                        new StudyRequest(mactivity, handler).COLLEXT(user.getUserid(), nurse_Data.getMid().toString(),
                                "4", community_title.getText().toString(), lv_answ.getText().toString(),
                                SETCOLLECT);
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    collectNum = 0;
                    Log.i("collectNum", "--------->" + collectNum);
                } else {
                    lv_collect.setSelected(false);
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).DELLCOLLEXT(user.getUserid(), nurse_Data.getMid().toString(),
                                "4", CANCELCOLLECT);
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    collectNum = 1;
                    Log.i("collectNum", "--------->" + collectNum);
                }
                break;
            //点赞
            case R.id.lv_thumb_up:


                if (weblike == 1) {
                    if (HttpConnect.isConnnected(mactivity)) {
                        //还有个time
                        new StudyRequest(mactivity, handler).setLike(user.getUserid(), nurse_Data.getMid().toString(), "2", SETLIKE);
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    weblike = 2;
                } else {
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).resetLike(user.getUserid(), nurse_Data.getMid().toString(), "2", RESETLIKE);
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    weblike = 1;
                }

                break;


        }
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


}
