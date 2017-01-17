package chinanurse.cn.nurse.Fragment_Nurse.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.R;

/**
 * Created by zhuchongkun on 2016/12/15.
 */

public class MeFragmentAdapter extends BaseAdapter {
    private String TAG="MeFragmentAdapter";
    private Activity mactivity;
    private ArrayList<CommunityBean> communityListData;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_bg_nor).showImageOnFail(R.mipmap.img_bg_nor).cacheInMemory(true).cacheOnDisc(true).build();



    public MeFragmentAdapter(Activity activity, ArrayList<CommunityBean> communityList) {
        this.mactivity = activity;
        if (communityList==null){
            communityList=new ArrayList<CommunityBean>();
        }
        this.communityListData=communityList;
    }

    @Override
    public int getCount() {
        return communityListData.size();
    }

    @Override
    public CommunityBean getItem(int position) {
        return communityListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        CommunityBean communityBeanData=communityListData.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mactivity).inflate(
                    R.layout.item_me_fragment, null);
            viewHolder.tv_community_name = (TextView) convertView.findViewById(R.id.tv_item_fragment_me_community_name);
            viewHolder.tv_community_comment = (TextView) convertView.findViewById(R.id.tv_item_fragment_me_community_comment);
            viewHolder.iv_community_photo = (ImageView) convertView.findViewById(R.id.iv_item_fragment_me_community_photo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_community_name.setText(communityBeanData.getName());
        imageLoader.displayImage(CommunityNetConstant.NET_IMAGE_HOST+communityBeanData.getPhoto(),viewHolder.iv_community_photo);
        viewHolder.tv_community_comment.setText(communityBeanData.getPerson_count()+"人参与，"+communityBeanData.getForum_count()+"个帖子");
        return convertView;
    }

    class ViewHolder {

        ImageView iv_community_photo;
        TextView tv_community_name, tv_community_comment;
    }
}
