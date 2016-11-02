package chinanurse.cn.nurse.adapter.main_adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.NurseEmployTalentBean;
import chinanurse.cn.nurse.bean.mine_main_bean.Mine_recruit_bean;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class Mine_Recruit_First_Adapter extends BaseAdapter {

    private Activity activity;
    private List<NurseEmployTalentBean.DataBean> list;
    private int type_num;


    public Mine_Recruit_First_Adapter(Activity activity, List<NurseEmployTalentBean.DataBean> list, int type_num) {
        this.activity = activity;
        this.list = list;
        this.type_num = type_num;

    }

    @Override
    public int getCount() {
        Log.e("result_data", list.size() + "");

        return list.size();
    }

    @Override
    public Object getItem(int position) {


        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            switch (type_num) {
                case 0:
                    convertView = View.inflate(activity, R.layout.listview_getresume, null);
                    vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                    break;

                case 1:


            }
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        switch (type_num) {
            case 0:
                if (!"".equals(list.get(position).getName())&&!"".equals(list.get(position).getWantposition())) {
                    String text = String.format(activity.getResources().getString(R.string.yaoqing),list.get(position).getName()+"",list.get(position).getWantposition()+""); ;
                    int index[] = new int[2];
                    index[0] = text.indexOf(list.get(position).getName()+"");
                    index[1] = text.indexOf(list.get(position).getWantposition()+"");
                    SpannableStringBuilder style=new SpannableStringBuilder(text);
                    style.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.purple)),index[0],index[0]+list.get(position).getName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    style.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.purple)),index[1],index[1]+list.get(position).getWantposition().length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    vh.tv_name.setText(style);
                }
                break;

        }


        return convertView;
    }


    class ViewHolder {
        TextView tv_name, tv_position, tv_time;


    }

}
