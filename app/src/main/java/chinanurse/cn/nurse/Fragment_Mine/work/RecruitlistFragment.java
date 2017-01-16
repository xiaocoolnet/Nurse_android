package chinanurse.cn.nurse.Fragment_Mine.work;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Nurse_job.adapter.NurseEmployTalentAdapter_recuit;
import chinanurse.cn.nurse.Fragment_Nurse_job.bean.Talent_work_bean;
import chinanurse.cn.nurse.HttpConn.NetUtil;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.adapter.main_adapter.Mine_Recruit_Two_Adapter;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

public class RecruitlistFragment extends Fragment {


    private static final int GETPUBLISH = 1;



    private Mine_Recruit_Two_Adapter recruit_adapter;
    private Activity activity;
    private OnFragmentInteractionListener mListener;
    private ImageButton imagebutton_bi;
    private List<Talent_work_bean.DataBean> talentlist = new ArrayList<>();
    private Talent_work_bean ralentwork;
    private UserBean user;
    private TextView detail_loading;
    private PullToRefreshListView pulllist;
    private ListView lv_view;
    private View view;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private NurseEmployTalentAdapter_recuit recuit_work_adpater;
    private RelativeLayout all_title_top;
    private RelativeLayout ril_shibai, ril_list;
    private TextView shuaxin_button;
    private ProgressDialog dialogpgd;



    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETPUBLISH:
                    String result = (String) msg.obj;
                    if (result != null){
                        ril_shibai.setVisibility(View.GONE);
                        ril_list.setVisibility(View.VISIBLE);
                    talentlist.clear();
                    Gson gson = new Gson();
                    ralentwork = gson.fromJson(result, Talent_work_bean.class);
                    talentlist.addAll(ralentwork.getData());
                    recuit_work_adpater = new NurseEmployTalentAdapter_recuit(getActivity(),talentlist,1);
                    lv_view.setAdapter(recuit_work_adpater);
                    recuit_work_adpater.notifyDataSetChanged();
                        dialogpgd.dismiss();
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


    public RecruitlistFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        user = new UserBean(activity);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_getresume, container, false);
        recruitlistview();
        setOnItemClick();


        return view;
    }

    private void recruitlistview() {
        shuaxin_button = (TextView) view.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) view.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) view.findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
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


    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void setOnItemClick() {
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), Mine_Recruit_Second_Detail_work.class);
                Talent_work_bean.DataBean mine_company = talentlist.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("company", mine_company);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getActivity(), "招聘列表");
        initData();
    }
    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "招聘列表");
    }
    private void initData() {
        if (NetUtil.isConnnected(activity)) {
            //user.getUserid()报错 数据的状态 error
            //接口暂时  采用  获取本公司收到简历列表
//            new StudyRequest(getActivity(), handler).employList();
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            new StudyRequest(getActivity(), handler).getMyPublishJobList(user.getUserid(), GETPUBLISH);
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