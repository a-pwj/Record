package com.pwj.record.ui.box

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.pwj.record.R

/**
 * @Author:          pwj
 * @Date:            2020/11/17 14:36
 * @FileName:        RectView
 * @Description:     description
 */
class RectView @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, def: Int = 0) : View(context, attributes, def) {

    private val mPaintBg by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.YELLOW
            strokeWidth = 1.5F
        }
    }
    private val mPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            strokeWidth = 1.5F
        }
    }


    private var mColor: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attributes, R.styleable.RectView)
        mColor = typedArray.getColor(R.styleable.RectView_rect_color, Color.RED)
        typedArray.recycle()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {

            setMeasuredDimension(600, 600)
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(600, heightSpecSize)
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 600)
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val right = width - paddingRight
        val bottom = height - paddingBottom

        if (mColor != 0) {
            mPaint.color = mColor
        }

        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), mPaintBg)
        canvas.drawRect(paddingLeft.toFloat(), paddingTop.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
    }


}