package com.ts.uidemo.bezier

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.ts.uidemo.R
import kotlinx.android.synthetic.main.activity_circle.*
import java.lang.String

class CircleActivity : AppCompatActivity() {

    private val MAX = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle)

//        ratio_seek_bar.max = MAX
//        circle_bezier_view.setRatio(0.55f)
//        tv_ratio.text = String.format(getString(R.string.ratio), "0.55")
//        ratio_seek_bar.progress = 55
//
//        ratio_seek_bar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                val r = progress / MAX as Float
//                tv_ratio.text = String.format(getString(R.string.ratio), "" + r)
//                circle_bezier_view.setRatio(r)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar) {}
//        })
    }
}
