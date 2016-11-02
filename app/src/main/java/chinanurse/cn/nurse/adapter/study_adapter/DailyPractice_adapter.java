package chinanurse.cn.nurse.adapter.study_adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.study_main_bean.Daily_Practice_bean;
import chinanurse.cn.nurse.new_Activity.Question_Activity;

/**
 * Created by Administrator on 2016/7/23.
 */
public class DailyPractice_adapter extends BaseExpandableListAdapter {
    private int[] imagetitle = new int[]{R.mipmap.study_daily_icon_rn,R.mipmap.study_daily_icon_inter,R.mipmap.study_daily_icon_moon,
            R.mipmap.study_daily_icon0, R.mipmap.study_daily_icon1,R.mipmap.study_daily_icon2};
    private Activity activity;
    private List<Daily_Practice_bean.DataEntity> dailylist;
    private Viewholder holder;
    private Viewholermine holdermine;
    private List<Daily_Practice_bean.DataEntity.ChildlistEntity> childlist = new ArrayList<Daily_Practice_bean.DataEntity.ChildlistEntity>();
    private String type, userid;

    public DailyPractice_adapter(Activity activity, List<Daily_Practice_bean.DataEntity> dailylist, String type, String userid) {
        super();
        this.activity = activity;
        this.dailylist = dailylist;
        this.type = type;
        this.userid = userid;
    }

    //得到大布局的id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    //得到一级分类的内容


    @Override
    public Object getGroup(int groupPosition) {

        return dailylist.get(groupPosition).getName();

    }

    //得到大组成员的总数
    @Override
    public int getGroupCount() {

        return dailylist.size();
    }

    //得到大布局的view
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (0 == dailylist.get(groupPosition).getHaschild()) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                convertView = inflater.inflate(R.layout.daily_one, null);
                holder = new Viewholder();
                holder.study_daily_iamgetitle = (ImageView) convertView.findViewById(R.id.study_daily_iamgetitle);
                holder.study_daily_titleone = (TextView) convertView.findViewById(R.id.study_daily_titleone);
                holder.study_daily_numone = (TextView) convertView.findViewById(R.id.study_daily_numone);
                holder.start_answer_one = (RelativeLayout) convertView.findViewById(R.id.start_answer_one);
                holder.tv_anser_one = (TextView) convertView.findViewById(R.id.tv_anser_one);
                convertView.setTag(holder);
            } else if (1 == dailylist.get(groupPosition).getHaschild()) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                convertView = inflater.inflate(R.layout.daily_two, null);
                holder = new Viewholder();
                holder.study_daily_iamgetitle = (ImageView) convertView.findViewById(R.id.study_daily_iamgetitle);
                holder.study_daily_titleone = (TextView) convertView.findViewById(R.id.study_daily_titleone);
                convertView.setTag(holder);
            }

        } else {
            holder = (Viewholder) convertView.getTag();
        }
        int a = dailylist.get(groupPosition).getHaschild();
        if (0 == a) {
            holder.study_daily_iamgetitle.setImageResource(imagetitle[groupPosition]);
            holder.study_daily_titleone.setText(dailylist.get(groupPosition).getName());
            holder.study_daily_numone.setText(dailylist.get(groupPosition).getCount());
            holder.start_answer_one.setVisibility(View.VISIBLE);

            holder.start_answer_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != userid && userid.length() > 0) {
                        getBundle(groupPosition, "dailyData", Question_Activity.class, "每日一练");
                    } else {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                    }
                }

            });

        } else if (1 == a) {
            holder.study_daily_iamgetitle.setImageResource(imagetitle[groupPosition]);
            holder.study_daily_titleone.setText(dailylist.get(groupPosition).getName());
            Log.i("长度", "======" + "" + dailylist.get(groupPosition).getChildlist().size());
        }
        return convertView;
    }


    //得到小布局的id

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    //得到二级分类的内容

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dailylist.get(groupPosition).getChildlist().get(childPosition).getName();

    }
    //得到二级分类的数量

    @Override
    public int getChildrenCount(int groupPosition) {
        if (0 == dailylist.get(groupPosition).getHaschild()) {
            return 0;
        } else {
            return dailylist.get(groupPosition).getChildlist().size();
        }
    }
    //得到小组成员的view

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            convertView = inflater.inflate(R.layout.daily_one, null);
            holdermine = new Viewholermine();
            holdermine.study_daily_iamgetitle = (ImageView) convertView.findViewById(R.id.study_daily_iamgetitle);
            holdermine.study_daily_titleone = (TextView) convertView.findViewById(R.id.study_daily_titleone);
            holdermine.study_daily_numone = (TextView) convertView.findViewById(R.id.study_daily_numone);
            holdermine.start_answer_one = (RelativeLayout) convertView.findViewById(R.id.start_answer_one);
            holdermine.ril_daily_iamgetitle = (RelativeLayout) convertView.findViewById(R.id.ril_daily_iamgetitle);
            holdermine.tv_anser_one_one = (TextView) convertView.findViewById(R.id.tv_anser_one);
            holdermine.ril_daily_iamgetitle.setVisibility(View.INVISIBLE);
            convertView.setTag(holdermine);
        } else {
            holdermine = (Viewholermine) convertView.getTag();
        }
        if (0 == dailylist.get(groupPosition).getHaschild()) {
//            holdermine.study_daily_iamgetitle.setImageResource(0);
            holdermine.study_daily_titleone.setText("");
            holdermine.study_daily_numone.setText("");

        } else {
            holdermine.ril_daily_iamgetitle.setVisibility(View.INVISIBLE);
//            if (imagetitle.length >= dailylist.size()) {
//                holdermine.study_daily_iamgetitle.setImageResource(imagetitle[groupPosition]);
//            } else {
//                holdermine.study_daily_iamgetitle.setImageResource(imagetitle[0]);
//            }
            holdermine.study_daily_titleone.setText(dailylist.get(groupPosition).getChildlist().get(childPosition).getName());
            holdermine.study_daily_numone.setText(dailylist.get(groupPosition).getChildlist().get(childPosition).getCount());
            holdermine.start_answer_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != userid && userid.length() > 0) {
                        getBundletwo(groupPosition, childPosition, "dailyDatatwo", Question_Activity.class, "在线考试");
                    } else {
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                    }


                }
            });
        }
        return convertView;
    }

    //二级分类是否被选择
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    class Viewholder {
        private ImageView study_daily_iamgetitle;
        private TextView study_daily_titleone, study_daily_numone,tv_anser_one;
        private RelativeLayout start_answer_one;
    }

    class Viewholermine {
        private ImageView study_daily_iamgetitle;
        private TextView study_daily_titleone, study_daily_numone,tv_anser_one_one;
        private RelativeLayout start_answer_one,ril_daily_iamgetitle;
    }

    @SuppressWarnings("rawtypes")
    public void getBundle(final int position, String key, Class clazz, String str) {
        Daily_Practice_bean.DataEntity dailyData = dailylist.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, dailyData);
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        intent.putExtra("type", type);
        intent.putExtra("exam_time", dailylist.get(position).getExam_time());
        activity.startActivity(intent);
    }

    @SuppressWarnings("rawtypes")
    public void getBundletwo(final int position, final int positiontwo, String key, Class clazz, String str) {
        Daily_Practice_bean.DataEntity.ChildlistEntity dailyDatatwo = dailylist.get(position).getChildlist().get(positiontwo);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, dailyDatatwo);
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        intent.putExtra("type", type);
        intent.putExtra("exam_time", dailylist.get(position).getChildlist().get(positiontwo).getExam_time());
        activity.startActivity(intent);
    }
}
