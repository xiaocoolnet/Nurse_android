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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
 * Created by zhuchongkun on 2016/12/31.
 * 护士站--圈子-我——认证
 */

public class MyAuthenticationActivity extends Activity implements View.OnClickListener {
    private String TAG = "MyAuthenticationActivity";
    private int INTENT_RESULT=101;
    private UserBean user;
    private Context mContext;
    private EditText et_company, et_departments;
    private Spinner sp_type;
    private ImageView iv_relevant_documents;
    private LinearLayout ll_submit;
    private RelativeLayout rl_back;
    private String company,departments;
    private String auth_type;
    // 保存的文件的路径
    @SuppressLint("SdCardPath")
    private String filepath = "/sdcard/myheader";
    private String picname = "";
    private String head;
    private String relevant_documents_path;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_authentication);
        mContext = this;
        user = new UserBean(mContext);
        inintView();
    }

    private void inintView() {
        sp_type= (Spinner) findViewById(R.id.spinner_type);
        et_company = (EditText) findViewById(R.id.et_fragment_my_authentication_company);
        et_departments = (EditText) findViewById(R.id.et_fragment_my_authentication_departments);
        iv_relevant_documents = (ImageView) findViewById(R.id.iv_fragment_my_authentication_relevant_documents);
        iv_relevant_documents.setOnClickListener(this);
        rl_back = (RelativeLayout) findViewById(R.id.rl_fragment_my_authentication_back);
        rl_back.setOnClickListener(this);
        ll_submit = (LinearLayout) findViewById(R.id.ll_fragment_my_authentication_submit);
        ll_submit.setOnClickListener(this);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] languages = getResources().getStringArray(R.array.authentication_spinner);
                sp_type.setSelection(pos);
                auth_type=languages[pos];
                LogUtils.e(TAG,"-auth_type-"+auth_type);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fragment_my_authentication_back:
                finish();
                break;
            case R.id.iv_fragment_my_authentication_relevant_documents:
                ShowPickDialog();
                break;
            case R.id.ll_fragment_my_authentication_submit:
                picname = "relevant_documents";
                ToastUtils.ToastShort(mContext, "提交申请");
                if (isCanSubmit()){
                    authenticationPerson();
                }
                break;
        }
    }

    private boolean isCanSubmit() {
        if (HttpConnect.isConnnected(mContext)) {
            company = et_company.getText().toString();
            departments = et_departments.getText().toString();
            if (TextUtils.isEmpty(auth_type)) {
                ToastUtils.ToastShort(mContext, "请选择认证类型");
                return false;
            }
            if (TextUtils.isEmpty(relevant_documents_path)) {
                ToastUtils.ToastShort(mContext, "请上传相关证件");
                return false;
            }
        } else {
            ToastUtils.ToastShort(mContext, "网络问题，请稍后重试！");
            return false;
        }
        return true;
    }

    private void authenticationPerson(){
//        userid,auth_type(认证类型 关联字典),auth_company(认证单位),auth_department(工作科室),photo(证件照)
        RequestParams params = new RequestParams();
        params.put("userid",user.getUserid());
        params.put("auth_type",auth_type);
        params.put("auth_company",company);
        params.put("auth_department",departments);
        params.put("photo",relevant_documents_path);
        NurseAsyncHttpClient.get(CommunityNetConstant.AUTHENTICATION_PERSON,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response!=null){
                    LogUtils.e(TAG,"relevant_documents_path-->"+relevant_documents_path);
                    LogUtils.e(TAG,"applyCommunity-->"+response.toString());
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
            iv_relevant_documents.setImageDrawable(drawable);
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
                        relevant_documents_path = response.getString("data");
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
        String name = ImageName + TimeToolUtils.getNowTime() + user.getUserid();
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
