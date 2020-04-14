package com.pwj.record.view.excludeInnerText

import android.graphics.Paint
import android.text.style.LineHeightSpan
import kotlin.math.roundToInt

/**
 * Author:           pwj
 * Date:             2020/4/13 10:01
 * FileName:         ExcludeInnerLineSpaceSpan
 * description:      修改TextView的行高工具类
 */
class ExcludeInnerLineSpaceSpan(val mHeight: Int = 0) : LineHeightSpan {

    override fun chooseHeight(text: CharSequence?, start: Int, end: Int, spanstartv: Int, lineHeight: Int, fm: Paint.FontMetricsInt) {
        //获取原始行高
        val originHeight = fm.descent - fm.ascent
        if (originHeight <= 0) {
            return
        }

        //计算比例值
        val ratio = mHeight * 1.0f / originHeight
        //根据最新行高，修改descent，ascent
        fm.descent = (fm.descent * ratio).roundToInt()
        fm.ascent = fm.ascent - mHeight
    }
}