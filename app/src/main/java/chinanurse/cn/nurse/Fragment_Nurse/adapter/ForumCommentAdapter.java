package chinanurse.cn.nurse.Fragment_Nurse.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chinanurse.cn.nurse.ui.MyListview;
import chinanurse.cn.nurse.Fragment_Nurse.activity.PersonalHomePageActivity;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommentBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommentChildBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.TimeToolUtils;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.picture.RoudImage;
import cz.msebera.android.httpclient.Header;

public class ForumCommentAdapter extends BaseAdapter {
    private String TAG = "ForumCommentAdapter";
    private UserBean userBean;
    private Context mContext;
    private ArrayList<CommentBean> commentBeanArrayList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private Handler handler;
    private ForumCommentChildAdapter childAdapter;
    private ArrayList<CommentChildBean> commentChildBeanArrayList;
    private boolean isCanDelete = false;
    private Dialog dialog;
    private Long commentCount;


    public ForumCommentAdapter(Context mContext, ArrayList<CommentBean> commentBeanArrayList, Handler handler, boolean isCanDelete) {
        this.mContext = mContext;
        this.commentBeanArrayList = commentBeanArrayList;
        this.handler = handler;
        this.isCanDelete=isCanDelete;
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_head_nor).showImageOnFail(R.mipmap.img_head_nor).cacheInMemory(true).cacheOnDisc(true).build();
        userBean = new UserBean(mContext);
    }

    public void setCommentCount(Long commentCount){
        this.commentCount=commentCount;
    }

    @Override
    public int getCount() {
        return commentBeanArrayList.size();
    }

    @Override
    public CommentBean getItem(int position) {
        return commentBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler holder = null;
        final CommentBean commentBean = commentBeanArrayList.get(position);
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_forum_comment, null);
            holder = new ViewHodler();
            holder.iv_head = (RoudImage) convertView.findViewById(R.id.iv_item_forum_comment_head);
            holder.iv_authentication = (ImageView) convertView.findViewById(R.id.iv_item_forum_comment_authentication);
            holder.iv_like = (ImageView) convertView.findViewById(R.id.iv_item_forum_comment_like);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_forum_comment_name);
            holder.tv_post = (TextView) convertView.findViewById(R.id.tv_item_forum_comment_post);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_item_forum_comment_time);
            holder.tv_grade = (TextView) convertView.findViewById(R.id.tv_item_forum_comment_grade);
            holder.tv_level = (TextView) convertView.findViewById(R.id.tv_item_forum_comment_level);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_item_forum_comment_content);
            holder.tv_time_type = (TextView) convertView.findViewById(R.id.tv_item_forum_comment_time_type);
            holder.tv_like = (TextView) convertView.findViewById(R.id.tv_item_forum_comment_like);
            holder.tv_replay = (TextView) convertView.findViewById(R.id.tv_item_forum_comment_replay);
            holder.tv_report = (TextView) convertView.findViewById(R.id.tv_item_forum_comment_report);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_item_forum_comment_delete);
            holder.ll_child = (MyListview) convertView.findViewById(R.id.ll_item_forum_comment_child);
            convertView.setTag(holder);
        } else {
            holder = (ViewHodler) convertView.getTag();
        }

        try {
            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + commentBean.getUserPhoto(), holder.iv_head, options);
            holder.tv_name.setText(commentBean.getUserName());
            LogUtils.e(TAG,"commentCount"+commentCount);
            if (commentBeanArrayList != null && commentBeanArrayList.size() > 0) {
                if (commentCount==0){
                    holder.tv_level.setText(commentBeanArrayList.size() - position + "楼");
                }else{
                    holder.tv_level.setText(commentCount - position + "楼");
                }

            }
            String post = commentBean.getAuthType();
            if (post == null || post.length() <= 0) {
                holder.tv_post.setVisibility(View.GONE);
            } else if (post.equals("在校生") || post.equals("应届生")) {
                holder.tv_post.setVisibility(View.VISIBLE);
                holder.tv_post.setText(post);
                holder.tv_post.setBackgroundResource(R.drawable.bg_post_green);
            } else if (post.equals("护士长") || post.equals("主任")) {
                holder.tv_post.setVisibility(View.VISIBLE);
                holder.tv_post.setText(post);
                holder.tv_post.setBackgroundResource(R.drawable.bg_post_green);
            } else if (post.equals("护士") || post.equals("护师") || post.equals("主管护师")) {
                holder.tv_post.setVisibility(View.VISIBLE);
                holder.tv_post.setText(post);
                holder.tv_post.setBackgroundResource(R.drawable.bg_post_green);
            } else {
                holder.tv_post.setVisibility(View.GONE);
            }
            holder.tv_time.setText(TimeToolUtils.fromateTimeShowByRule(Long.valueOf(commentBean.getAddTime()) * 1000));
            holder.tv_grade.setText("Lv." + commentBean.getUserLevel());
            holder.tv_content.setText(commentBean.getContent());
            holder.iv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentHomePage = new Intent();
                    ForumDataBean forumBean = new ForumDataBean();
                    forumBean.setUserId(commentBean.getUserId());
                    forumBean.setUserLevel(commentBean.getUserLevel());
                    forumBean.setUserPhoto(commentBean.getUserPhoto());
                    intentHomePage.setClass(mContext, PersonalHomePageActivity.class);
                    intentHomePage.putExtra("forum", forumBean);
                    intentHomePage.putExtra("from", "ForumCommentAdapter");
                    mContext.startActivity(intentHomePage);
                }
            });
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            long time = Long.parseLong(commentBean.getAddTime());
            String timeone = simpleDateFormat.format(new Date(time * 1000));
            holder.tv_time_type.setText(timeone + "");
            commentChildBeanArrayList = (ArrayList<CommentChildBean>) commentBean.getChildComments();
            childAdapter = new ForumCommentChildAdapter(commentChildBeanArrayList, mContext);
            holder.ll_child.setAdapter(childAdapter);
            setListViewHeightBasedOnChildren(holder.ll_child);
            holder.ll_child.setEnabled(false);
            holder.ll_child.setPressed(false);
            holder.ll_child.setClickable(false);
            if (isCanDelete) {
                holder.tv_delete.setVisibility(View.VISIBLE);
            } else {
                holder.tv_delete.setVisibility(View.INVISIBLE);
            }
            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = Message.obtain();
                    msg.what = 114;
                    msg.obj = position;
                    handler.sendMessage(msg);
                }
            });
            holder.tv_replay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = Message.obtain();
                    msg.what = 112;
                    msg.obj = position;
                    handler.sendMessage(msg);
                }
            });
            if (userBean.getUserid().equals(commentBean.getUserId())) {
                holder.tv_report.setVisibility(View.INVISIBLE);
            } else {
                holder.tv_report.setVisibility(View.VISIBLE);
            }
            holder.tv_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = Message.obtain();
                    msg.what = 113;
                    msg.obj = position;
                    handler.sendMessage(msg);
                }
            });
            if (commentBean.getAddLike().equals("1")) {//已点赞
                holder.iv_like.setImageResource(R.mipmap.ic_forum_like_purple);
            } else {//未点赞
                holder.iv_like.setImageResource(R.mipmap.ic_forum_like_gray);
            }
            holder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userBean.getUserid() != null && userBean.getUserid().length() > 0) {
                        if (commentBean.getAddLike().equals("1")) {//已点赞
                            resetLikeForForum(commentBean, position);
                        } else {//未点赞
                            setLikeForForum(commentBean, position);
                        }
                    } else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                    }

                }
            });
        } catch (Exception e) {

        }
        return convertView;
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

    class ViewHodler {
        private RoudImage iv_head;
        private TextView tv_name, tv_post, tv_time, tv_grade, tv_level, tv_content, tv_time_type, tv_like, tv_replay, tv_report, tv_delete;
        private MyListview ll_child;
        private ArrayAdapter<String> commentsadapter;
        private ImageView iv_like, iv_authentication;
    }

    /**
     * 点赞
     *
     * @param commentBean
     * @param position
     */
    private void setLikeForForum(CommentBean commentBean, final int position) {
        //入参：userid,id,type(新闻资讯为1，2圈子,3评论)
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userBean.getUserid());
        requestParams.put("id", commentBean.getId());
        requestParams.put("type", "3");
        NurseAsyncHttpClient.get(CommunityNetConstant.SET_LIKE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--setLikeForForum->" + response);
//                    {
//                    "status":"success",
//                            "data":{
//                        "score":"2",
//                                "event":"点赞",
//                                "create_time":1483975604,
//                                "userid":"17026"
//                    }
//                }
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        Message msg = Message.obtain();
                        msg.what = 110;
                        msg.obj = position;
                        handler.sendMessage(msg);
                        JSONObject json=response.getJSONObject("data");
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
     *
     * @param commentBean
     * @param position
     */
    private void resetLikeForForum(CommentBean commentBean, final int position) {
        //入参：userid,id,type
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userBean.getUserid());
        requestParams.put("id", commentBean.getId());
        requestParams.put("type", "3");
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
                            if (status.equals("success")) {
                                Message msg = Message.obtain();
                                msg.what = 111;
                                msg.obj = position;
                                handler.sendMessage(msg);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }
}
