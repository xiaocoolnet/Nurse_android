package chinanurse.cn.nurse.Fragment_Mine.my_collect_study.error;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.MinePage.MyStudy.MineStudt_question_error_Bean;
import chinanurse.cn.nurse.Fragment_Mine.my_collect_study.error.adapter.Study_Error_Adapter;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.Fragment_Mine.my_collect_study.error.bean.Question_hashmap_data_error;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class FirstFragment_Error extends Fragment implements View.OnClickListener {

    private Activity activity;
    private UserBean user;
    private ListView lv_view;
    private Study_Error_Adapter study_error_Adapter;
    private MineStudt_question_error_Bean question_error_Bean;
    private MineStudt_question_error_Bean.DataBean question_error_Bean_DataBean;
    private List<MineStudt_question_error_Bean.DataBean> list_question_bean;
    private View mView;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private PullToRefreshListView pulllist;
    private TextView detail_loading;
    private RelativeLayout all_title_top;
    private ProgressDialog dialogpgd;
    private RelativeLayout ril_shibai, ril_list;
    private TextView shuaxin_button;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case TEXTLIST_OVER:
                    if (msg.obj != null) {
                        list_question_bean.clear();
                        Question_hashmap_data_error.MyCollectList.clear();
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
                                JSONArray array = obj.getJSONArray("data");
                                Gson gson = new Gson();
                                question_error_Bean = gson.fromJson(result, MineStudt_question_error_Bean.class);
                                Question_hashmap_data_error.MyCollectList.addAll(question_error_Bean.getData());
                                list_question_bean.addAll(question_error_Bean.getData());
                                study_error_Adapter = new Study_Error_Adapter(activity, list_question_bean);
                                lv_view.setAdapter(study_error_Adapter);
                                dialogpgd.dismiss();
                            }else{
                                dialogpgd.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            dialogpgd.dismiss();
                        }
                    } else {
                        dialogpgd.dismiss();
                        ril_shibai.setVisibility(View.VISIBLE);
                        ril_list.setVisibility(View.GONE);
                        shuaxin_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fansfragment();
                            }
                        });
                    }
                    break;

//
//                        Question_hashmap_data.MyCollectList.addAll(quesbean.getData());
//                        list_first.addAll(quesbean.getData());
//                        collect_first_adapter = new Mine_Collect_Second_Adapter(mactivity, list_first,0);
//                        lv_view.setAdapter(collect_first_adapter);
//                        collect_first_adapter.notifyDataSetChanged();
            }


        }
    };
    private String type = "1";
    private final int TEXTLIST_OVER = 111;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        user = new UserBean(activity);
        mView = View.inflate(activity, R.layout.fragment_getresume, null);
        list_question_bean = new ArrayList<>();
        collectiniview();
        return mView;
    }
    private void collectiniview() {
        dialogpgd = new ProgressDialog(activity, android.app.AlertDialog.THEME_HOLO_LIGHT);
//        dialogpgd.setCancelable(false);
        shuaxin_button = (TextView) mView.findViewById(R.id.shuaxin_button);
        all_title_top = (RelativeLayout) mView.findViewById(R.id.all_title_top);
        all_title_top.setVisibility(View.GONE);
        ril_shibai = (RelativeLayout) mView.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) mView.findViewById(R.id.ril_list);
        detail_loading = (TextView) mView.findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView) mView.findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(true);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                fansfragment();
                stopRefresh();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                stopRefresh();
            }
        });
        setLastData();
//        pulllist.doPullRefreshing(true, 500);
        lv_view = pulllist.getRefreshableView();
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
    private String formatdatatime(long time){
        if (0==time){
            return "";
        }
        return mdata.format(new Date(time));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", "---------->onResume");
        StatService.onPageStart(getActivity(), "我的学习——每日一练错题集");
        fansfragment();
    }
    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "我的学习——每日一练错题集");
    }
    private void fansfragment() {
        if (HttpConnect.isConnnected(getActivity())) {
            //获取收藏列表 type 1  为新闻  user.getUserid()
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            new StudyRequest(getActivity(), handler).TEXTLIST_ERROR(user.getUserid(), "1", TEXTLIST_OVER);
        } else {
            Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
            Log.i("onResume", "initData2");
            ril_shibai.setVisibility(View.VISIBLE);
            ril_list.setVisibility(View.GONE);
            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fansfragment();
                }
            });
        }
    }
    @Override
    public void onClick(View v) {

    }
}
