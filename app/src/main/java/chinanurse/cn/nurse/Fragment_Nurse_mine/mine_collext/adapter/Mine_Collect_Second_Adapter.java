package chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext.MyCollect_Question_Activity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.MyCollectQuestion_Bean;

/**
 * 我的收藏---试题考题适配器
 * Created by Administrator on 2016/7/16 0016.
 */
public class Mine_Collect_Second_Adapter extends BaseAdapter {


    private Activity activity;
    private List<MyCollectQuestion_Bean.DataBean> list;
    private int flag;

    public Mine_Collect_Second_Adapter(Activity activity, List<MyCollectQuestion_Bean.DataBean> list, int flag) {
        this.activity = activity;
        this.list = list;
        this.flag = flag;
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

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.item_mycollect_second, null);
            vh = new ViewHolder();
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);//标题题目
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);//时间
            vh.rela_layout = (LinearLayout) convertView.findViewById(R.id.rela_item_mycollect_first);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (list.get(position).getTitle() != null&&list.get(position).getTitle().length() > 0){
            vh.tv_title.setText(list.get(position).getTitle());
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

//        switch (flag) {
//            case 0:

                vh.rela_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBundle(position, "collquestion", MyCollect_Question_Activity.class, "我的试题收藏");

                    }
                });

//                break;

//            case 1:
//                break;
//
//            default:
//                break;
//
//        }

        return convertView;
    }


    @SuppressWarnings("rawtypes")
    public void getBundle(final int position, String key, Class clazz, String str) {
        MyCollectQuestion_Bean.DataBean fndData = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, fndData);
        bundle.putString("pagertype", "1");
        Intent intent = new Intent(activity, clazz);
        intent.putExtra("Qposition", position);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    class ViewHolder {
        private TextView tv_title, tv_time;
        private LinearLayout rela_layout;
    }

}
