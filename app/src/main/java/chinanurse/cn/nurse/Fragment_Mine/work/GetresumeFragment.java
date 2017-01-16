package chinanurse.cn.nurse.Fragment_Mine.work;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Nurse_job.EmployResumeDetailsActivity;
import chinanurse.cn.nurse.HttpConn.NetUtil;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.adapter.main_adapter.Mine_Recruit_First_Adapter;
import chinanurse.cn.nurse.bean.NurseEmployTalentBean;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;


public class GetresumeFragment extends Fragment {


    private UserBean user;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Mine_Recruit_First_Adapter recruit_adapter;
    private Activity activity;
    private List<NurseEmployTalentBean.DataBean> list_mine_recruit;
    private final int GETMYRECIVERESUMELIST = 1111;
    private TextView detail_loading;
    private PullToRefreshListView pulllist;
    private ListView lv_view;
    private View view;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private RelativeLayout all_title_top;
    private RelativeLayout ril_shibai, ril_list;
    private TextView shuaxin_button;
    private ProgressDialog dialogpgd;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;




    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETMYRECIVERESUMELIST:
                    list_mine_recruit.clear();
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    if (jsonObject != null) {
                        ril_shibai.setVisibility(View.GONE);
                        ril_list.setVisibility(View.VISIBLE);
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                Log.e("result_data", "66666");
                                int length = jsonArray.length();
                                JSONObject itemObject;
                                for (int i = 0; i < length; i++) {
                                    itemObject = (JSONObject) jsonArray.get(i);
                                    NurseEmployTalentBean.DataBean mine_recruit = new NurseEmployTalentBean.DataBean();
                                    mine_recruit.setName(itemObject.getString("name"));
                                    mine_recruit.setSex(itemObject.getString("sex"));
                                    mine_recruit.setAvatar(itemObject.getString("avatar"));
                                    mine_recruit.setBirthday(itemObject.getString("birthday"));
                                    mine_recruit.setExperience(itemObject.getString("experience"));
                                    mine_recruit.setEducation(itemObject.getString("education"));
                                    mine_recruit.setCertificate(itemObject.getString("certificate"));
                                    mine_recruit.setWantposition(itemObject.getString("wantposition"));
                                    mine_recruit.setTitle(itemObject.getString("title"));
                                    mine_recruit.setAddress(itemObject.getString("address"));
                                    mine_recruit.setCurrentsalary(itemObject.getString("currentsalary"));
                                    mine_recruit.setJobstate(itemObject.getString("jobstate"));
                                    mine_recruit.setDescription(itemObject.getString("description"));
                                    mine_recruit.setEmail(itemObject.getString("email"));
                                    mine_recruit.setPhone(itemObject.getString("phone"));
                                    mine_recruit.setHiredate(itemObject.getString("hiredate"));
                                    mine_recruit.setWantcity(itemObject.getString("wantcity"));
                                    mine_recruit.setWantsalary(itemObject.getString("wantsalary"));
                                    mine_recruit.setCreate_time(itemObject.getString("create_time"));

                                    Log.e("result_data", itemObject.getString("birthday"));
                                    list_mine_recruit.add(mine_recruit);
                                }
                                editor.putString("list_mine_recruit",String.valueOf(list_mine_recruit.size()));
                                Log.e("list_mine_recruit","-------------->"+list_mine_recruit.size());
                                editor.commit();
                                recruit_adapter = new Mine_Recruit_First_Adapter(activity, list_mine_recruit, 0);
                                lv_view.setAdapter(recruit_adapter);
                                dialogpgd.dismiss();
                            }else{
                                dialogpgd.dismiss();
                            }
                        } catch (JSONException e) {
                            dialogpgd.dismiss();
                            e.printStackTrace();
                        }
                    }else{
                        dialogpgd.dismiss();
                        ril_shibai.setVisibility(View.VISIBLE);
                        ril_list.setVisibility(View.GONE);
                        shuaxin_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initData();
                            }
                        });
                    }
                    break;
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        user = new UserBean(activity);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_getresume_listview, container, false);
        sp = getActivity().getSharedPreferences("nursenum", Context.MODE_PRIVATE);
        editor=sp.edit();
        recuitview();
        list_mine_recruit = new ArrayList<>();
        setOnItemClick();

        return view;
    }

    private void recuitview() {
        shuaxin_button = (TextView) view.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) view.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) view.findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
//        dialogpgd.setCancelable(false);
        all_title_top = (RelativeLayout) view.findViewById(R.id.all_title_top);
        all_title_top.setVisibility(View.GONE);
        detail_loading = (TextView) view.findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView) view.findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(true);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
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
//        lv_view = new SwipeListView(activity);
    }
//



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

    private void setOnItemClick() {
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), EmployResumeDetailsActivity.class);
                NurseEmployTalentBean.DataBean mine_recruit = list_mine_recruit.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("recruit", mine_recruit);
                intent.putExtra("type", "1");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getActivity(), "收到简历");
        initData();
    }
    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "收到简历");
    }
    private void initData() {
        if (NetUtil.isConnnected(activity)) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            //user.getUserid()报错 数据的状态 error
            //接口暂时  采用  获取本公司收到简历列表
            new StudyRequest(getActivity(), handler).getResumeList(user.getUserid(), GETMYRECIVERESUMELIST);
        }else{
            Log.i("onResume", "initData2");
            Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
            ril_shibai.setVisibility(View.VISIBLE);
            ril_list.setVisibility(View.GONE);
            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initData();
                }
            });
        }
    }
}
