package com.pwj.logcat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Author: pwj
 * @Date: 2020/7/23 17:21
 * @FileName: ScrollLogcatView
 * @Description: description
 */
public class ScrollLogcatView extends android.widget.ScrollView {
    public ScrollLogcatView(Context context) {
        super(context);
    }

    public ScrollLogcatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollLogcatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollViewListener != null) {
            onScrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    private OnScrollViewListener onScrollViewListener;

    public void setOnScrollViewListener(OnScrollViewListener onScrollViewListener) {
        this.onScrollViewListener = onScrollViewListener;
    }

    public interface OnScrollViewListener {
        void onScrollChanged(ScrollLogcatView scrollView, int x, int y, int oldx, int oldy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        android.util.Log.e("hdltag", "onTouchEvent(MyScrollView.java:46):滑动了");
        return super.onTouchEvent(ev);
    }
}