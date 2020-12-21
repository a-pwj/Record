package com.pwj.record.ioc

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import com.pwj.record.ext.showToast

/**
 * @Author:          pwj
 * @Date:            2020/6/29 9:54
 * @FileName:        ToolsIocActivity.kt
 * @Description:     ToolsIocActivity.kt
 */
@ContentView(value = R.layout.activity_tools_ioc)
class ToolsIocActivity : AppCompatActivity(R.layout.activity_tools_ioc), View.OnClickListener {

    @ViewInject(R.id.btn1)
    private val mBtn1: Button? = null

    @ViewInject(R.id.btn2)
    private val mBtn2: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewInjectUtils.inject(this)

//        mBtn1?.setOnClickListener(this)
//        mBtn2?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn1 -> showToast("Why do you click me ?")
            R.id.btn2 -> showToast("I am sleeping !!!")
        }
    }

    @OnClick(R.id.btn1, R.id.btn2)
    fun clickBtnInvoked(view: View) {
        when (view!!.id) {
            R.id.btn1 -> showToast("Why do you click me ?")
            R.id.btn2 -> showToast("I am sleeping !!!")
        }
    }

}