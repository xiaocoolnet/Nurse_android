package chinanurse.cn.nurse.Fragment_Nurse.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.TimeToolUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2016/12/30.
 * 护士站--圈子---发现—-圈子列表--圈子详情--圈子详细情况--申请圈主
 */

public class ApplyCommunityActivity extends Activity implements View.OnClickListener {
    private String TAG = "ApplyCommunityActivity";
    private int INTENT_RESULT=100;
    private UserBean user;
    private Context mContext;
    private EditText  et_real_name, et_ID, et_address, et_phone, et_qq, et_words;
    private String real_name, ID, address, phone, qq, words;
    private ImageView iv_hand_photo, iv_live_photo;
    private LinearLayout ll_submit;
    private RelativeLayout rl_back;
    private CommunityBean communityBean;
    // 保存的文件的路径
    @SuppressLint("SdCardPath")
    private String filepath = "/sdcard/myheader";
    private String picname = "";
    private String head;
    private String live_path, hand_path;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_apply_community);
        mContext = this;
        user = new UserBean(mContext);
        communityBean = (CommunityBean) getIntent().getSerializableExtra("community");
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_fragment_apply_community_back);
        rl_back.setOnClickListener(this);
        et_real_name = (EditText) findViewById(R.id.et_fragment_apply_community_real_name);
        et_ID = (EditText) findViewById(R.id.et_fragment_apply_community_ID);
        et_address = (EditText) findViewById(R.id.et_fragment_apply_community_address);
        et_phone = (EditText) findViewById(R.id.et_fragment_apply_community_phone);
        et_qq = (EditText) findViewById(R.id.et_fragment_apply_community_qq);
        et_words = (EditText) findViewById(R.id.et_fragment_apply_community_words);
        iv_hand_photo = (ImageView) findViewById(R.id.iv_fragment_apply_community_hand_photo);
        iv_hand_photo.setOnClickListener(this);
        iv_live_photo = (ImageView) findViewById(R.id.iv_fragment_apply_community_live_photo);
        iv_live_photo.setOnClickListener(this);
        ll_submit = (LinearLayout) findViewById(R.id.ll_fragment_apply_community_submit);
        ll_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fragment_apply_community_back:
                finish();
                break;
            case R.id.iv_fragment_apply_community_live_photo:
                picname = "live";
                ShowPickDialog();
                break;
            case R.id.iv_fragment_apply_community_hand_photo:
                picname = "hand";
                ShowPickDialog();
                break;
            case R.id.ll_fragment_apply_community_submit:
                if (isCanSubmit()){
                    applyCommunity();
                }
                break;
        }

    }

    private void applyCommunity() {
//      c_name(圈子名称),userid,real_name(真实姓名),real_code(身份证号),real_address(联系地址),real_tel(手机号),real_qq(qq),real_content(申请感言),real_photo(个人日志生活照),real_photo_2(手持身份证照片)
        RequestParams params = new RequestParams();
        params.put("c_name",communityBean.getName());
        params.put("userid",user.getUserid());
        params.put("real_name",real_name);
        params.put("real_code",ID);
        params.put("real_address",address);
        params.put("real_tel",phone);
        params.put("real_qq",qq);
        params.put("real_content",words);
        params.put("real_photo",live_path);
        params.put("real_photo_2",hand_path);
        NurseAsyncHttpClient.get(CommunityNetConstant.APPLY_COMMUNITY,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response!=null){
                    LogUtils.e(TAG,"hand_path-->"+hand_path);
                    LogUtils.e(TAG,"live_path-->"+live_path);
                    LogUtils.e(TAG,"applyCommunity-->"+response.toString());
                    ToastUtils.ToastShort(mContext, "提交申请");
//                    {
//                        "status": "success",
//                            "data": "success"
//                    }
                    try {
                        String status=response.getString("status");
                        if (status.equals("success")){
                            ToastUtils.ToastShort(mContext, "提交申请成功！");
                            setResult(INTENT_RESULT);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }

    private boolean isCanSubmit() {
        if (HttpConnect.isConnnected(mContext)) {
            real_name = et_real_name.getText().toString();
            ID = et_ID.getText().toString();
            address = et_address.getText().toString();
            phone = et_phone.getText().toString();
            qq = et_qq.getText().toString();
            words = et_words.getText().toString();
            if (TextUtils.isEmpty(real_name)) {
                ToastUtils.ToastShort(mContext, "请输入您的真实姓名");
                return false;
            }
            if (TextUtils.isEmpty(ID)) {
                ToastUtils.ToastShort(mContext, "请输入您的身份证号");
                return false;
            }
            if (TextUtils.isEmpty(address)) {
                ToastUtils.ToastShort(mContext, "请输入您的联系地址");
                return false;
            }
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.ToastShort(mContext, "请输入您的手机号码");
                return false;
            }
            if (TextUtils.isEmpty(qq)) {
                ToastUtils.ToastShort(mContext, "请输入您的QQ号码");
                return false;
            }
            if (TextUtils.isEmpty(words)) {
                ToastUtils.ToastShort(mContext, "请输入您的申请感言");
                return false;
            }
            if (TextUtils.isEmpty(live_path)) {
                ToastUtils.ToastShort(mContext, "请上传个人日常生活照");
                return false;
            }
            if (TextUtils.isEmpty(hand_path)) {
                ToastUtils.ToastShort(mContext, "请上传手持身份证照");
                return false;
            }
        } else {
            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
            return false;
        }
        return true;
    }

    protected void ShowPickDialog() {
        new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT)
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intentFromGallery = new Intent();
                        intentFromGallery.setType("image/*"); // 设置文件类型
                        intentFromGallery.setAction(Intent.ACTION_PICK);
                        startActivityForResult(intentFromGallery,
                                PHOTO_REQUEST_ALBUM);

                    }
                })
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent intentFromCapture = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        // 判断存储卡是否可以用，可用进行存储
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            File path = Environment
                                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                            File file = new File(path, picname + ".jpg");
                            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(file));
                        }

                        startActivityForResult(intentFromCapture,
                                PHOTO_REQUEST_CAMERA);
                    }
                }).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:// 相册
                    // 判断存储卡是否可以用，可用进行存储
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        File path = Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, picname + ".jpg");
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(mContext.getApplicationContext(),
                                "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
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
            if (picname.equals("live")) {
                iv_live_photo.setImageDrawable(drawable);
            } else if (picname.equals("hand")) {
                iv_hand_photo.setImageDrawable(drawable);
            }
            storeImageToSDCARD(photo, picname, filepath);
            if (head != null && head.length() > 0) {
                if (HttpConnect.isConnnected(mContext)) {
                    uploadImg();
                } else {
                    ToastUtils.ToastShort(mContext, getString(R.string.net_erroy));
                }
            } else {
                ToastUtils.ToastShort(mContext, "请重新拍照");
            }
        }
    }

    private void uploadImg() {
        File f = new File(head);
        RequestParams r = new RequestParams();
        try {
            r.put("upfile", f);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        NurseAsyncHttpClient.post(CommunityNetConstant.UPLOAD_AVATAR, r, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--response-->" + response.toString());
                try {
                    if (response.getString("status").equals("success")) {
                        if (picname.equals("live")) {
                            live_path = response.getString("data");
                        } else if (picname.equals("hand")) {
                            hand_path = response.getString("data");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * storeImageToSDCARD 将bitmap存放到sdcard中
     */
    public void storeImageToSDCARD(Bitmap colorImage, String ImageName,
                                   String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        String name= ImageName+ TimeToolUtils.getNowTime() + user.getUserid();
        File imagefile = new File(file, name + ".jpg");
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

}
