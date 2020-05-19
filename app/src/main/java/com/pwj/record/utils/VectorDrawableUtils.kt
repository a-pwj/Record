package com.pwj.record.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

/**
 * @Author:          pwj
 * @Date:            2020/4/30 16:27
 * @FileName:        VectorDrawableUtils
 * @Description:     动态改变SVG的颜色
 */
object VectorDrawableUtils {

    /**
     * 给imageView 的svg初始化颜色
     */
    fun setVectorDrawableResColor(context: Context, @DrawableRes imgRes: Int, @ColorRes colors: Int): Drawable? {
        //利用ContextCompat工具类获取drawable图片资源
        val drawable = ContextCompat.getDrawable(context, imgRes) ?: return null
        //简单的使用tint改变drawable颜色
        return tintDrawable(drawable, ContextCompat.getColor(context, colors))
    }

    /**
     * 给drawable上色
     */
    private fun tintDrawable(drawable: Drawable, colors: Int): Drawable {
        val wrap = DrawableCompat.wrap(drawable).mutate()
        DrawableCompat.setTint(wrap, colors)
        return wrap
    }
}