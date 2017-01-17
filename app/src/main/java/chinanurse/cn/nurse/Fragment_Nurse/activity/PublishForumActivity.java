package chinanurse.cn.nurse.Fragment_Nurse.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Nurse.activity.release.Bimp;
import chinanurse.cn.nurse.Fragment_Nurse.activity.release.MultiImageSelectorActivity;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.FileUtils;
import chinanurse.cn.nurse.utils.KeyBoardUtils;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.TimeToolUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2016/12/18.
 * 发布帖子
 */

public class PublishForumActivity extends Activity implements View.OnClickListener {
    private String TAG = "PublishForumActivity";
    private final int INTENT_CHOICE_COMMUNITY = 20;
    private RelativeLayout rl_back, rl_submit;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private EditText et_title, et_content;
    private TextView tv_choice_community, tv_location;
    private LinearLayout ll_choice_community, ll_loaction;
    private Context mContext;
    private CommunityBean communityBean;
    private String from;
    private static final int REQUEST_IMAGE = 2;
    private ProgressDialog proDialog;
    private Dialog dialog;
    private int count = 0;
    private UserBean user;
    private ArrayList<String> mSelectPath= new ArrayList<>();
    private String title, content, publishImg, communityId, locationString;
    private ArrayList<String> imgs = new ArrayList<String>();
    public LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 3:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_publish);
        mContext = this;
        user = new UserBean(mContext);
        initView();
        initLocation();
    }


    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_activity_publish_back);
        rl_back.setOnClickListener(this);
        rl_submit = (RelativeLayout) findViewById(R.id.rl_activity_publish_submit);
        rl_submit.setOnClickListener(this);
        et_title = (EditText) findViewById(R.id.et_activity_publish_title);
        et_content = (EditText) findViewById(R.id.et_activity_publish_content);
        noScrollgridview = (GridView) findViewById(R.id.gridview_publish_img);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // if (arg2 == Bimp.bmp.size()) {
                // 多选
                int selectedMode = MultiImageSelectorActivity.MODE_MULTI;
                // 单选
                // int selectedMode =
                // MultiImageSelectorActivity.MODE_SINGLE;
                // 是否照相
                boolean showCamera = true;
                // 多选最大数
                Intent intent = new Intent(PublishForumActivity.this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
                if (mSelectPath != null && mSelectPath.size() > 0) {
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                }
                startActivityForResult(intent, REQUEST_IMAGE);

            }
        });
        ll_choice_community = (LinearLayout) findViewById(R.id.ll_choice_community);
        ll_choice_community.setOnClickListener(this);
        tv_choice_community = (TextView) findViewById(R.id.tv_choice_community);
        ll_loaction = (LinearLayout) findViewById(R.id.ll_location);
        ll_loaction.setOnClickListener(this);
        tv_location = (TextView) findViewById(R.id.tv_location);
        proDialog = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        KeyBoardUtils.showKeyBoardByTime(et_title, 300);
        from = getIntent().getStringExtra("from");
        if (from.equals("CommunityDetailActivity") || from.equals("TopOrBestActivity")) {
            communityBean = (CommunityBean) getIntent().getSerializableExtra("communityBean");
            communityId = communityBean.getId();
            tv_choice_community.setText(communityBean.getName());
            ll_choice_community.setClickable(false);
        } else {
            communityId = "";
            tv_choice_community.setText("请选择圈子");
            ll_choice_community.setClickable(true);
        }
    }

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_activity_publish_back:
                KeyBoardUtils.hidekeyBoardByTime(et_title, 0);
                KeyBoardUtils.hidekeyBoardByTime(et_content, 0);
                finish();
                break;
            case R.id.rl_activity_publish_submit:
                if (isCanSubmit()) {
                    rl_submit.setClickable(false);
                    proDialog.setMessage("上传中..");
                    proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    proDialog.setCanceledOnTouchOutside(false);
                    proDialog.show();
                    submitForum();
                }
                break;
            case R.id.ll_choice_community:
                Intent intentChoice = new Intent();
                intentChoice.setClass(mContext, ChoicePublishCommunityActivity.class);
                startActivityForResult(intentChoice, INTENT_CHOICE_COMMUNITY);
                break;
            case R.id.ll_location:
                break;
        }
    }

    private boolean isCanSubmit() {
        if (HttpConnect.isConnnected(mContext)) {
            title = et_title.getText().toString();
            content = et_content.getText().toString();
            if (TextUtils.isEmpty(title)) {
                ToastUtils.ToastShort(mContext, "请输入标题");
                return false;
            }
            if (title != null && title.length() < 5) {
                ToastUtils.ToastShort(mContext, "标题至少为5个字");
                return false;
            }

            if (TextUtils.isEmpty(content)) {
                ToastUtils.ToastShort(mContext, "请输入内容");
                return false;
            }
            if (TextUtils.isEmpty(communityId)) {
                ToastUtils.ToastShort(mContext, "请选择圈子");
                return false;
            }
        } else {
            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
            return false;
        }
        return true;
    }

    private void submitForum() {
        publishImg = "";
        if (mSelectPath.size() > 0) {
            compressImgs();
            proDialog.setMessage("上传图片中...");
            try {
                for (int i = 0; i < imgs.size(); i++) {
                    String path = FileUtils.SDPATH + imgs.get(i) + ".jpg";
                    final File f = new File(path);
                    RequestParams r = new RequestParams();
                    r.put("upfile", f);
                    NurseAsyncHttpClient.post(CommunityNetConstant.UPLOAD_AVATAR, r, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            LogUtils.e(TAG, "--response-->" + response.toString());
                            try {
                                if (response.getString("status").equals("success")) {
                                    String path = response.getString("data");
                                    if (publishImg.equals("")) {
                                        publishImg = path;
                                    } else {
                                        publishImg = publishImg + "," + path;
                                    }
                                    count++;
                                    LogUtils.e(TAG, "--publishImg-->" + publishImg);
                                    LogUtils.e(TAG, "--finalNum-->" + count);
                                    LogUtils.e(TAG, "--imgs.size()-->" + imgs.size());
                                    if (count == imgs.size()) {
                                        //userid,cid,title,content,description,photo
                                        proDialog.setMessage("上传圈子中...");
                                        RequestParams re = new RequestParams();
                                        re.put("userid", user.getUserid());
                                        re.put("cid", communityId);
                                        re.put("title", title);
                                        re.put("content", content);
                                        re.put("photo", publishImg);
                                        re.put("address", locationString);
//                                        ToastUtils.ToastShort(mContext, "上传了" + count + "张");
//                                        ToastUtils.ToastShort(mContext, "photo" + publishImg);
                                        LogUtils.e(TAG, "--locationString-->" + locationString);
                                        LogUtils.e(TAG, "--publishImg-->" + publishImg);
                                        LogUtils.e(TAG, "--re-->" + re.toString());

                                        NurseAsyncHttpClient.get(CommunityNetConstant.PUBLISH_FORUM, re, new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                super.onSuccess(statusCode, headers, response);
                                                LogUtils.e(TAG, "-上传圈子中-response-->" + response.toString());
//                                                {
//                                                    "status": "success",
//                                                        "data": {
//                                                    "score": "2",
//                                                            "event": "发布帖子",
//                                                            "create_time": 1483976836
//                                                }
//                                                }
                                                try {
                                                    if (response != null) {
                                                        String status = response.getString("status");
                                                        if (status.equals("success")) {
                                                            JSONObject json = response.getJSONObject("data");
                                                            if (json.getString("score") != null && json.getString("score").length() > 0) {
                                                                View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_score, null);
                                                                dialog = new AlertDialog.Builder(mContext).create();
                                                                dialog.show();
                                                                dialog.getWindow().setContentView(layout);
                                                                dialog.getWindow().clearFlags(
                                                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                                                                TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                                                tv_score.setText("+" + json.getString("score"));
                                                                TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                                                tv_score_name.setText(json.getString("event"));
                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        try {
                                                                            Thread.sleep(3000);
                                                                            if(dialog.isShowing()){
                                                                                dialog.dismiss();
                                                                            }
                                                                            setResult(1);
                                                                            finish();
                                                                        } catch (InterruptedException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }).start();
                                                            }else{
                                                                finish();
                                                            }
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                proDialog.dismiss();
                                                rl_submit.setClickable(true);
                                            }
                                        });
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            count++;

                        }
                    });
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            //userid,cid,title,content,description,photo
            proDialog.setMessage("上传圈子中...");
            RequestParams re = new RequestParams();
            re.put("userid", user.getUserid());
            re.put("cid", communityId);
            re.put("title", title);
            re.put("content", content);
            re.put("address", locationString);
            NurseAsyncHttpClient.get(CommunityNetConstant.PUBLISH_FORUM, re, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LogUtils.e(TAG, "-上传圈子中-response-->" + response.toString());
                    try {
                        if (response != null) {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONObject json = response.getJSONObject("data");
                                if (json.getString("score") != null && json.getString("score").length() > 0) {
                                    View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_score, null);
                                    dialog = new AlertDialog.Builder(mContext).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    dialog.getWindow().clearFlags(
                                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("+" + json.getString("score"));
                                    TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                    tv_score_name.setText(json.getString("event"));
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(2000);
                                                dialog.dismiss();
                                                setResult(1);
                                                finish();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }else{
                                    finish();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    proDialog.dismiss();
                    rl_submit.setClickable(true);
                }
            });
        }
    }

    private void compressImgs() {
        imgs.clear();
        for (String path : mSelectPath) {
            try {
                Bitmap bm = Bimp.revitionImageSize(path);
                String s = "circle" + TimeToolUtils.getNowTime() + user.getUserid();
                FileUtils.saveBitmap(bm, "" + s);
                imgs.add(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            return (Bimp.bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
            if (position == Bimp.bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_publish_photo));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.bmp.get(position));
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        public void loading() {
            imgs.clear();
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.drr.size()) {
                            break;
                        } else {
                            try {
                                String path = Bimp.drr.get(Bimp.max);
                                LogUtils.e("Bimp.max", path);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
                                String s = "circle" + TimeToolUtils.getNowTime() + user.getUserid();
//                                String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
//                                LogUtils.e("Bimp.max", "---->" + newStr);
                                FileUtils.saveBitmap(bm, "" + s);
                                imgs.add(s);
                                LogUtils.e("Bimp.max", "---->" + s);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 3;
                                handler.sendMessage(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        super.onRestart();
        adapter.update();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    Bimp.bmp.clear();
                    Bimp.drr.clear();
                    Bimp.max = 0;
                    for (String p : mSelectPath) {
                        if (!Bimp.drr.contains(p)) {
                            Bimp.drr.add(p);
                        }
                    }
                }
                break;
            case INTENT_CHOICE_COMMUNITY:
                if (resultCode == RESULT_OK) {
                    communityId = data.getStringExtra("id");
                    tv_choice_community.setText(data.getStringExtra("name"));
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.bmp.clear();
        Bimp.drr.clear();
        Bimp.max = 0;
        mLocationClient.stop();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            String province = location.getProvince();
            String city = location.getCity();
            if (province == null || province.length() <= 0) {
                if (city == null || city.length() <= 0) {
                    locationString = "北京市-北京市";
                } else {
                    locationString = city + "-" + city;
                }
            } else {
                locationString = city + "-" + city;
            }
            tv_location.setText(locationString);
        }
    }
}
