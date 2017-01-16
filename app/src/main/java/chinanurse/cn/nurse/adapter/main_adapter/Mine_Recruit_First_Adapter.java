package chinanurse.cn.nurse.adapter.main_adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Message;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.NurseEmployTalentBean;
import chinanurse.cn.nurse.bean.mine_main_bean.Mine_recruit_bean;
import chinanurse.cn.nurse.pnlllist.SlideView;
import chinanurse.cn.nurse.pnlllist.SwipeItemLayout;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        SlideView slideView = (SlideView) convertView;
        if (slideView == null) {
            switch (type_num) {
                case 0:
                    View itemView = View.inflate(activity,R.layout.listview_getresume, null);

                    slideView = new SlideView(activity);
                    slideView.setContentView(itemView);

                    vh = new ViewHolder();
//                    slideView.setOnSlideListener();
//                    View view01 = LayoutInflater.from(activity).inflate(R.layout.listview_getresume, null);
//                    View view02 = LayoutInflater.from(activity).inflate(R.layout.item_delet, null);
//                    slideView = new SwipeItemLayout(view01, view02, null, null);
//                    vh = new ViewHolder();
//                    convertView = View.inflate(activity, R.layout.listview_getresume, null);
//                    vh.news_delet = (TextView) itemView.findViewById(R.id.news_delet);
                    vh.tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                    vh.tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                    break;
            }
            slideView.setTag(vh);

        } else {
            vh = (ViewHolder) slideView.getTag();
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"+"\t\t\t"+"HH:mm");
                long time = Long.parseLong(list.get(position).getCreate_time());
                String timetv = simpleDateFormat.format(new Date(time * 1000));
//                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//                String str = simpleDateFormat.format(curDate);
//                int timemm = Integer.valueOf(str.substring(14))-Integer.valueOf(timetv.substring(14));
//                int timeHH = Integer.valueOf(str.substring(11,13))-Integer.valueOf(timetv.substring(11,13));
//                int timedd = Integer.valueOf(str.substring(8,10))-Integer.valueOf(timetv.substring(8,10));
//                int timeMM = Integer.valueOf(str.substring(5,7))-Integer.valueOf(timetv.substring(5,7));
//                int timeYY = Integer.valueOf(str.substring(0,4))-Integer.valueOf(timetv.substring(0,4));
//                Log.e("timemm","|||||||||||||||||||"+timemm);
//                Log.e("timeHH","|||||||||||||||||||"+timeHH);
//                Log.e("timedd","|||||||||||||||||||"+timedd);
//                Log.e("timeMM","|||||||||||||||||||"+timeMM);
//                Log.e("timeYY","|||||||||||||||||||"+timeYY);
//                if (timemm > 0&&timeHH <= 0){
//                    vh.tv_time.setText(timetv+"\t\t"+String.valueOf(timemm)+"分钟前");
//                }else if (timeHH > 0&&timedd == 0&&timeMM == 0&&timeYY == 0){
//                    vh.tv_time.setText(timetv+"\t\t"+String.valueOf(timeHH)+"小时前");
//                }else {
                    vh.tv_time.setText(timetv);
//                }
//                vh.news_delet.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(activity,"点击了第"+position+"条",Toast.LENGTH_SHORT).show();
//                    }
//                });
                break;
        }
        return slideView;
    }
    class ViewHolder {
        TextView tv_name, tv_position, tv_time,news_delet;

    }
}
