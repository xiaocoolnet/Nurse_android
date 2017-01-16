package chinanurse.cn.nurse.Fragment_Mine.fans_and_attion;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Mine.personal.GetresumeFragment_personal;
import chinanurse.cn.nurse.Fragment_Nurse.activity.PersonalHomePageActivity;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.adapter.main_adapter.Main_Attion_Adapter;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.bean.mine_main_bean.Mine_attion;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import chinanurse.cn.nurse.utils.LogUtils;


public class AttentionFragment extends Fragment implements GetresumeFragment_personal.OnFragmentInteractionListener{

    private String TAG="AttentionFragment";
    private static final int GETATTIONLIST = 1;
    private UserBean user;
    private ListView lv_view;
    private TextView fans_no_num,detail_loading;
    private Mine_attion mineattion;
    private List<Mine_attion.DataEntity> attionlist = new ArrayList<Mine_attion.DataEntity>();

    private Main_Attion_Adapter attionadapter;


    private View view;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private PullToRefreshListView pulllist;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETATTIONLIST:
                    if (msg.obj != null) {
                        attionlist.clear();
                        String result = (String) msg.obj;
                        Gson gson = new Gson();
                        LogUtils.e(TAG,"--->"+result);
                        mineattion = gson.fromJson(result, Mine_attion.class);
                        if ("success".equals(mineattion.getStatus())) {
                            if (mineattion.getData() != null || mineattion.getData().size() > 0) {
                                fans_no_num.setVisibility(View.GONE);
                                pulllist.setVisibility(View.VISIBLE);
                                detail_loading.setVisibility(View.VISIBLE);
                                //获取过来的列表
                                attionlist.addAll(mineattion.getData());
                                if (attionadapter==null){
                                    attionadapter = new Main_Attion_Adapter(getActivity(), attionlist, 0);
                                    lv_view.setAdapter(attionadapter);
                                }
                                attionadapter.notifyDataSetChanged();
                            } else {
                                fans_no_num.setVisibility(View.VISIBLE);
                                pulllist.setVisibility(View.GONE);
                                detail_loading.setVisibility(View.GONE);
                            }
                        }else{
                            fans_no_num.setVisibility(View.VISIBLE);
                            pulllist.setVisibility(View.GONE);
                            detail_loading.setVisibility(View.GONE);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attention, container, false);
        user = new UserBean(getActivity());
        attioniniview();
        fans_no_num = (TextView) view.findViewById(R.id.fans_no_num);
        return view;
    }

    private void attioniniview() {
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
        lv_view = pulllist.getRefreshableView();
        attionadapter = new Main_Attion_Adapter(getActivity(), attionlist, 0);
        lv_view.setAdapter(attionadapter);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.e(TAG,"-点击第几->"+position);
                ForumDataBean forumData = new ForumDataBean();
                forumData.setUserId(attionlist.get(position).getUserid().toString());
                forumData.setUserPhoto(attionlist.get(position).getPhoto().toString());
                forumData.setUserLevel(attionlist.get(position).getLevel().toString());
                Intent intentHomePage = new Intent();
                intentHomePage.setClass(getActivity(), PersonalHomePageActivity.class);
                intentHomePage.putExtra("forum", forumData);
                intentHomePage.putExtra("from", "AttentionFragment");
                startActivity(intentHomePage);
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
        StatService.onPageStart(getActivity(), "关注");
        fansfragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "关注");
    }
    private void fansfragment() {
        if (HttpConnect.isConnnected(getActivity())) {
            new StudyRequest(getActivity(), handler).GETATTIONLIST(user.getUserid(), GETATTIONLIST);
        } else {
            Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
