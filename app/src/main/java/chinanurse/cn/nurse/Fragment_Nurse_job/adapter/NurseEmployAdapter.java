package chinanurse.cn.nurse.Fragment_Nurse_job.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.Fragment_Nurse_job.Add_EmployWork_Fragment;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.MineResumeinfo;
import chinanurse.cn.nurse.bean.NurseEmployBean;
import chinanurse.cn.nurse.dao.CommunalInterfaces;
import chinanurse.cn.nurse.picture.RoudImage;

/**
 * Created by wzh on 2016/6/25.
 */
public class NurseEmployAdapter extends BaseAdapter  {

    private static final int GETRESUMEINFO = 1;
    private List<NurseEmployBean.DataBean> nurseEmployDataList;
    private LayoutInflater layoutInflater;
    private Context mcontext;
    private String companyid,jobid;
    private MineResumeinfo.DataBean mrinfo;
    private String userid,usertype;
    private static final int APPLYjOB = 2;
    private Handler handlerone;
    private int type;
    private Boolean isname = true;
    private Boolean isnameone = true;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    // 显示图片的配置
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.nurse_ex_logo).showImageOnFail(R.mipmap.nurse_ex_logo).cacheInMemory(true).cacheOnDisc(true).build();

    public NurseEmployAdapter(Context mContext, List<NurseEmployBean.DataBean> nurseEmployDataList,String userid,String usertype,Handler handler,int type) {
        this.nurseEmployDataList = nurseEmployDataList;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.mcontext = mContext;
        this.userid = userid;
        this.handlerone = handler;
        this.usertype = usertype;
        this.type = type;
    }
    @Override
    public int getCount() {
        return nurseEmployDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return nurseEmployDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh  ;
        if (convertView == null){
            vh = new ViewHolder();
            switch (type){
                case 0:
                    convertView = View.inflate(mcontext, R.layout.listview_getresume_personal, null);
                    vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                    break;
                case 1:
                    convertView = View.inflate(mcontext,R.layout.nurse_employ_list_item, null);

                    vh.tvTitle = (TextView) convertView.findViewById(R.id.nurse_employ_title);//标题
                    vh.tvmoney = (TextView) convertView.findViewById(R.id.recruitment_money);//薪资待遇
                    vh.tvcertificate = (TextView) convertView.findViewById(R.id.recruitment_certificate);//学历要求
                    vh.tvsecurity = (TextView) convertView.findViewById(R.id.recruitment_security);//福利待遇
                    vh.tvjobtime = (TextView) convertView.findViewById(R.id.recruitment_jobtime);//工作年限
                    vh.tvposition = (TextView) convertView.findViewById(R.id.recruitment_position);
                    vh.tvcertificate_new = (TextView) convertView.findViewById(R.id.recruitment_certificate_new);//招聘人数
                    vh.tvcreatetime = (TextView) convertView.findViewById(R.id.recruitment_createtime);//证件
                    vh.post_resume = (LinearLayout) convertView.findViewById(R.id.post_resume);
                    vh.tvAddress = (TextView) convertView.findViewById(R.id.nurse_employ_address);//地址
                    vh.roudImage = (RoudImage) convertView.findViewById(R.id.commany_photo);//头像
                    vh.nurse_employ_company_name = (TextView) convertView.findViewById(R.id.nurse_employ_company_name);//公司名称

                    break;
            }
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case 0:
                if (!"".equals(nurseEmployDataList.get(position).getCompanyname())&&!"".equals(nurseEmployDataList.get(position).getTitle())) {
//                    String text = nurseEmployDataList.get(position).getCompanyname()+"公司邀请您面试"+nurseEmployDataList.get(position).getTitle()+"的职位";
                    String text = String.format(mcontext.getResources().getString(R.string.zhaopin),nurseEmployDataList.get(position).getCompanyname()+"",nurseEmployDataList.get(position).getTitle()+""); ;
                    int index[] = new int[2];
                    index[0] = text.indexOf(nurseEmployDataList.get(position).getCompanyname()+"");
                    index[1] = text.indexOf(nurseEmployDataList.get(position).getTitle()+"");
                    SpannableStringBuilder style=new SpannableStringBuilder(text);
                    style.setSpan(new ForegroundColorSpan(mcontext.getResources().getColor(R.color.purple)),index[0],index[0]+nurseEmployDataList.get(position).getCompanyname().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    style.setSpan(new ForegroundColorSpan(mcontext.getResources().getColor(R.color.purple)),index[1],index[1]+nurseEmployDataList.get(position).getTitle().length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    vh.tv_name.setText(style);
                }
                break;
            case 1:
                vh.tvTitle.setText(nurseEmployDataList.get(position).getTitle()+"");
                String moneyname = nurseEmployDataList.get(position).getSalary();
                isname = moneyname.contains("&lt;");
                isnameone = moneyname.contains("&gt;");
                if (isname){
                    vh.tvmoney.setText("<"+nurseEmployDataList.get(position).getSalary().substring(4)+"");
                }else if (isnameone){
                    vh.tvmoney.setText(">"+nurseEmployDataList.get(position).getSalary().substring(4)+"");
                }else{
                    vh.tvmoney.setText(nurseEmployDataList.get(position).getSalary()+"");
                }
                vh.tvcertificate.setText(nurseEmployDataList.get(position).getEducation()+"");
                vh.tvsecurity.setText(nurseEmployDataList.get(position).getWelfare()+"");
                vh.tvposition.setText(nurseEmployDataList.get(position).getJobtype()+"");
                if (nurseEmployDataList.get(position).getCreate_time() != null && nurseEmployDataList.get(position).getCreate_time().length() > 0) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
                    long time = Long.parseLong(nurseEmployDataList.get(position).getCreate_time());
                    vh.tvcreatetime.setText(simpleDateFormat.format(new Date(time * 1000)));
                } else {
                    vh.tvcreatetime.setText("");
                }
                vh.tvcertificate_new.setText(nurseEmployDataList.get(position).getCount()+"");
                vh.tvjobtime.setText(nurseEmployDataList.get(position).getExperience()+"");
                vh.nurse_employ_company_name.setText(nurseEmployDataList.get(position).getCompanyname()+"");
                Log.i("photo","------------->"+nurseEmployDataList.get(position).getPhoto());
                if (!"".equals(nurseEmployDataList.get(position).getPhoto())&&nurseEmployDataList.get(position).getPhoto() != null&&nurseEmployDataList.get(position).getPhoto().length() > 0){
                    Log.i("photo","------------->"+nurseEmployDataList.get(position).getPhoto());
                    imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + nurseEmployDataList.get(position).getPhoto(),vh.roudImage,options);
                }
                String s = new String(nurseEmployDataList.get(position).getAddress());
                String[]  destString = s.split("-");
                vh.tvAddress.setText(destString[0]+ "-"+destString[1]+"");
                vh.post_resume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (userid == null || userid.length() <= 0) {
                            Intent intent = new Intent(mcontext, LoginActivity.class);
                            mcontext.startActivity(intent);
                        } else {
                            if (usertype != null && usertype.length() > 0) {
                                if ("1".equals(usertype)) {
                                    Message msg = new Message();
                                    msg.what = 5;
                                    msg.obj = position;
                                    handlerone.sendMessage(msg);
                                } else if ("2".equals(usertype)) {
                                    Toast.makeText(mcontext, "您是企业用户，不能投递简历", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
                break;
        }
        return convertView;
    }
    class ViewHolder {
        private TextView tv_name, tv_position ;

        private TextView tv_time,tvTitle,tvAddress,tvCompanyName,tvmoney,tvcertificate,tvsecurity,tvjobtime,tvposition,tvcertificate_new,tvcreatetime,nurse_employ_company_name;
        private LinearLayout post_resume;
        private RoudImage roudImage;
    }
}
