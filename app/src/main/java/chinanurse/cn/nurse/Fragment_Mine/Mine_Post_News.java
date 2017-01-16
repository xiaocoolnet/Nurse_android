package chinanurse.cn.nurse.Fragment_Mine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.HttpConn.NetUtil;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/10/27 0027.
 */

public class Mine_Post_News extends Activity implements View.OnClickListener{
    private static final int GETMYRECIVERESUME = 1;
    private RelativeLayout btn_back;
    private EditText content;
    private TextView send_news,title_top;
    private Activity mactivity;
    private UserBean user;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GETMYRECIVERESUME:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))){
                                Toast.makeText(mactivity,"发送成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(mactivity, "请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                }

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_post_news);
        mactivity = this;
        user = new UserBean(mactivity);
        getnews();
    }

    private void getnews() {
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        title_top = (TextView) findViewById(R.id.top_title);
        title_top.setText("反馈信息编辑");
        send_news = (TextView) findViewById(R.id.send_post_news);
        send_news.setOnClickListener(this);
        content = (EditText) findViewById(R.id.content_news);
    }
    private String getcontent(){
     return content.getText().toString();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.send_post_news:
                Log.e(" android.os.Build.MODEL","''''''''''''''''"+ android.os.Build.MODEL);
                Log.e(" VERSION.RELEASE","''''''''''''''''"+ android.os.Build.VERSION.RELEASE);
                TelephonyManager tm = (TelephonyManager)mactivity.getSystemService(Context.TELEPHONY_SERVICE);
                Log.e("deviced","''''''''''''''''"+tm.getDeviceId());
                String devicestate = android.os.Build.MODEL+"-"+android.os.Build.VERSION.RELEASE+"-5.2.1"+"\t"+"devicesID"+tm.getDeviceId();
                if (getcontent() == null ||"".equals(getcontent())){
                    Toast.makeText(this,"请输入反馈内容",Toast.LENGTH_SHORT).show();
                }else{
                    if (NetUtil.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).addfeedback(user.getUserid(),content.getText().toString(),devicestate, GETMYRECIVERESUME);
                    }else{
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
            }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "发送反馈信息");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "发送反馈信息");
    }
}
