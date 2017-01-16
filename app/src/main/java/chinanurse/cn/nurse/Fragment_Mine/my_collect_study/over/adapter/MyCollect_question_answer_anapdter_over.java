package chinanurse.cn.nurse.Fragment_Mine.my_collect_study.over.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import chinanurse.cn.nurse.MinePage.MyStudy.MineStudt_question_over_Bean;
import chinanurse.cn.nurse.R;

/**
 *
 * Created by Administrator on 2016/7/2.
 */
public class MyCollect_question_answer_anapdter_over extends BaseAdapter{
    private Activity mActivity;
    private List<MineStudt_question_over_Bean.DataBean.QuestionBean> listbean;
    private int typeid=0;
    private int currentNum;
    private static CommonViewHolder commonHolder = null;
    private String[] option = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    public MyCollect_question_answer_anapdter_over(Activity mActivity, List<MineStudt_question_over_Bean.DataBean.QuestionBean> listbean, int currentNum) {
        this.mActivity = mActivity;
        this.listbean = listbean;
        this.currentNum = currentNum;
    }

    @Override
    public int getCount() {
        return listbean.get(currentNum).getAnswers().size();
    }

    @Override
    public Object getItem(int position) {
        return listbean.get(currentNum).getAnswers().get(position);
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
                commonHolder.tv_answer_list.setText("、"+listbean.get(currentNum).getAnswers().get(position).getTitle().toString() + "");
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
