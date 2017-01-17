package chinanurse.cn.nurse.Fragment_Nurse.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chinanurse.cn.nurse.Fragment_Nurse.bean.CommunityBean;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.ToastUtils;
import chinanurse.cn.nurse.Fragment_Nurse.view.MyTextViewButton;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 2016/12/26.
 */

public class CommunityListAdapter extends BaseAdapter {
    private String TAG = "CommunityListAdapter";
    private Context context;
    private UserBean user;
    private ArrayList<CommunityBean> communityBeanArrayList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_bg_nor).showImageOnFail(R.mipmap.img_bg_nor).cacheInMemory(true).cacheOnDisc(true).build();


    public CommunityListAdapter(Context mContext, ArrayList<CommunityBean> communityList) {
        this.context = mContext;
        if (communityList == null) {
            communityList = new ArrayList<CommunityBean>();
        }
        this.communityBeanArrayList = communityList;
        user = new UserBean(context);
    }


    @Override
    public int getCount() {
        return communityBeanArrayList.size();
    }

    @Override
    public CommunityBean getItem(int position) {
        return communityBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        CommunityBean communityBeanData = communityBeanArrayList.get(position);
        LogUtils.e(TAG, "position" + position);
        LogUtils.e(TAG, "communityBean" + communityBeanData.toString());
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_community_list, null);
            viewHolder.tv_community_name = (TextView) convertView.findViewById(R.id.tv_item_community_list_name);
            viewHolder.tv_community_people = (TextView) convertView.findViewById(R.id.tv_item_community_list_people);
            viewHolder.tv_community_forum = (TextView) convertView.findViewById(R.id.tv_item_community_list_forum);
            viewHolder.tv_community_note = (TextView) convertView.findViewById(R.id.tv_item_community_list_note);
            viewHolder.iv_community_photo = (ImageView) convertView.findViewById(R.id.iv_item_community_list_photo);
            viewHolder.tv_join = (MyTextViewButton) convertView.findViewById(R.id.tv_item_community_list_join);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_community_name.setText(communityBeanData.getName());
        imageLoader.displayImage(CommunityNetConstant.NET_IMAGE_HOST + communityBeanData.getPhoto(), viewHolder.iv_community_photo, options);
        viewHolder.tv_community_people.setText(communityBeanData.getPerson_count() + "人");
        viewHolder.tv_community_forum.setText(communityBeanData.getForum_count() + "帖子");
        viewHolder.tv_community_note.setText(communityBeanData.getDescription());
        final String isJoin = communityBeanData.getJoin();
        if (isJoin!=null&&isJoin.equals("1")) {
            viewHolder.tv_join.setBackgroundResource(R.drawable.bg_join_ready);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_join_ready);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.tv_join.setCompoundDrawables(drawable, null, null, null);
            viewHolder.tv_join.setText("已加入");
            viewHolder.tv_join.setTextColor(context.getResources().getColor(R.color.whilte));
        } else {
            viewHolder.tv_join.setBackgroundResource(R.drawable.bg_join);
            Drawable drawable = context.getResources().getDrawable(R.mipmap.ic_join);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.tv_join.setCompoundDrawables(drawable, null, null, null);
            viewHolder.tv_join.setText("加入");
            viewHolder.tv_join.setTextColor(context.getResources().getColor(R.color.purple));
        }
        final int p = position;
        viewHolder.tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    if (isJoin!=null&&isJoin.equals("1")) {
                        ToastUtils.ToastShort(context,"你已经加入该圈子！");
                    } else {
                        joinCommunity(p);

                    }
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }

            }
        });

        return convertView;
    }

    private void joinCommunity(final int p) {
//        userid,cid
        final RequestParams r = new RequestParams();
        r.put("userid", user.getUserid());
        r.put("cid", communityBeanArrayList.get(p).getId());
        NurseAsyncHttpClient.get(CommunityNetConstant.ADD_COMMUNITY, r, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
//                    {
//                        "status": "error",
//                            "data": "had add"
//                    }
//                    {
//                        "status": "success",
//                            "data": "success"
//                    }
                    LogUtils.e(TAG,"-->"+response.toString());

                    try {
                        if (response.getString("status").equals("success")){
                            communityBeanArrayList.get(p).setJoin("1");
                            notifyChanged();
                        }else{
                            ToastUtils.ToastShort(context,"加入失败！");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private void notifyChanged() {
        this.notifyDataSetChanged();
    }


    class ViewHolder {
        MyTextViewButton tv_join;
        ImageView iv_community_photo;
        TextView tv_community_name, tv_community_people, tv_community_forum, tv_community_note;
    }
}
