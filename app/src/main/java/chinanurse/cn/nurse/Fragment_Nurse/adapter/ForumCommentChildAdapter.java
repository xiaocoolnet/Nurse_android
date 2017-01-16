package chinanurse.cn.nurse.Fragment_Nurse.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import chinanurse.cn.nurse.Fragment_Nurse.bean.CommentChildBean;
import chinanurse.cn.nurse.R;


public class ForumCommentChildAdapter extends BaseAdapter{
    private ArrayList<CommentChildBean> commentChildBeanArrayList;
    private Context content;


    public ForumCommentChildAdapter(ArrayList<CommentChildBean> commentChildBeanArrayList, Context mContext) {
        this.commentChildBeanArrayList=commentChildBeanArrayList;
        this.content=mContext;
    }

    @Override
    public int getCount() {
        return commentChildBeanArrayList.size();
    }

    @Override
    public CommentChildBean getItem(int position) {
        return commentChildBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler holder = null;
        if (convertView == null){
            convertView = View.inflate(content, R.layout.item_forum_comment_child, null);
            holder = new ViewHodler();
            holder.tv_child_content = (TextView) convertView.findViewById(R.id.tv_child_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHodler) convertView.getTag();
        }
        try {
            String text = String.format("%1$s" + ":\n" + commentChildBeanArrayList.get(position).getContent().toString() + "", commentChildBeanArrayList.get(position).getUserName() + "");
            ;
            int index[] = new int[1];
            index[0] = text.indexOf(commentChildBeanArrayList.get(position).getUserName() + "");
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.setSpan(new ForegroundColorSpan(content.getResources().getColor(R.color.purple)), index[0], index[0] + commentChildBeanArrayList.get(position).getUserName().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tv_child_content.setText(style);
        }catch (Exception e){

        }
        return convertView;
    }
    class ViewHodler {
        private TextView tv_child_content;
    }
}
