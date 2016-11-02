package chinanurse.cn.nurse.Fragment_Nurse_mine.fans_and_attion;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Nurse_mine.personal.GetresumeFragment_personal;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.adapter.main_adapter.Main_Attion_Adapter;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.bean.mine_main_bean.Mine_attion;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <<<<<<< HEAD
 * {@link OnFragmentInteractionListener} interface
 * =======
 * {@link OnFragmentInteractionListener} interface
 * >>>>>>> d5bff86c138279956b22f9d9335dfca88afe17c7
 * to handle interaction events.
 * Use the {@link AttentionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttentionFragment extends Fragment implements GetresumeFragment_personal.OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int GETATTIONLIST = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserBean user;
    private ListView lv_view;
    private TextView fans_no_num,detail_loading;
    private Mine_attion mineattion;
    private List<Mine_attion.DataEntity> attionlist = new ArrayList<Mine_attion.DataEntity>();

    private OnFragmentInteractionListener mListener;
    private Main_Attion_Adapter attionadapter;


    private View view;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private PullToRefreshListView pulllist;





    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttentionFragment.
     */
//    // TODO: Rename and change types and number of parameters
//    public static AttentionFragment newInstance(String param1, String param2) {
//        AttentionFragment fragment = new AttentionFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETATTIONLIST:
                    if (msg.obj != null) {
                        attionlist.clear();
                        String result = (String) msg.obj;
                        Gson gson = new Gson();
                        mineattion = gson.fromJson(result, Mine_attion.class);
                        if ("success".equals(mineattion.getStatus())) {
                            if (mineattion.getData() != null || mineattion.getData().size() > 0) {
//                                for (int i = 0;i <mineattion.getData().size();i++){
                                fans_no_num.setVisibility(View.GONE);
                                pulllist.setVisibility(View.VISIBLE);
                                detail_loading.setVisibility(View.VISIBLE);
                                //获取过来的列表
                                attionlist.addAll(mineattion.getData());
//                                }
                                attionadapter = new Main_Attion_Adapter(getActivity(), attionlist, 0);
                                lv_view.setAdapter(attionadapter);
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
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
//        pulllist.doPullRefreshing(true, 500);
        lv_view = pulllist.getRefreshableView();
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
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
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", "---------->onResume");
        fansfragment();
    }

    private void fansfragment() {
        if (HttpConnect.isConnnected(getActivity())) {

            //user.getUserid()

            new StudyRequest(getActivity(), handler).GETATTIONLIST(user.getUserid(), GETATTIONLIST);
        } else {
            Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
}
