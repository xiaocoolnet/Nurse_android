package chinanurse.cn.nurse.Fragment_Nurse.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import chinanurse.cn.nurse.Fragment_Nurse.adapter.SearchForumAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.KeyBoardUtils;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.Fragment_Nurse.view.ClearEditText;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import cz.msebera.android.httpclient.Header;

import static chinanurse.cn.nurse.R.id.pager;

/**
 * Created by zhuchongkun on 2017/1/5.
 */

public class SearchForumActivity extends Activity implements View.OnClickListener {
    private String TAG="SearchForumActivity";
    private final int INTENT_FORUM_DETAILS=1;
    private Context mContext;
    private RelativeLayout rl_back;
    private ClearEditText mClearEditText;
    private UserBean userBean;

    private ListView lv_view;
    private TextView shuaxin_button;
    private RelativeLayout ril_shibai, ril_list;
    private PullToRefreshListView pulllist;
    private TextView detail_loading;
    private SearchForumAdapter adapter;
    private String title="";
    private ArrayList<ForumDataBean> forumList;
    private ForumBean forumBean;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_forum);
        mContext = this;
        userBean=new UserBean(mContext);
        initView();
    }


    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_search_forum_back);
        rl_back.setOnClickListener(this);
        mClearEditText = (ClearEditText) findViewById(R.id.clearEditText_search_forum);
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    if (s.length() > 0) {
                        title=s.toString();
                        getData(s.toString());
                    } else {
                        title="";
                        forumList.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        shuaxin_button = (TextView)findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout)findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout)findViewById(R.id.ril_list);
        detail_loading = (TextView) findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView)findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(false);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setPullRefreshEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

        });
        lv_view = pulllist.getRefreshableView();
        lv_view.setDivider(null);
        forumList = new ArrayList<ForumDataBean>();
        adapter = new SearchForumAdapter(mContext, forumList);
        lv_view.setAdapter(adapter);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>=3){
                    LogUtils.e(TAG,"-qian-->"+forumList.get(position-3).toString());
                    Intent intentForumDetails=new Intent();
                    intentForumDetails.setClass(mContext, ForumDetailsActivity.class);
                    intentForumDetails.putExtra("forum",forumList.get(position-3));
                    intentForumDetails.putExtra("from","SearchForumActivity");
                    startActivityForResult(intentForumDetails,INTENT_FORUM_DETAILS);
                }
            }
        });
    }
    private void getData(final String title) {
        if (HttpConnect.isConnnected(mContext)) {
            getForumListByPager(title);
        } else {
            ToastUtils.ToastShort(mContext,getString(R.string.net_erroy));
            ril_shibai.setVisibility(View.VISIBLE);
            ril_list.setVisibility(View.GONE);
            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData(title);
                }
            });
        }
    }
    private void getForumListByPager(final String title) {
//        cid(圈子的id),userid,isbest(选精1,不0),istop(置顶1,不0),pager 分页 ,title 搜索标题
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userBean.getUserid());
        requestParams.put("isbest", 0);
        requestParams.put("istop", 0);
        requestParams.put("pager", 1);
        requestParams.put("title", title);
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
                            adapter = new SearchForumAdapter(mContext, forumList);
                            lv_view.setAdapter(adapter);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    ril_shibai.setVisibility(View.VISIBLE);
                    ril_list.setVisibility(View.GONE);
                    shuaxin_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getData(title);
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.ToastShort(mContext,getString(R.string.net_erroy));
                ril_shibai.setVisibility(View.VISIBLE);
                ril_list.setVisibility(View.GONE);
                shuaxin_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData(title);
                    }
                });
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_search_forum_back:
                KeyBoardUtils.hidekeyBoardByTime(mClearEditText, 0);
                finish();
                break;
        }
    }
}
