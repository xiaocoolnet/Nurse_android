package chinanurse.cn.nurse.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.Spinner_Bean;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class Spinner_Adapter extends BaseAdapter {

    private Activity activity;
    private List<Spinner_Bean.DataBean> list;


    public Spinner_Adapter(Activity activity, List<Spinner_Bean.DataBean> list) {
        this.activity = activity;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(activity, R.layout.item_spinner, null);
            vh = new ViewHolder(convertView);

            convertView.setTag(vh);


        } else {

            vh = (ViewHolder) convertView.getTag();
        }

        vh.item_spinner_name.setText(list.get(position).getName());

        return convertView;
    }

    class ViewHolder {
        private TextView item_spinner_num, item_spinner_name;

        public ViewHolder(View convertView) {
            item_spinner_name = (TextView) convertView.findViewById(R.id.item_spinner_name);

        }
    }

}
