package chinanurse.cn.nurse.Fragment_Nurse.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import chinanurse.cn.nurse.Fragment_Nurse.activity.PersonalHomePageActivity;
import chinanurse.cn.nurse.Fragment_Nurse.bean.ForumDataBean;
import chinanurse.cn.nurse.Fragment_Nurse.bean.MessageDataBean;
import chinanurse.cn.nurse.utils.LogUtils;
import chinanurse.cn.nurse.utils.TimeToolUtils;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.picture.RoudImage;

/**
 * Created by zhuchongkun on 2016/12/14.
 */

public class MessageAdapter extends BaseAdapter {
    private String TAG = "MessageAdapter";
    private Context context;
    private ArrayList<MessageDataBean> messageDataBeanArrayList;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_bg_nor).showImageOnFail(R.mipmap.img_bg_nor).cacheInMemory(true).cacheOnDisc(true).build();


    public MessageAdapter(Context mContext, ArrayList<MessageDataBean> messageDataBeanArrayList) {
        this.context = mContext;
        if (messageDataBeanArrayList == null) {
            messageDataBeanArrayList = new ArrayList<MessageDataBean>();
        }
        this.messageDataBeanArrayList = messageDataBeanArrayList;
    }

    @Override
    public int getCount() {
        return messageDataBeanArrayList.size();
    }

    @Override
    public MessageDataBean getItem(int position) {
        return messageDataBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final MessageDataBean messageDataBean = messageDataBeanArrayList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_message, null);
            viewHolder.iv_head = (RoudImage) convertView.findViewById(R.id.iv_item_message_head);
            viewHolder.iv_authentication = (ImageView) convertView.findViewById(R.id.iv_item_message_authentication);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_message_name);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_item_message_time);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_item_message_content);
            viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_item_message_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LogUtils.e(TAG, "MessageDataBean" + messageDataBean.toString());
        imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + messageDataBean.getUserPhoto(), viewHolder.iv_head, options);
        viewHolder.tv_type.setText(messageDataBean.getMessageCentent());
        viewHolder.tv_name.setText("用户:" + messageDataBean.getUserName());
        long time = Long.valueOf(messageDataBean.getMessageCreateTime());
        viewHolder.tv_time.setText(TimeToolUtils.fromateTimeShowByRule(time * 1000));
        viewHolder.tv_content.setText("帖子:" + messageDataBean.getForumTitle());
        viewHolder.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ForumDataBean forumData = new ForumDataBean();
                forumData.setUserId(messageDataBean.getFromUserId());
                forumData.setUserPhoto(messageDataBean.getUserPhoto());
                forumData.setUserLevel(messageDataBean.getUserLevel());
                Intent intentHomePage = new Intent();
                intentHomePage.setClass(context, PersonalHomePageActivity.class);
                intentHomePage.putExtra("forum", forumData);
                intentHomePage.putExtra("from", "MessageAdapter");
                context.startActivity(intentHomePage);
            }
        });

        if (messageDataBean.getMessagetType().equals("4") ||
                messageDataBean.getMessagetType().equals("8")){
            viewHolder.tv_name.setVisibility(View.GONE);
            viewHolder.tv_content.setVisibility(View.GONE);
        }

            return convertView;
    }

    class ViewHolder {
        RoudImage iv_head;
        ImageView iv_authentication;
        TextView tv_name, tv_time, tv_type, tv_content;
    }
}
