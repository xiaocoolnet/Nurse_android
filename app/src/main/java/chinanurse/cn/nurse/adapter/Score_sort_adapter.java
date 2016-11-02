package chinanurse.cn.nurse.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.Score_sort_bean;
import chinanurse.cn.nurse.bean.Scroe_bean;
import chinanurse.cn.nurse.picture.RoudImage;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class Score_sort_adapter extends BaseAdapter{
    private List<Score_sort_bean.DataBean> scorelist;
    private Activity mactivtiy;
    private int type;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_head_nor).showImageOnFail(R.mipmap.img_head_nor).cacheInMemory(true).cacheOnDisc(true).build();;

    public Score_sort_adapter(List<Score_sort_bean.DataBean> scorelist, Activity mactivtiy, int type) {
        this.scorelist = scorelist;
        this.mactivtiy = mactivtiy;
        this.type = type;
    }

    @Override
    public int getCount() {
        return scorelist.size();
    }

    @Override
    public Object getItem(int position) {
        return scorelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder comholder = null;

        if (convertView == null){
            switch (type){
                case 0:

                    convertView = View.inflate(mactivtiy, R.layout.score_short_adapter, null);
                    comholder = new CommonViewHolder();
                    comholder.lv_imaage = (TextView) convertView.findViewById(R.id.score_sort_image);
                    comholder.lv_head_image = (RoudImage) convertView.findViewById(R.id.myinof_image_head_show);
                    comholder.score_name = (TextView) convertView.findViewById(R.id.score_name);
                    comholder.score_detail_num = (TextView) convertView.findViewById(R.id.score_detail_num);
                    comholder.score_time = (TextView) convertView.findViewById(R.id.score_time);
                    break;
            }
            convertView.setTag(comholder);
        }else{
            comholder = (CommonViewHolder) convertView.getTag();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
        if (position == 0){
            comholder.lv_imaage.setBackgroundResource(R.mipmap.rank_1);
            int timei = Integer.parseInt(scorelist.get(0).getTime().toString());
            String times = sdf.format(new Date(timei * 1000L));
            comholder.score_time.setText(times+"");
            comholder.score_detail_num.setText("+"+scorelist.get(0).getScore().toString()+"");
            comholder.score_name.setText(scorelist.get(0).getName().toString() + "");
            if (scorelist.get(0).getPhoto() != null &&scorelist.get(0).getPhoto().length() > 0){
                imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + scorelist.get(0).getPhoto(), comholder.lv_head_image, options);
            }
        }else if (position == 1){
            comholder.lv_imaage.setBackgroundResource(R.mipmap.rank_2);
            int timei = Integer.parseInt(scorelist.get(1).getTime().toString());
            String times = sdf.format(new Date(timei * 1000L));
            comholder.score_time.setText(times+"");
            comholder.score_detail_num.setText("+"+scorelist.get(1).getScore().toString()+"");
            comholder.score_name.setText(scorelist.get(1).getName().toString() + "");
            if (scorelist.get(1).getPhoto() != null &&scorelist.get(1).getPhoto().length() > 0){
                imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + scorelist.get(1).getPhoto(), comholder.lv_head_image, options);
            }
        }else if (position == 2){
            comholder.lv_imaage.setBackgroundResource(R.mipmap.rank_3);
            int timei = Integer.parseInt(scorelist.get(2).getTime().toString());
            String times = sdf.format(new Date(timei * 1000L));
            comholder.score_time.setText(times+"");
            comholder.score_detail_num.setText("+"+scorelist.get(2).getScore().toString()+"");
            comholder.score_name.setText(scorelist.get(2).getName().toString() + "");
            if (scorelist.get(2).getPhoto() != null &&scorelist.get(2).getPhoto().length() > 0){
                imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + scorelist.get(2).getPhoto(), comholder.lv_head_image, options);
            }
        }else if (position > 9){
            for (int i = 3;i < 10;i++){
                comholder.lv_imaage.setText(String.valueOf(i+1));
                int timei = Integer.parseInt(scorelist.get(i).getTime().toString());
                String times = sdf.format(new Date(timei * 1000L));
                comholder.score_time.setText(times+"");
                comholder.score_detail_num.setText("+"+scorelist.get(i).getScore().toString()+"");
                comholder.score_name.setText(scorelist.get(i).getName().toString() + "");
                if (scorelist.get(i).getPhoto() != null &&scorelist.get(i).getPhoto().length() > 0){
                    imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + scorelist.get(i).getPhoto(), comholder.lv_head_image, options);
                }
            }
        }else if (position <= 9){
            for (int i = 3;i < position+1;i++){
                comholder.lv_imaage.setText(String.valueOf(i + 1));
                int timei = Integer.parseInt(scorelist.get(i).getTime().toString());
                String times = sdf.format(new Date(timei * 1000L));
                comholder.score_time.setText(times+"");
                comholder.score_detail_num.setText("+"+scorelist.get(i).getScore().toString()+"");
                comholder.score_name.setText(scorelist.get(i).getName().toString() + "");
                if (scorelist.get(i).getPhoto() != null &&scorelist.get(i).getPhoto().length() > 0){
                    imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + scorelist.get(i).getPhoto(), comholder.lv_head_image, options);
                }
            }
        }
        return convertView;
    }
    public static class CommonViewHolder {
         private TextView score_name,score_detail_num,score_time,lv_imaage;
        private RoudImage lv_head_image;
    }
}
