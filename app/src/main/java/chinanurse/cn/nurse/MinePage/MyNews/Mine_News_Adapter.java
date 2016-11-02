package chinanurse.cn.nurse.MinePage.MyNews;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.FirstPageNews;
import chinanurse.cn.nurse.bean.News_bean;
import chinanurse.cn.nurse.bean.News_list_type;
import chinanurse.cn.nurse.imageload.MyApplication;
import chinanurse.cn.nurse.pnlllist.SwipeItemLayout;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class Mine_News_Adapter extends BaseAdapter {
    private Activity activity;
    private List<News_list_type.DataBean> list;
    private List<String> listone;
    private Handler handler;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.ic_lan).showImageOnFail(R.mipmap.ic_lan).cacheInMemory(true).cacheOnDisc(true).build();

    public Mine_News_Adapter(Activity activity, List<News_list_type.DataBean> list,List<String>  listone, Handler handler) {
        this.activity = activity;
        this.list = list;
        this.listone = listone;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null) {
            View view01 = LayoutInflater.from(activity).inflate(R.layout.item_mine_news, null);
            View view02 = LayoutInflater.from(activity).inflate(R.layout.item_delet, null);
            convertView = new SwipeItemLayout(view01, view02, null, null);
            vh = new ViewHolder();
            vh.news_delet = (TextView) view02.findViewById(R.id.news_delet);
            vh.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            vh.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            vh.dot_red = (ImageView) convertView.findViewById(R.id.dot_red);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
//        for (int j = 0;j < list.size();j++){
//            if (j == position){
                String listread = list.get(position).getMessage_id();
                for (int i = 0;i < listone.size();i++){
                    String listnot = listone.get(i);
                    if (listnot.equals(listread)){
                        vh.dot_red.setVisibility(View.GONE);
                        break;
                    }else{
                        vh.dot_red.setVisibility(View.VISIBLE);
                    }
//                }
//            }
        }
        if (list.get(position).getThumb() != null &&list.get(position).getThumb().size() > 0){
            imageLoader.displayImage(NetBaseConstant.NET_PIC_PREFIX_THUMB+"/"+list.get(position).getThumb().get(0).getUrl().toString(),vh.iv_image,options);
        }else{
            vh.iv_image.setBackground(activity.getResources().getDrawable(R.mipmap.ic_lan));
        }
        vh.tv_title.setText(list.get(position).getPost_title()+"");
        vh.tv_content.setText(list.get(position).getPost_date().substring(0,10)+"");
        vh.news_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0;i < list.size();i++) {
                    if (i == position) {
                        Message msg = Message.obtain();
                        msg.what = 5;
                        msg.obj = position;
                        handler.sendMessage(msg);
                    }
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView tv_title, tv_content,tv_time,news_delet;
        private RelativeLayout rela_mine_news;
        private ImageView iv_image,dot_red;

    }
}
