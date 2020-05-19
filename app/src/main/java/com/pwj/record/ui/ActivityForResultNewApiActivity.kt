package com.pwj.record.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pwj.record.R
import kotlinx.android.synthetic.main.activity_for_result_new_api.*

/**
* Author:           pwj
* Date:             2020/4/15 17:34
* FileName:         ActivityForResultNewApiActivity.kt
* description:      ActivityForResultNewApiActivity.kt
*/
class ActivityForResultNewApiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_result_new_api)
        back.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra("value","I am back !"))
            finish()
        }
    }
}
