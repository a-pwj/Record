package com.pwj.record.ui.tools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pwj.record.R
import kotlinx.android.synthetic.main.activity_stetho.*

/**
 * @Author:          pwj
 * @Date:            2020-5-20 14:18:53
 * @FileName:        StethoActivity
 * @Description:
 *   Application onCreate()
 *   Stetho.initializeWithDefaults(this)
 *
 */
class StethoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stetho)
    }
}
