package com.pwj.record.ui

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import com.pwj.record.bean.Person
import com.pwj.record.ext.startExtActivity
import com.pwj.record.ui.lottie.LottieActivity
import com.pwj.record.ui.tools.ToolsActivity
import com.pwj.record.view.expandable.ExpandableActivity
import com.pwj.record.view.recyclerView.ParallaxImageActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 主入口
 */
class MainActivity : AppCompatActivity(R.layout.activity_main), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        code1.setOnClickListener(this)
        code2.setOnClickListener(this)
        code3.setOnClickListener(this)
        code4.setOnClickListener(this)
        code5.setOnClickListener(this)
        code6.setOnClickListener(this)
        code7.setOnClickListener(this)
        code8.setOnClickListener(this)
        code9.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.code1 -> startExtActivity<ExpandableActivity>()
            R.id.code2 -> {
                val list = ArrayList<Pair<String, Any>>().apply {
                    add(Pair("array<*>", Array<Person>(3) { i: Int -> Person(i.toString()) }))
                    add(Pair("ArrayList<*>", ArrayList<Person>().apply {
                        add(Person("10"))
                        add(Person("11"))
                        add(Person("12"))
                        add(Person("13"))
                    }))
                    add(Pair("bundle", Bundle().putString("bundle", "bundleResult")))
                }
                startExtActivity<ExtTestActivity>(values = list)
            }
            R.id.code3 -> {
                startExtActivity<ActivityResultNewApiActivity>()
            }
            R.id.code4 -> {
                startExtActivity<ViewPagerActivity>()
            }
            R.id.code5 -> {
                startExtActivity<DSLAnimActivity>()
            }
            R.id.code6 -> {
                startExtActivity<ParallaxImageActivity>()
            }
            R.id.code7 -> {
                startExtActivity<DialogActivity>()
            }
            R.id.code8 -> {
                startExtActivity<LottieActivity>()
            }
            R.id.code9 -> {
                startExtActivity<ToolsActivity>()
            }
        }
    }

    /**
     * 点击外部 输入框消失
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    v?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }

}

