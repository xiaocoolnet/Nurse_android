package chinanurse.cn.nurse.adapter.AbroadFragmentadapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class AbroadFragmentadapter extends PagerAdapter{
    private List<Fragment> abroadfragment;
    private Activity mactivity;

    public AbroadFragmentadapter(List<Fragment> abroadfragment, Activity mactivity) {
        this.abroadfragment = abroadfragment;
        this.mactivity = mactivity;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
