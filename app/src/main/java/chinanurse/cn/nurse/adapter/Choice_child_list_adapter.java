package chinanurse.cn.nurse.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.webview_comments_bean.Webview_comments_bean;
import chinanurse.cn.nurse.picture.RoudImage;

/**
 * Created by Administrator on 2016/9/30 0030.
 */
public class Choice_child_list_adapter extends BaseAdapter{
    private List<Webview_comments_bean.DataBean.ChildCommentsBean> choicelist;
    private Context contect;

    public Choice_child_list_adapter(List<Webview_comments_bean.DataBean.ChildCommentsBean> choicelist, Context contect) {
        this.choicelist = choicelist;
        this.contect = contect;
    }

    @Override
    public int getCount() {
        return choicelist.size();
    }

    @Override
    public Object getItem(int position) {
        return choicelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler holder = null;
        if (convertView == null){
            convertView = View.inflate(contect, R.layout.chpice_child_list, null);
            holder = new ViewHodler();
            holder.child_choice = (TextView) convertView.findViewById(R.id.child_choice);
            convertView.setTag(holder);
        }else{
            holder = (ViewHodler) convertView.getTag();
        }
        try {
            String text = String.format("%1$s" + ":\n" + choicelist.get(position).getContent().toString() + "", choicelist.get(position).getUsername() + "");
            ;
            int index[] = new int[1];
            index[0] = text.indexOf(choicelist.get(position).getUsername() + "");
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.setSpan(new ForegroundColorSpan(contect.getResources().getColor(R.color.purple)), index[0], index[0] + choicelist.get(position).getUsername().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.child_choice.setText(style);
        }catch (Exception e){

        }
        return convertView;
    }
    class ViewHodler {
        private TextView child_choice;
    }
}
