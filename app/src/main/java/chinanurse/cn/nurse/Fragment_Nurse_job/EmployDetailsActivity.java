package chinanurse.cn.nurse.Fragment_Nurse_job;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.MineResumeinfo;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.NurseEmployBean;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.dao.CommunalInterfaces;

/**
 * Created by wzh on 2016/6/25.
 */
public class EmployDetailsActivity extends Activity implements View.OnClickListener {


    private static final int GETRESUMEINFO = 4;
    private static final int APPLYjOB = 5;
    private Activity mactivity;
    private String id, title, infor, companyid, companyName, education, address, count, welfare, description, phone,type;
    private TextView tvTitle, tvCompanyName, tvCompanyIntro, tvEducation,
            tvAddress, tvCount, tvWelfare, tvDescrip, tvContact,title_top,tvContact_name,tvcrite,employ_tv_xinzi,employ_tv_jobtime;
    private RelativeLayout btnBack,ril_phonename;
    private UserBean user;
    private RelativeLayout rela_sendjianli;
    private NurseEmployBean.DataBean nurseEmployData;

    private LinearLayout linear_text_phone;
    private TextView employ_tv_contact_gone,tv_sendjianli;
    private MineResumeinfo.DataBean mrinfo;
    private String result;
    private Dialog dialog;
    private Boolean isname = true;
    private Boolean isnameone = true;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETRESUMEINFO://判断是否有简历
                    if (msg.obj != null) {
                        result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String data = json.getString("data");
                            if ("success".endsWith(json.optString("status"))) {
                                if ("".equals(data)) {
                                    new AlertDialog.Builder(mactivity).setTitle("系统提示").setMessage("请填写简历")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(mactivity, Add_EmployWork_Fragment.class);
                                                    mactivity.startActivity(intent);
                                                }
                                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                } else {
                                    JSONObject obj = new JSONObject(data);
                                    mrinfo = new MineResumeinfo.DataBean();
                                    mrinfo.setId(obj.getString("id"));
                                    mrinfo.setUserid(obj.getString("userid"));
                                    mrinfo.setName(obj.getString("name"));
                                    mrinfo.setSex(obj.getString("sex"));
                                    mrinfo.setAvatar(obj.getString("avatar"));
                                    mrinfo.setBirthday(obj.getString("birthday"));
                                    mrinfo.setExperience(obj.getString("experience"));
                                    mrinfo.setEducation(obj.getString("education"));
                                    mrinfo.setCertificate(obj.getString("certificate"));
                                    mrinfo.setWantposition(obj.getString("wantposition"));
                                    mrinfo.setTitle(obj.getString("title"));
                                    mrinfo.setAddress(obj.getString("address"));
                                    mrinfo.setCurrentsalary(obj.getString("currentsalary"));
                                    mrinfo.setJobstate(obj.getString("jobstate"));
                                    mrinfo.setDescription(obj.getString("description"));
                                    mrinfo.setEmail(obj.getString("email"));
                                    mrinfo.setPhone(obj.getString("phone"));
                                    mrinfo.setHiredate(obj.getString("hiredate"));
                                    mrinfo.setWantcity(obj.getString("wantcity"));
                                    if (HttpConnect.isConnnected(mactivity)) {
//                     new StudyRequest(mcontext, handler).send_ApplyJob(companyid, jobid, userid);
                                        new StudyRequest(mactivity, handler).ApplyJob_judge(user.getUserid(), companyid, id, APPLYjOB);
//                                        new StudyRequest(mactivity, handler).send_ApplyJob(companyid, id,  user.getUserid());
                                    } else {
                                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case APPLYjOB://判断是否投递过这家公司
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".endsWith(json.optString("status"))){
                                String data = json .getString("data");
                                if ("1".equals(data)){
                                    Toast.makeText(mactivity, "您已经投递过该公司", Toast.LENGTH_SHORT).show();
                                }else if ("0".equals(data)){
                                    if (HttpConnect.isConnnected(mactivity)) {
                                        new StudyRequest(mactivity, handler).send_ApplyJob(companyid, id, user.getUserid());
                                    } else {
                                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CommunalInterfaces.APPLYJOB:
                    JSONObject json = (JSONObject) msg.obj;
                    try {
                        String status = json.getString("status");
                        Log.e("data", status);
                        if (status.equals("success")) {
                            JSONObject obj = new JSONObject(json.getString("data"));
                            Log.e("data", "666666");
                            Toast.makeText(mactivity, "投递成功", Toast.LENGTH_SHORT).show();
                            if (obj.getString("score") != null &&obj.getString("score").length() > 0){
                                View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                dialog = new android.app.AlertDialog.Builder(mactivity).create();
                                dialog.show();
                                dialog.getWindow().setContentView(layout);
                                TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                tv_score.setText("+"+obj.getString("score"));
                                TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                tv_score_name.setText(obj.getString("event"));
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
                        } else {
                            Toast.makeText(mactivity, "投递失败", Toast.LENGTH_SHORT).show();
                            Log.e("data", "77777");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_employ_detailsactivity);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
        mactivity = this;
        user = new UserBean(mactivity);
        Intent intent = getIntent();
        nurseEmployData = (NurseEmployBean.DataBean) intent.getSerializableExtra("recruit");
        initView();
        if (nurseEmployData !=null ){
            id  = nurseEmployData.getId();
            companyid = nurseEmployData.getCompanyid();
        }
        if (nurseEmployData.getTitle() != null&&nurseEmployData.getTitle().length() > 0){
            tvTitle.setText(nurseEmployData.getTitle().toString()+"");
        }else{
            tvTitle.setText("无");
        }
        if (nurseEmployData.getCompanyname() != null&&nurseEmployData.getCompanyname().length() > 0){
            tvCompanyName.setText(nurseEmployData.getCompanyname().toString()+"");
        }else{
            tvCompanyName.setText("无");
        }
        if (nurseEmployData.getEducation() != null&&nurseEmployData.getEducation().length() > 0){
            tvEducation.setText(nurseEmployData.getEducation().toString()+"");
        }else{
            tvEducation.setText("无");
        }
        if (nurseEmployData.getAddress() != null&&nurseEmployData.getAddress().length() > 0){
            tvAddress.setText(nurseEmployData.getAddress().toString()+"");
        }else{
            tvAddress.setText("无");
        }
        if (nurseEmployData.getCount() != null&&nurseEmployData.getCount().length() > 0){
            tvCount.setText(nurseEmployData.getCount().toString()+"");
        }else{
            tvCount.setText("无");
        }
        if (nurseEmployData.getCompanyinfo() != null&&nurseEmployData.getCompanyinfo().length() > 0){
            tvCompanyIntro.setText(nurseEmployData.getCompanyinfo().toString()+"");
        }else{
            tvCompanyIntro.setText("无");
        }
        if (nurseEmployData.getWelfare() != null&&nurseEmployData.getWelfare().length() > 0){
            tvWelfare.setText(nurseEmployData.getWelfare().toString()+"");
        }else{
            tvWelfare.setText("无");
        }
        if (nurseEmployData.getDescription() != null&&nurseEmployData.getDescription().length() > 0){
            tvDescrip.setText(nurseEmployData.getDescription().toString()+"");
        }else{
            tvDescrip.setText("无");
        }
        if (user.getUserid() != null&&user.getUserid().length() > 0){
            linear_text_phone.setVisibility(View.GONE);
            ril_phonename.setVisibility(View.VISIBLE);
            if (nurseEmployData.getPhone() != null&&nurseEmployData.getPhone().length() > 0){
                tvContact.setText(nurseEmployData.getPhone().toString()+"");
            }else{
                tvContact.setText("无");
            }
            if (nurseEmployData.getLinkman() != null&&nurseEmployData.getLinkman().length() > 0){
                tvContact_name.setText(nurseEmployData.getLinkman().toString()+"");
            }else{
                tvContact_name.setText("无");
            }
        }else{
            linear_text_phone.setVisibility(View.VISIBLE);
            employ_tv_contact_gone.setOnClickListener(this);
            ril_phonename.setVisibility(View.GONE);
        }
        if (nurseEmployData.getCreate_time() != null&&nurseEmployData.getCreate_time().length() > 0){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long time = Long.parseLong(nurseEmployData.getCreate_time());
            tvcrite.setText(simpleDateFormat.format(new Date(time * 1000)));
        }else{
            tvcrite.setText("无");
        }
        //薪资要求
        if (nurseEmployData.getSalary() != null&&nurseEmployData.getSalary().length() > 0){
            String moneyname = nurseEmployData.getSalary();
            isname = moneyname.contains("&lt;");
            isnameone = moneyname.contains("&gt;");
            if (isname){
                employ_tv_xinzi.setText("<"+nurseEmployData.getSalary().substring(4) +"");//期望薪资
            }else if (isnameone){
                employ_tv_xinzi.setText(">"+nurseEmployData.getSalary().substring(4) +"");//期望薪资
            }else{
                employ_tv_xinzi.setText(nurseEmployData.getSalary() +"");//期望薪资
            }
        }else{
            employ_tv_xinzi.setText("无");
        }
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.employ_tv_title);
        tvCompanyName = (TextView) findViewById(R.id.employ_tv_company_name);
        tvCompanyIntro = (TextView) findViewById(R.id.employ_tv_company_intro);
        tvEducation = (TextView) findViewById(R.id.employ_tv_education);
        tvAddress = (TextView) findViewById(R.id.employ_tv_address);
        tvCount = (TextView) findViewById(R.id.employ_tv_count);
        tvWelfare = (TextView) findViewById(R.id.employ_tv_welfare);
        tvDescrip = (TextView) findViewById(R.id.employ_tv_description);
        tvcrite = (TextView) findViewById(R.id.employ_tv_company_time);
        tv_sendjianli = (TextView) findViewById(R.id.tv_sendjianli);
        tv_sendjianli.setOnClickListener(this);
        btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        title_top = (TextView) findViewById(R.id.top_title);
        title_top.setText("招聘详情");

        linear_text_phone = (LinearLayout) findViewById(R.id.linear_text_phone);//隐藏部分
        ril_phonename = (RelativeLayout) findViewById(R.id.linear_text_phone_name);//隐藏部分
        employ_tv_contact_gone = (TextView) findViewById(R.id.employ_tv_contact_gone);//可点击但是点击要判断是否有用户名
        tvContact = (TextView) findViewById(R.id.employ_tv_contact);//电话
        tvContact_name = (TextView) findViewById(R.id.employ_tv_contact_name);//姓名称呼
        employ_tv_xinzi = (TextView) findViewById(R.id.employ_tv_xinzi);//薪资
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", "----------->onResume");
        workview();
    }

    private void workview() {
        if (user.getUserid() != null&&user.getUserid().length() > 0){
            linear_text_phone.setVisibility(View.GONE);
            ril_phonename.setVisibility(View.VISIBLE);
        }else{
            linear_text_phone.setVisibility(View.VISIBLE);
            employ_tv_contact_gone.setOnClickListener(this);
            ril_phonename.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.employ_tv_contact_gone:
                Intent intent  = new Intent(mactivity, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sendjianli://发送简历
                if (user.getUserid() == null || user.getUserid().length() <= 0) {
                    Intent intentone = new Intent(mactivity, LoginActivity.class);
                    mactivity.startActivity(intentone);
                }else{
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).getResumeInfo(user.getUserid(), GETRESUMEINFO);
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_back://返回按钮
                unregisterReceiver(receiver);
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
            context.unregisterReceiver(this);
//            AlertDialog alert = builder.create();
//            alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            alert.show();
        }
    }
}