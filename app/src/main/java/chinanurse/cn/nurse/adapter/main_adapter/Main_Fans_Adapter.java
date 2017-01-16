package chinanurse.cn.nurse.adapter.main_adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.mine_main_bean.Mine_fans;
import chinanurse.cn.nurse.picture.RoudImage;
import chinanurse.cn.nurse.ui.HeadPicture;

/**
 * Created by Administrator on 2016/7/15.
 */
public class Main_Fans_Adapter extends BaseAdapter{
    private int typeid;
    private Activity mactivity;
    private List<Mine_fans.DataBean> minefanslist;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public Main_Fans_Adapter(Activity mactivity, List<Mine_fans.DataBean> minefanslist, int typeid) {
        this.mactivity = mactivity;
        this.minefanslist = minefanslist;
        this.typeid = typeid;
    }

    @Override
    public int getCount() {
        return minefanslist.size();
    }

    @Override
    public Object getItem(int position) {
        return minefanslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;
        MainFans mainfnas = null;
        if (convertView == null){
            switch (typeid){
                case 0:
                    inflater = (LayoutInflater) mactivity.getSystemService(mactivity.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.listview_fansfragment, null);
                    mainfnas = new MainFans();
                    mainfnas.img_head = (RoudImage) convertView.findViewById(R.id.img_head);
                    mainfnas.name = (TextView) convertView.findViewById(R.id.name);
                    mainfnas.level = (TextView) convertView.findViewById(R.id.level);
                    // 显示图片的配置
                    options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_head_nor).showImageOnFail(R.mipmap.img_head_nor).cacheInMemory(true).cacheOnDisc(true).build();
                    break;
            }
            convertView.setTag(mainfnas);
        }else{
            mainfnas = (MainFans) convertView.getTag();
        }
        switch (typeid){
            case 0:
//                mainfnas.img_head.setImageBitmap();
                //头部图片
                if (minefanslist.get(position).getPhoto() != null&&minefanslist.get(position).getPhoto().length() > 0) {
                    imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + minefanslist.get(position).getPhoto(), mainfnas.img_head, options);
                } else {
                    new HeadPicture().getHeadPicture(mainfnas.img_head);
                }
                mainfnas.name.setText(minefanslist.get(position).getName());
                mainfnas.level.setText(minefanslist.get(position).getLevel());
                break;
        }
        return convertView;
    }
    class MainFans{
        private RoudImage img_head;
        private TextView name,level;
    }
}
