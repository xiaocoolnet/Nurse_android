package chinanurse.cn.nurse.Fragment_Nurse.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
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

import chinanurse.cn.nurse.Fragment_Nurse.adapter.MeFragmentAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.PersonalHomePageAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.PersonalHomePageRecyclerViewAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityNetBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.picture.RoudImage;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2016/12/30.
 * 个人主页
 */

public class PersonalHomePageActivity extends Activity implements View.OnClickListener {
    private String TAG = "PersonalHomePageActivity";
    private final int INTENT_FORUM_DETAILS = 18;
    private final int INTENT_COMMUNITY_DETAILS = 19;
    private Context mContext;
    private UserBean user;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View viewC = null;
    private View viewR = null;
    private RoudImage iv_head;
    private ImageView iv_authentication, iv_gender;
    private TextView tv_name, tv_level, tv_fans, tv_attention, tv_focus;
    private RelativeLayout rl_focus;
    private RecyclerView mRecyclerView;
    private TextView tv_community;

    private RelativeLayout rl_back;
    private TextView tv_title;
    private ListView lv_view;
    private TextView shuaxin_button;
    private RelativeLayout ril_shibai, ril_list;
    private ProgressDialog dialogpgd;
    private PullToRefreshListView pulllist;
    private TextView detail_loading;
    private PersonalHomePageAdapter adapter;
    private PersonalHomePageRecyclerViewAdapter recyclerViewAdapter;
    private int pager = 1;
    private ArrayList<CommunityBean> communityList;
    private CommunityNetBean communityBean;
    private ArrayList<ForumDataBean> forumList;
    private ForumBean forumBean;
    private ForumDataBean forumBeanData = new ForumDataBean();
    private Gson gson;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_bg_nor).showImageOnFail(R.mipmap.img_bg_nor).cacheInMemory(true).cacheOnDisc(true).build();
    private boolean isFollow = false;
    private Dialog dialog;
//    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_homepage);
        mContext = this;
        user = new UserBean(mContext);
//        from = getIntent().getStringExtra("from");
        forumBeanData = (ForumDataBean) getIntent().getSerializableExtra("forum");
        LogUtils.e(TAG, "--传递-》" + forumBeanData.toString());
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_fragment_personal_homepage_back);
        rl_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_fragment_personal_homepage_title);
        shuaxin_button = (TextView) findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
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
        viewC = LayoutInflater.from(mContext).inflate(R.layout.fragment_personal_homepage_head_personal, null);
        iv_head = (RoudImage) viewC.findViewById(R.id.iv_head);
        iv_authentication = (ImageView) viewC.findViewById(R.id.iv_authentication);
        iv_gender = (ImageView) viewC.findViewById(R.id.iv_gender);
        tv_name = (TextView) viewC.findViewById(R.id.tv_name);
        tv_level = (TextView) viewC.findViewById(R.id.tv_level);
        tv_fans = (TextView) viewC.findViewById(R.id.tv_fans);
        tv_attention = (TextView) viewC.findViewById(R.id.tv_attention);
        tv_focus = (TextView) viewC.findViewById(R.id.tv_focus);
        rl_focus = (RelativeLayout) viewC.findViewById(R.id.rl_focus);
        rl_focus.setOnClickListener(this);
        viewR = LayoutInflater.from(mContext).inflate(R.layout.fragment_personal_homepage_head_community, null);
        mRecyclerView = (RecyclerView) viewR.findViewById(R.id.recycler_fragment_personal_homepage_head_community);
        tv_community = (TextView) viewR.findViewById(R.id.tv_fragment_personal_homepage_head_community);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        communityList = new ArrayList<CommunityBean>();
        recyclerViewAdapter = new PersonalHomePageRecyclerViewAdapter(mContext, communityList);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickLitener(new PersonalHomePageRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intentCommunityDetails = new Intent();
                intentCommunityDetails.setClass(mContext, CommunityDetailActivity.class);
                intentCommunityDetails.putExtra("community", communityList.get(position));
                intentCommunityDetails.putExtra("from", "PersonalHomePageActivity");
                startActivityForResult(intentCommunityDetails, INTENT_COMMUNITY_DETAILS);
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                Toast.makeText(getActivity(),"长按"+position,Toast.LENGTH_SHORT).show();
            }
        });
        lv_view.addHeaderView(viewC);
        lv_view.addHeaderView(viewR);
        forumList = new ArrayList<ForumDataBean>();
        adapter = new PersonalHomePageAdapter(mContext, forumList);
        lv_view.setAdapter(adapter);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 2) {
                    Intent intentForumDetails = new Intent();
                    intentForumDetails.setClass(mContext, ForumDetailsActivity.class);
                    intentForumDetails.putExtra("forum", forumList.get(position - 2));
                    intentForumDetails.putExtra("from", "PersonalHomePageActivity");
                    startActivityForResult(intentForumDetails, INTENT_FORUM_DETAILS);
                }

            }
        });
        imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + forumBeanData.getUserPhoto(), iv_head, options);
        tv_title.setText(forumBeanData.getUserName());
        tv_name.setText(forumBeanData.getUserName());
        tv_level.setText(forumBeanData.getUserLevel());
        tv_community.setText("他/她的圈子");
        getUserInfo();
        getData(pager);
        judgeFollowFans();
    }

    private void judgeFollowFans() {
//        入参：follow_id(被关注人id),fans_id(关注人id)
        RequestParams requestParams = new RequestParams();
        requestParams.put("follow_id", forumBeanData.getUserId());
        requestParams.put("fans_id", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.JUDGE_FOLLOW_FANS, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--judgeFollowFans->" + response);
//                    {
//                        "status": "success",
//                            "data": "0"
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            String data = response.getString("data");
                            if (data.equals("1")) {//1:已关注，0:未关注
                                isFollow = true;
                                Drawable drawable = getResources().getDrawable(R.mipmap.ic_join_ready);
                                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                tv_focus.setCompoundDrawables(drawable, null, null, null);
                                tv_focus.setText("已关注");
                            } else if (data.equals("0")) {
                                isFollow = false;
                                Drawable drawable = getResources().getDrawable(R.mipmap.ic_focus);
                                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                tv_focus.setCompoundDrawables(drawable, null, null, null);
                                tv_focus.setText("关注Ta");
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
    protected void onResume() {
        super.onResume();
        getFanNum();

    }

    private void getUserInfo() {
        //入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", forumBeanData.getUserId());
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_USER_INFO, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getUserInfo->" + response);
//                   {
//                    "status": "success",
//                            "data": {
//                        "id": "608",
//                                "name": "我叫箱子地理",
//                                "sex": "1",
//                                "level": "1",
//                                "score": "2711",
//                                "birthday": "2016-11-01",
//                                "realname": "张伟利",
//                                "address": "北京市-北京市-朝阳区",
//                                "phone": "18600885251",
//                                "city": "",
//                                "email": "755759622@qq.com",
//                                "qq": "",
//                                "weixin": "",
//                                "photo": "avatar6081480043199804.jpg",
//                                "school": "北京应用技术大学",
//                                "major": "心理学",
//                                "education": "本科",
//                                "time": "1467040253",
//                                "all_information": "1",
//                                "usertype": "1",
//                                "money": 0,
//                                "fanscount": "0",
//                                "followcount": "0"
//                    }
//                }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            JSONObject j = response.getJSONObject("data");
                            imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST + j.getString("photo"), iv_head, options);
                            tv_title.setText(j.getString("name"));
                            tv_name.setText(j.getString("name"));
                            tv_level.setText(j.getString("level"));
                            if (j.getString("sex").equals("1")) {
                                iv_gender.setImageResource(R.mipmap.ic_man);
                            } else {
                                iv_gender.setImageResource(R.mipmap.ic_woman);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void getFanNum() {
        //入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", forumBeanData.getUserId());
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

    private void getData(final int page) {
        if (HttpConnect.isConnnected(mContext)) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            getForumListByPagerFromOther(page);

//            if (from.equals("ForumCommentAdapter")) {
//            } else {
//                getForumListByPager(page);
//            }
            if (page == 1) {
                getCommunityListByPagerFromOther();

//                if (from.equals("ForumCommentAdapter")) {
//                } else {
//                    getCommunityListByPager(page);
//                }
            }
        } else {
            ToastUtils.ToastShort(mContext, getString(R.string.net_erroy));
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

    private void getCommunityListByPager(int page) {
//        入参：userid,term_id(父类的分类id,全部则传0),best(选精1,不0),hot(热门1,不0),pager 分页
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", forumBeanData.getUserId());
        requestParams.put("term_id", 0);
        requestParams.put("best", 0);
        requestParams.put("hot", 1);
        requestParams.put("pager", page);
        NurseAsyncHttpClient.get(CommunityNetConstant.COMMUNITY_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getCommunityListByPager->" + response);
                    ril_shibai.setVisibility(View.GONE);
                    ril_list.setVisibility(View.VISIBLE);
                    if (pager == 1) {
                        communityList.clear();
                    }
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        communityBean = gson.fromJson(response.toString(), CommunityNetBean.class);
                        communityList.addAll(communityBean.getData());
                        if (recyclerViewAdapter == null) {
                            recyclerViewAdapter = new PersonalHomePageRecyclerViewAdapter(mContext, communityList);
                            mRecyclerView.setAdapter(recyclerViewAdapter);
                        }
                        recyclerViewAdapter.notifyDataSetChanged();
                        stopRefresh();
                    } else {
//                            if ("end".equals(data)){
//                                stopRefresh();
//                            }
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
                dialogpgd.dismiss();
                ToastUtils.ToastShort(mContext, getString(R.string.net_erroy));
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

    /**
     * 获取他人加入的圈子
     */
    private void getCommunityListByPagerFromOther() {
//        入参：userid,term_id(父类的分类id,全部则传0),best(选精1,不0),hot(热门1,不0),pager 分页
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", forumBeanData.getUserId());
//        requestParams.put("pager", 1);
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_MY_COMMUNITY_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getCommunityListByPager->" + response);
                    ril_shibai.setVisibility(View.GONE);
                    ril_list.setVisibility(View.VISIBLE);
                    if (pager == 1) {
                        communityList.clear();
                    }
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        communityBean = gson.fromJson(response.toString(), CommunityNetBean.class);
                        communityList.addAll(communityBean.getData());
                        if (recyclerViewAdapter == null) {
                            recyclerViewAdapter = new PersonalHomePageRecyclerViewAdapter(mContext, communityList);
                            mRecyclerView.setAdapter(recyclerViewAdapter);
                        }
                        recyclerViewAdapter.notifyDataSetChanged();
                        stopRefresh();
                    } else {
//                            if ("end".equals(data)){
//                                stopRefresh();
//                            }
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
                dialogpgd.dismiss();
                ToastUtils.ToastShort(mContext, getString(R.string.net_erroy));
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
        requestParams.put("userid", forumBeanData.getUserId());
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
                            adapter = new PersonalHomePageAdapter(mContext, forumList);
                            lv_view.setAdapter(adapter);
                        }
                        adapter.notifyDataSetChanged();
                        stopRefresh();

                    } else {
//                            if ("end".equals(data)){
//                                stopRefresh();
//                            }
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
                dialogpgd.dismiss();
                ToastUtils.ToastShort(mContext, getString(R.string.net_erroy));
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

    private void getForumListByPagerFromOther(int page) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", forumBeanData.getUserId());
        requestParams.put("pager", page);
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_MY_FORUM_LIST, requestParams, new JsonHttpResponseHandler() {
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
                            adapter = new PersonalHomePageAdapter(mContext, forumList);
                            lv_view.setAdapter(adapter);
                        }
                        adapter.notifyDataSetChanged();
                        stopRefresh();

                    } else {
//                            if ("end".equals(data)){
//                                stopRefresh();
//                            }
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
                dialogpgd.dismiss();
                ToastUtils.ToastShort(mContext, getString(R.string.net_erroy));
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
            case R.id.rl_fragment_personal_homepage_back:
                finish();
                break;
            case R.id.rl_focus:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    if (isFollow) {
                        cancelFollow();
                    } else {
                        toFollow();
                    }
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void toFollow() {
//        入参：follow_id(被关注人id,别人的userid),fans_id(关注人id,我的userid)
        RequestParams requestParams = new RequestParams();
        requestParams.put("follow_id", forumBeanData.getUserId());
        requestParams.put("fans_id", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.ADD_FOLLOW_FANS, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--toFollow->" + response);

//                   {
//                    "status": "success",
//                            "data": {
//                        "follow_userid": "608",
//                                "fans_userid": "17026",
//                                "create_time": 1483954098,
//                                "status": "1",
//                                "score": "20",
//                                "event": "第一次关注"
//                    }
//                }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            getFanNum();
                            ToastUtils.ToastShort(mContext, "关注成功");
                            isFollow = true;
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_join_ready);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            tv_focus.setCompoundDrawables(drawable, null, null, null);
                            tv_focus.setText("已关注");
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
        });


    }

    private void cancelFollow() {
//        入参：follow_id(被关注人id,别人的userid),fans_id(关注人id,我的userid)
        RequestParams requestParams = new RequestParams();
        requestParams.put("follow_id", forumBeanData.getUserId());
        requestParams.put("fans_id", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.DEL_FOLLOW_FANS, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--cancelFollow->" + response);
//                    {
//                        "status": "success",
//                            "data": 1
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            getFanNum();
                            ToastUtils.ToastShort(mContext, "成功取消关注");
                            isFollow = false;
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_focus);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            tv_focus.setCompoundDrawables(drawable, null, null, null);
                            tv_focus.setText("关注Ta");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_FORUM_DETAILS:
                break;
            case INTENT_COMMUNITY_DETAILS:
                break;
        }


    }
}
