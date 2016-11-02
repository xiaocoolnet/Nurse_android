package chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext.adapter.Mine_Collect_Second_Adapter;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.MyCollectQuestion_Bean;
import chinanurse.cn.nurse.bean.Question_hashmap_data;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * 我的收藏----试题界面
 * Created by Administrator on 2016/7/17 0017.
 */
public class Mine_Collect_Second extends Fragment {

    private UserBean user;


    // TODO: Rename and change types of parameters
    private Mine_Collect_Second_Adapter collect_first_adapter;
    private ListView lv_view;
    private Activity mactivity;

    //收藏中的 文章 data:[]  没有值,所以接口用的 收藏列表，但是容器用的 新闻的
    private List<MyCollectQuestion_Bean.DataBean> list_first;
    private final int GETMYRECIVERESUMELIST = 1111;
    private View view;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private PullToRefreshListView pulllist;
    private TextView detail_loading,view1;
    private RelativeLayout all_title_top;
    private RelativeLayout ril_shibai, ril_list;
    private TextView shuaxin_button;
    private ProgressDialog dialogpgd;


    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETMYRECIVERESUMELIST:
                    list_first.clear();
                    String result = (String) msg.obj;
                    if (result != null){
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");
                        if ("success".equals(status)) {
                            Question_hashmap_data.MyCollectList.clear();
                            Gson gson = new Gson();
                            MyCollectQuestion_Bean quesbean = gson.fromJson(result,MyCollectQuestion_Bean.class);
                            Question_hashmap_data.MyCollectList.addAll(quesbean.getData());
                            list_first.addAll(quesbean.getData());
                            collect_first_adapter = new Mine_Collect_Second_Adapter(mactivity, list_first,0);
                            lv_view.setAdapter(collect_first_adapter);
                            collect_first_adapter.notifyDataSetChanged();
                            dialogpgd.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }else{
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
            }

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mactivity = getActivity();
        view = View.inflate(mactivity, R.layout.fragment_getresume, null);
        user = new UserBean(mactivity);
        list_first = new ArrayList<>();
        collectwotiniview();


        return view;
    }

    private void collectwotiniview() {
        shuaxin_button = (TextView) view.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) view.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) view.findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        all_title_top = (RelativeLayout) view.findViewById(R.id.all_title_top);
        all_title_top.setVisibility(View.GONE);
        view1 = (TextView) view.findViewById(R.id.view);
        view1.setVisibility(View.GONE);
        detail_loading = (TextView) view.findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView) view.findViewById(R.id.lv_comprehensive);
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
        fansfragment();
    }

    private void fansfragment() {
        if (HttpConnect.isConnnected(mactivity)) {
            //获取收藏列表 type 1  为新闻  user.getUserid() type 2为试题
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            new StudyRequest(mactivity, handler).SETCOLLEXT(user.getUserid(), "2", GETMYRECIVERESUMELIST);
        } else {
            Log.i("onResume", "initData2");
            Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
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
}
