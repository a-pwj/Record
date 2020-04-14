package com.ts.uidemo.bezier

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ts.uidemo.R
import com.ts.uidemo.toActivity

class MainBezierActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bezier_main)
    }

    fun drawCircle(view: View?) {
        toActivity<CircleActivity>(this)
    }

    fun showBezierPlay(view: View?) {
        startActivity(Intent(this, BezierActivity::class.java))
    }

    fun diy(view: View?) {
        startActivity(Intent(this, DIYBezierActivity::class.java))
    }

    fun stickPoint(view: View?) {
        startActivity(Intent(this, StickDotActivity::class.java))
    }

    fun changeToHeart(view: View?) {
        startActivity(Intent(this, HeartActivity::class.java))
    }

}
