package chinanurse.cn.nurse.Fragment_Nurse_Study_News.goman;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView;
import chinanurse.cn.nurse.WebView.News_WebView_study;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.publicall.adapter.News_Down_Adapter;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class Goman_topic_Nation_fragment extends Fragment {


    private static final int FIRSTPAGELIST = 4;
    public static final int BTNCHECKHADFAVORITE = 7;
    public static final int CHECKHADFAVORITE = 8;
    private static final int CANCELCOLLECT = 9;
    private static final int SETCOLLECT = 10;
    private static final int RESETLIKE = 11;
    private static final int SETLIKE = 12;
    private static final int ADDSCORE = 13;
    private News_Down_Adapter lv_Adapter;
    private String channelid = "131",pagetype,result;
    private ArrayList<FirstPageNews.DataBean> fndlist = new ArrayList<>();

    private PullToRefreshListView pulllist;
    private TextView detail_loading,news_first_title;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private View mView;
    private Gson gson;
    private FirstPageNews fndbean;
    private UserBean user;
    private int position;
    private RelativeLayout ril_shibai, ril_list;
    private TextView shuaxin_button;
    private ProgressDialog dialogpgd;
    private Dialog dialog;
    private int page = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FIRSTPAGELIST:
                    result = (String) msg.obj;
                    if (result != null) {
                        ril_shibai.setVisibility(View.GONE);
                        ril_list.setVisibility(View.VISIBLE);
                        if (page == 1) {
                            fndlist.clear();
                        }
                        try {
                            JSONObject json = new JSONObject(result);
                            String data = json.getString("data");
                            if ("success".equals(json.optString("status"))){
                                gson = new Gson();
                                fndbean = gson.fromJson(result, FirstPageNews.class);
                                fndlist.addAll(fndbean.getData());
                                if (lv_Adapter==null){
                                    lv_Adapter = new News_Down_Adapter(getActivity(), fndlist, 4, handler);
                                    lv_view.setAdapter(lv_Adapter);
                                }else {
                                    if (page == 1) {
                                        lv_Adapter = new News_Down_Adapter(getActivity(), fndlist, 4, handler);
                                        lv_view.setAdapter(lv_Adapter);
                                    }else {
                                        lv_Adapter.notifyDataSetChanged();
                                    }
                                }
                                stopRefresh();
                            }else{
                                if ("end".equals(data)){
                                    Toast.makeText(getActivity(),"无更新数据",Toast.LENGTH_SHORT).show();
                                    stopRefresh();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                                gomandata();
                            }
                        });
                    }

                    break;
                case 5:
                    position = (int) msg.obj;
                    if (HttpConnect.isConnnected(getActivity())) {
                        new StudyRequest(getActivity(), handler).CheckHadLike(user.getUserid(), fndlist.get(position).getObject_id(), "1", BTNCHECKHADFAVORITE);
                    } else {
                        Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
//                    initData();
//                    flv_Adapter.notifyDataSetChanged();
                    break;
                case 6:
                    position = (int) msg.obj;
                    if (HttpConnect.isConnnected(getActivity())) {
                        new StudyRequest(getActivity(), handler).CheckHadFavorite(user.getUserid(), fndlist.get(position).getObject_id(), "3", CHECKHADFAVORITE);
                        Log.i("collect1", "-------------------->" + fndlist.get(position).getTerm_id());
                    } else {
                        Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
//                    initData();
//                    flv_Adapter.notifyDataSetChanged();
                    break;
                case BTNCHECKHADFAVORITE:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
//                                webview_sc.setBackgroundResource(R.mipmap.ic_collect_sel);

                                if (HttpConnect.isConnnected(getActivity())) {
                                    new StudyRequest(getActivity(), handler).resetLike(user.getUserid(), fndlist.get(position).getObject_id().toString(), "1", RESETLIKE);
                                } else {
                                    Toast.makeText(getActivity(),R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                webview_sc.setBackgroundResource(R.mipmap.ic_collect_nor);
                                if (HttpConnect.isConnnected(getActivity())) {
                                    //还有个time
                                    new StudyRequest(getActivity(), handler).setLike(user.getUserid(), fndlist.get(position).getObject_id().toString(), "1", SETLIKE);
                                } else {
                                    Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CHECKHADFAVORITE:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))) {
                                if (HttpConnect.isConnnected(getActivity())) {
                                    new StudyRequest(getActivity(), handler).DELLCOLLEXT(user.getUserid(), fndlist.get(position).getObject_id().toString(),
                                            "3", CANCELCOLLECT);
                                } else {
                                    Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                if (HttpConnect.isConnnected(getActivity())) {
                                    new StudyRequest(getActivity(), handler).COLLEXT(user.getUserid(), fndlist.get(position).getObject_id().toString(),
                                            "3", fndlist.get(position).getPost_title().toString(), fndlist.get(position).getPost_excerpt().toString(),
                                            SETCOLLECT);
                                } else {
                                    Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case RESETLIKE://取消赞图标
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                View v = getViewByPosition(position, lv_view);
                                TextView tv = (TextView) v.findViewById(R.id.tv_collect);
                                ImageView iv = (ImageView) v.findViewById(R.id.image_3);
                                iv.setBackgroundResource(R.mipmap.ic_like_gray);
                                if ("0".equals(tv.getText().toString())||tv.getText().toString().length() < 0){
                                    tv.setText("0");
                                }else{
                                    tv.setText(Integer.valueOf(tv.getText().toString()) - 1 + "");
                                }

                                Toast.makeText(getActivity(), "取消点赞", Toast.LENGTH_SHORT).show();
                                gomandata();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CANCELCOLLECT:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String data = obj.getString("data");
                            if ("success".equals(status)) {
                                View v = getViewByPosition(position, lv_view);
                                TextView tv = (TextView) v.findViewById(R.id.tv_tv_collect);
                                ImageView iv = (ImageView) v.findViewById(R.id.image_collect);
                                iv.setBackgroundResource(R.mipmap.ic_collect_nor);
                                if ("0".equals(tv.getText().toString())||tv.getText().toString().length() < 0){
                                    tv.setText("0");
                                }else{
                                    tv.setText(Integer.valueOf(tv.getText().toString()) - 1 + "");
                                }
                                Toast.makeText(getActivity(), "取消收藏", Toast.LENGTH_SHORT).show();
                                gomandata();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case SETLIKE://点赞变化图标
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                JSONObject jsonObject = new JSONObject(data);
                                View v = getViewByPosition(position,lv_view);
                                TextView tv = (TextView) v.findViewById(R.id.tv_collect);
                                ImageView iv = (ImageView) v.findViewById(R.id.image_3);
                                iv.setBackgroundResource(R.mipmap.ic_like_sel);
                                tv.setText(Integer.valueOf(tv.getText().toString()) + 1 + "");
                                Toast.makeText(getActivity(), "成功点赞", Toast.LENGTH_SHORT).show();
                                gomandata();
                                if (jsonObject.getString("score") != null &&jsonObject.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_score, null);
                                    dialog = new AlertDialog.Builder(getActivity()).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("+"+jsonObject.getString("score"));
                                    TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                    tv_score_name.setText(jsonObject.getString("event"));
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(1000);
                                                dialog.dismiss();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case SETCOLLECT:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String data = obj.getString("data");
                            if ("success".equals(status)) {
                                View v = getViewByPosition(position,lv_view);
                                TextView tv = (TextView) v.findViewById(R.id.tv_tv_collect);
                                ImageView iv = (ImageView) v.findViewById(R.id.image_collect);
                                iv.setBackgroundResource(R.mipmap.ic_collect_sel);
                                tv.setText(Integer.valueOf(tv.getText().toString()) + 1 + "");
                                Toast.makeText(getActivity(),"成功收藏", Toast.LENGTH_SHORT).show();
                                gomandata();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case ADDSCORE:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("data"))){
                                JSONObject json = new JSONObject(obj.getString("data"));
                                View layout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_score, null);
                                final Dialog dialog = new AlertDialog.Builder(getActivity()).create();
                                dialog.show();
                                dialog.getWindow().setContentView(layout);
                                TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                tv_score.setText("+" + json.getString("score"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }


        }
    };
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.nation_fragment_layout, null);
        user = new UserBean(getActivity());
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
            pagetype = bundle.getString("pagename");
        }

        topiciniview();
        return mView;
    }

    private void topiciniview() {
        shuaxin_button = (TextView) mView.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) mView.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) mView.findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        detail_loading = (TextView) mView.findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView) mView.findViewById(R.id.lv_comprehensive);
        pulllist.setPullLoadEnabled(true);
        pulllist.setScrollLoadEnabled(false);
        pulllist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                gomandata();
                stopRefresh();
            }



            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (fndlist.size()%20 != 0){
                    stopRefresh();
                    Toast.makeText(getActivity(),"无更新数据",Toast.LENGTH_SHORT).show();
                    return;
                }
                page = page+1;
                try{
                    getnewslistother();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        setLastData();
//        pulllist.doPullRefreshing(true, 500);
        lv_view = pulllist.getRefreshableView();
        lv_view.setDivider(null);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FirstPageNews.DataBean fndData = fndlist.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fndinfo", fndData);
                    bundle.putString("position", String.valueOf(position));
                    Intent intent = new Intent(getActivity(), News_WebView_study.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
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
    private void getnewslistother() {
        if (HttpConnect.isConnnected(getActivity())){
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            new StudyRequest(getActivity(), handler).getNewsListother(channelid,String.valueOf(page),FIRSTPAGELIST);
        }else{
            Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
            ril_shibai.setVisibility(View.VISIBLE);
            ril_list.setVisibility(View.GONE);
            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getnewslistother();
                }
            });
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        gomandata();
    }

    private void gomandata() {
        if (HttpConnect.isConnnected(getActivity())){
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            page = 1;
            new StudyRequest(getActivity(), handler).getNewsListother(channelid,String.valueOf(page),FIRSTPAGELIST);

        }else{
            Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
            ril_shibai.setVisibility(View.VISIBLE);
            ril_list.setVisibility(View.GONE);
            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gomandata();
                }
            });
        }
    }
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
