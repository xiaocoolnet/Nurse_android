package chinanurse.cn.nurse.Fragment_Nurse.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import chinanurse.cn.nurse.AppApplication;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.ChoicePublishCommunityAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ChoicePublishCommunty;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2016/12/20.
 */

public class ChoicePublishCommunityActivity extends Activity implements View.OnClickListener {
    private String TAG = "ChoicePublishCommunityActivity";
    private static final int UPDATE_ADPTER = 120;
    private UserBean user;
    private Context mContext;
    private ProgressDialog proDialog;
    private ListView lv_view;
    private TextView shuaxin_button, tv_sure;
    private RelativeLayout ril_shibai, ril_list, rl_back;
    private ProgressDialog dialogpgd;
    private PullToRefreshListView pulllist;
    private TextView detail_loading;
    private ChoicePublishCommunityAdapter adapter;
    private ArrayList<ChoicePublishCommunty.ChoicePublishData> choicePublishArrayList;
    private ChoicePublishCommunty choicePublishCommunty;
    private Gson gson;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_ADPTER:

                    if (adapter == null) {
                        adapter = new ChoicePublishCommunityAdapter(mContext, choicePublishArrayList, handler);
                        lv_view.setAdapter(adapter);
                    }
                    adapter.notifyDataSetChanged();

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choice_publish_community);
        mContext = this;
        user = new UserBean(mContext);
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_choice_publish_community_back);
        rl_back.setOnClickListener(this);
        tv_sure = (TextView) findViewById(R.id.tv_choice_publish_community_sure);
        tv_sure.setOnClickListener(this);
        shuaxin_button = (TextView) findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        detail_loading = (TextView) findViewById(R.id.detail_loading);
        lv_view = (ListView) findViewById(R.id.lv_comprehensive);
        lv_view.setDivider(null);
        choicePublishArrayList = new ArrayList<ChoicePublishCommunty.ChoicePublishData>();
        adapter = new ChoicePublishCommunityAdapter(mContext, choicePublishArrayList, handler);
        lv_view.setAdapter(adapter);
        getData();
    }

    private void getData() {
        if (HttpConnect.isConnnected(mContext)) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            getCommunityListByPager();

        } else {
            ToastUtils.ToastShort(mContext, getResources().getString(R.string.net_erroy));
            ril_shibai.setVisibility(View.VISIBLE);
            ril_list.setVisibility(View.GONE);
            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData();
                }
            });
        }
    }

    private void getCommunityListByPager() {
//        入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_PUBLISH_COMMUNITY, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getCommunityListByPager->" + response);
                    ril_shibai.setVisibility(View.GONE);
                    ril_list.setVisibility(View.VISIBLE);
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        choicePublishCommunty = gson.fromJson(response.toString(), ChoicePublishCommunty.class);
                        choicePublishArrayList.addAll(choicePublishCommunty.getData());
                        if (adapter == null) {
                            adapter = new ChoicePublishCommunityAdapter(mContext, choicePublishArrayList, handler);
                            lv_view.setAdapter(adapter);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    dialogpgd.dismiss();
                } else {
                    dialogpgd.dismiss();
                    ril_shibai.setVisibility(View.VISIBLE);
                    ril_list.setVisibility(View.GONE);
                    shuaxin_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getData();
                        }
                    });
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                dialogpgd.dismiss();
                ToastUtils.ToastShort(mContext, getResources().getString(R.string.net_erroy));
                ril_shibai.setVisibility(View.VISIBLE);
                ril_list.setVisibility(View.GONE);
                shuaxin_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_choice_publish_community_back:
                finish();
                break;
            case R.id.tv_choice_publish_community_sure:
//                if ()
                Intent intentDack = new Intent();
                intentDack.putExtra("id", AppApplication.TagId);
                intentDack.putExtra("name", AppApplication.TagName);
                setResult(RESULT_OK, intentDack);
                finish();
                break;
        }

    }
}
