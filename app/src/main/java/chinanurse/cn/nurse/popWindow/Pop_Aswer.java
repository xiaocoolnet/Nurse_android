package chinanurse.cn.nurse.popWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RatingBar;
import android.widget.TextView;

import chinanurse.cn.nurse.R;

/**
 * Created by Administrator on 2016/6/30.
 */
public class Pop_Aswer implements OnDismissListener{
    private Activity mActivity;
    private PopupWindow popupWindow;
    //评分星星
    private RatingBar firstBar;
    private RatingBar secondBar;
    private TextView analysis_text,mine_answer,corrent_answer;
    public Pop_Aswer(Activity mActivity){
        this.mActivity = mActivity;
        View view = LayoutInflater.from(mActivity).inflate(R.layout.pop_answer,null);
        firstBar = (RatingBar) view.findViewById(R.id.room_ratingbar);
        secondBar = (RatingBar) view.findViewById(R.id.room_second_ratingbar);
        analysis_text = (TextView) view.findViewById(R.id.question_analysis_text);
        mine_answer = (TextView) view.findViewById(R.id.main_answer);
        corrent_answer = (TextView) view.findViewById(R.id.corrent_answer);
        popupWindow=new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
    }

    @Override
    public void onDismiss() {

    }
    /**弹窗显示的位置*/
    public void showAsDropDown(View parent,int barnumber,String correct_answer,String answer_mine,String answer_description,int answer_difficulty){
        if (!"".equals(correct_answer)){
            corrent_answer.setText(correct_answer);
        }else{
            corrent_answer.setText("");
        }
        if (!"".equals(answer_mine)){
            mine_answer.setText(answer_mine);
        }else{
            mine_answer.setText("");
        }
        if (!"".equals(answer_description)){
            analysis_text.setText(answer_description);
        }else{
            analysis_text.setText("");
        }
//        popupWindow.showAtLocation(parent,0,0);
        if(answer_difficulty<=3){
            firstBar.setRating(answer_difficulty);
        }else {
            firstBar.setRating(3);
            secondBar.setRating(answer_difficulty-3);
        }
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

    /**消除弹窗*/
    public void dissmiss(){
        popupWindow.dismiss();
    }
}
