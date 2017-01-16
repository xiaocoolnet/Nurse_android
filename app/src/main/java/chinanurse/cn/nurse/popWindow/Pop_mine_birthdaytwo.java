package chinanurse.cn.nurse.popWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import chinanurse.cn.nurse.Fragment_Mine.My_personal_recruit;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WheelView.OnWheelChangedListener;
import chinanurse.cn.nurse.WheelView.WheelView;
import chinanurse.cn.nurse.WheelView.adapters.ArrayWheelAdapter;
import chinanurse.cn.nurse.bean.UserBean;

//import com.mrwujay.cascade.service.XmlParserHandler;

/**
 * Created by Administrator on 2016/7/21.
 */
public class Pop_mine_birthdaytwo implements PopupWindow.OnDismissListener, View.OnClickListener, OnWheelChangedListener {

    private static final int UPDATEBIRTHDAY = 1;
    private My_personal_recruit mactivity;
    private PopupWindow popupWindow;
    private TextView mine_pop_cancle, mine_pop_sure;
    private WheelView id_province, id_city, id_district;
    private UserBean user;
    private String et_content,date;
    private View rootView;
    private TextView textviewlala,birthady_year, birthady_month, birthady_day;


    /**
     * 所有省
     */
//	protected String[] mProvinceDatas;
    public static String[] mYearDatas,daynum,monthnum,areas;
    /**
     * key - 省 value - 市
     */
//	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    public Map<String, String[]> mYearMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
//	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    public Map<String, String[]> mMonthMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
//	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    public Map<String, String> mDayMap = new HashMap<String, String>();
    public Map<String, String[]> mDayallMap = new HashMap<String, String[]>();

    /**
     * 当前省的名称
     */
//	protected String mCurrentProviceName;
    public String mYear;
    /**
     * 当前市的名称
     */
//	protected String mCurrentCityName;
    public String mMonth;
    /**
     * 当前区的名称
     */
//	protected String mCurrentDistrictName ="";
    public String mDay = "";

    /**
     * 当前区的邮政编码
     */
    public int k;
    private int zhengchu,quyu;
    public String mCurrentZipCode = "";
    TextView textview;
    public Pop_mine_birthdaytwo(My_personal_recruit mActivity) {
        this.mactivity = mActivity;
        user = new UserBean(mactivity);

        View view = LayoutInflater.from(mActivity).inflate(R.layout.mine_location_popupwindow, null);
        mine_pop_cancle = (TextView) view.findViewById(R.id.mine_pop_cancle);
        mine_pop_cancle.setOnClickListener(this);
        mine_pop_sure = (TextView) view.findViewById(R.id.mine_pop_sure);
        mine_pop_sure.setOnClickListener(this);
        id_province = (WheelView) view.findViewById(R.id.id_province);
        id_province.addChangingListener(this);
        id_city = (WheelView) view.findViewById(R.id.id_city);
        id_city.addChangingListener(this);
        id_district = (WheelView) view.findViewById(R.id.id_district);
        id_district.addChangingListener(this);
//        mactivity.findViewById(R.id.myinfo_righttext_text).setT

        //获取activity根视图,rootView设为全局变量
        rootView = mactivity.getWindow().getDecorView();
        birthady_year = (TextView) rootView.findViewById(R.id.edit_personal_year);
        birthady_month = (TextView) rootView.findViewById(R.id.edit_personal_month);
        birthady_day = (TextView) rootView.findViewById(R.id.edit_personal_day);

        setUpData();
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置popwindow的动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOnDismissListener(this);// 当popWindow消失时的监听
    }

    private void setUpData() {
        initProvinceDatas();
//        id_province.setViewAdapter(new ArrayWheelAdapter<String>(mactivity,baseactivity.mProvinceDatas));
        id_province.setViewAdapter(new ArrayWheelAdapter<String>(mactivity, mYearDatas));
        // 设置可见条目数量
        id_province.setVisibleItems(7);
        id_city.setVisibleItems(7);
        id_district.setVisibleItems(7);
        updateCities();
        updateAreas();
        updatedirst();
    }

    private void updatedirst() {
        int pCurrent = id_district.getCurrentItem();
        if (0 == zhengchu){
            if (0 == quyu){
                mDay = mDayallMap.get(mMonth)[pCurrent];
                mCurrentZipCode = mDayMap.get(mDay);
            }else{
                mDay = mMonthMap.get(mMonth)[pCurrent];
                mCurrentZipCode = mDayMap.get(mDay);
            }
        }else{
            quyu = Integer.valueOf(mYear)%4;
            if (0 == quyu){
                mDay = mDayallMap.get(mMonth)[pCurrent];
                mCurrentZipCode = mDayMap.get(mDay);
            }else{
                mDay = mMonthMap.get(mMonth)[pCurrent];
                mCurrentZipCode = mDayMap.get(mDay);
            }
        }

    }

    private void updateAreas() {
        int pCurrent = id_city.getCurrentItem();
        mMonth = mYearMap.get(mYear)[pCurrent];
        zhengchu = Integer.valueOf(mYear)%100;
        quyu = Integer.valueOf(mYear)%400;
        if (0 == zhengchu){
            if (0 == quyu){
                areas = mDayallMap.get(mMonth);
            }else{
                areas = mMonthMap.get(mMonth);
            }
        }else{
            quyu = Integer.valueOf(mYear)%4;
            if (0 == quyu){
                areas = mDayallMap.get(mMonth);
            }else{
                areas = mMonthMap.get(mMonth);
            }
        }
        if (areas == null) {
            areas = new String[]{""};
        }
        id_district.setViewAdapter(new ArrayWheelAdapter<String>(mactivity, areas));
        id_district.setCurrentItem(0);
        updatedirst();
    }

    private void updateCities() {
        int pCurrent = id_province.getCurrentItem();
        mYear = mYearDatas[pCurrent];
        String[] cities = mYearMap.get(mYear);
        if (cities == null) {
            cities = new String[]{""};
        }
        id_city.setViewAdapter(new ArrayWheelAdapter<String>(mactivity, cities));
        id_city.setCurrentItem(0);
        updateAreas();
    }

    private void initProvinceDatas() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy");
        String date = sDateFormat.format(new Date());
        //*/ 默认当前年月
        mYear = date;
        mMonth = "1";
        mDay = "1";
        mYearDatas = new String[Integer.valueOf(date) - 1016];
        for (int i = 1017; i <= Integer.valueOf(date); i++) {
            // 遍历所有年
            mYearDatas[Integer.valueOf(date) - i] = String.valueOf(i);
            String shijiyear = String.valueOf(i).substring(2);
            zhengchu = i % 400;
            quyu = i % 100;
            if (0 == quyu) {
                if (0 == zhengchu) {
                    monthnum = new String[12];
                    for (int j = 1; j <= 12; j++) {
                        // 遍历省下面的所有市的数据
                        monthnum[j - 1] = String.valueOf(j);
                        if (j == 1 || j == 3 || j == 5 || j == 7 || j == 8 || j == 10 || j == 12) {
                            String[] daynum = new String[31];
                            for (k = 1; k <= 31; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);
                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mDayallMap.put(String.valueOf(j), daynum);
                        } else if (j == 4 || j == 6 || j == 9 || j == 11) {
                            String[] daynum = new String[30];
                            for (int k = 1; k <= 30; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);
                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mDayallMap.put(String.valueOf(j), daynum);
                        } else if (j == 2) {
                            daynum = new String[29];
                            for (int k = 1; k <= 29; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);

                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mDayallMap.put(String.valueOf(j), daynum);
                        }
                    }
                    // 省-市的数据，保存到mCitisDatasMap
                    mYearMap.put(String.valueOf(i), monthnum);
                }else{
                    monthnum = new String[12];
                    for (int j = 1; j <= 12; j++) {
                        // 遍历省下面的所有市的数据
                        monthnum[j - 1] = String.valueOf(j);
                        if (j == 1 || j == 3 || j == 5 || j == 7 || j == 8 || j == 10 || j == 12) {
                            String[] daynum = new String[31];
                            for (k = 1; k <= 31; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);
                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mMonthMap.put(String.valueOf(j), daynum);
                        } else if (j == 4 || j == 6 || j == 9 || j == 11) {
                            String[] daynum = new String[30];
                            for (int k = 1; k <= 30; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);
                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mMonthMap.put(String.valueOf(j), daynum);
                        } else if (j == 2) {
                            daynum = new String[28];
                            for (int k = 1; k <= 28; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);

                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mMonthMap.put(String.valueOf(j), daynum);
                        }
                    }
                    // 省-市的数据，保存到mCitisDatasMap
                    mYearMap.put(String.valueOf(i), monthnum);
                }
            } else {
                zhengchu = i % 4;
                Log.e("zhengchu1", "==================>" + zhengchu);
                Log.e("quyu", "==================>" + quyu);
                if (0 == zhengchu) {
                    monthnum = new String[12];
                    for (int j = 1; j <= 12; j++) {
                        // 遍历省下面的所有市的数据
                        monthnum[j - 1] = String.valueOf(j);
                        if (j == 1 || j == 3 || j == 5 || j == 7 || j == 8 || j == 10 || j == 12) {
                            String[] daynum = new String[31];
                            for (k = 1; k <= 31; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);
                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mDayallMap.put(String.valueOf(j), daynum);
                        } else if (j == 4 || j == 6 || j == 9 || j == 11) {
                            String[] daynum = new String[30];
                            for (int k = 1; k <= 30; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);
                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mDayallMap.put(String.valueOf(j), daynum);
                        } else if (j == 2) {
                            daynum = new String[29];
                            for (int k = 1; k <= 29; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);
                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mDayallMap.put(String.valueOf(j), daynum);
                        }
                    }
                    // 省-市的数据，保存到mCitisDatasMap
                    mYearMap.put(String.valueOf(i), monthnum);
                } else {
                    monthnum = new String[12];
                    for (int j = 1; j <= 12; j++) {
                        // 遍历省下面的所有市的数据
                        monthnum[j - 1] = String.valueOf(j);
                        if (j == 1 || j == 3 || j == 5 || j == 7 || j == 8 || j == 10 || j == 12) {
                            String[] daynum = new String[31];
                            for (k = 1; k <= 31; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);
                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mMonthMap.put(String.valueOf(j), daynum);
                        } else if (j == 4 || j == 6 || j == 9 || j == 11) {
                            String[] daynum = new String[30];
                            for (int k = 1; k <= 30; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);
                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mMonthMap.put(String.valueOf(j), daynum);
                        } else if (j == 2) {
                            daynum = new String[28];
                            for (int k = 1; k <= 28; k++) {
                                daynum[k - 1] = String.valueOf(k);
                                mDayMap.put(String.valueOf(k), null);
                            }
                            // 市-区/县的数据，保存到mDistrictDatasMap
                            mMonthMap.put(String.valueOf(j), daynum);
                        }
                    }
                    // 省-市的数据，保存到mCitisDatasMap
                    mYearMap.put(String.valueOf(i), monthnum);
                }
            }

        }
    }

    @Override
    public void onDismiss() {

    }

    /**
     * 弹窗显示的位置
     */
    public void showAsDropDown(View parent) {
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

    /**
     * 消除弹窗
     */
    public void dissmiss() {
        popupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_pop_cancle:
                dissmiss();
                break;
            case R.id.mine_pop_sure:
                et_content = mYear + "-" + mMonth + "-" + mDay;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                date = formatter.format(new Date());
                if (Integer.valueOf(mYear) > Integer.valueOf(date)) {
                    Toast.makeText(mactivity, "请选择正确的出生日期", Toast.LENGTH_SHORT).show();
                } else {
                    birthady_year.setText(mYear + "");
                    birthady_month.setText(mMonth + "");
                    birthady_day.setText(mDay + "");
                }
                dissmiss();
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
// TODO Auto-generated method stub
        if (wheel == id_province) {
            updateCities();
        } else if (wheel == id_city) {
            updateAreas();
        } else if (wheel == id_district) {
            updatedirst();
        }
    }

    public String getString() {
        return et_content;
    }
}
