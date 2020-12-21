package com.pwj.record.ui.surfaceview.sview;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.pwj.record.utils.log.ZLog;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Author: pwj
 * @Date: 2020/9/11 15:14
 * @FileName: GLWorld
 * @Description: description
 */
public class GLWorld extends GLSurfaceView implements GLSurfaceView.Renderer {
    private GLPoint glPoint;

    public GLWorld(Context context) {
        super(context,null);
    }

    public GLWorld(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置Open GL ES 版本为3.0
        setEGLContextClientVersion(3);
        //设置渲染器
        setRenderer(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        ZLog.e("onSurfaceCreated");
        //rgba
        //设置清空屏幕用的颜色；前三个参数分别对应红，绿和蓝，最后的参数对应透明度。
//        GLES30.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

        glPoint= new GLPoint();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        ZLog.e("onSurfaceChanged");
        //设置视口的尺寸，这个视口尺寸怎么理解呢，
        // 就是锁定你操作的渲染区域是哪部分，整个屏幕那就是 (0.0)点开始，宽度为widht，长度为hight咯。
        // 如果你只想渲染左半个屏幕，那就(0.0)，宽度width/2，长度为hight/2。
        // 这样设置viewport大小后，你之后的GL画图操作，都只作用这部分区域，右半屏幕是不会有任何反应的。
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        ZLog.e("onDrawFrame");

        glPoint.draw();
    }
}
