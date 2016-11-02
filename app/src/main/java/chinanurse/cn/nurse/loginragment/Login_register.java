package chinanurse.cn.nurse.loginragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.RegexUtil;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/6/27.
 */
public class Login_register extends Fragment implements View.OnClickListener,View.OnTouchListener{
    private static final int IS_KAY = 1;
    private static final int CODE_ONE = 2;
    private static final int CODE_TWO = 3;
    private static final int KEY = 4;
    private static final int REGISTER_NOW = 5;
    private int i = 30;
    private EditText register_phone,register_code,register_password;
    private Button bu_gain_code,bu_regist;
    private RadioButton mRB_personal,mRB_business;
    private Activity mactivity;
    private UserBean user;
    private String code,rgt_code,rgt_pass,rgt_phone;
    private String usertype;
    private String devicestate = "2";//1.是ios 2.是android
    private ProgressDialog dialog;
    private Dialog dialogone;
    private ScrollView all_scrollview;
    private Handler mHandlerone = new Handler();

    private int Mars = 1;
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
                                Toast.makeText(mactivity,R.string.login_thoast_mphone,Toast.LENGTH_SHORT).show();
                            }else{
                                getVerificationCode();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CODE_ONE:
                    bu_gain_code.setText("重发(" + i + ")");
                    break;
                case CODE_TWO:
                    bu_gain_code.setText("重新发送");
                    bu_gain_code.setClickable(true);
                    bu_gain_code.setSelected(true);
                    i = 60;
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
                case REGISTER_NOW:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            Log.i("statusregister","------------>"+status);
                            if (status.equals("success")){
                                dialog.dismiss();
                                String userIdTemp = json.getString("data");
                                JSONObject jsonobj = new JSONObject(userIdTemp);
                                user.setUserid(jsonobj.getString("userid"));
                                user.setUsertype(usertype);
                                // // 进入主页面
//                                LoginActivity la = new LoginActivity();
//                                la.initFragment(new Login_login());
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
                                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                                        startActivity(intent);
//                                    }
//                                }).start();

                                mactivity.finish();
                                if (jsonobj.getString("score") != null &&jsonobj.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    dialogone = new AlertDialog.Builder(mactivity).create();
                                    dialogone.show();
                                    dialogone.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("+"+jsonobj.getString("score"));
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mview = inflater.inflate(R.layout.login_regist,null);
        mactivity = getActivity();
        user = new UserBean(mactivity);
        register_phone = (EditText) mview.findViewById(R.id.login_regist_phone);
        register_phone.setOnClickListener(this);
        register_phone.setOnTouchListener(this);
        register_code = (EditText) mview.findViewById(R.id.login_regist_code);
        register_code.setOnClickListener(this);
        register_code.setOnTouchListener(this);
        bu_gain_code = (Button) mview.findViewById(R.id.btn_register_gain_code);
        bu_gain_code.setOnClickListener(this);
        bu_gain_code.setOnTouchListener(this);
        register_password = (EditText) mview.findViewById(R.id.login_regist_password);
        register_password.setOnClickListener(this);
        register_password.setOnTouchListener(this);
        mRB_personal = (RadioButton) mview.findViewById(R.id.login_regist_personal);
        mRB_personal.setOnClickListener(this);
        mRB_business = (RadioButton) mview.findViewById(R.id.login_regist_business);
        mRB_business.setOnClickListener(this);
        bu_regist = (Button) mview.findViewById(R.id.login_regist_submit);
        bu_regist.setOnClickListener(this);
        dialog=new ProgressDialog(mactivity, AlertDialog.THEME_HOLO_LIGHT);
        all_scrollview = (ScrollView) getActivity().findViewById(R.id.all_scrollview);
        return mview;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_regist_business:
                mRB_personal.setSelected(false);
                mRB_personal.setSelected(true);
                usertype = "2";
//                private String getcode() {
//                return register_phone.getText().toString();
//            }
                break;
            case R.id.login_regist_personal:
                mRB_personal.setSelected(true);
                mRB_personal.setSelected(false);
                usertype = "1";
                break;
            case R.id.btn_register_gain_code:
                if (HttpConnect.isConnnected(mactivity)){
                    if (isCanGetCode()){
                        register_code.setText("");
                        register_code.clearFocus();
                        bu_gain_code.setClickable(false);
                        new StudyRequest(mactivity, handler).isCanRegister(getPhoneNumber(), IS_KAY);
                    }
                }else{
                    Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_regist_submit:
                rgt_code = register_code.getText().toString();
                rgt_pass = register_password.getText().toString();
                rgt_phone = register_phone.getText().toString();
                getregisterpair();
                break;
        }
    }
    private Boolean isCanGetCode() {
        if (!RegexUtil.checkMobile(getPhoneNumber())){
            Toast.makeText(mactivity,R.string.register_thoast_surephone,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private String getPhoneNumber() {
        return register_phone.getText().toString();
    }
    private void getVerificationCode() {
        bu_gain_code.setSelected(false);
        bu_gain_code.setText("重发(" + i + ")");
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
    private void getregisterpair() {
        Log.i("codepanduan","----------->"+code);
        if (register_phone.getText().length() <= 0){
            Toast.makeText(mactivity,R.string.register_thoast_phone,Toast.LENGTH_SHORT).show();
        }else if(register_password.getText().length() <= 0){
            Toast.makeText(mactivity,R.string.register_thoast_pass,Toast.LENGTH_SHORT).show();
        }else if(register_code.getText().length() <= 0){
            Toast.makeText(mactivity,R.string.register_thoast_code,Toast.LENGTH_SHORT).show();
        }else if(getcode().isEmpty()){
            register_code.setError(Html.fromHtml("<font color=#E10979>请输入验证码！</font>"));
        }
        else if(!code.equals(getcode())){
            register_code.setError(Html.fromHtml("<font color=#E10979>验证码错误！</font>"));
        } else{
            dialog.setMessage("正在注册...");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            if (HttpConnect.isConnnected(mactivity)){
                new StudyRequest(mactivity,handler).getRegister(rgt_phone,rgt_pass,rgt_code,usertype,devicestate,REGISTER_NOW);
            }else{
                Toast.makeText(mactivity,R.string.net_erroy,Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getcode() {
        Log.i("getcode","----------->"+register_code.getText().toString());
        return register_code.getText().toString();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.login_regist_phone:
                register_phone.setFocusable(true);
                register_phone.setFocusableInTouchMode(true);
                register_phone.requestFocus();
                register_phone.setHint("");
                register_code.setFocusable(false);
                register_code.setFocusableInTouchMode(false);
                register_code.setHint("验证码");
                register_password.setFocusable(false);
                register_password.setFocusableInTouchMode(false);
                register_password.setHint("密码");
                //这里必须要给一个延迟，如果不加延迟则没有效果。我现在还没想明白为什么

                mHandlerone.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                            //将ScrollView滚动到底
                            all_scrollview.fullScroll(View.FOCUS_DOWN);
                    }
                },100);
                break;
            case R.id.login_regist_code:
                register_code.setFocusable(true);
                register_code.setFocusableInTouchMode(true);
                register_code.requestFocus();
                register_code.setHint("");
                register_phone.setFocusable(false);
                register_phone.setFocusableInTouchMode(false);
                register_phone.setHint("手机号");
                register_password.setFocusable(false);
                register_password.setFocusableInTouchMode(false);
                register_password.setHint("密码");
                //这里必须要给一个延迟，如果不加延迟则没有效果。我现在还没想明白为什么

                mHandlerone.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                            //将ScrollView滚动到底
                            all_scrollview.fullScroll(View.FOCUS_DOWN);
                    }
                },100);
                break;
            case R.id.login_regist_password:
                register_password.setFocusable(true);
                register_password.setFocusableInTouchMode(true);
                register_password.requestFocus();
                register_password.setHint("");
                register_phone.setFocusable(false);
                register_phone.setFocusableInTouchMode(false);
                register_phone.setHint("手机号");
                register_code.setFocusable(false);
                register_code.setFocusableInTouchMode(false);
                register_code.setHint("验证码");
                //这里必须要给一个延迟，如果不加延迟则没有效果。我现在还没想明白为什么

                mHandlerone.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                            //将ScrollView滚动到底
                            all_scrollview.fullScroll(View.FOCUS_DOWN);
                    }
                },100);
                break;
            case R.id.btn_register_gain_code:

                break;
        }
        return false;
    }
}
