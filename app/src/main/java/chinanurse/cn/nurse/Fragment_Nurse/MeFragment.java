package chinanurse.cn.nurse.Fragment_Nurse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chinanurse.cn.nurse.Fragment_Nurse.activity.CommunityDetailActivity;
import chinanurse.cn.nurse.Fragment_Nurse.activity.MyAuthenticationActivity;
import chinanurse.cn.nurse.Fragment_Nurse.activity.MyForumActivity;
import chinanurse.cn.nurse.Fragment_Nurse.activity.SearchForumActivity;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.MeFragmentAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityNetBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
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
 * Created by zhuchongkun on 2016/12/11.
 * 护士站--圈子--我
 */

public class MeFragment extends Fragment implements View.OnClickListener {
    private String TAG = "MeFragment";

    private final int INTENT_COMMUNITY_DETAILS = 13;
    private final int INTENT_MY_FOTUM = 14;
    private final int INTENT_MY_AUTHENTICATION = 15;
    private final int INTENT_SEARCH=16;
    private UserBean user;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View viewH = null;
    private LinearLayout ll_search;
    private RelativeLayout rl_my_forum, rl_authentication, rl_manager_community;
    private TextView tv_status;

    private ListView lv_view;
    private TextView shuaxin_button;
    private RelativeLayout ril_shibai, ril_list;
    private ProgressDialog dialogpgd;
    private PullToRefreshListView pulllist;
    private TextView detail_loading;
    private MeFragmentAdapter adapter;
    private Gson gson;
    private int pager = 1;
    private ArrayList<CommunityBean> communityList;
    private CommunityNetBean communityBean;
    private boolean isCommunityMaster=false;
    private  String auth_status="";
    private AlertDialog auth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = View.inflate(getActivity(), R.layout.fragment_me, null);
        user = new UserBean(getActivity());
        inintView(mView);
        return mView;
    }

    private void inintView(View mView) {
        shuaxin_button = (TextView) mView.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) mView.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) mView.findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        detail_loading = (TextView) mView.findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView) mView.findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(false);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pager = 1;
                getData(pager);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (communityList.size() % 20 != 0) {
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
        lv_view.setDivider(null);
        viewH = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me_head, null);
        ll_search= (LinearLayout) viewH.findViewById(R.id.ll_fragment_me_head_search);
        ll_search.setVisibility(View.VISIBLE);
        ll_search.setOnClickListener(this);
        rl_my_forum = (RelativeLayout) viewH.findViewById(R.id.rl_fragment_me_head_my_forum);
        rl_my_forum.setOnClickListener(this);
        rl_authentication = (RelativeLayout) viewH.findViewById(R.id.rl_fragment_me_head_authentication);
        rl_authentication.setOnClickListener(this);
        tv_status= (TextView) viewH.findViewById(R.id.tv_status);
        rl_manager_community = (RelativeLayout) viewH.findViewById(R.id.rl_fragment_me_head_manager_community);
        rl_manager_community.setOnClickListener(this);
        lv_view.addHeaderView(viewH);
        communityList = new ArrayList<CommunityBean>();
        adapter = new MeFragmentAdapter(getActivity(), communityList);
        lv_view.setAdapter(adapter);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intentCommunityDetails = new Intent();
                    intentCommunityDetails.setClass(getActivity(), CommunityDetailActivity.class);
                    intentCommunityDetails.putExtra("community", communityList.get(position - 1));
                    intentCommunityDetails.putExtra("from", "MeFragment");
                    startActivityForResult(intentCommunityDetails, INTENT_COMMUNITY_DETAILS);
                }


            }
        });
        getData(pager);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPersonAuth();
        judgeAdmin();
        judgeCommunityMaster();
    }

    /**
     * 判断是否为圈主
     */
    private void judgeCommunityMaster() {
        //        入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.JUDGE_APPLY_COMMUNITY, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--judgeAdmin->" + response);
//                    {
//                        "status": "success",
//                            "data": "0"
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            String data = response.getString("data");
                            if (data.equals("yes")) {//1是，0不是
                                isCommunityMaster=true;
                                rl_manager_community.setVisibility(View.VISIBLE);
                                getCommunityId();
                            } else if (data.equals("no")) {
                                if (!isCommunityMaster){
                                    rl_manager_community.setVisibility(View.GONE);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getCommunityId() {
        //        入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_COMMUNITY_ID, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getCommunityId->" + response);
//                    {
//                        "status": "success",
//                            "data": "0"
//                    }
//                    try {
//                        String status = response.getString("status");
//                        if (status.equals("success")) {
//                            String data = response.getString("data");
//                            if (data.equals("yes")) {//1是，0不是
//                                isCommunityMaster=true;
//                                rl_manager_community.setVisibility(View.VISIBLE);
//                                getCommunityId();
//                            } else if (data.equals("no")) {
//                                if (!isCommunityMaster){
//                                    rl_manager_community.setVisibility(View.GONE);
//                                }
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }


    /**
     * 获取个人认证状态
     */
    private void getPersonAuth() {
//        入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_PERSON_AUTHENTICATION, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getPersonAuth->" + response);
//                    {
//                        "status": "success",
//                            "data": {
//                        "id": "17026",
//                                "status": "0"
//                    }
//                    }{"status":"success","data":{"id":"17026","status":"-2"}}?
//                    认证状态 1通过，-1拒绝，2认证中,0未认证
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            JSONObject jsonObject=response.getJSONObject("data");
                            auth_status=jsonObject.getString("status");
                            if (auth_status.equals("")||auth_status.equals("0")){
                                tv_status.setVisibility(View.GONE);
                            }else if (auth_status.equals("-1")){
                                tv_status.setVisibility(View.VISIBLE);
                                tv_status.setText("认证被拒绝");
                            }else if (auth_status.equals("1")){
                                tv_status.setVisibility(View.VISIBLE);
                                tv_status.setText("认证通过");
                            }else if (auth_status.equals("2")){
                                tv_status.setVisibility(View.VISIBLE);
                                tv_status.setText("认证中");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    /**
     * 判断是否是圈子管理员
     */
    private void judgeAdmin() {
//        入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.JUDGE_COMMUNITY_ADMIN, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--judgeAdmin->" + response);
//                    {
//                        "status": "success",
//                            "data": "0"
//                    }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            String data = response.getString("data");
                            if (data.equals("1")) {//1是，0不是
                                isCommunityMaster=true;
                                rl_manager_community.setVisibility(View.VISIBLE);
                            } else if (data.equals("0")) {
                                if (!isCommunityMaster){
                                    rl_manager_community.setVisibility(View.GONE);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getData(final int page) {
        if (HttpConnect.isConnnected(getActivity())) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            getMyCommunityListByPager(page);
        } else {
            ToastUtils.ToastShort(getActivity(),getString(R.string.net_erroy));
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

    private void getMyCommunityListByPager(int page) {
//        入参：userid,term_id(父类的分类id,全部则传0),best(选精1,不0),hot(热门1,不0),pager 分页
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("pager", page);
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_MY_COMMUNITY_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--getMyCommunityListByPager-onSuccess>" + response);
                if (response != null) {
                    LogUtils.e(TAG, "getMyCommunityListByPager-->" + response.toString());
                    ril_shibai.setVisibility(View.GONE);
                    ril_list.setVisibility(View.VISIBLE);
                    if (pager == 1) {
                        communityList.clear();
                    }
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        communityBean = gson.fromJson(response.toString(), CommunityNetBean.class);
                        communityList.addAll(communityBean.getData());
                        if (adapter == null) {
                            adapter = new MeFragmentAdapter(getActivity(), communityList);
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
                LogUtils.e(TAG, "--getMyCommunityListByPager-onFailure>" + responseString);
                dialogpgd.dismiss();
                ToastUtils.ToastShort(getActivity(),getString(R.string.net_erroy));
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
        switch (v.getId()) {
            case R.id.ll_fragment_me_head_search:
                Intent intentSearch=new Intent();
                intentSearch.setClass(getActivity(), SearchForumActivity.class);
                intentSearch.putExtra("from","MeFragment");
                startActivityForResult(intentSearch,INTENT_SEARCH);
                break;
            case R.id.rl_fragment_me_head_my_forum:
                Intent intentMyForum = new Intent();
                intentMyForum.setClass(getActivity(), MyForumActivity.class);
                intentMyForum.putExtra("from", "MeFragment");
                startActivityForResult(intentMyForum, INTENT_MY_FOTUM);
                break;
            case R.id.rl_fragment_me_head_authentication://1通过，-1拒绝，2认证中,0未认证
                if (auth_status.equals("")||auth_status.equals("0")){
                    Intent intentMyAuthentication = new Intent();
                    intentMyAuthentication.setClass(getActivity(), MyAuthenticationActivity.class);
                    intentMyAuthentication.putExtra("from", "MeFragment");
                    startActivityForResult(intentMyAuthentication, INTENT_MY_AUTHENTICATION);
                }else if (auth_status.equals("-1")){
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View view = inflater.inflate(R.layout.dialog_auth, (ViewGroup)getActivity().findViewById(R.id.ll_dialog_auth));
                    final TextView tv_auth_status = (TextView) view.findViewById(R.id.tv_dialog_status);
                    final TextView tv_auth_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                    final TextView tv_auth_again = (TextView) view.findViewById(R.id.tv_dialog_again);
                    tv_auth_status.setText("认证被拒绝");
                    tv_auth_cancel.setText("取消");
                    tv_auth_again.setText("重新认证");
                    tv_auth_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            auth.dismiss();
                        }
                    });
                    tv_auth_again.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentMyAuthentication = new Intent();
                            intentMyAuthentication.setClass(getActivity(), MyAuthenticationActivity.class);
                            intentMyAuthentication.putExtra("from", "MeFragment");
                            startActivityForResult(intentMyAuthentication, INTENT_MY_AUTHENTICATION);
                            auth.dismiss();
                        }
                    });
                    auth = new AlertDialog.Builder(getActivity()).setView(view).show();
                }else if (auth_status.equals("1")){
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View view = inflater.inflate(R.layout.dialog_auth, (ViewGroup)getActivity().findViewById(R.id.ll_dialog_auth));
                    final TextView tv_auth_status = (TextView) view.findViewById(R.id.tv_dialog_status);
                    final TextView tv_auth_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                    final TextView tv_auth_again = (TextView) view.findViewById(R.id.tv_dialog_again);
                    tv_auth_status.setText("认证通过");
                    tv_auth_cancel.setText("取消");
                    tv_auth_again.setText("重新认证");
                    tv_auth_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            auth.dismiss();
                        }
                    });
                    tv_auth_again.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentMyAuthentication = new Intent();
                            intentMyAuthentication.setClass(getActivity(), MyAuthenticationActivity.class);
                            intentMyAuthentication.putExtra("from", "MeFragment");
                            startActivityForResult(intentMyAuthentication, INTENT_MY_AUTHENTICATION);
                            auth.dismiss();
                        }
                    });
                    auth = new AlertDialog.Builder(getActivity()).setView(view).show();
                }else if (auth_status.equals("2")){
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View view = inflater.inflate(R.layout.dialog_auth, (ViewGroup)getActivity().findViewById(R.id.ll_dialog_auth));
                    final TextView tv_auth_status = (TextView) view.findViewById(R.id.tv_dialog_status);
                    final TextView tv_auth_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                    final TextView tv_auth_again = (TextView) view.findViewById(R.id.tv_dialog_again);
                    tv_auth_status.setText("认证中");
                    tv_auth_cancel.setText("取消");
                    tv_auth_again.setText("重新认证");
                    tv_auth_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            auth.dismiss();
                        }
                    });
                    tv_auth_again.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentMyAuthentication = new Intent();
                            intentMyAuthentication.setClass(getActivity(), MyAuthenticationActivity.class);
                            intentMyAuthentication.putExtra("from", "MeFragment");
                            startActivityForResult(intentMyAuthentication, INTENT_MY_AUTHENTICATION);
                            auth.dismiss();
                        }
                    });
                    auth = new AlertDialog.Builder(getActivity()).setView(view).show();
                }
                break;
            case R.id.rl_fragment_me_head_manager_community:
                CommunityBean communityBean=new CommunityBean();
                Intent intentMyAuthentication = new Intent();
                intentMyAuthentication.setClass(getActivity(), CommunityDetailActivity.class);
                intentMyAuthentication.putExtra("from", "MeFragment");
                intentMyAuthentication.putExtra("community", "communityBean");
                startActivityForResult(intentMyAuthentication, INTENT_MY_AUTHENTICATION);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_COMMUNITY_DETAILS:
                break;
            case INTENT_MY_FOTUM:
                break;
            case INTENT_MY_AUTHENTICATION:
                switch (resultCode){
                    case 101:
                        getPersonAuth();
                        break;
                }
                break;
        }


    }
}
