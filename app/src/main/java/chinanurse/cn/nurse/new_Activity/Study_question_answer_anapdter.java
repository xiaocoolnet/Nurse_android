package chinanurse.cn.nurse.new_Activity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import chinanurse.cn.nurse.R;

/**
 * Created by Administrator on 2016/7/2.
 */
public class Study_question_answer_anapdter extends BaseAdapter{
    private Activity mActivity;
    private List<Answer_option_Bean.DataEntity> listbean;
    private int typeid=0;
    private int currentNum;
    private static CommonViewHolder commonHolder = null;
    private String[] option = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    public Study_question_answer_anapdter(Activity mActivity, List<Answer_option_Bean.DataEntity> listbean, int currentNum) {
        this.mActivity = mActivity;
        this.listbean = listbean;
        this.currentNum = currentNum;
    }

    @Override
    public int getCount() {
        return listbean.get(currentNum).getAnswerlist().size();
    }

    @Override
    public Object getItem(int position) {
        return listbean.get(currentNum).getAnswerlist().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = null;
        if (convertView == null) {
            switch (typeid) {
                case 0:
                    inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.question_lv_adapter, null);
                    commonHolder = new CommonViewHolder();
                    commonHolder.tv_answer_list = (TextView) convertView.findViewById(R.id.tv_answer_list);
                    commonHolder.ril_choose_option = (LinearLayout) convertView.findViewById(R.id.ril_choose_option);
                    commonHolder.question_option = (TextView) convertView.findViewById(R.id.question_option);
                    break;
            }
            convertView.setTag(commonHolder);
        } else {
            commonHolder = (CommonViewHolder) convertView.getTag();
        }
        switch (typeid) {
            case 0:
                commonHolder.tv_answer_list.setText("、"+listbean.get(currentNum).getAnswerlist().get(position).getAnswer_title().toString() + "");
                commonHolder.question_option.setText( option[position]);

                break;

        }
        return convertView;
    }
    public static class CommonViewHolder {
        private TextView tv_answer_list;
        private LinearLayout ril_choose_option;
        private TextView question_option;//ABCD

    }
}
