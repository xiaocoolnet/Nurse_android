package chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.Fragment_Nurse_mine.mine_collext.adapter.MyCollectQuestion_Adapter;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.MyCollectQuestion_Bean;
import chinanurse.cn.nurse.bean.Question_hashmap_data;
import chinanurse.cn.nurse.bean.UserBean;
import chinanurse.cn.nurse.new_Activity.SaveQuestionInfo;
import chinanurse.cn.nurse.new_Activity.ViewPagerScroller;
import chinanurse.cn.nurse.new_Activity.VoteSubmitViewPager;

/**
 * 我的收藏试题题目的详情界面
 * Created by Administrator on 2016/7/6.
 */
public class MyCollect_Question_Activity extends Activity implements View.OnClickListener {
    private static final int GETTESTTEXT_OVER = 222;
    private static final int SETCOLLECT = 5;//收藏
    private static final int CANCELCOLLECT = 6;//取消收藏
    private static final int ISCOLLECT = 7;//判断是否收藏
    private static final int ISCOLLECTONE = 8;

    Activity mactivity;
//    private List<MyCollectQuestion_Bean.DataBean> answerlistbean_over = new ArrayList<>();
    private List<View> viewItems = new ArrayList<View>();
    public List<SaveQuestionInfo> questionInfos = new ArrayList<SaveQuestionInfo>();
    private List<MyCollectQuestion_Bean.DataBean> listdata=Question_hashmap_data.MyCollectList;

//    public static int gradview_position;
    private int ViewPosition=0;

    private TextView question_answer_submit, top_title;
    private RelativeLayout btn_back;
    private VoteSubmitViewPager vote_submit_viewpager;
    private Button collectTag;

    //弹出框弹出内容
    private String errorMsg = "",type;
    boolean isPause = false;
    private MyCollectQuestion_Adapter pagerAdapter;
    private int pageScore, allpageNum, errortopicNums, errortopicNums1;//分数，页数（题数）、错题数

    private UserBean user;
    private Intent intent;
    private MyCollectQuestion_Bean.DataBean dailydata;
    private String questionType, questionNum;
    private ImageView submit_image;
    private Boolean isDaily;
    private int a;
    private boolean ISc;//判断是否收藏过
    private String decrisp;
    private Dialog dialog;


    private Handler collecthandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SETCOLLECT:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String data = obj.getString("data");
                            if ("success".equals(status)) {
                                Toast.makeText(MyCollect_Question_Activity.this, R.string.question_collect_succcess, Toast.LENGTH_SHORT).show();
                                if (collectTag == null) {
                                    return;
                                }
                                collectTag.setSelected(true);
                                collectTag.setTextColor(getResources().getColor(R.color.purple));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case CANCELCOLLECT:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            String status = obj.getString("status");
                            String data = obj.getString("data");
                            if ("success".equals(status)) {
//                                collectTag.setBackgroundResource(R.mipmap.btn_collet);
                                Toast.makeText(MyCollect_Question_Activity.this, R.string.question_cancel_collect, Toast.LENGTH_SHORT).show();
                                if(collectTag==null){
                                    return;
                                }
                                collectTag.setSelected(false);
                                collectTag.setTextColor(getResources().getColor(R.color.gray6));
                                Log.i("试题收藏.取消..", "============>" + listdata.get(ViewPosition).getTitle());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            super.handleMessage(msg);

        }
    };

    private Handler hanlder = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETTESTTEXT_OVER:
//                    answerlistbean_over.clear();
//                    answerlistbean_over.add(dailydata);
                    for (int i = 0; i < listdata.size(); i++) {
                        viewItems.add(getLayoutInflater().inflate(
                                R.layout.question_mycollect_layout, null));
                    }
                    pagerAdapter = new MyCollectQuestion_Adapter(
                            MyCollect_Question_Activity.this, viewItems,
                            listdata, questionType, questionNum, a);
                    vote_submit_viewpager.setAdapter(pagerAdapter);
                    vote_submit_viewpager.getParent()
                            .requestDisallowInterceptTouchEvent(false);
                    vote_submit_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            ViewPosition = position;
                            TextView tv = (TextView) viewItems.get(position).findViewById(R.id.quedtion_at_present_topic);
                            tv.setText(position + 1 + "/" + listdata.size());
                            isCollect();
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    isCollect();
                    setCurrentView(a);
                    break;

                case ISCOLLECT://判断是否收藏
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
                                if("had".equals(obj.optString("data"))){
                                    if(collectTag==null){
                                        return;
                                    }
                                    collectTag.setSelected(true);
                                    collectTag.setTextColor(getResources().getColor(R.color.purple));
                                }
                            }else if("error".equals(obj.optString("status"))){
                                if("no".equals(obj.optString("data"))){
                                    if(collectTag==null){
                                        return;
                                    }
                                    collectTag.setSelected(false);
                                    collectTag.setTextColor(getResources().getColor(R.color.gray6));

                                }
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case ISCOLLECTONE://判断是否收藏
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        try {
                            JSONObject obj = new JSONObject(result);
                            if ("success".equals(obj.optString("status"))) {
                                if("had".equals(obj.optString("data"))){
                                    if (HttpConnect.isConnnected(MyCollect_Question_Activity.this)) {
                                         new StudyRequest(MyCollect_Question_Activity.this, collecthandler).DELLCOLLEXT(user.getUserid(), listdata.get(ViewPosition).getObject_id(),
                                       questionType, CANCELCOLLECT);
//
                                    } else {
                                    Toast.makeText(MyCollect_Question_Activity.this, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                  }
                                }
                            }else if("error".equals(obj.optString("status"))){
                                if("no".equals(obj.optString("data"))){
                                    if (listdata.get(ViewPosition).getDescription() != null &&listdata.get(ViewPosition).getDescription().length() > 0){
                                        decrisp = listdata.get(ViewPosition).getPost_description().toString();
                                    }else{
                                        decrisp = "";
                                    }
                                    if (HttpConnect.isConnnected(MyCollect_Question_Activity.this)) {
                                        Log.i("试题收藏.....","============>"+ listdata.get(ViewPosition).getTitle());
                                    new StudyRequest(MyCollect_Question_Activity.this, collecthandler).COLLEXT(user.getUserid(), listdata.get(ViewPosition).getObject_id(), questionType,
                                    listdata.get(ViewPosition).getTitle(), decrisp, SETCOLLECT);
                                    } else {
                                    Toast.makeText(MyCollect_Question_Activity.this, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_mian);
        //先清除之前的记录
        mactivity = this;
        user = new UserBean(mactivity);
        intent = getIntent();
        //获取当前题目
        dailydata = (MyCollectQuestion_Bean.DataBean) intent.getSerializableExtra("collquestion");
        //获取总列表
//        listdata = (List<MyCollectQuestion_Bean.DataBean>) intent.getSerializableExtra("collList");
        type = intent.getStringExtra("type");
        a = intent.getExtras().getInt("Qposition");
//        gradview_position = intent.getIntExtra("mposition", -1);
        questionType = dailydata.getType();
        questionNum = String.valueOf(listdata.size());
        iniview();
    }

    private void iniview() {
        top_title = (TextView) findViewById(R.id.top_title);
        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        vote_submit_viewpager = (VoteSubmitViewPager) findViewById(R.id.vote_submit_viewpager);
        vote_submit_viewpager.setOffscreenPageLimit(10);
        top_title.setText("我的试题收藏");
//        if ("1".equals(type)){
//            top_title.setText("练习题");
//            isDaily =true;
//        }else if("11".equals(type)){
//            top_title.setText("在线考试");
//            isDaily =false;
//        }
        initViewPagerScroll();
        Message msg = new Message();
        msg.what=GETTESTTEXT_OVER;
        hanlder.sendMessage(msg);

    }

    /**
     * @param index 根据索引值切换页面
     */
    public void setCurrentView(int index) {
        vote_submit_viewpager.setCurrentItem(index);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(vote_submit_viewpager.getContext());
            mScroller.set(vote_submit_viewpager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        if (!"".equals(questionType)&&null != questionType&&null != questionNum){
//            if (HttpConnect.isConnnected(mactivity)) {
//                    new StudyRequest(mactivity, hanlder).TEXTLIST(user.getUserid(), questionType, questionNum, GETTESTTEXT_OVER);
//            } else {
//                Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
//            }
//        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                mactivity.finish();
                break;
        }
    }
    //每日一练和在线考试中的取消添加收藏
    public void isCollect() {
        collectTag = (Button) vote_submit_viewpager.findViewWithTag("collectTag"+ViewPosition);
        if (HttpConnect.isConnnected(mactivity)) {
            new StudyRequest(mactivity, hanlder).ISCOLLECT(user.getUserid(), listdata.get(ViewPosition).getObject_id()/**题目ID**/, "2", ISCOLLECT);
        }else{
            Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
        if(collectTag!=null){
        collectTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpConnect.isConnnected(mactivity)) {
                    new StudyRequest(mactivity, hanlder).ISCOLLECT(user.getUserid(), listdata.get(ViewPosition).getObject_id()/**题目ID**/, "2", ISCOLLECTONE);
                } else {
                    Toast.makeText(mactivity, R.string.net_erroy, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    }
}
