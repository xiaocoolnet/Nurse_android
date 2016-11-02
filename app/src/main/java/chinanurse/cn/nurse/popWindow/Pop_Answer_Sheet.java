package chinanurse.cn.nurse.popWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.adapter.study_adapter.Question_Sheet_Gridview_Adapter;
import chinanurse.cn.nurse.bean.Question_hashmap_data;
import chinanurse.cn.nurse.new_Activity.ExaminationSubmitAdapter;
import chinanurse.cn.nurse.new_Activity.Question_Activity;

/**
 * Created by Administrator on 2016/6/30.
 */
public class Pop_Answer_Sheet implements PopupWindow.OnDismissListener {
    private Activity mActivity;
    private PopupWindow popupWindow;
    private GridView gridview;
    private Question_Sheet_Gridview_Adapter gridviewadapter;
    private int currentNumber, allpageNum;
    private ArrayList<String> listnomal = new ArrayList<String>();
    int mposition;


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            gridviewadapter.notifyDataSetChanged();
        }
    };


    public Pop_Answer_Sheet(Activity mActivity) {
        this.mActivity = mActivity;
        View view = LayoutInflater.from(mActivity).inflate(R.layout.pop_answer_sheet, null);

        gridview = (GridView) view.findViewById(R.id.question_pop_gridview);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
    }

    @Override
    public void onDismiss() {

    }

    /**
     * 弹窗显示的位置
     */
    public void showAsDropDown(Question_Activity ac,View parent, int barnumber, int currentNumber, int allpageNum, String questionType, String questionNum) {
//        currentNumber = currentNumber;
//        allpageNum = allpageNum;
//            if (Question_hashmap_data.hashmap_question != null&&Question_hashmap_data.hashmap_question.size() > 0){
//                gridviewadapter = new Question_Sheet_Gridview_Adapter(mActivity, Question_hashmap_data.hashmap_question);
//                gridviewadapter = new Question_Sheet_Gridview_Adapter(mActivity,currentNumber,allpageNum, Question_hashmap_data.hashmap_question);
        gridviewadapter = new Question_Sheet_Gridview_Adapter(ac, currentNumber, allpageNum, Question_hashmap_data.hashmap_question, questionType, questionNum);
        gridviewadapter.send(parent, barnumber);
        handler.sendEmptyMessage(500);


        gridview.setAdapter(gridviewadapter);
        gridviewadapter.notifyDataSetChanged();
//            }
//        popupWindow.showAtLocation(parent,0,0);
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        WindowManager manager = (WindowManager) mActivity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int screenHeight = display.getHeight();
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, screenHeight - location[1]);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    /**
     * 消除弹窗
     */
    public void dissmiss() {
        popupWindow.dismiss();
    }


    public int get_Position(int position) {
        mposition = position;
        Log.e("mposition---2", mposition + "");
        mActivity.finish();
        ExaminationSubmitAdapter.getGradview_position(mposition);

        return mposition;

    }


}
