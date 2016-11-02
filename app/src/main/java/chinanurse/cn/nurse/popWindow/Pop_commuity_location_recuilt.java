package chinanurse.cn.nurse.popWindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import chinanurse.cn.nurse.Fragment_Nurse_mine.My_personal_recruit;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.WheelView.OnWheelChangedListener;
import chinanurse.cn.nurse.WheelView.WheelView;
import chinanurse.cn.nurse.WheelView.adapters.ArrayWheelAdapter;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.model.CityModel;
import chinanurse.cn.nurse.model.DistrictModel;
import chinanurse.cn.nurse.model.ProvinceModel;
import chinanurse.cn.nurse.service.XmlParserHandler;

//import com.mrwujay.cascade.service.XmlParserHandler;

/**
 * Created by Administrator on 2016/7/21.
 */
public class Pop_commuity_location_recuilt implements PopupWindow.OnDismissListener, View.OnClickListener, OnWheelChangedListener {

    private static final int UPDATEADDRESS = 1;
    private My_personal_recruit mactivity;
    private PopupWindow popupWindow;
    private TextView mine_pop_cancle, mine_pop_sure;
    private WheelView id_province, id_city, id_district;
    private UserBean user;
    private String et_content;
    private View rootView;
    private TextView textviewcity,textviewshi,textviewqu;

    /**
     * 所有省
     */
//	protected String[] mProvinceDatas;
    public static String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
//	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    public Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
//	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    public Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
//	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    public Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
//	protected String mCurrentProviceName;
    public String mCurrentProviceName;
    /**
     * 当前市的名称
     */
//	protected String mCurrentCityName;
    public String mCurrentCityName;
    /**
     * 当前区的名称
     */
//	protected String mCurrentDistrictName ="";
    public String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
//	protected String mCurrentZipCode ="";
    public String mCurrentZipCode = "";
    TextView textview;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEADDRESS:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            String status = json.getString("status");
                            String data = json.getString("data");
                            if (status.equals("success")) {
                                Toast.makeText(mactivity, R.string.update_content, Toast.LENGTH_SHORT).show();
                                user.setName(et_content);
                                Intent intent = new Intent();
                                intent.putExtra("LOCATION", et_content);
                                mactivity.setResult(Activity.RESULT_OK, intent);
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

    public Pop_commuity_location_recuilt(My_personal_recruit mActivity) {
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
        textviewcity = (TextView) rootView.findViewById(R.id.sheng_spinner);
//        textviewshi = (TextView) rootView.findViewById(R.id.shi_spinner);
//        textviewqu = (TextView) rootView.findViewById(R.id.qu_spinner);

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
        id_province.setViewAdapter(new ArrayWheelAdapter<String>(mactivity, mProvinceDatas));
        // 设置可见条目数量
        id_province.setVisibleItems(7);
        id_city.setVisibleItems(7);
        id_district.setVisibleItems(7);
        updateCities();
        updateAreas();
        updateid_district();
    }

    private void updateid_district() {
        int newValue = id_district.getCurrentItem();
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
    }

    private void updateAreas() {
        int pCurrent = id_city.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        id_district.setViewAdapter(new ArrayWheelAdapter<String>(mactivity, areas));
        id_district.setCurrentItem(0);
        updateid_district();
    }

    private void updateCities() {
        int pCurrent = id_province.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        id_city.setViewAdapter(new ArrayWheelAdapter<String>(mactivity, cities));
        id_city.setCurrentItem(0);
        updateAreas();
    }

    private void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = mactivity.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    if (districtList != null && !districtList.isEmpty()){
                        mCurrentDistrictName = districtList.get(0).getName();
                        mCurrentZipCode = districtList.get(0).getZipcode();
                    }
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
//        return new String[0];
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
                et_content = mCurrentProviceName +"-"+ mCurrentCityName + "-"+mCurrentDistrictName;
                textviewcity.setText(et_content);
//                textviewshi.setText(mCurrentCityName);
//                textviewqu.setText(mCurrentDistrictName);
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
            updateid_district();
//            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
//            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    public String getString() {
        return et_content;
    }
}
