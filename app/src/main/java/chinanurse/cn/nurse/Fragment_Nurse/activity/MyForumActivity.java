package chinanurse.cn.nurse.Fragment_Nurse.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import chinanurse.cn.nurse.FragmentTag;
import chinanurse.cn.nurse.Fragment_Nurse.MyForumListFragment;
import chinanurse.cn.nurse.MainActivity;
import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.bean.UserBean;

/**
 * Created by zhuchongkun on 2016/12/31.
 * 护士站--圈子-我——我的帖子
 */

public class MyForumActivity extends FragmentActivity implements View.OnClickListener {
    private String TAG="MyForumActivity";
    private Context mContext;
    private UserBean user;
    private TextView tvForum,tvContent;
    private ImageView ivForum,ivContent;
    private RelativeLayout rl_back;
    private int index = 0;
    /**
     * 当前Fragment的key
     */
    private FragmentTag mCurrentTag;
    /**
     * 当前Fragment
     */
    private Fragment mCurrentFragment;
    private MainActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_forum);
        mContext = this;
        user = new UserBean(mContext);
        inintView();
    }

    private void inintView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_fragment_my_forum_back);
        rl_back.setOnClickListener(this);
        tvForum = (TextView) findViewById(R.id.tv_my_forum_forum);
        tvForum.setOnClickListener(this);
        tvContent = (TextView) findViewById(R.id.tv_my_forum_content);
        tvContent.setOnClickListener(this);
        ivForum = (ImageView) findViewById(R.id.iv_my_forum_forum_wire);
        ivContent = (ImageView) findViewById(R.id.iv_my_forum_content_wire);
        mCurrentTag = FragmentTag.TAG_MY_FORUM_LIST;
        mCurrentFragment = new MyForumListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.my_forum_replace_container, mCurrentFragment,
                mCurrentTag.getTag()).show(mCurrentFragment).commit();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fragment_my_forum_back:
                finish();
                break;
            case R.id.tv_my_forum_forum:
                tvForum.setTextColor(this.getResources().getColor(R.color.purple));
                tvContent.setTextColor(this.getResources().getColor(R.color.gray4));
                ivForum.setBackgroundColor(this.getResources().getColor(R.color.purple));
                ivContent.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                index = 0;
                switchFragmentone(FragmentTag.TAG_MY_FORUM_LIST);
                break;
            case R.id.tv_my_forum_content:
                tvForum.setTextColor(this.getResources().getColor(R.color.gray4));
                tvContent.setTextColor(this.getResources().getColor(R.color.purple));
                ivForum.setBackgroundColor(this.getResources().getColor(R.color.whilte));
                ivContent.setBackgroundColor(this.getResources().getColor(R.color.purple));
                index = 1;
                switchFragmentone(FragmentTag.TAG_MY_FORUM_CONTENT);
                break;
        }
    }

    /**
     * 切换Fragment
     *
     * @param to
     *            目标Fragment
     */
    public void switchFragmentone(FragmentTag to) {
        if (to != null) {
            if (!mCurrentTag.equals(to)) {
                Fragment currentF = getSupportFragmentManager().findFragmentByTag(
                        mCurrentTag.getTag());
                Fragment toF = getSupportFragmentManager().findFragmentByTag(to.getTag());
                if (null == toF) { // 先判断是否被add过
                    try {
                        // 切换按钮动画
                        // 更新当前Fragment
                        mCurrentTag = to;
                        mCurrentFragment = toF;
                        // 未add过，使用反射新建一个Fragment并add到FragmentManager中
                        toF = (Fragment) Class.forName(to.getTag()).newInstance();
                        getSupportFragmentManager().beginTransaction().hide(currentF)
                                .add(R.id.my_forum_replace_container, toF, to.getTag()).commit(); // 隐藏当前的fragment，add下一个到Activity中
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
                    if (detailpage.equals(to)||to.equals(FragmentTag.TAG_DETAILWORK)){
                        try {
                            toF = (Fragment) Class.forName(to.getTag()).newInstance();
                            getSupportFragmentManager().beginTransaction().hide(currentF)
                                    .add(R.id.my_forum_replace_container, toF, to.getTag()).commit();
                        } catch (Exception e) {
                        }
                    }else {
                        // add过，直接hide当前，并show出目标Fragment
                        getSupportFragmentManager().beginTransaction().hide(currentF)
                                .show(toF).commit(); // 隐藏当前的fragment，显示下一个
                    }
                    // 更新当前Fragment
                    mCurrentTag = to;
                    mCurrentFragment = toF;

                }
            }
        }
    }
}
