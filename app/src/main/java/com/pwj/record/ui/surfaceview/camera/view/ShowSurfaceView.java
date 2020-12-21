package com.pwj.record.ui.surfaceview.camera.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @Author: pwj
 * @Date: 2020/9/28 13:50
 * @FileName: ShowSurfaceView
 * @Description: <p>
 * 首先要继承SurfaceView，实现SurfaceHolder.Callback接口。
 * <p>
 * 重写方法：
 * surfaceChanged：surface大小或格式发生变化时触发，在surfaceCreated调用后该函数至少会被调用一次。
 * surfaceCreated：Surface创建时触发，一般在这个函数开启绘图线程（新的线程，不要再这个线程中绘制Surface）。
 * surfaceDestroyed：销毁时触发，一般不可见时就会销毁。
 * <p>
 * 利用getHolder()获取SurfaceHolder对象，调用SurfaceHolder.addCallback添加回调
 * <p>
 * SurfaceHolder.lockCanvas 获取Canvas对象并锁定画布，调用Canvas绘图，SurfaceHolder.unlockCanvasAndPost 结束锁定画布，提交改变。
 */
public class ShowSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mSurfaceHolder;

    /**
     * 绘图的Canvas
     */
    private Canvas mCanvas;
    /**
     * 子线程标志位
     */
    private boolean mIsDrawing;
    private Bitmap bitmap;
    private Paint mPaint;


    public ShowSurfaceView(Context context) {
        this(context, null);
    }

    public ShowSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        //开启子线程
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //改变
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            drawSomething();
        }
    }

    private void drawSomething() {
        try {
            //获得canvas对象
            mCanvas = mSurfaceHolder.lockCanvas();
//            Bitmap bitmap1= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            if (bitmap != null) {
                mCanvas.drawBitmap(bitmap, 0, 0, mPaint);
            }
            //绘图
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                //释放canvas对象并提交画布
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
