package chinanurse.cn.nurse.Fragment_Nurse.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;

import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;

/**
 * Created by zhuchongkun on 2016/12/17.
 */

public class FindRecommendRecyclerViewAdapter extends RecyclerView.Adapter<FindRecommendRecyclerViewAdapter.ViewHolder> {
    private String TAG="FindRecommendRecyclerViewAdapter";
    private Activity mactivity;
    private ArrayList<CommunityBean> communityBeanArrayList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_bg_nor).showImageOnFail(R.mipmap.img_bg_nor).cacheInMemory(true).cacheOnDisc(true).build();

    public FindRecommendRecyclerViewAdapter(Activity activity, ArrayList<CommunityBean> communityList) {
         this.mactivity=activity;
         this.communityBeanArrayList=communityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.item_community_recommend,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        imageLoader.displayImage(NetBaseConstant.NET_IMAGE_HOST+communityBeanArrayList.get(position).getPhoto(),holder.iv_photo,options);

        holder.tv_name.setText(communityBeanArrayList.get(position).getName());
        holder.tv_people.setText(communityBeanArrayList.get(position).getPerson_count()+"人");
        holder.tv_forum.setText(communityBeanArrayList.get(position).getForum_count()+"帖子");
        if (onItemClickLitener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    onItemClickLitener.onItemClick(holder.itemView,pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos=holder.getLayoutPosition();
                    onItemClickLitener.onItemLongClick(holder.itemView,pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return communityBeanArrayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_photo;
        public TextView tv_name,tv_people,tv_forum;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_photo= (ImageView) itemView.findViewById(R.id.iv_item_community_recommend_photo);
            tv_name= (TextView) itemView.findViewById(R.id.tv_item_community_recommend_name);
            tv_people= (TextView) itemView.findViewById(R.id.tv_item_community_recommend_people);
            tv_forum= (TextView) itemView.findViewById(R.id.tv_item_community_recommend_forum);
        }
    }
    public interface OnItemClickLitener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    private OnItemClickLitener onItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener){
        this.onItemClickLitener=onItemClickLitener;
    }
}
