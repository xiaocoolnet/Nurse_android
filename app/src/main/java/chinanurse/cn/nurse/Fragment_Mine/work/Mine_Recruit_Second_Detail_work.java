package chinanurse.cn.nurse.Fragment_Mine.work;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import chinanurse.cn.nurse.Fragment_Nurse_job.bean.Talent_work_bean;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.dao.CommunalInterfaces;

public class Mine_Recruit_Second_Detail_work extends AppCompatActivity implements View.OnClickListener {


    private Activity mactivity;
    private String id, title, infor, companyid, companyName, education, address, count, welfare, description, phone,type;
    private TextView tvTitle, tvCompanyName, tvCompanyIntro, tvEducation,
            tvAddress, tvCount, tvWelfare, tvDescrip, tvContact,title_top,employ_tv_contact_name,tvcreatetime,employ_tv_salary;
    private RelativeLayout btnBack;
    private LinearLayout image_sendjianli;
    private UserBean user;
    private Talent_work_bean.DataBean mine_company;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommunalInterfaces.APPLYJOB:
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        String status = jsonObject.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Toast.makeText(mactivity, "投递成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mactivity, "投递失败", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_mine__recruit__second__detail);

        mactivity = this;
        user = new UserBean(mactivity);


        initView();
        Intent intent = getIntent();
        mine_company = (Talent_work_bean.DataBean) intent.getSerializableExtra("company");
//        id = mine_company.getId();
//        companyid = mine_company.getCompanyid();
        if (mine_company.getTitle() != null&&mine_company.getTitle().length() > 0){
            tvTitle.setText(mine_company.getTitle()+"");
        }else{
            tvTitle.setText("无");
        }
        if (mine_company.getCompanyname() != null&&mine_company.getCompanyname().length() > 0){
            tvCompanyName.setText(mine_company.getCompanyname()+"");
        }else{
            tvCompanyName.setText("无");
        }if (mine_company.getCompanyinfo() != null&&mine_company.getCompanyinfo().length() > 0){
            tvCompanyIntro.setText(mine_company.getCompanyinfo()+"");
        }else{
            tvCompanyIntro.setText("无");
        }if (mine_company.getEducation() != null&&mine_company.getEducation().length() > 0){
            tvEducation.setText(mine_company.getEducation()+"");
        }else{
            tvEducation.setText("无");
        }if (mine_company.getAddress() != null&&mine_company.getAddress().length() > 0){
            tvAddress.setText(mine_company.getAddress()+"");
        }else{
            tvAddress.setText("无");
        }if (mine_company.getCount() != null&&mine_company.getCount().length() > 0){
            tvCount.setText(mine_company.getCount()+"");
        }else{
            tvCount.setText("无");
        }if (mine_company.getWelfare() != null&&mine_company.getWelfare().length() > 0){
            tvWelfare.setText(mine_company.getWelfare()+"");
        }else{
            tvWelfare.setText("无");
        }
        if (mine_company.getDescription() != null&&mine_company.getDescription().length() > 0){
            tvDescrip.setText(mine_company.getDescription()+"");
        }else{
            tvDescrip.setText("无");
        }
        if (mine_company.getPhone() != null&&mine_company.getPhone().length() > 0){
            tvContact.setText(mine_company.getPhone()+"");
        }
        if (mine_company.getLinkman() != null&&mine_company.getLinkman().length() > 0){
            employ_tv_contact_name.setText(mine_company.getLinkman());
        }
        if (mine_company.getCreate_time() != null&&mine_company.getCreate_time().length() > 0){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long time = Long.parseLong(mine_company.getCreate_time());
            tvcreatetime.setText(simpleDateFormat.format(new Date(time * 1000)));
        }
        if (mine_company.getSalary() != null&&mine_company.getSalary().length() > 0){
            employ_tv_salary.setText(mine_company.getSalary()+"");
        }
    }

    private void initView() {
        title_top = (TextView) findViewById(R.id.top_title);
        title_top.setText("详情");
        tvTitle = (TextView) findViewById(R.id.employ_tv_title);
        tvCompanyName = (TextView) findViewById(R.id.employ_tv_company_name);
        tvCompanyIntro = (TextView) findViewById(R.id.employ_tv_company_intro);
        tvEducation = (TextView) findViewById(R.id.employ_tv_education);
        tvAddress = (TextView) findViewById(R.id.employ_tv_address);
        tvCount = (TextView) findViewById(R.id.employ_tv_count);
        tvWelfare = (TextView) findViewById(R.id.employ_tv_welfare);
        tvDescrip = (TextView) findViewById(R.id.employ_tv_description);
        tvContact = (TextView) findViewById(R.id.employ_tv_contact);
        btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        tvcreatetime = (TextView) findViewById(R.id.employ_tv_company_time);
        btnBack.setOnClickListener(this);
        image_sendjianli = (LinearLayout) findViewById(R.id.image_sendjianli);
        employ_tv_salary = (TextView) findViewById(R.id.employ_tv_salary);
//        if ("0".equals(type)){
//            image_sendjianli.setVisibility(View.VISIBLE);
//            image_sendjianli.setOnClickListener(this);
//        }else if ("1".equals(type)){
            image_sendjianli.setVisibility(View.GONE);
//        }
        employ_tv_contact_name = (TextView) findViewById(R.id.employ_tv_contact_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
//            case R.id.image_sendjianli:
//                String jobid = id;
//                String userid = user.getUserid();
//                if (NetUtil.isConnnected(mactivity)) {
//                    new StudyRequest(mactivity, handler).send_ApplyJob(companyid, jobid, userid);
//                }
//                break;

        }
    }
}