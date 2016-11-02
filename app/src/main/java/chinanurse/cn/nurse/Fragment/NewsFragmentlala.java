package chinanurse.cn.nurse.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.HttpConn.HttpConnect;
import chinanurse.cn.nurse.HttpConn.request.StudyRequest;
import chinanurse.cn.nurse.Fragment_Nurse_News.NewsFirstPager;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.adapter.update_newspage.NewsFragmentnew_Activity;

/**
 * 新闻界面
 * Created by Administrator on 2016/6/2.
 */
public class NewsFragmentlala extends Fragment {
    public TabPageIndicator mTab;
    public ViewPager mViewpager;
    private List<String> Title;//头标题集合
    private List<String> titleid = new ArrayList<String>();//头部标题的id
    private String parentid;
    private static final int FIRSTTitlrLIST = 1;
    private List<Fragment> viewItems = new ArrayList<Fragment>();//view集合
    private int titletype;

    private NewsFragmentnew_Activity newsFragment;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FIRSTTitlrLIST:
                    if (msg.obj != null) {
                        String mTitle = (String) msg.obj;
                        Log.i("newfragment", "------------------>" + mTitle);
                        viewItems.clear();
                        Title.clear();
                        titleid.clear();
                        //解析JSON数据
                        JSONObject mJson;
                        try {
                            //解析JSON数据
                            mJson = new JSONObject(mTitle);
                            //。。获取data数组
                            JSONArray mJarray = mJson.getJSONArray("data");
                            if (mJarray != null || mJarray.length() > 0) {
                                for (int i = 0; i < mJarray.length(); i++) {
                                    JSONObject temp = (JSONObject) mJarray.get(i);
                                    titletype = i;
                                    Title.add(temp.getString("name"));
                                    titleid.add(temp.getString("term_id"));
                                    //将数组信息放入头标题集合中
                                    if ("7".equals(titleid.get(i))){
//                                        NewsFourthPager fouthpage = new NewsFourthPager();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("index", i);//当前fg标识
                                        bundle.putString("titletype", titleid.get(i));
                                        bundle.putString("Title", Title.get(i));
//                                        fouthpage.setArguments(bundle);
//                                        viewItems.add(fouthpage);
                                    }else{
                                        NewsFirstPager firstPage = new NewsFirstPager();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("index", i);//当前fg标识
                                        bundle.putString("titletype",titleid.get(i));
                                        bundle.putString("Title", Title.get(i));
                                        firstPage.setArguments(bundle);
                                        viewItems.add(firstPage);
                                    }
                                }
                            }
                            try {

                                newsFragment = new NewsFragmentnew_Activity(getChildFragmentManager(), viewItems, Title, titleid);
                                mViewpager.setAdapter(newsFragment);
                                newsFragment.notifyDataSetChanged();
                                //将标题栏与viewpager关联起来
                                mTab.setVisibility(View.VISIBLE);
                                mTab.setViewPager(mViewpager);



                            } catch (Exception e) {

                                throw new RuntimeException(e);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

//    private void hideFragments(FragmentTransaction transaction) {
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Title = new ArrayList<>();
        parentid = "1";
        View mView = View.inflate(getContext(), R.layout.newsfragment, null);
        mTab = (TabPageIndicator) mView.findViewById(R.id.news_indicator);//初始化标题栏
        mViewpager = (ViewPager) mView.findViewById(R.id.news_viewpager);//初始化Viewpager
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (HttpConnect.isConnnected(getActivity())) {
            new StudyRequest(getActivity(), handler).getNewsTitleFirst(parentid, FIRSTTitlrLIST);
        } else {
            Toast.makeText(getActivity(), R.string.net_erroy, Toast.LENGTH_SHORT).show();
        }
    }
}
