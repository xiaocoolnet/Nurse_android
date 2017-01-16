package chinanurse.cn.nurse.Fragment_Nurse.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import chinanurse.cn.nurse.Fragment_Nurse.activity.ForumDetailsActivity;
import chinanurse.cn.nurse.Fragment_Nurse.constant.CommunityNetConstant;
import chinanurse.cn.nurse.Fragment_Nurse.net.NurseAsyncHttpClient;
import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.utils.LogUtils;
import cz.msebera.android.httpclient.Header;

public class ForumDetailsEditText implements PopupWindow.OnDismissListener, View.OnClickListener, View.OnFocusChangeListener {
    private static final int SENDCOMMENT = 1;
    private String TAG = "ForumDetailsEditText";
    private ForumDetailsActivity forumActivity;
    private UserBean user;
    private EditText pop_et_choice;
    private Button pop_bt_send_choice;
    private PopupWindow popupWindow;
    private View rootView;
    private String replayId, replayName, et_content;
    private TextView tv_choic;
    public static final int RESULT_OK = 2;
    private String type;
    private LinearLayout out_edittext;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
        }
    };

    public ForumDetailsEditText(ForumDetailsActivity mactivity) {
        this.forumActivity = mactivity;
        user = new UserBean(forumActivity);
        View view = LayoutInflater.from(forumActivity).inflate(R.layout.pop_edittext, null);
        pop_et_choice = (EditText) view.findViewById(R.id.pop_et_choice);
        pop_et_choice.setOnClickListener(this);


        pop_bt_send_choice = (Button) view.findViewById(R.id.pop_bt_send_choice);
        pop_bt_send_choice.setOnClickListener(this);
        pop_et_choice.setOnFocusChangeListener(this);
        if (getet() != null && getet().length() > 0 && !"".equals(getet())) {
            pop_et_choice.setFocusable(true);
            pop_et_choice.setFocusableInTouchMode(true);
            pop_et_choice.requestFocus();
        } else {
            pop_et_choice.setFocusable(false);
            pop_et_choice.setFocusableInTouchMode(false);
            pop_et_choice.clearFocus();
        }
        //获取activity根视图,rootView设为全局变量
        rootView = forumActivity.getWindow().getDecorView();
        tv_choic = (TextView) rootView.findViewById(R.id.et_forum_details_comment_submit);
        out_edittext = (LinearLayout) view.findViewById(R.id.out_edittext);
        out_edittext.setOnClickListener(this);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听

    }

    private String getet() {
        Log.i("getet3", "----------------->" + pop_et_choice.getText().toString());
        return pop_et_choice.getText().toString();
    }

    @Override
    public void onDismiss() {
        if (!"".equals(getet()) && getet() != null && !"".equals(getet())) {
            tv_choic.setText(pop_et_choice.getText().toString());
        } else {
            tv_choic.setText("");
        }
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
    public void showAsRelayComment(LinearLayout ll_comment_submit, String replayID, String replayType, String replayName) {
        this.replayId = replayID;
        this.type = replayType;
        this.replayName = replayName;
        if (getet() != null && getet().length() > 0 && !"".equals(getet())) {
            pop_et_choice.setFocusable(true);
            pop_et_choice.setFocusableInTouchMode(true);
            pop_et_choice.requestFocus();
        } else {
            pop_et_choice.setFocusable(false);
            pop_et_choice.setFocusableInTouchMode(false);
            pop_et_choice.clearFocus();
            if (type.equals("2")) {
                pop_et_choice.setHint("写评论");
            } else if (type.equals("3")) {
                pop_et_choice.setHint("回复" + replayName);
            }
        }
        int[] location = new int[2];
        ll_comment_submit.getLocationOnScreen(location);
        WindowManager manager = (WindowManager) forumActivity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int screenHeight = display.getHeight();
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(ll_comment_submit, Gravity.BOTTOM, 0, screenHeight - location[1]);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    private void replay(String comment) {
//        入参(post)：userid,id，content,type:,1、新闻2、圈子,3评论,photo
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", user.getUserid());
        requestParams.put("id", replayId);
        requestParams.put("content", comment);
        requestParams.put("type", type);

        NurseAsyncHttpClient.post(CommunityNetConstant.SET_COMMENT, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            LogUtils.e(TAG, "--replay->" + response);
////                    {
//                    "status":"success",
//                            "data":{
//                        "score":"2",
//                                "event":"回帖",
//                                "create_time":1483976183,
//                                "userid":"17026"
//                    }
//                }
                            try {
                                String status = response.getString("status");
                                pop_bt_send_choice.setClickable(true);
                                if (status.equals("success")) {
                                    dissmiss();
                                    tv_choic.setText("");
                                    pop_et_choice.setText("");
                                    forumActivity.getForumCommentsCount();
                                    forumActivity.getForumComments();
                                    JSONObject json = response.getJSONObject("data");
                                    if (json.getString("score") != null && json.getString("score").length() > 0) {
                                        View layout = LayoutInflater.from(forumActivity).inflate(R.layout.dialog_score, null);
                                        final Dialog dialog = new AlertDialog.Builder(forumActivity).create();
                                        dialog.show();
                                        dialog.getWindow().setContentView(layout);
                                        dialog.getWindow().clearFlags(
                                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                                        TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                        tv_score.setText("+" + json.getString("score"));
                                        TextView tv_score_name = (TextView) layout.findViewById(R.id.dialog_score_text);
                                        tv_score_name.setText(json.getString("event"));
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(3000);
                                                    dialog.dismiss();
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }

        );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_bt_send_choice:
                pop_bt_send_choice.setClickable(false);
                if ("".equals(getet()) || getet() == null) {
                    Toast.makeText(forumActivity, "评论内容不能为空!", Toast.LENGTH_SHORT).show();
                    pop_bt_send_choice.setClickable(true);
                } else {
                    if (HttpConnect.isConnnected(forumActivity)) {
                        replay(getet());
                    } else {
                        Toast.makeText(forumActivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                        pop_bt_send_choice.setClickable(true);
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
//                popupInputMethodWindow();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            pop_bt_send_choice.setSelected(true);
            pop_et_choice.setHint("");
            popupInputMethodWindow();
        } else {
            if (getet() != null && !"".equals(getet()) && getet().length() > 0) {
                pop_bt_send_choice.setSelected(true);
            } else {
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
