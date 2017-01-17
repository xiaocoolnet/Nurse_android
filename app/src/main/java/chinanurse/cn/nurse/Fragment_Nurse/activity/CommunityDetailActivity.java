package chinanurse.cn.nurse.Fragment_Nurse.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chinanurse.cn.nurse.Fragment_Nurse.adapter.CommunityDetailRecommendAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.CommunityDetailsAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.Fragment_Nurse.view.MyTextViewButton;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2016/12/30.
 * 护士站--圈子---发现—-圈子列表--圈子详情
 */

public class CommunityDetailActivity extends Activity implements View.OnClickListener {
    private String TAG = "CommunityDetailActivity";
    private final int INTENT_COMMUNITY_PARTICULARS = 5;
    private final int INTENT_FORUM_DETAILS = 6;
    private final int INTENT_PUBLISH_FORUM = 7;
    private UserBean user;
    private RelativeLayout rl_back;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private TextView shuaxin_button;
    private RelativeLayout ril_shibai, ril_list;
    private ProgressDialog dialogpgd;
    private PullToRefreshListView pulllist;
    private TextView detail_loading;
    private CommunityDetailsAdapter adapter;
    private Context mContext;
    private View viewH;
    private LinearLayout ll_head,ll_recommend;;
    private TextView tv_community_name, tv_people, tv_forum;
    private ImageView iv_community_photo;
    private MyTextViewButton tv_join;
    private ImageButton bt_edit;
    private CommunityBean communityBean;
    private ListView lv_recommend;
    private CommunityDetailRecommendAdapter recommendAdapter;
    private int pager = 1;
    private ArrayList<ForumDataBean> forumList;
    private ForumBean forumBean;
    private ArrayList<ForumDataBean> forumRecommendList;
    private ForumBean forumRecommendBean;
    private Gson gson;
    private String from = "";
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_bg_nor).showImageOnFail(R.mipmap.img_bg_nor).cacheInMemory(true).cacheOnDisc(true).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_community_details);
        mContext = this;
        user = new UserBean(mContext);
        from = getIntent().getStringExtra("from");
        LogUtils.e(TAG, "from-->" + from);
        LogUtils.e(TAG, "community-->" + getIntent().getSerializableExtra("community").toString());
        communityBean = (CommunityBean) getIntent().getSerializableExtra("community");
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_fragment_community_details_back);
        rl_back.setOnClickListener(this);
        shuaxin_button = (TextView) findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        bt_edit = (ImageButton) findViewById(R.id.imagebutton_bi);
        bt_edit.setOnClickListener(this);
        dialogpgd = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        detail_loading = (TextView) findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView) findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(true);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pager = 1;
                getData(pager);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (forumList.size() % 20 != 0) {
                    stopRefresh();
                    return;
                }
                pager = pager + 1;
                getData(pager);
            }

        });
        //获取当前时间
        setLastData();
        lv_view = pulllist.getRefreshableView();
        lv_view.setDivider(null);
        viewH = LayoutInflater.from(mContext).inflate(R.layout.fragment_community_details_head, null);
        ll_head = (LinearLayout) viewH.findViewById(R.id.ll_community_details_head);
        ll_head.setOnClickListener(this);
        iv_community_photo = (ImageView) viewH.findViewById(R.id.iv_community_details_head_photo);
        tv_community_name = (TextView) viewH.findViewById(R.id.tv_community_details_head_name);
        tv_people = (TextView) viewH.findViewById(R.id.tv_community_details_head_people);
        tv_forum = (TextView) viewH.findViewById(R.id.tv_community_details_head_forum);
        tv_join = (MyTextViewButton) viewH.findViewById(R.id.tv_community_details_head_join);
        tv_join.setOnClickListener(this);
        lv_recommend= (ListView) viewH.findViewById(R.id.lv_forum_recommend);
        ll_recommend= (LinearLayout) viewH.findViewById(R.id.ll_community_details_head_recommend);
        forumRecommendList=new ArrayList<ForumDataBean>();
        recommendAdapter=new CommunityDetailRecommendAdapter(mContext,forumRecommendList);
        lv_recommend.setAdapter(recommendAdapter);
        lv_view.addHeaderView(viewH);
        tv_community_name.setText(communityBean.getName());
        imageLoader.displayImage(CommunityNetConstant.NET_IMAGE_HOST + communityBean.getPhoto(), iv_community_photo, options);
        if (from.equals("CommunityListActivity") || from.equals("FindFragment") || from.equals("MeFragment")) {
            tv_people.setText(communityBean.getPerson_count() + "人");
            tv_forum.setText(communityBean.getForum_count() + "帖子");
            final String isJoin = communityBean.getJoin();
            if (isJoin.equals("1")) {
                tv_join.setBackgroundResource(R.drawable.bg_join_ready);
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_join_ready);
                // 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_join.setCompoundDrawables(drawable, null, null, null);
                tv_join.setText("已加入");
                tv_join.setTextColor(getResources().getColor(R.color.whilte));
            } else {
                tv_join.setBackgroundResource(R.drawable.bg_join);
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_join);
                // 这一步必须要做,否则不会显示.
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_join.setCompoundDrawables(drawable, null, null, null);
                tv_join.setText("加入");
                tv_join.setTextColor(getResources().getColor(R.color.purple));
            }
        }
        forumList = new ArrayList<ForumDataBean>();
        adapter = new CommunityDetailsAdapter(mContext, forumList);
        lv_view.setAdapter(adapter);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 1) {
                    Intent intentForumDetails = new Intent();
                    intentForumDetails.setClass(mContext, ForumDetailsActivity.class);
                    intentForumDetails.putExtra("forum", forumList.get(position - 1));
                    intentForumDetails.putExtra("from", "CommunityDetailActivity");
                    startActivityForResult(intentForumDetails, INTENT_FORUM_DETAILS);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCommunityInfo();
        getData(pager);
    }

    private void getCommunityInfo() {
//        入参：userid,cid 圈子id
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("cid", communityBean.getId());
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_COMMUNITY_INFO, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--getCommunityInfo-onSuccess>" + response);
                if (response != null) {
                    LogUtils.e(TAG, "getCommunityInfo" + response.toString());
//                    {
//                        "status":"success",
//                            "data":{
//                        "id":"14",
//                                "community_name":"灌水吐槽",
//                                "photo":"20161227/5861c4b3d72dd.png",
//                                "description":"灌水吐槽",
//                                "best":"0",
//                                "create_time":"1482802358",
//                                "hot":"0",
//                                "term_id":"282",
//                                "term_name":"交流",
//                                "f_count":"6",
//                                "person_num":"3",
//                                "join":"1"
//                    }
//                    }

                    try {
                        if ("success".equals(response.optString("status"))) {
                            JSONObject data = response.getJSONObject("data");
                            gson = new Gson();
                            communityBean = gson.fromJson(data.toString(), CommunityBean.class);
                            tv_community_name.setText(communityBean.getName());
                            imageLoader.displayImage(CommunityNetConstant.NET_IMAGE_HOST + communityBean.getPhoto(), iv_community_photo, options);
                            tv_people.setText(communityBean.getPerson_count() + "人");
                            tv_forum.setText(communityBean.getForum_count() + "帖子");
                            final String isJoin = communityBean.getJoin();
                            if (isJoin.equals("1")) {
                                tv_join.setBackgroundResource(R.drawable.bg_join_ready);
                                Drawable drawable = getResources().getDrawable(R.mipmap.ic_join_ready);
                                // 这一步必须要做,否则不会显示.
                                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                tv_join.setCompoundDrawables(drawable, null, null, null);
                                tv_join.setText("已加入");
                                tv_join.setTextColor(getResources().getColor(R.color.whilte));
                            } else {
                                tv_join.setBackgroundResource(R.drawable.bg_join);
                                Drawable drawable = getResources().getDrawable(R.mipmap.ic_join);
                                // 这一步必须要做,否则不会显示.
                                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                tv_join.setCompoundDrawables(drawable, null, null, null);
                                tv_join.setText("加入");
                                tv_join.setTextColor(getResources().getColor(R.color.purple));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, "--getCommunityInfo-onFailure>" + responseString);
            }
        });
    }

    private void getData(final int page) {
        if (HttpConnect.isConnnected(mContext)) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            getForumListByPager(page);
            if (page==1){
                getRecommendForumListByPager();
            }
        } else {
            ToastUtils.ToastShort(mContext,getResources().getString(R.string.net_erroy));
            ril_shibai.setVisibility(View.VISIBLE);
            ril_list.setVisibility(View.GONE);
            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager = 1;
                    getData(pager);
                }
            });
        }
    }

    private void getRecommendForumListByPager() {
        //        cid(圈子的id),userid,isbest(选精1,不0),istop(置顶1,不0),pager 分页
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("cid", communityBean.getId());
        requestParams.put("isbest", 1);
        requestParams.put("istop", 1);
        NurseAsyncHttpClient.get(CommunityNetConstant.FORUM_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getForumRecommendListByPager->" + response);
                    ril_shibai.setVisibility(View.GONE);
                    ril_list.setVisibility(View.VISIBLE);
                    forumRecommendList.clear();
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        forumRecommendBean = gson.fromJson(response.toString(), ForumBean.class);
                        forumRecommendList.addAll(forumRecommendBean.getData());

                        if (recommendAdapter == null) {
                            recommendAdapter = new CommunityDetailRecommendAdapter(mContext, forumRecommendList);
                            lv_recommend.setAdapter(adapter);
                        }
                        recommendAdapter.notifyDataSetChanged();
                        if (forumRecommendList.size()==0){
                            ll_recommend.setVisibility(View.GONE);
                        }else {
                            ll_recommend.setVisibility(View.VISIBLE);
                        }
                        stopRefresh();
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
                            pager = 1;
                            getData(pager);
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, "--getRecommendForumListByPager-onFailure>" + responseString);
                dialogpgd.dismiss();
                ToastUtils.ToastShort(mContext,getString(R.string.net_erroy));
                ril_shibai.setVisibility(View.VISIBLE);
                ril_list.setVisibility(View.GONE);
                shuaxin_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pager = 1;
                        getData(pager);
                    }
                });
            }
        });
    }

    private void getForumListByPager(int page) {
//        cid(圈子的id),userid,isbest(选精1,不0),istop(置顶1,不0),pager 分页
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("cid", communityBean.getId());
        requestParams.put("isbest", 0);
        requestParams.put("istop", 0);
        requestParams.put("pager", page);
        NurseAsyncHttpClient.get(CommunityNetConstant.FORUM_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getForumListByPager->" + response);
                    ril_shibai.setVisibility(View.GONE);
                    ril_list.setVisibility(View.VISIBLE);
                    if (pager == 1) {
                        forumList.clear();
                    }
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        forumBean = gson.fromJson(response.toString(), ForumBean.class);
                        forumList.addAll(forumBean.getData());

                        if (adapter == null) {
                            adapter = new CommunityDetailsAdapter(mContext, forumList);
                            lv_view.setAdapter(adapter);
                        }
                        adapter.notifyDataSetChanged();
                        stopRefresh();
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
                            pager = 1;
                            getData(pager);
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, "--getForumListByPager-onFailure>" + responseString);
                dialogpgd.dismiss();
                ToastUtils.ToastShort(mContext,getResources().getString(R.string.net_erroy));
                ril_shibai.setVisibility(View.VISIBLE);
                ril_list.setVisibility(View.GONE);
                shuaxin_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pager = 1;
                        getData(pager);
                    }
                });
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

    private String formatdatatime(long time) {
        if (0 == time) {
            return "";
        }
        return mdata.format(new Date(time));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fragment_community_details_back:
                finish();
                break;
            case R.id.ll_community_details_head:
                Intent intentForumDetails = new Intent();
                intentForumDetails.setClass(mContext, CommunityParticularsActivity.class);
                intentForumDetails.putExtra("community", communityBean);
                intentForumDetails.putExtra("from", "CommunityDetailActivity");
                startActivityForResult(intentForumDetails, INTENT_COMMUNITY_PARTICULARS);
                break;
            case R.id.tv_community_details_head_join:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    if (communityBean.getJoin().equals("1")) {
                        ToastUtils.ToastShort(mContext, "你已经加入该圈子！");
                    } else {
                        joinCommunity();
                    }
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.imagebutton_bi:
                Intent intentPublish = new Intent();
                intentPublish.putExtra("from", "CommunityDetailActivity");
                intentPublish.putExtra("communityBean", communityBean);
                intentPublish.setClass(mContext, PublishForumActivity.class);
                startActivityForResult(intentPublish, INTENT_PUBLISH_FORUM);
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
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_COMMUNITY_PARTICULARS:
                break;
            case INTENT_FORUM_DETAILS:
                break;
            case INTENT_PUBLISH_FORUM:
                break;
        }

    }
}
