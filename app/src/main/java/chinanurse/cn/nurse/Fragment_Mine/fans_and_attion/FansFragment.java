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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Mine.personal.GetresumeFragment_personal;
import chinanurse.cn.nurse.Fragment_Nurse.activity.PersonalHomePageActivity;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.adapter.main_adapter.Main_Fans_Adapter;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.bean.mine_main_bean.Mine_fans;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import chinanurse.cn.nurse.utils.LogUtils;

public class FansFragment extends Fragment implements GetresumeFragment_personal.OnFragmentInteractionListener{
    private static final int GETFANSLIST = 1;
    private String TAG="FansFragment";
    private UserBean user;
private ListView lv_view;
    private TextView fans_no_num,detail_loading;
    private Main_Fans_Adapter mainfans;
    private List<Mine_fans.DataBean> mainfanslist = new ArrayList<Mine_fans.DataBean>();
    private Mine_fans minfans;
    private View view;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private PullToRefreshListView pulllist;


    private Handler handler  = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GETFANSLIST:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        LogUtils.e(TAG,"--->"+result);
                        mainfanslist.clear();
                        Gson gson = new Gson();
                        minfans = gson.fromJson(result,Mine_fans.class);
                        if ("success".equals(minfans.getStatus())){
                            if (minfans.getData() != null||minfans.getData().size() > 0){
                                fans_no_num.setVisibility(View.GONE);
                                pulllist.setVisibility(View.VISIBLE);
                                detail_loading.setVisibility(View.VISIBLE);
                                //获取过来的列表
                                mainfanslist.addAll(minfans.getData());
                                if (mainfans==null){
                                    mainfans = new Main_Fans_Adapter(getActivity(),mainfanslist,0);
                                    lv_view.setAdapter(mainfans);
                                }
                                mainfans.notifyDataSetChanged();
                            }else{
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
        view=inflater.inflate(R.layout.fragment_fans, container, false);
        user = new UserBean(getActivity());
        fansiniview();
        fans_no_num = (TextView) view.findViewById(R.id.fans_no_num);
        return view;
    }

    private void fansiniview() {
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
        mainfans = new Main_Fans_Adapter(getActivity(),mainfanslist,0);
        lv_view.setAdapter(mainfans);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumDataBean forumData = new ForumDataBean();
                forumData.setUserId(mainfanslist.get(position).getUserid().toString());
                forumData.setUserPhoto(mainfanslist.get(position).getPhoto().toString());
                forumData.setUserLevel(mainfanslist.get(position).getLevel().toString());
                Intent intentHomePage = new Intent();
                intentHomePage.setClass(getActivity(), PersonalHomePageActivity.class);
                intentHomePage.putExtra("forum", forumData);
                intentHomePage.putExtra("from", "FansFragment");
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
        StatService.onPageStart(getActivity(), "粉丝");
        fansfragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "粉丝");
    }
    private void fansfragment() {
        if (HttpConnect.isConnnected(getActivity())){
            new StudyRequest(getActivity(),handler).GETFANSLIST(user.getUserid(),GETFANSLIST);
        }else{
            Toast.makeText(getActivity(),R.string.net_erroy,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
