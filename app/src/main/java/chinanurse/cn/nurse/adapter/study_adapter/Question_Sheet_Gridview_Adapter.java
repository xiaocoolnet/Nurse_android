package chinanurse.cn.nurse.adapter.study_adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.Question_hashmap_data;
import chinanurse.cn.nurse.new_Activity.Question_Activity;
import chinanurse.cn.nurse.popWindow.Pop_Answer_Sheet;

/**
 * Created by Administrator on 2016/7/2.
 */
public class Question_Sheet_Gridview_Adapter extends BaseAdapter {
    private Question_Activity mActivity;
    private ArrayList<HashMap<String, Object>> arrayList;
    private int allPage;
    private int currentNumber;
    private Map<Integer, String> map;
    public static int mposition = -1;
    View pop_position;
    int pop_barnumber;
    String questionType;
    String questionNum;

    public Question_Sheet_Gridview_Adapter(Question_Activity mActivity, int currentNumber, int allPage, ArrayList<HashMap<String, Object>> arrayList, String questionType, String questionNum) {
        this.mActivity = mActivity;
        this.arrayList = arrayList;
        this.map = Question_hashmap_data.sheetmap;
        this.allPage = allPage;
        this.currentNumber = currentNumber;

        this.questionType = questionType;
        this.questionNum = questionNum;
    }

    @Override
    public int getCount() {
        return allPage;
    }

    @Override
    public Object getItem(int positon) {
        return arrayList.get(positon);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
//        mposition = position;
        final HolderView holderview;
        LayoutInflater inflater = null;
//        if (convertView == null) {
            inflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.question_answer_sheet_adapter, null);
            holderview = new HolderView();
            holderview.answer_Num_title = (TextView) convertView.findViewById(R.id.answer_Num_title);
            holderview.answer_type = (TextView) convertView.findViewById(R.id.answer_type);
            holderview.line_answer_sheet = (LinearLayout) convertView.findViewById(R.id.line_answer_sheet);
            convertView.setTag(holderview);
//        } else {
//            holderview = (HolderView) convertView.getTag();
//        }
        holderview.answer_Num_title.setText(position + 1 + "");
        if (Question_hashmap_data.hashmap_question != null && Question_hashmap_data.hashmap_question.size() > 0) {
            holderview.currentpage = Integer.valueOf(arrayList.get(0).get("currentNumber").toString());
            holderview.type_answer = arrayList.get(0).get("answer_type").toString();
        }
        if (position < map.size()) {
            if ("0".equals(map.get(position + 1))) {
                holderview.answer_type.setBackgroundResource(R.drawable.question_sheet_dot_red);
            } else if ("1".equals(map.get(position + 1))) {
                holderview.answer_type.setBackgroundResource(R.drawable.question_sheet_dot_green);
            }
        }

        if (position + 1 == currentNumber) {
            holderview.answer_Num_title.setBackgroundResource(R.drawable.question_sheet_dot_violet_big);
        } else {
            holderview.answer_Num_title.setBackgroundResource(R.color.white);
        }
        Log.i("当前页面", "------------>" + currentNumber);
//        for (int i = 0;i < Integer.valueOf(arrayList.get(position).get("allQuestionNum").toString());i++){
//            if (holderview.currentpage == i){
//                holderview.answer_Num_title.setBackgroundResource(R.drawable.question_sheet_dot_violet_big);
//            }else{
//                holderview.answer_Num_title.setBackgroundResource(R.drawable.question_sheet_dot_silver);
//            }
//        }
        //宝宝写的红绿判断方法
        if(parent.getChildCount()==allPage){
            for(int i =0;i<map.size();i++){
                if("0".equals(map.get(i))){
                    parent.getChildAt(i).findViewById(R.id.answer_type).setBackgroundResource(R.drawable.question_sheet_dot_red);
                }else{
                    parent.getChildAt(i).findViewById(R.id.answer_type).setBackgroundResource(R.drawable.question_sheet_dot_green);
                }
            }
        }

        holderview.line_answer_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mposition = position;
                Log.e("mposition---1", mposition + "");
//                Intent intent = new Intent(mActivity, Question_Activity.class);
//                intent.putExtra("questionNUM", questionNum);
//                intent.putExtra("questioncount", questionType);
//                intent.putExtra("mposition", mposition);
//                mActivity.startActivity(intent);

//                Pop_Answer_Sheet pop_answer_sheet = new Pop_Answer_Sheet(mActivity);
//                pop_answer_sheet.get_Position(mposition);
//                pop_answer_sheet.dissmiss();
                if(position<=map.size()){
                    mActivity.setCurrentView(position);
                    for(int i=0;i<parent.getChildCount();i++){
                        parent.getChildAt(i).findViewById(R.id.answer_Num_title).setBackgroundResource(R.color.white);
                    }
                    holderview.answer_Num_title.setBackgroundResource(R.drawable.question_sheet_dot_violet_big);

                }else{
                    Toast.makeText(mActivity,"别着急，慢慢来", Toast.LENGTH_SHORT).show();

                }
            }
        });


        return convertView;
    }


    public void send(View pop_position, int pop_barnumber) {

        this.pop_position = pop_position;
        this.pop_barnumber = pop_barnumber;

    }

    public class HolderView {

        private TextView answer_Num_title, answer_type;
        private String type_answer;
        private int currentpage;
        private LinearLayout line_answer_sheet;

    }

}
