package chinanurse.cn.nurse.Fragment_Nurse_mine.my_collect_study.over.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import chinanurse.cn.nurse.Fragment_Nurse_mine.my_collect_study.over.MyCollect_Question_Activity_over;
import chinanurse.cn.nurse.MinePage.MyStudy.MineStudt_question_over_Bean;
import chinanurse.cn.nurse.R;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class Study_Over_Adapter extends BaseAdapter {

    //新容器的
    private Activity activity;
    private List<MineStudt_question_over_Bean.DataBean.QuestionBean> list;

    public Study_Over_Adapter(Activity activity, List<MineStudt_question_over_Bean.DataBean.QuestionBean> list) {
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
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.item_mycollect_second_error, null);
            vh = new ViewHolder();
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);//标题题目
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);//时间
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
//        if (list.get(position).getCreate_time() != null&&list.get(position).getCreate_time().length() > 0){
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            long time = Long.parseLong(list.get(position).getCreate_time().toString());
//            String timeone = simpleDateFormat.format(new Date(time*1000));
//            vh.tv_time.setText(timeone+"");
//        }else{
//            vh.tv_time.setText("");
//        }

//        switch (flag) {
//            case 0:

        vh.rela_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBundle(position, "collquestion", MyCollect_Question_Activity_over.class, "我的试题收藏");

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
        MineStudt_question_over_Bean.DataBean.QuestionBean fndData = list.get(position);
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
