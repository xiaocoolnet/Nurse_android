package chinanurse.cn.nurse.adapter.update_newspage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/7/26.
 */
public class NewsFragmentnew_Activity extends FragmentStatePagerAdapter{
    private List<Fragment> mList;
    private FragmentManager mFragmentManager;
    private List<String> mTitles;
    private List<String> mTitlesid;

    public NewsFragmentnew_Activity(FragmentManager mf, List<Fragment> mList, List<String> mTitles,List<String> mTitlesid) {
        super(mf);
        this.mFragmentManager = mf;
        this.mList = mList;
        this.mTitles = mTitles;

    }
    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
         super.destroyItem(container, position, object);
    }
}
