package com.pwj.record.ui.surfaceview.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import com.pwj.record.ui.surfaceview.opengl.IDrawer
import kotlinx.android.synthetic.main.activity_main_open_g_l.*

class OpenGL1Activity : AppCompatActivity() {


    lateinit var drawer: IDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_open_g_l)

//        drawer = if (intent.getIntExtra("type", 0) == 0) {
//            TriangleDrawer()
//        } else {
//            BitmapDrawer(BitmapFactory.decodeResource(this, R.mipmap.ic_launcher))
//        }
//        initRender(drawer)
    }

    private fun initRender(drawer: IDrawer) {
        gl_surface.setEGLContextClientVersion(2)
//        gl_surface.setRenderer(SimpleRender(drawer))
    }

    private fun TriangleDrawer(): IDrawer {

    }

    override fun onDestroy() {
        drawer.release()
        super.onDestroy()
    }
}