package chinanurse.cn.nurse;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.bean.UserBean;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/6/22.
 */
public class AppApplication extends Application {
    public static DisplayImageOptions options;
    public static Tencent mTencent;
    private static final String TAG = "AliyunApplication";
    private Context applicationContext;
    public  static String TagId="";
    public  static String TagName="";
    public  static String photo="";

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        initImageLoader();
        // 注册方法会
        mTencent=Tencent.createInstance("1105552541",this.getApplicationContext());
        initOneSDK(this);
        getHomePage();


    }

    private void getHomePage() {
        UserBean user=new UserBean(this);
        //        入参：userid
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        NurseAsyncHttpClient.get(CommunityNetConstant.GET_HOME_PAGE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    LogUtils.e(TAG, "--getHomePage->" + response);
//                    {
//                    "status":"success",
//                        "data":{
//                            "id": "1",
//                                "photo": "20170103/586b72532acca.png",
//                                "create_time": "1483436630",
//                                "title": "aaa",
//                                "description": "aaa"
//                    }
//                }
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            JSONObject data=response.getJSONObject("data");
                            photo=data.getString("photo");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheSizePercentage(30)
                .build();
        ImageLoader.getInstance().init(config);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.icon_nurse)
                .showImageOnFail(R.drawable.icon_nurse)
                .build();
    }
    /**
     *
     *
     * @param applicationContext
     */
    private void initOneSDK(final Context applicationContext) {
        Log.w(TAG, "get App Package Name: " + applicationContext.getPackageName());

        AlibabaSDK.asyncInit(applicationContext, new InitResultCallback() {

            public void onSuccess() {
                Log.d(TAG, "init onesdk success");
                //alibabaSDK初始化成功后，初始化移动推送通道
                initCloudChannel(applicationContext);
            }

            public void onFailure(int code, String message) {
                Log.e(TAG, "init onesdk failed : " + message);
            }
        });
    }
    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtils.d("TAG", "init cloudchannel success");
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                LogUtils.d("TAG", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }
}