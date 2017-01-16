package chinanurse.cn.nurse.Fragment_Nurse_job.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.StringTokenizer;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.Fragment_Nurse_job.Add_EmployTalent_Fragment;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.NurseEmployTalentBean;
import chinanurse.cn.nurse.bean.TalentAdapter_bean;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.picture.RoudImage;

/**
 * Created by wzh on 2016/6/25.
 */
public class NurseEmployTalentAdapter extends BaseAdapter  {
    private static final int INDERVIEW = 1;
    private static final int GETMYPUBLISHJOB = 4;
    private TextView tvName,tvWantMoney,tvWantPosition,tvAddress,tvEducation,tvYear,tvSex,tvSalary,tvJobtime,tvJobstate,tvCertificate;
    private RoudImage ivPic;
    private List<NurseEmployTalentBean.DataBean> talentDataList;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private LinearLayout interview,all_voerical ;
    private Context mcontext;
    private String companyid,userid,city;
    private TalentAdapter_bean talenbean;
    private UserBean user;
    private Handler handler;
    private DisplayImageOptions options;
    private String[] provinces;
    private Boolean isname = true;
    private Boolean isnameone = true;

    public NurseEmployTalentAdapter(Context mContext, List<NurseEmployTalentBean.DataBean> talentDataList,Handler handler) {
        this.talentDataList = talentDataList;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.mcontext = mContext;
        this.handler = handler;
        user = new UserBean(mContext);

    }
    @Override
    public int getCount() {
        return talentDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return talentDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.nurse_employ_resume_list_item, null);
        ivPic = (RoudImage) convertView.findViewById(R.id.iv_nurse_employ_resume_pic);//头像
        tvName = (TextView) convertView.findViewById(R.id.employ_resume_name);//姓名
        tvSex = (TextView) convertView.findViewById(R.id.employ_resume_sex);//性别  0是女   1是男
        tvEducation = (TextView) convertView.findViewById(R.id.employ_resume_education);//学历
        tvSalary = (TextView) convertView.findViewById(R.id.employ_resume_currentsalary);//当前薪资
        tvJobtime = (TextView) convertView.findViewById(R.id.employ_resume_birthday);//出生日期
        tvJobstate = (TextView) convertView.findViewById(R.id.employ_resume_jobstate);//工作状态
        tvCertificate= (TextView) convertView.findViewById(R.id.employ_resume_certificate);//工作状态
        tvAddress = (TextView) convertView.findViewById(R.id.employ_resume_address);//当前地址
        tvYear = (TextView) convertView.findViewById(R.id.employ_resume_year);//发布时间
        interview= (LinearLayout) convertView.findViewById(R.id.Invited_to_the_interview);
        all_voerical = (LinearLayout) convertView.findViewById(R.id.all_voerical);
        // 显示图片的配置
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.headlogo).showImageOnFail(R.mipmap.headlogo).cacheInMemory(true).cacheOnDisc(true).build();
//        imageLoader.displayImage(NetBaseConstant.NET_HOST + talentDataList.get(position).getAvatar(),
//                ivPic, displayImage);
        if (!"".equals(talentDataList.get(position).getAvatar())&&talentDataList.get(position).getAvatar() != null&&talentDataList.get(position).getAvatar().length() > 0){
            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + talentDataList.get(position).getAvatar(),ivPic,options);
        }

        tvName.setText(talentDataList.get(position).getName());
        if ("0".equals(talentDataList.get(position).getSex())){
            tvSex.setText("女");
        }else if ("1".equals(talentDataList.get(position).getSex())){
            tvSex.setText("男");
        }
        tvEducation.setText(talentDataList.get(position).getEducation()+"");
        String moneyname = talentDataList.get(position).getCurrentsalary();
        isname = moneyname.contains("&lt;");
        isnameone = moneyname.contains("&gt;");
        if (isname){
            tvSalary.setText("<"+talentDataList.get(position).getCurrentsalary().substring(4)+"");
        }else if (isnameone){
            tvSalary.setText(">"+talentDataList.get(position).getCurrentsalary().substring(4)+"");
        }else{
            tvSalary.setText(talentDataList.get(position).getCurrentsalary()+"");
        }
        tvJobtime.setText(talentDataList.get(position).getBirthday()+"");
        tvJobstate.setText(talentDataList.get(position).getJobstate()+"");
        tvCertificate.setText(talentDataList.get(position).getCertificate()+"");
        String s = new String(talentDataList.get(position).getAddress()+"");
        String[]  destString = s.split("-");
        tvAddress.setText(destString[0]+ "-"+destString[1]+"");
//        tvAddress.setText(talentDataList.get(position).getAddress()+"");
        tvYear.setText(talentDataList.get(position).getCreate_time()+"");

        companyid = user.getUserid();
        userid = talentDataList.get(position).getUserid();

        interview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getUserid() == null||user.getUserid().length() <= 0) {
                    Intent intent = new Intent(mcontext, LoginActivity.class);
                    mcontext.startActivity(intent);
                }else {
                    if (user.getUsertype() != null && user.getUsertype().length() > 0) {
                        if ("1".equals(user.getUsertype())) {
                            Toast.makeText(mcontext, "您是个人用户，不能邀请面试", Toast.LENGTH_SHORT).show();
                        } else if ("2".equals(user.getUsertype())) {
                        Message msg = new Message();
                        msg.what = 5;
                        msg.obj = position;
                        handler.sendMessage(msg);
                    }}
                }}});
        all_voerical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                msg.what = 10;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        });
        return convertView;
    }
}
