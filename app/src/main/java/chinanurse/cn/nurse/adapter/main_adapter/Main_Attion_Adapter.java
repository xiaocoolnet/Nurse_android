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
import chinanurse.cn.nurse.bean.mine_main_bean.Mine_attion;
import chinanurse.cn.nurse.picture.RoudImage;
import chinanurse.cn.nurse.ui.HeadPicture;

/**
 * Created by Administrator on 2016/7/15.
 */
public class Main_Attion_Adapter extends BaseAdapter {
    private List<Mine_attion.DataEntity> attionlist;
    private Activity mactivity;
    private int typeid;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public Main_Attion_Adapter(Activity mactivity, List<Mine_attion.DataEntity> attionlist, int typeid) {
        this.attionlist = attionlist;
        this.mactivity = mactivity;
        this.typeid = typeid;
    }

    @Override
    public int getCount() {
        return attionlist.size();
    }

    @Override
    public Object getItem(int position) {
        return attionlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;
        AttionActivity attionactivity = null;
        if (convertView == null) {
            inflater = (LayoutInflater) mactivity.getSystemService(mactivity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_fansfragment, null);
            attionactivity = new AttionActivity();
            attionactivity.img_head = (RoudImage) convertView.findViewById(R.id.img_head);
            attionactivity.name = (TextView) convertView.findViewById(R.id.name);
            attionactivity.level = (TextView) convertView.findViewById(R.id.level);
            // 显示图片的配置
            options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.img_head_nor).showImageOnFail(R.mipmap.img_head_nor).cacheInMemory(true).cacheOnDisc(true).build();

            convertView.setTag(attionactivity);
        } else {
            attionactivity = (AttionActivity) convertView.getTag();
        }
        //头部图片
        if (attionlist.get(position).getPhoto() != null&&attionlist.get(position).getPhoto().length() > 0) {
            imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + attionlist.get(position).getPhoto(), attionactivity.img_head, options);
        } else {
            new HeadPicture().getHeadPicture(attionactivity.img_head);
        }
        attionactivity.name.setText(attionlist.get(position).getName());
        attionactivity.level.setText(attionlist.get(position).getLevel());
        return convertView;
    }

    class AttionActivity {
        private RoudImage img_head;
        private TextView name,level;

    }
}
