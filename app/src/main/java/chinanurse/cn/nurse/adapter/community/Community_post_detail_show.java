package chinanurse.cn.nurse.adapter.community;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.Nuser_community;

/**
 * 论坛评论
 * Created by Administrator on 2016/7/15.
 */
public class Community_post_detail_show extends BaseAdapter {
    private Activity mactivity;
//    private List<Nuser_community.DataEntity> nurseList;

    private List<String> nurseList;

    public Community_post_detail_show(Activity mactivity, List<String> nurseList) {
        this.mactivity = mactivity;
        this.nurseList = nurseList;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return nurseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = null;
//        ViewHolderDetail detail = null;
//        if (convertView == null) {
//            inflater = (LayoutInflater) mactivity.getSystemService(mactivity.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.post_detail_show_adapter, parent, false);
//            detail = new ViewHolderDetail();
//            detail.image_head = (ImageView) convertView.findViewById(R.id.myinof_image_head_show);
//            detail.post_username = (TextView) convertView.findViewById(R.id.show_detail_post_username_text);
//            detail.post_fooler = (TextView) convertView.findViewById(R.id.show_detail_post_floor_text);
//            detail.post_position = (TextView) convertView.findViewById(R.id.show_detail_post_position_text);
//            detail.post_level = (TextView) convertView.findViewById(R.id.show_detail_post_level_text);
//            detail.post_content = (TextView) convertView.findViewById(R.id.show_detail_post_content_text);
//            detail.post_time = (TextView) convertView.findViewById(R.id.show_detail_time_text);
//            detail.post_location = (TextView) convertView.findViewById(R.id.show_detail_specific_location_text);
//            detail.post_reply = (TextView) convertView.findViewById(R.id.show_detail_specific_reply_text);
//            convertView.setTag(detail);
//        } else {
//            detail = (ViewHolderDetail) convertView.getTag();
//        }
////        detail.post_username
////        detail.post_fooler
////        detail.post_position
////        detail.post_level
////        detail.post_content
////        detail.post_time
////        detail.post_location
//        detail.post_reply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mactivity, "此功能还未开放", Toast.LENGTH_SHORT).show();
//            }
//        });


        convertView=View.inflate(mactivity,R.layout.post_detail_show_adapter,null);

        return convertView;
    }

    class ViewHolderDetail {
        private ImageView image_head;//头像
        //发表评论用户名,发表的评论所处的位置,所在职位,用户等级,发布内容,发布时间,发布地点,回复楼主
        private TextView post_username, post_fooler, post_position, post_level, post_content, post_time, post_location, post_reply;

    }
}
