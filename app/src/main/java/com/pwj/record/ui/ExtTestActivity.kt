package com.pwj.record.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import com.pwj.record.bean.Person
import kotlinx.android.synthetic.main.activity_ext_test.*

/**
* @FileName:         ${NAME}
* @Date:             ${DATE} ${TIME}
* @Author:           ${USER}
*/
class ExtTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ext_test)
        intent?.let {
            intent.getParcelableArrayExtra("array<*>")?.let { arrayButton.text = it.joinToString() }
            intent.getParcelableArrayListExtra<Person>("ArrayList<*>")?.let {
                arrayListButton.text = it.joinToString()
            }
            intent.getBundleExtra("bundle")?.let { bundleButton.text = it.getCharSequence("bundle") }
        }
    }
}
