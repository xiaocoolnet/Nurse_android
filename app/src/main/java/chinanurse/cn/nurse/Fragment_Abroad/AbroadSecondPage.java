package chinanurse.cn.nurse.Fragment_Abroad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.GoAbroad_chest_WebView;
import chinanurse.cn.nurse.Fragment_News.activity.NewsWebViewActivity;
import chinanurse.cn.nurse.publicall.adapter.News_Down_Adapter;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

public class AbroadSecondPage extends Fragment implements View.OnClickListener{
    private ListView lv_view;
    private News_Down_Adapter lv_Adapter;
    private static final int SECONDPAGELIST = 4;
    public static final int BTNCHECKHADFAVORITE = 10;
    public static final int RESETLIKE = 5;
    public static final int SETLIKE = 6;
    private static final int SETCOLLECT = 7;
    private static final int CANCELCOLLECT = 8;
    private static final int CHECKHADFAVORITE = 9;
    private String channelid = "9", title_slide, title_image_name;

    private ArrayList<FirstPageNews.DataBean> fndlist = new ArrayList<>();
    private RelativeLayout online_translation, exchange_rate_query, time_difference_query, degree_certificate,
            the_weather_query, map_query, ticket_query, hotel_reservation, visa_query;
    private Intent intent;

    private TextView detail_loading, news_first_title, detail_loadingview;
    private PullToRefreshListView pulllist;
    private View viewH = null;
    private View view;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private int position;
    private FirstPageNews fndbean;
    private UserBean user;
    private RelativeLayout ril_shibai,ril_list;
    private TextView shuaxin_button;
    private ProgressDialog dialogpgd;
    private Dialog dialog;
    private int page = 1;
    private String result;
    private Gson gson;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SECONDPAGELIST:
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
                                    lv_Adapter = new News_Down_Adapter(getActivity(), fndlist, 2, handler);
                                    lv_view.setAdapter(lv_Adapter);
                                }else {
                                    if (page == 1) {
                                        lv_Adapter = new News_Down_Adapter(getActivity(), fndlist, 2, handler);
                                        lv_view.setAdapter(lv_Adapter);
                                    }else {
                                        lv_Adapter.notifyDataSetChanged();
                                    }
                                }
                                stopRefresh();
                            }else{
                                if ("end".equals(data)){
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
                                initData();
                            }
                        });
                    }
                    break;
                case 0:
                    position = (int) msg.obj;
                    if (HttpConnect.isConnnected(getActivity())) {
                        new StudyRequest(getActivity(), handler).CheckHadLike(user.getUserid(), fndlist.get(position).getObject_id(), "1", BTNCHECKHADFAVORITE);
                    } else {
                        Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
//                    initData();
//                    flv_Adapter.notifyDataSetChanged();
                    break;
                case 1:
                    position = (int) msg.obj;
                    if (HttpConnect.isConnnected(getActivity())) {
                        new StudyRequest(getActivity(), handler).CheckHadFavorite(user.getUserid(), fndlist.get(position).getObject_id(), "1", CHECKHADFAVORITE);
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
                                    Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
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
                                            "1", CANCELCOLLECT);
                                } else {
                                    Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                if (HttpConnect.isConnnected(getActivity())) {
                                    new StudyRequest(getActivity(), handler).COLLEXT(user.getUserid(), fndlist.get(position).getObject_id().toString(),
                                            "1", fndlist.get(position).getPost_title().toString(), fndlist.get(position).getPost_excerpt().toString(),
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
                                View v = getViewByPosition(position,lv_view);
                                TextView tv = (TextView) v.findViewById(R.id.news_fourth_like);
                                ImageView iv = (ImageView) v.findViewById(R.id.four_image_like);
                                iv.setBackgroundResource(R.mipmap.ic_like_gray);
                                if ("0".equals(tv.getText().toString())||tv.getText().toString().length() < 0){
                                    tv.setText("0");
                                }else{
                                    tv.setText(Integer.valueOf(tv.getText().toString()) - 1 + "");
                                }

                                Toast.makeText(getActivity(), "取消点赞", Toast.LENGTH_SHORT).show();
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
                                TextView tv = (TextView) v.findViewById(R.id.news_fourth_collect);
                                ImageView iv = (ImageView) v.findViewById(R.id.four_image_collect);
                                iv.setBackgroundResource(R.mipmap.ic_collect_nor);
                                if ("0".equals(tv.getText().toString())||tv.getText().toString().length() < 0){
                                    tv.setText("0");
                                }else{
                                    tv.setText(Integer.valueOf(tv.getText().toString()) - 1 + "");
                                }
                                Toast.makeText(getActivity(), "取消收藏", Toast.LENGTH_SHORT).show();
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
                                TextView tv = (TextView) v.findViewById(R.id.news_fourth_like);
                                ImageView iv = (ImageView) v.findViewById(R.id.four_image_like);
                                iv.setBackgroundResource(R.mipmap.ic_like_sel);
                                tv.setText(Integer.valueOf(tv.getText().toString()) + 1 + "");
                                Toast.makeText(getActivity(), "点赞成功", Toast.LENGTH_SHORT).show();
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
                                                Thread.sleep(3000);
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
                                TextView tv = (TextView) v.findViewById(R.id.news_fourth_collect);
                                ImageView iv = (ImageView) v.findViewById(R.id.four_image_collect);
                                iv.setBackgroundResource(R.mipmap.ic_collect_sel);
                                tv.setText(Integer.valueOf(tv.getText().toString()) + 1 + "");
                                Toast.makeText(getActivity(), "成功收藏", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    //    public AbroadSecondPage(Activity activity) {
//        super(activity);
//
//
//    }
//    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initgotwoactivity();
    }

    private void initgotwoactivity() {
        //第一个在线翻译
        online_translation = (RelativeLayout) viewH.findViewById(R.id.goabroad_Online_translation);
        online_translation.setOnClickListener(this);
        //第二个汇率查询
        exchange_rate_query = (RelativeLayout) viewH.findViewById(R.id.goabroad_Exchange_rate_query);
        exchange_rate_query.setOnClickListener(this);
        //第三个时差查询
        time_difference_query = (RelativeLayout) viewH.findViewById(R.id.goabroad_the_time_difference_query);
        time_difference_query.setOnClickListener(this);
        //第四个学历认证
        degree_certificate = (RelativeLayout) viewH.findViewById(R.id.goabroad_degree_certificate);
        degree_certificate.setOnClickListener(this);
        //第五个天气查询
        the_weather_query = (RelativeLayout) viewH.findViewById(R.id.gobroad_the_weather_query);
        the_weather_query.setOnClickListener(this);
        //第六个地图查询
        map_query = (RelativeLayout) viewH.findViewById(R.id.gobroad_map_query);
        map_query.setOnClickListener(this);
        //第七个机票查询
        ticket_query = (RelativeLayout) viewH.findViewById(R.id.goabroad_ticket_query);
        ticket_query.setOnClickListener(this);
        //第八个酒店预订
        hotel_reservation = (RelativeLayout) viewH.findViewById(R.id.goabroad_hotel_reservation);
        hotel_reservation.setOnClickListener(this);
        //第九个签证查询
        visa_query = (RelativeLayout) viewH.findViewById(R.id.goabroad_visa_query);
        visa_query.setVisibility(View.GONE);
        visa_query.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.abroad_first_listview, null);
        user = new UserBean(getActivity());
        secondiniview();
        Log.e("1111", "AbroadFirstPage  initView");
        return view;
    }

    private void secondiniview() {
        shuaxin_button = (TextView) view.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) view.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) view.findViewById(R.id.ril_list);
        dialogpgd=new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
//        detail_loadingview = (TextView) view.findViewById(R.id.detail_loading_view);
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
                if (fndlist.size()%20 != 0){
                    stopRefresh();
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
        viewH = LayoutInflater.from(getActivity()).inflate(R.layout.go_abroad_three, null);
        lv_view.addHeaderView(viewH);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FirstPageNews.DataBean fndData = fndlist.get(position - 1);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fndinfo", fndData);
                    bundle.putString("position", String.valueOf(position));
                    Intent intent = new Intent(getActivity(), NewsWebViewActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                }
        });
    }

    private void getnewslistother() {
        if (HttpConnect.isConnnected(getActivity())) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            Log.i("1111", "StudyRequest");
            if (user.getUserid() != null && user.getUserid().length() > 0) {
                new StudyRequest(getActivity(), handler).getNewsListtwo(channelid, String.valueOf(page),user.getUserid(),"1", SECONDPAGELIST);
            }else{
                new StudyRequest(getActivity(), handler).getNewsListtwo(channelid, String.valueOf(page),"","", SECONDPAGELIST);
            }
        } else {
            Log.i("1111", "StudyRequest");
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
        Log.i("1111", "---------->onResume");
        StatService.onPageStart(getActivity(), "护士培训");
        initData();
    }
    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "护士培训");
    }
    public void initData() {
        if (HttpConnect.isConnnected(getActivity())) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            Log.i("1111", "StudyRequest");
            page = 1;
            if (user.getUserid() != null && user.getUserid().length() > 0) {
                new StudyRequest(getActivity(), handler).getNewsListtwo(channelid, String.valueOf(page),user.getUserid(),"1", SECONDPAGELIST);
            }else{
                new StudyRequest(getActivity(), handler).getNewsListtwo(channelid, String.valueOf(page),"","", SECONDPAGELIST);
            }
        } else {
            Log.i("1111", "StudyRequest");
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
            //在线翻译
            case R.id.goabroad_Online_translation:
                intent = new Intent(getActivity(), GoAbroad_chest_WebView.class);
                intent.putExtra("chesttype", "1");
                getActivity().startActivity(intent);
                break;
            //汇率查询
            case R.id.goabroad_Exchange_rate_query:
                intent = new Intent(getActivity(), GoAbroad_chest_WebView.class);
                intent.putExtra("chesttype", "2");
                getActivity().startActivity(intent);
                break;
            //时差查询
            case R.id.goabroad_the_time_difference_query:
                intent = new Intent(getActivity(), GoAbroad_chest_WebView.class);
                intent.putExtra("chesttype", "3");
                getActivity().startActivity(intent);
                break;
            //学历认证
            case R.id.goabroad_degree_certificate:
                intent = new Intent(getActivity(), GoAbroad_chest_WebView.class);
                intent.putExtra("chesttype", "4");
                getActivity().startActivity(intent);
                break;
            //天气查询
            case R.id.gobroad_the_weather_query:
                intent = new Intent(getActivity(), GoAbroad_chest_WebView.class);
                intent.putExtra("chesttype", "5");
                getActivity().startActivity(intent);
                break;
            //地图查询
            case R.id.gobroad_map_query:
                intent = new Intent(getActivity(), GoAbroad_chest_WebView.class);
                intent.putExtra("chesttype", "6");
                getActivity().startActivity(intent);
                break;
            //机票查询
            case R.id.goabroad_ticket_query:
                intent = new Intent(getActivity(), GoAbroad_chest_WebView.class);
                intent.putExtra("chesttype", "7");
                getActivity().startActivity(intent);
                break;
            //酒店预订
            case R.id.goabroad_hotel_reservation:
                intent = new Intent(getActivity(), GoAbroad_chest_WebView.class);
                intent.putExtra("chesttype", "8");
                getActivity().startActivity(intent);
                break;
            //签证查询
            case R.id.goabroad_visa_query:
                intent = new Intent(getActivity(), GoAbroad_chest_WebView.class);
                intent.putExtra("chesttype", "9");
                getActivity().startActivity(intent);
                break;
        }
    }
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition() - 1;
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}