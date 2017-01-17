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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chinanurse.cn.nurse.Fragment_Nurse.adapter.CommunityListAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.CommunityListHeadAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ChoicePublishCommunty;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityNetBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityTag;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.LogUtils;
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
 */

public class CommunityListActivity extends Activity implements View.OnClickListener {
    private String TAG = "CommunityListActivity";
    private final int INTENT_COMMUNITY_DETAILS = 4;
    private UserBean user;
    private Context mContext;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Spinner spinner_one, spinner_two;
    private CommunityListHeadAdapter fristAdapter, secondAdapter;
    private ArrayList<ChoicePublishCommunty.ChoicePublishData> choicePublishArrayList = new ArrayList<ChoicePublishCommunty.ChoicePublishData>();
    private ChoicePublishCommunty choicePublishCommunty;
    private ArrayList<CommunityTag> fristTags = new ArrayList<CommunityTag>();
    private ArrayList<CommunityTag> secondTags = new ArrayList<CommunityTag>();

    private RelativeLayout rl_back;
    private LinearLayout ll_spinner;
    private ListView lv_view;
    private TextView shuaxin_button, tv_title;
    private RelativeLayout ril_shibai, ril_list;
    private ProgressDialog dialogpgd;
    private PullToRefreshListView pulllist;
    private TextView detail_loading;
    private CommunityListAdapter adapter;
    private MainActivity activity;
    private Gson gson;
    private int pager = 1;
    private ArrayList<CommunityBean> communityList;
    private CommunityNetBean communityBean;
    private int hot = 0;
    private int best = 0;
    private String term_id;
    private String sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comunity_list);
        mContext = this;
        user = new UserBean(mContext);
        term_id = "0";
        sort = "0";
        inintView();

    }

    private void inintView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_fragment_community_list_back);
        rl_back.setOnClickListener(this);
        ll_spinner = (LinearLayout) findViewById(R.id.ll_spinner);
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
                if (communityList.size() % 20 != 0) {
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
        lv_view.setDividerHeight(2);
        tv_title = (TextView) findViewById(R.id.tv_fragment_community_list_title);
        spinner_one = (Spinner) findViewById(R.id.spinner_one);
        spinner_two = (Spinner) findViewById(R.id.spinner_two);
        CommunityTag tag = new CommunityTag();
        tag.setIsSelected("1");
        tag.setTagId("0");
        tag.setTagName("全部圈子");
        fristTags.add(tag);
        fristAdapter = new CommunityListHeadAdapter(mContext, fristTags);
        spinner_one.setAdapter(fristAdapter);
        spinner_one.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getItemAtPosition(position).toString();
                LogUtils.e(TAG, "---->" + s);
                LogUtils.e(TAG, "---->" + fristTags.get(position).toString());
                term_id = fristTags.get(position).getTagId();
                for (int i = 0; i < fristTags.size(); i++) {
                    if (i == position) {
                        fristTags.get(i).setIsSelected("1");
                    } else {
                        fristTags.get(i).setIsSelected("0");
                    }
                }
                fristAdapter.notifyDataSetChanged();
                getData(pager);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_one.setSelection(0);
        CommunityTag taga = new CommunityTag();
        taga.setIsSelected("1");
        taga.setTagId("0");
        taga.setTagName("智能排序");
        secondTags.add(taga);
        CommunityTag tagb = new CommunityTag();
        tagb.setIsSelected("0");
        tagb.setTagId("1");
        tagb.setTagName("圈子关注度");
        secondTags.add(tagb);
        CommunityTag tagc = new CommunityTag();
        tagc.setIsSelected("0");
        tagc.setTagId("2");
        tagc.setTagName("圈子帖子量");
        secondTags.add(tagc);
        secondAdapter = new CommunityListHeadAdapter(mContext, secondTags);
        spinner_two.setAdapter(secondAdapter);
        spinner_two.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getItemAtPosition(position).toString();
                LogUtils.e(TAG, "--onItemSelected-->" + s);
                LogUtils.e(TAG, "--onItemSelected-->" + secondTags.get(position).toString());
                sort = secondTags.get(position).getTagId();
                for (int i = 0; i < secondTags.size(); i++) {
                    if (i == position) {
                        secondTags.get(i).setIsSelected("1");
                    } else {
                        secondTags.get(i).setIsSelected("0");
                    }
                }
                secondAdapter.notifyDataSetChanged();
                getData(pager);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_two.setSelection(0);
        communityList = new ArrayList<CommunityBean>();
        adapter = new CommunityListAdapter(mContext, communityList);
        lv_view.setAdapter(adapter);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentCommunityDetails = new Intent();
                intentCommunityDetails.setClass(mContext, CommunityDetailActivity.class);
                intentCommunityDetails.putExtra("community", communityList.get(position));
                intentCommunityDetails.putExtra("from", "CommunityListActivity");
                startActivityForResult(intentCommunityDetails, INTENT_COMMUNITY_DETAILS);
            }
        });
        getCommunityList();
        String type = getIntent().getStringExtra("type");
        if (type.equals("hot")) {
            tv_title.setText("品管圈");
            hot = 0;
            best = 0;
            term_id = "300";
            sort = "0";
            ll_spinner.setVisibility(View.GONE);
        } else if (type.equals("choice")) {
            tv_title.setText("精选圈子");
            hot = 0;
            best = 1;
            term_id = "0";
            sort = "0";
            ll_spinner.setVisibility(View.GONE);
        } else {
            tv_title.setText("圈子列表");
            hot = 0;
            best = 0;
            term_id = "0";
            sort = "0";
            ll_spinner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(pager);
    }

    private void getCommunityList() {
//        入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.add("parentid","295");
        NurseAsyncHttpClient.get(CommunityNetConstant.CHANNEL_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getCommunityListByPager->" + response);
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        choicePublishCommunty = gson.fromJson(response.toString(), ChoicePublishCommunty.class);
                        choicePublishArrayList.addAll(choicePublishCommunty.getData());
                        for (int i = 0; i < choicePublishArrayList.size(); i++) {
                            CommunityTag tag = new CommunityTag();
                            tag.setIsSelected("0");
                            tag.setTagName(choicePublishArrayList.get(i).getTermName());
                            tag.setTagId(choicePublishArrayList.get(i).getTermId());
                            fristTags.add(tag);
                        }
                        fristAdapter.notifyDataSetChanged();
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
            getCommunityListByPager(page);
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
        requestParams.put("userid", user.getUserid());
        requestParams.put("term_id", term_id);
        requestParams.put("best", best);
        requestParams.put("hot", hot);
        requestParams.put("sort", sort);
        requestParams.put("pager", page);
        NurseAsyncHttpClient.get(CommunityNetConstant.COMMUNITY_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "getCommunityListByPager" + response.toString());
                    ril_shibai.setVisibility(View.GONE);
                    ril_list.setVisibility(View.VISIBLE);
                    if (pager == 1) {
                        communityList.clear();
                    }
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        communityBean = gson.fromJson(response.toString(), CommunityNetBean.class);
                        communityList.addAll(communityBean.getData());
                        if (adapter == null) {
                            adapter = new CommunityListAdapter(mContext, communityList);
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
            case R.id.rl_fragment_community_list_back:
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_COMMUNITY_DETAILS:
                break;
        }


    }
}
