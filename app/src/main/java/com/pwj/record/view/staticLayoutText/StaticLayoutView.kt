package com.pwj.record.view.staticLayoutText

import android.content.Context
import android.graphics.Canvas
import android.text.Layout
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

/**
 * Author:           pwj
 * Date:             2020/4/13 10:36
 * FileName:         StaticLayoutView
 * description:      StaticLayout out
 */
class StaticLayoutView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var layout: Layout? = null

    private var mWidth: Int by Delegates.notNull<Int>()
    private var mHeight: Int by Delegates.notNull<Int>()

     fun setLayout(layout: Layout) {
        this.layout = layout
         if (width!=mWidth||height!=mHeight){
             mWidth =width
             mHeight = height
             requestLayout()
         }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.save()
        layout?.let { it.draw(canvas,null,null,0)  }
        canvas?.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (layout!=null){
            setMeasuredDimension(width,height)
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

}