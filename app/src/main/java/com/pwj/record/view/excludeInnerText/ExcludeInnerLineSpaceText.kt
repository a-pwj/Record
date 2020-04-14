package com.pwj.record.view.excludeInnerText

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Author:           pwj
 * Date:             2020/4/13 9:55
 * FileName:         ExcludeInnerLineSpaceText
 * description:      自定义行高的TextView，适配不用机型
 */
class ExcludeInnerLineSpaceText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    fun setExcludeInnerLineText(text: CharSequence?) {
        text?.let {
            //获取视觉定义的每行文字的行高
            val lineHeight = textSize.toInt()

            val ssb: SpannableStringBuilder = if (text is SpannableStringBuilder) text else SpannableStringBuilder(text)

            //设置LineHeightSpan
            ssb.setSpan(ExcludeInnerLineSpaceSpan(lineHeight), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            //调用系统setText()方法
            setText(ssb)
        }
    }

}