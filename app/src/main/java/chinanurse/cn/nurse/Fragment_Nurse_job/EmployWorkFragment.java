package chinanurse.cn.nurse.Fragment_Nurse_job;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.FragmentTag;
import chinanurse.cn.nurse.Fragment_Nurse.EmployFragment;
import chinanurse.cn.nurse.Fragment_Nurse_job.adapter.Bean_list;
import chinanurse.cn.nurse.Fragment_Nurse_job.adapter.NurseEmployAdapter;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.NetUtil;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.MainActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.Fragment_News.activity.NewsWebViewActivity;
import chinanurse.cn.nurse.adapter.News_Title_Adapter;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.bean.MineResumeinfo;
import chinanurse.cn.nurse.bean.NurseEmployBean;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.dao.CommunalInterfaces;
import chinanurse.cn.nurse.pnlllist.PullToRefreshBase;
import chinanurse.cn.nurse.pnlllist.PullToRefreshListView;

/**
 * 找工作
 * Created by wzh on 2016/6/26.
 */
public class EmployWorkFragment extends Fragment implements View.OnClickListener {
    private static final int FIRTPAGETITLE = 2;
    private static final int FIRSTPAGEIMAGE = 3;
    private static final int GETRESUMEINFO = 4;
    private static final int APPLYjOB = 6;

    private ImageButton imagebutton_bi;
    private NurseEmployAdapter nurseEmployAdapter;
    private List<NurseEmployBean.DataBean> nurseEmployDataList;
    private ArrayList<FirstPageNews.DataBean> fndlistlist = new ArrayList<>();
    private FirstPageNews fndbean;
    private FirstPageNews.DataBean fnd;

    private View mView;
    private ListView lv_view;
    private RelativeLayout ril_shibai, ril_list;
    private TextView shuaxin_button, detail_loading_nonum;
    private ProgressDialog dialogpgd;

    private TextView detail_loading;
    private PullToRefreshListView pulllist;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private View viewH;

    private News_Title_Adapter vp_Adapter;
    //解析图片第三方初始化
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean isStop;
    private UserBean user;
    private Banner banner;
    private String[] images;
    private String[] imageTitle;
    private int position;
    private MineResumeinfo.DataBean mrinfo;
    private String result;
    private Bundle savedInstanceState;
    private FragmentTag mCurrentTag;
    private Fragment mCurrentFragment;
    private EmployFragment cf;
    private SharedPreferences prefences;
    private String isopen;
    private int pager = 1;
    private MainActivity activity;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CommunalInterfaces.EMPLOY_LIST:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    if (jsonObject != null) {
                        ril_shibai.setVisibility(View.GONE);
                        ril_list.setVisibility(View.VISIBLE);
                        try {
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                int length = jsonArray.length();
                                if (pager == 1) {
                                    nurseEmployDataList.clear();
                                }
                                JSONObject itemObject;
                                for (int i = 0; i < length; i++) {
                                    itemObject = (JSONObject) jsonArray.get(i);
                                    NurseEmployBean.DataBean nurseEmployData = new NurseEmployBean.DataBean();
                                    nurseEmployData.setId(itemObject.getString("id"));
                                    nurseEmployData.setCompanyid(itemObject.getString("companyid"));
                                    nurseEmployData.setCompanyname(itemObject.getString("companyname"));
                                    nurseEmployData.setCompanyinfo(itemObject.getString("companyinfo"));
                                    nurseEmployData.setPhoto(itemObject.getString("photo"));
                                    nurseEmployData.setTitle(itemObject.getString("title"));
                                    nurseEmployData.setJobtype(itemObject.getString("jobtype"));
                                    nurseEmployData.setExperience(itemObject.getString("experience"));
                                    nurseEmployData.setEducation(itemObject.getString("education"));
                                    nurseEmployData.setCertificate(itemObject.getString("certificate"));
                                    nurseEmployData.setAddress(itemObject.getString("address"));
                                    nurseEmployData.setSalary(itemObject.getString("salary"));
                                    nurseEmployData.setWelfare(itemObject.getString("welfare"));
                                    nurseEmployData.setCount(itemObject.getString("count"));
                                    nurseEmployData.setDescription(itemObject.getString("description"));
                                    nurseEmployData.setLinkman(itemObject.getString("linkman"));
                                    nurseEmployData.setPhone(itemObject.getString("phone"));
                                    nurseEmployData.setEmail(itemObject.getString("email"));
                                    nurseEmployData.setCreate_time(itemObject.getString("create_time"));
                                    nurseEmployData.setHits(itemObject.getInt("hits"));

                                    nurseEmployDataList.add(nurseEmployData);
                                }
                                if (nurseEmployAdapter==null){
                                    nurseEmployAdapter = new NurseEmployAdapter(getActivity(), nurseEmployDataList, user.getUserid(), user.getUsertype(), handler, 1);
                                    lv_view.setAdapter(nurseEmployAdapter);
                                    dialogpgd.dismiss();
                                }else {
                                    if (pager == 1) {
                                        nurseEmployAdapter = new NurseEmployAdapter(getActivity(), nurseEmployDataList, user.getUserid(), user.getUsertype(), handler, 1);
                                        lv_view.setAdapter(nurseEmployAdapter);
                                        dialogpgd.dismiss();
                                    }else {
                                        nurseEmployAdapter.notifyDataSetChanged();
                                        dialogpgd.dismiss();
                                    }
                                }
                                stopRefresh();
                            } else {
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
                            dialogpgd.dismiss();
                            e.printStackTrace();
                        }
                    } else {
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
                        Gson gson = new Gson();
                        fndbean = gson.fromJson(result, FirstPageNews.class);
                        fndlistlist.addAll(fndbean.getData());
                        showImage();
                        dialogpgd.dismiss();
                    } else {
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
                case 5:
                    position = (int) msg.obj;
                    if (HttpConnect.isConnnected(getActivity())) {
                        dialogpgd.setMessage("正在投递...");
                        dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialogpgd.show();
                        new StudyRequest(getActivity(), handler).getResumeInfo(user.getUserid(), GETRESUMEINFO);
                    } else {
                        Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case GETRESUMEINFO:
                    if (msg.obj != null) {
                        result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String data = json.getString("data");
                            if ("success".equals(json.optString("status"))) {

                                    JSONObject obj = new JSONObject(data);
                                    mrinfo = new MineResumeinfo.DataBean();
                                    mrinfo.setId(obj.getString("id"));
                                    mrinfo.setUserid(obj.getString("userid"));
                                    mrinfo.setName(obj.getString("name"));
                                    mrinfo.setSex(obj.getString("sex"));
                                    mrinfo.setAvatar(obj.getString("avatar"));
                                    mrinfo.setBirthday(obj.getString("birthday"));
                                    mrinfo.setExperience(obj.getString("experience"));
                                    mrinfo.setEducation(obj.getString("education"));
                                    mrinfo.setCertificate(obj.getString("certificate"));
                                    mrinfo.setWantposition(obj.getString("wantposition"));
                                    mrinfo.setTitle(obj.getString("title"));
                                    mrinfo.setAddress(obj.getString("address"));
                                    mrinfo.setCurrentsalary(obj.getString("currentsalary"));
                                    mrinfo.setJobstate(obj.getString("jobstate"));
                                    mrinfo.setDescription(obj.getString("description"));
                                    mrinfo.setEmail(obj.getString("email"));
                                    mrinfo.setPhone(obj.getString("phone"));
                                    mrinfo.setHiredate(obj.getString("hiredate"));
                                    mrinfo.setWantcity(obj.getString("wantcity"));
                                    if (HttpConnect.isConnnected(getActivity())) {
                                        new StudyRequest(getActivity(), handler).ApplyJob_judge(user.getUserid(), nurseEmployDataList.get(position).getCompanyid(), nurseEmployDataList.get(position).getId(), APPLYjOB);
                                    } else {
                                        Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                    }
                            }else{
                                dialogpgd.dismiss();
                                new AlertDialog.Builder(getActivity()).setTitle("系统提示").setMessage("请填写简历")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(getActivity(), Add_EmployWork_Fragment.class);
                                                getActivity().startActivity(intent);
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).create().show();
                            }
                        } catch (JSONException e) {
                            dialogpgd.dismiss();
                            e.printStackTrace();
                        }

                    }else{
                        dialogpgd.dismiss();
                        Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case APPLYjOB:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".endsWith(json.optString("status"))) {
                                String data = json.getString("data");
                                if ("1".equals(data)) {
                                    dialogpgd.dismiss();
                                    Toast.makeText(getActivity(), "您已经投递过该公司", Toast.LENGTH_SHORT).show();
                                } else if ("0".equals(data)) {
                                    if (HttpConnect.isConnnected(getActivity())) {
                                        new StudyRequest(getActivity(), handler).send_ApplyJob(nurseEmployDataList.get(position).getCompanyid(), nurseEmployDataList.get(position).getId(), user.getUserid());
                                    } else {
                                        Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                dialogpgd.dismiss();
                            }
                        } catch (JSONException e) {
                            dialogpgd.dismiss();
                            e.printStackTrace();
                        }
                    }else{
                        dialogpgd.dismiss();
                        Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CommunalInterfaces.APPLYJOB:
                    JSONObject json = (JSONObject) msg.obj;
                    if (json != null) {
                        try {
                            String status = json.getString("status");
                            Log.e("data", status);
                            if (status.equals("success")) {
                                JSONObject obj = new JSONObject(json.getString("data"));
                                Log.e("data", "666666");
                                Toast.makeText(getActivity(), "投递成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "投递失败", Toast.LENGTH_SHORT).show();
                                Log.e("data", "77777");
                            }
                            dialogpgd.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        dialogpgd.dismiss();
                        Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };

    //新写的图片轮播
    private void showImage() {

        Log.e("hello-----", fndlistlist.size() + "");
        if (fndlistlist.size() > 0 && fndlistlist.size() <= 5) {
            images = new String[fndlistlist.size()];
            imageTitle = new String[fndlistlist.size()];
            for (int i = 0; i < fndlistlist.size(); i++) {
                Log.e("hello-----", fndlistlist.get(i).getThumb().get(0).getUrl());
                images[i] = NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlistlist.get(i).getThumb().get(0).getUrl();
            }
            for (int i = 0; i < fndlistlist.size(); i++) {
                imageTitle[i] = fndlistlist.get(i).getPost_title();
            }
        } else if (fndlistlist.size() > 5) {
            images = new String[5];
            imageTitle = new String[5];
            for (int i = 0; i < 5; i++) {
                Log.e("hello-----", fndlistlist.get(i).getThumb().get(0).getUrl());
                images[i] = NetBaseConstant.NET_PIC_PREFIX_THUMB + fndlistlist.get(i).getThumb().get(0).getUrl();
            }
            for (int i = 0; i < 5; i++) {
                imageTitle[i] = fndlistlist.get(i).getPost_title();
            }
        }
        switch (4) {
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
                fnd = fndlistlist.get(position - 1);
                Log.e("obj_id", "---------->" + fnd);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fndinfo", fnd);
                Intent intent = new Intent(getActivity(), NewsWebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        user = new UserBean(getActivity());
        workviewpager();


    }

    private void workviewpager() {
        banner = (Banner) viewH.findViewById(R.id.banner1);//新 图片轮播

    }

    private void setOnItemClick() {
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public static final String SEARCHPROJECT = "";

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NurseEmployBean.DataBean nurseEmployData = nurseEmployDataList.get(position - 1);
                Bean_list.Bean_ist.clear();
                Bean_list.Bean_ist.add(nurseEmployData);
                activity = (MainActivity) getActivity();
                activity.switchFragmentAddHide(FragmentTag.TAG_DETAILWORK);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.employ_work_fragment, null);
        user = new UserBean(getActivity());
        workiniview();
        setOnItemClick();
        return mView;
    }

    private void workiniview() {
        detail_loading_nonum = (TextView) mView.findViewById(R.id.detail_loading_nonum);
        nurseEmployDataList = new ArrayList<>();
        shuaxin_button = (TextView) mView.findViewById(R.id.shuaxin_button);
        ril_shibai = (RelativeLayout) mView.findViewById(R.id.ril_shibai);
        ril_list = (RelativeLayout) mView.findViewById(R.id.ril_list);
        dialogpgd = new ProgressDialog(getActivity(), android.app.AlertDialog.THEME_HOLO_LIGHT);
        dialogpgd.setCancelable(false);
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
                if (nurseEmployDataList.size()%20 != 0){
                    stopRefresh();
                    return;
                }
                pager = pager+1;
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
        viewH = LayoutInflater.from(getActivity()).inflate(R.layout.firstpagenew, null);
        lv_view.addHeaderView(viewH);
        initData();
    }

    private void getnewslistother() {
        if (NetUtil.isConnnected(getActivity())) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            new StudyRequest(getActivity(), handler).employList(String.valueOf(pager));
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
        StatService.onPageStart(getActivity(), "公司招聘列表");
        Log.i("onResume", "---------->onResume");
        initData();
    }
    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "公司招聘列表");
    }
    public void initData() {

//        if (user.getUserid() == null || user.getUserid().length() <= 0) {
//            //网络请求
//            if (NetUtil.isConnnected(getActivity())) {
//
//                title_slide = "7";
//                new StudyRequest(getActivity(), handler).employList();
//                //暂时用新闻的接口测试一下
//                new StudyRequest(getActivity(), handler).getHttpImage(title_slide, FIRTPAGETITLE);
//            }
//        } else {
        //网络请求
        if (NetUtil.isConnnected(getActivity())) {
            dialogpgd.setMessage("正在加载...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            //暂时用新闻的接口测试一下
            pager = 1;
//                new StudyRequest(getActivity(), handler).getResumeInfo(user.getUserid(), GETRESUMEINFO);
            new StudyRequest(getActivity(), handler).employList(String.valueOf(pager));
            //暂时用新闻的接口测试一下
//            }
            new StudyRequest(getActivity(), handler).getNewsList("121", FIRSTPAGEIMAGE);

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

    /**
     * 图片滚动任务
     */
    private void startImageTimerTask() {
        stopImageTimerTask();
        // 图片滚动
//        handler.postDelayed(mImageTimerTask, 3000);bt
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        isStop = true;
//        handler.removeCallbacks(mImageTimerTask);bt
    }
}
