package chinanurse.cn.nurse.Fragment_Study.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import chinanurse.cn.nurse.R;

/**
 * Created by Administrator on 2016/11/7 0007.
 */

public class Goman_topic_Second_Adapetr extends BaseAdapter{
    private List<String> listname;
    private Activity mactivity;
    private HolderView commonHolder;

    public Goman_topic_Second_Adapetr(List<String> listname, Activity mactivity) {
        this.listname = listname;
        this.mactivity = mactivity;
    }

    @Override
    public int getCount() {
        return listname.size();
    }

    @Override
    public Object getItem(int position) {
        return listname.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(mactivity, R.layout.goman_tipic_adapter, null);
            commonHolder = new HolderView();
            commonHolder.tv_listname = (TextView) convertView.findViewById(R.id.tv_listname);
            convertView.setTag(commonHolder);
        }else{
            commonHolder = (HolderView) convertView.getTag();
        }
        commonHolder.tv_listname.setText(listname.get(position));
        return convertView;
    }
    class HolderView {
       private TextView tv_listname;
    }
}
