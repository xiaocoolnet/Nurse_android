package chinanurse.cn.nurse.Fragment_Nurse_mine.mine_info;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/6/28.
 */
public class MyInFo_Update extends Activity implements View.OnClickListener{
    private static final int UPDATENAME = 1;
    private static final int UPDATESEX = 2;
    private static final int UPDATEPHONE = 3;
    private static final int UPDATEEMAIL = 4;
    private static final int UPDATEBIRTHDAY = 5;
    private static final int UPDATEADDRESS = 6;
    private static final int UPDATENASCHOOL = 7;
    private static final int UPDATEMAJOR = 8;
    private static final int UPDATEEDUCATION = 9;
    private static final int UPDATENAREALNAME = 10;
    private TextView update_save,update_top_title;
    private EditText update_content;
    private Intent intent;
    private UserBean user;
    private Activity mactivity;
    private String updatetype,et_content;
    private RelativeLayout back;
    private String woman,man;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;
    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case UPDATENAME:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")){
                                JSONObject jsonObject = new JSONObject(json.getString("data"));
                                Toast.makeText(mactivity,R.string.update_content,Toast.LENGTH_SHORT).show();
                                user.setName(et_content);
                                if (jsonObject.getString("score") != null &&jsonObject.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    final Dialog dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("积分\t\t+"+json.getString("score"));
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
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case UPDATESEX:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")){
                                JSONObject jsonObject = new JSONObject(json.getString("data"));
                                Toast.makeText(mactivity,R.string.update_content,Toast.LENGTH_SHORT).show();
                                user.setName(et_content);
                                if (jsonObject.getString("score") != null &&jsonObject.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    final Dialog dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("积分\t\t+"+json.getString("score"));
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
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case UPDATEPHONE:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")){
                                JSONObject jsonObject = new JSONObject(json.getString("data"));
                                Toast.makeText(mactivity,R.string.update_content,Toast.LENGTH_SHORT).show();
                                user.setName(et_content);
                                if (jsonObject.getString("score") != null &&jsonObject.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    final Dialog dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("积分\t\t+"+json.getString("score"));
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
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case UPDATEEMAIL:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")){
                                JSONObject jsonObject = new JSONObject(json.getString("data"));
                                Toast.makeText(mactivity,R.string.update_content,Toast.LENGTH_SHORT).show();
                                user.setName(et_content);
                                if (jsonObject.getString("score") != null &&jsonObject.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    final Dialog dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("积分\t\t+"+json.getString("score"));
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
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case UPDATENASCHOOL:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")){
                                JSONObject jsonObject = new JSONObject(json.getString("data"));
                                Toast.makeText(mactivity,R.string.update_content,Toast.LENGTH_SHORT).show();
                                user.setName(et_content);
                                if (jsonObject.getString("score") != null &&jsonObject.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    final Dialog dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("积分\t\t+"+json.getString("score"));
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
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case UPDATENAREALNAME:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")){
                                JSONObject jsonObject = new JSONObject(json.getString("data"));
                                Toast.makeText(mactivity,R.string.update_content,Toast.LENGTH_SHORT).show();
                                user.setRealname(et_content);
                                if (jsonObject.getString("score") != null &&jsonObject.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    final Dialog dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("积分\t\t+"+json.getString("score"));
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
                                finish();
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_update);
        mactivity = this;
        user = new UserBean(mactivity);
        intent = getIntent();
        updatetype = intent.getStringExtra("updatetype");
        myinfoupdate();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
    }

    private void myinfoupdate() {
        update_save = (TextView) findViewById(R.id.myinfo_update_save);
        update_save.setOnClickListener(this);
        update_top_title = (TextView) findViewById(R.id.top_title);
        update_content = (EditText) findViewById(R.id.myinbfo_update_content);
        back = (RelativeLayout) findViewById(R.id.btn_back);
        back.setOnClickListener(this);
        if ("name".equals(updatetype)) {
            update_top_title.setText(R.string.my_info_update_title_name);
        } else if ("sex".equals(updatetype)) {
            update_top_title.setText(R.string.my_info_update_title_sex);
        } else if ("phone".equals(updatetype)) {
            update_top_title.setText(R.string.my_info_update_title_phone);
        } else if ("email".equals(updatetype)) {
            update_top_title.setText(R.string.my_info_update_title_email);
        } else if ("school".equals(updatetype)) {
            update_top_title.setText(R.string.my_info_update_title_school);
        }else if ("realname".equals(updatetype)){
            update_top_title.setText("修改真实姓名");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myinfo_update_save:
                if ("name".equals(updatetype)) {
                    et_content = update_content.getText().toString();
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).updateusername(user.getUserid(), et_content, UPDATENAME);
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                } else if ("sex".equals(updatetype)) {
                    et_content = update_content.getText().toString();
                    if(et_content.equals("女")){
                        woman = "0";
                        if (HttpConnect.isConnnected(mactivity)) {
                            new StudyRequest(mactivity, handler).updateUserSex(user.getUserid(), woman, UPDATESEX);
                        } else {
                            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                        }
                    }else if (et_content.equals("男")){
                        man = "1";
                        if (HttpConnect.isConnnected(mactivity)) {
                            new StudyRequest(mactivity, handler).updateUserSex(user.getUserid(), man, UPDATESEX);
                        } else {
                            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(mactivity,R.string.my_info_update_womanorman,Toast.LENGTH_SHORT).show();
                    }

                } else if ("phone".equals(updatetype)) {
                    et_content = update_content.getText().toString();
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).updateUserPhone(user.getUserid(), et_content, UPDATEPHONE);
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                } else if ("email".equals(updatetype)) {
                    et_content = update_content.getText().toString();
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).updateUserEmail(user.getUserid(), et_content, UPDATEEMAIL);
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                } else if ("school".equals(updatetype)) {
                    et_content = update_content.getText().toString();
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).updateUserSchool(user.getUserid(), et_content, UPDATENASCHOOL);
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                }else if ("realname".equals(updatetype)){
                    et_content = update_content.getText().toString();
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).UpdateUserRealName(user.getUserid(), et_content, UPDATENAREALNAME);
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            newstypebean = (News_list_type.DataBean) intent.getSerializableExtra("fndinfo");

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("是否点击查看")
                    .setCancelable(false)
                    .setPositiveButton("立即查看", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("fndinfo", newstypebean);
                            Intent intent = new Intent(mactivity, News_WebView_url.class);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mactivity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
//            AlertDialog alert = builder.create();
//            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            alert.show();
        }
    }
}
