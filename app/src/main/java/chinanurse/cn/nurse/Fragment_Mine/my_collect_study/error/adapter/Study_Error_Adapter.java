package chinanurse.cn.nurse.Fragment_Mine.my_collect_study.error.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.Fragment_Mine.my_collect_study.error.MyCollect_Question_Activity_mystudy;
import chinanurse.cn.nurse.MinePage.MyStudy.MineStudt_question_error_Bean;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.new_Activity.Answer_option_Bean;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class Study_Error_Adapter extends BaseAdapter {

    //我的错题新容器
    private List<Answer_option_Bean.DataEntity> answerlistbean = new ArrayList<Answer_option_Bean.DataEntity>();
    private List<Answer_option_Bean.DataEntity.AnswerlistEntity> list_AnswerlistEntity;
    Answer_option_Bean.DataEntity Answer_dataEntity;


    private Activity activity;
    private List<MineStudt_question_error_Bean.DataBean> list;

    public Study_Error_Adapter(Activity activity, List<MineStudt_question_error_Bean.DataBean> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {

        Log.e("size", "list.size()====" + list.size());

//        当用 list.size()时，数值太大，


        //list.size()
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
            vh.rela_layout = (LinearLayout) convertView.findViewById(R.id.rela_item_mycollect_first);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (list.size() > 0&&list.get(position).getPost_title() != null&&list.get(position).getPost_title().length() > 0){
            vh.tv_title.setText(list.get(position).getPost_title()+"");
        }else{
            vh.tv_title.setText("");
        }


//        switch (flag) {
//            case 0:

        vh.rela_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBundle(position, "collquestion", MyCollect_Question_Activity_mystudy.class, "我的试题收藏");

            }
        });



        return convertView;
    }
    @SuppressWarnings("rawtypes")
    public void getBundle(final int position, String key, Class clazz, String str) {
        MineStudt_question_error_Bean.DataBean fndData = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, fndData);
        bundle.putString("pagertype", "1");
        Intent intent = new Intent(activity, clazz);
        intent.putExtra("Qposition", position);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    class ViewHolder {
        private TextView tv_title;
        private LinearLayout rela_layout;
    }


}
