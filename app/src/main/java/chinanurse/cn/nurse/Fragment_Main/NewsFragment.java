package chinanurse.cn.nurse.Fragment_Main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.mobstat.StatService;
import chinanurse.cn.nurse.Fragment_News.NewsFirstPager;
import chinanurse.cn.nurse.Fragment_News.NewsFourthPager;
import chinanurse.cn.nurse.Fragment_News.NewsSecondPager;
import chinanurse.cn.nurse.Fragment_News.NewsThirdPager;
import chinanurse.cn.nurse.R;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class NewsFragment extends Fragment implements View.OnClickListener {
    private TextView topline, nurseing, heaith, meeting;
    private ImageView toplinewire, nurseingwire, heaithwire, meetingwire;
    private int index = 0, currentIndex;
    private FragmentManager fragmentManager;
    private Fragment[] fragments;
    private NewsFirstPager firstPager;
    private NewsSecondPager secondpager;
    private NewsThirdPager thirdPager;
    private NewsFourthPager fourthPager;
    private final static int FIRSTTitlrLIST = 1;
    private FragmentTransaction fragmentTransaction;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsiniview();
        topline.setOnClickListener(this);
        nurseing.setOnClickListener(this);
        heaith.setOnClickListener(this);
        meeting.setOnClickListener(this);
    }

    private void newsiniview() {
        fragmentManager = getChildFragmentManager();
        firstPager = new NewsFirstPager();
        secondpager = new NewsSecondPager();
        thirdPager = new NewsThirdPager();
        fourthPager = new NewsFourthPager();
        fragments = new Fragment[]{firstPager, secondpager, thirdPager,fourthPager};
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.news_replace_container, firstPager);
        fragmentTransaction.commit();

        topline = (TextView) getView().findViewById(R.id.news_first_topline);//头条咨询
        toplinewire = (ImageView) getView().findViewById(R.id.news_first_topline_wire);//第一个标题下面的横线
        nurseing = (TextView) getView().findViewById(R.id.news_second_nursing);//护理界
        nurseingwire = (ImageView) getView().findViewById(R.id.news_second_nursing_wire);//护理界下面横线
        heaith = (TextView) getView().findViewById(R.id.news_third_health);//健康
        heaithwire = (ImageView) getView().findViewById(R.id.news_third_health_wire);//健康下面横线
        meeting = (TextView) getView().findViewById(R.id.news_four_meeting);//学术会议
        meetingwire = (ImageView) getView().findViewById(R.id.news_four_meeting_wire);//学术会议下面横线
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = View.inflate(getContext(), R.layout.newfragment, null);
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.news_first_topline:
                topline.setTextColor(this.getResources().getColor(R.color.purple));
                nurseing.setTextColor(this.getResources().getColor(R.color.gray4));
                heaith.setTextColor(this.getResources().getColor(R.color.gray4));
                meeting.setTextColor(this.getResources().getColor(R.color.gray4));
                toplinewire.setBackgroundColor(this.getResources().getColor(R.color.purple));
                nurseingwire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                heaithwire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                meetingwire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                index = 0;
                break;
            case R.id.news_second_nursing:
                topline.setTextColor(this.getResources().getColor(R.color.gray4));
                nurseing.setTextColor(this.getResources().getColor(R.color.purple));
                heaith.setTextColor(this.getResources().getColor(R.color.gray4));
                meeting.setTextColor(this.getResources().getColor(R.color.gray4));
                toplinewire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                nurseingwire.setBackgroundColor(this.getResources().getColor(R.color.purple));
                heaithwire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                meetingwire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                fragmentTransaction.add(R.id.news_replace_container, fragments[index]);
                index = 1;
                break;
            case R.id.news_third_health:
                topline.setTextColor(this.getResources().getColor(R.color.gray4));
                nurseing.setTextColor(this.getResources().getColor(R.color.gray4));
                heaith.setTextColor(this.getResources().getColor(R.color.purple));
                meeting.setTextColor(this.getResources().getColor(R.color.gray4));
                toplinewire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                nurseingwire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                heaithwire.setBackgroundColor(this.getResources().getColor(R.color.purple));
                meetingwire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                index = 2;
                break;
            case R.id.news_four_meeting:
                topline.setTextColor(this.getResources().getColor(R.color.gray4));
                nurseing.setTextColor(this.getResources().getColor(R.color.gray4));
                heaith.setTextColor(this.getResources().getColor(R.color.gray4));
                meeting.setTextColor(this.getResources().getColor(R.color.purple));
                toplinewire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                nurseingwire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                heaithwire.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                meetingwire.setBackgroundColor(this.getResources().getColor(R.color.purple));
                index = 3;
                break;
        }
        if (currentIndex != index) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()) {
                fragmentTransaction.add(R.id.news_replace_container, fragments[index]);
            }
            fragmentTransaction.show(fragments[index]);
            fragmentTransaction.commit();
        }
        currentIndex = index;
    }
    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getActivity(), "新闻");
    }

    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "新闻");
    }
}
