package com.pwj.hencoder.level1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @Author:          pwj
 * @Date:            2020/8/6 10:33
 * @FileName:        View1
 * @Description:     description
 */
class View1 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var actionFlag = -1
    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (actionFlag) {
            MotionEvent.ACTION_DOWN -> {
                paint.color = Color.RED
            }
            MotionEvent.ACTION_UP->{
                paint.color = Color.BLUE
            }
            MotionEvent.ACTION_MOVE->{
                paint.color = Color.DKGRAY
            }
        }
        canvas.drawCircle(300F, 300F, 200F, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                actionFlag = MotionEvent.ACTION_DOWN
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP->{
                actionFlag = MotionEvent.ACTION_UP
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE->{
                actionFlag = MotionEvent.ACTION_MOVE
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}