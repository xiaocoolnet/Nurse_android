package chinanurse.cn.nurse.Fragment_Abroad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.Fragment_News.activity.NewsWebViewActivity;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.publicall.adapter.News_Down_Adapter;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.Fragment_Abroad.Abroad_Firstpage.Abroad_first_abroad;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

public class AbroadFirstPage extends Fragment implements View.OnClickListener {

    private static final int FIRSTPAGEIMAGE = 6;
    //    View view;
    private RelativeLayout re_country_01, re_country_02, re_country_03, re_country_04, re_country_05, re_country_06, re_country_07, re_country_08;
//    private ListView abroad_first_listview;
    private News_Down_Adapter lv_Adapter;
    private static final int FIRSTPAGELIST = 4;
    private String channelid = "8";
    private String country,abroadtype,result;
    private ArrayList<FirstPageNews.DataBean> fndlist = new ArrayList<>();
    private ArrayList<FirstPageNews.DataBean> fndlistlist = new ArrayList<>();
    private List<String> imagename = new ArrayList<>();
    private TextView detail_loading;
    private String title_image_name,title_slide,titlename;
    //解析图片第三方初始化
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private Activity mactivity;
    private ListView lv_view;
    private Intent intent;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private PullToRefreshListView pulllist;
    private View viewH = null;
    private View mview;
    private Banner banner;
    private Gson gson;
    private FirstPageNews fndbean;
    private FirstPageNews.DataBean fnd;
    private String[] images, imageTitle;
    private UserBean user;
    private RelativeLayout ril_shibai,ril_list;
    private TextView shuaxin_button;
    private ProgressDialog dialogpgd;
    private int page = 1;
    private SharedPreferences prefences;
    private String isopen;


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
                                    lv_Adapter = new News_Down_Adapter(getActivity(), fndlist, 0, handler);
                                    lv_view.setAdapter(lv_Adapter);
                                }else {
                                    if (page == 1) {
                                        lv_Adapter = new News_Down_Adapter(getActivity(), fndlist, 0, handler);
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

                case FIRSTPAGEIMAGE:
                    result = (String) msg.obj;
                    if (result != null) {
                        ril_shibai.setVisibility(View.GONE);
                        ril_list.setVisibility(View.VISIBLE);
                        fndlistlist.clear();
                        gson = new Gson();
                        fndbean = gson.fromJson(result, FirstPageNews.class);
                        fndlistlist.addAll(fndbean.getData());
                        showImage();
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


    //新写的图片轮播
    private void showImage(){
        if (fndlistlist.size() > 0 && fndlistlist.size() <= 5) {
            images = new String[fndlistlist.size()];
            imageTitle = new String[fndlistlist.size()];
            for (int i = 0; i < fndlistlist.size(); i++) {
                Log.e("hello-----", fndlistlist.get(i).getThumb().get(0).getUrl());
                images[i] = NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlistlist.get(i).getThumb().get(0).getUrl();
            }
            for (int i = 0; i < fndlistlist.size(); i++) {
                imageTitle[i] = fndlistlist.get(i).getPost_title();
            }
        } else if (fndlistlist.size() > 5) {
            images = new String[5];
            imageTitle = new String[5];
            for (int i = 0; i < 5; i++) {
                Log.e("hello-----", fndlistlist.get(i).getThumb().get(0).getUrl());
                images[i] = NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlistlist.get(i).getThumb().get(0).getUrl();
            }
            for (int i = 0; i < 5; i++) {
                imageTitle[i] = fndlistlist.get(i).getPost_title();
            }
        }
        switch (4){
            case 0:
                break;
            case 1:
                //设置样式 显示圆形指示器
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                break;
            case 2:
                //显示数字指示器
                banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
                break;
            case 3:
                //显示数字指示器和标题
                banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
                //记得设置标题列表哦
//                banner.setBannerTitle(titles);
                break;
            case 4:
                //显示圆形指示器和标题
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                //记得设置标题列表哦
                banner.setBannerTitle(imageTitle);
                break;
            case 5:
                //设置指示器居中（CIRCLE_INDICATOR或者CIRCLE_INDICATOR_TITLE模式下）
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                banner.setIndicatorGravity(BannerConfig.CENTER);
                break;
        }
        prefences=getActivity().getSharedPreferences("nursenum", Context.MODE_PRIVATE);
        isopen=prefences.getString("isopen",null);
        banner.setImages(images,isopen);//可以选择设置图片网址，或者资源文件，默认用Glide加载
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {
            @Override
            public void OnBannerClick(View view, int position) {
                fnd = fndlistlist.get(position-1);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fndinfo", fnd);
                Intent intent = new Intent(getActivity(), NewsWebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewactivity();
    }

    private void initViewactivity() {
        re_country_01 = (RelativeLayout) mview.findViewById(R.id.re_country_01);
        re_country_01.setOnClickListener(this);
        re_country_02 = (RelativeLayout) mview.findViewById(R.id.re_country_02);
        re_country_02.setOnClickListener(this);
        re_country_03 = (RelativeLayout) mview.findViewById(R.id.re_country_03);
        re_country_03.setOnClickListener(this);
        re_country_04 = (RelativeLayout) mview.findViewById(R.id.re_country_04);
        re_country_04.setOnClickListener(this);
        re_country_05 = (RelativeLayout) mview.findViewById(R.id.re_country_05);
        re_country_05.setOnClickListener(this);
        re_country_06 = (RelativeLayout) mview.findViewById(R.id.re_country_06);
        re_country_06.setOnClickListener(this);
        re_country_07 = (RelativeLayout) mview.findViewById(R.id.re_country_07);
        re_country_07.setOnClickListener(this);
        re_country_08 = (RelativeLayout) mview.findViewById(R.id.re_country_08);
        re_country_08.setOnClickListener(this);

    }

    @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        mactivity = getActivity();
        mview = View.inflate(mactivity, R.layout.abroad_first_listview, null);
        user = new UserBean(getActivity());
        abroadiniview();
        Log.e("1111", "AbroadFirstPage  initView");
        return mview;
    }

    private void abroadiniview() {
        shuaxin_button = (TextView) mview.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) mview.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) mview.findViewById(R.id.ril_list);
        dialogpgd=new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
        detail_loading = (TextView) mview.findViewById(R.id.detail_loading);
        pulllist = (PullToRefreshListView) mview.findViewById(R.id.lv_comprehensive);
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
        viewH = LayoutInflater.from(getActivity()).inflate(R.layout.abroad_first, null);
        lv_view.addHeaderView(viewH);
        banner = (Banner) viewH.findViewById(R.id.banner1);//新 图片轮播
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FirstPageNews.DataBean fndData = fndlist.get(position-1);
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
        if (HttpConnect.isConnnected(mactivity)) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            Log.i("1111", "StudyRequest");
            new StudyRequest(mactivity, handler).getNewsListother(channelid,String.valueOf(page), FIRSTPAGELIST);
        } else {
            Log.i("1111", "StudyRequest");
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
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
        Log.i("1111", "---------->onResume");
        StatService.onPageStart(getActivity(), "护士出国");
        initData();
    }
    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "护士出国");
    }
    public void initData() {

        if (HttpConnect.isConnnected(mactivity)) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            Log.i("1111", "StudyRequest");
            page = 1;
            new StudyRequest(mactivity, handler).getNewsListother(channelid,String.valueOf(page), FIRSTPAGELIST);
            new StudyRequest(mactivity, handler).getNewsList("112",FIRSTPAGEIMAGE);
        } else {
            Log.i("1111", "StudyRequest");
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            ril_shibai.setVisibility(View.VISIBLE);
            ril_list.setVisibility(View.GONE);
            shuaxin_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initData();
                }
            });
        }
//        if (HttpConnect.isConnnected(mactivity)) {
//            Log.i("onResume", "initData3");
//            title_slide = "7";
//            new StudyRequest(mactivity, handler).getHttpImage(title_slide, FIRTPAGETITLE);
//        } else {
//            Log.i("onResume", "initData4");
//            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_country_01:
                country = "美国";
                abroadtype = "17";
                title_slide = "113";
                Log.i("============>", "---------->美国");
                intent = new Intent(mactivity, Abroad_first_abroad.class);
                intent.putExtra("abroadtype",abroadtype);
                intent.putExtra("country",country);
                intent.putExtra("title_slide",title_slide);
                mactivity.startActivity(intent);
                break;
            case R.id.re_country_02:
                country = "加拿大";
                abroadtype = "18";
                title_slide = "114";
                intent = new Intent(mactivity, Abroad_first_abroad.class);
                intent.putExtra("abroadtype",abroadtype);
                intent.putExtra("country",country);
                intent.putExtra("title_slide",title_slide);
                mactivity.startActivity(intent);
                break;
            case R.id.re_country_03:
                country = "德国";
                abroadtype = "19";
                title_slide = "115";
                intent = new Intent(mactivity, Abroad_first_abroad.class);
                intent.putExtra("abroadtype",abroadtype);
                intent.putExtra("country",country);
                intent.putExtra("title_slide",title_slide);
                mactivity.startActivity(intent);
                break;
            case R.id.re_country_04:
                country = "芬兰";
                abroadtype = "20";
                title_slide = "116";
                intent = new Intent(mactivity, Abroad_first_abroad.class);
                intent.putExtra("abroadtype",abroadtype);
                intent.putExtra("country",country);
                intent.putExtra("title_slide",title_slide);
                mactivity.startActivity(intent);
                break;
            case R.id.re_country_05:
                country = "澳洲";
                abroadtype = "21";
                title_slide = "117";
                intent = new Intent(mactivity, Abroad_first_abroad.class);
                intent.putExtra("abroadtype",abroadtype);
                intent.putExtra("country",country);
                intent.putExtra("title_slide",title_slide);
                mactivity.startActivity(intent);
                break;
            case R.id.re_country_06:
                country = "日本";
                abroadtype = "22";
                title_slide = "120";
                intent = new Intent(mactivity, Abroad_first_abroad.class);
                intent.putExtra("abroadtype",abroadtype);
                intent.putExtra("country",country);
                intent.putExtra("title_slide",title_slide);
                mactivity.startActivity(intent);
                break;
            case R.id.re_country_07:
                country = "新加坡";
                abroadtype = "23";
                title_slide = "118";
                intent = new Intent(mactivity, Abroad_first_abroad.class);
                intent.putExtra("abroadtype",abroadtype);
                intent.putExtra("country",country);
                intent.putExtra("title_slide",title_slide);
                mactivity.startActivity(intent);
                break;
            case R.id.re_country_08:
                country = "沙特";
                abroadtype = "24";
                title_slide = "119";
                intent = new Intent(mactivity, Abroad_first_abroad.class);
                intent.putExtra("abroadtype",abroadtype);
                intent.putExtra("country",country);
                intent.putExtra("title_slide",title_slide);
                mactivity.startActivity(intent);
                break;
        }

    }
}