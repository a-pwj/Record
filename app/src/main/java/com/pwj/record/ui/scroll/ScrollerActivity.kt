package com.pwj.record.ui.scroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import kotlinx.android.synthetic.main.activity_scroller.*

/**
 * @Author:          pwj
 * @Date:            2020-11-16 14:06:55
 * @FileName:        ScrollerActivity
 * @Description:     description
 */
class ScrollerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroller)
        button.setOnClickListener {
            customScrollerView.smoothScrollTo(-300, -300)
        }
    }
}
