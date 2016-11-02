package chinanurse.cn.nurse.NurseFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.adapter.community.Community_gridview_adapter;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/7/12.
 */
public class Write_Community_Post extends Activity implements View.OnClickListener {
    private static final int GETBBSTYPE = 1;
    private static final int POSTCOMMUNITY = 2;
    private static final int ADD_IMG_KEY = 333;
    private Activity mactivity;
    private RelativeLayout btn_back, write_release, insert_image, choose_column;
    private TextView top_title, write_post_classify, answer_sunmit;
    private ImageView submit_image;
    private String type;
    private int typeid;
    private UserBean user;
    private String[] arrtitle;
    private GridView gridview_image;
    private String gettitle, getptcontent;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁
    private String[] getcontent;
    //    private String picname = "newpic";
    private ArrayList<String> titlelist = new ArrayList<String>();
    private EditText community_title, community_content;
    private List<Drawable> griviewimage = new ArrayList<Drawable>();
    private List<String> picurl = new ArrayList<>();
    private List<String> list_picname = new ArrayList<>();
    private String filepath = "/sdcard/myheader";
    private Community_gridview_adapter gridview_adapter;
    private String picname = "";
    private String imagePath;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETBBSTYPE:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
                                JSONArray array = obj.getJSONArray("data");
                                if (array != null || array.length() > 0) {
                                    arrtitle = new String[array.length()];
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject json = array.getJSONObject(i);
                                        typeid = i + 1;
                                        write_post_classify.setText(arrtitle[0]);
                                        arrtitle[i] = json.getString("name");
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case POSTCOMMUNITY:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            Log.e("YAG", obj.toString());
                            Log.e("YAG", status);


                            String data = obj.getString("data");
                            if ("success".equals(status)) {
                                Log.e("YAG", "image66666");

                                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
//                                new StudyRequest(mactivity, handler).POSTCOMMUNITY(user.getUserid(), String.valueOf(typeid), gettitle, getptcontent, picname, ADD_IMG_KEY);


                                if (list_picname.size() > 0) {
                                    for (int i = 0; i < list_picname.size(); i++) {
                                        Log.e("image", "image777777**********");
                                        new StudyRequest(mactivity, handler).updateUserImg(list_picname.get(i), ADD_IMG_KEY);
                                    }

                                } else {

                                    finish();
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;


                case ADD_IMG_KEY:
                    if (msg.obj == null) {
                        Toast.makeText(getApplicationContext(), "上传图片失败！", Toast.LENGTH_SHORT).show();
                    }

                    finish();
                    break;


            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_community_post);

/*
*
* 我一开始用的是安卓2.1
        此程序在2.1版本完全正常，但是换安卓4.0的系统这地方就抛出异常了，完全不执行httpClient.execute；这是版本不兼容问题吗？
*
*
* */
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectAll()   // or .detectAll() for all detectable problems
//                .penaltyLog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());


        mactivity = this;
        user = new UserBean(mactivity);
        writepostview();
    }

    private void writepostview() {
        top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText(R.string.community_Posting);
        answer_sunmit = (TextView) findViewById(R.id.question_answer_submit);
        answer_sunmit.setText("提交");
        submit_image = (ImageView) findViewById(R.id.question_answer_submit_image);
        submit_image.setBackgroundResource(R.mipmap.ic_gou_nor);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        write_release = (RelativeLayout) findViewById(R.id.community_write_release);
        write_release.setOnClickListener(this);
        insert_image = (RelativeLayout) findViewById(R.id.community_write_insert_image);
        insert_image.setOnClickListener(this);
        choose_column = (RelativeLayout) findViewById(R.id.community_write_choose_column);
        choose_column.setOnClickListener(this);
        write_post_classify = (TextView) findViewById(R.id.write_post_classify);
        community_title = (EditText) findViewById(R.id.et_community_write_title);
        community_content = (EditText) findViewById(R.id.et_community_write_content);
        gridview_image = (GridView) findViewById(R.id.community_write_gridview_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            //发布帖子
            case R.id.community_write_release:
                gettitle = community_title.getText().toString();
                getptcontent = community_content.getText().toString();
                if (gettitle.length() == 0) {
                    Toast.makeText(mactivity, "请填写标题！", Toast.LENGTH_SHORT).show();
                } else if (getptcontent.length() <= 5) {
                    Toast.makeText(mactivity, "正文不少于5个字", Toast.LENGTH_SHORT).show();
                } else {
                    if (HttpConnect.isConnnected(mactivity)) {
                        String urlstr = "";
                        for (int i = 0; i < list_picname.size(); i++) {
                            if (i <= list_picname.size() - 1) {
                                urlstr += list_picname.get(i).toString() + ",";
                            } else if (i == list_picname.size()) {
                                urlstr += list_picname.get(i).toString();
                            }
                        }

                        Log.i("urlstr", "========>" + urlstr);

//                        if (list_picname.size() > 0) {
//                            Log.e("image", "image777777**********");
////                            new StudyRequest(mactivity, handler).updateUserImg(urlstr, ADD_IMG_KEY);
//                            new StudyRequest(mactivity, handler).POSTCOMMUNITY(user.getUserid(), String.valueOf(typeid), gettitle, getptcontent, urlstr, POSTCOMMUNITY);
//
//                        } else {
//                            Log.e("image", "image777777_________**********");
//                            new StudyRequest(mactivity, handler).POSTCOMMUNITY(user.getUserid(), String.valueOf(typeid), gettitle, getptcontent, urlstr, POSTCOMMUNITY);
//                        }

                        new StudyRequest(mactivity, handler).POSTCOMMUNITY(user.getUserid(), String.valueOf(typeid), gettitle, getptcontent, urlstr, POSTCOMMUNITY);


                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                   }
                break;
            //上传图片
            case R.id.community_write_insert_image:
                showphoto();
                break;
            //选择分类
            case R.id.community_write_choose_column:
//                type
                new AlertDialog.Builder(Write_Community_Post.this).setTitle("请选择分类").
                        setItems(arrtitle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                typeid = which + 1;
                                write_post_classify.setText(arrtitle[which]);
                            }
                        }).create().show();
                break;
        }
    }

    private void showphoto() {
        new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setNegativeButton("相册", new DialogInterface.OnClickListener() {
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
                        File tempFile = new File(path, "newpic.png");
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
//        Bundle extras = data.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            String strpic = convertIconToString(photo);
//            picurl.add(strpic);
//            griviewimage.add(photo);
//            gridview_adapter = new Community_gridview_adapter(mactivity, griviewimage);
//            gridview_image.setAdapter(gridview_adapter);
//            picname = "avatar" + String.valueOf(new Date().getTime()) + ".png";
//            list_picname.add(picname);
//            storeImageToSDCARD(photo, picname, filepath);
//        }

        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(this.getResources(), photo);
            griviewimage.add(drawable);
            gridview_adapter = new Community_gridview_adapter(mactivity, griviewimage);
            gridview_image.setAdapter(gridview_adapter);
            picname = "avatar" + String.valueOf(new Date().getTime()) + ".png";
            list_picname.add(picname);
            storeImageToSDCARD(photo, picname, filepath);
        }

    }

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组

        return Base64.encodeToString(appicon, Base64.DEFAULT);


//
//        return "avatar" + String.valueOf(new Date().getTime());

    }

    /**
     * storeImageToSDCARD 将bitmap存放到sdcard中
     */
    public void storeImageToSDCARD(Bitmap colorImage, String ImageName, String path) {
//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdir();
//        }
//        File imagefile = new File(file, ImageName + ".jpg");
//        try {
//            imagefile.createNewFile();
//            FileOutputStream fos = new FileOutputStream(imagefile);
//            colorImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
////            head = imagefile.getPath();
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        File imagefile = new File(file, ImageName);
        try {
            imagefile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imagefile);
            colorImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            imagePath = imagefile.getPath();
            picurl.add(imagefile.getPath());

            Log.e("YAG", "YAG___imagePath____" + imagePath);
            Log.e("YAG", "YAG___picurl______" + picurl.size());

            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "---------->onResume");
        gettitlechoose();
    }

    private void gettitlechoose() {
        if (HttpConnect.isConnnected(mactivity)) {
            //获取头部分类
            new StudyRequest(mactivity, handler).GETBBSTYPE(GETBBSTYPE);
        } else {
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }
}
