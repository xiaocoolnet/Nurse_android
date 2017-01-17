package chinanurse.cn.nurse.Fragment_Nurse.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityMasterBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.Fragment_Nurse.view.CollapsibleTextView;
import chinanurse.cn.nurse.Fragment_Nurse.view.MyTextViewButton;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2016/12/30.
 * 护士站--圈子---发现—-圈子列表--圈子详情--圈子详细情况
 */

public class CommunityParticularsActivity extends Activity implements View.OnClickListener {
    private String TAG = "CommunityParticularsFragment";
    private final int INTENT_TOP_FORUM_LIST = 8;
    private final int INTENT_BEST_FORUM_LIST = 9;
    private final int INTENT_APPLY_COMMUNITY = 10;
    private UserBean user;
    private Context mContext;
    private RelativeLayout rl_back, rl_top_forum, rl_best_forum, rl_apply_community, rl_cancel_focus;
    private ImageView iv_community_photo;
    private TextView tv_community_name, tv_community_people, tv_community_forum;
    private TextView tv_status;
    private MyTextViewButton tv_join;
    private CommunityBean communityBean;
    private CollapsibleTextView tv_description;
    private ImageView iv_one, iv_two, iv_three;
    private TextView tv_one, tv_two, tv_three;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_bg_nor).showImageOnFail(R.mipmap.img_bg_nor).cacheInMemory(true).cacheOnDisc(true).build();
    private ArrayList<CommunityMasterBean.DataBean> communityMasterList;
    private CommunityMasterBean communityMasterBean;
    private Gson gson;
    private String status = "";
    private AlertDialog apply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_community_particulars);
        mContext = this;
        user = new UserBean(mContext);
        communityBean = (CommunityBean) getIntent().getSerializableExtra("community");
        inintView();
    }

    private void inintView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_fragment_community_particulars_back);
        rl_back.setOnClickListener(this);
        iv_community_photo = (ImageView) findViewById(R.id.iv_fragment_community_particulars_photo);
        tv_community_name = (TextView) findViewById(R.id.tv_fragment_community_particulars_name);
        tv_community_people = (TextView) findViewById(R.id.tv_fragment_community_particulars_people);
        tv_community_forum = (TextView) findViewById(R.id.tv_fragment_community_particulars_forum);
        rl_top_forum = (RelativeLayout) findViewById(R.id.rl_fragment_community_particulars_top);
        tv_join = (MyTextViewButton) findViewById(R.id.tv_fragment_community_particulars_join);
        tv_join.setOnClickListener(this);
        rl_top_forum.setOnClickListener(this);
        rl_best_forum = (RelativeLayout) findViewById(R.id.rl_fragment_community_particulars_best);
        rl_best_forum.setOnClickListener(this);
        rl_apply_community = (RelativeLayout) findViewById(R.id.rl_fragment_community_particulars_apply_community);
        rl_apply_community.setOnClickListener(this);
        tv_status= (TextView) findViewById(R.id.tv_status);
        rl_cancel_focus = (RelativeLayout) findViewById(R.id.rl_fragment_community_particulars_cancel_focus);
        rl_cancel_focus.setOnClickListener(this);
        tv_description = (CollapsibleTextView) findViewById(R.id.tv_fragment_community_particulars_description);
        tv_one = (TextView) findViewById(R.id.tv_fragment_community_particulars_one);
        tv_two = (TextView) findViewById(R.id.tv_fragment_community_particulars_two);
        tv_three = (TextView) findViewById(R.id.tv_fragment_community_particulars_three);
        iv_one = (ImageView) findViewById(R.id.iv_fragment_community_particulars_one);
        iv_two = (ImageView) findViewById(R.id.iv_fragment_community_particulars_two);
        iv_three = (ImageView) findViewById(R.id.iv_fragment_community_particulars_three);
        tv_community_name.setText(communityBean.getName());
        imageLoader.displayImage(CommunityNetConstant.NET_IMAGE_HOST + communityBean.getPhoto(), iv_community_photo, options);
        tv_community_people.setText(communityBean.getPerson_count() + "人");
        tv_community_forum.setText(communityBean.getForum_count() + "帖子");
        final String isJoin = communityBean.getJoin();
        if (isJoin!=null&&isJoin.equals("1")) {
            tv_join.setBackgroundResource(R.drawable.bg_join_ready);
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_join_ready);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_join.setCompoundDrawables(drawable, null, null, null);
            tv_join.setText("已加入");
            tv_join.setTextColor(getResources().getColor(R.color.whilte));
            rl_cancel_focus.setVisibility(View.VISIBLE);
            tv_join.setClickable(false);
        } else {
            tv_join.setBackgroundResource(R.drawable.bg_join);
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_join);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_join.setCompoundDrawables(drawable, null, null, null);
            tv_join.setText("加入");
            tv_join.setTextColor(getResources().getColor(R.color.purple));
            rl_cancel_focus.setVisibility(View.GONE);
            tv_join.setClickable(true);
        }
        tv_description.setDesc(communityBean.getDescription(), tv_description, TextView.BufferType.NORMAL);
        getCommunityMaster();

    }

    @Override
    protected void onResume() {
        super.onResume();
        judgeApplyCommunity();
    }

    private void judgeApplyCommunity() {
//      入参：userid,cid(圈子id)
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
//        requestParams.put("cid", communityBean.getId());
        NurseAsyncHttpClient.get(CommunityNetConstant.JUDGE_APPLY_COMMUNITY, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--judgeApplyCommunity->" + response.toString());
//                    {
//                        "status":"success",
//                            "data":"no"
//                    }
                    try {
                        if ("success".equals(response.optString("status"))) {
                            status = response.getString("data");
                            if (status.equals("yes")) {

                            } else if (status.equals("no")) {
                                getApplyCommunityStatus();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void getApplyCommunityStatus() {
        //入参：userid,cid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("cid", communityBean.getId());
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_A_C_STATUS, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {//申请状态 2拒绝，1通过,0正在审核,not found未审核
                    LogUtils.e(TAG, "--getApplyCommunityStatus->" + response.toString());
//                    {
//                        "status":"success",
//                            "data":"not found"
//                    }
                    try {
                        if ("success".equals(response.optString("status"))) {
                            status = response.getString("data");
                            if (status.equals("yes")){//已经是圈主
                                tv_status.setVisibility(View.VISIBLE);
                                tv_status.setText("已经是圈主");

                            }else if (status.equals("0")){//正在审核
                                tv_status.setVisibility(View.VISIBLE);
                                tv_status.setText("正在审核");

                            } else if (status.equals("1")) {//通过
                                tv_status.setVisibility(View.VISIBLE);
                                tv_status.setText("审核通过");

                            } else if (status.equals("2")){//拒绝
                                tv_status.setVisibility(View.VISIBLE);
                                tv_status.setText("审核拒绝");
                            }else if (status.equals("not found")){//未审核
                                tv_status.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void getCommunityMaster() {
        //userid,cid(圈子id),pager 分页
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("cid", communityBean.getId());
        requestParams.put("pager", 1);
        NurseAsyncHttpClient.get(CommunityNetConstant.MASTER_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    communityMasterList = new ArrayList<CommunityMasterBean.DataBean>();
                    LogUtils.e(TAG, "--getCommunityListByPager->" + response);
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        communityMasterBean = gson.fromJson(response.toString(), CommunityMasterBean.class);
                        communityMasterList.addAll(communityMasterBean.getData());
                        if (communityMasterList.size() == 0) {
                            tv_one.setText("还有空位哦～");
                            tv_two.setText("还有空位哦～");
                            tv_three.setText("还有空位哦～");
                        } else if (communityMasterList.size() == 1) {
                            imageLoader.displayImage(CommunityNetConstant.NET_IMAGE_HOST + communityMasterList.get(0).getUserPhoto(), iv_one, options);
                            tv_one.setText(communityMasterList.get(0).getUserName());
                            tv_two.setText("还有空位哦～");
                            tv_three.setText("还有空位哦～");
                        } else if (communityMasterList.size() == 2) {
                            imageLoader.displayImage(CommunityNetConstant.NET_IMAGE_HOST + communityMasterList.get(0).getUserPhoto(), iv_one, options);
                            imageLoader.displayImage(CommunityNetConstant.NET_IMAGE_HOST + communityMasterList.get(1).getUserPhoto(), iv_two, options);
                            tv_one.setText(communityMasterList.get(0).getUserName());
                            tv_two.setText(communityMasterList.get(1).getUserName());
                            tv_three.setText("还有空位哦～");
                        } else if (communityMasterList.size() >= 3) {
                            imageLoader.displayImage(CommunityNetConstant.NET_IMAGE_HOST + communityMasterList.get(0).getUserPhoto(), iv_one, options);
                            imageLoader.displayImage(CommunityNetConstant.NET_IMAGE_HOST + communityMasterList.get(1).getUserPhoto(), iv_two, options);
                            imageLoader.displayImage(CommunityNetConstant.NET_IMAGE_HOST + communityMasterList.get(2).getUserPhoto(), iv_three, options);
                            tv_one.setText(communityMasterList.get(0).getUserName());
                            tv_two.setText(communityMasterList.get(1).getUserName());
                            tv_three.setText(communityMasterList.get(2).getUserName());
                        }

                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fragment_community_particulars_back:
                finish();
                break;
            case R.id.tv_fragment_community_particulars_join:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    joinCommunity();
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_fragment_community_particulars_top:
                Intent intentTop = new Intent();
                intentTop.setClass(mContext, TopOrBestActivity.class);
                intentTop.putExtra("type", "top");
                intentTop.putExtra("community", communityBean);
                startActivityForResult(intentTop, INTENT_TOP_FORUM_LIST);
                break;
            case R.id.rl_fragment_community_particulars_best:
                Intent intentBest = new Intent();
                intentBest.setClass(mContext, TopOrBestActivity.class);
                intentBest.putExtra("type", "best");
                intentBest.putExtra("community", communityBean);
                startActivityForResult(intentBest, INTENT_BEST_FORUM_LIST);
                break;
            case R.id.rl_fragment_community_particulars_apply_community:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    if (status.equals("yes")){//已经是圈主
                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.dialog_apply, (ViewGroup)findViewById(R.id.ll_dialog_apply));
                        final TextView tv_auth_status = (TextView) view.findViewById(R.id.tv_dialog_status);
                        final TextView tv_auth_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                        final TextView tv_auth_again = (TextView) view.findViewById(R.id.tv_dialog_again);
                        tv_auth_status.setText("您已经是圈主");
                        tv_auth_cancel.setText("取消");
                        tv_auth_again.setText("重新申请");
                        tv_auth_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                apply.dismiss();
                            }
                        });
                        tv_auth_again.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intentApply = new Intent();
                                intentApply.setClass(mContext, ApplyCommunityActivity.class);
                                intentApply.putExtra("community", communityBean);
                                startActivityForResult(intentApply, INTENT_APPLY_COMMUNITY);
                                apply.dismiss();
                            }
                        });
                        apply = new AlertDialog.Builder(mContext).setView(view).show();

                    }else if (status.equals("0")){//正在审核
                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.dialog_apply, (ViewGroup)findViewById(R.id.ll_dialog_apply));
                        final TextView tv_auth_status = (TextView) view.findViewById(R.id.tv_dialog_status);
                        final TextView tv_auth_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                        final TextView tv_auth_again = (TextView) view.findViewById(R.id.tv_dialog_again);
                        tv_auth_status.setText("正在审核");
                        tv_auth_cancel.setText("取消");
                        tv_auth_again.setText("重新申请");
                        tv_auth_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                apply.dismiss();
                            }
                        });
                        tv_auth_again.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intentApply = new Intent();
                                intentApply.setClass(mContext, ApplyCommunityActivity.class);
                                intentApply.putExtra("community", communityBean);
                                startActivityForResult(intentApply, INTENT_APPLY_COMMUNITY);
                                apply.dismiss();
                            }
                        });
                        apply = new AlertDialog.Builder(mContext).setView(view).show();

                    } else if (status.equals("1")) {//通过
                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.dialog_apply, (ViewGroup)findViewById(R.id.ll_dialog_apply));
                        final TextView tv_auth_status = (TextView) view.findViewById(R.id.tv_dialog_status);
                        final TextView tv_auth_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                        final TextView tv_auth_again = (TextView) view.findViewById(R.id.tv_dialog_again);
                        tv_auth_status.setText("审核通过");
                        tv_auth_cancel.setText("取消");
                        tv_auth_again.setText("重新申请");
                        tv_auth_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                apply.dismiss();
                            }
                        });
                        tv_auth_again.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intentApply = new Intent();
                                intentApply.setClass(mContext, ApplyCommunityActivity.class);
                                intentApply.putExtra("community", communityBean);
                                startActivityForResult(intentApply, INTENT_APPLY_COMMUNITY);
                                apply.dismiss();
                            }
                        });
                        apply = new AlertDialog.Builder(mContext).setView(view).show();

                    } else if (status.equals("2")){//拒绝
                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.dialog_apply, (ViewGroup)findViewById(R.id.ll_dialog_apply));
                        final TextView tv_auth_status = (TextView) view.findViewById(R.id.tv_dialog_status);
                        final TextView tv_auth_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                        final TextView tv_auth_again = (TextView) view.findViewById(R.id.tv_dialog_again);
                        tv_auth_status.setText("审核拒绝");
                        tv_auth_cancel.setText("取消");
                        tv_auth_again.setText("重新申请");
                        tv_auth_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                apply.dismiss();
                            }
                        });
                        tv_auth_again.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intentApply = new Intent();
                                intentApply.setClass(mContext, ApplyCommunityActivity.class);
                                intentApply.putExtra("community", communityBean);
                                startActivityForResult(intentApply, INTENT_APPLY_COMMUNITY);
                                apply.dismiss();
                            }
                        });
                        apply = new AlertDialog.Builder(mContext).setView(view).show();
                    }else if (status.equals("not found")){//未审核
                        Intent intentApply = new Intent();
                        intentApply.setClass(mContext, ApplyCommunityActivity.class);
                        intentApply.putExtra("community", communityBean);
                        startActivityForResult(intentApply, INTENT_APPLY_COMMUNITY);
                    }
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_fragment_community_particulars_cancel_focus:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    cancalJoinCommunity();
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }

    private void joinCommunity() {
//        userid,cid
        final RequestParams r = new RequestParams();
        r.put("userid", user.getUserid());
        r.put("cid", communityBean.getId());
        NurseAsyncHttpClient.get(CommunityNetConstant.ADD_COMMUNITY, r, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    communityBean.setJoin("1");
                    tv_join.setBackgroundResource(R.drawable.bg_join_ready);
                    Drawable drawable = getResources().getDrawable(R.mipmap.ic_join_ready);
                    // 这一步必须要做,否则不会显示.
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tv_join.setCompoundDrawables(drawable, null, null, null);
                    tv_join.setText("已加入");
                    tv_join.setTextColor(getResources().getColor(R.color.whilte));
                    rl_cancel_focus.setVisibility(View.VISIBLE);
                    tv_join.setClickable(false);
                }
            }
        });

    }

    private void cancalJoinCommunity() {
//        userid,cid
        final RequestParams r = new RequestParams();
        r.put("userid", user.getUserid());
        r.put("cid", communityBean.getId());
        NurseAsyncHttpClient.get(CommunityNetConstant.DEL_JOIN_COMMUNITY, r, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    communityBean.setJoin("0");
                    tv_join.setBackgroundResource(R.drawable.bg_join);
                    Drawable drawable = getResources().getDrawable(R.mipmap.ic_join);
                    // 这一步必须要做,否则不会显示.
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tv_join.setCompoundDrawables(drawable, null, null, null);
                    tv_join.setText("加入");
                    tv_join.setTextColor(getResources().getColor(R.color.purple));
                    rl_cancel_focus.setVisibility(View.GONE);
                    tv_join.setClickable(true);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_TOP_FORUM_LIST:
                break;
            case INTENT_BEST_FORUM_LIST:
                break;
            case INTENT_APPLY_COMMUNITY:
                switch (resultCode){
                    case 100:
                        getApplyCommunityStatus();
                        break;
                }
                break;
        }

    }

}
