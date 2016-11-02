package chinanurse.cn.nurse.popWindow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.Fragment_Nurse_mine.Myinfo;

import chinanurse.cn.nurse.Other.SetListview;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.adapter.Spinner_Adapter;
import chinanurse.cn.nurse.bean.Spinner_Bean;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/7/22.
 */
public class Pop_spinner implements PopupWindow.OnDismissListener, View.OnClickListener {
    private static final int UPDATEMAJOR = 2;
    private static final int UPDATEEDUCATION = 3;
    private Myinfo mactivity;
    private PopupWindow popupWindow;
    private UserBean user;
    private TextView mine_pop_cancle, mine_pop_sure;
    private ListView lv_pop_spinner;
    private String titlename, et_content;
    private final int typeid_18 = 18;
    private final int typeid_1 = 1;
    private int listid = 0;
    private SharedPreferences sp;
    private List<Spinner_Bean.DataBean> spinnerlist;
    private Spinner_Adapter spinner_adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEMAJOR:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                JSONObject jsonobj = new JSONObject(result);
                                Toast.makeText(mactivity, R.string.update_content, Toast.LENGTH_SHORT).show();
                                user.setName(et_content);
                                dissmiss();
                                if (json.getString("score") != null &&json.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    final Dialog dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("+"+json.getString("score"));
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case typeid_18:
                    JSONObject jsonObject18 = (JSONObject) msg.obj;
                    try {
                        String status = jsonObject18.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject18.getJSONArray("data");
                            spinnerlist = new ArrayList<Spinner_Bean.DataBean>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                dataBean.setName(jo.getString("name"));
                                spinnerlist.add(dataBean);
                            }
                            spinner_adapter = new Spinner_Adapter(mactivity, spinnerlist);
                            lv_pop_spinner.setAdapter(spinner_adapter);
                            setListViewHeightBasedOnChildren(lv_pop_spinner);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case typeid_1:
                    JSONObject jsonObject1 = (JSONObject) msg.obj;
                    try {
                        String status = jsonObject1.getString("status");
                        if (status.equals("success")) {
                            JSONArray jsonArray = jsonObject1.getJSONArray("data");
                            spinnerlist = new ArrayList<Spinner_Bean.DataBean>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                Spinner_Bean.DataBean dataBean = new Spinner_Bean.DataBean();
                                dataBean.setId(jo.getString("id"));
                                dataBean.setName(jo.getString("name"));
                                spinnerlist.add(dataBean);
                            }
                            spinner_adapter = new Spinner_Adapter(mactivity, spinnerlist);
                            lv_pop_spinner.setAdapter(spinner_adapter);
                            setListViewHeightBasedOnChildren(lv_pop_spinner);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case UPDATEEDUCATION:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                JSONObject jsonobj = new JSONObject(result);
                                Toast.makeText(mactivity, R.string.update_content, Toast.LENGTH_SHORT).show();
                                user.setName(et_content);
                                dissmiss();
                                if (json.getString("score") != null &&json.getString("score").length() > 0){
                                    View layout = LayoutInflater.from(mactivity).inflate(R.layout.dialog_score, null);
                                    final Dialog dialog = new AlertDialog.Builder(mactivity).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    TextView tv_score = (TextView) layout.findViewById(R.id.dialog_score);
                                    tv_score.setText("+"+json.getString("score"));
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    public Pop_spinner(final Myinfo mactivity) {
        this.mactivity = mactivity;
        user = new UserBean(mactivity);
        View view = LayoutInflater.from(mactivity).inflate(R.layout.pop_spinner, null);
        mine_pop_cancle = (TextView) view.findViewById(R.id.mine_pop_cancle);
        mine_pop_cancle.setOnClickListener(this);
        mine_pop_sure = (TextView) view.findViewById(R.id.mine_pop_sure);
        mine_pop_sure.setOnClickListener(this);
        lv_pop_spinner = (ListView) view.findViewById(R.id.lv_pop_spinner);

        lv_pop_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("et_content11111", "---------->" + et_content);

                for(int i=0;i<parent.getCount();i++){
                    View v=parent.getChildAt(i);
                    if (position == i) {
                        v.setBackgroundColor(mactivity.getResources().getColor(R.color.gray2));
                        et_content = spinnerlist.get(position).getName();
                    } else {
                        v.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
//                et_content = spinnerlist.get(position).getName();
                Log.i("et_content", "---------->" + et_content);

            }
        });
        popupWindow = new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_pop_cancle:
                dissmiss();
                break;
            case R.id.mine_pop_sure:
//                et_content;
                Log.i("et_content00000", "---------->" + et_content);

                if ("专业".equals(titlename)) {
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).updateUserMajor(user.getUserid(), et_content, UPDATEMAJOR);
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                } else if ("学历".equals(titlename)) {
                    if (HttpConnect.isConnnected(mactivity)) {
                        new StudyRequest(mactivity, handler).updateUserEducation(user.getUserid(), et_content, UPDATEEDUCATION);
                    } else {
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                }
                mactivity.getPersoanl();
                break;
        }
    }

    @Override
    public void onDismiss() {

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
    public void showAsDropDown(View parent, String titlename) {
        this.titlename = titlename;
        if ("专业".equals(titlename)) {
            if (HttpConnect.isConnnected(mactivity)) {
                //18 专业
                new StudyRequest(mactivity, handler).get_DictionaryList(typeid_18);
            } else {
                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            }
        } else if ("学历".equals(titlename)) {
            if (HttpConnect.isConnnected(mactivity)) {
                //1 学历
                new StudyRequest(mactivity, handler).get_DictionaryList(typeid_1);
            } else {
                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
            }
        }
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        WindowManager manager = (WindowManager) mactivity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int screenHeight = display.getHeight();
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, screenHeight - location[1]);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }
    /*
     解决scrollview下listview显示不全
   */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


}
