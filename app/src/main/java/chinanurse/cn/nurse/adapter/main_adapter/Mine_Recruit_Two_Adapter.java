package chinanurse.cn.nurse.adapter.main_adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.NurseEmployBean;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class Mine_Recruit_Two_Adapter extends BaseAdapter {


    private List<NurseEmployBean.DataBean> list_recruit_two;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Context mContext;
    private DisplayImageOptions displayImage;

    public Mine_Recruit_Two_Adapter(Context mContext, List<NurseEmployBean.DataBean> list_recruit_two) {

        this.mContext = mContext;

        this.list_recruit_two = list_recruit_two;
        this.layoutInflater = LayoutInflater.from(mContext);
        displayImage = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnLoading(null).showImageOnFail(null)
                .cacheInMemory(true).cacheOnDisc(true).build();
    }


    @Override
    public int getCount() {
        return list_recruit_two.size();
    }

    @Override
    public Object getItem(int position) {
        return list_recruit_two.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder vh;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.nurse_employ_list_item, null);
            vh = new ViewHolder();


            vh.tvTitle = (TextView) convertView.findViewById(R.id.nurse_employ_title);
            vh.tvAddress = (TextView) convertView.findViewById(R.id.nurse_employ_address);
            vh.tvCompanyName = (TextView) convertView.findViewById(R.id.nurse_employ_company_name);


            vh.recruitment_money = (TextView) convertView.findViewById(R.id.recruitment_money);//薪资待遇
            vh.recruitment_certificate = (TextView) convertView.findViewById(R.id.recruitment_certificate);//学历
            vh.recruitment_security = (TextView) convertView.findViewById(R.id.recruitment_security);//社保
            vh.recruitment_jobtime = (TextView) convertView.findViewById(R.id.recruitment_jobtime);//工作时间
            vh.recruitment_position = (TextView)convertView.findViewById(R.id.recruitment_position);//招聘职位
            vh.recruitment_certificate_new = (TextView) convertView.findViewById(R.id.recruitment_certificate_new);//学位证书
            vh.post_resume = (LinearLayout) convertView.findViewById(R.id.post_resume);//点击按钮


            convertView.setTag(vh);


        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.post_resume.setVisibility(View.GONE);
        vh.tvTitle.setText(list_recruit_two.get(position).getTitle());
        vh.tvAddress.setText(list_recruit_two.get(position).getAddress());
        vh.tvCompanyName.setText(list_recruit_two.get(position).getCompanyname());


        vh.recruitment_money.setText(list_recruit_two.get(position).getSalary());
        vh.recruitment_certificate.setText(list_recruit_two.get(position).getEducation());
        vh.recruitment_security.setText(list_recruit_two.get(position).getWelfare());
        vh.recruitment_jobtime.setText(list_recruit_two.get(position).getExperience());
        vh.recruitment_position.setText(list_recruit_two.get(position).getTitle());
        vh.recruitment_certificate_new.setText(list_recruit_two.get(position).getCertificate());


        return convertView;
    }

    class ViewHolder {
        private TextView tvTitle, tvDescrip, tvAddress, tvCompanyName,recruitment_money,recruitment_certificate,
                recruitment_security,recruitment_jobtime,recruitment_position,recruitment_certificate_new;
        private LinearLayout post_resume;

    }

}
