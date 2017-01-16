package chinanurse.cn.nurse.adapter.main_adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import chinanurse.cn.nurse.Fragment_Mine.MyPost.Mine_Post_Detail;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.mine_main_bean.Mine_Post_bean;

/**
 * Created by Administrator on 2016/7/16 0016.
 */
public class Mine_Post_Adapter extends BaseAdapter {

    private Activity activity;
    private List<Mine_Post_bean.DataBean> list;


    public Mine_Post_Adapter(Activity activity, List<Mine_Post_bean.DataBean> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
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

        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.listview_mypost, null);
            vh = new ViewHolder();
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.tv_read = (TextView) convertView.findViewById(R.id.tv_read);
            vh.tv_like = (TextView) convertView.findViewById(R.id.tv_like);
            vh.rela_item_mypost = (RelativeLayout) convertView.findViewById(R.id.rela_item_mypost);
            convertView.setTag(vh);


        } else {

            vh = (ViewHolder) convertView.getTag();

        }
        vh.tv_like.setText(list.get(position).getLike().size() + "");
//        vh.tv_read
        vh.tv_time.setText(list.get(position).getWrite_time());
        vh.tv_title.setText(list.get(position).getTitle());
        vh.rela_item_mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBundle(position, "goabroad", Mine_Post_Detail.class, "学习信息");
            }
        });

        return convertView;
    }


    class ViewHolder {
        private TextView tv_title, tv_time, tv_read, tv_like;
        private RelativeLayout rela_item_mypost;


    }


    public void getBundle(final int position, String key, Class clazz, String str) {

        if (list != null && list.size() != 0) {
            Mine_Post_bean.DataBean nurse_Data = list.get(position);
            Log.e("position", "_____________" + position);
            Intent intent = new Intent(activity, clazz);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            Bundle bundle = new Bundle();
            bundle.putParcelable(key, nurse_Data);
            bundle.putString("pagertype", "1");
            intent.putExtras(bundle);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "数据加载中，稍候请重试！", Toast.LENGTH_SHORT).show();
        }


    }


}
