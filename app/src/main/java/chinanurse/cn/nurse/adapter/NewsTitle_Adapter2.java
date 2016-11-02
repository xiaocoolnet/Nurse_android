package chinanurse.cn.nurse.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import chinanurse.cn.nurse.R;

/**
 * Created by Administrator on 2016/6/22.
 */
public class NewsTitle_Adapter2 extends PagerAdapter {
    private List<ImageView> titlevplist;
    private Activity mactivity;
    private ViewPager news_fisr_vp;


    private Context mContext;
    private DisplayImageOptions options;// 设置ImageLoder参数
    private long downIime;// 轮播图按下时间
    private long upTime;// 轮播图按下松开时间
    //解析图片第三方初始化
    private ImageLoader imageLoader;
    private LunboTask mTask;// 轮播图控制器
    private final int BANNER_PLAY_TIME = 5000;// 自动轮播间隔时长
    private final int BANNER_ANIM_TIME = 500;// 轮播切换动画的时长
    private ViewPager mViewPager;//
    private List<String>url;
    private TextView news_first_title;
    private View v;
    private ArrayList<String> imagename;

    public NewsTitle_Adapter2(Activity mactivity, List<ImageView> titlevplist, ViewPager news_fisr_vp, View view, ArrayList<String> imagename) {
        this.titlevplist = titlevplist;
        this.mactivity = mactivity;
        this.news_fisr_vp = news_fisr_vp;
        this.v = view;
        this.imagename = imagename;


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher).showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisc(true).considerExifParams(true).build();

                 ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mactivity).build();
        imageLoader = ImageLoader.getInstance();
//        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance(); //必须初始化
        imageLoader.init(config);
        news_first_title = (TextView) v.findViewById(R.id.tv_title);
        if (mTask == null) {
            mTask = new LunboTask();
            mTask.startLunbo();
        }
        setScroller();
    }
    /**
     * 轮播控制中心
     */
    public  class LunboTask extends Handler implements Runnable {
        public void startLunbo() {
            stopLunbo();// 开始轮播之前清除一下原来的消息
            postDelayed(this, BANNER_PLAY_TIME);
        }

        public void stopLunbo() {
            removeCallbacksAndMessages(null);// 清除所有消息和回调
        }

        @Override
        public void run() {
            setViewPagerItem();
            postDelayed(this, BANNER_PLAY_TIME);
        }
    }
    public void setViewPagerItem() {

        if (news_fisr_vp.getCurrentItem() == news_fisr_vp.getAdapter().getCount() - 1) {
            news_fisr_vp.setCurrentItem(0, true);
//            news_first_title.setText(imagename.get(0).toString()+"");
        } else {
            news_fisr_vp.setCurrentItem(news_fisr_vp.getCurrentItem() + 1, true);
//            news_first_title.setText(imagename.get(news_fisr_vp.getCurrentItem()).toString()+"");

        }
    }
    public void setScroller() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            MyScroller mScroller = new MyScroller(news_fisr_vp.getContext(),
                    new AccelerateInterpolator());
            mField.set(news_fisr_vp, mScroller);
        } catch (Exception ee) {
            L.d("Exception: " + ee.getMessage());
        }
    }
    class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy,
                                int duration) {
            super.startScroll(startX, startY, dx, dy, BANNER_ANIM_TIME);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, BANNER_ANIM_TIME);
        }
    }
    @Override
    public int getCount() {
        //设置成最大，使用户看不到边界
         return titlevplist.size();
//        return Integer.MAX_VALUE;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = titlevplist.get(position);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        return imageView;


//        container.addView(titlevplist.get(position));
//        return titlevplist.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        //Warning：不要在这里调用removeView
    }

//    public void setCurrentItem(int currentItem) {
//        this.currentItem = currentItem;
//    }
}
