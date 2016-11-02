package chinanurse.cn.nurse.MinePage.MyCollect;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext.Mine_Collect_First_Adapter;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.adapter.community.Community_listview_Adapter;
import chinanurse.cn.nurse.bean.MyCollect_bean;
import chinanurse.cn.nurse.bean.Nuser_community;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * Created by Administrator on 2016/7/17 0017.
 */
public class Mine_Collect_Third extends Fragment {

    private UserBean user;
    private Mine_Collect_First_Adapter collect_first_adapter;
    private ListView lv_view;

    //收藏中的 文章 data:[]  没有值,所以接口用的 收藏列表，但是容器用的 新闻的
    private List<MyCollect_bean.DataBean> list_first;
//    private ArrayList<MyCollect_bean.DataBean.LikesBean> fndlistlike;
//    private ArrayList<MyCollect_bean.DataBean.ThumbBean> fndthumblist;
//    private ArrayList<MyCollect_bean.DataBean.FavoritesBean> fndcollect;
    private final int GETMYRECIVERESUMELIST = 1111;
    private Activity mactivity;

    private static final int GETCOMMUNITYLIST = 1;
    //下listview

    private Nuser_community contentbean, contentbean_2;
    private Community_listview_Adapter clalistviewadapter, clalistviewadapter_2;
    private View view;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private PullToRefreshListView pulllist;
    private TextView detail_loading;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case GETMYRECIVERESUMELIST:
//                    list_first.clear();
//                    String result = (String) msg.obj;
//                    JSONObject jsonObject;
//                    try {
//                        jsonObject = new JSONObject(result);
//                        String status = jsonObject.getString("status");
//                        if (status.equals("success")) {
//                            JSONArray jsonArray = jsonObject.getJSONArray("data");
//                            Log.e("result_data", "66666");
//                            int length = jsonArray.length();
//                            JSONObject data;
//                            for (int i = 0; i < length; i++) {
//                                data = (JSONObject) jsonArray.get(i);
//                                MyCollect_bean.DataBean fnd = new MyCollect_bean.DataBean();
//                                fnd.setObject_id(data.getString("object_id"));
//                                fnd.setTerm_id(data.getString("term_id"));
//                                fnd.setTerm_name(data.getString("term_name"));
//                                fnd.setPost_title(data.getString("post_title"));
//                                fnd.setPost_excerpt(data.getString("post_excerpt"));
//                                fnd.setPost_date(data.getString("post_date"));
//                                fnd.setPost_modified(data.getString("post_modified"));
//                                fnd.setPost_source(data.getString("post_source"));
//                                fnd.setPost_like(data.getString("post_like"));
//                                fnd.setPost_hits(data.getString("post_hits"));
//                                fnd.setRecommended(data.getString("recommended"));
//                                fnd.setSmeta(NetBaseConstant.NET_WEB_VIEW + data.getString("object_id"));
//                                JSONArray jsonarraythumb = data.getJSONArray("thumb");
//                                fndthumblist = new ArrayList<>();
//                                for (int m = 0;m < jsonarraythumb.length();m++){
//                                    JSONObject datajsonthumb = jsonarraythumb.getJSONObject(m);
//                                    MyCollect_bean.DataBean.ThumbBean firstEntitythumb = new MyCollect_bean.DataBean.ThumbBean();
//                                    firstEntitythumb.setAlt(datajsonthumb.getString("alt"));
//                                    firstEntitythumb.setUrl(NetBaseConstant.NET_PIC_PREFIX_THUMB + datajsonthumb.getString("url"));
//                                    Log.i("URLPIC", "--------->" + NetBaseConstant.NET_PIC_PREFIX_THUMB + datajsonthumb.getString("url"));
//                                    fndthumblist.add(firstEntitythumb);
//                                }
//                                fnd.setThumb(fndthumblist);
//                                JSONArray jsonarrayone = data.getJSONArray("likes");
//                                fndlistlike = new ArrayList<>();
//                                for (int j = 0; j < jsonarrayone.length(); j++) {
//                                    JSONObject datajsonone = jsonarrayone.getJSONObject(j);
//                                    MyCollect_bean.DataBean.LikesBean firstEntitylike = new MyCollect_bean.DataBean.LikesBean();
//                                    firstEntitylike.setUserid(datajsonone.getString("userid"));
//                                    firstEntitylike.setAvatar(NetBaseConstant.NET_PIC_PREFIX + datajsonone.getString("avatar"));
//                                    firstEntitylike.setUsername(datajsonone.getString("username"));
//                                    fndlistlike.add(firstEntitylike);
//                                }
//                                fnd.setLikes(fndlistlike);
//                                JSONArray jsonarraycollect = data.getJSONArray("favorites");
//                                fndcollect = new ArrayList<>();
//                                for (int j = 0; j < jsonarraycollect.length(); j++) {
//                                    JSONObject datajsoncollect = jsonarraycollect.getJSONObject(j);
//                                    MyCollect_bean.DataBean.FavoritesBean firstEntitycollect = new MyCollect_bean.DataBean.FavoritesBean();
//                                    firstEntitycollect.setUserid(datajsoncollect.getString("userid"));
//                                    fndcollect.add(firstEntitycollect);
//                                }
//                                fnd.setFavorites(fndcollect);
//
//
//                                Log.e("result_data", i + "    ???????????");
//                                list_first.add(fnd);
//                            }
//                            collect_first_adapter = new Mine_Collect_First_Adapter(mactivity, list_first,0);
//                            lv_view.setAdapter(collect_first_adapter);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
            }

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mactivity = getActivity();
        view = View.inflate(mactivity, R.layout.fragment_getresume, null);
        user = new UserBean(mactivity);
        list_first = new ArrayList<>();
        collecthirdiniview();


        return view;
    }

    private void collecthirdiniview() {
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
            //获取收藏列表 type 1  为新闻  user.getUserid()
            new StudyRequest(mactivity, handler).SETCOLLEXT(user.getUserid(), "4", GETMYRECIVERESUMELIST);

        } else {
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }

    }

}
