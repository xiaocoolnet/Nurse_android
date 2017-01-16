package chinanurse.cn.nurse.Fragment_Nurse_job;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Nurse_job.bean.Talent_work_bean;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.bean.NurseEmployTalentBean;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.ui.HeadPicture;

/**
 * Created by wzh on 2016/6/26.
 */
public class EmployResumeDetailsActivity extends Activity implements View.OnClickListener {

    private static final int GETMYPUBLISHJOB = 2;
    private static final int INDERVIEWAPPLY = 3;
    private static final int JBOPOST = 4;
    private RelativeLayout btnBack;
    private static final int INDERVIEW = 1;
    private String companyid,userid,avatar,type;
    private TextView tvName, tvSex, tvBirthday, tvExperience, tvEducation, tvCertificate, tvWantPosition,
            tvTitle, tvAddress, tvCurrentSalary, tvJobState, tcDescription, tvEmail, tvPhone, tvHiredate, tvWantCity, tvWantSalary,tv_marriage,tv_height,tv_residence,
            tv_current_address,top_title,employ_tv_contact_gone,rela_yaoqingmianshi,tv_lianxiren;
    private ImageView ivPic;
    private DisplayImageOptions displayImageOptions;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private RelativeLayout linear_text_phone_name;
    private NurseEmployTalentBean.DataBean mine_recruit;
    private UserBean user;
    private  String result,jobid;
    private Gson gson;
    private Talent_work_bean ralentwork;
    private List<Talent_work_bean.DataBean> talentlist = new ArrayList<>();
    private LinearLayout linear_text_phone;
    private DisplayImageOptions options;
    private Boolean isname = true;
    private Boolean isnameone = true;
    private ProgressDialog dialogpgd;
    /**
     * 推送消息发送广播
     */
    private MyReceiver receiver;
    private News_list_type.DataBean newstypebean;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
//                case INDERVIEW:
//                    if (msg.obj != null){
//                        result = (String) msg.obj;
//                        try {
//                            JSONObject json = new JSONObject(result);
//                            if ("success".equals(json.optString("status"))){
//
//                                Toast.makeText(getApplicationContext(),"邀请成功",Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(getApplicationContext(),"邀请失败",Toast.LENGTH_SHORT).show();
//                            }
//                            dialogpgd.dismiss();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }else{
//                        dialogpgd.dismiss();
//                    }
//                    break;
                case GETMYPUBLISHJOB:
                    result = (String) msg.obj;
                    talentlist.clear();
                    gson = new Gson();
                    ralentwork = gson.fromJson(result, Talent_work_bean.class);
                    talentlist.addAll(ralentwork.getData());
                    String[] ralent = new String[talentlist.size()];
                    for (int i = 0; i < talentlist.size(); i++) {
                        ralent[i] = talentlist.get(i).getJobtype();
                    }
                    dialogpgd.dismiss();
//                    final String[] sexchoose = new String[]{"女", "男"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(EmployResumeDetailsActivity.this);
                    builder.setTitle("请选择");
                    builder.setSingleChoiceItems(ralent, 0, new DialogInterface.OnClickListener() {

                        @Override

                        public void onClick(DialogInterface dialog, int which) {
                            jobid = talentlist.get(which).getId();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (HttpConnect.isConnnected(getApplicationContext())) {
                                dialogpgd.setMessage("正在邀请...");
                                dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                dialogpgd.show();
                                new StudyRequest(getApplicationContext(), handler).InviteJob_judge(mine_recruit.getUserid(), user.getUserid(), jobid, INDERVIEWAPPLY);
//                            new StudyRequest(mcontext, handler).getMyPublishJobList(userid, INDERVIEW);
                                //userid个人  companyid企业   jobid招聘信息
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    break;
                case INDERVIEWAPPLY:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".endsWith(json.optString("status"))){
                                String data = json .getString("data");
                                if ("1".equals(data)){
                                    Toast.makeText(getApplicationContext(),"您已经邀请过该用户",Toast.LENGTH_SHORT).show();
                                    dialogpgd.dismiss();
                                }else if ("0".equals(data)){
                                    if (HttpConnect.isConnnected(getApplicationContext())) {
                                        new StudyRequest(getApplicationContext(), handler).InviteJob(user.getUserid(), jobid, mine_recruit.getUserid(),JBOPOST);
                                    } else {
                                        Toast.makeText(getApplicationContext(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case JBOPOST:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))){
                                JSONObject json = new JSONObject(obj.getString("data"));
                                Toast.makeText(getApplicationContext(), "邀请成功", Toast.LENGTH_SHORT).show();
                                View layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_score, null);
                                final Dialog dialog = new android.app.AlertDialog.Builder(getApplicationContext()).create();
                                dialog.show();
                                dialog.getWindow().setContentView(layout);
                                TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                tv_score.setText("+"+json.getString("score"));
                            }else{
                                Toast.makeText(getApplicationContext(), "邀请失败", Toast.LENGTH_SHORT).show();
                            }
                            dialogpgd.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        dialogpgd.dismiss();
                    }
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_employ_resume_details_activity);
        user = new UserBean(getApplicationContext());
        initDisPlay();
        Intent intent = getIntent();
        mine_recruit = (NurseEmployTalentBean.DataBean) intent.getSerializableExtra("recruit");
        if (mine_recruit != null){
            companyid = mine_recruit.getId();
            userid = mine_recruit.getUserid();
        }
        type = intent.getStringExtra("type");
        initView();

        setText();
        // 显示图片的配置
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_head_nor).showImageOnFail(R.mipmap.img_head_nor).cacheInMemory(true).cacheOnDisc(true).build();

    }

    private void initDisPlay() {
        displayImageOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true).cacheOnDisc(true).build();
    }

    private void setText() {
        if (mine_recruit.getAvatar().length() > 0&&null != mine_recruit.getAvatar()){//头像
            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + mine_recruit.getAvatar(), ivPic, options);
        }else{
            ivPic.setBackground(getResources().getDrawable(R.mipmap.headlogo));
        }
        if (mine_recruit.getSex().length() > 0&&null != mine_recruit.getSex()){
            if ("0".equals(mine_recruit.getSex())){
                tvSex.setText("女");
            }else if ("1".equals(mine_recruit.getSex())){
                tvSex.setText("男");
            }
        }else{
            tvSex.setText("保密");//性别
        }
        if (mine_recruit.getBirthday().length() > 0&&null != mine_recruit.getBirthday()){
            tvBirthday.setText(mine_recruit.getBirthday() +"");//出生日期
        }else{
            tvBirthday.setText("无");//名字
        }
        if (mine_recruit.getEducation().length() > 0&&null != mine_recruit.getEducation()){
            tvEducation.setText(mine_recruit.getEducation() +"");//学历
        }else{
            tvEducation.setText("无");
        }
        if (mine_recruit.getAddress().length() > 0&&null != mine_recruit.getAddress()){
            tvAddress.setText(mine_recruit.getAddress() +"");//居住地
        }else{
            tvAddress.setText("无");
        }
        if (mine_recruit.getExperience().length() > 0&&null != mine_recruit.getExperience()){
            tvExperience.setText(mine_recruit.getExperience() +"");//工作经验
        }else{
            tvExperience.setText("无");//名字
        }
        if (mine_recruit.getName().length() > 0&&null != mine_recruit.getName()){
            tvName.setText(mine_recruit.getName() +"");//出生日期
            tv_lianxiren.setText(mine_recruit.getName() +"");
        }else{
            tvName.setText("无");//名字
            tv_lianxiren.setText("无");
        }
        if (mine_recruit.getJobstate().length() > 0&&null != mine_recruit.getJobstate()){
            tvJobState.setText(mine_recruit.getJobstate() +"");//在职状态
        }else{
            tvJobState.setText("无");//名字
        }
        if (mine_recruit.getCurrentsalary().length() > 0&&null != mine_recruit.getCurrentsalary()){
            String moneyname = mine_recruit.getCurrentsalary();
            isname = moneyname.contains("&lt;");
            isnameone = moneyname.contains("&gt;");
            if (isname){
                tvCurrentSalary.setText("<"+mine_recruit.getCurrentsalary().substring(4) +"");//期望薪资
            }else if (isnameone){
                tvCurrentSalary.setText(">"+mine_recruit.getCurrentsalary().substring(4) +"");//期望薪资
            }else{
                tvCurrentSalary.setText(mine_recruit.getCurrentsalary() +"");//期望薪资
            }
        }else{
            tvCurrentSalary.setText("无");//名字
        }
        if (mine_recruit.getCertificate().length() > 0&&null != mine_recruit.getCertificate()){
            tvCertificate.setText(mine_recruit.getCertificate() +"");//职务
        }else{
            tvCertificate.setText("无");//名字
        }
        if (mine_recruit.getHiredate().length() > 0&&null != mine_recruit.getHiredate()){
            tvHiredate.setText(mine_recruit.getHiredate() +"");//到岗时间
        }else{
            tvHiredate.setText("无");//名字
        }
        if (mine_recruit.getWantsalary().length() > 0&&null != mine_recruit.getWantsalary()){
            String moneyname = mine_recruit.getWantsalary();
            isname = moneyname.contains("&lt;");
            isnameone = moneyname.contains("&gt;");
            if (isname){
                tvWantSalary.setText("<"+mine_recruit.getWantsalary().substring(4) +"");//期望薪资
            }else if (isnameone){
                tvWantSalary.setText(">"+mine_recruit.getWantsalary().substring(4) +"");//期望薪资
            }else{
                tvWantSalary.setText(mine_recruit.getWantsalary() +"");//期望薪资
            }

        }else{
            tvWantSalary.setText("无");//名字
        }
        if (mine_recruit.getWantposition().length() > 0&&null != mine_recruit.getWantposition()){
            tvWantPosition.setText(mine_recruit.getWantposition() +"");//目标目标职位
        }else{
            tvWantPosition.setText("无");//名字
        }
        if (mine_recruit.getWantcity().length() > 0&&null != mine_recruit.getWantcity()){
            tvWantCity.setText(mine_recruit.getWantcity() +"");//目标地点
        }else{
            tvWantCity.setText("无");//名字
        }
        if (mine_recruit.getDescription().length() > 0&&null != mine_recruit.getDescription()){
            tcDescription.setText(mine_recruit.getDescription() +"");//自我评价
        }else{
            tcDescription.setText("无");//名字
        }
        if (user.getUserid() != null&&user.getUserid().length() > 0){
            if ("2".equals(user.getUsertype())) {
                linear_text_phone.setVisibility(View.GONE);
                linear_text_phone_name.setVisibility(View.VISIBLE);
                if (mine_recruit.getPhone().length() > 0 && null != mine_recruit.getPhone()) {
                    tvPhone.setText(mine_recruit.getPhone() + "");
                } else {
                    tvPhone.setText("无");
                }
                if (mine_recruit.getEmail().length() > 0 && null != mine_recruit.getEmail()) {
                    tvEmail.setText(mine_recruit.getEmail() + "");
                } else {
                    tvEmail.setText("无");
                }
            }else{
                linear_text_phone.setVisibility(View.VISIBLE);
                linear_text_phone_name.setVisibility(View.GONE);
                employ_tv_contact_gone.setOnClickListener(this);
            }
        }else{
            linear_text_phone.setVisibility(View.VISIBLE);
            linear_text_phone_name.setVisibility(View.GONE);
            employ_tv_contact_gone.setOnClickListener(this);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "----------->onResume");
        StatService.onPageStart(this, "公司招聘信息");
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.USER_ACTION");
        registerReceiver(receiver, filter);
        workview();
    }

    private void workview() {
        if (user.getUserid() != null&&user.getUserid().length() > 0){
            linear_text_phone.setVisibility(View.GONE);
            linear_text_phone_name.setVisibility(View.VISIBLE);
        }else{
            linear_text_phone.setVisibility(View.VISIBLE);
            employ_tv_contact_gone.setOnClickListener(this);
            linear_text_phone_name.setVisibility(View.GONE);
        }
    }
    private void initView() {
        btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        dialogpgd=new ProgressDialog(this, android.app.AlertDialog.THEME_HOLO_LIGHT);
        btnBack.setOnClickListener(this);
        ivPic = (ImageView) findViewById(R.id.nurse_employ_resume_pic);
        tvName = (TextView) findViewById(R.id.nurse_employ_resume_name);//名字
        tvBirthday = (TextView) findViewById(R.id.nurse_employ_resume_birthday);//出生日期
        tvExperience = (TextView) findViewById(R.id.nurse_employ_resume_experience);//工作经验
        tvSex = (TextView) findViewById(R.id.nurse_employ_resume_sex);//性别
        tvAddress = (TextView) findViewById(R.id.nurse_employ_resume_live_address);//地址
        tvJobState = (TextView) findViewById(R.id.nurse_employ_resume_state);//在职状态
        tvCurrentSalary = (TextView) findViewById(R.id.nurse_employ_resume_money_now);//目前薪资
        tvHiredate = (TextView) findViewById(R.id.nurse_employ_resume_arrive_time);//到岗时间
        tvWantSalary = (TextView) findViewById(R.id.nurse_employ_resume_want_money);//期望薪资
        tvWantCity = (TextView) findViewById(R.id.nurse_employ_resume_want_address);//目标地点
        tvWantPosition = (TextView) findViewById(R.id.nurse_employ_resume_want_position);//目标职位
        tvEducation = (TextView) findViewById(R.id.nurse_employ_resume_education);//学历
        tvCertificate = (TextView) findViewById(R.id.nurse_employ_resume_jobstate);//职务
        tcDescription = (TextView) findViewById(R.id.nurse_employ_resume_evaluate);
        tvPhone = (TextView) findViewById(R.id.nurse_employ_resume_contact);
        tvEmail = (TextView) findViewById(R.id.nurse_employ_resume_contact_email);
        tv_lianxiren = (TextView) findViewById(R.id.tv_lianxiren);
//        rela_yaoqingmianshi = (TextView) findViewById(R.id.rela_yaoqingmianshi);
//        rela_yaoqingmianshi.setOnClickListener(this);
        top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText("个人简历");
        linear_text_phone = (LinearLayout) findViewById(R.id.linear_text_phone);//隐藏部分
        linear_text_phone_name = (RelativeLayout) findViewById(R.id.linear_text_phone_name);//隐藏部分
        employ_tv_contact_gone = (TextView) findViewById(R.id.employ_tv_contact_gone);//可点击但是点击要判断是否有用户名
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                unregisterReceiver(receiver);
                EmployResumeDetailsActivity.this.finish();
                break;

            case R.id.rela_yaoqingmianshi:
                if (user.getUserid() == null||user.getUserid().length() <= 0) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    if (user.getUsertype() != null && user.getUsertype().length() > 0) {
                        if ("1".equals(user.getUsertype())) {
                            Toast.makeText(getApplicationContext(), "您是个人用户，不能邀请面试", Toast.LENGTH_SHORT).show();
                        } else if ("2".equals(user.getUsertype())) {
                            if (HttpConnect.isConnnected(getApplicationContext())) {
                                dialogpgd.setMessage("正在获取您的职位列表..");
                                dialogpgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                dialogpgd.show();
                                new StudyRequest(getApplicationContext(), handler).getMyPublishJobList(user.getUserid(), GETMYPUBLISHJOB);
//                            new StudyRequest(mcontext, handler).getMyPublishJobList(userid, INDERVIEW);
                                //userid个人  companyid企业   jobid招聘信息
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
                            }
                        }
                }}


                break;
            case R.id.employ_tv_contact_gone:
                Intent intent  = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            newstypebean = (News_list_type.DataBean) intent.getSerializableExtra("fndinfo");
            String title = intent.getStringExtra("title");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("新通知")
                    .setMessage(title)
                    .setCancelable(false)
                    .setPositiveButton("立即查看", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("fndinfo", newstypebean);
                            Intent intent = new Intent(EmployResumeDetailsActivity.this, News_WebView_url.class);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            EmployResumeDetailsActivity.this.startActivity(intent);
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


    @Override
    protected void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(this, "公司招聘信息");
    }
}
