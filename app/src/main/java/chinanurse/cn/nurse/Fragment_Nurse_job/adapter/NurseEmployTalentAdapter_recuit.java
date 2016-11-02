package chinanurse.cn.nurse.Fragment_Nurse_job.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Nurse_job.bean.Talent_work_bean;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.NurseEmployTalentBean;
import chinanurse.cn.nurse.bean.TalentAdapter_bean;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by wzh on 2016/6/25.
 */
public class NurseEmployTalentAdapter_recuit extends BaseAdapter  {
    private static final int INDERVIEW = 1;
    private static final int GETMYPUBLISHJOB = 4;

    private ImageView ivPic;
    private List<Talent_work_bean.DataBean> talentDataList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions displayImage;
    private LinearLayout interview ;
    private Context mcontext;
    private String companyid,userid;
    private int type;
    private TalentAdapter_bean talenbean;
    private UserBean user;


    public NurseEmployTalentAdapter_recuit(Context mContext, List<Talent_work_bean.DataBean> talentDataList,int type) {
        this.talentDataList = talentDataList;
        this.mcontext = mContext;
        this.type = type;
        user = new UserBean(mContext);
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(null).showImageOnFail(null)
                .cacheInMemory(true).cacheOnDisc(true).build();
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
        ViewHolder vh  ;
        if (convertView == null){
            vh = new ViewHolder();
            switch (type){
                case 0:
                    convertView = View.inflate(mcontext, R.layout.listview_getresume_personal, null);
                    vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    vh.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
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
                    vh.tvcertificate_new = (TextView) convertView.findViewById(R.id.recruitment_certificate_new);//证件
                    vh.tvcreatetime = (TextView) convertView.findViewById(R.id.recruitment_createtime);//证件
                    vh.post_resume = (LinearLayout) convertView.findViewById(R.id.post_resume);
                    vh.tvAddress = (TextView) convertView.findViewById(R.id.nurse_employ_address);//地址
                    break;

            }
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case 0:
                if (!"".equals(talentDataList.get(position).getCompanyname())) {
                    vh.tv_name.setText(talentDataList.get(position).getCompanyname()+"");
                }
                if (!"".equals(talentDataList.get(position).getJobtype())) {
                    vh.tv_position.setText(talentDataList.get(position).getJobtype()+"");
                }
                break;
            case 1:
                vh.tvTitle.setText(talentDataList.get(position).getTitle()+"");
                vh.tvmoney.setText(talentDataList.get(position).getSalary()+"");
                vh.tvcertificate.setText(talentDataList.get(position).getEducation()+"");
                vh.tvsecurity.setText(talentDataList.get(position).getWelfare()+"");
                vh.tvposition.setText(talentDataList.get(position).getJobtype()+"");
                if (talentDataList.get(position).getCreate_time() != null&&talentDataList.get(position).getCreate_time().length() >0){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    long time = Long.parseLong(talentDataList.get(position).getCreate_time());
                    vh.tvcreatetime.setText(simpleDateFormat.format(new Date(time*1000)));
                }
                vh.tvcertificate_new.setText(talentDataList.get(position).getCertificate()+"");
                vh.tvjobtime.setText(talentDataList.get(position).getExperience()+"");
                vh.post_resume.setVisibility(View.GONE);
                vh.tvAddress.setText(talentDataList.get(position).getAddress()+"");
                break;
        }
        return convertView;

    }
    class ViewHolder {
        private TextView tv_name, tv_position ;

        private TextView tv_time,tvTitle,tvAddress,tvCompanyName,tvmoney,tvcertificate,tvsecurity,tvjobtime,tvposition,tvcertificate_new,tvcreatetime;
        private LinearLayout post_resume;
    }
}
