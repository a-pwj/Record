package com.ts.uidemo.bezier.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.ts.lib.base.UIUtils

/**
 * Author:           pwj
 * Date:             2020/3/31 15:06
 * FileName:         BaseView
 */
abstract class BaseView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private lateinit var mCoordinatePoint: Paint
    private lateinit var mGridPoint: Paint
    private lateinit var mTextPaint: Paint

    private var mCoordinateColor: Int = 0
    private var mGridColor: Int = 0

    private var mGridWidth = 50

    private val mCoordinateLineWidth = 2.5f
    private val mGridLineWidth = 1f

    private var mTextSize = 15

    private var mCoordinateFlagHeight = 8f

    protected var mWidth = 0f
    protected var mHeight = 0f


    init {
        initCoordinate(context)
        initView(context)
    }

    /**
    --> 构造View()
    --> onFinishInflate()
    --> onAttachedToWindow()
    --> onMeasure()
    --> onSizeChanged()
    --> onLayout()
    --> onDraw()
    --> onWindowFocusChanged()
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()
    }

    protected abstract fun initView(context: Context)

    private fun initCoordinate(context: Context) {
        mCoordinateColor = Color.BLACK
        mGridColor = Color.LTGRAY

        mTextSize = UIUtils.sp2px(context, 10F)

        mCoordinatePoint = Paint()
        mCoordinatePoint.run {
            isAntiAlias = true
            color = mCoordinateColor
            strokeWidth = mCoordinateLineWidth
        }

        mGridPoint = Paint()
        mGridPoint.run {
            isAntiAlias = true
            color = mGridColor
            strokeWidth = mGridLineWidth
        }

        mTextPaint = Paint()
        mTextPaint.run {
            isAntiAlias = true
            color = mCoordinateColor
            textAlign = Paint.Align.CENTER
            textSize = mTextSize.toFloat()
        }
    }

    protected fun drawCoordinate(canvas: Canvas) {
        val halfWidth = mWidth / 2
        val halfHeight = mHeight / 2

        canvas.save()
        canvas.translate(halfWidth, halfHeight)//移动坐标轴到中心
        var curWidth: Float = mGridWidth.toFloat()

        while (curWidth < halfWidth + mGridWidth) {
            canvas.drawLine(curWidth, -halfHeight, curWidth, halfHeight, mGridPoint)    // 向右画
            canvas.drawLine(-curWidth, -halfHeight, -curWidth, halfHeight, mGridPoint)   // 向左画

            canvas.drawLine(curWidth, 0.toFloat(), curWidth, -mCoordinateFlagHeight, mCoordinatePoint)
            canvas.drawLine(-curWidth, 0.toFloat(), -curWidth, -mCoordinateFlagHeight, mCoordinatePoint)

            if (curWidth.toInt() % (mGridWidth * 2) == 0) {
                canvas.drawText(curWidth.toString(), curWidth, mTextSize * 1.5f, mTextPaint)
                canvas.drawText((-curWidth).toString(), -curWidth, mTextSize * 1.5f, mTextPaint)
            }

            curWidth += mGridWidth
        }

        var curHeight: Float = mGridWidth.toFloat()
        while (curHeight < halfHeight + mGridWidth) {
            canvas.drawLine(-halfWidth, curHeight, halfWidth, curHeight, mGridPoint)
            canvas.drawLine(-halfWidth, -curHeight, halfWidth, -curHeight, mGridPoint)

            canvas.drawLine(-mCoordinateLineWidth, curHeight, 0.toFloat(), curHeight, mCoordinatePoint)
            canvas.drawLine(-mCoordinateLineWidth, -curHeight, 0.toFloat(), -curHeight, mCoordinatePoint)

            // 标柱宽度（每两个画一个）
            if (curHeight.toInt() % (mGridWidth * 2) == 0) {
                canvas.drawText(curHeight.toString(), (-mTextSize * 2).toFloat(), curHeight + mTextSize / 2, mTextPaint)
                canvas.drawText((-curHeight).toString(), (-mTextSize * 2).toFloat(), -curHeight + mTextSize / 2, mTextPaint)
            }
            curHeight += mGridWidth
        }
        canvas.restore()

        canvas.drawLine(halfWidth, 0.toFloat(), halfWidth, mHeight, mCoordinatePoint)
        canvas.drawLine(0.toFloat(), halfHeight, mWidth, halfHeight, mCoordinatePoint)
    }

    /**
     * 转换 sp 至 px
     *
     * @param spValue sp值
     * @return px值
     */
    protected open fun spToPx(spValue: Any): Int {
        val sp = when (spValue) {
            is Float -> spValue
            is Int -> spValue.toFloat()
            else -> throw  IllegalStateException("Type not supported")
        }
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return (sp * fontScale + 0.5f).toInt()

    }

    /**
     * 转换 dp 至 px
     *
     * @param dpValue dp值
     * @return px值
     */
    open fun dpToPx(dpValue: Any): Int {
        val dp = when (dpValue) {
            is Float -> dpValue
            is Int -> dpValue.toFloat()
            else -> throw  IllegalStateException("Type not supported")
        }
        val metrics = Resources.getSystem().displayMetrics
        return (dp * metrics.density + 0.5f).toInt()
    }


}
