package com.ts.uidemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ts.uidemo.bezier.MainBezierActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_code1_bezier.setOnClickListener(this)
        tv_code2_pathmeasure.setOnClickListener(this)
        tv_code3_anim.setOnClickListener(this)
        tv_code4_xfermode.setOnClickListener(this)
        tv_code5_scroller_velocityTracker.setOnClickListener(this)
        tv_code6_draw_flow.setOnClickListener(this)
        tv_code7_svg.setOnClickListener(this)
        tv_code8_canvas_clip.setOnClickListener(this)
        tv_code8_canvas_draw.setOnClickListener(this)
        tv_code8_canvas_text.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_code1_bezier -> toActivity<MainBezierActivity>(this)
//            R.id.tv_code2_pathmeasure -> startActivity(
//                Intent(
//                    this,
//                    com.zinc.pathmeasure.activity.ClientActivity::class.java
//                )
//            )
//            R.id.tv_code3_anim -> startActivity(
//                Intent(
//                    this,
//                    com.zinc.animation.activity.ClientActivity::class.java
//                )
//            )
//            R.id.tv_code4_xfermode -> startActivity(
//                Intent(
//                    this,
//                    com.zinc.xfermode.ClientActivity::class.java
//                )
//            )
//            R.id.tv_code5_scroller_velocityTracker -> startActivity(
//                Intent(
//                    this,
//                    com.zinc.velocitytracker_scroller.BarActivity::class.java
//                )
//            )
//            R.id.tv_code6_draw_flow -> startActivity(
//                Intent(
//                    this,
//                    com.zinc.flowlayout.ClientActivity::class.java
//                )
//            )
//            R.id.tv_code7_svg -> startActivity(
//                Intent(
//                    this,
//                    com.zinc.svg.ClientActivity::class.java
//                )
//            )
//            R.id.tv_code8_canvas_clip -> startActivity(
//                Intent(
//                    this,
//                    com.zinc.code8_canvas_clip.activity.ClientActivity::class.java
//                )
//            )
//            R.id.tv_code8_canvas_draw -> startActivity(
//                Intent(
//                    this,
//                    com.zinc.code8_canvas_draw.activity.ClientActivity::class.java
//                )
//            )
//            R.id.tv_code8_canvas_text -> startActivity(
//                Intent(
//                    this,
//                    com.zinc.code8_canvas_text_paint.ClientActivity::class.java
//                )
//            )
        }
    }
}