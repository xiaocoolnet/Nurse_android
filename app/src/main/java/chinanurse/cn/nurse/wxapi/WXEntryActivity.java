package chinanurse.cn.nurse.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/6/25.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
    private static final int ADDSCORE = 1;
    private final String SHARE_SUCCESS="shareSuccess";
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private UserBean user;
    private Activity mactivity;
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ADDSCORE:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))){
                                JSONObject json = new JSONObject(obj.getString("data"));
                                View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                final Dialog dialog = new AlertDialog.Builder(mactivity).create();
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this,"R.id.wxapi",false);
        api.handleIntent(getIntent(),this);
        super.onCreate(savedInstanceState);
        mactivity = this;
        user = new UserBean(mactivity);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }
    @Override
    public void onResp(BaseResp baseResp) {
//        LogManager.show();
        String result = "";
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "errcode_success";
//                if (HttpConnect.isConnnected(WXEntryActivity.this)) {
//                    new StudyRequest(WXEntryActivity.this, handler).ADDSCORE(user.getUserid(), ADDSCORE);
//                } else {
////                    Toast.makeText(WXEntryActivity.this, R.string.net_erroy, Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(WXEntryActivity.this, "微信分享成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setAction(SHARE_SUCCESS);
                sendBroadcast(intent);
//                getscore();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "errcode_cancel";
                Toast.makeText(WXEntryActivity.this, "取消微信分享", Toast.LENGTH_SHORT).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "errcode_deny";
                Toast.makeText(WXEntryActivity.this, "微信分享失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                result = "errcode_unknown";
                Toast.makeText(WXEntryActivity.this, "微信分享未知错误", Toast.LENGTH_SHORT).show();
                break;
        }
        finish();
    }
    private void getscore() {
        if (user.getUserid().length() > 0) {
//            if (HttpConnect.isConnnected(this)) {
//                Log.i("onResume", "initData1");
//                new StudyRequest(mactivity, handler).ADDSCORE_read(user.getUserid(), ADDSCORE);
//            } else {
//                Log.i("onResume", "initData2");
//                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
//            }
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    try {
//                        Thread.sleep(3000);
                        new StudyRequest(WXEntryActivity.this, handler).ADDSCORE(user.getUserid(), ADDSCORE);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }).start();

        }
    }
}
