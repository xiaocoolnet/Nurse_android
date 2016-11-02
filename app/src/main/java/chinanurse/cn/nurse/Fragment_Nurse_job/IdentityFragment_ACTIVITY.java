package chinanurse.cn.nurse.Fragment_Nurse_job;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Date;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.bean.mine_main_bean.Company_news_beans;


public class IdentityFragment_ACTIVITY extends Activity implements View.OnClickListener
{
    private static final int COMPANYCERTIFY = 7;
    private static final int RESULT_CANCELED = 0;
    private static final int GETCOMANYCERTIFY = 8;
    private EditText et_company_name,et_company_synopsis,et_company_linkman,et_company_phone,et_company_email;
    private String company_name,company_synopsis,company_linkman,company_phone,company_email,path,company_time,head;
    private LinearLayout company_post_photo;
    private TextView tv_submit;
    private ImageView company_post_text,company_post_text_one;
    private Activity activity;
    private UserBean user;
    private SimpleDateFormat mdata;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    // 保存的文件的路径
    @SuppressLint("SdCardPath")
    private String filepath = "/sdcard/myheader";
    private String picname = "newpic";
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁
    public static final int UPDATE_AVATAR_KEY = 5;
    private static final int KEY = 6;
    private boolean isfalse = true;
    private RelativeLayout btnback;
    private TextView toptitle;
    private LinearLayout ril_shenhechenggong,ril_renzhengshibai,ril_zhengzaishenhe;
    private Company_news_beans.DataBean combean;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEY://上传头像
                    String key = (String) msg.obj;
                    if (key != null) {
                        try {
                            JSONObject json = new JSONObject(key);
                            String state = json.getString("status");
                            if ("success".equals(state)) {
                                path = json.getString("data");
                                imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + path, company_post_text, options);
                            } else {
                                Toast.makeText(activity, "上传图像失败，请重试！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(activity, "上传图像失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case COMPANYCERTIFY:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                            try {
                                JSONObject json = new JSONObject(result);
                                if ("success".equals(json.optString("status"))){
                                    Toast.makeText(activity,"发布成功",Toast.LENGTH_SHORT).show();
                                    companyviews();
                                }else{
                                    Toast.makeText(activity,"发布失败",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(activity,"发布失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case GETCOMANYCERTIFY:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String data = json.getString("data");
                            if ("success".equals(json.optString("status"))){
                                JSONObject item =  new JSONObject(data);
                                combean = new Company_news_beans.DataBean();
                                combean.setId(item.getString("id"));
                                combean.setUserid(item.getString("userid"));
                                combean.setCreate_time(item.getString("create_time"));
                                combean.setCompanyname(item.getString("companyname"));
                                combean.setCompanyinfo(item.getString("companyinfo"));
                                combean.setLinkman(item.getString("linkman"));
                                combean.setPhone(item.getString("phone"));
                                combean.setEmail(item.getString("email"));
                                combean.setLicense(item.getString("license"));
                                combean.setStatus(item.getString("status"));
                                if (!"".equals(item.getString("status"))&&item.getString("status") != null){
                                    if ("1".equals(item.getString("status"))){
                                        et_company_name.setText(combean.getCompanyname());
                                        et_company_synopsis.setText(combean.getCompanyinfo());
                                        et_company_linkman.setText(combean.getLinkman());
                                        et_company_phone.setText(combean.getPhone());
                                        et_company_email.setText(combean.getEmail());
                                        imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + combean.getLicense(), company_post_text, options);
//                                     company_post_photo
                                        enable(false);
                                        tv_submit.setText("修改");
                                        company_post_text_one.setVisibility(View.GONE);
                                    }else if ("0".equals(item.getString("status"))){
                                        et_company_name.setText("");
                                        et_company_synopsis.setText("");
                                        et_company_linkman.setText("");
                                        et_company_phone.setText("");
                                        et_company_email.setText("");
                                        enable(true);
                                    }else if ("-1".equals(item.getString("status"))){
                                        ril_shenhechenggong.setVisibility(View.GONE);
                                        ril_renzhengshibai.setVisibility(View.VISIBLE);
                                        ril_zhengzaishenhe.setVisibility(View.GONE);
                                        tv_submit.setText("请重新认证");
//                                     company_post_photo
                                    }else if ("-2".equals(item.getString("status"))){
                                        ril_shenhechenggong.setVisibility(View.GONE);
                                        ril_renzhengshibai.setVisibility(View.GONE);
                                        ril_zhengzaishenhe.setVisibility(View.VISIBLE);
                                        identity_submit.setVisibility(View.GONE);
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        et_company_name.setText("");
                        et_company_synopsis.setText("");
                        et_company_linkman.setText("");
                        et_company_phone.setText("");
                        et_company_email.setText("");
                        enable(true);
                    }
                    break;
            }
        }
    };

    private LinearLayout identity_submit;
    private TextView title_top;
    private RelativeLayout btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_identity_activity);
        activity = this;
        user = new UserBean(activity);
        indentityview();
    }
    private void indentityview() {
        btnback = (RelativeLayout) findViewById(R.id.btn_back);
        btnback.setOnClickListener(this);
        toptitle = (TextView) findViewById(R.id.top_title);
        toptitle.setText("企业认证");
        ril_shenhechenggong = (LinearLayout) findViewById(R.id.ril_shenhechenggong);
        ril_renzhengshibai = (LinearLayout) findViewById(R.id.ril_renzhengshibai);
        ril_zhengzaishenhe = (LinearLayout) findViewById(R.id.ril_zhengzaishenhe);

        identity_submit = (LinearLayout) findViewById(R.id.identity_submit);
        identity_submit.setOnClickListener(this);
        et_company_name = (EditText) findViewById(R.id.company_name);
        et_company_synopsis = (EditText) findViewById(R.id.company_synopsis);
        et_company_linkman = (EditText) findViewById(R.id.company_linkman);
        et_company_phone = (EditText) findViewById(R.id.company_phone);
        et_company_email = (EditText) findViewById(R.id.company_email);
        company_post_photo = (LinearLayout) findViewById(R.id.company_post_photo);
        company_post_photo.setOnClickListener(this);
        company_post_text = (ImageView) findViewById(R.id.company_post_text);//改text为已经上传
        company_post_text_one = (ImageView) findViewById(R.id.company_post_text_one);
        //获取当前时间
        mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        company_time = formatdatatime(System.currentTimeMillis());
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.ic_shangchuan).showImageOnFail(R.mipmap.ic_shangchuan).cacheInMemory(true).cacheOnDisc(true).build();
    }
    private String formatdatatime(long time){
        if (0==time){
            return "";
        }
        return mdata.format(new Date(time));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.identity_submit:
                if ("修改".equals(tv_submit.getText().toString())){
                    enable(true);
                    posynews();
                }else if ("提交".equals(tv_submit.getText().toString())){
                    enable(true);
                    posynews();
                }else if ("请重新认证".equals(tv_submit.getText().toString())){
                    ril_shenhechenggong.setVisibility(View.VISIBLE);
                    ril_renzhengshibai.setVisibility(View.GONE);
                    ril_zhengzaishenhe.setVisibility(View.GONE);
                    tv_submit.setText("提交");
                    enable(true);
                    posynews();
                }
                break;
            case R.id.company_post_photo:
                //上传图片
                getCheck();
                ShowPickDialog();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
    //6.0申请动态权限
    private void getCheck() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                    100);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                    100);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE},
                    100);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},
                    100);
        }
    }
    public void enable(boolean isfalse){
        et_company_name.setEnabled(isfalse);
        et_company_synopsis.setEnabled(isfalse);
        et_company_linkman.setEnabled(isfalse);
        et_company_phone.setEnabled(isfalse);
        et_company_email.setEnabled(isfalse);
        company_post_photo.setEnabled(isfalse);
    }
    protected void ShowPickDialog() {
        new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT).setNegativeButton("相册", new DialogInterface.OnClickListener() {
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

    private void posynews() {
        company_name = et_company_name.getText().toString();
        company_synopsis = et_company_synopsis.getText().toString();
        company_linkman = et_company_linkman.getText().toString();
        company_phone = et_company_phone.getText().toString();
        company_email = et_company_email.getText().toString();
        if (company_name == null || "".equals(company_name)){
            Toast.makeText(activity,"请输入公司名称",Toast.LENGTH_SHORT).show();
        }else if (company_synopsis == null || "".equals(company_synopsis)){
            Toast.makeText(activity,"请输入公司简介",Toast.LENGTH_SHORT).show();
        }else if (company_linkman == null || "".equals(company_linkman)){
            Toast.makeText(activity,"请输入联系人",Toast.LENGTH_SHORT).show();
        }else if (company_phone == null || "".equals(company_phone)){
            Toast.makeText(activity,"请输入联系电话",Toast.LENGTH_SHORT).show();
        }else if (company_email == null || "".equals(company_email)){
            Toast.makeText(activity,"请输入联系邮箱",Toast.LENGTH_SHORT).show();
        }else if (path == null || "".equals(path)){
            Toast.makeText(activity,"请上传图片",Toast.LENGTH_SHORT).show();
        }else{
            if (HttpConnect.isConnnected(activity)){
                new StudyRequest(activity,handler).UpdataCompanyCertify(user.getUserid(),company_name,company_synopsis,company_time,company_phone,company_email,company_linkman,path,COMPANYCERTIFY);
            }else{
                Toast.makeText(activity,R.string.net_erroy,Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        Toast.makeText(activity, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
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
            picname = "avatar" + user.getUserid() + String.valueOf(new Date().getTime());
            storeImageToSDCARD(photo, picname, filepath);
            if (HttpConnect.isConnnected(activity)) {
                new StudyRequest(activity, handler).updateUserImg(head, KEY);
            } else {
                Toast.makeText(activity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", "-------->onResume");
        companyviews();
    }

    private void companyviews() {
        if (HttpConnect.isConnnected(activity)){
            new StudyRequest(activity,handler).getCompanyCertify(user.getUserid(),GETCOMANYCERTIFY);
        }else{
            Toast.makeText(activity,R.string.net_erroy,Toast.LENGTH_SHORT).show();
        }
    }
}
