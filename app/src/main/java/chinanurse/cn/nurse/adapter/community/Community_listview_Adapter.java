package chinanurse.cn.nurse.adapter.community;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import chinanurse.cn.nurse.NurseFragment.Write_Community_Show;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.UrlPath.NetBaseConstant;
import chinanurse.cn.nurse.bean.Nuser_community;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Community_listview_Adapter extends BaseAdapter {
    private Activity mactivity;
    private List<Nuser_community.DataEntity> contentlist;
    private int typeid;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public Community_listview_Adapter(Activity mactivity, List<Nuser_community.DataEntity> contentlist, int typeid) {
        this.contentlist = contentlist;
        this.mactivity = mactivity;
        this.typeid = typeid;
        imageLoader.init(ImageLoaderConfiguration.createDefault(mactivity));

    }


    public void addList(List<Nuser_community.DataEntity> contentlist) {
        this.contentlist.removeAll(this.contentlist);
        this.contentlist.addAll(contentlist);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return contentlist.size();
    }

    @Override
    public Object getItem(int position) {
        return contentlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;
        ViewHolder viewholder = null;
        VieupHolder viewhupolder = null;


        switch (typeid) {
            case 0:
                if (convertView == null) {
                    inflater = (LayoutInflater) mactivity.getSystemService(mactivity.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.nurse_firstfrag_lv_item, null);
                    viewholder = new ViewHolder();
                    viewholder.lv_name = (TextView) convertView.findViewById(R.id.lv_name);
                    viewholder.lv_image = (ImageView) convertView.findViewById(R.id.lv_image);
                    viewholder.lv_time = (TextView) convertView.findViewById(R.id.lv_time);
                    viewholder.lv_request = (TextView) convertView.findViewById(R.id.lv_request);//标题
                    viewholder.lv_answ = (TextView) convertView.findViewById(R.id.lv_answ);//内容
                    viewholder.lv_text_like = (TextView) convertView.findViewById(R.id.lv_text_like);
                    viewholder.lv_ll = (TextView) convertView.findViewById(R.id.lv_ll);
                    viewholder.lv_all_layout = (LinearLayout) convertView.findViewById(R.id.lv_all_layout);
                    convertView.setTag(viewholder);
                } else {
                    viewholder = (ViewHolder) convertView.getTag();
                }
                break;
            case 1:
                if (convertView == null) {
                    inflater = (LayoutInflater) mactivity.getSystemService(mactivity.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.nurse_firseup_lv_item, null);
                    viewhupolder = new VieupHolder();
                    viewhupolder.firstiup_title = (TextView) convertView.findViewById(R.id.nurse_firstiup_title_text);
                    viewhupolder.firstiup_hot = (ImageView) convertView.findViewById(R.id.nurse_firstiup_hot);
                    viewhupolder.firstiup_image = (ImageView) convertView.findViewById(R.id.nurse_firstiup_image);
                    viewhupolder.firstiup_fine = (ImageView) convertView.findViewById(R.id.nurse_firstiup_fine);
                    convertView.setTag(viewhupolder);
                } else {
                    viewhupolder = (VieupHolder) convertView.getTag();
                }
                break;
        }

        switch (typeid) {
            case 0:
                viewholder.lv_name.setText(contentlist.get(position).getName() + "");
//                viewholder.lv_image.setText(contentlist.get(position).getPhoto() + "");

                Date date = new Date();
                date.setTime(Long.parseLong(contentlist.get(position).getWrite_time()) * 1000);
                viewholder.lv_time.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(date));
                if (!"".equals(contentlist.get(position).getTitle()) && contentlist != null && contentlist.get(position) != null) {
                    viewholder.lv_request.setText(contentlist.get(position).getTitle() + "");
                }

                viewholder.lv_answ.setText(contentlist.get(position).getContent() + "");
                viewholder.lv_text_like.setText(contentlist.get(position).getLike().size() + "");
                viewholder.lv_ll.setText(contentlist.get(position).getComment().size() + "");


                //图片路径 NetBaseConstant.NET_HOST

                Log.e("aaa", NetBaseConstant.NET_HOST + "/" + contentlist.get(position).getPhoto());
                imageLoader.displayImage(NetBaseConstant.NET_HOST + "/" + contentlist.get(position).getPhoto(), viewholder.lv_image);
                viewholder.lv_all_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getBundle(position, "goabroad", Write_Community_Show.class, "学习信息");
                    }
                });
                break;

            case 1:
                viewhupolder.firstiup_title.setText(contentlist.get(position).getTitle() + "");

//                viewholder.firstiup_hot
//                viewholder.firstiup_image
//                viewholder.firstiup_fine
                break;
        }

        return convertView;
    }

    public void getBundle(final int position, String key, Class clazz, String str) {

        if (contentlist != null && contentlist.size() != 0) {
            Nuser_community.DataEntity nurse_Data = contentlist.get(position);
            Log.e("position", "_____________" + position);
            Intent intent = new Intent(mactivity, clazz);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            Bundle bundle = new Bundle();
            bundle.putParcelable(key, nurse_Data);
            bundle.putString("pagertype", "1");
            intent.putExtras(bundle);
            mactivity.startActivity(intent);
        } else {
            Toast.makeText(mactivity, "数据加载中，稍候请重试！", Toast.LENGTH_SHORT).show();
        }


    }

    class ViewHolder {
        private ImageView lv_image;
        private TextView lv_name, lv_time, lv_request, lv_answ, lv_text_like, lv_ll;
        private LinearLayout lv_all_layout;
    }

    class VieupHolder {
        private ImageView firstiup_fine, firstiup_image, firstiup_hot;
        private TextView firstiup_title;
    }
}
