package chinanurse.cn.nurse.Fragment_Nurse.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.AppApplication;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ChoicePublishCommunty;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.Fragment_Nurse.view.tag.Tag;
import chinanurse.cn.nurse.Fragment_Nurse.view.tag.TagListView;
import chinanurse.cn.nurse.Fragment_Nurse.view.tag.TagView;

import chinanurse.cn.nurse.R;

/**
 * Created by zhuchongkun on 2016/12/20.
 */

public class ChoicePublishCommunityAdapter extends BaseAdapter {
    private String TAG="ChoicePublishCommunityAdapter";
    private Context context;
    private ArrayList<ChoicePublishCommunty.ChoicePublishData> ChoicePublishArrayList;
    private List<Tag> tags=new ArrayList<Tag>();
    private Handler handler;
    public ChoicePublishCommunityAdapter(Context mContext, ArrayList<ChoicePublishCommunty.ChoicePublishData> forumList,Handler handler) {
        this.context = mContext;
        if (forumList==null){
            forumList=new ArrayList<ChoicePublishCommunty.ChoicePublishData>();
        }
        this.ChoicePublishArrayList=forumList;
        this.handler=handler;
    }

    @Override
    public int getCount() {
        return ChoicePublishArrayList.size();
    }

    @Override
    public ChoicePublishCommunty.ChoicePublishData getItem(int position) {
        return ChoicePublishArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final ChoicePublishCommunty.ChoicePublishData choicePublishBean=ChoicePublishArrayList.get(position);
        tags.clear();
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_choice_publish_commnunity, null);
            viewHolder.tv_community= (TextView) convertView.findViewById(R.id.tv_item_publish_community);
            viewHolder.lv_tag= (TagListView) convertView.findViewById(R.id.lv_item_publish_community_tag);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_community.setText(choicePublishBean.getTermName());
        for (int i=0;i<choicePublishBean.getCommunityData().size();i++){
            Tag tag=new Tag();
            tag.setId(Integer.valueOf(choicePublishBean.getCommunityData().get(i).getCommunityId()));
            tag.setTitle(choicePublishBean.getCommunityData().get(i).getCommunityName());
            tag.setChecked(true);
            tags.add(tag);
        }
        viewHolder.lv_tag.setTags(tags);
        viewHolder.lv_tag.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {;
                AppApplication.TagId=""+tag.getId();
                AppApplication.TagName=tag.getTitle();
                Message msg = Message.obtain();
                msg.what = 120;
                handler.sendMessage(msg);

            }
        });
        LogUtils.e(TAG,"forumBean"+choicePublishBean.toString());
        return convertView;
    }

    class ViewHolder {
        TextView tv_community;
        TagListView lv_tag;
    }

}
