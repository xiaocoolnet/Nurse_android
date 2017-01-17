package chinanurse.cn.nurse.Fragment_Nurse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.R;

/**
 * Created by zhuchongkun on 2016/12/14.
 */

public class CommunityDetailRecommendAdapter extends BaseAdapter {
    private String TAG="CommunityDetailRecommendAdapter";
    private Context context;
    private ArrayList<ForumDataBean> forumBeanArrayList;
    public CommunityDetailRecommendAdapter(Context mContext, ArrayList<ForumDataBean> forumList) {
        this.context = mContext;
        if (forumList==null){
            forumList=new ArrayList<ForumDataBean>();
        }
        this.forumBeanArrayList=forumList;
    }

    @Override
    public int getCount() {
        return forumBeanArrayList.size();
    }

    @Override
    public ForumDataBean getItem(int position) {
        return forumBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final ForumDataBean forumBean=forumBeanArrayList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_community_detail_recommend, null);
            viewHolder.iv_top=(ImageView) convertView.findViewById(R.id.iv_item_find_fragment_recommend_top);
            viewHolder.iv_best = (ImageView) convertView.findViewById(R.id.iv_item_find_fragment_recommend_best);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_find_fragment_recommend);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LogUtils.e(TAG,"forumBean"+forumBean.toString());
        viewHolder.tv_title.setText(forumBean.getTitle());
        if (forumBean.getIsTop().equals("1")){
            viewHolder.iv_top.setVisibility(View.VISIBLE);
        }else{
            viewHolder.iv_top.setVisibility(View.GONE);
        }
        if (forumBean.getIsBest().equals("1")){
            viewHolder.iv_best.setVisibility(View.VISIBLE);
        }else{
            viewHolder.iv_best.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_top,iv_best;
        TextView tv_title;
    }
}
