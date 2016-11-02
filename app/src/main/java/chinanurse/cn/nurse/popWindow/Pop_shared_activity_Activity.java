package chinanurse.cn.nurse.popWindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
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

import java.util.ArrayList;

import chinanurse.cn.nurse.AppApplication;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.weibo.AccessTokenKeeper;
import chinanurse.cn.nurse.weibo.Constants;

/**
 * Created by Administrator on 2016/8/21 0021.
 */
public class Pop_shared_activity_Activity implements PopupWindow.OnDismissListener,View.OnClickListener{
    private Activity activity;
    private RelativeLayout weixinhaoyou,weixin,qq,qqkongjian,weibo;
    private PopupWindow popupWindow;
    private String smeta;
    private UserBean user;
    //微信
    private IWXAPI iwxapi;
    private String IWXAPI_ID = "wxdd50558e711439e8";
    //微博
    /** 微博分享的接口实例 */
    private IWeiboShareAPI mWeiboShareAPI;
    private boolean isChecked = true;

    public Pop_shared_activity_Activity(Activity activity) {
        this.activity = activity;
        user = new UserBean(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_shared, null);
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
    /**
     * 消除弹窗
     */
    public void dissmiss() {
        popupWindow.dismiss();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pop_weixinhaoyou://微信好友
                wechatShare(1);
                dissmiss();
                break;
            case R.id.weixin://微信
                wechatShare(0);
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
                sendMultiMessage(isChecked);
                dissmiss();
                break;
        }
    }
    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     * @param hasWebpage 分享的内容是否有网页
     */
    private void sendMultiMessage( boolean hasWebpage) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title =  "中国护士网";;
        mediaObject.description = "服务于中国320万护士 白衣天使的网上家园";
        Bitmap  bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logo);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = "http://app.chinanurse.cn/index.php?g=Score&m=Score&a=scorepengyou&userid="+user.getUserid();
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
        AuthInfo authInfo = new AuthInfo(activity, chinanurse.cn.nurse.weibo.Constants.APP_KEY, chinanurse.cn.nurse.weibo.Constants.REDIRECT_URL, chinanurse.cn.nurse.weibo.Constants.SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(activity.getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest(activity, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException( WeiboException arg0 ) {
            }

            @Override
            public void onComplete( Bundle bundle ) {
                // TODO Auto-generated method stub
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(activity.getApplicationContext(), newToken);
                Toast.makeText(activity.getApplicationContext(), "onAuthorizeComplete token = " + newToken.getToken(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }
        });
        isChecked = false;
    }
    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://app.chinanurse.cn/index.php?g=Score&m=Score&a=scorepengyou&userid="+user.getUserid();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title =  "中国护士网";
        msg.description = "服务于中国320万护士 白衣天使的网上家园";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(activity.getResources(), R.drawable.icon_nurse);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);
    }

    private void onClickShare() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "中国护士网");//标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "服务于中国320万护士 白衣天使的网上家园");//内容
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://app.chinanurse.cn/index.php?g=Score&m=Score&a=scorepengyou&userid="+user.getUserid());
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "测试应用222222");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  "其他附加功能");
        AppApplication.mTencent.shareToQQ(activity, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {//成功

                Toast.makeText(activity, "QQ分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(UiError uiError) {//错误

                Toast.makeText(activity, "QQ分享错误"+uiError, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {//取消
                Toast.makeText(activity, "取消QQ分享", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void shareToQQzone() {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "中国护士网");
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,  "服务于中国320万护士 白衣天使的网上家园");
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,  "http://app.chinanurse.cn/index.php?g=Score&m=Score&a=scorepengyou&userid="+user.getUserid());
        ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add("http://media-cdn.tripadvisor.com/media/photo-s/01/3e/05/40/the-sandbar-that-links.jpg");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);

        AppApplication.mTencent.shareToQzone(activity, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Toast.makeText(activity, "QQ空间分享成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(activity, "QQ空间分享错误"+uiError, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(activity, "取消QQ空间分享", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void showAsDropDown(View parent) {
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
}
