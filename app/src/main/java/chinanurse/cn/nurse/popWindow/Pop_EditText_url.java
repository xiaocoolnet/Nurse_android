package chinanurse.cn.nurse.popWindow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WebView.News_WebView;
import chinanurse.cn.nurse.WebView.News_WebView_url;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/7/22.
 */
public class Pop_EditText_url implements PopupWindow.OnDismissListener, View.OnClickListener,View.OnFocusChangeListener{
    private static final int SENDCOMMENT = 1;
    private News_WebView_url mactivity;
    private UserBean user;
    private EditText pop_et_choice;
    private Button pop_bt_send_choice;
    private PopupWindow popupWindow;
    private View rootView;
    private String objectid;
    private TextView tv_choic;
    public static final int RESULT_OK = 2;
    private String type;
    private LinearLayout out_edittext;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SENDCOMMENT:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))){
                                JSONObject jsondata = new JSONObject(json.getString("data"));
//                                "score": "2","event": "评论","create_time": 1473411782,"userid": "600"
                                dissmiss();
                                tv_choic.setText("");
                                pop_et_choice.setText("");
                                Toast.makeText(mactivity,"发表成功",Toast.LENGTH_SHORT).show();
                                if (jsondata.getString("score") != null &&jsondata.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    final Dialog dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("+" + jsondata.getString("score"));
                                    TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                    tv_score_name.setText(jsondata.getString("event"));
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(1000);
                                                dialog.dismiss();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }

                                mactivity.collect();
                            }else{
                                Toast.makeText(mactivity,"发表失败",Toast.LENGTH_SHORT).show();
                                dissmiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                break;
            }
        }
    };

    public Pop_EditText_url(News_WebView_url mactivity) {
        this.mactivity = mactivity;
        user = new UserBean(mactivity);
        View view = LayoutInflater.from(mactivity).inflate(R.layout.pop_edittext, null);
        pop_et_choice = (EditText) view.findViewById(R.id.pop_et_choice);
        pop_et_choice.setOnClickListener(this);
        pop_et_choice.setOnFocusChangeListener(this);

        pop_bt_send_choice = (Button) view.findViewById(R.id.pop_bt_send_choice);
        pop_bt_send_choice.setOnClickListener(this);
        out_edittext = (LinearLayout) view.findViewById(R.id.out_edittext);
        out_edittext.setOnClickListener(this);
        if (getet() != null&&getet().length() > 0&&!"".equals(getet())){
            pop_et_choice.setFocusable(true);
            pop_et_choice.setFocusableInTouchMode(true);
            pop_et_choice.requestFocus();
        }else{
            pop_et_choice.setFocusable(false);
            pop_et_choice.setFocusableInTouchMode(false);
            pop_et_choice.clearFocus();
        }
        //获取activity根视图,rootView设为全局变量
        rootView = mactivity.getWindow().getDecorView();
        tv_choic = (TextView) rootView.findViewById(R.id.et_iamge_choice);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
    }
    @Override
    public void onDismiss() {
        if (!"".equals(getet())&&getet()!=null){
            tv_choic.setText(getet());
        }else{
            tv_choic.setText("");
        }
    }
    private String getet(){
        return pop_et_choice.getText().toString();
    }
    /**
     * 消除弹窗
     */
    public void dissmiss() {
        popupWindow.dismiss();

    }

    /**
     * 弹窗显示的位置
     */
    public void showAsDropDown(View parent, String objectid,String type) {
        this.objectid = objectid;
        this.type = type;
        if (getet() != null&&getet().length() > 0&&!"".equals(getet())){
            pop_et_choice.setFocusable(true);
            pop_et_choice.setFocusableInTouchMode(true);
            pop_et_choice.requestFocus();
        }else{
            pop_et_choice.setFocusable(false);
            pop_et_choice.setFocusableInTouchMode(false);
            pop_et_choice.clearFocus();
        }
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        WindowManager manager = (WindowManager) mactivity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int screenHeight = display.getHeight();
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, screenHeight - location[1]);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_bt_send_choice:
                pop_bt_send_choice.setSelected(true);
                if (type != null&&type.length() > 0){
                    if ("1".equals(type)){
                        if ("".equals(getet())||getet()  == null){
                            Toast.makeText(mactivity,"评论内容不能为空!",Toast.LENGTH_SHORT).show();
                        }else {
                            if (HttpConnect.isConnnected(mactivity)) {
                                new StudyRequest(mactivity, handler).ADDCOMMENT(user.getUserid(), objectid, getet(), "1", "", SENDCOMMENT);
                            } else {
                                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        if ("".equals(getet())||getet()  == null){
                            Toast.makeText(mactivity,"评论内容不能为空!",Toast.LENGTH_SHORT).show();
                        }else {
                            if (HttpConnect.isConnnected(mactivity)) {
                                new StudyRequest(mactivity, handler).ADDCOMMENT(user.getUserid(), objectid, getet(), "3", "", SENDCOMMENT);
                            } else {
                                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;
            case R.id.out_edittext:
                pop_bt_send_choice.setSelected(false);
                pop_et_choice.setFocusable(false);
                pop_et_choice.setFocusableInTouchMode(false);
                popupInputMethodWindowhide();
                break;
            case R.id.pop_et_choice:
                pop_et_choice.setFocusable(true);
                pop_et_choice.setFocusableInTouchMode(true);
                pop_et_choice.requestFocus();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
         if (hasFocus){
             pop_bt_send_choice.setSelected(true);
             popupInputMethodWindow();

         }else{
             if (getet()!= null&&!"".equals(getet())&&getet().length() > 0){
                 pop_bt_send_choice.setSelected(true);
             }else{
                 pop_bt_send_choice.setSelected(false);
             }
             pop_et_choice.setHint("写评论");
             popupInputMethodWindowhide();
         }
    }
    /**
     * 弹出输入法窗口
     */
    private void popupInputMethodWindow() {

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) pop_bt_send_choice.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 400);
    }
    /**
     * 弹出输入法窗口
     */
    private void popupInputMethodWindowhide() {

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) pop_bt_send_choice.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(pop_bt_send_choice.getWindowToken(), 0);
            }
        }, 400);
    }
}
