package chinanurse.cn.nurse.Fragment_Nurse.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.ForumCommentAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommentBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommentNetBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumInfoBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.Fragment_Nurse.view.ForumDetailsEditText;
import chinanurse.cn.nurse.utils.KeyBoardUtils;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.TimeToolUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.picture.RoudImage;
import chinanurse.cn.nurse.popWindow.Pop_shared_Activity;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2016/12/30.
 * 护士站--圈子---发现—-帖子详情
 */

public class ForumDetailsActivity extends Activity implements View.OnClickListener {
    private String TAG = "ForumDetailsActivity";
    private final int INTENT_HOME_PAGE = 17;
    private static final int SET_LIKE = 110;
    private static final int RESET_LIKE = 111;
    private static final int REPLAY = 112;
    private static final int REPORT = 113;
    private static final int DELETE = 114;
    private ForumDetailsEditText forumDetailsEditText;
    private UserBean user;
    private Context mContext;
    private RelativeLayout rl_back, rl_share;
    private RoudImage iv_head;
    private ImageView iv_authentication;
    private TextView tv_name, tv_post, tv_level, tv_time, tv_title, tv_content;
    private ImageView iv_one, iv_two, iv_three, iv_four, iv_five, iv_six, iv_seven, iv_eight, iv_nine;
    private ImageView iv_collect, iv_like, iv_reward;
    private TextView tv_collect, tv_like, tv_reward, tv_more;
    private LinearLayout ll_report;

    private Pop_shared_Activity popshared;
    private String path, webview;

    private ScrollView ll_fullsreen;
    private LinearLayout ll_comment_submit;
    private RelativeLayout rl_comment_submit;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_bg_nor).showImageOnFail(R.mipmap.img_bg_nor).cacheInMemory(true).cacheOnDisc(true).build();
    private ForumDataBean forumBean = new ForumDataBean();
    private ForumInfoBean forumInfoBean;
    private Gson gson;
    private AlertDialog reward, report;
    private Dialog recommend;
    private ListView lv_comment;
    private TextView tv_no_comment;
    private ForumCommentAdapter commentAdapter;
    private CommentNetBean commentNetBean;
    private ArrayList<CommentBean> commentBeanArrayList;
    private boolean isMaster = false;
    private boolean isAdmin = false;
    private boolean isCanDelete=false;
    private String reportString = "";
    private String rewardString = "";
    private String replayType = "2";
    private String replayID = "";
    private String replayName="";
    private Dialog dialog;
    private Long commentCount = 0L;
    private int pager = 1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_LIKE://点赞变化图标
                    if (msg.obj != null) {
                        int position = (int) msg.obj;
                        commentBeanArrayList.get(position).setAddLike("1");
                        commentAdapter.notifyDataSetChanged();

                    }
                    break;
                case RESET_LIKE://取消赞图标
                    if (msg.obj != null) {
                        int position = (int) msg.obj;
                        commentBeanArrayList.get(position).setAddLike("0");
                        commentAdapter.notifyDataSetChanged();
                    }
                    break;
                case REPLAY://TODO
                    if (msg.obj != null) {
                        int position = (int) msg.obj;
                        replayID = commentBeanArrayList.get(position).getId();
                        replayName=commentBeanArrayList.get(position).getUserName();
                        if (user.getUserid() != null && user.getUserid().length() > 0) {
                            replayType = "3";
                            forumDetailsEditText.showAsRelayComment(ll_comment_submit, replayID, replayType,replayName);
                        } else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                    break;
                case REPORT:
                    if (msg.obj != null) {
                        int position = (int) msg.obj;
                        String cid = commentBeanArrayList.get(position).getId();
                        if (user.getUserid() != null && user.getUserid().length() > 0) {
                            toReport(cid, "2");
                        } else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        }

                    }
                    break;
                case DELETE:
                    if (msg.obj != null) {
                        int position = (int) msg.obj;
                        String cid = commentBeanArrayList.get(position).getId();
                        if (user.getUserid() != null && user.getUserid().length() > 0) {
                            deleteComment(cid);
                        } else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                    break;
            }
        }
    };

    private void deleteComment(String cid) {
//        入参：id(评论id),type(评论类型：2删除帖子父评论，3删除帖子子评论),userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", cid);
        requestParams.put("type", "2");

        NurseAsyncHttpClient.post(CommunityNetConstant.DEL_FORUM_COMMENTS, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--deleteComment->" + response);
//                    {
//                        "status":"success",
//                            "data":{
//                        "type":"1",
//                                "userid":"17026",
//                                "t_id":"38",
//                                "score":"1",
//                                "create_time":1483366785
//                    }
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            getForumComments();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forum_details);
        forumBean = (ForumDataBean) getIntent().getSerializableExtra("forum");
        replayID = forumBean.getId();
        LogUtils.e(TAG, "-hou--->" + forumBean.toString());
        mContext = this;
        user = new UserBean(mContext);
        inintView();
    }

    private void inintView() {
        ll_fullsreen = (ScrollView) findViewById(R.id.news_scroll);
        ll_fullsreen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                replayID = forumBean.getId();
                replayType = "2";
                return false;
            }
        });
        rl_back = (RelativeLayout) findViewById(R.id.rl_forum_details_back);
        rl_back.setOnClickListener(this);
        rl_share = (RelativeLayout) findViewById(R.id.rl_forum_details_share);
        rl_share.setOnClickListener(this);
        iv_head = (RoudImage) findViewById(R.id.iv_forum_details_head);
        iv_head.setOnClickListener(this);
        iv_authentication = (ImageView) findViewById(R.id.iv_forum_details_authentication);
        tv_name = (TextView) findViewById(R.id.tv_forum_details_name);
        tv_post = (TextView) findViewById(R.id.tv_forum_details_post);
        tv_post.setVisibility(View.GONE);
        tv_level = (TextView) findViewById(R.id.tv_forum_details_grade);
        tv_time = (TextView) findViewById(R.id.tv_forum_details_time);
        tv_title = (TextView) findViewById(R.id.tv_forum_details_title);
        tv_content = (TextView) findViewById(R.id.tv_forum_details_content);
        iv_one = (ImageView) findViewById(R.id.iv_forum_details_one);
        iv_two = (ImageView) findViewById(R.id.iv_forum_details_two);
        iv_three = (ImageView) findViewById(R.id.iv_forum_details_three);
        iv_four = (ImageView) findViewById(R.id.iv_forum_details_four);
        iv_five = (ImageView) findViewById(R.id.iv_forum_details_five);
        iv_six = (ImageView) findViewById(R.id.iv_forum_details_six);
        iv_seven = (ImageView) findViewById(R.id.iv_forum_details_seven);
        iv_eight = (ImageView) findViewById(R.id.iv_forum_details_eight);
        iv_nine = (ImageView) findViewById(R.id.iv_forum_details_nine);
        iv_one.setVisibility(View.GONE);
        iv_two.setVisibility(View.GONE);
        iv_three.setVisibility(View.GONE);
        iv_four.setVisibility(View.GONE);
        iv_five.setVisibility(View.GONE);
        iv_six.setVisibility(View.GONE);
        iv_seven.setVisibility(View.GONE);
        iv_eight.setVisibility(View.GONE);
        iv_nine.setVisibility(View.GONE);
        iv_collect = (ImageView) findViewById(R.id.iv_collect);
        iv_collect.setOnClickListener(this);
        iv_like = (ImageView) findViewById(R.id.iv_like);
        iv_like.setOnClickListener(this);
        iv_reward = (ImageView) findViewById(R.id.iv_reward);
        iv_reward.setOnClickListener(this);
        tv_collect = (TextView) findViewById(R.id.tv_collect);
        tv_like = (TextView) findViewById(R.id.tv_like);
        tv_reward = (TextView) findViewById(R.id.tv_reward);
        tv_reward.setText("打赏");
        ll_report = (LinearLayout) findViewById(R.id.ll_report);
        ll_report.setOnClickListener(this);
        lv_comment = (ListView) findViewById(R.id.lv_forum_details_comment);
        ll_comment_submit = (LinearLayout) findViewById(R.id.ll_forum_details_comment_submit);
        ll_comment_submit.setOnClickListener(this);
        rl_comment_submit = (RelativeLayout) findViewById(R.id.rl_forum_details_comment_submit);
        rl_comment_submit.setOnClickListener(this);
        commentBeanArrayList = new ArrayList<CommentBean>();
        commentAdapter = new ForumCommentAdapter(mContext, commentBeanArrayList, handler, isCanDelete);
        lv_comment.setAdapter(commentAdapter);
        tv_no_comment = (TextView) findViewById(R.id.tv_forum_details_comment);
        if (commentBeanArrayList.size() > 0) {
            lv_comment.setVisibility(View.VISIBLE);
            tv_no_comment.setVisibility(View.GONE);
        } else {
            lv_comment.setVisibility(View.GONE);
            tv_no_comment.setVisibility(View.VISIBLE);
        }
        tv_more = (TextView) findViewById(R.id.tv_forum_details_comment_more);
        tv_more.setOnClickListener(this);
        tv_more.setVisibility(View.GONE);
        forumDetailsEditText=new ForumDetailsEditText(ForumDetailsActivity.this);
        popshared = new Pop_shared_Activity(this);
        imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + forumBean.getUserPhoto(), iv_head, options);
        tv_name.setText(forumBean.getUserName());
        tv_level.setText("Lv." + forumBean.getUserLevel());
        long time = Long.valueOf(forumBean.getCreatTime());
        tv_time.setText(TimeToolUtils.fromateTimeShowByRule(time * 1000));
        tv_title.setText(forumBean.getTitle());
        tv_content.setText(forumBean.getContent());
        String[] photo = forumBean.getPhoto();
        displayImageView(photo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        judgeAdmin();
        judgeCommunityMaster();
        getForumInfo();
        getForumCommentsCount();
        getForumComments();
    }

    public void getForumCommentsCount() {
//      id(文章id/帖子id),type(1文章，2帖子)
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", forumBean.getId());
        requestParams.put("type", "2");
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_COMMENTS_COUNT, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getForumCommentsCount->" + response);
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
                                commentAdapter.setCommentCount(commentCount);
                                commentAdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 判断是否为圈主
     */
    private void judgeCommunityMaster() {
        //        入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.JUDGE_APPLY_COMMUNITY, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--judgeAdmin->" + response);
//                    {
//                        "status": "success",
//                            "data": "0"
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            String data = response.getString("data");
                            if (data.equals("yes")) {//1是，0不是
                                isMaster = true;
                                isCanDelete=true;
                            } else {
                                isMaster = false;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 判断是否是圈子管理员
     */
    private void judgeAdmin() {
//        入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.JUDGE_COMMUNITY_ADMIN, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--judgeAdmin->" + response);
//                    {
//                        "status": "success",
//                            "data": "0"
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            String data = response.getString("data");
                            if (data.equals("1")) {//1是，0不是
                                isAdmin = true;
                                isCanDelete=true;
                            } else {
                                isAdmin = false;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getForumComments() {
//        入参：userid，refid(圈子id，或父评论id)
        RequestParams requestParrams = new RequestParams();
        requestParrams.put("userid", user.getUserid());
        requestParrams.put("refid", forumBean.getId());
        requestParrams.put("pager", pager);

        NurseAsyncHttpClient.get(CommunityNetConstant.GET_FORUM_COMMENTS, requestParrams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "getForumComments" + response.toString());
                if ("success".equals(response.optString("status"))) {
                    gson = new Gson();
                    if (pager == 1) {
                        commentBeanArrayList.clear();
                    }
                    commentNetBean = gson.fromJson(response.toString(), CommentNetBean.class);
                    commentBeanArrayList.addAll(commentNetBean.getData());
                    if (commentAdapter == null) {
                        commentAdapter = new ForumCommentAdapter(mContext, commentBeanArrayList, handler, isCanDelete);
                        lv_comment.setAdapter(commentAdapter);
                    }
                    commentAdapter.setCommentCount(commentCount);
                    commentAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(lv_comment);
                    if (commentBeanArrayList.size() > 0) {
                        lv_comment.setVisibility(View.VISIBLE);
                        tv_no_comment.setVisibility(View.GONE);
                    } else {
                        lv_comment.setVisibility(View.GONE);
                        tv_no_comment.setVisibility(View.VISIBLE);
                    }
                    if (commentBeanArrayList.size() >= commentCount) {
                        tv_more.setVisibility(View.GONE);
                    } else {
                        tv_more.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void getForumInfo() {
        //userid,tid
        RequestParams requestParrams = new RequestParams();
        requestParrams.put("userid", user.getUserid());
        requestParrams.put("tid", forumBean.getId());
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_FORUM_INFO, requestParrams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "getForumInfo" + response.toString());
                if ("success".equals(response.optString("status"))) {
                    gson = new Gson();
                    forumInfoBean = gson.fromJson(response.toString(), ForumInfoBean.class);
                    LogUtils.e(TAG, "forumInfoBean-->" + forumInfoBean.toString());
                    LogUtils.e(TAG, "forumInfoBean-->" + forumInfoBean.getData());
                    LogUtils.e(TAG, "forumInfoBean-->" + forumInfoBean.getData().toString());
                    display(forumInfoBean.getData());
                }
            }
        });
    }

    private void display(ForumInfoBean.DataBean dataBean) {
        LogUtils.e(TAG, "dataBean" + dataBean.toString());
        long time = Long.valueOf(dataBean.getCreatTime());
        tv_time.setText(TimeToolUtils.fromateTimeShowByRule(time * 1000));
        tv_title.setText(dataBean.getTitle());
        tv_content.setText(dataBean.getContent());
        String[] photo = dataBean.getPhoto();
        displayImageView(photo);
        tv_collect.setText(dataBean.getCollect_num());
        tv_like.setText(dataBean.getLike());
        if (dataBean.getCollect().equals("1")) {//收藏
            iv_collect.setImageResource(R.mipmap.ic_collecttion);
        } else {
            iv_collect.setImageResource(R.mipmap.ic_collecttion_defult);
        }
        if (dataBean.getAddLike().equals("1")) {//点赞
            iv_like.setImageResource(R.mipmap.ic_like);
        } else {
            iv_like.setImageResource(R.mipmap.ic_like_defult);
        }
        if (dataBean.getIsReward().equals("1")) {//打赏
            iv_reward.setImageResource(R.mipmap.ic_reward);
        } else {
            iv_reward.setImageResource(R.mipmap.ic_reward_defult);
        }
    }

    private void displayImageView(String[] photo) {
        if (photo != null && photo.length > 0) {
            int imNumb = photo.length;
            iv_one.setVisibility(View.VISIBLE);
            imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[0], iv_one, options);
            iv_one.setOnClickListener(new ImageListener(photo, 0));
            LogUtils.e("imNumb--->>", String.valueOf(imNumb));
            // 四张图的时间情况比较特殊
            if (imNumb == 4) {
                iv_two.setVisibility(View.VISIBLE);
                imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[1], iv_two, options);
                iv_two.setOnClickListener(new ImageListener(photo, 1));
                iv_four.setVisibility(View.VISIBLE);
                imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[2], iv_four, options);
                iv_four.setOnClickListener(new ImageListener(photo, 2));
                iv_five.setVisibility(View.VISIBLE);
                imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[3], iv_five, options);
                iv_five.setOnClickListener(new ImageListener(photo, 3));
            } else {
                if (imNumb > 1) {
                    iv_two.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[1], iv_two, options);
                    iv_two.setOnClickListener(new ImageListener(photo, 1));
                    if (imNumb > 2) {
                        iv_three.setVisibility(View.VISIBLE);
                        imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[2], iv_three, options);
                        iv_three.setOnClickListener(new ImageListener(photo, 2));
                        if (imNumb > 3) {
                            iv_four.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[3], iv_four, options);
                            iv_four.setOnClickListener(new ImageListener(photo, 3));
                            if (imNumb > 4) {
                                iv_five.setVisibility(View.VISIBLE);
                                imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[4], iv_five, options);
                                iv_five.setOnClickListener(new ImageListener(photo, 4));
                                if (imNumb > 5) {
                                    iv_six.setVisibility(View.VISIBLE);
                                    imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[5], iv_six, options);
                                    iv_six.setOnClickListener(new ImageListener(photo, 5));
                                    if (imNumb > 6) {
                                        iv_seven.setVisibility(View.VISIBLE);
                                        imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[6], iv_seven, options);
                                        iv_seven.setOnClickListener(new ImageListener(photo, 6));
                                        if (imNumb > 7) {
                                            iv_eight.setVisibility(View.VISIBLE);
                                            imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[7], iv_eight, options);
                                            iv_eight.setOnClickListener(new ImageListener(photo, 7));
                                            if (imNumb > 8) {
                                                iv_nine.setVisibility(View.VISIBLE);
                                                imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + photo[8], iv_nine, options);
                                                iv_nine.setOnClickListener(new ImageListener(photo, 8));
                                            } else {
                                                iv_nine.setVisibility(View.GONE);
                                            }
                                        } else {
                                            iv_eight.setVisibility(View.GONE);
                                            iv_nine.setVisibility(View.GONE);
                                        }
                                    } else {
                                        iv_seven.setVisibility(View.GONE);
                                        iv_eight.setVisibility(View.GONE);
                                        iv_nine.setVisibility(View.GONE);
                                    }
                                } else {
                                    iv_six.setVisibility(View.GONE);
                                    iv_seven.setVisibility(View.GONE);
                                    iv_eight.setVisibility(View.GONE);
                                    iv_nine.setVisibility(View.GONE);
                                }
                            } else {
                                iv_five.setVisibility(View.GONE);
                                iv_six.setVisibility(View.GONE);
                                iv_seven.setVisibility(View.GONE);
                                iv_eight.setVisibility(View.GONE);
                                iv_nine.setVisibility(View.GONE);
                            }
                        } else {
                            iv_four.setVisibility(View.GONE);
                            iv_five.setVisibility(View.GONE);
                            iv_six.setVisibility(View.GONE);
                            iv_seven.setVisibility(View.GONE);
                            iv_eight.setVisibility(View.GONE);
                            iv_nine.setVisibility(View.GONE);
                        }
                    } else {
                        iv_three.setVisibility(View.GONE);
                        iv_four.setVisibility(View.GONE);
                        iv_five.setVisibility(View.GONE);
                        iv_six.setVisibility(View.GONE);
                        iv_seven.setVisibility(View.GONE);
                        iv_eight.setVisibility(View.GONE);
                        iv_nine.setVisibility(View.GONE);
                    }
                } else {
                    iv_two.setVisibility(View.GONE);
                    iv_three.setVisibility(View.GONE);
                    iv_four.setVisibility(View.GONE);
                    iv_five.setVisibility(View.GONE);
                    iv_six.setVisibility(View.GONE);
                    iv_seven.setVisibility(View.GONE);
                    iv_eight.setVisibility(View.GONE);
                    iv_nine.setVisibility(View.GONE);
                }
            }
        } else {
            iv_one.setVisibility(View.GONE);
            iv_two.setVisibility(View.GONE);
            iv_three.setVisibility(View.GONE);
            iv_four.setVisibility(View.GONE);
            iv_five.setVisibility(View.GONE);
            iv_six.setVisibility(View.GONE);
            iv_seven.setVisibility(View.GONE);
            iv_eight.setVisibility(View.GONE);
            iv_nine.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_forum_details_back:
                finish();
                break;
            case R.id.tv_forum_details_comment_more:
                pager = pager + 1;
                getForumCommentsCount();
                getForumComments();
                break;
            case R.id.ll_forum_details_comment_submit:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    replayID = forumBean.getId();
                    replayType = "2";
                    forumDetailsEditText.showAsRelayComment(ll_comment_submit, replayID, replayType, replayName);
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_forum_details_share:
                if (user.getUserid().length() <= 0) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                } else {
                    //TODO 无图片的情况未处理
                    webview = "http://nurse.xiaocool.net/index.php?g=HomePage&m=CommunityShare&a=index&tid=" + forumInfoBean.getData().getId() + "&type=1&userid=" + user.getUserid();
                    if (forumInfoBean.getData().getPhoto().length > 0) {
                        path = NetBaseConstant.NET_HOST + "/"+forumInfoBean.getData().getPhoto()[0];
                    } else {
                        path = null;
                    }

                    //分享到朋友圈
                    popshared.showAsDropDown(ll_comment_submit, forumInfoBean.getData().getDescription(), webview, forumInfoBean.getData().getTitle(), path);
                }
                break;
            case R.id.iv_forum_details_head:
                Intent intentHomePage = new Intent();
                intentHomePage.setClass(mContext, PersonalHomePageActivity.class);
                intentHomePage.putExtra("forum", forumBean);
                intentHomePage.putExtra("from", "ForumDetailsActivity");
                startActivityForResult(intentHomePage, INTENT_HOME_PAGE);
                break;
            case R.id.iv_collect:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    if (forumInfoBean.getData().getCollect().equals("1")) {//收藏
                        cancelFavoriteForForum();
                    } else {
                        iv_collect.setImageResource(R.mipmap.ic_collecttion);
                        addFavoriteForForum();
                    }
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.iv_like:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    if (forumInfoBean.getData().getAddLike().equals("1")) {//点赞
                        resetLikeForForum();
                    } else {
                        setLikeForForum();
                    }
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.iv_reward://打赏
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    getNurseScore();
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.ll_report:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    if (isAdmin) {//管理员
                        if (forumInfoBean.getData().getUserId().equals(user.getUserid())) {//自己
                            LayoutInflater inflater = getLayoutInflater();
                            View view = inflater.inflate(R.layout.dialog_forum_detail_recommend, (ViewGroup) findViewById(R.id.ll_dialog_forum_details_recommend));
                            final TextView tv_recommend = (TextView) view.findViewById(R.id.tv_dialog_recommend);
                            final TextView tv_best = (TextView) view.findViewById(R.id.tv_dialog_best);
                            final TextView tv_top = (TextView) view.findViewById(R.id.tv_dialog_top);
                            final TextView tv_delete = (TextView) view.findViewById(R.id.tv_dialog_delete);
                            final TextView tv_report = (TextView) view.findViewById(R.id.tv_dialog_report);
                            final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                            tv_report.setVisibility(View.GONE);
                            tv_recommend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setRecommend();
                                    recommend.dismiss();
                                }
                            });
                            tv_best.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setBest();
                                    recommend.dismiss();
                                }
                            });
                            tv_top.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setTop();
                                    recommend.dismiss();
                                }
                            });
                            tv_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteForum();
                                    recommend.dismiss();
                                }
                            });
                            tv_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recommend.dismiss();
                                }
                            });
                            recommend = new Dialog(mContext);
                            recommend.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            recommend.setContentView(view);
                            Window dialogWindow = recommend.getWindow();
                            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM | Gravity.RIGHT);
                            recommend.show();

                        } else {
                            LayoutInflater inflater = getLayoutInflater();
                            View view = inflater.inflate(R.layout.dialog_forum_detail_recommend, (ViewGroup) findViewById(R.id.ll_dialog_forum_details_recommend));
                            final TextView tv_recommend = (TextView) view.findViewById(R.id.tv_dialog_recommend);
                            final TextView tv_best = (TextView) view.findViewById(R.id.tv_dialog_best);
                            final TextView tv_top = (TextView) view.findViewById(R.id.tv_dialog_top);
                            final TextView tv_delete = (TextView) view.findViewById(R.id.tv_dialog_delete);
                            final TextView tv_report = (TextView) view.findViewById(R.id.tv_dialog_report);
                            final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                            tv_recommend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setRecommend();
                                    recommend.dismiss();
                                }
                            });
                            tv_best.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setBest();
                                    recommend.dismiss();
                                }
                            });
                            tv_top.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setTop();
                                    recommend.dismiss();
                                }
                            });
                            tv_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteForum();
                                    recommend.dismiss();
                                }
                            });
                            tv_report.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toReport(forumInfoBean.getData().getId(), "1");
                                    recommend.dismiss();
                                }
                            });
                            tv_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recommend.dismiss();
                                }
                            });
                            recommend = new Dialog(mContext);
                            recommend.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            recommend.setContentView(view);
                            Window dialogWindow = recommend.getWindow();
                            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM | Gravity.RIGHT);
                            recommend.show();


                        }
                    } else {
                        if (isMaster) {//圈主
                            if (forumInfoBean.getData().getUserId().equals(user.getUserid())) {//自己
                                LayoutInflater inflater = getLayoutInflater();
                                View view = inflater.inflate(R.layout.dialog_forum_detail_recommend, (ViewGroup) findViewById(R.id.ll_dialog_forum_details_recommend));
                                final TextView tv_recommend = (TextView) view.findViewById(R.id.tv_dialog_recommend);
                                final TextView tv_best = (TextView) view.findViewById(R.id.tv_dialog_best);
                                final TextView tv_top = (TextView) view.findViewById(R.id.tv_dialog_top);
                                final TextView tv_delete = (TextView) view.findViewById(R.id.tv_dialog_delete);
                                final TextView tv_report = (TextView) view.findViewById(R.id.tv_dialog_report);
                                final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                                tv_recommend.setVisibility(View.GONE);
                                tv_report.setVisibility(View.GONE);
                                tv_best.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setBest();
                                        recommend.dismiss();
                                    }
                                });
                                tv_top.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setTop();
                                        recommend.dismiss();
                                    }
                                });
                                tv_delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteForum();
                                        recommend.dismiss();
                                    }
                                });
                                tv_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        recommend.dismiss();
                                    }
                                });
                                recommend = new Dialog(mContext);
                                recommend.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                recommend.setContentView(view);
                                Window dialogWindow = recommend.getWindow();
                                dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM | Gravity.RIGHT);
                                recommend.show();

                            } else {
                                LayoutInflater inflater = getLayoutInflater();
                                View view = inflater.inflate(R.layout.dialog_forum_detail_recommend, (ViewGroup) findViewById(R.id.ll_dialog_forum_details_recommend));
                                final TextView tv_recommend = (TextView) view.findViewById(R.id.tv_dialog_recommend);
                                final TextView tv_best = (TextView) view.findViewById(R.id.tv_dialog_best);
                                final TextView tv_top = (TextView) view.findViewById(R.id.tv_dialog_top);
                                final TextView tv_delete = (TextView) view.findViewById(R.id.tv_dialog_delete);
                                final TextView tv_report = (TextView) view.findViewById(R.id.tv_dialog_report);
                                final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                                tv_recommend.setVisibility(View.GONE);
                                tv_best.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setBest();
                                        recommend.dismiss();
                                    }
                                });
                                tv_top.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setTop();
                                        recommend.dismiss();
                                    }
                                });
                                tv_delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteForum();
                                        recommend.dismiss();
                                    }
                                });
                                tv_report.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toReport(forumInfoBean.getData().getId(), "1");
                                        recommend.dismiss();
                                    }
                                });
                                tv_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        recommend.dismiss();
                                    }
                                });
                                recommend = new Dialog(mContext);
                                recommend.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                recommend.setContentView(view);
                                Window dialogWindow = recommend.getWindow();
                                dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM | Gravity.RIGHT);
                                recommend.show();

                            }
                        } else {
                            if (forumInfoBean.getData().getUserId().equals(user.getUserid())) {//自己
                                LayoutInflater inflater = getLayoutInflater();
                                View view = inflater.inflate(R.layout.dialog_forum_detail_recommend, (ViewGroup) findViewById(R.id.ll_dialog_forum_details_recommend));
                                final TextView tv_recommend = (TextView) view.findViewById(R.id.tv_dialog_recommend);
                                final TextView tv_best = (TextView) view.findViewById(R.id.tv_dialog_best);
                                final TextView tv_top = (TextView) view.findViewById(R.id.tv_dialog_top);
                                final TextView tv_delete = (TextView) view.findViewById(R.id.tv_dialog_delete);
                                final TextView tv_report = (TextView) view.findViewById(R.id.tv_dialog_report);
                                final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                                tv_recommend.setVisibility(View.GONE);
                                tv_best.setVisibility(View.GONE);
                                tv_top.setVisibility(View.GONE);
                                tv_report.setVisibility(View.GONE);
                                tv_delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteForum();
                                        recommend.dismiss();
                                    }
                                });
                                tv_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        recommend.dismiss();
                                    }
                                });
                                recommend = new Dialog(mContext);
                                recommend.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                recommend.setContentView(view);
                                Window dialogWindow = recommend.getWindow();
                                dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM | Gravity.RIGHT);
                                recommend.show();

                            } else {

                                LayoutInflater inflater = getLayoutInflater();
                                View view = inflater.inflate(R.layout.dialog_forum_detail_recommend, (ViewGroup) findViewById(R.id.ll_dialog_forum_details_recommend));
                                final TextView tv_recommend = (TextView) view.findViewById(R.id.tv_dialog_recommend);
                                final TextView tv_best = (TextView) view.findViewById(R.id.tv_dialog_best);
                                final TextView tv_top = (TextView) view.findViewById(R.id.tv_dialog_top);
                                final TextView tv_delete = (TextView) view.findViewById(R.id.tv_dialog_delete);
                                final TextView tv_report = (TextView) view.findViewById(R.id.tv_dialog_report);
                                final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                                tv_recommend.setVisibility(View.GONE);
                                tv_best.setVisibility(View.GONE);
                                tv_top.setVisibility(View.GONE);
                                tv_delete.setVisibility(View.GONE);
                                tv_report.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toReport(forumInfoBean.getData().getId(), "1");
                                        recommend.dismiss();
                                    }
                                });
                                tv_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        recommend.dismiss();
                                    }
                                });
                                recommend = new Dialog(mContext);
                                recommend.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                recommend.setContentView(view);
                                Window dialogWindow = recommend.getWindow();
                                dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM | Gravity.RIGHT);
                                recommend.show();

                            }
                        }
                    }

                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                break;

        }
    }

    private void setBest() {
//        入参：tid(帖子id),to_userid(收信人用户id),from_userid(发信人id)
        RequestParams requestParams = new RequestParams();
        requestParams.put("tid", forumBean.getId());
        requestParams.put("to_userid", forumBean.getUserId());
        requestParams.put("from_userid", user.getUserid());
        NurseAsyncHttpClient.post(CommunityNetConstant.FORUM_SET_BEST, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            LogUtils.e(TAG, "--setBest->" + response);
////                    {
//                    "status":"success",
//                            "data":{
//                        "score":"2",
//                                "event":"回帖",
//                                "create_time":1483976183,
//                                "userid":"17026"
//                    }
//                }
                            try {
                                String status = response.getString("status");
                                if (status.equals("success")) {
                                    getForumComments();
                                    JSONObject json = response.getJSONObject("data");
                                    if (json.getString("score") != null && json.getString("score").length() > 0) {
                                        View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_score, null);
                                        dialog = new AlertDialog.Builder(mContext).create();
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
                    }
                }

        );

    }

    private void setTop() {
        //入参：tid(帖子id),to_userid(收信人用户id),from_userid(发信人id)
        RequestParams requestParams = new RequestParams();
        requestParams.put("tid", forumBean.getId());
        requestParams.put("to_userid", forumBean.getUserId());
        requestParams.put("from_userid", user.getUserid());
        NurseAsyncHttpClient.post(CommunityNetConstant.FORUM_SET_TOP, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            LogUtils.e(TAG, "--setTop->" + response);
////                    {
//                    "status":"success",
//                            "data":{
//                        "score":"2",
//                                "event":"回帖",
//                                "create_time":1483976183,
//                                "userid":"17026"
//                    }
//                }
                            try {
                                String status = response.getString("status");
                                if (status.equals("success")) {
                                    getForumComments();
                                    JSONObject json = response.getJSONObject("data");
                                    if (json.getString("score") != null && json.getString("score").length() > 0) {
                                        View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_score, null);
                                        dialog = new AlertDialog.Builder(mContext).create();
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
                    }
                }

        );


    }

    private void setRecommend() {
        //        入参：tid(帖子id),to_userid(收信人用户id),from_userid(发信人id)
        RequestParams requestParams = new RequestParams();
        requestParams.put("tid", forumBean.getId());
        requestParams.put("to_userid", forumBean.getUserId());
        requestParams.put("from_userid", user.getUserid());
        NurseAsyncHttpClient.post(CommunityNetConstant.FORUM_SET_RECOMMEND, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            LogUtils.e(TAG, "--setRecommend->" + response);
////                    {
//                    "status":"success",
//                            "data":{
//                        "score":"2",
//                                "event":"回帖",
//                                "create_time":1483976183,
//                                "userid":"17026"
//                    }
//                }
                            try {
                                String status = response.getString("status");
                                if (status.equals("success")) {
                                    getForumComments();
                                    JSONObject json = response.getJSONObject("data");
                                    if (json.getString("score") != null && json.getString("score").length() > 0) {
                                        View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_score, null);
                                        dialog = new AlertDialog.Builder(mContext).create();
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
                    }
                }

        );


    }


    private void replay(String comment) {
//        入参(post)：userid,id，content,type:,1、新闻2、圈子,3评论,photo
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("id", replayID);
        requestParams.put("content", comment);
        requestParams.put("type", replayType);

        NurseAsyncHttpClient.post(CommunityNetConstant.SET_COMMENT, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            LogUtils.e(TAG, "--replay->" + response);
////                    {
//                    "status":"success",
//                            "data":{
//                        "score":"2",
//                                "event":"回帖",
//                                "create_time":1483976183,
//                                "userid":"17026"
//                    }
//                }
                            try {
                                String status = response.getString("status");
                                if (status.equals("success")) {
                                    getForumComments();
                                    JSONObject json = response.getJSONObject("data");
                                    if (json.getString("score") != null && json.getString("score").length() > 0) {
                                        View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_score, null);
                                        dialog = new AlertDialog.Builder(mContext).create();
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
                    }
                }

        );

    }

    private void toReport(final String id, final String type) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_forum_details_report, (ViewGroup) findViewById(R.id.ll_dialog_forum_details_report));
        final TextView tv_report_abusive = (TextView) view.findViewById(R.id.tv_report_abusive);
        final TextView tv_report_pornographic = (TextView) view.findViewById(R.id.tv_report_pornographic);
        final TextView tv_report_advertisement = (TextView) view.findViewById(R.id.tv_report_advertisement);
        final TextView tv_report_violence = (TextView) view.findViewById(R.id.tv_report_violence);
        final LinearLayout ll_report_cheat = (LinearLayout) view.findViewById(R.id.ll_report_cheat);
        final TextView tv_report_cheat = (TextView) view.findViewById(R.id.tv_report_cheat);
        final TextView tv_report_cheat_item = (TextView) view.findViewById(R.id.tv_report_cheat_item);
        final LinearLayout ll_report_illegal = (LinearLayout) view.findViewById(R.id.ll_report_illegal);
        final TextView tv_report_illegal = (TextView) view.findViewById(R.id.tv_report_illegal);
        final TextView tv_report_illegal_item = (TextView) view.findViewById(R.id.tv_report_illegal_item);
        reportString = getString(R.string.tv_report_abusive);
        tv_report_abusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportString = getString(R.string.tv_report_abusive);
                LogUtils.e(TAG, "-reportString--" + reportString);
                tv_report_abusive.setTextColor(getResources().getColor(R.color.whilte));
                tv_report_abusive.setBackgroundResource(R.drawable.bt_dialog_forum_details_report);
                tv_report_pornographic.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_pornographic.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_advertisement.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_advertisement.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_violence.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_violence.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_cheat.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_cheat_item.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                ll_report_cheat.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_illegal.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_illegal_item.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                ll_report_illegal.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);


            }
        });

        tv_report_pornographic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportString = getString(R.string.tv_report_pornographic);
                LogUtils.e(TAG, "-reportString--" + reportString);
                tv_report_abusive.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_abusive.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_pornographic.setTextColor(getResources().getColor(R.color.white));
                tv_report_pornographic.setBackgroundResource(R.drawable.bt_dialog_forum_details_report);
                tv_report_advertisement.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_advertisement.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_violence.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_violence.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_cheat.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_cheat_item.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                ll_report_cheat.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_illegal.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_illegal_item.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                ll_report_illegal.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);

            }
        });

        tv_report_advertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportString = getString(R.string.tv_report_advertisement);
                LogUtils.e(TAG, "-reportString--" + reportString);
                tv_report_abusive.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_abusive.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_pornographic.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_pornographic.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_advertisement.setTextColor(getResources().getColor(R.color.white));
                tv_report_advertisement.setBackgroundResource(R.drawable.bt_dialog_forum_details_report);
                tv_report_violence.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_violence.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_cheat.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_cheat_item.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                ll_report_cheat.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_illegal.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_illegal_item.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                ll_report_illegal.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);

            }
        });

        tv_report_violence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportString = getString(R.string.tv_report_violence);
                LogUtils.e(TAG, "-reportString--" + reportString);
                tv_report_abusive.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_abusive.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_pornographic.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_pornographic.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_advertisement.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_advertisement.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_violence.setTextColor(getResources().getColor(R.color.white));
                tv_report_violence.setBackgroundResource(R.drawable.bt_dialog_forum_details_report);
                tv_report_cheat.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_cheat_item.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                ll_report_cheat.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_illegal.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_illegal_item.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                ll_report_illegal.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);

            }
        });

        ll_report_cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportString = getString(R.string.tv_report_cheat) + getString(R.string.tv_report_cheat_item);
                LogUtils.e(TAG, "-reportString--" + reportString);
                tv_report_abusive.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_abusive.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_pornographic.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_pornographic.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_advertisement.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_advertisement.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_violence.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_violence.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_cheat.setTextColor(getResources().getColor(R.color.white));
                tv_report_cheat_item.setTextColor(getResources().getColor(R.color.white));
                ll_report_cheat.setBackgroundResource(R.drawable.bt_dialog_forum_details_report);
                tv_report_illegal.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_illegal_item.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                ll_report_illegal.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);

            }
        });

        ll_report_illegal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportString = getString(R.string.tv_report_illegal) + getString(R.string.tv_report_illegal_item);
                LogUtils.e(TAG, "-reportString--" + reportString);
                tv_report_abusive.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_abusive.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_pornographic.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_pornographic.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_advertisement.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_advertisement.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_violence.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_violence.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_cheat.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                tv_report_cheat_item.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                ll_report_cheat.setBackgroundResource(R.drawable.bt_dialog_forum_details_normal);
                tv_report_illegal.setTextColor(getResources().getColor(R.color.white));
                tv_report_illegal_item.setTextColor(getResources().getColor(R.color.white));
                ll_report_illegal.setBackgroundResource(R.drawable.bt_dialog_forum_details_report);

            }
        });
        Button bt_reward = (Button) view.findViewById(R.id.bt_dialog_forum_details_reward);
        bt_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReportForForum(id, type);
            }
        });
        report = new AlertDialog.Builder(mContext).setView(view).show();

    }

    private void getNurseScore() {
//        入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_NURSE_SCORE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getNurseScore->" + response);
//                    {
//                        "status": "success",
//                            "data": "203"
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            String score = response.getString("data");
                            LayoutInflater inflater = getLayoutInflater();
                            View view = inflater.inflate(R.layout.dialog_forum_details_reward, (ViewGroup) findViewById(R.id.ll_dialog_forum_details_reward));
                            TextView tv_money = (TextView) view.findViewById(R.id.tv_dialog_forum_reward_money);
                            tv_money.setText(score);
                            final long myScore = Long.valueOf(score);
                            final TextView tv_money_one = (TextView) view.findViewById(R.id.tv_dialog_forum_reward_money_one);
                            final TextView tv_money_two = (TextView) view.findViewById(R.id.tv_dialog_forum_reward_money_two);
                            final TextView tv_money_five = (TextView) view.findViewById(R.id.tv_dialog_forum_reward_money_five);
                            final TextView tv_money_ten = (TextView) view.findViewById(R.id.tv_dialog_forum_reward_money_ten);

                            rewardString = "1";
                            tv_money_one.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rewardString = "1";
                                    LogUtils.e(TAG, "-rewardString--" + rewardString);
                                    tv_money_one.setTextColor(getResources().getColor(R.color.white));
                                    tv_money_one.setBackgroundResource(R.drawable.bg_dialog_forum_reward_press);
                                    tv_money_two.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_two.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);
                                    tv_money_five.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_five.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);
                                    tv_money_ten.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_ten.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);

                                }
                            });
                            tv_money_two.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rewardString = "2";
                                    LogUtils.e(TAG, "-rewardString--" + rewardString);
                                    tv_money_one.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_one.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);
                                    tv_money_two.setTextColor(getResources().getColor(R.color.white));
                                    tv_money_two.setBackgroundResource(R.drawable.bg_dialog_forum_reward_press);
                                    tv_money_five.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_five.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);
                                    tv_money_ten.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_ten.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);

                                }
                            });
                            tv_money_five.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rewardString = "5";
                                    LogUtils.e(TAG, "-rewardString--" + rewardString);
                                    tv_money_one.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_one.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);
                                    tv_money_two.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_two.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);
                                    tv_money_five.setTextColor(getResources().getColor(R.color.white));
                                    tv_money_five.setBackgroundResource(R.drawable.bg_dialog_forum_reward_press);
                                    tv_money_ten.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_ten.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);

                                }
                            });
                            tv_money_ten.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rewardString = "10";
                                    LogUtils.e(TAG, "-rewardString--" + rewardString);
                                    tv_money_one.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_one.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);
                                    tv_money_two.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_two.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);
                                    tv_money_five.setTextColor(getResources().getColor(R.color.mine_gray_unselected));
                                    tv_money_five.setBackgroundResource(R.drawable.bg_dialog_forum_reward_defult);
                                    tv_money_ten.setTextColor(getResources().getColor(R.color.white));
                                    tv_money_ten.setBackgroundResource(R.drawable.bg_dialog_forum_reward_press);

                                }
                            });
                            Button bt_reward = (Button) view.findViewById(R.id.bt_dialog_forum_details_reward);
                            bt_reward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (myScore >= Long.valueOf(rewardString)) {
                                        addReward();
                                    } else {
                                        ToastUtils.ToastShort(mContext, "您的护士币不足！");
                                    }

                                }
                            });
                            reward = new AlertDialog.Builder(mContext).setView(view).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 删除帖子
     */
    private void deleteForum() {
//        入参：tid 帖子id
        RequestParams requestParams = new RequestParams();
        requestParams.put("tid", forumInfoBean.getData().getId());
        NurseAsyncHttpClient.get(CommunityNetConstant.DELETE_FORUM, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--deleteForum->" + response);

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
                           ToastUtils.ToastShort(mContext,"删除成功!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    /**
     * 添加举报
     *
     * @param id
     * @param type
     */
    private void addReportForForum(String id, String type) {
        //入参：userid,t_id(帖子id或评论id),score,type(1帖子，2评论)
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("t_id", id);
        requestParams.put("score", reportString);
        requestParams.put("type", type);
        NurseAsyncHttpClient.get(CommunityNetConstant.ADD_REPORT, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--addReportForForum->" + response);
//                    {
//                        "status":"success",
//                            "data":{
//                        "type":"1",
//                                "userid":"17026",
//                                "t_id":"38",
//                                "score":"1",
//                                "create_time":1483366785
//                    }
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            ToastUtils.ToastShort(mContext, "举报成功！");
                            report.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    /**
     * 添加打赏
     */
    private void addReward() {
        //入参：to_userid(被打赏用户id),from_userid(发出打赏用户的id),t_id(帖子id),score
        RequestParams requestParams = new RequestParams();
        requestParams.put("to_userid", forumInfoBean.getData().getUserId());
        requestParams.put("from_userid", user.getUserid());
        requestParams.put("t_id", forumInfoBean.getData().getId());
        requestParams.put("score", rewardString);
        NurseAsyncHttpClient.get(CommunityNetConstant.ADD_REWARD, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--addReward->" + response);

//                    {
//                        "status":"success",
//                            "data":"success"
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            ToastUtils.ToastShort(mContext, "打赏成功！");
                            iv_reward.setImageResource(R.mipmap.ic_reward);
                            forumInfoBean.getData().setIsReward("1");
                            reward.dismiss();
                        }else{
                            ToastUtils.ToastShort(mContext, "打赏失败！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }

    /**
     * 点赞
     */
    private void setLikeForForum() {
        //入参：userid,id,type(新闻资讯为1，2圈子,3评论)
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("id", forumInfoBean.getData().getId());

        requestParams.put("type", 2);


        NurseAsyncHttpClient.get(CommunityNetConstant.SET_LIKE, requestParams, new

                        JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (response != null) {
                                    LogUtils.e(TAG, "--setLikeForForum->" + response);
//             {
//                                    "status": "success",
//                                            "data": {
//                                        "score": "2",
//                                                "event": "点赞",
//                                                "create_time": 1483976389,
//                                                "userid": "17026"
//                                    }
//                                }
                                    try {
                                        String status = response.getString("status");
                                        if (status.equals("success")) {
                                            forumInfoBean.getData().setAddLike("1");
                                            iv_like.setImageResource(R.mipmap.ic_like);
                                            forumInfoBean.getData().setLike("" + (Integer.valueOf(forumInfoBean.getData().getLike()) + 1));
                                            tv_like.setText(forumInfoBean.getData().getLike());
                                            JSONObject json = response.getJSONObject("data");
                                            if (json.getString("score") != null && json.getString("score").length() > 0) {
                                                View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_score, null);
                                                dialog = new AlertDialog.Builder(mContext).create();
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
                            }
                        }

        );
    }

    /**
     * 取消点赞
     */
    private void resetLikeForForum() {
        //入参：userid,id,type
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("id", forumInfoBean.getData().getId());
        requestParams.put("type", "2");
        NurseAsyncHttpClient.get(CommunityNetConstant.RESET_LIKE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--resetLikeForForum->" + response);
//                    {
//                        "status":"success",
//                            "data":"取消赞成功！"
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            forumInfoBean.getData().setAddLike("0");
                            iv_like.setImageResource(R.mipmap.ic_like_defult);
                            forumInfoBean.getData().setLike("" + (Integer.valueOf(forumInfoBean.getData().getLike()) - 1));
                            tv_like.setText(forumInfoBean.getData().getLike());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    /**
     * 检查是否点赞
     */
    private void checkHadLikeForForum() {
        //入参：userid,id,type(新闻资讯为1，2圈子,3评论)
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("id", forumInfoBean.getData().getId());
        requestParams.put("type", "2");
        NurseAsyncHttpClient.get(CommunityNetConstant.CHECK_HAD_LIKE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--checkHadLikeForForum->" + response);
//                    {
//                        "status": "success",
//                            "data": {
//                        "follows_count": "0",
//                                "fans_count": "1"
//                    }
//                    }
//                    try {
//                        String status = response.getString("status");
//                        if (status.equals("success")) {
//                            JSONObject j = response.getJSONObject("data");
//                            tv_attention.setText(j.getString("follows_count"));
//                            tv_fans.setText(j.getString("fans_count"));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                }
            }
        });


    }

    /**
     * 收藏
     */
    private void addFavoriteForForum() {
        //入参：userid,refid,type:1、新闻、2考试,3其他收藏(学习中的新闻),4论坛帖子,5招聘,6用户,title,description
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("refid", forumInfoBean.getData().getId());
        requestParams.put("type", "4");
        requestParams.put("title", forumInfoBean.getData().getTitle());
        requestParams.put("description", forumInfoBean.getData().getDescription());
        NurseAsyncHttpClient.get(CommunityNetConstant.ADD_FAVORITE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--addFavoriteForForum->" + response);
//                    {
//                        "status":"success",
//                            "data":"50216"
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            forumInfoBean.getData().setCollect("1");
                            iv_collect.setImageResource(R.mipmap.ic_collecttion);
                            forumInfoBean.getData().setCollect_num("" + (Integer.valueOf(forumInfoBean.getData().getCollect_num()) + 1));
                            tv_collect.setText(forumInfoBean.getData().getCollect_num());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }

    /**
     * 取消收藏
     */
    private void cancelFavoriteForForum() {
        //入参：userid,refid,type:1、新闻、2考试,4论坛帖子,5招聘,6用户
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("refid", forumInfoBean.getData().getId());
        requestParams.put("type", "4");
        NurseAsyncHttpClient.get(CommunityNetConstant.CANCEL_FAVORITE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--cancelFavoriteForForum->" + response);
//                    {
//                        "status":"success",
//                            "data":1
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            forumInfoBean.getData().setCollect("0");
                            iv_collect.setImageResource(R.mipmap.ic_collecttion_defult);
                            forumInfoBean.getData().setCollect_num("" + (Integer.valueOf(forumInfoBean.getData().getCollect_num()) - 1));
                            tv_collect.setText(forumInfoBean.getData().getCollect_num());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }

    class ImageListener implements View.OnClickListener {
        String[] photo;
        int page;

        public ImageListener(String[] photo, int page) {
            this.photo = photo;
            this.page = page;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClass(mContext, ImageCycleActivity.class);
            intent.putExtra("photo", photo);
            intent.putExtra("position", page);
            mContext.startActivity(intent);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_HOME_PAGE:
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
