package chinanurse.cn.nurse.Fragment_Nurse.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chinanurse.cn.nurse.Fragment_Nurse.adapter.MessageAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.bean.MessageBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.MessageDataBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2017/1/9.
 */

public class MessageActivity extends Activity implements View.OnClickListener{
    private String TAG="MessageActivity";
    private Context mContext;
    private UserBean user;
    private RelativeLayout rl_back;

    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private TextView shuaxin_button;
    private RelativeLayout ril_shibai, ril_list;
    private ProgressDialog dialogpgd;
    private PullToRefreshListView pulllist;
    private TextView detail_loading;
    private MessageAdapter adapter;
    private int pager = 1;
    private ArrayList<MessageDataBean> messageDataBeanArrayList;
    private MessageBean messageBean;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);
        mContext=this;
        user = new UserBean(mContext);
        inintView();
    }

    private void inintView() {
        rl_back= (RelativeLayout) findViewById(R.id.rl_message_back);
        rl_back.setOnClickListener(this);
        shuaxin_button = (TextView)findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout)findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout)findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        detail_loading = (TextView)findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView)findViewById(R.id.lv_comprehensive);
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
                if (messageDataBeanArrayList.size() % 20 != 0) {
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
        lv_view.setDividerHeight(1);
        messageDataBeanArrayList = new ArrayList<MessageDataBean>();
        adapter = new MessageAdapter(mContext, messageDataBeanArrayList);
        lv_view.setAdapter(adapter);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        getData(pager);
    }
    private void getData(final int page) {
        if (HttpConnect.isConnnected(mContext)) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            getMessageListByPager(page);
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
    private void getMessageListByPager(int page) {
//        入参：userid,pager
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("pager", page);
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_MY_MESSAGELIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getMessageListByPager->" + response);
                    ril_shibai.setVisibility(View.GONE);
                    ril_list.setVisibility(View.VISIBLE);
                    if (pager == 1) {
                        messageDataBeanArrayList.clear();
                    }
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        messageBean = gson.fromJson(response.toString(), MessageBean.class);
                        messageDataBeanArrayList.addAll(messageBean.getData());

                        if (adapter == null) {
                            adapter = new MessageAdapter(mContext, messageDataBeanArrayList);
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
        switch (v.getId()){
            case R.id.rl_message_back:
                finish();
                break;
        }

    }
}
