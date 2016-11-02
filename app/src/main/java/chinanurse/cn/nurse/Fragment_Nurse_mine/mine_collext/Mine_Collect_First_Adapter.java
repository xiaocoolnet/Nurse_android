package chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView;
import chinanurse.cn.nurse.WebView.News_WebView_collect;
import chinanurse.cn.nurse.WebView.News_WebView_study;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.bean.MyCollect_bean;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class Mine_Collect_First_Adapter extends BaseAdapter {


    private Activity activity;
    private List<FirstPageNews.DataBean> list;
    private int flag;

    public Mine_Collect_First_Adapter(Activity activity, List<FirstPageNews.DataBean> list, int flag) {
        this.activity = activity;
        this.list = list;
        this.flag = flag;
    }

    @Override
    public int getCount() {

        Log.e("result_data", "--------------" + list.size());

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
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.item_mycollect_first, null);
            vh = new ViewHolder();
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.tv_from = (TextView) convertView.findViewById(R.id.tv_from_textview);
            vh.rela_layout = (LinearLayout) convertView.findViewById(R.id.rela_item_mycollect_first);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (list.get(position).getPost_title() != null&&list.get(position).getPost_title().length() > 0){
            vh.tv_title.setText(list.get(position).getPost_title());
        }else{
            vh.tv_title.setText("");
        }
        if (list.get(position).getCreatetime() != null&&list.get(position).getCreatetime().length() > 0){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long time = Long.parseLong(list.get(position).getCreatetime().toString());
            String timeone = simpleDateFormat.format(new Date(time*1000));
            vh.tv_time.setText(timeone+"");
        }else{
            vh.tv_time.setText("");
        }
        if (list.get(position).getPost_source() != null&&list.get(position).getPost_source().length() > 0){
            vh.tv_from.setText(list.get(position).getPost_source());
        }else{
            vh.tv_from.setText("");
        }
        switch (flag) {
            case 0:

                vh.rela_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBundle(position, "fndinfo", News_WebView_collect.class, "学术会议");

                    }
                });
            case 1:

                vh.rela_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBundle(position, "fndinfo", News_WebView_collect.class, "学术会议");

                    }
                });

                break;
            
            default:
                break;

        }


        return convertView;
    }


    @SuppressWarnings("rawtypes")
    public void getBundle(final int position, String key, Class clazz, String str) {


        FirstPageNews.DataBean fndData = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, fndData);
        bundle.putString("pagertype", "1");
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


    class ViewHolder {

        private TextView tv_title, tv_time, tv_from;
        private LinearLayout rela_layout;


    }


}
