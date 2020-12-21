package com.pwj.record.ui.stickdot;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.pwj.record.utils.log.ZLog;

/**
 * @Author: pwj
 * @Date: 2020/12/11 14:33
 * @FileName: CircleLoadingView
 * @Description: description
 */
public class CircleLoadingView extends View {

    private Path path;

    private Paint paint;

    private int mWidth;

    private int mHeight;

    private PathMeasure pathMeasure;

    private float curValue;
    private Path loadedPath;
    private float length;

    private Path destPath;

    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        path = new Path();

        loadedPath = new Path();

        destPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        path.addCircle(mWidth / 2, mHeight / 2, 60F, Path.Direction.CW);
        pathMeasure = new PathMeasure(path, false);
        length = pathMeasure.getLength();

        initAnimator();
    }

    private void initAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(2000);
        animator.setRepeatCount(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curValue = (float) animator.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        destPath.reset();
        destPath.lineTo(0, 0);
        canvas.rotate(360.0F * curValue - 45f, mWidth / 2, mHeight / 2);
        float stop = curValue * length;
        float start = (float) (stop - (0.5 - Math.abs(curValue - 0.5)) * length);

        Log.e("rotate degress ===",(360.0F * curValue - 45f)+"");

        pathMeasure.getSegment(start, stop, destPath, true);
        canvas.drawPath(destPath, paint);


    }
}
