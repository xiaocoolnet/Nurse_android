package chinanurse.cn.nurse.popWindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import chinanurse.cn.nurse.AppApplication;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.weibo.AccessTokenKeeper;
import chinanurse.cn.nurse.weibo.Constants;

/**
 * Created by Administrator on 2016/8/21 0021.
 */
public class Pop_shared_Activity implements PopupWindow.OnDismissListener, View.OnClickListener {
    private static final int ADDSCORE = 1;
    private Activity activity;
    private RelativeLayout weixinhaoyou, weixin, qq, qqkongjian, weibo;
    private PopupWindow popupWindow;
    private String excerpt, webview, title,path;
    private UserBean user;
    boolean isInstalledWeibo;
    private Button cancel_button;

    //微信
    private IWXAPI iwxapi;
    private String IWXAPI_ID = "wxdd50558e711439e8";
    //微博
    /**
     * 微博分享的接口实例
     */
    private IWeiboShareAPI mWeiboShareAPI;
    private boolean isChecked = true;
    private Bitmap thumbBmp;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADDSCORE:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))){
                                JSONObject json = new JSONObject(obj.getString("data"));
                                View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_score, null);
                                final Dialog dialog = new AlertDialog.Builder(activity).create();
                                dialog.show();
                                dialog.getWindow().setContentView(layout);
                                TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                tv_score.setText("+" + json.getString("score"));
                                TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                tv_score_name.setText(json.getString("event"));
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };


    public Pop_shared_Activity(final Activity activity) {
        this.activity = activity;
        user = new UserBean(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_shared, null);
        cancel_button = (Button) view.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(this);
        weixinhaoyou = (RelativeLayout) view.findViewById(R.id.pop_weixinhaoyou);
        //微信
        //获取API
        iwxapi = WXAPIFactory.createWXAPI(activity, IWXAPI_ID);
        //注册API
        iwxapi.registerApp(IWXAPI_ID);
        //微博
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, Constants.APP_KEY);
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
        weixinhaoyou.setOnClickListener(this);
        weixin = (RelativeLayout) view.findViewById(R.id.weixin);
        weixin.setOnClickListener(this);
        qq = (RelativeLayout) view.findViewById(R.id.pop_qq);
        qq.setOnClickListener(this);
        qqkongjian = (RelativeLayout) view.findViewById(R.id.pop_qqkongjian);
        qqkongjian.setOnClickListener(this);
        weibo = (RelativeLayout) view.findViewById(R.id.pop_weibo);
        weibo.setOnClickListener(this);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
//        Toast.makeText(activity,smeta,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_weixinhaoyou://微信好友
                getBitmap(1);
                dissmiss();
                break;
            case R.id.weixin://微信
                getBitmap(0);
                dissmiss();
                break;
            case R.id.pop_qq://QQ
                onClickShare();
                dissmiss();
                break;
            case R.id.pop_qqkongjian://qq空间
                shareToQQzone();
                dissmiss();
                break;
            case R.id.pop_weibo://微博
                getBitmapweibo(isChecked);
                dissmiss();
                break;
            case R.id.cancel_button:
                dissmiss();
                break;
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     *
     * @param hasWebpage 分享的内容是否有网页
     */
    private void sendMultiMessage(boolean hasWebpage) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        ;
        mediaObject.description = excerpt;
        if (compressBitmapToData(checkImageSize(thumbBmp),32) != null &&compressBitmapToData(checkImageSize(thumbBmp),32).length > 0){
            mediaObject.thumbData = compressBitmapToData(checkImageSize(thumbBmp),32);
        }else{
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logo);
            // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
            mediaObject.setThumbImage(bitmap);
        }
        mediaObject.actionUrl = webview;
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        // 1. 初始化微博的分享消息
        if (hasWebpage) {
            weiboMessage.mediaObject = mediaObject;
        }
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
//        mWeiboShareAPI.sendRequest(GoAbroad_WebView.this, request);
        AuthInfo authInfo = new AuthInfo(activity, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity.getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(activity, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
            }

            @Override
            public void onComplete(Bundle bundle) {
                // TODO Auto-generated method stub
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(activity.getApplicationContext(), newToken);
//                Toast.makeText(activity.getApplicationContext(), "分享成功 ", Toast.LENGTH_SHORT).show();
                addshared();
            }


            @Override
            public void onCancel() {
                dissmiss();
            }
        });

    }

    private void addshared() {
        if (HttpConnect.isConnnected(activity)) {
            new StudyRequest(activity, handler).ADDSCORE(user.getUserid(), ADDSCORE);
        } else {
            Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }
    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webview;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = excerpt;
        if (compressBitmapToData(checkImageSize(thumbBmp),32) != null &&compressBitmapToData(checkImageSize(thumbBmp),32).length > 0){
            msg.thumbData = compressBitmapToData(checkImageSize(thumbBmp),32);
        }else{
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logo);
            // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
            msg.setThumbImage(bitmap);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }

    private void onClickShare() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);//标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, excerpt);//内容
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webview);
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,path);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  "其他附加功能");
        AppApplication.mTencent.shareToQQ(activity, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {//成功
                Toast.makeText(activity, "QQ分享成功", Toast.LENGTH_SHORT).show();
                addshared();
            }
            @Override
            public void onError(UiError uiError) {//错误
                Toast.makeText(activity, "QQ分享错误" + uiError, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {//取消
                Toast.makeText(activity, "取消QQ分享", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shareToQQzone() {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, excerpt);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, webview);
        ArrayList<String> imageUrls = new ArrayList<String>();
//        imageUrls.add("http://media-cdn.tripadvisor.com/media/photo-s/01/3e/05/40/the-sandbar-that-links.jpg");
        imageUrls.add(path);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);

        AppApplication.mTencent.shareToQzone(activity, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                addshared();
                Toast.makeText(activity, "QQ空间分享成功", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(UiError uiError) {
                Toast.makeText(activity, "QQ空间分享错误" + uiError, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancel() {
                Toast.makeText(activity, "取消QQ空间分享", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 消除弹窗
     */
    public void dissmiss() {
        popupWindow.dismiss();
    }

    public void showAsDropDown(View parent, String excerpt, String webview, String title,String path) {
        this.excerpt = excerpt;//内容
        this.webview = webview+"&type=1";//网址
        this.title = title;//题目
        this.path = path;//图片

        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        WindowManager manager = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int screenHeight = display.getHeight();
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, screenHeight - location[1]);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }
    //网络下载图片
    private void getBitmap(final int num){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //这里下载数据
                try{
                    URL  url = new URL(path);
                    HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream inputStream=conn.getInputStream();
                    thumbBmp = BitmapFactory.decodeStream(inputStream);
                    wechatShare(num);
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //网络下载图片
    private void getBitmapweibo(final boolean isChecked){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //这里下载数据
                try{
                    URL  url = new URL(path);
                    HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream inputStream=conn.getInputStream();
                    thumbBmp = BitmapFactory.decodeStream(inputStream);
                    sendMultiMessage(isChecked);
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //剪裁图片
    public static Bitmap checkImageSize(Bitmap bitmap) {
        //防止超长图文件大小超过微信限制，需要进行截取，暂定比例上限为5
        final int MAX_RATIO = 5;
        Bitmap result = bitmap;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float ratio = width > height ? width*1.0f / height : height*1.0f / width;
            if (ratio > MAX_RATIO) {
                int size = Math.min(width, height);
                result = Bitmap.createBitmap(bitmap, 0, 0, size, size);
            }
        }
        return result;
    }
    //压缩图片到32KB
    public static byte[] compressBitmapToData(Bitmap bmp,float size) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] result;
        try {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while ( output.toByteArray().length / 1024 >= size) {  //循环判断如果压缩后图片是否大于size kb,大于继续压缩
                output.reset();//重置baos即清空baos
                bmp.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到baos中
                if(options==1){
                    break;
                }
                options -= 10;//每次都减少20
                if(options<=0){
                    options=1;
                }
            }

            result = output.toByteArray();

            Log.i("","compressBitmap return length = " + result.length);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
