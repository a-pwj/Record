package com.pwj.record.ui.surfaceview.render

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.pwj.record.ui.surfaceview.opengl.IDrawer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @Author:          pwj
 * @Date:            2021/1/13 15:29
 * @FileName:        SimpleRender
 * @Description:     description
 */
class SimpleRender(private val mDrawer: IDrawer) : GLSurfaceView.Renderer {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
//        mDrawer.setTextureID(openGt)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

    }

    override fun onDrawFrame(gl: GL10?) {

    }
}