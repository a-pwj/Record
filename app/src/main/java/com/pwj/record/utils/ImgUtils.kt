package com.pwj.record.utils

import android.content.Context
import android.icu.text.UFormat
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * @Author:          pwj
 * @Date:            2020/5/7 10:43
 * @FileName:        ImgUtils
 * @Description:     description
 */
object ImgUtils {



    fun getConstantState(view: ImageView, context: Context, @DrawableRes res: Int): Boolean {
        val constantState = view.drawable.current.constantState
        val resConstantState = ContextCompat.getDrawable(context, res)?.constantState
        return constantState ==resConstantState
    }

}