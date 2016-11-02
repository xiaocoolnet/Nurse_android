package chinanurse.cn.nurse.Fragment_Nurse_mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;

public class Mine_Recruit_Frist_Details extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout btnBack;
    private String name, sex, avatar, birthday, experience, education, certificate, wantposition, title,
            address, currentsalary, jobstate, description, email, phone, hiredate, wantcity, wantsalary;
    private TextView tvName, tvSex, tvBirthday, tvExperience, tvEducation, tvCertificate, tvWantPosition,
            tvTitle, tvAddress, tvCurrentSalary, tvJobState, tcDescription, tvEmail, tvPhone, tvHiredate, tvWantCity, tvWantSalary;
    private ImageView ivPic;
    private DisplayImageOptions displayImageOptions;
    private ImageLoader imageLoader = ImageLoader.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine__recruit__details);


        initDisPlay();
        initView();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        Log.e("name is ", name);
        sex = intent.getStringExtra("sex");
        avatar = intent.getStringExtra("avatar");
        birthday = intent.getStringExtra("birthday");
        experience = intent.getStringExtra("experience");
        education = intent.getStringExtra("education");
        certificate = intent.getStringExtra("certificate");
        wantposition = intent.getStringExtra("wantposition");
        certificate = intent.getStringExtra("certificate");
        title = intent.getStringExtra("title");
        address = intent.getStringExtra("address");
        currentsalary = intent.getStringExtra("currentsalary");
        jobstate = intent.getStringExtra("jobstate");
        description = intent.getStringExtra("description");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        hiredate = intent.getStringExtra("hiredate");
        wantcity = intent.getStringExtra("wantcity");
        wantsalary = intent.getStringExtra("wantsalary");
        setText();
        imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX + avatar, ivPic, displayImageOptions);

    }

    private void initDisPlay() {
        displayImageOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true).cacheOnDisc(true).build();
    }

    private void setText() {

        if (!"".equals(name)) {
            tvName.setText(name);
        }
        if (!"".equals(sex)) {
            tvSex.setText(sex);
        }

        if (!"".equals(birthday)) {
            tvBirthday.setText(birthday);
        }
        if (!"".equals(experience)) {
            tvExperience.setText(experience);
        }
        if (!"".equals(education)) {
            tvEducation.setText(education);
        }
        if (!"".equals(wantposition)) {
            tvWantPosition.setText(wantposition);
        }
        if (!"".equals(address)) {
            tvAddress.setText(address);
        }
        if (!"".equals(currentsalary)) {
            tvCurrentSalary.setText(currentsalary);
        }
        if (!"".equals(jobstate)) {
            tvJobState.setText(jobstate);
        }
        if (!"".equals(description)) {
            tcDescription.setText(description);
        }
        if (!"".equals(phone) || !"".equals(email)) {
            tvPhone.setText(phone + "    " + email);
        }
        if (!"".equals(hiredate)) {
            tvHiredate.setText(hiredate);
        }
        if (!"".equals(wantcity)) {
            tvWantCity.setText(wantcity);
        }
        if (!"".equals(wantsalary)) {
            tvWantSalary.setText(wantsalary);
        }


    }

    private void initView() {
        btnBack = (RelativeLayout) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        ivPic = (ImageView) findViewById(R.id.nurse_employ_resume_pic);
        tvName = (TextView) findViewById(R.id.nurse_employ_resume_name);
        tvSex = (TextView) findViewById(R.id.nurse_employ_resume_sex);
        tvBirthday = (TextView) findViewById(R.id.nurse_employ_resume_birthday);
        tvExperience = (TextView) findViewById(R.id.nurse_employ_resume_experience);
        tvEducation = (TextView) findViewById(R.id.nurse_employ_resume_education);
//        tvCertificate = (TextView) findViewById(R.id.nurse_employ_resume_name);证书
        tvWantPosition = (TextView) findViewById(R.id.nurse_employ_resume_want_position);
//        tvTitle = (TextView) findViewById(R.id.nurse_employ_resume_name);
        tvAddress = (TextView) findViewById(R.id.nurse_employ_resume_live_address);
        tvCurrentSalary = (TextView) findViewById(R.id.nurse_employ_resume_money_now);
        tvJobState = (TextView) findViewById(R.id.nurse_employ_resume_state);
        tcDescription = (TextView) findViewById(R.id.nurse_employ_resume_evaluate);
//        tvEmail = (TextView) findViewById(R.id.nurse_employ_resume_);
        tvPhone = (TextView) findViewById(R.id.nurse_employ_resume_contact);
        tvHiredate = (TextView) findViewById(R.id.nurse_employ_resume_arrive_time);
        tvWantCity = (TextView) findViewById(R.id.nurse_employ_resume_want_address);
        tvWantSalary = (TextView) findViewById(R.id.nurse_employ_resume_want_money);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
        }
    }

}
