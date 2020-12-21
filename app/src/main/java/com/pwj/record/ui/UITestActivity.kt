package com.pwj.record.ui

import android.graphics.Path
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import com.pwj.record.ui.stickdot.PathView
import kotlinx.android.synthetic.main.activity_u_i_test.*


/**
 * @Author:          pwj
 * @Date:            2020-11-17 14:35:11
 * @FileName:        UITestActivity
 * @Description:     description
 */
class UITestActivity : AppCompatActivity() {

    private lateinit var mPathViews: Array<PathView?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_u_i_test)
        button.setOnClickListener { play() }
    }

    override fun onResume() {
        super.onResume()
//        Handler(Looper.getMainLooper()).postDelayed({
//            pathView.startAnimation()
//        }, 200)
    }


    fun draw(view: View?) {
        mCanvasView.clear()
        mCanvasView.setVisibility(View.VISIBLE)
        stopAnimations()
    }


    fun play() {
        stopAnimations()
        val paths: Array<Path> = mCanvasView.getPaths()
        if (paths.size > 0) {
            mCanvasView.setVisibility(View.INVISIBLE)
            mPathViews = arrayOfNulls<PathView>(paths.size)
            for (i in paths.indices) {
                val path = paths[i]
                val pathView = PathView(this)
                //设置线宽
                pathView.setLineWidth(10f)
                //动画时长
//                pathView.setDuration(mDuration)
////                //动画模式
//                pathView.setMode(mMode)
                //设置路径
                pathView.setPath(path)
                //重复播放
//                pathView.setRepeat(isRepeat)
                mPathViews[i] = pathView
                mContainer.addView(pathView)
            }
            for (pathView in mPathViews) {
                pathView?.start()
            }
        }
    }

    private fun stopAnimations() {
//        if (mPathViews != null) {
//            for (tmp in mPathViews) {
//                if (tmp != null) {
//                    tmp.stop()
//                    mContainer.removeView(tmp)
//                }
//            }
//        }
    }
}
