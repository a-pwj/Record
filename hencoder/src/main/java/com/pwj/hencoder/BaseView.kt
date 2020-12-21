package com.pwj.hencoder

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * @Author:          pwj
 * @Date:            2020/8/6 10:34
 * @FileName:        BaseView
 * @Description:
 *
 * Canvas 类下的所有 draw- 打头的方法，例如 drawCircle() drawBitmap()。
 * Paint 类的几个最常用的方法。具体是：
 * Paint.setStyle(Style style) 设置绘制模式
 * Paint.setColor(int color) 设置颜色
 * Paint.setStrokeWidth(float width) 设置线条宽度
 * Paint.setTextSize(float textSize) 设置文字大小
 * Paint.setAntiAlias(boolean aa) 设置抗锯齿开关
 */
open class BaseView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    protected val paint = Paint(Paint.ANTI_ALIAS_FLAG)

}