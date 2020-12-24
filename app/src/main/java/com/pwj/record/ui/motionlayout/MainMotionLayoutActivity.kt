package com.pwj.record.ui.motionlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pwj.record.R
import kotlinx.android.synthetic.main.activity_main_motion_layout.*

/**
 * @Author:          pwj
 * @Date:            2020-12-23 15:10:06
 * @FileName:        MainMotionLayoutActivity
 * @Description:     description
 */
class MainMotionLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_motion_layout)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, fragment)
            .commitAllowingStateLoss()
    }


}
