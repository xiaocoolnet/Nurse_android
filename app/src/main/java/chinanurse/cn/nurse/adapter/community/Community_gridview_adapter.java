package chinanurse.cn.nurse.adapter.community;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import chinanurse.cn.nurse.R;

/**
 * Created by Administrator on 2016/7/15.
 */
public class Community_gridview_adapter extends BaseAdapter {
    private Activity mactivity;
    private List<Drawable> bitmaplist;

    public Community_gridview_adapter(Activity mactivity, List<Drawable> bitmaplist) {
        this.bitmaplist = bitmaplist;
        this.mactivity = mactivity;
    }

    @Override
    public int getCount() {
        return bitmaplist.size();
    }

    @Override
    public Object getItem(int position) {
        return bitmaplist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;
        ViewHolder holder = null;
        if (convertView == null) {
            inflater = (LayoutInflater) mactivity.getSystemService(mactivity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.community_gridview_adapter, null);
            holder = new ViewHolder();
            holder.griview_image = (ImageView) convertView.findViewById(R.id.community_gridview_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.griview_image.setImageBitmap(bitmaplist.get(position));
        holder.griview_image.setImageDrawable(bitmaplist.get(position));

        return convertView;
    }

    class ViewHolder {
        private ImageView griview_image;
    }
}
