package chinanurse.cn.nurse.Fragment_Nurse.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chinanurse.cn.nurse.Fragment_Nurse.adapter.TopOrBestFragmentAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.MainActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2016/12/30.
 * 护士站--圈子--发现——-圈子列表--圈子详情--圈子详细情况--加精置顶帖子列表
 */

public class TopOrBestActivity extends Activity implements View.OnClickListener {
    private String TAG = "TopOrBestActivity";
    private final int INTENT_FORUM_DETAILS = 11;
    private final int INTENT_PUBLISH_FORUM = 12;
    private UserBean user;
    private Context mContext;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private TextView shuaxin_button;
    private RelativeLayout ril_shibai, ril_list;
    private ProgressDialog dialogpgd;
    private PullToRefreshListView pulllist;
    private TextView detail_loading;
    private TopOrBestFragmentAdapter adapter;
    private RelativeLayout rl_back;
    private TextView tv_title;
    private MainActivity activity;
    private CommunityBean communityBean;
    private int pager = 1;
    private ArrayList<ForumDataBean> forumList;
    private ForumBean forumBean;
    private Gson gson;
    private ImageButton bt_edit;
    int top = 0;
    int best = 0;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_top_or_best);
        mContext = this;
        user = new UserBean(mContext);
        communityBean = (CommunityBean) getIntent().getSerializableExtra("community");
        String type = getIntent().getStringExtra("type");
        if (type.equals("best")) {
            top = 0;
            best = 1;
            title = "加精帖子列表";
        } else if (type.equals("top")) {
            top = 1;
            best = 0;
            title = "置顶帖子列表";
        }
        inintView();
    }

    private void inintView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_fragment_top_or_best_back);
        rl_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_fragment_top_or_best_title);
        tv_title.setText(title);
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
        forumList = new ArrayList<ForumDataBean>();
        adapter = new TopOrBestFragmentAdapter(mContext, forumList);
        lv_view.setAdapter(adapter);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentForumDetails = new Intent();
                intentForumDetails.setClass(mContext, ForumDetailsActivity.class);
                intentForumDetails.putExtra("forum", forumList.get(position - 3));
                intentForumDetails.putExtra("from", "TopOrBestActivity");
                startActivityForResult(intentForumDetails, INTENT_FORUM_DETAILS);
            }
        });
        getData(pager);
    }

    private void getData(final int page) {
        if (HttpConnect.isConnnected(mContext)) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            getForumListByPager(page);
        } else {
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
    }

    private void getForumListByPager(int page) {
//      cid(圈子的id),userid,isbest(选精1,不0),istop(置顶1,不0),pager 分页
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("cid", communityBean.getId());
        requestParams.put("isbest", best);
        requestParams.put("istop", top);
        requestParams.put("pager", page);
        NurseAsyncHttpClient.get(CommunityNetConstant.FORUM_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
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
                            adapter = new TopOrBestFragmentAdapter(mContext, forumList);
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
            case R.id.rl_fragment_top_or_best_back:
                finish();
                break;
            case R.id.imagebutton_bi:
                Intent intentPublish=new Intent();
                intentPublish.putExtra("from","TopOrBestActivity");
                intentPublish.putExtra("communityBean",communityBean);
                intentPublish.setClass(mContext, PublishForumActivity.class);
                startActivityForResult(intentPublish,INTENT_PUBLISH_FORUM);
                break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_FORUM_DETAILS:
                break;
            case INTENT_PUBLISH_FORUM:
                break;
        }

    }
}
