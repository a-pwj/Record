package com.pwj.record.ui.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.recyclerview.widget.RecyclerView;


/**
 * @Author: pwj
 * @Date: 2020/12/17 15:43
 * @FileName: DragScaleView
 * @Description: description
 */
public class DragRecyclerView extends RecyclerView {

    private String TAG = DragRecyclerView.class.getSimpleName();

    protected int screenWidth;
    protected int screenHeight;
    protected int lastX;
    protected int lastY;
    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;
    private int offset = 0; //可超出其父控件的偏移量

    // 初始的两个手指按下的触摸点的距离
    private int touchSlop;

    /**
     * 初始化获取屏幕宽高
     */
    protected void init() {
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        touchSlop = viewConfiguration.getScaledTouchSlop();//最小滑动值
    }

    public DragRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DragRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragRecyclerView(Context context) {
        super(context);
        init();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e(TAG, "onInterceptTouchEvent=========x========" + event.getRawX() + "=========y========" + event.getRawY());

        //点击在子控件上会走拦截
//        if (dragTouch(event)) {
//            return true;
//        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent=========x========" + event.getRawX() + "=========y========" + event.getRawY());
        if (dragTouch(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean dragTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oriLeft = getLeft();
                oriRight = getRight();
                oriTop = getTop();
                oriBottom = getBottom();
                lastY = (int) event.getRawY();
                lastX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs((int) event.getRawX() - lastX) > touchSlop || Math.abs((int) event.getRawY() - lastY) > touchSlop) {
                    delDrag(this, event, event.getAction());
                    invalidate();
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * 处理拖动事件
     *
     * @param v
     * @param event
     * @param action
     */
    protected void delDrag(View v, MotionEvent event, int action) {
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;

                //对view边界的处理，如果子view达到父控件的边界，offset代表允许超出父控件多少
                if (oriTop < -offset) {
                    oriTop = -offset;
                }
                if (oriBottom > screenHeight + offset) {
                    oriBottom = screenHeight + offset;
                }
                if (oriRight > screenWidth + offset) {
                    oriRight = screenWidth + offset;
                }
                if (oriLeft < -offset) {
                    oriLeft = -offset;
                }

                int left = oriLeft + dx;
                int top = oriTop + dy;
                int right = oriRight + dx;
                int bottom = oriBottom + dy;
                if (left < -offset) {
                    left = -offset;
                    right = left + getWidth();
                }
                if (right > screenWidth + offset) {
                    right = screenWidth + offset;
                    left = right - getWidth();
                }
                if (top < -offset) {
                    top = -offset;
                    bottom = top + getHeight();
                }
                if (bottom > screenHeight + offset) {
                    bottom = screenHeight + offset;
                    top = bottom - getHeight();
                }
                Log.d(TAG, left + "  " + top + "  " + right + "  " + bottom + "  " + dx);
                layout(left, top, right, bottom);

                oriLeft = left;
                oriRight = right;
                oriTop = top;
                oriBottom = bottom;
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }
}