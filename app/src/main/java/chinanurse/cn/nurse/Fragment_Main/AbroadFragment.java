package chinanurse.cn.nurse.Fragment_Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import chinanurse.cn.nurse.Fragment_Abroad.AbroadFirstPage;
import chinanurse.cn.nurse.Fragment_Abroad.AbroadSecondPage;
import chinanurse.cn.nurse.Fragment_Abroad.AbroadThirdPage;
import chinanurse.cn.nurse.R;

/**
 * Created by Administrator on 2016/6/2.
 */
public class AbroadFragment extends Fragment implements View.OnClickListener{
//    private TabPageIndicator mTab;
//    private ViewPager mViewpager;

//    String[] title = {"出国动态", "成功案例", "出国百宝箱"};
//    private List<Fragment> mActivity;
    private RelativeLayout  rela_more;
    private LinearLayout line_rn, line_nation, line_competency;
    private View view_1, view_2, view_3;
    private TextView tv_rn, tv_nation, tv_conpetency;


    private AbroadFirstPage abroad_first_fragment;
    private AbroadSecondPage abroad_fsecond_fragment;
    private AbroadThirdPage abroad_third_fragment;
    private FragmentManager fragmentManager;
    private Fragment[] fragments;
    private int index, currentIndex;

    private RelativeLayout rela_fragment;

    private String titlename;
    private Intent intent;
    private FragmentTransaction fragmentTransaction;
    private View mView;
    private Activity activity;
//    112

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getContext(), R.layout.abroadfragment, null);
        activity = getActivity();
        initViewgobroad();
//        mTab = (TabPageIndicator) mView.findViewById(R.id.news_indicator);
//        mViewpager = (ViewPager) mView.findViewById(R.id.news_viewpager);
        return mView;
    }

    private void initViewgobroad() {
        rela_more = (RelativeLayout)mView.findViewById(R.id.rela_more);
        rela_more.setVisibility(View.GONE);

        rela_fragment = (RelativeLayout) mView.findViewById(R.id.rela_fragment);
        line_rn = (LinearLayout) mView.findViewById(R.id.line_rn);
        line_nation = (LinearLayout) mView.findViewById(R.id.line_nation);
        line_competency = (LinearLayout) mView.findViewById(R.id.line_competency);
        view_1 = mView.findViewById(R.id.view_1);
        view_2 = mView.findViewById(R.id.view_2);
        view_3 = mView.findViewById(R.id.view_3);
        tv_rn = (TextView) mView.findViewById(R.id.tv_rn);
        tv_nation = (TextView) mView.findViewById(R.id.tv_nation);
        tv_conpetency = (TextView) mView.findViewById(R.id.tv_conpetency);

        line_rn.setOnClickListener(this);
        line_nation.setOnClickListener(this);
        line_competency.setOnClickListener(this);
        rela_more.setOnClickListener(this);


        fragmentManager = getChildFragmentManager();
        abroad_first_fragment = new AbroadFirstPage();
        abroad_fsecond_fragment = new AbroadSecondPage();
        abroad_third_fragment = new AbroadThirdPage();
        fragments = new Fragment[]{abroad_first_fragment, abroad_third_fragment, abroad_fsecond_fragment};

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rela_fragment, abroad_first_fragment).commit();

    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        //viewpager子布局设置List
//        mActivity = new ArrayList<>();
//        //添加子布局
//        mActivity.add(new AbroadFirstPage());
//        mActivity.add(new AbroadSecondPage(getActivity()));
//        mActivity.add(new AbroadThirdPage(getActivity()));
//        mViewpager.setAdapter(new MyAdapter());
//        //viewpager设置适配器
//        mTab.setViewPager(mViewpager);
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_rn:
                tv_rn.setTextColor(getResources().getColor(R.color.purple));
                tv_nation.setTextColor(getResources().getColor(R.color.gray4));
                tv_conpetency.setTextColor(getResources().getColor(R.color.gray4));
                view_1.setBackgroundColor(getResources().getColor(R.color.purple));
                view_2.setBackgroundColor(getResources().getColor(R.color.whilte));
                view_3.setBackgroundColor(getResources().getColor(R.color.whilte));
                abroad_first_fragment = new AbroadFirstPage();
                Bundle bundle = new Bundle();
                titlename = tv_rn.getText().toString();
                bundle.putString("titlename", titlename);
                abroad_first_fragment.setArguments(bundle);
                //如果transaction  commit（）过  那么我们要重新得到transaction
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rela_fragment, abroad_first_fragment).commit();
                index = 0;

                break;


            case R.id.line_nation:
                tv_rn.setTextColor(getResources().getColor(R.color.gray4));
                tv_nation.setTextColor(getResources().getColor(R.color.purple));
                tv_conpetency.setTextColor(getResources().getColor(R.color.gray4));
                view_1.setBackgroundColor(getResources().getColor(R.color.whilte));
                view_2.setBackgroundColor(getResources().getColor(R.color.purple));
                view_3.setBackgroundColor(getResources().getColor(R.color.whilte));
                abroad_third_fragment = new AbroadThirdPage();
                Bundle bundlenation = new Bundle();
                titlename = tv_nation.getText().toString();
                bundlenation.putString("titlename", titlename);
                abroad_third_fragment.setArguments(bundlenation);
                //如果transaction  commit（）过  那么我们要重新得到transaction
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rela_fragment, abroad_third_fragment).commit();
                index = 1;

                break;


            case R.id.line_competency:
                tv_rn.setTextColor(getResources().getColor(R.color.gray4));
                tv_nation.setTextColor(getResources().getColor(R.color.gray4));
                tv_conpetency.setTextColor(getResources().getColor(R.color.purple));
                view_1.setBackgroundColor(getResources().getColor(R.color.whilte));
                view_2.setBackgroundColor(getResources().getColor(R.color.whilte));
                view_3.setBackgroundColor(getResources().getColor(R.color.purple));
                abroad_fsecond_fragment = new AbroadSecondPage();
                Bundle bundleconpetency = new Bundle();
                titlename = tv_conpetency.getText().toString();
                bundleconpetency.putString("titlename", titlename);
                abroad_fsecond_fragment.setArguments(bundleconpetency);
                //如果transaction  commit（）过  那么我们要重新得到transaction
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rela_fragment, abroad_fsecond_fragment).commit();
                index = 2;
                break;

            case R.id.rela_more:
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                break;


        }

        if (currentIndex != index) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()) {
                fragmentTransaction.add(R.id.rela_fragment, fragments[index]);
            }
            fragmentTransaction.show(fragments[index]);
            fragmentTransaction.commit();
        }
        currentIndex = index;
    }
    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getActivity(), "出国");
    }

    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "出国");
    }
}
