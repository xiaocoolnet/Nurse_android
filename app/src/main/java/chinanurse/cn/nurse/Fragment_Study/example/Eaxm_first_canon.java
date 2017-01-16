package chinanurse.cn.nurse.Fragment_Study.example;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by Administrator on 2016/11/7 0007.
 */

public class Eaxm_first_canon extends Fragment{
    private static final int FOURTHPAGELIST = 1;
    private View mView;
    private UserBean user;
    private HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件
    private ViewPager mViewPager;	//下方的可横向拖动的控件
    private ImageView mImageView;//下方横线
    private ArrayList<View> mViews;//用来存放下方滚动的layout(layout_1,layout_2,layout_3)
    private Activity mactivity;
    private String pagename,pageid;
    private List<String> listname = new ArrayList<>();
    private List<String> listid = new ArrayList<>();
    LocalActivityManager manager = null;
    private RadioGroup myRadioGroup;
    private LinearLayout layout,titleLayout;
    private float mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离
    private int _id = 1000;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FOURTHPAGELIST:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        listname.clear();
                        listid.clear();
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))){
                                JSONArray jsonarray = json.getJSONArray("data");
                                if (jsonarray != null &&jsonarray.length() > 0){
                                    listname.add("全部");
                                    listid.add(pageid);
                                    for (int i = 0;i < jsonarray.length();i++){
                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        listname.add(jsonobject.getString("name"));
                                        listid.add(jsonobject.getString("term_id"));
                                    }
                                    getgroup();
                                    iniListener();
                                    iniVariable();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        private void getgroup() {
            myRadioGroup = new RadioGroup(mactivity);
            myRadioGroup.setLayoutParams( new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
            layout.addView(myRadioGroup);
            for (int i = 0; i <listname.size(); i++) {
                RadioButton radio = new RadioButton(mactivity);
                radio.setBackgroundResource(R.drawable.radiobtn_selector);
                radio.setButtonDrawable(android.R.color.transparent);
                LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
                radio.setLayoutParams(l);
                radio.setGravity(Gravity.CENTER);
                radio.setPadding(20, 15, 20, 15);
                //radio.setPadding(left, top, right, bottom)
                radio.setId(_id+i);
                radio.setText(listname.get(i)+"");
                radio.setTextColor(Color.BLACK);
//                radio.setTag(map);
                if (i == 0) {
                    radio.setChecked(true);
                    int itemWidth = (int) radio.getPaint().measureText(listname.get(i)+"");
                    mImageView.setLayoutParams(new  LinearLayout.LayoutParams(itemWidth+radio.getPaddingLeft()+radio.getPaddingRight(),4));
                }
                myRadioGroup.addView(radio);
            }
            myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    //Map<String, Object> map = (Map<String, Object>) group.getChildAt(checkedId).getTag();
                    int radioButtonId = group.getCheckedRadioButtonId();
                    //根据ID获取RadioButton的实例
                    RadioButton rb = (RadioButton)mView.findViewById(radioButtonId);
                    AnimationSet animationSet = new AnimationSet(true);
                    TranslateAnimation translateAnimation;
                    translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, rb.getLeft(), 0f, 0f);
                    animationSet.addAnimation(translateAnimation);
                    animationSet.setFillBefore(true);
                    animationSet.setFillAfter(true);
                    animationSet.setDuration(300);

                    mImageView.startAnimation(animationSet);//开始上面蓝色横条图片的动画切换
                    mViewPager.setCurrentItem(radioButtonId-_id);//让下方ViewPager跟随上面的HorizontalScrollView切换
                    mCurrentCheckedRadioLeft = rb.getLeft();//更新当前蓝色横条距离左边的距离
                    mHorizontalScrollView.smoothScrollTo((int)mCurrentCheckedRadioLeft-(int)getResources().getDimension(R.dimen.activity_size_100), 0);

                    mImageView.setLayoutParams(new  LinearLayout.LayoutParams(rb.getRight()-rb.getLeft(),4));

                }
            });
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.exam_first_canon, null);
        mactivity = getActivity();
        user = new UserBean(getActivity());
        Bundle bundle = getArguments();
        if (bundle != null){
            pagename = bundle.getString("pagename");
        }
        Log.e("pagename","==================>"+pagename);
        manager = new LocalActivityManager(mactivity, true);
        manager.dispatchCreate(savedInstanceState);
        gomanview();


        return mView;
    }



    private void gomanview() {
        mHorizontalScrollView = (HorizontalScrollView)mView.findViewById(R.id.horizontalScrollView);//上面的水平滚动控件
        mViewPager = (ViewPager)mView.findViewById(R.id.pager);//下方的可横向拖动的控件
        mImageView = (ImageView)mView.findViewById(R.id.img1);//下方横线
        layout = (LinearLayout) mView.findViewById(R.id.lay);
    }
    private void iniVariable() {
        mViews = new ArrayList<View>();
        for (int i = 0; i < listname.size(); i++) {
            Intent intent1 = new Intent(mactivity,Exam_second_canon.class);
            intent1.putExtra("pageid", listid.get(i));
            mViews.add(getView("View"+listid.get(i), intent1));
        }
        mViewPager.setAdapter(new MyPagerAdapter());//设置ViewPager的适配器
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getActivity(), "考试宝典子页标题");
        gettitle();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "考试宝典子页标题");
    }
    private void gettitle() {
        if ("护士资格".equals(pagename)){
            pageid = "149";
        }else if ("初级护师".equals(pagename)){
            pageid = "150";
        }else if ("主管护师".equals(pagename)){
            pageid = "151";
        }
        if (HttpConnect.isConnnected(mactivity)) {
            new StudyRequest(mactivity,handler).getNewsTitleFirst(pageid,FOURTHPAGELIST);
        } else {
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }
    private View getView(String id, Intent intent) {
        return manager.startActivity(id, intent).getDecorView();
    }
    private void iniListener() {
        // TODO Auto-generated method stub
        mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());
    }
    /**
     * ViewPager的适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View v, int position, Object obj) {
            // TODO Auto-generated method stub
            ((ViewPager)v).removeView(mViews.get(position));
        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mViews.size();
        }

        @Override
        public Object instantiateItem(View v, int position) {
            ((ViewPager)v).addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub
        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub
        }

    }
    /**
     * ViewPager的PageChangeListener(页面改变的监听器)
     */
    private class MyPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }
        /**
         * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
         */
        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            RadioButton radioButton = (RadioButton) mView.findViewById(_id+position);
            radioButton.performClick();
        }
    }
}
