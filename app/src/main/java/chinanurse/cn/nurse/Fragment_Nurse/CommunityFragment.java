package chinanurse.cn.nurse.Fragment_Nurse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

import chinanurse.cn.nurse.FragmentTag;
import chinanurse.cn.nurse.LoginActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by zhuchongkun on 2016/12/11.
 * 护士站--圈子
 */

public class CommunityFragment extends Fragment implements View.OnClickListener {
    private UserBean user;
    private TextView tvFind, tv_follow, tvMe;
    private ImageView ivFind, iv_follow, ivMe;
    private int index = 0;
    /**
     * 当前Fragment的key
     */
    private FragmentTag mCurrentTag;
    /**
     * 当前Fragment
     */
    private Fragment mCurrentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = View.inflate(getActivity(), R.layout.fragment_community, null);
        user = new UserBean(getActivity());
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        mCurrentTag = FragmentTag.TAG_FIND;
        mCurrentFragment = new FindFragment();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.community_replace_container, mCurrentFragment,
                        mCurrentTag.getTag()).show(mCurrentFragment).commit();
    }

    private void initView() {
        tvFind = (TextView) getView().findViewById(R.id.community_first_find);
        tvFind.setOnClickListener(this);
        tv_follow = (TextView) getView().findViewById(R.id.community_second_follow);
        tv_follow.setOnClickListener(this);
        tvMe = (TextView) getView().findViewById(R.id.community_third_me);
        tvMe.setOnClickListener(this);
        ivFind = (ImageView) getView().findViewById(R.id.community_first_find_wire);
        iv_follow = (ImageView) getView().findViewById(R.id.community_second_follow_wire);
        ivMe = (ImageView) getView().findViewById(R.id.community_third_me_wire);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.community_first_find:
                tvFind.setTextColor(this.getResources().getColor(R.color.purple));
                tv_follow.setTextColor(this.getResources().getColor(R.color.gray4));
                tvMe.setTextColor(this.getResources().getColor(R.color.gray4));
                ivFind.setBackgroundColor(this.getResources().getColor(R.color.purple));
                iv_follow.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                ivMe.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                index = 0;
                switchFragmentone(FragmentTag.TAG_FIND);
                break;
            case R.id.community_second_follow:
                if (user.getUserid() != null && user.getUserid().length() > 0) {
                    tv_follow.setTextColor(this.getResources().getColor(R.color.purple));
                    tvFind.setTextColor(this.getResources().getColor(R.color.gray4));
                    tvMe.setTextColor(this.getResources().getColor(R.color.gray4));
                    iv_follow.setBackgroundColor(this.getResources().getColor(R.color.purple));
                    ivFind.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                    ivMe.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                    index = 1;
                    switchFragmentone(FragmentTag.TAG_FOLLOW_FORUM_LIST);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.community_third_me:
                if (user.getUserid() != null && user.getUserid().length() > 0) {

                    tvMe.setTextColor(this.getResources().getColor(R.color.purple));
                    tvFind.setTextColor(this.getResources().getColor(R.color.gray4));
                    tv_follow.setTextColor(this.getResources().getColor(R.color.gray4));
                    ivFind.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                    iv_follow.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                    ivMe.setBackgroundColor(this.getResources().getColor(R.color.purple));
                    index = 2;
                    switchFragmentone(FragmentTag.TAG_ME);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

                break;
        }
    }

    /**
     * 切换Fragment
     *
     * @param to 目标Fragment
     */
    public void switchFragmentone(FragmentTag to) {
        if (to != null) {
            if (!mCurrentTag.equals(to)) {
                Fragment currentF = getChildFragmentManager().findFragmentByTag(
                        mCurrentTag.getTag());
                Fragment toF = getChildFragmentManager().findFragmentByTag(to.getTag());
                if (null == toF) { // 先判断是否被add过
                    try {
                        // 切换按钮动画
                        // 更新当前Fragment
                        mCurrentTag = to;
                        mCurrentFragment = toF;
                        // 未add过，使用反射新建一个Fragment并add到FragmentManager中
                        toF = (Fragment) Class.forName(to.getTag()).newInstance();
                        getChildFragmentManager().beginTransaction().hide(currentF)
                                .add(R.id.community_replace_container, toF, to.getTag()).commit(); // 隐藏当前的fragment，add下一个到Activity中
                        // 更新当前Fragment
                        mCurrentTag = to;
                        mCurrentFragment = toF;
                    } catch (Exception e) {
                    }
                } else {
                    // 更新当前Fragment
                    mCurrentTag = to;
                    mCurrentFragment = toF;
                    FragmentTag detailpage = FragmentTag.TAG_DETAILTALENT;
                    if (detailpage.equals(to) || to.equals(FragmentTag.TAG_DETAILWORK)) {
                        try {
                            toF = (Fragment) Class.forName(to.getTag()).newInstance();
                            getChildFragmentManager().beginTransaction().hide(currentF)
                                    .add(R.id.community_replace_container, toF, to.getTag()).commit();
                        } catch (Exception e) {
                        }
                    } else {
                        // add过，直接hide当前，并show出目标Fragment
                        getChildFragmentManager().beginTransaction().hide(currentF)
                                .show(toF).commit(); // 隐藏当前的fragment，显示下一个
                    }
                    // 更新当前Fragment
                    mCurrentTag = to;
                    mCurrentFragment = toF;

                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(getActivity(), "护士站");
    }

    @Override
    public void onPause() {
        super.onPause();
        // 配对页面埋点，与start的页面名称要一致
        StatService.onPageEnd(getActivity(), "护士站");
    }
}
