package chinanurse.cn.nurse.loginragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.MainActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.utils.RegexUtil;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/6/27.
 */
@SuppressLint("ValidFragment")
public class Login_login extends Fragment implements View.OnClickListener,View.OnTouchListener{
    private static final int LOGIN_NOW = 1;
    private static final int GETUSERINFO = 2;
    private EditText login_phone,login_password;
    private TextView navigation_register;
    private Button bu_login;
    private String lg_phone,lg_pass,now_btn,login_now;
    private Activity mactivity;
    private ProgressDialog dialogpgd;
    private UserBean user;
    private Intent intentlogin;
    private ScrollView all_scrollview;
    private Handler mHandler = new Handler();
    //    private FirstPageNews.DataEntity fndData;
    private String titletype ;
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_NOW:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        if (result != null){
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            Log.i("USERData", "----------->" + status);
                            String data = json.getString("data");
                            Log.i("USERData", "----------->" + status);
                            if (status.equals("success")){
                                JSONObject item = new JSONObject(data);
                                user.setUserid(item.getString("userid"));
                                user.setUsertype(item.getString("usertype"));
                                user.setPhone(item.getString("phone"));
                                user.setDevicestate(item.getString("devicestate"));
                                if (HttpConnect.isConnnected(mactivity)){
                                    new StudyRequest(mactivity,handler).getuserinfo(user.getUserid(),GETUSERINFO);
                                }else{
                                    Toast.makeText(mactivity,R.string.net_erroy,Toast.LENGTH_SHORT).show();
                                }
                                getActivity().finish();
                                dialogpgd.dismiss();

                            }else{
                                dialogpgd.dismiss();
                                Toast.makeText(mactivity,R.string.login_thoast_else,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        }else{
                            dialogpgd.dismiss();
                            Toast.makeText(mactivity,R.string.net_erroy,Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case GETUSERINFO:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")){
                                JSONObject obj = new JSONObject(data);
                                user.setUserid(obj.getString("id"));
                                user.setUsertype(obj.getString("usertype"));
                                user.setName(obj.getString("name"));
                                user.setSex(obj.getString("sex"));
                                user.setLevel(obj.getString("level"));
                                user.setScore(obj.getString("score"));
                                user.setBirthday(obj.getString("birthday"));
                                user.setRealname(obj.getString("realname"));
                                user.setAddress(obj.getString("address"));
                                user.setPhone(obj.getString("phone"));
                                user.setCity(obj.getString("city"));
                                user.setEmail(obj.getString("email"));
                                user.setQq(obj.getString("qq"));
                                user.setWeixin(obj.getString("weixin"));
                                user.setPhoto(obj.getString("photo"));
                                user.setSchool(obj.getString("school"));
                                user.setMajor(obj.getString("major"));
                                user.setEducation(obj.getString("education"));
                                user.setFanscount(obj.getString("fanscount"));
                                user.setMoney(obj.getString("money"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };
    public Login_login(String now_btn) {
        this.now_btn = now_btn;
        login_now = now_btn;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = View.inflate(getActivity(), R.layout.login_login, null);
        mactivity = getActivity();
        user = new UserBean(mactivity);
//        intentlogin = mactivity.getIntent();
//        login_now = intentlogin.getStringExtra("now_btn");

        if(user.isLogined()){
            Intent intent = new Intent(mactivity,MainActivity.class);
            mactivity.startActivity(intent);
        }
        login_phone = (EditText) mView.findViewById(R.id.login_login_phone);
        login_phone.setOnClickListener(this);
        login_phone.setOnTouchListener(this);
        login_password = (EditText) mView.findViewById(R.id.login_login_password);
        login_password.setOnClickListener(this);
        login_password.setOnTouchListener(this);
        bu_login = (Button) mView.findViewById(R.id.login_regist_login);
        bu_login.setOnClickListener(this);
        dialogpgd=new ProgressDialog(mactivity, AlertDialog.THEME_HOLO_LIGHT);
        navigation_register = (TextView) mView.findViewById(R.id.navigation_register);
        navigation_register.setOnClickListener(this);
        all_scrollview = (ScrollView) getActivity().findViewById(R.id.all_scrollview);
        return mView;
    }
    private Boolean isCanGetCode() {
        if (!RegexUtil.checkMobile(lg_phone)){
            Toast.makeText(mactivity,R.string.register_thoast_surephone,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_regist_login:
                lg_phone = login_phone.getText().toString();
                lg_pass = login_password.getText().toString();
                if (isCanGetCode()){
                    getloginpair();
                }
                break;
            case R.id.navigation_register:
                Intent intent = new Intent(getActivity(),Navigation_register.class);
                startActivity(intent);
                break;
        }
    }
    private void getloginpair() {

        if (login_phone.getText().length() <= 0){
            Toast.makeText(mactivity, R.string.login_thoast_name, Toast.LENGTH_SHORT).show();
        }else if (login_password.getText().length() <= 0){
            Toast.makeText(mactivity,R.string.login_thoast_pass,Toast.LENGTH_SHORT).show();
        }else{
            dialogpgd.setMessage("正在登录...");
            dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialogpgd.show();
            if (HttpConnect.isConnnected(mactivity)){
                new StudyRequest(mactivity,handler).getLogin(lg_phone,lg_pass,LOGIN_NOW);
            }else{
                Toast.makeText(mactivity,R.string.net_erroy,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.login_login_phone:
                login_phone.setFocusable(true);
                login_phone.setFocusableInTouchMode(true);
                login_phone.requestFocus();
                login_password.setFocusable(false);
                login_password.clearFocus();
//                login_password.setFocusableInTouchMode(false);
                login_password.setHint("密码");
                login_phone.setHint("");
                //这里必须要给一个延迟，如果不加延迟则没有效果。我现在还没想明白为什么
                mHandler.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        //将ScrollView滚动到底
                        all_scrollview.fullScroll(View.FOCUS_DOWN);
                    }
                },100);
                break;
            case R.id.login_login_password:
                login_phone.setFocusable(false);
//                login_phone.setFocusableInTouchMode(false);
                login_phone.clearFocus();
                login_phone.setHint("手机号");
                login_password.setFocusable(true);
                login_password.setFocusableInTouchMode(true);
                login_password.requestFocus();
                login_password.setHint("");
                //这里必须要给一个延迟，如果不加延迟则没有效果。我现在还没想明白为什么
                mHandler.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        //将ScrollView滚动到底
                        all_scrollview.fullScroll(View.FOCUS_DOWN);
                    }
                },100);
                break;
        }
        return false;
    }
}
