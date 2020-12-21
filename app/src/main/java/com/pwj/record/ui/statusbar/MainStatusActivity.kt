package com.pwj.record.ui.statusbar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import com.pwj.record.ext.startExtActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @Author:          pwj
 * @Date:            2020-10-22 17:09:33
 * @FileName:        MainStatusActivity
 * @Description:     description
 */
class MainStatusActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_status)

        code0.setOnClickListener(this)
        code1.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.code0 -> startExtActivity<StatusImgActivity>()
            R.id.code1 -> startExtActivity<StatusViewPagerActivity>()
        }
    }
}
