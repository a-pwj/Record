package com.pwj.record.ui.surfaceview.ui

import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGL1Activity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(GLWorld(this))

        val mGLSurfaceView = GLSurfaceView(this)
        setContentView(mGLSurfaceView)
        //设置版本
        mGLSurfaceView.setEGLContextClientVersion(3)
        mGLSurfaceView.setRenderer(ColorRenderer(Color.GRAY))
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    private fun isSupportOpenGL(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val deviceConfigurationInfo = activityManager.deviceConfigurationInfo
        val glVersion = deviceConfigurationInfo.reqGlEsVersion >= 3
        return glVersion
//        return glVersion >= 0x20000
//                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
//                && (Build.FINGERPRINT.startsWith("generic")
//                || Build.FINGERPRINT.startsWith("unknown")
//                || Build.MODEL.contains("google_sdk")
//                || Build.MODEL.contains("Emulator")
//                || Build.MODEL.contains("Android SDK built for x86")))
    }

    class ColorRenderer(val color: Int) : GLSurfaceView.Renderer {

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            //设置背景颜色
            val redF = (Color.red(color) / 255).toFloat()
            val greenF = (Color.green(color) / 255).toFloat()
            val blueF = (Color.blue(color) / 255).toFloat()
            val alphaF = (Color.alpha(color) / 255).toFloat()
            GLES30.glClearColor(redF, greenF, blueF, alphaF)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            //设置窗口
            GLES30.glViewport(0, 0, width, height)
        }

        override fun onDrawFrame(gl: GL10?) {

            GLES30.glClear(GL10.GL_COLOR_BUFFER_BIT)
        }

    }
}