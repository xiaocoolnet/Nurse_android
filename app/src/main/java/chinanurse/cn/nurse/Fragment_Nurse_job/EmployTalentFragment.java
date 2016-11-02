package chinanurse.cn.nurse.Fragment_Nurse_job;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.FragmentTag;
import chinanurse.cn.nurse.Fragment_Main.SecondFragment;
import chinanurse.cn.nurse.Fragment_Nurse_job.adapter.Bean_list;
import chinanurse.cn.nurse.Fragment_Nurse_job.adapter.NurseEmployTalentAdapter;
import chinanurse.cn.nurse.Fragment_Nurse_job.bean.Talent_work_bean;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.NetUtil;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.MainActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.NurseEmployTalentBean;
import chinanurse.cn.nurse.bean.TalentAdapter_bean;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.dao.CommunalInterfaces;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * Created by wzh on 2016/6/26.
 */
public class EmployTalentFragment extends Fragment implements View.OnClickListener {


    private static final int GETMYPUBLISHJOB = 6;
    private static final int INDERVIEWAPPLY = 7;
    private static final int JBOPOST = 8;
    private ImageButton imagebutton_bi;
    private NurseEmployTalentAdapter nurseEmployTalentAdapter;
    private List<NurseEmployTalentBean.DataBean> talentDataList;
    private View mView;
    private ListView lv_view;

//    private ViewPager news_fisr_vp;

    private TextView detail_loading;
    private PullToRefreshListView pulllist;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View viewH;
    private UserBean user;
    private Talent_work_bean ralentwork;
    private List<Talent_work_bean.DataBean> talentlist = new ArrayList<>();
    private int position;
    private TalentAdapter_bean talenbean;
    private String result, jobid;
    private Gson gson;
    private RelativeLayout ril_shibai,ril_list;
    private TextView shuaxin_button,detail_loading_nonum;
    private ProgressDialog dialogpgd;
    private SecondFragment cf;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.TALENT_LIST:
                    talentDataList.clear();
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    if (jsonObject != null){
                    try {
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            int length = jsonArray.length();
                            JSONObject itemObject;
                            for (int i = 0; i < length; i++) {
                                itemObject = (JSONObject) jsonArray.get(i);
                                NurseEmployTalentBean.DataBean talentDataBean = new NurseEmployTalentBean.DataBean();
                                talentDataBean.setId(itemObject.getString("id"));
                                talentDataBean.setUserid(itemObject.getString("userid"));
                                talentDataBean.setName(itemObject.getString("name"));
                                talentDataBean.setSex(itemObject.getString("sex"));
                                talentDataBean.setAvatar(itemObject.getString("avatar"));
                                talentDataBean.setBirthday(itemObject.getString("birthday"));
                                talentDataBean.setExperience(itemObject.getString("experience"));
                                talentDataBean.setEducation(itemObject.getString("education"));
                                talentDataBean.setCertificate(itemObject.getString("certificate"));
                                talentDataBean.setWantposition(itemObject.getString("wantposition"));
                                talentDataBean.setTitle(itemObject.getString("title"));
                                talentDataBean.setAddress(itemObject.getString("address"));
                                talentDataBean.setCurrentsalary(itemObject.getString("currentsalary"));
                                talentDataBean.setJobstate(itemObject.getString("jobstate"));
                                talentDataBean.setDescription(itemObject.getString("description"));
                                talentDataBean.setEmail(itemObject.getString("email"));
                                talentDataBean.setPhone(itemObject.getString("phone"));
                                talentDataBean.setHiredate(itemObject.getString("hiredate"));
                                talentDataBean.setWantcity(itemObject.getString("wantcity"));
                                talentDataBean.setWantsalary(itemObject.getString("wantsalary"));
                                if (itemObject.getString("create_time") != null && itemObject.getString("create_time").length() > 0) {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
                                    long time = Long.parseLong(itemObject.getString("create_time"));
                                    talentDataBean.setCreate_time(simpleDateFormat.format(new Date(time * 1000)));
                                } else {
                                    talentDataBean.setCreate_time("");
                                }
                                talentDataList.add(talentDataBean);
                            }
                            nurseEmployTalentAdapter = new NurseEmployTalentAdapter(getActivity(), talentDataList, handler);
                            lv_view.setAdapter(nurseEmployTalentAdapter);
                            dialogpgd.dismiss();
                        }else{
                            dialogpgd.dismiss();
                            detail_loading_nonum.setVisibility(View.VISIBLE);
                            ril_list.setVisibility(View.GONE);
                            detail_loading_nonum.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    initData();
                                }
                            });
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
                        ril_shibai.setVisibility(View.GONE);
                        ril_list.setVisibility(View.VISIBLE);
                        initData();
                    }
                });}
                    break;
                case 5:
                    position = (int) msg.obj;
                    if (HttpConnect.isConnnected(getActivity())) {
                        new StudyRequest(getActivity(), handler).getMyPublishJobList(user.getUserid(), GETMYPUBLISHJOB);
//                            new StudyRequest(mcontext, handler).getMyPublishJobList(userid, INDERVIEW);
                        //userid个人  companyid企业   jobid招聘信息
                    } else {
                        Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case GETMYPUBLISHJOB:
                    result = (String) msg.obj;
                    if (result != null){
                    talentlist.clear();
                    gson = new Gson();
                    ralentwork = gson.fromJson(result, Talent_work_bean.class);
                    talentlist.addAll(ralentwork.getData());
                    String[] ralent = new String[talentlist.size()];
                    for (int i = 0; i < talentlist.size(); i++) {
                        ralent[i] = talentlist.get(i).getJobtype();
                    }
//                    final String[] sexchoose = new String[]{"女", "男"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(EmployTalentFragment.this.getActivity());
                    builder.setTitle("请选择");
                    builder.setSingleChoiceItems(ralent, 0, new DialogInterface.OnClickListener() {

                        @Override

                        public void onClick(DialogInterface dialog, int which) {
                            jobid = talentlist.get(which).getId();
                            if (HttpConnect.isConnnected(getActivity())) {
                                new StudyRequest(getActivity(), handler).InviteJob_judge(talentDataList.get(position).getUserid(), user.getUserid(), jobid, INDERVIEWAPPLY);
//                            new StudyRequest(mcontext, handler).getMyPublishJobList(userid, INDERVIEW);
                                //userid个人  companyid企业   jobid招聘信息
                            } else {
                                Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                            }

                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                    }else{
                        Toast.makeText(getActivity(), "暂无招聘信息", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case INDERVIEWAPPLY:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".endsWith(json.optString("status"))) {
                                String data = json.getString("data");
                                if ("1".equals(data)) {
                                    Toast.makeText(getActivity(), "您已经邀请过该用户", Toast.LENGTH_SHORT).show();
                                } else if ("0".equals(data)) {
                                    if (HttpConnect.isConnnected(getActivity())) {
                                        new StudyRequest(getActivity(), handler).InviteJob(user.getUserid(), jobid, talentDataList.get(position).getUserid(), JBOPOST);
                                    } else {
                                        Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(getActivity(), "邀请失败，重新邀请...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case JBOPOST:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
                                JSONObject json = new JSONObject(obj.getString("data"));
                                Toast.makeText(getActivity(), "邀请成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "邀请失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(getActivity(), "邀请失败，重新邀请...", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 10:
                    position = (int) msg.obj;
                    Log.i("posititon","--------------->"+position);
                    Bean_list.Beantanle_ist.clear();
                    NurseEmployTalentBean.DataBean employlist = talentDataList.get(position);
                    Bean_list.Beantanle_ist.add(employlist);

                    try{
                        cf = (SecondFragment) ((MainActivity) getActivity())
                                .getSupportFragmentManager().findFragmentByTag(
                                        FragmentTag.TAG_NURSE.getTag());
                        cf.switchFragmentone(FragmentTag.TAG_DETAILTALENT);
                        cf.visible();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        activity.getCurrentTage_three();
        user = new UserBean(getActivity());
        initView();

    }

    private void setOnItemClick() {
//        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("position","---------------->"+position);
//
//
//            }
//        });
    }

    private void initView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate( R.layout.employ_talent_fragment_switch,container, false);

        employiniview();
        setOnItemClick();
        return mView;
    }

    private void employiniview() {
        talentDataList = new ArrayList<>();
        detail_loading_nonum = (TextView) mView.findViewById(R.id.detail_loading_nonum);
        shuaxin_button = (TextView) mView.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) mView.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) mView.findViewById(R.id.ril_list);
        dialogpgd=new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        shuaxin_button = (TextView) mView.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) mView.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) mView.findViewById(R.id.ril_list);
        dialogpgd=new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        detail_loading = (TextView) mView.findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView) mView.findViewById(R.id.lv_comprehensive);
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
        lv_view = pulllist.getRefreshableView();
        lv_view.setDivider(null);
    }

    @Override
    public void onClick(View v) {

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
    public void onResume() {
        super.onResume();
        Log.i("onResume", "---------->onResume");
        initData();

    }

    public void initData() {
//        网络请求
        if (NetUtil.isConnnected(getActivity())) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            new StudyRequest(getActivity(), handler).talentList();
        }else {
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
