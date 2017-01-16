package chinanurse.cn.nurse.Fragment_Nurse.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import chinanurse.cn.nurse.Fragment_Nurse.activity.CommunityDetailActivity;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;

/**
 * Created by zhuchongkun on 2016/12/14.
 */

public class PersonalHomePageAdapter extends BaseAdapter {
    private String TAG="PersonalHomePageAdapter";
    private Context context;
    private ArrayList<ForumDataBean> forumBeanArrayList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_bg_nor).showImageOnFail(R.mipmap.img_bg_nor).cacheInMemory(true).cacheOnDisc(true).build();

    public PersonalHomePageAdapter(Context mContext, ArrayList<ForumDataBean> forumList) {
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
                    R.layout.item_person_homepage, null);
            viewHolder.ll_one = (LinearLayout) convertView.findViewById(R.id.ll_item_find_fragment_one);
            viewHolder.ll_other = (LinearLayout) convertView.findViewById(R.id.ll_item_find_fragment_other);
            viewHolder.tv_head= (TextView) convertView.findViewById(R.id.tv_fragment_personal_homepage_head_forum);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_find_fragment_title);
            viewHolder.tv_title_other = (TextView) convertView.findViewById(R.id.tv_item_find_fragment_title_other);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_item_find_fragment_content);
            viewHolder.tv_content_other = (TextView) convertView.findViewById(R.id.tv_item_find_fragment_content_other);
            viewHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_item_find_fragment_comment);
            viewHolder.tv_like = (TextView) convertView.findViewById(R.id.tv_item_find_fragment_like);
            viewHolder.tv_location = (TextView) convertView.findViewById(R.id.tv_item_find_fragment_location);
            viewHolder.tv_community_name = (TextView) convertView.findViewById(R.id.tv_item_find_fragment_community_name);
            viewHolder.tv_community_into = (TextView) convertView.findViewById(R.id.tv_item_find_fragment_community_into);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_item_find_fragment_image);
            viewHolder.iv_image_one = (ImageView) convertView.findViewById(R.id.iv_item_find_fragment_image_one);
            viewHolder.iv_image_two = (ImageView) convertView.findViewById(R.id.iv_item_find_fragment_image_two);
            viewHolder.iv_image_three = (ImageView) convertView.findViewById(R.id.iv_item_find_fragment_image_three);
            viewHolder.iv_community_photo = (ImageView) convertView.findViewById(R.id.iv_item_find_fragment_community_photo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LogUtils.e(TAG,"forumBean"+forumBean.toString());
        String[] photo=forumBean.getPhoto();
        if (position==0){
            viewHolder.tv_head.setVisibility(View.VISIBLE);
        }else{
            viewHolder.tv_head.setVisibility(View.GONE);
        }
        if (photo==null||photo.length==0){//无图
            viewHolder.ll_one.setVisibility(View.GONE);
            viewHolder.ll_other.setVisibility(View.VISIBLE);
            viewHolder.tv_title_other.setText(forumBean.getTitle());
            viewHolder.tv_content_other.setText(forumBean.getContent());
            viewHolder.iv_image_one.setVisibility(View.GONE);
            viewHolder.iv_image_two.setVisibility(View.GONE);
            viewHolder.iv_image_three.setVisibility(View.GONE);

        }else if(photo.length==1||photo.length==2){//一张
            viewHolder.ll_one.setVisibility(View.VISIBLE);
            viewHolder.ll_other.setVisibility(View.GONE);
            viewHolder.tv_title.setText(forumBean.getTitle());
            viewHolder.tv_content.setText(forumBean.getContent());
            imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST+forumBean.getPhoto()[0],viewHolder.iv_image,options);


        }else if(photo.length>=3){//三张
            viewHolder.ll_one.setVisibility(View.GONE);
            viewHolder.ll_other.setVisibility(View.VISIBLE);
            viewHolder.tv_title_other.setText(forumBean.getTitle());
            viewHolder.tv_content_other.setText(forumBean.getContent());
            viewHolder.iv_image_one.setVisibility(View.VISIBLE);
            viewHolder.iv_image_two.setVisibility(View.VISIBLE);
            viewHolder.iv_image_three.setVisibility(View.VISIBLE);
            imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST+forumBean.getPhoto()[0],viewHolder.iv_image_one,options);
            imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST+forumBean.getPhoto()[1],viewHolder.iv_image_two,options);
            imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST+forumBean.getPhoto()[2],viewHolder.iv_image_three,options);
        }
        viewHolder.tv_like.setText(forumBean.getLike());
        viewHolder.tv_comment.setText(forumBean.getLike());
        viewHolder.tv_community_name.setText(forumBean.getCommunityName());
        if (forumBean.getPublishAddress()!=null&&forumBean.getPublishAddress().length()>0){
            viewHolder.tv_location.setText(forumBean.getPublishAddress());
        }else{
            viewHolder.tv_location.setText("发帖位置");
        }
        imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST+forumBean.getCommunityPhoto(),viewHolder.iv_community_photo,options);
        viewHolder.tv_community_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCommunityDetails=new Intent();
                CommunityBean communityBean=new CommunityBean();
                communityBean.setId(forumBean.getCommunityId());
                communityBean.setName(forumBean.getCommunityName());
                communityBean.setPhoto(forumBean.getCommunityPhoto());
                intentCommunityDetails.setClass(context, CommunityDetailActivity.class);
                intentCommunityDetails.putExtra("community",communityBean);
                intentCommunityDetails.putExtra("from","PersonalHomePageAdapter");
                context.startActivity(intentCommunityDetails);
            }
        });


        return convertView;
    }

    class ViewHolder {
        LinearLayout ll_one, ll_other;
        ImageView iv_image, iv_image_one, iv_image_two, iv_image_three, iv_community_photo;
        TextView tv_head,tv_title, tv_content, tv_comment, tv_like, tv_community_name, tv_community_into, tv_title_other, tv_content_other, tv_location;
    }
}
