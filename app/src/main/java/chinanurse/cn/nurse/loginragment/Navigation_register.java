package chinanurse.cn.nurse.loginragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.RegexUtil;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class Navigation_register extends Activity implements View.OnClickListener{
    private static final int IS_KAY = 1;
    public static final int CODE_ONE = 2;
    public static final int CODE_TWO = 3;
    public static final int KEY = 4;
    private static final int FORGETPWD = 5;
    private int i = 30;
    private EditText navigatin_phone,navigation_code_text,navigatin_pass,navigatin_password;
    private String phone,codetext,pass,password,code;
    private Button navigation_code,navigation_submit;
    private Activity mactivity;
    private RelativeLayout btn_back;
    private ProgressDialog dialog;
    private TextView title_top;
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case IS_KAY:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))){
//                                Toast.makeText(mactivity,R.string.login_thoast_mphone,Toast.LENGTH_SHORT).show();
                                getVerificationCode();
                            }else{
                                Toast.makeText(mactivity,"该手机号码未被注册",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CODE_ONE:
                    navigation_code.setText("重发(" + i + ")");
                    break;
                case CODE_TWO:
                    navigation_code.setText("重新发送");
                    navigation_code.setSelected(true);
                    navigation_code.setClickable(true);
                    i = 30;
                    break;
                case KEY:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            Log.i("USERData", "---------->" + json.toString());
                            if (status.equals("success")){
                                JSONObject obj = new JSONObject(data);
                                code = obj.getString("code");
                                Log.i("code","---------->"+code);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case FORGETPWD:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            Log.i("statusregister","------------>"+status);
                            if (status.equals("success")){
                                dialog.setMessage("重置成功");
                                String userIdTemp = json.getString("data");
//                                Intent intent = new Intent(mactivity,LoginActivity.class);
//                                startActivity(intent);
                                }
                                mactivity.finish();
                                dialog.dismiss();
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_register);
        mactivity = this;
        navigationview();
    }

    private void navigationview() {
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        title_top = (TextView) findViewById(R.id.top_title);
        title_top.setText("重置密码");
        navigatin_phone = (EditText) findViewById(R.id.navigatin_phone);
        navigation_code_text = (EditText) findViewById(R.id.navigation_code_text);
        navigation_code = (Button) findViewById(R.id.navigation_gain_code);
        navigation_code.setOnClickListener(this);
        navigatin_pass = (EditText) findViewById(R.id.navigatin_pass);
        navigatin_password = (EditText) findViewById(R.id.navigatin_password);
        navigation_submit = (Button) findViewById(R.id.navigation_submit);
        navigation_submit.setOnClickListener(this);
        dialog=new ProgressDialog(mactivity, AlertDialog.THEME_HOLO_LIGHT);
    }
    private String getPhoneNumber() {
        return navigatin_phone.getText().toString();
    }
    private Boolean isCanGetCode() {
        if (!RegexUtil.checkMobile(getPhoneNumber())){
            Toast.makeText(Navigation_register.this, R.string.register_thoast_surephone, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void getVerificationCode() {

        navigation_code.setSelected(false);
        navigation_code.setText("重发(" + i + ")");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(CODE_ONE);
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(CODE_TWO);
            }
        }).start();
        new StudyRequest(mactivity, handler).sendMobileCode(getPhoneNumber(), KEY);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigation_gain_code:
                phone =navigatin_phone.getText().toString();
                if (phone != null && phone.length() > 0){
                    if (HttpConnect.isConnnected(mactivity)){
                        if (isCanGetCode()){
//                        bu_gain_code.setText("");
                            navigation_code_text.setText("");
                            navigation_code.setClickable(false);
                            Log.i("clickable","[[[[[[[[[[[[[[[[");
                            new StudyRequest(mactivity, handler).isCanRegister(getPhoneNumber(), IS_KAY);
                        }
                    }else{
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                }else{

            }
                break;
            case R.id.navigation_submit:
                phone =navigatin_phone.getText().toString();
                codetext = navigation_code_text.getText().toString();
                pass = navigatin_pass.getText().toString();
                password  = navigatin_password.getText().toString();
                submitgetnavigation();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void submitgetnavigation() {
        if (phone == null&&phone.length() <= 0){
            Toast.makeText(mactivity, "请输入您注册的手机号", Toast.LENGTH_SHORT).show();
        }else if (pass == null&&pass.length() <= 0){
            Toast.makeText(mactivity, "请输入密码", Toast.LENGTH_SHORT).show();
        }else if (password == null&&password.length() <= 0){
            Toast.makeText(mactivity, "请输入确认密码", Toast.LENGTH_SHORT).show();
        }else if (!pass.equals(password)){
            Toast.makeText(mactivity, "两次密码不一致", Toast.LENGTH_SHORT).show();
        }else if(codetext.isEmpty()){
            navigation_code_text.setError(Html.fromHtml("<font color=#E10979>请输入验证码！</font>"));
        }else if(!code.equals(codetext)){
            navigation_code_text.setError(Html.fromHtml("<font color=#E10979>验证码错误！</font>"));
        }else{
            dialog.setMessage("正在重置...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            if (HttpConnect.isConnnected(mactivity)){
                new StudyRequest(mactivity,handler).forgetpwd(phone, code, password, FORGETPWD);
            }else{
                Toast.makeText(mactivity, R.string.net_erroy,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
