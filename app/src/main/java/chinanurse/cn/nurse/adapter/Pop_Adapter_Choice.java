package chinanurse.cn.nurse.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.WebView.webview_comments_bean.Webview_comments_bean;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.picture.RoudImage;

/**
 * Created by Administrator on 2016/9/9 0009.
 */
public class Pop_Adapter_Choice extends BaseAdapter {
    private  List<Webview_comments_bean.DataBean> commentslist;
    private Activity mactivity;
    private int type;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private List<Webview_comments_bean.DataBean.ChildCommentsBean> childcomment = new ArrayList<>();
    private Handler handler;
    private Choice_child_list_adapter choiceadapter;

    public Pop_Adapter_Choice( List<Webview_comments_bean.DataBean> commentslist, Activity mactivity, int type,Handler handler) {
        this.commentslist = commentslist;
        this.mactivity = mactivity;
        this.type = type;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return commentslist.size();
    }

    @Override
    public Object getItem(int position) {
        return commentslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler holder = null;
        if (convertView == null) {
            switch (type) {
                case 0:
                    convertView = View.inflate(mactivity, R.layout.hot_choice, null);
                    holder = new ViewHodler();
                    holder.choice_userimage = (RoudImage) convertView.findViewById(R.id.myinof_image_head_show);//用户头像
                    holder.choice_username = (TextView) convertView.findViewById(R.id.choice_username);//用户名称
                    holder.choice_float = (TextView) convertView.findViewById(R.id.choice_float);//楼
                    holder.choice_content = (TextView) convertView.findViewById(R.id.choice_content);//发布内容
                    holder.choice_time = (TextView) convertView.findViewById(R.id.choice_time);//发布时间
                    holder.choice_content_sun = (ListView) convertView.findViewById(R.id.choice_content_sun);//字评论列表
                    holder.choice_time_reply = (TextView) convertView.findViewById(R.id.choice_time_reply);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHodler) convertView.getTag();
        }
        switch (type) {
            case 0:
                try {
                    options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_head_nor).showImageOnFail(R.mipmap.img_head_nor).cacheInMemory(true).cacheOnDisc(true).build();
                    imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + commentslist.get(position).getPhoto().toString(), holder.choice_userimage, options);
                    holder.choice_username.setText(commentslist.get(position).getUsername().toString() + "");
                    if (commentslist != null && commentslist.size() > 0) {
                        holder.choice_float.setText(commentslist.size() - position + "楼");
                    }
                    holder.choice_content.setText(commentslist.get(position).getContent().toString() + "");

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    long time = Long.parseLong(commentslist.get(position).getAdd_time().toString());
                    String timeone = simpleDateFormat.format(new Date(time * 1000));
                    holder.choice_time.setText(timeone + "");
                    childcomment = commentslist.get(position).getChild_comments();
                    choiceadapter = new Choice_child_list_adapter(childcomment, mactivity);
                    holder.choice_content_sun.setAdapter(choiceadapter);
                    setListViewHeightBasedOnChildren(holder.choice_content_sun);
                    holder.choice_content_sun.setEnabled(false);
                    holder.choice_content_sun.setPressed(false);
                    holder.choice_content_sun.setClickable(false);
                    holder.choice_time_reply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Message msg = Message.obtain();
                            msg.what = 15;
                            msg.obj = position;
                            handler.sendMessage(msg);
                        }
                    });
                }catch (Exception e){

                }
                break;
        }
        return convertView;
    }

    /*
      解决scrollview下listview显示不全
    */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    class ViewHodler {
        private RoudImage choice_userimage;
        private TextView choice_username, choice_float, choice_content, choice_time,choice_time_reply;
        private ListView choice_content_sun;
        private ArrayAdapter<String> commentsadapter;
    }
}
