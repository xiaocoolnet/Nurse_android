package chinanurse.cn.nurse.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.Scroe_bean;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class Score_adapter extends BaseAdapter{
    private List<Scroe_bean.DataBean> scorelist;
    private Activity mactivtiy;
    private int type;

    public Score_adapter(List<Scroe_bean.DataBean> scorelist, Activity mactivtiy,int type) {
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

                    convertView = View.inflate(mactivtiy, R.layout.score_adapter, null);
                    comholder = new CommonViewHolder();
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
        int timei = Integer.parseInt(scorelist.get(position).getCreate_time().toString());
        String times = sdf.format(new Date(timei*1000L));
        comholder.score_detail_num.setText("+"+scorelist.get(position).getScore().toString()+"");
        comholder.score_name.setText(scorelist.get(position).getEvent().toString()+"");
        comholder.score_time.setText(times+"");
        return convertView;
    }
    public static class CommonViewHolder {
         private TextView score_name,score_detail_num,score_time;
    }
}
