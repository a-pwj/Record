package com.pwj.record.ui.tools.webp

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @Author:          pwj
 * @Date:            2020/7/3 11:38
 * @FileName:        AspectRatioImageView
 * @Description:     description
 */
class AspectRatioImageView @JvmOverloads constructor(context: Context,  attrs: AttributeSet? = null,  defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    private var mAspectRatio = 1.0f

    fun getAspectRatio(): Float {
        return mAspectRatio
    }

    fun setAspectRatio(aspectRatio: Float) {
        mAspectRatio = aspectRatio
        requestLayout()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width= MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    }
}