package chinanurse.cn.nurse.Fragment_Mine.adapter;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.Mine_news_bean;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class Mine_news_Adapter extends BaseAdapter{
    private List<Mine_news_bean.DataBean> newsliat;
    private Activity mactivity;
    private int type;

    public Mine_news_Adapter(List<Mine_news_bean.DataBean> newsliat, Activity mactivity,int type) {
        this.newsliat = newsliat;
        this.mactivity = mactivity;
        this.type = type;
    }

    @Override
    public int getCount() {
        return newsliat.size();
    }

    @Override
    public Object getItem(int position) {
        return newsliat.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holder = null;
        if (convertView == null){
            convertView = View.inflate(mactivity,R.layout.mine_port_newslist,null);
            holder = new HolderView();
            holder.news_title = (TextView) convertView.findViewById(R.id.news_title);
            holder.child_choice = (TextView) convertView.findViewById(R.id.child_choice);
            holder.newscreate_time = (TextView) convertView.findViewById(R.id.newscreate_time);
            holder.news_title_num = (TextView) convertView.findViewById(R.id.news_title_num);
            convertView.setTag(holder);
        }else{
            holder = (HolderView) convertView.getTag();
        }
        holder.news_title_num.setText(String.valueOf(position+1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int timei = Integer.parseInt(newsliat.get(position).getCreate_time().toString());
        String times = sdf.format(new Date(timei*1000L));
        holder.newscreate_time.setText(times+"");
        holder.news_title.setText(newsliat.get(position).getContent()+"");
        if (newsliat.get(position).getReply() != null&&newsliat.get(position).getReply().size() > 0) {
            try {
                String text = String.format("%1$s" + ":\n" + newsliat.get(position).getReply().get(0).getContent().toString() + "", newsliat.get(position).getReply().get(0).getTitle() + "");
                ;
                int index[] = new int[1];
                index[0] = text.indexOf(newsliat.get(position).getReply().get(0).getTitle() + "");
                SpannableStringBuilder style = new SpannableStringBuilder(text);
                style.setSpan(new ForegroundColorSpan(mactivity.getResources().getColor(R.color.purple)), index[0], index[0] + newsliat.get(position).getReply().get(0).getTitle().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.child_choice.setText(style);
            } catch (Exception e) {

            }
        }else{
            holder.child_choice.setVisibility(View.GONE);
        }

        return convertView;
    }
    class HolderView{
        private TextView news_title,child_choice,newscreate_time,news_title_num;
    }
}
