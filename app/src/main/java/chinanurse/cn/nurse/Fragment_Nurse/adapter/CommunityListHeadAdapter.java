package chinanurse.cn.nurse.Fragment_Nurse.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityTag;
import chinanurse.cn.nurse.R;

/**
 * Created by zhuchongkun on 2016/12/30.
 */

public class CommunityListHeadAdapter extends BaseAdapter {
    private String TAG = "MeFragmentAdapter";
    private Context context;
    private ArrayList<CommunityTag> communityListData;
    public CommunityListHeadAdapter(Context mContext, ArrayList<CommunityTag> communityList) {
        this.context = mContext;
        if (communityList == null) {
            communityList = new ArrayList<CommunityTag>();
        }
        this.communityListData = communityList;
    }

    @Override
    public int getCount() {
        return communityListData.size();
    }

    @Override
    public CommunityTag getItem(int position) {
        return communityListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        CommunityTag communityBeanData = communityListData.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_community_list_head, null);
            viewHolder.tv_community_name = (TextView) convertView.findViewById(R.id.tv_item_community_list_head);
            viewHolder.iv_community_photo = (ImageView) convertView.findViewById(R.id.iv_item_community_list_head);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_community_name.setText(communityBeanData.getTagName());
        if (communityBeanData.getIsSelected().equals("1")){
            viewHolder.iv_community_photo.setVisibility(View.VISIBLE);
            viewHolder.tv_community_name.setTextColor(context.getResources().getColor(R.color.purple));
        }else{
            viewHolder.iv_community_photo.setVisibility(View.INVISIBLE);
            viewHolder.tv_community_name.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }

    class ViewHolder {

        ImageView iv_community_photo;
        TextView tv_community_name;
    }
}
