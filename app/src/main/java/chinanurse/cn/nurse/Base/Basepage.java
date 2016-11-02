package chinanurse.cn.nurse.Base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Administrator on 2016/6/3.
 */
public abstract class Basepage extends Fragment{
    public Activity mactivity;
    public View mRootView;
    public Basepage(Activity activity){
        mactivity=activity;
        mRootView=initView();
    }

    public abstract View initView();

    public abstract void initData();
}
