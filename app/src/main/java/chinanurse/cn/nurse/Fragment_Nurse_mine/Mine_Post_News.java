package chinanurse.cn.nurse.Fragment_Nurse_mine;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
                if (getcontent() == null &&"".equals(getcontent())){
                    Toast.makeText(this,"请输入反馈内容",Toast.LENGTH_SHORT).show();
                }else{
                    if (NetUtil.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).addfeedback(user.getUserid(),content.getText().toString(), GETMYRECIVERESUME);
                    }else{
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
            }
                break;
        }
    }
}
