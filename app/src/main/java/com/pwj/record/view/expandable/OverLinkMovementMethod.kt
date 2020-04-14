package com.pwj.record.view.expandable

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.widget.TextView


/**
 * Author:           pwj
 * Date:             2020/4/13 11:26
 * FileName:         OverLinkMovementMethod
 * description:      防止父布局点击事件失效
 */
class OverLinkMovementMethod : LinkMovementMethod() {

    companion object {
        @JvmStatic
        private var canScroll = false

        @Volatile
        private var sInstance: OverLinkMovementMethod? = null

        @JvmStatic
        fun getInstance(): OverLinkMovementMethod {
            if (sInstance != null) {
                sInstance =
                    OverLinkMovementMethod()
            }
            return sInstance!!
        }
    }

    override fun onTouchEvent(widget: TextView?, buffer: Spannable?, event: MotionEvent?): Boolean {
        val action = event?.action
        if (action == MotionEvent.ACTION_MOVE) {
            if (!canScroll) {
                return true
            }
        }

        return super.onTouchEvent(widget, buffer, event)
    }

//    private val FROM_BELOW: Any = Concrete()
}
