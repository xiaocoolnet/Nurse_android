package chinanurse.cn.nurse.Fragment_Nurse_mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.Fragment_Nurse_mine.mine_info.MyInFo_Update;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.picture.RoudImage;
import chinanurse.cn.nurse.popWindow.Pop_mine_birthday;
import chinanurse.cn.nurse.popWindow.Pop_mine_location;
import chinanurse.cn.nurse.popWindow.Pop_spinner;
import chinanurse.cn.nurse.ui.HeadPicture;

public class Myinfo extends AppCompatActivity implements View.OnClickListener {

    private static final int MYGRTUSERINFO = 1;
    private static final int KEY = 2;
    private static final int UPDATEBIRTHDAY = 4;
    private static final int UPDATESEX = 5;
    private static final int UPDATE_AVATAR_KEY = 6;
    private LinearLayout back;
    private RelativeLayout update_head, update_name, update_sex, update_phone, update_email, update_birthday,
            update_address, update_school, update_profession, update_education,real_name;
    private Activity mactivity;
    private UserBean user;
    private Intent intent;

    private String updatetype, head, date, titlename;
    private RoudImage show_head_image;
    private Calendar calendar;// 用来装日期的
    private DatePickerDialog dialog;
    // 保存的文件的路径
    @SuppressLint("SdCardPath")
    private String filepath = "/sdcard/myheader";
    private String picname = "newpic",woman,et_content,all_info;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁
    private DisplayImageOptions options;

    private Pop_mine_location poplocation;
    private Pop_spinner popspinner;
    private Pop_mine_birthday popbirthday;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private TextView tv_name, tv_sex, tv_phone, tv_email, tv_birthday, tv_address, tv_school, tv_major, tv_education, textview,realname;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MYGRTUSERINFO:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                JSONObject obj = new JSONObject(data);
                                user.setUserid(obj.getString("id"));
                                Log.i("userid", "--------->" + user.getUserid());
                                user.setName(obj.getString("name"));
                                user.setPhone(obj.getString("phone"));
                                user.setCity(obj.getString("city"));
                                user.setEmail(obj.getString("email"));
                                user.setQq(obj.getString("qq"));
                                user.setWeixin(obj.getString("weixin"));
                                user.setPhoto(obj.getString("photo"));
                                user.setSchool(obj.getString("school"));
                                user.setAddress(obj.getString("address"));
                                user.setMajor(obj.getString("major"));
                                user.setEducation(obj.getString("education"));
                                user.setFanscount(obj.getString("fanscount"));
                                user.setMoney(obj.getString("money"));
                                user.setSex(obj.getString("sex"));
                                user.setRealname(obj.getString("realname"));
                                user.setBirthday(obj.getString("birthday"));
                                all_info = obj.getString("all_information");
                                showpersonal();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case UPDATEBIRTHDAY:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                JSONObject jsonObject = new JSONObject(data);
                                Toast.makeText(mactivity, R.string.update_content, Toast.LENGTH_SHORT).show();
                                user.setName(tv_birthday.getText().toString());
                                getPersoanl();
                                if (jsonObject.getString("score") != null &&jsonObject.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    final Dialog dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("积分\t\t+"+jsonObject.getString("score"));
                                    TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                    tv_score_name.setText(jsonObject.getString("event"));
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
                case UPDATESEX:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")){
                                JSONObject jsonObject = new JSONObject(data);
                                Toast.makeText(mactivity,R.string.update_content,Toast.LENGTH_SHORT).show();
                                user.setName(et_content);
                                getPersoanl();
                                if (jsonObject.getString("score") != null &&jsonObject.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    final Dialog dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("积分\t\t+"+jsonObject.getString("score"));
                                    TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                    tv_score_name.setText(jsonObject.getString("event"));
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

                case KEY://上传头像
                    String key = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(key);
                        String state=json.getString("status");
                        if ("success".equals(state)) {
//                            JSONObject js=json.getJSONObject("data");
                            String path=json.getString("data");
//                            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/"+path, iv_head, options);
                            if (HttpConnect.isConnnected(mactivity)) {
                                new StudyRequest(mactivity, handler).updateUserAvatar(user.getUserid(), path, UPDATE_AVATAR_KEY);
                            }else{
                                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(mactivity,"上传头像失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case UPDATE_AVATAR_KEY://修改头像
                    String data = (String) msg.obj;
                    try {
                        JSONObject json = new JSONObject(data);
                        String state=json.getString("status");
                        if ("success".equals(state)) {
                            JSONObject jsonObject = new JSONObject(json.getString("data"));
                            Toast.makeText(mactivity,"修改头像成功！", Toast.LENGTH_SHORT).show();
                            getPersoanl();
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

                        }else{
                            Toast.makeText(mactivity,"修改头像失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };



    private void showpersonal() {
        //头部图片
        if (user.getPhone() != null&&user.getPhone().length() > 0) {
            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + user.getPhoto(), show_head_image, options);
        } else {
            new HeadPicture().getHeadPicture(show_head_image);
        }
//        姓名
        if (user.getName().length() > 0) {
            tv_name.setText(user.getName());
        } else {
            tv_name.setText(R.string.my_info_update_result);
        }
        //性别
        if (user.getSex().length() > 0) {
            if ("0".equals(user.getSex())) {
                tv_sex.setText(R.string.my_info_update_woman);
            } else {
                tv_sex.setText(R.string.my_info_update_man);
            }
        } else {
            tv_sex.setText(R.string.my_info_update_result);
        }
        //手机
        if (user.getPhone().length() > 0) {
            tv_phone.setText(user.getPhone());
        } else {
            tv_phone.setText(R.string.my_info_update_result);
        }
        //邮箱
        if (user.getEmail().length() > 0) {
            tv_email.setText(user.getEmail());
        } else {
            tv_email.setText(R.string.my_info_update_result);
        }
        //生日
        if (user.getBirthday().length() > 0) {
            tv_birthday.setText(user.getBirthday());
        } else {
            tv_birthday.setText(R.string.my_info_update_result);
        }
        //地址
        if (user.getAddress().length() > 0) {
            tv_address.setText(user.getAddress());
        } else {
            tv_address.setText(R.string.my_info_update_result);
        }
        //学校
        if (user.getSchool().length() > 0) {
            tv_school.setText(user.getSchool());
        } else {
            tv_school.setText(R.string.my_info_update_result);
        }
        //专业
        if (user.getMajor().length() > 0) {
            tv_major.setText(user.getMajor());
        } else {
            tv_school.setText(R.string.my_info_update_result);
        }
        //学历
        if (user.getEducation() != null && !"".equals(user.getEducation())) {
            tv_education.setText(user.getEducation());
        } else {
            tv_education.setText(R.string.my_info_update_result);
        }
        //真实姓名
        if (user.getRealname() != null && !"".equals(user.getRealname())) {
            realname.setText(user.getRealname());
        } else {
            realname.setText(R.string.my_info_update_result);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        mactivity = this;
        user = new UserBean(mactivity);
        poplocation = new Pop_mine_location(Myinfo.this);
        popspinner = new Pop_spinner(Myinfo.this);
        popbirthday = new Pop_mine_birthday(Myinfo.this);
        iniview();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);


    }

    private void iniview() {
        //头部
        update_head = (RelativeLayout) findViewById(R.id.myinfo_update_head);
        show_head_image = (RoudImage) findViewById(R.id.myinof_image_head_show);
        update_head.setOnClickListener(this);

        //姓名
        update_name = (RelativeLayout) findViewById(R.id.myinfo_update_username);
        tv_name = (TextView) findViewById(R.id.tv_update_name);
        update_name.setOnClickListener(this);

        //性别
        update_sex = (RelativeLayout) findViewById(R.id.myinfo_update_sex);
        tv_sex = (TextView) findViewById(R.id.tv_update_sex);
        tv_sex.setText(user.getSex());
        update_sex.setOnClickListener(this);

        //手机
        update_phone = (RelativeLayout) findViewById(R.id.myinfo_update_phone);
        tv_phone = (TextView) findViewById(R.id.tv_update_phone);
        update_phone.setOnClickListener(this);
        //真实姓名
        real_name = (RelativeLayout) findViewById(R.id.myinfo_update_realname);
        realname = (TextView) findViewById(R.id.real_name);
        real_name.setOnClickListener(this);

        //邮箱
        update_email = (RelativeLayout) findViewById(R.id.myinfo_update_email);
        tv_email = (TextView) findViewById(R.id.tv_update_email);
        update_email.setOnClickListener(this);

        //生日
        update_birthday = (RelativeLayout) findViewById(R.id.myinfo_update_birthday);
        tv_birthday = (TextView) findViewById(R.id.tv_update_birthday);
        update_birthday.setOnClickListener(this);

        //地址
        update_address = (RelativeLayout) findViewById(R.id.myinfo_update_address);
        tv_address = (TextView) findViewById(R.id.myinfo_righttext_text);
        update_address.setOnClickListener(this);

        //学校
        update_school = (RelativeLayout) findViewById(R.id.myinfo_update_school);
        tv_school = (TextView) findViewById(R.id.tv_update_school);
        update_school.setOnClickListener(this);

        //专业
        update_profession = (RelativeLayout) findViewById(R.id.myinfo_update_profession);
        tv_major = (TextView) findViewById(R.id.tv_update_major);
        update_profession.setOnClickListener(this);

        //学历
        update_education = (RelativeLayout) findViewById(R.id.myinfo_update_education);
        tv_education = (TextView) findViewById(R.id.tv_update_education);
        update_education.setOnClickListener(this);

        back = (LinearLayout) findViewById(R.id.myinfo_back);
        back.setOnClickListener(this);

        textview = (TextView) findViewById(R.id.textview);
        // 显示图片的配置
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_head_nor).showImageOnFail(R.mipmap.img_head_nor).cacheInMemory(true).cacheOnDisc(true).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myinfo_back:
                unregisterReceiver(receiver);
                finish();
                break;
            case R.id.myinfo_update_realname:
                intent = new Intent(mactivity, MyInFo_Update.class);
                intent.putExtra("updatetype", "realname");
                startActivity(intent);
                break;
            case R.id.myinfo_update_head:
                getCheck();
                ShowPickDialog();// 点击更换头像
                break;
            case R.id.myinfo_update_username:
                intent = new Intent(mactivity, MyInFo_Update.class);
                intent.putExtra("updatetype", "name");
                startActivity(intent);
                break;
            case R.id.myinfo_update_sex:
                final String[] sexchoose = new String[]{"女", "男"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Myinfo.this);
                builder.setTitle("请选择性别");
                builder.setSingleChoiceItems(sexchoose, 0, new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        if ("女".equals(sexchoose[which])) {
                            et_content = sexchoose[which];
                            woman = "0";
                            if (HttpConnect.isConnnected(mactivity)) {
                                new StudyRequest(mactivity, handler).updateUserSex(user.getUserid(), woman, UPDATESEX);
                            } else {
                                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            et_content = sexchoose[which];
                            woman = "1";
                            if (HttpConnect.isConnnected(mactivity)) {
                                new StudyRequest(mactivity, handler).updateUserSex(user.getUserid(), woman, UPDATESEX);
                            } else {
                                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                            }
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
            case R.id.myinfo_update_phone:
                intent = new Intent(mactivity, MyInFo_Update.class);
                intent.putExtra("updatetype", "phone");
                startActivity(intent);
                break;
            case R.id.myinfo_update_email:
                intent = new Intent(mactivity, MyInFo_Update.class);
                intent.putExtra("updatetype", "email");
                startActivity(intent);
                break;
            case R.id.myinfo_update_birthday:

//                //获取当前系统时间
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//                date = formatter.format(new Date());
//                calendar = Calendar.getInstance();
//                dialog = new DatePickerDialog(Myinfo.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                Log.i("calender", "年-->" + year + "月-->" + monthOfYear + "日-->" + dayOfMonth);
//                                if (year > Integer.valueOf(date)) {
//                                    Toast.makeText(mactivity, "请选择正确的出生日期", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    int monthofyear = monthOfYear + 1;
//                                    tv_birthday.setText(year + "-" + monthofyear + "-"
//                                            + dayOfMonth);
//                                    if (HttpConnect.isConnnected(mactivity)) {
//                                        new StudyRequest(mactivity, handler).updateUserBirthday(user.getUserid(), tv_birthday.getText().toString(), UPDATEBIRTHDAY);
//                                    } else {
//                                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
//                                    }
//                                    getPersoanl();
//                                }
//                            }
//                        }, calendar.get(Calendar.YEAR), calendar
//                        .get(Calendar.MONTH), calendar
//                        .get(Calendar.DAY_OF_MONTH));
//                dialog.show();
                popbirthday.showAsDropDown(textview);
                break;
            case R.id.myinfo_update_address:
                poplocation.showAsDropDown(textview);
                break;
            case R.id.myinfo_update_school:
                intent = new Intent(mactivity, MyInFo_Update.class);
                intent.putExtra("updatetype", "school");
                startActivity(intent);
                break;
            case R.id.myinfo_update_profession:
                titlename = "专业";
                popspinner.showAsDropDown(update_profession, titlename);
                break;
            case R.id.myinfo_update_education:
                titlename = "学历";
                popspinner.showAsDropDown(update_education, titlename);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "----------->onResume");
        getPersoanl();
    }

    public void getPersoanl() {
        if (HttpConnect.isConnnected(mactivity)) {
            new StudyRequest(mactivity, handler).getuserinfo(user.getUserid(), MYGRTUSERINFO);
        } else {
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }
    //6.0申请动态权限
    private void getCheck() {
        if (ContextCompat.checkSelfPermission(mactivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(mactivity, new String[]{Manifest.permission.CAMERA},
                    100);
        }
        if (ContextCompat.checkSelfPermission(mactivity, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(mactivity, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                    100);
        }
        if (ContextCompat.checkSelfPermission(mactivity, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(mactivity, new String[]{Manifest.permission.READ_PHONE_STATE},
                    100);
        }
        if (ContextCompat.checkSelfPermission(mactivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(mactivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }
        if (ContextCompat.checkSelfPermission(mactivity, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(mactivity, new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},
                    100);
        }
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
//        if (requestCode==100){
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            } else {
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.RECEIVE_BOOT_COMPLETED)){
//                    Toast.makeText(mactivity,"没有权限,请去应用设置权限",Toast.LENGTH_SHORT).show();
//                }
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.CAMERA)){
//                    Toast.makeText(mactivity,"没有权限,请去应用设置权限",Toast.LENGTH_SHORT).show();
//                }
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.RECEIVE_BOOT_COMPLETED)){
//                    Toast.makeText(mactivity,"没有权限,请去应用设置权限",Toast.LENGTH_SHORT).show();
//                }
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//                    Toast.makeText(mactivity,"没有权限,请去应用设置权限",Toast.LENGTH_SHORT).show();
//                }
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(mactivity, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)){
//                    Toast.makeText(mactivity,"没有权限,请去应用设置权限",Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
    protected void ShowPickDialog() {
        new AlertDialog.Builder(this, android.app.AlertDialog.THEME_HOLO_LIGHT).setNegativeButton("相册", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(intentFromGallery, PHOTO_REQUEST_ALBUM);

            }
        }).setPositiveButton("拍照", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File file = new File(path, "newpic.jpg");
                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                }

                startActivityForResult(intentFromCapture, PHOTO_REQUEST_CAMERA);
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:// 相册
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, "newpic.jpg");
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PHOTO_REQUEST_ALBUM:// 图库
                    startPhotoZoom(data.getData());
                    break;

                case PHOTO_REQUEST_CUT: // 图片缩放完成后
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param data
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(this.getResources(), photo);
            show_head_image.setImageDrawable(drawable);
            picname = "avatar" + user.getUserid() + String.valueOf(new Date().getTime());
            storeImageToSDCARD(photo, picname, filepath);
            if (HttpConnect.isConnnected(mactivity)) {
                new StudyRequest(mactivity, handler).updateUserImg(head, KEY);
            } else {
                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * storeImageToSDCARD 将bitmap存放到sdcard中
     */
    public void storeImageToSDCARD(Bitmap colorImage, String ImageName, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        File imagefile = new File(file, ImageName + ".jpg");
        try {
            imagefile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imagefile);
            colorImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            head = imagefile.getPath();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
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
