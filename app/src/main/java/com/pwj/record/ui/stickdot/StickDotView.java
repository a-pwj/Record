package com.pwj.record.ui.stickdot;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.pwj.record.R;

/**
 * @Author: pwj
 * @Date: 2020/12/10 14:40
 * @FileName: StickDotView
 * @Description: description
 */
@Deprecated
public class StickDotView extends AppCompatTextView {

    private DragView mDragView;
    private float mWidth, mHeight;

    public StickDotView(Context context) {
        this(context, null);
    }

    public StickDotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View rootView = getRootView();

        float mRowX = event.getRawY();
        float mRowY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);

                int[] cLocation = new int[2];
                getLocationOnScreen(cLocation);

                if (rootView instanceof ViewGroup) {

                }


                break;


        }


        return super.onTouchEvent(event);
    }

    public class DragView extends View {
        private static final int STATE_INIT = 0;//默认静止状态
        private static final int STATE_DRAG = 1;//拖拽状态
        private static final int STATE_MOVE = 2;//移动状态
        private static final int STATE_DISMISS = 3;//消失状态

        private Path mPath;
        private Paint mPaint;
        private Bitmap mCacheBitmap;
        // 拖拽圆的圆点
        private PointF mDragPointF;
        // 固定圆的圆点
        private PointF mStickyPointF;
        // 二阶贝塞尔曲线控制点
        private PointF mControlPoint;
        // 拖拽的距离
        private float mDragDistance;
        // 最大拖拽距离
        private float mMaxDistance = dp2px(100);
        // View的宽和高
        private float mWidth, mHeight;

        // 当前红点的状态
        private int mState;

        // 固定圆的半径
        private int mStickRadius;
        // 固定小圆的默认半径
        private int mDefaultRadius = dp2px(10);
        // 拖拽圆的半径
        private int mDragRadius = dp2px(15);

        private int[] mExplodeRes = {R.drawable.explode1,
                R.drawable.explode2,
                R.drawable.explode3,
                R.drawable.explode4,
                R.drawable.explode5};

        private Bitmap[] mBitmaps;

        private int mExplodeIndex;

        public DragView(Context context) {
            this(context, null, 0);
        }

        public DragView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            mPath = new Path();

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setDither(true);
            mPaint.setColor(Color.RED);

            mDragPointF = new PointF();
            mStickyPointF = new PointF();

            mState = STATE_INIT;

            mBitmaps = new Bitmap[mExplodeRes.length];
            for (int i = 0; i < mExplodeRes.length; i++) {
                mBitmaps[i] = BitmapFactory.decodeResource(getResources(), mExplodeRes[i]);
            }
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (isInsideRange() && mState == STATE_DRAG) {
                canvas.drawCircle(mStickyPointF.x, mStickyPointF.y, mStickRadius, mPaint);

                Float link = MathUtils.getLineSlope(mDragPointF, mStickyPointF);

                // 然后通过两个圆心和半径、斜率来获得外切线的切点
                PointF[] stickPoints = MathUtils.getIntersectionPoints(mStickyPointF, mStickRadius, link);
                mDragRadius = (int) Math.min(mWidth, mHeight) / 2;
                PointF[] dragPoints = MathUtils.getIntersectionPoints(mDragPointF, mDragRadius, link);

                // 二阶贝塞尔曲线的控制点取得两圆心的中点
                mControlPoint = MathUtils.getMiddlePoint(mDragPointF, mStickyPointF);

                //绘制贝塞尔曲线
                mPath.reset();
                mPath.moveTo(stickPoints[0].x, stickPoints[0].y);
                mPath.quadTo(mControlPoint.x, mControlPoint.y, dragPoints[0].x, dragPoints[0].y);
                mPath.lineTo(dragPoints[1].x, dragPoints[1].y);
                mPath.quadTo(mControlPoint.x, mControlPoint.y, stickPoints[1].x, stickPoints[1].y);
                mPath.lineTo(stickPoints[0].x, stickPoints[0].y);

                canvas.drawPath(mPath, mPaint);
            }

            if (mCacheBitmap != null && mState != STATE_DISMISS) {
                canvas.drawBitmap(mCacheBitmap, mDragPointF.x - mWidth / 2, mDragPointF.y - mHeight / 2, mPaint);
            }

            if (mState == STATE_DISMISS && mExplodeIndex < mExplodeRes.length) {
                // 绘制小红点消失时的爆炸动画
                canvas.drawBitmap(mBitmaps[mExplodeIndex],
                        mDragPointF.x - mWidth / 2,
                        mDragPointF.y - mHeight / 2,
                        mPaint);
            }
        }


        /**
         * 设置缓存Bitmap，即 StickDotView 的视图
         *
         * @param mCacheBitmap 缓存Bitmap
         */
        public void setCacheBitmap(Bitmap mCacheBitmap) {
            this.mCacheBitmap = mCacheBitmap;
            mWidth = mCacheBitmap.getWidth();
            mHeight = mCacheBitmap.getHeight();
            mDefaultRadius = (int) Math.min(mWidth, mHeight) / 2;
        }



        /**
         * 是否在最大拖拽范围之内
         *
         * @return true 范围之内 false 范围之外
         */
        private boolean isInsideRange() {
            return mDragDistance <= mMaxDistance;
        }
    }

    private int dp2px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5f);
    }
}
