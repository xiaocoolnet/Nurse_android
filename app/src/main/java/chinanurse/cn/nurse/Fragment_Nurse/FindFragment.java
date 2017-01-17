package chinanurse.cn.nurse.Fragment_Nurse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import chinanurse.cn.nurse.Fragment_Nurse.activity.CommunityDetailActivity;
import chinanurse.cn.nurse.Fragment_Nurse.activity.CommunityListActivity;
import chinanurse.cn.nurse.Fragment_Nurse.activity.ForumDetailsActivity;
import chinanurse.cn.nurse.Fragment_Nurse.activity.PublishForumActivity;
import chinanurse.cn.nurse.Fragment_Nurse.activity.SearchForumActivity;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.ChoicePublishCommunityAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.FindFragmentAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.FindFragmentRecommendAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.adapter.FindRecommendRecyclerViewAdapter;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ChoicePublishCommunty;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityNetBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.MainActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2016/12/11.
 * 护士站--圈子--发现
 */

public class FindFragment extends Fragment implements View.OnClickListener,View.OnTouchListener{
    private String TAG = "FindFragment";
    private final int INTENT_FORUM_DETAILS=1;
    private final int INTENT_COMMUNITY_LIST=2;
    private final int INTENT_COMMUNITY_DETAILS=3;
    private final int INTENT_PUBLISH_FORUM=4;
    private final int INTENT_SEARCH=5;
    private UserBean user;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View viewH = null;
    private LinearLayout ll_search,ll_message,ll_recommend;
    private TextView tv_new_message;
    private LinearLayout ll_chocie_community, ll_all_community, ll_hot_community;
    private RecyclerView mRecyclerView;
    private ListView lv_recommend;


    private ListView lv_view;
    private TextView shuaxin_button;
    private RelativeLayout ril_shibai, ril_list;
    private ProgressDialog dialogpgd;
    private PullToRefreshListView pulllist;
    private TextView detail_loading;
    private FindFragmentAdapter adapter;
    private FindFragmentRecommendAdapter recommendAdapter;
    private FindRecommendRecyclerViewAdapter recyclerViewAdapter;
    private MainActivity activity;
    private int pager = 1;
    private ArrayList<CommunityBean> communityList;
    private CommunityNetBean communityBean;
    private ArrayList<ForumDataBean> forumList;
    private ForumBean forumBean;
    private ArrayList<ForumDataBean> forumRecommendList;
    private ForumBean forumRecommendBean;
    private Gson gson;
    private ImageButton bt_edit;
    private ArrayList<ChoicePublishCommunty.ChoicePublishData> choicePublishArrayList=new ArrayList<ChoicePublishCommunty.ChoicePublishData>();
    private ChoicePublishCommunty choicePublishCommunty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = View.inflate(getActivity(), R.layout.fragment_find, null);
        user = new UserBean(getActivity());
        inintView(mView);
        return mView;
    }

    private void inintView(View mView) {
        shuaxin_button = (TextView) mView.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) mView.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) mView.findViewById(R.id.ril_list);
        bt_edit= (ImageButton) mView.findViewById(R.id.imagebutton_bi);
        bt_edit.setOnClickListener(this);
        dialogpgd = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        detail_loading = (TextView) mView.findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView) mView.findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(true);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pager = 1;
                getData(pager);
                pulllist.setPullLoadEnabled(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (forumList.size() % 10 != 0) {//发现页面每次返回10条数据
                    stopRefresh();
                    pulllist.setPullLoadEnabled(false);
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
        viewH = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_find_head, null);
        ll_search= (LinearLayout) viewH.findViewById(R.id.ll_fragment_find_head_search);
        ll_search.setOnClickListener(this);
        ll_chocie_community = (LinearLayout) viewH.findViewById(R.id.ll_choice_community);
        ll_chocie_community.setOnClickListener(this);
        ll_all_community = (LinearLayout) viewH.findViewById(R.id.ll_all_community);
        ll_all_community.setOnClickListener(this);
        ll_hot_community = (LinearLayout) viewH.findViewById(R.id.ll_hot_community);
        ll_hot_community.setOnClickListener(this);
        mRecyclerView = (RecyclerView) viewH.findViewById(R.id.recycler_community_recommend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        communityList = new ArrayList<CommunityBean>();
        recyclerViewAdapter = new FindRecommendRecyclerViewAdapter(getActivity(), communityList);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickLitener(new FindRecommendRecyclerViewAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intentCommunityDetails=new Intent();
                intentCommunityDetails.setClass(getActivity(), CommunityDetailActivity.class);
                intentCommunityDetails.putExtra("community",communityList.get(position));
                intentCommunityDetails.putExtra("from","FindFragment");
                startActivityForResult(intentCommunityDetails,INTENT_COMMUNITY_DETAILS);
            }
            @Override
            public void onItemLongClick(View view, int position) {
//                Toast.makeText(getActivity(),"长按"+position,Toast.LENGTH_SHORT).show();
            }
        });
        lv_recommend= (ListView) viewH.findViewById(R.id.lv_forum_recommend);
        ll_recommend= (LinearLayout) viewH.findViewById(R.id.ll_forum_recommend);
        forumRecommendList=new ArrayList<ForumDataBean>();
        recommendAdapter=new FindFragmentRecommendAdapter(getActivity(),forumRecommendList);
        lv_recommend.setAdapter(recommendAdapter);
        lv_recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentForumDetails=new Intent();
                intentForumDetails.setClass(getActivity(), ForumDetailsActivity.class);
                intentForumDetails.putExtra("forum",forumRecommendList.get(position));
                intentForumDetails.putExtra("from","FindFragment");
                startActivityForResult(intentForumDetails,INTENT_FORUM_DETAILS);
            }
        });
        lv_view.addHeaderView(viewH);
        forumList = new ArrayList<ForumDataBean>();
        adapter = new FindFragmentAdapter(getActivity(), forumList);
        lv_view.setAdapter(adapter);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position>=1){
                    LogUtils.e(TAG,"-qian-->"+forumList.get(position-1).toString());
                    Intent intentForumDetails=new Intent();
                    intentForumDetails.setClass(getActivity(), ForumDetailsActivity.class);
                    intentForumDetails.putExtra("forum",forumList.get(position-1));
                    intentForumDetails.putExtra("from","FindFragment");
                    startActivityForResult(intentForumDetails,INTENT_FORUM_DETAILS);
                }
            }
        });
        getData(pager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case INTENT_FORUM_DETAILS:
                break;
            case INTENT_COMMUNITY_DETAILS:
                break;
            case INTENT_COMMUNITY_LIST:
                break;
            case INTENT_PUBLISH_FORUM:
                switch (resultCode){
                    case 1:
                        pager=1;
                        getForumListByPager(pager);
                        break;
                }
                break;
        }


    }

    private void getData(final int page) {
        if (HttpConnect.isConnnected(getActivity())) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            getForumListByPager(page);
            if (page == 1) {
                getCommunityListByPager(page);
                getForumRecommendListByPager();
            }
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

    private void getForumRecommendListByPager() {
        //        cid(圈子的id),userid,isbest(选精1,不0),istop(置顶1,不0),pager 分页
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("recommend", 1);
        NurseAsyncHttpClient.get(CommunityNetConstant.FORUM_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getForumRecommendListByPager->" + response);
                    ril_shibai.setVisibility(View.GONE);
                    ril_list.setVisibility(View.VISIBLE);
                    forumRecommendList.clear();
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        forumRecommendBean = gson.fromJson(response.toString(), ForumBean.class);
                        forumRecommendList.addAll(forumRecommendBean.getData());
                        if (recommendAdapter == null) {
                            recommendAdapter = new FindFragmentRecommendAdapter(getActivity(), forumRecommendList);
                            lv_recommend.setAdapter(adapter);
                        }
                        recommendAdapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(lv_recommend);
                        if (forumRecommendList.size()==0){
                            ll_recommend.setVisibility(View.GONE);
                        }else {
                            ll_recommend.setVisibility(View.VISIBLE);
                        }
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
                ToastUtils.ToastShort(getActivity(),getString(R.string.net_erroy));
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
        });

}

    private void getCommunityListByPager(int page) {
//        入参：userid,term_id(父类的分类id,全部则传0),best(选精1,不0),hot(热门1,不0),pager 分页
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("term_id", 0);
        requestParams.put("best", 0);
        requestParams.put("hot", 1);
        requestParams.put("pager", page);
        NurseAsyncHttpClient.get(CommunityNetConstant.COMMUNITY_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getCommunityListByPager->" + response);
                    ril_shibai.setVisibility(View.GONE);
                    ril_list.setVisibility(View.VISIBLE);
                    if (pager == 1) {
                        communityList.clear();
                    }
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        communityBean = gson.fromJson(response.toString(), CommunityNetBean.class);
                        communityList.addAll(communityBean.getData());
                        if (recyclerViewAdapter == null) {
                            recyclerViewAdapter = new FindRecommendRecyclerViewAdapter(getActivity(), communityList);
                            mRecyclerView.setAdapter(recyclerViewAdapter);
                        }
                        recyclerViewAdapter.notifyDataSetChanged();
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
                ToastUtils.ToastShort(getActivity(),getString(R.string.net_erroy));
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
        });

    }

    private void getForumListByPager(int page) {
//        cid(圈子的id),userid,isbest(选精1,不0),istop(置顶1,不0),pager 分页
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("pager", page);
        NurseAsyncHttpClient.get(CommunityNetConstant.FORUM_LIST, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getForumListByPager->" + response);
                    ril_shibai.setVisibility(View.GONE);
                    ril_list.setVisibility(View.VISIBLE);
                    if (pager == 1) {
                        forumList.clear();
                    }
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        forumBean = gson.fromJson(response.toString(), ForumBean.class);
                        forumList.addAll(forumBean.getData());

                        if (adapter == null) {
                            adapter = new FindFragmentAdapter(getActivity(), forumList);
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
                ToastUtils.ToastShort(getActivity(),getString(R.string.net_erroy));
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fragment_find_head_search:
                Intent intentSearch=new Intent();
                intentSearch.setClass(getActivity(), SearchForumActivity.class);
                intentSearch.putExtra("from","FindFragment");
                startActivityForResult(intentSearch,INTENT_SEARCH);
                break;
            case R.id.ll_choice_community:
                Intent intentChoice=new Intent();
                intentChoice.setClass(getActivity(), CommunityListActivity.class);
                intentChoice.putExtra("type","choice");
                startActivityForResult(intentChoice,INTENT_COMMUNITY_LIST);
                break;
            case R.id.ll_all_community:
                Intent intentAll=new Intent();
                intentAll.setClass(getActivity(),CommunityListActivity.class);
                intentAll.putExtra("type","all");
                startActivityForResult(intentAll,INTENT_COMMUNITY_LIST);
                break;
            case R.id.ll_hot_community:
                Intent intentHot=new Intent();
                intentHot.putExtra("type","hot");
                intentHot.setClass(getActivity(),CommunityListActivity.class);
                startActivityForResult(intentHot,INTENT_COMMUNITY_LIST);
                break;
            case R.id.imagebutton_bi:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    getFoucsCommunity();
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

                break;
        }
    }
    private void getFoucsCommunity() {
//        入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_PUBLISH_COMMUNITY, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getFoucsCommunity->" + response);
                    if ("success".equals(response.optString("status"))) {
                        gson = new Gson();
                        choicePublishCommunty = gson.fromJson(response.toString(), ChoicePublishCommunty.class);
                        choicePublishArrayList.addAll(choicePublishCommunty.getData());
                        if (choicePublishArrayList.size()>0){
                            Intent intentPublish=new Intent();
                            intentPublish.putExtra("from","FindFragment");
                            intentPublish.setClass(getActivity(), PublishForumActivity.class);
                            startActivityForResult(intentPublish,INTENT_PUBLISH_FORUM);
                        }else {
                            ToastUtils.ToastShort(getActivity(),"未加入圈子!");
                        }

                    }
                }
            }

        });

    }

    /*
      解决scrollview下listview显示不全
    */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(this);
    }
}
