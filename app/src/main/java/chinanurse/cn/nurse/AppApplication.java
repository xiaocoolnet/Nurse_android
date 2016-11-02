package chinanurse.cn.nurse;

import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.util.Log;
import android.view.WindowManager;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.tauth.Tencent;

import chinanurse.cn.nurse.Fragment_Nurse_mine.mine_news.MyMessageReceiver;

/**
 * Created by Administrator on 2016/6/22.
 */
public class AppApplication extends Application {
    public static DisplayImageOptions options;
    public static Tencent mTencent;
    private static final String TAG = "AliyunApplication";
    private Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;


        initImageLoader();
        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
//        MiPushRegister.register(applicationContext, "小米AppID", "小米AppKey");
// 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        HuaWeiRegister.register(applicationContext);
        mTencent=Tencent.createInstance("1105552541",this.getApplicationContext());
        initOneSDK(this);


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
                Log.d("TAG", "init cloudchannel success");
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d("TAG", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }
}