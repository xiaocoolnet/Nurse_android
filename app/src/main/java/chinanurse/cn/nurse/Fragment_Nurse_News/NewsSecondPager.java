package chinanurse.cn.nurse.Fragment_Nurse_News;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;
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
import chinanurse.cn.nurse.WebView.News_WebView;
import chinanurse.cn.nurse.adapter.NewsTitle_Adapter2;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.publicall.adapter.News_Down_Adapter;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * Created by Administrator on 2016/7/29.
 */
public class NewsSecondPager extends Fragment{
    private static final int FIRSTPAGELIST = 1;
    private static final int FIRTPAGETITLE = 2;
    private static final int FIRSTPAIMAGE = 4;
    private static final int ADDSCORE = 5;
    private View view;
    private int index;
    private String titletype,titlename,channelid,title_slide,title_image_name;
    private ArrayList<FirstPageNews.DataBean> fndlist = new ArrayList<>();
    private ArrayList<FirstPageNews.DataBean> fndlistlist = new ArrayList<>();

    private News_Down_Adapter lv_Adapter;
    //解析图片第三方初始化
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ArrayList<String> image_url = new ArrayList<>();
    private ArrayList<String> imagename = new ArrayList<>();
    private List<ImageView> imageList = new ArrayList<>();
    //    private News_Title_Adapter vp_Adapter;
    private NewsTitle_Adapter2 vp_Adapter;
    private PullToRefreshListView pulllist;
    private TextView detail_loading,news_first_title;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View viewH = null;
    private ListView lv_view;
    private ViewPager news_fisr_vp;
    private CirclePageIndicator circlePageIndicator;
    private  int currPage = 0;//当前显示的页
    private static final int CHANGIMAGE = 3;
    private int currentIndex;
    private Banner banner;
    private FirstPageNews.DataBean fnd;
    private FirstPageNews fndbean;
    private String[] images,imageTitle;
    private UserBean user;
    private ProgressDialog dialogpgd;
    private RelativeLayout ril_shibai,ril_list;
    private TextView shuaxin_button;
    private int page = 1;
    private SharedPreferences prefences;
    private String isopen;
    private String result;
    private Gson gson;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FIRSTPAGELIST://获取新闻列表数据
                    result = (String) msg.obj;
                    if (result != null) {
                        ril_shibai.setVisibility(View.GONE);
                        ril_list.setVisibility(View.VISIBLE);
                        if (page == 1) {
                            fndlist.clear();
                        }
//                        fndlist.clear();
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
                                initData();
                            }
                        });
                    }
                    break;
                case CHANGIMAGE:
                    if(currPage==1){
                        news_fisr_vp.setCurrentItem(imageList.size()-1, true);//带动画切换
                        news_fisr_vp.setCurrentItem(currPage, false);//带动画切换
                    }else {
                        news_fisr_vp.setCurrentItem(currPage, true);//带动画切换
                    }
                    break;
                case FIRSTPAIMAGE:
                    result = (String) msg.obj;

                    if (result != null) {
                        ril_shibai.setVisibility(View.GONE);
                        ril_list.setVisibility(View.VISIBLE);
                        fndlistlist.clear();
                        Gson gson = new Gson();
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
                case ADDSCORE:
                    if (msg.obj != null){
                        String resulttwo = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(resulttwo);
                            if ("success".equals(obj.optString("data"))){
                                JSONObject json = new JSONObject(obj.getString("data"));
                                View layout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_score, null);
                                final Dialog dialog = new AlertDialog.Builder(getActivity()).create();
                                dialog.show();
                                dialog.getWindow().setContentView(layout);
                                TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                tv_score.setText("+"+json.getString("score"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    //新写的图片轮播
    private void showImage(){
//        //不加这一句会提示：ImageLoader must be init with configuration before 且不显示图片
//        MyApplication.imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        Log.e("hello-----",fndlistlist.size()+"");
        if (fndlistlist.size() > 0&&fndlistlist.size() <= 5){
            images = new String[fndlistlist.size()];
            imageTitle = new String[fndlistlist.size()];
            for (int i = 0; i < fndlistlist.size(); i++) {
                Log.e("hello-----",fndlistlist.get(i).getThumb().get(0).getUrl());
                images[i]= NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlistlist.get(i).getThumb().get(0).getUrl();
            }
            for (int i = 0; i < fndlistlist.size(); i++) {
                imageTitle[i]=fndlistlist.get(i).getPost_title();
            }
        }else if (fndlistlist.size() > 5){
            images = new String[5];
            imageTitle = new String[5];
            for (int i = 0; i < 5; i++) {
                Log.e("hello-----",fndlistlist.get(i).getThumb().get(0).getUrl());
                images[i]= NetBaseConstant.NET_PIC_PREFIX_THUMB+fndlistlist.get(i).getThumb().get(0).getUrl();
            }
            for (int i = 0; i < 5; i++) {
                imageTitle[i]=fndlistlist.get(i).getPost_title();
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
                Intent intent = new Intent(getActivity(), News_WebView.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.firstlistview, null);
        user = new UserBean(getActivity());
        iniviewfirst();
        initData();
        return view;
    }

    private void iniviewfirst() {
        shuaxin_button = (TextView) view.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) view.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) view.findViewById(R.id.ril_list);
        dialogpgd=new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
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
        //获取当前时间
        setLastData();
//        pulllist.doPullRefreshing(true, 500);
        lv_view = pulllist.getRefreshableView();
        lv_view.setDivider(null);
        viewH = LayoutInflater.from(getActivity()).inflate(R.layout.firstpagenew, null);
        lv_view.addHeaderView(viewH);
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FirstPageNews.DataBean fndData = fndlist.get(position-1);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fndinfo", fndData);
                    bundle.putString("position", String.valueOf(position));
                    Intent intent = new Intent(getActivity(), News_WebView.class);
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
    //    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewactivity();
    }

    private void initViewactivity() {
        banner = (Banner) viewH.findViewById(R.id.banner1);//新 图片轮播
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.i("onResume", "---------->onResume");
//
//    }
private void getnewslistother() {
    if (HttpConnect.isConnnected(getActivity())) {
        dialogpgd.setMessage("正在加载...");
        dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogpgd.show();
        Log.i("onResume", "initData1");
        new StudyRequest(getActivity(), handler).getNewsListother("5", String.valueOf(page), FIRSTPAGELIST);
    } else {
        Log.i("onResume", "initData2");
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
    public void initData() {
        if (HttpConnect.isConnnected(getActivity())) { dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();

            Log.i("onResume", "initData1");
            page = 1;
            new StudyRequest(getActivity(), handler).getNewsListother("5", String.valueOf(page), FIRSTPAGELIST);
            new StudyRequest(getActivity(), handler).getNewsList("109",FIRSTPAIMAGE);
        } else {
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
