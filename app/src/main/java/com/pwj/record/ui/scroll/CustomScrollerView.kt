package com.pwj.record.ui.scroll

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import com.pwj.record.utils.log.ZLog

/**
 * @Author:          pwj
 * @Date:            2020/11/16 14:07
 * @FileName:        CustomScrollerView
 * @Description:     description
 */
class CustomScrollerView @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, def: Int = 0) : View(context, attributes, def) {


    private var lastX: Int = 0
    private var lastY: Int = 0
    private val mScroller by lazy { Scroller(context) }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.getX().toInt()
        val y = event.getY().toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
                ZLog.d("lastx=${lastX},lastY=${lastX}")
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX = x - lastX
                val offsetY = y - lastY
//                layout(left+offsetX,top+offsetY,right+offsetX,bottom+offsetY)
                ZLog.d("offsetX=${offsetX},offsetX=${offsetX}")
//                (parent as View).scrollTo(-offsetX, -offsetY)
            }
        }

        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            (parent as View).scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }

    fun smoothScrollTo(destX: Int, destY: Int) {
        mScroller.startScroll(scrollX, scrollY, destX - scrollX, destY - scrollY, 2000)
        invalidate()
    }
}