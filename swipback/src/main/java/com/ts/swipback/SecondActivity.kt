package com.ts.swipback

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : BaseSlideCloseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        second.setOnClickListener { toActivity<MainActivity>(this) }
    }
}
