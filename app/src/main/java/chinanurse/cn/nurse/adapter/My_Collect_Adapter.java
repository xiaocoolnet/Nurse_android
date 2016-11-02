package chinanurse.cn.nurse.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.MyCollect_bean;

/**
 * Created by Administrator on 2016/7/12.
 */
public class My_Collect_Adapter extends BaseAdapter{
    private List<MyCollect_bean.DataBean> collectlist;
    private Activity mactivity;
    private int typeid;
    private ViewHolder holder;
    private String time;

    public My_Collect_Adapter(Activity mactivity,List<MyCollect_bean.DataBean> collectlist, int typeid) {
        this.collectlist = collectlist;
        this.mactivity = mactivity;
        this.typeid = typeid;
    }

    @Override
    public int getCount() {
        return collectlist.size();
    }

    @Override
    public Object getItem(int position) {
        return collectlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=null;
        if (convertView == null){
            inflater = (LayoutInflater) mactivity.getSystemService(mactivity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_collect_adapter, null);
            holder = new ViewHolder();
            holder.news_title = (TextView) convertView.findViewById(R.id.collect_news_title_text);
            holder.news_description = (TextView) convertView.findViewById(R.id.collect_news_description);
            holder.news_createtime = (TextView) convertView.findViewById(R.id.collect_news_createtime);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.news_title.setText(collectlist.get(position).getPost_title()+"");
        holder.news_description.setText(collectlist.get(position).getPost_excerpt()+"");
        holder.news_createtime.setText(collectlist.get(position).getCreatetime()+"");
        return convertView;
    }
    class ViewHolder{
        private TextView news_title,news_description,news_createtime;
    }
}
