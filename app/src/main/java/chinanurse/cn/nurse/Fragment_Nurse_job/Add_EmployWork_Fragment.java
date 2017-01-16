package chinanurse.cn.nurse.Fragment_Nurse_job;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.NetUtil;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.utils.RegexUtil;
import chinanurse.cn.nurse.adapter.Spinner_Adapter;
import chinanurse.cn.nurse.bean.MineResumeinfo;
import chinanurse.cn.nurse.bean.Spinner_Bean;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.picture.RoudImage;
import chinanurse.cn.nurse.popWindow.Pop_commuity_location;
import chinanurse.cn.nurse.popWindow.Pop_commuity_location_two;
import chinanurse.cn.nurse.popWindow.Pop_mine_birthdayone;

/**
 * 添加简历
 * Created by Administrator on 2016/7/11 0011.
 */
public class Add_EmployWork_Fragment extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,View.OnTouchListener{
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 3;// 相册
    private static final int PHOTO_REQUEST_ALBUM = 2;// 剪裁
    private static final int KEY = 4;
    public static final int UPDATE_AVATAR_KEY = 5;
    private static final int RESULT_CANCELED = 0;
    private static final int GETRESUMEINFO = 17;//获取个人简历
    private static final int SENDREMINFO = 18;
    private static final int UPDATAREMINFO = 19;

    private UserBean user;
    private Activity mactivity;

    private RadioGroup rg_work_state,rg_sex_state;
    private RadioButton rb_work_state_01, rb_work_state_02, rb_work_state_03,rb_sex_state_01,rb_sex_state_02;
    private EditText ed_name, ed_phone, ed_email, ed_evaluation;
    private Spinner tv_work_02, tv_work_education, tv_work_jobname, tv_money_02, tv_arrive_time_02, tv_money_want_02, tv_position_02, sheng_spinner_02, shi_spinner_02;
    private final int typeid_12 = 12, typeid_13 = 13, typeid_14 = 14, typeid_15 = 15, typeid_16 = 16, typeid_1 = 1, typeid_6 = 6;
    private List<Spinner_Bean.DataBean> data_list_12, data_list_13, data_list_14, data_list_15, data_list_16, spinnerlist_1, spinnerlist_6;
    private Spinner_Adapter spinner_adapter;
    private RoudImage img_head;//头像
    // 保存的文件的路径
    @SuppressLint("SdCardPath")
    private String filepath = "/sdcard/myheader", head, date, jobstatenew,sexnew;
    private String picname = "newpic";

    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private TextView tv_head, title_top, tv, tv_save;
    private MineResumeinfo.DataBean mrinfo;
//    private AdapterView tv;

    private TextView birthady_year, birthady_month, birthady_day, textview, sheng_spinner, shi_spinner, qu_spinner, shi_spinner02, qu_spinner02;
    private LinearLayout birthday_data, linear_location, city_two_linear, re_save, linear_button;
    private Dialog dialog;
    private Calendar calendar;// 用来装日期的

    private String year, month, day, shengcity, shicity, qucity, twoshicity, twoqucity;

    private int merry_num = 0, work_num = 0, is_save = 0, is_name = 0,is_phone = 0,is_email = 0,is_evmine = 0,is_sex = 0;

    private List<String> datalist2, datalist13, datalist14, datalist15, datalist16,datalist6;
    private List<String> datalist1 = new ArrayList<String>();

    private ArrayAdapter<String> dataadapter2, dataadapter13, dataadapter14, dataadapter15, dataadapter16, dataadapter1, dataadapter6;

    private String userid, avatar, name, experience, educationdata, jobnamedata, money02data, arrivetime, jobWantdata, wantmoneydata,
            sex, birthday, marital, address, path, jobstate, currentsalary, phone, email,
            hiredate, wantcity, wantsalary, wantposition, description;

    private Pop_commuity_location popcomm;
    private Pop_commuity_location_two popcomtwo;
    private RelativeLayout btn_back;

    private Boolean iS = true;
    private Boolean isname = true;
    private Boolean isnameone = true;
    private Pop_mine_birthdayone mineborthday;


    private Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SENDREMINFO:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
                                Toast.makeText(mactivity, "保存成功", Toast.LENGTH_SHORT).show();
                                mactivity.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(mactivity, "保存失败", Toast.LENGTH_SHORT).show();
                }
                    break;

                case UPDATAREMINFO:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        if (result != null){


                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
                                Toast.makeText(mactivity, "保存成功", Toast.LENGTH_SHORT).show();
                                mactivity.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }  }
                    }else{
                        Toast.makeText(mactivity, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case typeid_12:
                    JSONObject jsonObject1 = (JSONObject) msg.obj;
                    if (jsonObject1 != null){
                    try {
                        String status = jsonObject1.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            data_list_12 = new ArrayList<Spinner_Bean.DataBean>();
                            datalist2 = new ArrayList<String>();
                            data_list_12.clear();
                            datalist2.clear();
                            datalist2.add("请选择");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                String moneyname = jo.getString("name");
                                isname = moneyname.contains("&lt;");
                                isnameone = moneyname.contains("&gt;");
                                if (isname){
                                    dataBean.setName("<"+jo.getString("name").substring(4));
                                    data_list_12.add(dataBean);
                                    datalist2.add("<"+jo.getString("name").substring(4));
                                }else if (isnameone){
                                    dataBean.setName(">"+jo.getString("name").substring(4));
                                    data_list_12.add(dataBean);
                                    datalist2.add(">"+jo.getString("name").substring(4));
                                }else{
                                    dataBean.setName(jo.getString("name"));
                                    data_list_12.add(dataBean);
                                    datalist2.add(jo.getString("name"));
                                }
                            }
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_12);
//                            tv_money_want_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                            dataadapter2 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist2);
                            dataadapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_money_want_02.setAdapter(dataadapter2);
                            dataadapter2.notifyDataSetChanged();

                        } else {
                            datalist2 = new ArrayList<String>();
                            datalist2.add("请选择");
                            dataadapter2 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist2);
                            dataadapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_money_want_02.setAdapter(dataadapter2);
                            dataadapter2.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}else{
                        dataadapter2 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist2);
                        dataadapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_money_want_02.setAdapter(dataadapter2);
                        dataadapter2.notifyDataSetChanged();
                    }
                    break;

                case typeid_13:
                    JSONObject jsonObject1_13 = (JSONObject) msg.obj;
                    if (jsonObject1_13 != null){
                    try {
                        String status = jsonObject1_13.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject1_13.getJSONArray("data");
                            data_list_13 = new ArrayList<Spinner_Bean.DataBean>();
                            datalist13 = new ArrayList<String>();
                            datalist13.add("请选择");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                dataBean.setName(jo.getString("name"));
                                data_list_13.add(dataBean);
                                datalist13.add(jo.getString("name"));
                            }
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_13);
//                            tv_position_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                            dataadapter13 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist13);
                            dataadapter13.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_position_02.setAdapter(dataadapter13);
                            dataadapter13.notifyDataSetChanged();

                        } else {
                            datalist13 = new ArrayList<String>();
                            datalist13.add("请选择");
                            dataadapter13 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist13);
                            dataadapter13.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_position_02.setAdapter(dataadapter13);
                            dataadapter13.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}else{
                        datalist13 = new ArrayList<String>();
                        datalist13.add("请选择");
                        dataadapter13 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist13);
                        dataadapter13.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_position_02.setAdapter(dataadapter13);
                        dataadapter13.notifyDataSetChanged();
                    }
                    break;

                case typeid_14:
                    JSONObject jsonObject1_14 = (JSONObject) msg.obj;
                    if (jsonObject1_14 != null){
                    try {
                        String status = jsonObject1_14.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject1_14.getJSONArray("data");
                            data_list_14 = new ArrayList<Spinner_Bean.DataBean>();
                            datalist14 = new ArrayList<String>();
                            data_list_14.clear();
                            datalist14.clear();
                            datalist14.add("请选择");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                String moneyname = jo.getString("name");
                                isname = moneyname.contains("&lt;");
                                isnameone = moneyname.contains("&gt;");
                                if (isname){
                                    dataBean.setName("<"+jo.getString("name").substring(4));
                                    data_list_14.add(dataBean);
                                    datalist14.add("<"+jo.getString("name").substring(4));
                                }else if (isnameone){
                                    dataBean.setName(">"+jo.getString("name").substring(4));
                                    data_list_14.add(dataBean);
                                    datalist14.add(">"+jo.getString("name").substring(4));
                                }else{
                                    dataBean.setName(jo.getString("name"));
                                    data_list_14.add(dataBean);
                                    datalist14.add(jo.getString("name"));
                                }
                            }
                            dataadapter14 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist14);
                            dataadapter14.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_money_02.setAdapter(dataadapter14);
                            dataadapter14.notifyDataSetChanged();
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_14);
//                            tv_money_02.setAdapter(spinner_adapter);
//                            tv_money_want_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();

                        }else{
                            datalist14 = new ArrayList<String>();
                            datalist14.add("请选择");
                            dataadapter14 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist14);
                            dataadapter14.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_money_02.setAdapter(dataadapter14);
                            dataadapter14.notifyDataSetChanged();
                            tv_money_02.setSelection(0);
                            experience = datalist14.get(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}else{
                        datalist15 = new ArrayList<String>();
                        datalist15.add("请选择");
                        dataadapter15 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist15);
                        dataadapter15.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_work_02.setAdapter(dataadapter15);
                        dataadapter15.notifyDataSetChanged();
                        tv_work_02.setSelection(0);
                        experience = datalist15.get(0);
                    }
                    break;

                case typeid_15:
                    JSONObject jsonObject1_15 = (JSONObject) msg.obj;
                    if (jsonObject1_15!= null){


                    try {
                        String status = jsonObject1_15.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject1_15.getJSONArray("data");
                            data_list_15 = new ArrayList<Spinner_Bean.DataBean>();
                            datalist15 = new ArrayList<String>();
                            datalist15.add("请选择");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                dataBean.setName(jo.getString("name"));
                                data_list_15.add(dataBean);
                                datalist15.add(jo.getString("name"));
                            }
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_15);
//                            tv_work_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                            dataadapter15 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist15);
                            dataadapter15.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_work_02.setAdapter(dataadapter15);
                            dataadapter15.notifyDataSetChanged();

                        } else {
                            datalist15 = new ArrayList<String>();
                            datalist15.add("请选择");
                            dataadapter15 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist15);
                            dataadapter15.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_work_02.setAdapter(dataadapter15);
                            dataadapter15.notifyDataSetChanged();
                            tv_work_02.setSelection(0);
                            experience = datalist15.get(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } }else{
                        datalist15 = new ArrayList<String>();
                        datalist15.add("请选择");
                        dataadapter15 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist15);
                        dataadapter15.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_work_02.setAdapter(dataadapter15);
                        dataadapter15.notifyDataSetChanged();
                        tv_work_02.setSelection(0);
                        experience = datalist15.get(0);
                    }
                    break;

                case typeid_16:
                    JSONObject jsonObject1_16 = (JSONObject) msg.obj;
                    if (jsonObject1_16!= null){


                    try {
                        String status = jsonObject1_16.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject1_16.getJSONArray("data");
                            data_list_16 = new ArrayList<Spinner_Bean.DataBean>();
                            datalist16 = new ArrayList<String>();
                            datalist16.add("请选择");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                dataBean.setName(jo.getString("name"));
                                data_list_16.add(dataBean);
                                datalist16.add(jo.getString("name"));
                            }
//                            spinner_adapter = new Spinner_Adapter(mactivity, data_list_16);
//                            tv_arrive_time_02.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                            dataadapter16 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist16);
                            dataadapter16.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_arrive_time_02.setAdapter(dataadapter16);
                            dataadapter16.notifyDataSetChanged();

                        } else {
                            datalist16 = new ArrayList<String>();
                            datalist16.add("请选择");
                            dataadapter16 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist16);
                            dataadapter16.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_arrive_time_02.setAdapter(dataadapter16);
                            dataadapter16.notifyDataSetChanged();
                            tv_arrive_time_02.setSelection(1);
                            arrivetime = datalist16.get(1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } }else{
                        datalist16 = new ArrayList<String>();
                        datalist16.add("请选择");
                        dataadapter16 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist16);
                        dataadapter16.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_arrive_time_02.setAdapter(dataadapter16);
                        dataadapter16.notifyDataSetChanged();
                        tv_arrive_time_02.setSelection(1);
                        arrivetime = datalist16.get(1);
                    }
                    break;

                case KEY://上传头像
                    String key = (String) msg.obj;
                    if (key != null){
                    try {
                        JSONObject json = new JSONObject(key);
                        String state = json.getString("status");
                        if ("success".equals(state)) {
//                            JSONObject js=json.getJSONObject("data");
                            path = json.getString("data");
                            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + path, img_head, options);
                        } else {
                            Toast.makeText(mactivity, "上传头像失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }else{
                        Toast.makeText(mactivity, "上传头像失败，请重试！", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case typeid_1:
                    JSONObject jsonObject_1 = (JSONObject) msg.obj;
                    if (jsonObject_1 != null){


                    try {
                        String status = jsonObject_1.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject_1.getJSONArray("data");
                            spinnerlist_1 = new ArrayList<Spinner_Bean.DataBean>();
                            datalist1.add("请选择");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                dataBean.setName(jo.getString("name"));
                                spinnerlist_1.add(dataBean);
                                datalist1.add(jo.getString("name"));
                            }
//                                spinner_adapter = new Spinner_Adapter(mactivity, spinnerlist_1);
//                                tv_work_education.setAdapter(spinner_adapter);
//                                spinner_adapter.notifyDataSetChanged();
                            dataadapter1 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist1);
                            dataadapter1.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_work_education.setAdapter(dataadapter1);
                            dataadapter1.notifyDataSetChanged();


                        } else {
                            datalist1 = new ArrayList<String>();
                            datalist1.add("请选择");
                            dataadapter1 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist1);
                            dataadapter1.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_work_education.setAdapter(dataadapter1);
                            dataadapter1.notifyDataSetChanged();
                            tv_work_education.setSelection(1);
                            educationdata = datalist1.get(1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }else{
                        datalist1 = new ArrayList<String>();
                        datalist1.add("请选择");
                        dataadapter1 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist1);
                        dataadapter1.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_work_education.setAdapter(dataadapter1);
                        dataadapter1.notifyDataSetChanged();
                        tv_work_education.setSelection(1);
                        educationdata = datalist1.get(1);
                    }
                    break;

                case typeid_6:
                    JSONObject jsonObject_6 = (JSONObject) msg.obj;
                    if (jsonObject_6 != null){
                    try {
                        String status = jsonObject_6.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject_6.getJSONArray("data");
                            spinnerlist_6 = new ArrayList<Spinner_Bean.DataBean>();
                            datalist6 = new ArrayList<String>();
                            datalist6.add("请选择");
                            Spinner_Bean.DataBean dataBeanout = new Spinner_Bean.DataBean();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                dataBean.setName(jo.getString("name"));
                                spinnerlist_6.add(dataBean);
                                datalist6.add(jo.getString("name"));
                            }
//                            spinner_adapter = new Spinner_Adapter(mactivity, spinnerlist_6);
//                            tv_work_jobname.setAdapter(spinner_adapter);
//                            spinner_adapter.notifyDataSetChanged();
                            dataadapter6 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist6);
                            dataadapter6.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_work_jobname.setAdapter(dataadapter6);
                            dataadapter6.notifyDataSetChanged();
                            tv_work_jobname.setSelection(1);
                            jobnamedata = datalist6.get(1);
                        } else {
                            datalist6 = new ArrayList<String>();
                            datalist6.add("请选择");
                            dataadapter6 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist6);
                            dataadapter6.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                            tv_work_jobname.setAdapter(dataadapter6);
                            dataadapter6.notifyDataSetChanged();
                            tv_work_jobname.setSelection(1);
                            jobnamedata = datalist6.get(1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }else{
                        datalist6 = new ArrayList<String>();
                        datalist6.add("请选择");
                        dataadapter6 = new ArrayAdapter<String>(mactivity, R.layout.spinner_layout_ymd, datalist6);
                        dataadapter6.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        tv_work_jobname.setAdapter(dataadapter6);
                        dataadapter6.notifyDataSetChanged();
                        tv_work_jobname.setSelection(1);
                        jobnamedata = datalist6.get(1);
                    }
                    break;

                case GETRESUMEINFO://获取个人简历
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        if (result != null){
                        try {
                            JSONObject obj = new JSONObject(result);
                            String data = obj.getString("data");
                            if ("success".endsWith(obj.optString("status"))) {
                                JSONObject jsonobj = new JSONObject(data);
                                mrinfo = new MineResumeinfo.DataBean();
                                mrinfo.setId(jsonobj.getString("id"));
                                Log.e("id", "==================" + mrinfo.getId());
                                mrinfo.setUserid(jsonobj.getString("userid"));
                                mrinfo.setName(jsonobj.getString("name"));
                                mrinfo.setSex(jsonobj.getString("sex"));
                                mrinfo.setAvatar(jsonobj.getString("avatar"));
                                mrinfo.setBirthday(jsonobj.getString("birthday"));
                                mrinfo.setExperience(jsonobj.getString("experience"));
                                mrinfo.setEducation(jsonobj.getString("education"));
                                mrinfo.setCertificate(jsonobj.getString("certificate"));
                                mrinfo.setWantposition(jsonobj.getString("wantposition"));
                                mrinfo.setTitle(jsonobj.getString("title"));
                                mrinfo.setAddress(jsonobj.getString("address"));
                                mrinfo.setCurrentsalary(jsonobj.getString("currentsalary"));
                                mrinfo.setJobstate(jsonobj.getString("jobstate"));
                                mrinfo.setDescription(jsonobj.getString("description"));
                                mrinfo.setEmail(jsonobj.getString("email"));
                                mrinfo.setPhone(jsonobj.getString("phone"));
                                mrinfo.setHiredate(jsonobj.getString("hiredate"));
                                mrinfo.setWantcity(jsonobj.getString("wantcity"));
                                mrinfo.setWantsalary(jsonobj.getString("wantsalary"));
                                tv_save.setText("编辑简历");
                                path = mrinfo.getAvatar();
                                getemployView();//填写过简历
                            } else {
                                getemployunView();//没填写过简历
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        }else{
                            getemployunView();
                        }
                    }
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_employwork_fragment);
        mactivity = this;
        user = new UserBean(mactivity);
        popcomm = new Pop_commuity_location(Add_EmployWork_Fragment.this);
        popcomtwo = new Pop_commuity_location_two(Add_EmployWork_Fragment.this);
        mineborthday = new Pop_mine_birthdayone(Add_EmployWork_Fragment.this);
        initDataView();


    }

    private void initDataView() {
        ed_name = (EditText) findViewById(R.id.ed_name);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        title_top = (TextView) findViewById(R.id.top_title);
        title_top.setText("个人信息编辑");
        textview = (TextView) findViewById(R.id.textview);
        img_head = (RoudImage) findViewById(R.id.img_head);
        // 显示图片的配置
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.headlogo).showImageOnFail(R.mipmap.headlogo).cacheInMemory(true).cacheOnDisc(true).build();
        tv_head = (TextView) findViewById(R.id.tv_head);
        //性别
        rg_sex_state = (RadioGroup) findViewById(R.id.rg_sex_state);
        rb_sex_state_01 = (RadioButton) findViewById(R.id.rb_sex_state_01);
        rb_sex_state_02 = (RadioButton) findViewById(R.id.rb_sex_state_02);
        //出生日期
        birthady_year = (TextView) findViewById(R.id.edit_personal_year);
        birthady_month = (TextView) findViewById(R.id.edit_personal_month);
        birthady_day = (TextView) findViewById(R.id.edit_personal_day);
        birthday_data = (LinearLayout) findViewById(R.id.linear_birthdat_data);
        //学历
        tv_work_education = (Spinner) findViewById(R.id.tv_work_education);
        tv_work_education.setDropDownVerticalOffset(140);
        tv_work_education.setDropDownWidth(400);
        tv_work_education.setOnItemSelectedListener(this);
        //居住地
        linear_location = (LinearLayout) findViewById(R.id.linear_location);
        sheng_spinner = (TextView) findViewById(R.id.sheng_spinner);
//        shi_spinner = (TextView) findViewById(R.id.shi_spinner);
//        qu_spinner = (TextView) findViewById(R.id.qu_spinner);
        //工作经验
        tv_work_02 = (Spinner) findViewById(R.id.tv_work_02);
        tv_work_02.setDropDownVerticalOffset(140);
        tv_work_02.setDropDownWidth(400);
        tv_work_02.setOnItemSelectedListener(this);
        //职称
        tv_work_jobname = (Spinner) findViewById(R.id.tv_work_jobname);
        tv_work_jobname.setDropDownVerticalOffset(140);
        tv_work_jobname.setDropDownWidth(400);
        tv_work_jobname.setOnItemSelectedListener(this);
        //目前薪资
        tv_money_02 = (Spinner) findViewById(R.id.tv_money_02);
        tv_money_02.setDropDownVerticalOffset(140);
        tv_money_02.setDropDownWidth(400);
        tv_money_02.setOnItemSelectedListener(this);
        //在职状态
        rg_work_state = (RadioGroup) findViewById(R.id.rg_work_state);
        rb_work_state_01 = (RadioButton) findViewById(R.id.rb_work_state_01);
        rb_work_state_02 = (RadioButton) findViewById(R.id.rb_work_state_02);
        rb_work_state_03 = (RadioButton) findViewById(R.id.rb_work_state_03);
        //联系方式
        ed_phone = (EditText) findViewById(R.id.ed_phone);
        //邮箱
        ed_email = (EditText) findViewById(R.id.ed_email);
        //到岗时间
        tv_arrive_time_02 = (Spinner) findViewById(R.id.tv_arrive_time_02);
        tv_arrive_time_02.setDropDownVerticalOffset(140);
        tv_arrive_time_02.setDropDownWidth(400);
        tv_arrive_time_02.setOnItemSelectedListener(this);
        //目标城市
        city_two_linear = (LinearLayout) findViewById(R.id.city_two_linear);
        shi_spinner02 = (TextView) findViewById(R.id.shi_spinner_02);
//        qu_spinner02 = (TextView) findViewById(R.id.qu_spinner_02);
        //目标薪资
        tv_money_want_02 = (Spinner) findViewById(R.id.tv_money_want_02);
        tv_money_want_02.setDropDownVerticalOffset(140);
        tv_money_want_02.setDropDownWidth(400);
        tv_money_want_02.setOnItemSelectedListener(this);
        //期待职位
        tv_position_02 = (Spinner) findViewById(R.id.tv_position_02);
        tv_position_02.setDropDownVerticalOffset(140);
        tv_position_02.setDropDownWidth(400);
        tv_position_02.setOnItemSelectedListener(this);
        //自我评价
        ed_evaluation = (EditText) findViewById(R.id.ed_evaluation);
        //保存
        re_save = (LinearLayout) findViewById(R.id.re_save);
        tv_save = (TextView) findViewById(R.id.tv_save);
        linear_button = (LinearLayout) findViewById(R.id.linear_button);//全部布局


        img_head.setOnTouchListener(this);
        linear_button.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        img_head.setOnClickListener(this);
        linear_location.setOnClickListener(this);
        city_two_linear.setOnClickListener(this);
        re_save.setOnClickListener(this);
        birthday_data.setOnClickListener(this);
        rg_work_state.setOnClickListener(this);
        ed_evaluation.setOnClickListener(this);
        ed_name.setOnClickListener(this);
        ed_phone.setOnClickListener(this);
        ed_email.setOnClickListener(this);

        rg_sex_state.setOnTouchListener(this);//性别
        birthday_data.setOnTouchListener(this);//出生日期
        tv_work_education.setOnTouchListener(this);//学历
        linear_location.setOnTouchListener(this);//居住地
        tv_work_02.setOnTouchListener(this);//工作经验
        tv_work_jobname.setOnTouchListener(this);//职称
        tv_money_02.setOnTouchListener(this);//目前薪资
        rg_work_state.setOnTouchListener(this);//在职状态
        tv_arrive_time_02.setOnTouchListener(this);//到岗时间
        city_two_linear.setOnTouchListener(this);//目标城市
        tv_money_want_02.setOnTouchListener(this);//目标薪资
        tv_position_02.setOnTouchListener(this);//期望职位
        worview();
        rg_sex_state.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sex_state_01:
                        rb_sex_state_01.setChecked(true);
                        rb_sex_state_02.setChecked(false);
                        sexnew = rb_sex_state_01.getText().toString();
                        is_sex = 0;
                        break;
                    case R.id.rb_sex_state_02:
                        rb_sex_state_01.setChecked(false);
                        rb_sex_state_02.setChecked(true);
                        sexnew = rb_sex_state_02.getText().toString();
                        is_sex = 1;
                        break;
                }
            }
        });
        //求职状态
        rg_work_state.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rb_work_state_01:
                        rb_work_state_01.setChecked(true);
                        rb_work_state_02.setChecked(false);
                        rb_work_state_03.setChecked(false);
                        jobstatenew = rb_work_state_01.getText().toString();
                        work_num = 0;
                        break;
                    case R.id.rb_work_state_02:
                        rb_work_state_01.setChecked(false);
                        rb_work_state_03.setChecked(false);
                        rb_work_state_02.setChecked(true);
                        jobstatenew = rb_work_state_02.getText().toString();
                        work_num = 1;
                        break;
                    case R.id.rb_work_state_03:
                        rb_work_state_01.setChecked(false);
                        rb_work_state_02.setChecked(false);
                        rb_work_state_03.setChecked(true);
                        jobstatenew = rb_work_state_03.getText().toString();
                        work_num = 2;
                        break;
                }
            }
        });

    }



    private void worview() {
        if (NetUtil.isConnnected(mactivity)) {
            //12 期望薪资
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_12);
            //13 期望职位
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_13);
            //14目前薪资
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_14);
            //15 工作经验
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_15);
            //16 到岗时间
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_16);
            //学历
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_1);
            //职称
            new StudyRequest(mactivity, handler).get_DictionaryList(typeid_6);
//            datalist2, datalist13, datalist14, datalist15, datalist16, datalist1, datalist6;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        //获取个人简历
                        new StudyRequest(mactivity, handler).getResumeInfo(user.getUserid(), GETRESUMEINFO);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    @Override
    public void onClick(View v) {
//        if (iS){
        switch (v.getId()) {
            case R.id.img_head:
//                if (mrinfo!= null){
//                        isShowDialog();
//                }else{
                    getCheck();
                    ShowPickDialog();// 点击更换头像
//                    iS = false;
//                }
                break;
            case R.id.linear_birthdat_data:
                if (mrinfo!= null){
                    isShowDialog();
                }else{
//                //获取当前系统时间
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//                date = formatter.format(new Date());
//                calendar = Calendar.getInstance();
//                dialog = new DatePickerDialog(mactivity,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                Log.i("calender", "年-->" + year + "月-->" + monthOfYear + "日-->" + dayOfMonth);
//                                if (year > Integer.valueOf(date)) {
//                                    Toast.makeText(mactivity, "请选择正确的出生日期", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    birthady_year.setText(year + "");
//                                    birthady_month.setText(monthOfYear + 1 + "");
//                                    birthady_day.setText(dayOfMonth + "");
//                                }
//                            }
//                        }, calendar.get(Calendar.YEAR), calendar
//                        .get(Calendar.MONTH), calendar
//                        .get(Calendar.DAY_OF_MONTH));
//                dialog.show();
                    mineborthday.showAsDropDown(textview);
                    iS = false;
                }
                break;
            case R.id.re_save://保存
                 if (mrinfo != null) {
                     if (is_save == 0){
                        bianjiview();
                         is_save = 1;
                     }else if (is_save == 1){
                         saveview();//保存修改
                         is_save = 0;
                     }
                } else {
                    saveviewsub();//保存提交
                     iS = false;
                }
                break;
//            case R.id.linear_button://全部布局
//                if (mrinfo != null) {
//                    isShowDialog();
//                } else {
//                    bianjiview();//保存提交
//                }
//                break;
            case R.id.linear_location:
                popcomm.showAsDropDown(textview);
                break;
            case R.id.city_two_linear:
                popcomtwo.showAsDropDown(textview);
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.ed_evaluation:
                if (mrinfo!= null){
                        isShowDialog();
                }else{
                    ed_evaluation.setFocusable(true);
                    ed_evaluation.setFocusableInTouchMode(true);
                    ed_evaluation.requestFocus();
                    iS = false;
                }
                break;
            case R.id.ed_name://姓名
                if (mrinfo != null){
                        isShowDialog();
                }else{
                    ed_name.setFocusable(true);
                    ed_name.setFocusableInTouchMode(true);
                    ed_name.requestFocus();
                    iS = false;
                }
                break;
            case R.id.ed_phone://联系方式
                if (mrinfo != null){
                        isShowDialog();
                }else{
                    ed_phone.setFocusable(true);
                    ed_phone.setFocusableInTouchMode(true);
                    ed_phone.requestFocus();
                    iS = false;
                }
                break;
            case R.id.ed_email://邮箱
                if (mrinfo != null){
                        isShowDialog();
                }else{
                    ed_email.setFocusable(true);
                    ed_email.setFocusableInTouchMode(true);
                    ed_email.requestFocus();
                    iS = false;
                }
                break;
        }}
//    }

    private void saveviewsub() {
        userid = user.getUserid() + "";
        avatar = path + "";
        name = ed_name.getText().toString() + "";
//                experience = tv_work_02.getText().toString();
        year = birthady_year.getText().toString() + "";
        month = birthady_month.getText().toString() + "";
        day = birthady_day.getText().toString();
        phone = ed_phone.getText().toString();
        email = ed_email.getText().toString();
        shengcity = sheng_spinner.getText().toString();
//        shicity =shi_spinner .getText().toString();
//        qucity = qu_spinner.getText().toString();
        twoshicity = shi_spinner02.getText().toString();
//        twoqucity = qu_spinner02.getText().toString();
        description = ed_evaluation.getText().toString();
        if ("请选择".equals(educationdata)) {
            Toast.makeText(mactivity, "请选择您的学历", Toast.LENGTH_SHORT).show();//学历
        } else if ("请选择".equals(experience)) {
            Toast.makeText(mactivity, "请选择您的工作经验", Toast.LENGTH_SHORT).show();//工作经验
        } else if ("请选择".equals(jobnamedata)) {
            Toast.makeText(mactivity, "请选择您的职称", Toast.LENGTH_SHORT).show();//职称
        } else if ("请选择".equals(money02data)) {
            Toast.makeText(mactivity, "请选择您目前薪资", Toast.LENGTH_SHORT).show();//目前薪资
        } else if ("请选择".equals(arrivetime)) {
            Toast.makeText(mactivity, "请选择您的到岗时间", Toast.LENGTH_SHORT).show();//到岗时间
        } else if ("请选择".equals(wantsalary)) {
            Toast.makeText(mactivity, "请选择您的期望薪资", Toast.LENGTH_SHORT).show();//期望薪资
        } else if ("请选择".equals(jobWantdata)) {
            Toast.makeText(mactivity, "请选择您的目标职位", Toast.LENGTH_SHORT).show();//目标职位
        } else if (name == null || "".equals(name)) {
            Toast.makeText(mactivity, "请输入姓名", Toast.LENGTH_SHORT).show();//姓名
        } else if (3==work_num) {
            Toast.makeText(mactivity, "请选择求职状态", Toast.LENGTH_SHORT).show();//求职状态
        } else if (jobstatenew == null &&jobstatenew.length() <= 0){
            Toast.makeText(mactivity, "请选择求职状态", Toast.LENGTH_SHORT).show();//求职状态
        }else if (phone == null || "".equals(phone)) {
            Toast.makeText(mactivity, "请完善联系电话", Toast.LENGTH_SHORT).show();//电话
        } else if (email == null || "".equals(email)) {
            Toast.makeText(mactivity, "请完善联系邮箱", Toast.LENGTH_SHORT).show();//邮箱
        } else if (description == null || "".equals(description)) {
            Toast.makeText(mactivity, "请完善自我评述", Toast.LENGTH_SHORT).show();//描述
        }else if (2==is_sex) {//出生日期
            Toast.makeText(mactivity, "请选择性别", Toast.LENGTH_SHORT).show();
        }  else if (year == null && "".equals(year) && month == null && "".equals(month) && day == null && "".equals(day)) {//出生日期
            Toast.makeText(mactivity, "请选择出生日期", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("sexnew","=================>"+sexnew);
            if (NetUtil.isConnnected(mactivity)) {
                new StudyRequest(mactivity, handler).send_PublishResume(user.getUserid(),String.valueOf(is_sex), path, name, year + "-" + month +
                                "-" + day + "", educationdata, shengcity, experience, jobnamedata, money02data, phone, email,
                        arrivetime, twoshicity, jobstatenew, wantsalary, jobWantdata, description, SENDREMINFO);

            }
        }
    }

    private void bianjiview() {
        setEnable(true);
        tv_save.setText("保存简历");
    }

    private void saveview() {
        setEnable(false);
        userid = user.getUserid() + "";
        avatar = path + "";
        name = ed_name.getText().toString() + "";
//                experience = tv_work_02.getText().toString();
        year = birthady_year.getText().toString() + "";
        month = birthady_month.getText().toString() + "";
        day = birthady_day.getText().toString();
        phone = ed_phone.getText().toString();
        email = ed_email.getText().toString();
        shengcity = sheng_spinner.getText().toString();
//        shicity =shi_spinner .getText().toString();
//        qucity = qu_spinner.getText().toString();
        twoshicity = shi_spinner02.getText().toString();
//        twoqucity = qu_spinner02.getText().toString();
        description = ed_evaluation.getText().toString();
        if ("请选择".equals(educationdata)) {
            Toast.makeText(mactivity, "请选择您的学历", Toast.LENGTH_SHORT).show();//学历
        } else if ("请选择".equals(experience)) {
            Toast.makeText(mactivity, "请选择您的工作经验", Toast.LENGTH_SHORT).show();//工作经验
        } else if ("请选择".equals(jobnamedata)) {
            Toast.makeText(mactivity, "请选择您的职称", Toast.LENGTH_SHORT).show();//职称
        } else if ("请选择".equals(money02data)) {
            Toast.makeText(mactivity, "请选择您目前薪资", Toast.LENGTH_SHORT).show();//目前薪资
        } else if ("请选择".equals(arrivetime)) {
            Toast.makeText(mactivity, "请选择您的到岗时间", Toast.LENGTH_SHORT).show();//到岗时间
        } else if ("请选择".equals(wantsalary)) {
            Toast.makeText(mactivity, "请选择您的期望薪资", Toast.LENGTH_SHORT).show();//期望薪资
        } else if ("请选择".equals(jobWantdata)) {
            Toast.makeText(mactivity, "请选择您的目标职位", Toast.LENGTH_SHORT).show();//目标职位
        } else if (name == null || "".equals(name)) {
            Toast.makeText(mactivity, "请输入姓名", Toast.LENGTH_SHORT).show();//姓名
        } else if (3==work_num) {
            Toast.makeText(mactivity, "请选择求职状态", Toast.LENGTH_SHORT).show();//求职状态
        } else if (2==is_sex) {//出生日期
            Toast.makeText(mactivity, "请选择性别", Toast.LENGTH_SHORT).show();
        }else if (phone == null || "".equals(phone)) {
            Toast.makeText(mactivity, "请完善联系电话", Toast.LENGTH_SHORT).show();//电话
        } else if (!isCanGetCode()) {
            Toast.makeText(mactivity, "请填写正确的手机号", Toast.LENGTH_SHORT).show();//电话
        } else if (!isCanGetCodeemile()) {
            Toast.makeText(mactivity, "请填写正确的邮箱", Toast.LENGTH_SHORT).show();//电话
        } else if (email == null || "".equals(email)) {
            Toast.makeText(mactivity, "请完善联系邮箱", Toast.LENGTH_SHORT).show();//邮箱
        } else if (description == null || "".equals(description)) {
            Toast.makeText(mactivity, "请完善自我评述", Toast.LENGTH_SHORT).show();//描述
        } else if (year == null && "".equals(year) && month == null && "".equals(month) && day == null && "".equals(day)) {//出生日期
            Toast.makeText(mactivity, "请选择出生日期", Toast.LENGTH_SHORT).show();
        } else {
            if (NetUtil.isConnnected(mactivity)) {
                new StudyRequest(mactivity, handler).UpdataMyResume(user.getUserid(),String.valueOf(is_sex), path, name, year + "-" + month + "-" + day + "", educationdata,
                        shengcity, experience, jobnamedata, money02data, phone, email, arrivetime, twoshicity, jobstatenew,
                        wantsalary, jobWantdata, description, UPDATAREMINFO);
            }
        }
    }
    private Boolean isCanGetCode() {
        if (!RegexUtil.checkMobile(phone)){
            return false;
        }
        return true;
    }
    private Boolean isCanGetCodeemile() {
        if (!RegexUtil.checkEmail(email)){
            return false;
        }
        return true;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.tv_work_education://学历
                if (datalist1 != null && datalist1.size() > 0) {
                    educationdata = datalist1.get(position);
                }
                break;
            case R.id.tv_work_02://工作经验
                if (datalist15 != null && datalist15.size() > 0) {
                    experience = datalist15.get(position);
                }
                break;
            case R.id.tv_work_jobname://职称

                if (datalist6 != null && datalist6.size() > 0) {
                    jobnamedata = datalist6.get(position);
                }
                break;
            case R.id.tv_money_02://目前薪资
                if (datalist14 != null && datalist14.size() > 0) {
                    money02data = datalist14.get(position);
                }
                break;
            case R.id.tv_arrive_time_02://到岗时间

                if (datalist16 != null && datalist16.size() > 0) {
                    arrivetime = datalist16.get(position);
                }
                break;
            case R.id.tv_money_want_02://目标薪资
                if (datalist2 != null && datalist2.size() > 0) {
                    wantsalary = datalist2.get(position);
                }
                break;
            case R.id.tv_position_02://期待职位
                if (datalist13 != null && datalist13.size() > 0) {
                    jobWantdata = datalist13.get(position);
                }
                break;
        }
    }

    private void isShowDialog() {
        if (iS) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Add_EmployWork_Fragment.this);
            builder.setMessage("是否编辑个人简历");
            builder.setTitle("提示");
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bianjiview();
                    iS = false;
                    is_save = 1;
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    is_save = 0;
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    private void setEnable(Boolean isFalse) {
        img_head.setClickable(isFalse);//头像
        ed_name.setFocusable(isFalse);//姓名
        ed_name.setFocusableInTouchMode(isFalse);
        ed_name.setTextColor(getResources().getColor(R.color.gray2));
        birthday_data.setClickable(isFalse);//出生日期
        tv_work_education.setClickable(isFalse);//学历
        linear_location.setClickable(isFalse);//居住地
        tv_work_02.setClickable(isFalse);//工作经验
        tv_work_jobname.setClickable(isFalse);//职称
        tv_money_02.setClickable(isFalse);//目前薪资
        rb_work_state_01.setEnabled(isFalse);
        rb_work_state_02.setEnabled(isFalse);
        rb_work_state_03.setEnabled(isFalse);//求职状态
        rb_sex_state_01.setEnabled(isFalse);//性别
        rb_sex_state_02.setEnabled(isFalse);
        ed_phone.setFocusable(isFalse);//联系方式
        ed_phone.setFocusableInTouchMode(isFalse);
        ed_phone.setTextColor(getResources().getColor(R.color.gray2));
        ed_email.setFocusable(isFalse);//邮箱
        ed_email.setFocusableInTouchMode(isFalse);
        ed_email.setTextColor(getResources().getColor(R.color.gray2));
        tv_arrive_time_02.setClickable(isFalse);//当岗时
        city_two_linear.setClickable(isFalse);//目标城市
        tv_money_want_02.setClickable(isFalse);//期望薪资
        tv_position_02.setClickable(isFalse);//期望职位
        ed_evaluation.setFocusable(isFalse);//自我评价
        ed_evaluation.setFocusableInTouchMode(isFalse);
        ed_evaluation.setTextColor(getResources().getColor(R.color.gray2));
    }

    private void getemployunView() {
        setEnable(true);
        tv_money_want_02.setSelection(0);
        jobWantdata = datalist2.get(0);
        tv_position_02.setSelection(0);
        jobWantdata = datalist13.get(0);
        tv_money_02.setSelection(0);
        tv_money_want_02.setSelection(0);
        money02data = datalist14.get(0);//目前薪资
        wantmoneydata = datalist14.get(0);//目标薪资
        tv_work_02.setSelection(0);
        experience = datalist15.get(0);
        tv_arrive_time_02.setSelection(0);
        arrivetime = datalist16.get(0);
        tv_work_education.setSelection(0);
        educationdata = datalist1.get(0);
    }

    private void getemployView() {//填写过简历
        setEnable(false);
        //姓名
        ed_name.setText(mrinfo.getName() + "");
        if (mrinfo.getAvatar() != null &&mrinfo.getAvatar().length() > 0){
            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + path, img_head, options);
        }

        //性别
        if (mrinfo.getSex() != null && mrinfo.getSex().length() > 0) {
            Log.e("getsex","==========>"+mrinfo.getSex());
            if ("0".equals(mrinfo.getSex())) {
                rb_sex_state_01.setChecked(true);
                rb_sex_state_02.setChecked(false);
                sexnew = mrinfo.getSex();
                is_sex = 0;
            } else if ("1".equals(mrinfo.getSex())) {
                rb_sex_state_01.setChecked(false);
                rb_sex_state_02.setChecked(true);
                is_sex = 1;
                sexnew = mrinfo.getSex();
            } else {
                rb_sex_state_01.setChecked(false);
                rb_sex_state_02.setChecked(false);
                is_sex = 2;
                sexnew = "";
            }
        } else {
            rb_work_state_01.setChecked(false);
            rb_work_state_02.setChecked(false);
            rb_work_state_03.setChecked(false);
        }
        //生日
        birthady_year.setText(mrinfo.getBirthday().substring(0, 4) + "");
        birthady_month.setText(mrinfo.getBirthday().substring(5, 7) + "");
        birthady_day.setText(mrinfo.getBirthday().substring(8) + "");

        //学历
        if (datalist1 != null && datalist1.size() > 0) {
            if (mrinfo.getEducation() != null && mrinfo.getEducation().length() > 0) {
                for (int i = 0; i < datalist1.size(); i++) {
                    String education = mrinfo.getEducation();
                    if (education.equals(datalist1.get(i))) {
                        tv_work_education.setSelection(i);
                        educationdata = datalist1.get(i);
                    }
                }
            } else {
                tv_work_education.setSelection(0);
                educationdata = datalist1.get(0);
            }
        } else {
            tv_work_education.setSelection(0);
            educationdata = datalist1.get(0);
        }
        //居住地，得改一下市、区
        if (mrinfo.getAddress() != null && mrinfo.getAddress().length() > 0) {
            sheng_spinner.setText(mrinfo.getAddress() + "");
        }

        //工作经验
        if (datalist15 != null && datalist15.size() > 0) {
            if (mrinfo.getExperience() != null && mrinfo.getExperience().length() > 0) {
                for (int i = 0; i < datalist15.size(); i++) {
                    String wortime = mrinfo.getExperience();
                    if (wortime.equals(datalist15.get(i))) {
                        tv_work_02.setSelection(i);
                        experience = datalist15.get(i);
                    }
                }
            } else {
                tv_work_02.setSelection(0);
                experience = datalist15.get(0);
            }
        } else {
            tv_work_02.setSelection(0);
            experience = datalist15.get(0);
        }
        //职称
        if (datalist6 != null && datalist6.size() > 0) {
            if (mrinfo.getCertificate() != null && mrinfo.getCertificate().length() > 0) {
                for (int i = 0; i < datalist6.size(); i++) {
                    String workname = mrinfo.getCertificate();
                    if (workname.equals(datalist6.get(i))) {
                        tv_work_jobname.setSelection(i);
                        jobnamedata = datalist6.get(i);
                    }
                }
            } else {
                tv_work_jobname.setSelection(0);
                jobnamedata = datalist6.get(0);
            }
        } else {
            tv_work_jobname.setSelection(0);
            jobnamedata = datalist6.get(0);

        }

        //目前薪资
        if (datalist14 != null && datalist14.size() > 0) {
            if (mrinfo.getCurrentsalary() != null && mrinfo.getCurrentsalary().length() > 0) {
                for (int i = 0; i < datalist14.size(); i++) {
                    String cursalary = mrinfo.getCurrentsalary();
                    if (cursalary.equals(datalist14.get(i))) {
                        tv_money_02.setSelection(i);
                        money02data = datalist14.get(i);//目前薪资
                    }
                }
            } else {
                tv_money_02.setSelection(0);
                money02data = datalist14.get(0);
            }
        } else {
            tv_money_02.setSelection(0);
            money02data = datalist14.get(0);
        }

        //求职状态  readio
        if (mrinfo.getJobstate() != null && mrinfo.getJobstate().length() > 0) {
            if ("在职".equals(mrinfo.getJobstate())) {
                rb_work_state_01.setChecked(true);
                rb_work_state_02.setChecked(false);
                rb_work_state_03.setChecked(false);
                jobstatenew = mrinfo.getJobstate();
                work_num = 0;
            } else if ("离职".equals(mrinfo.getJobstate())) {
                rb_work_state_01.setChecked(false);
                rb_work_state_02.setChecked(true);
                rb_work_state_03.setChecked(false);
                work_num = 1;
                jobstatenew = mrinfo.getJobstate();
            } else if ("在校生".equals(mrinfo.getJobstate())) {
                rb_work_state_01.setChecked(false);
                rb_work_state_02.setChecked(false);
                rb_work_state_03.setChecked(true);
                work_num = 2;
                jobstatenew = mrinfo.getJobstate();
            } else {
                rb_work_state_01.setChecked(false);
                rb_work_state_02.setChecked(false);
                rb_work_state_03.setChecked(false);
                work_num = 3;
                jobstatenew = mrinfo.getJobstate();
            }
        } else {
            work_num = 0;
            rb_work_state_01.setChecked(false);
            rb_work_state_02.setChecked(false);
            rb_work_state_03.setChecked(false);
        }

        //联系方式
        if (mrinfo.getPhone() != null && mrinfo.getPhone().length() > 0) {
            ed_phone.setText(mrinfo.getPhone() + "");
        }

        //联系邮箱
        if (mrinfo.getEmail() != null && mrinfo.getEmail().length() > 0) {
            ed_email.setText(mrinfo.getEmail() + "");
        }

        //到岗时间 spinner
        if (datalist16 != null && datalist16.size() > 0) {
            if (mrinfo.getHiredate() != null && mrinfo.getHiredate().length() > 0) {
                for (int i = 0; i < datalist16.size(); i++) {
                    String arrivetimes = mrinfo.getHiredate();
                    if (arrivetimes.equals(datalist16.get(i))) {
                        tv_arrive_time_02.setSelection(i);
                        arrivetime = datalist16.get(i);
                    }
                }
            } else {
                tv_arrive_time_02.setSelection(0);
                arrivetime = datalist16.get(0);
            }
        } else {
            tv_arrive_time_02.setSelection(0);
            arrivetime = datalist16.get(0);
        }
        //目标城市
        if (mrinfo.getWantcity() != null && mrinfo.getWantcity().length() > 0) {
            shi_spinner02.setText(mrinfo.getWantcity() + "");
        }

        //期望薪资  spinner
        if (datalist2 != null && datalist2.size() > 0) {
            if (mrinfo.getWantsalary() != null && mrinfo.getWantsalary().length() > 0) {
                for (int i = 0; i < datalist2.size(); i++) {
                    String Wantsalary = mrinfo.getWantsalary();
                    if (Wantsalary.equals(datalist2.get(i))) {
                        tv_money_want_02.setSelection(i);
                        wantsalary = datalist2.get(i);
                        Log.e("wantsaly", "--------------->" + wantsalary);
                    }
                }
            } else {
                tv_money_want_02.setSelection(0);
                wantsalary = datalist2.get(0);
                Log.e("wantsaly1", "--------------->" + wantsalary);
            }
        } else {
            tv_money_want_02.setSelection(0);
            wantsalary = datalist2.get(0);
        }
        //期望职位 spinner
        if (datalist13 != null && datalist13.size() > 0) {
            if (mrinfo.getWantposition() != null && mrinfo.getWantposition().length() > 0) {
                for (int i = 0; i < datalist13.size(); i++) {
                    String workname = mrinfo.getWantposition();
                    if (workname.equals(datalist13.get(i))) {
                        tv_position_02.setSelection(i);
                        jobWantdata = datalist13.get(i);
                    }
                }
            } else {
                tv_position_02.setSelection(0);
                jobWantdata = datalist13.get(0);
            }
        } else {
            tv_position_02.setSelection(0);
            jobWantdata = datalist13.get(0);
        }

        //自我评价
        if (mrinfo.getDescription() != null && mrinfo.getDescription().length() > 0) {
            ed_evaluation.setText(mrinfo.getDescription());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    protected void ShowPickDialog() {
        new AlertDialog.Builder(mactivity, android.app.AlertDialog.THEME_HOLO_LIGHT).setNegativeButton("相册", new DialogInterface.OnClickListener() {
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
                        Toast.makeText(mactivity, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
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
            img_head.setImageDrawable(drawable);
            picname = "avatar" + user.getUserid() + String.valueOf(new Date().getTime());
            storeImageToSDCARD(photo, picname, filepath);
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
            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + path, img_head, options);
            tv_head.setVisibility(View.GONE);
            fos.flush();
            fos.close();
            if (head != null&&head.length() > 0) {
                if (HttpConnect.isConnnected(mactivity)) {
                    new StudyRequest(mactivity, handler).updateUserImg(head, KEY);
                } else {
                    Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(mactivity,"请重新拍照", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (iS) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                switch (v.getId()) {
                    case R.id.img_head://头像
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            getCheck();
                            bianjiview();
                            iS = false;
                        }
                        break;
                    case R.id.rg_sex_state://性别
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            bianjiview();
                            iS = false;
                        }
                        break;
                    case R.id.linear_birthdat_data://出生日期
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            bianjiview();
                            iS = false;
                        }
                        break;
                    case R.id.tv_work_education://学历
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            bianjiview();
                            iS = false;
                        }
                        break;
                    case R.id.linear_location://居住地
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            setEnable(true);
                        }
                        break;
                    case R.id.tv_work_02://工作经验
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            bianjiview();
                            iS = false;
                        }
                        break;
                    case R.id.tv_work_jobname://职称
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            bianjiview();
                            iS = false;
                        }
                        break;
                    case R.id.tv_money_02://目前薪资
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            bianjiview();
                            iS = false;
                        }
                        break;
                    case R.id.rg_work_state://在职状态
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            bianjiview();
                            iS = false;
                        }
                        break;
                    case R.id.tv_arrive_time_02://到岗时间
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            bianjiview();
                            iS = false;
                        }
                        break;
                    case R.id.city_two_linear://目标城市
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            bianjiview();
                            iS = false;
                        }
                        break;
                    case R.id.tv_money_want_02://目标（期望）薪资
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            bianjiview();
                            iS = false;
                        }
                        break;
                    case R.id.tv_position_02://期望职位
                        if (mrinfo != null) {
                            isShowDialog();
                        } else {
                            bianjiview();
                            iS = false;
                        }
                        break;
                }
                return true;
            }
            return true;
        }else {
            return false;
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
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPageStart(this, "个人简历编辑");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "个人简历编辑");
    }
}
