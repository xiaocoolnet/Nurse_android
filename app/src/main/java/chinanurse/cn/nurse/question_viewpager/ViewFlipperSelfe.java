package chinanurse.cn.nurse.question_viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

import chinanurse.cn.nurse.R;
import chinanurse.cn.nurse.question_lintener.MyGestureListener;

/**
 * 自定义ViewFlipper视图
 * Created by Administrator on 2016/6/30.
 */
public class ViewFlipperSelfe extends ViewFlipper implements MyGestureListener.OnFlingListener {
    private GestureDetector mGestureDetector = null;

    private OnViewFlipperListener mOnViewFlipperListener = null;

    public ViewFlipperSelfe(Context context) {
        super(context);
    }

    public ViewFlipperSelfe(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnViewFlipperListener(OnViewFlipperListener mOnViewFlipperListener) {
        this.mOnViewFlipperListener = mOnViewFlipperListener;
        MyGestureListener myGestureListener = new MyGestureListener();
        myGestureListener.setOnFlingListener(this);
        mGestureDetector = new GestureDetector(myGestureListener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (null != mGestureDetector) {
            return mGestureDetector.onTouchEvent(ev);
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public void flingToNext() {
        if (null != mOnViewFlipperListener) {
            int childCnt = getChildCount();
            if (childCnt == 2) {
                removeViewAt(1);
            }
            addView(mOnViewFlipperListener.getNextView(), 0);
            if (0 != childCnt) {
                setInAnimation(getContext(), R.anim.left_slip_in);
                setOutAnimation(getContext(), R.anim.left_slip_out);
                setDisplayedChild(0);
            }
        }
    }

    @Override
    public void flingToPrevious() {
        if (null != mOnViewFlipperListener) {
            int childCnt = getChildCount();
            if (childCnt == 2) {
                removeViewAt(1);
            }
            addView(mOnViewFlipperListener.getPreviousView(), 0);
            if (0 != childCnt) {
                setInAnimation(getContext(), R.anim.right_slip_in);
                setOutAnimation(getContext(), R.anim.right_slip_out);
                setDisplayedChild(0);
            }
        }
    }

    public interface OnViewFlipperListener {
        View getNextView();

        View getPreviousView();
    }
}
