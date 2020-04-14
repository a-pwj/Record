package com.ts.uidemo.bezier.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import java.util.*
import kotlin.math.pow
import kotlin.math.sin

/**
 * Author:           pwj
 * Date:             2020/4/1 11:45
 * FileName:         HeartView
 */
class HeartView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : BaseView(context, attributeSet, defStyleAttr) {

    private lateinit var mPath: Path
    private lateinit var mPaint: Paint

    private lateinit var mHeartPointList: MutableList<PointF>
    private lateinit var mCirclePointList: MutableList<PointF>
    private lateinit var mCurPointList: MutableList<PointF>

    private lateinit var mAnimator: ValueAnimator

    override fun initView(context: Context) {
        mPaint = Paint()
        mPaint.apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.FILL
        }

        mPath = Path()

        mHeartPointList = mutableListOf()
        mHeartPointList.add(PointF(0f, dpToPx(-38f).toFloat()))
        mHeartPointList.add(PointF(dpToPx(50f).toFloat(), dpToPx(-103f).toFloat()))
        mHeartPointList.add(PointF(dpToPx(112f).toFloat(), dpToPx(-61f).toFloat()))
        mHeartPointList.add(PointF(dpToPx(112f).toFloat(), dpToPx(-12f).toFloat()))
        mHeartPointList.add(PointF(dpToPx(112f).toFloat(), dpToPx(37f).toFloat()))
        mHeartPointList.add(PointF(dpToPx(51f).toFloat(), dpToPx(90f).toFloat()))
        mHeartPointList.add(PointF(0f, dpToPx(129f).toFloat()))
        mHeartPointList.add(PointF(dpToPx(-51f).toFloat(), dpToPx(90f).toFloat()))
        mHeartPointList.add(PointF(dpToPx(-112f).toFloat(), dpToPx(37f).toFloat()))
        mHeartPointList.add(PointF(dpToPx(-112f).toFloat(), dpToPx(-12f).toFloat()))
        mHeartPointList.add(PointF(dpToPx(-112f).toFloat(), dpToPx(-61f).toFloat()))
        mHeartPointList.add(PointF(dpToPx(-50f).toFloat(), dpToPx(-103f).toFloat()))


        mCirclePointList = ArrayList()
        mCirclePointList.add(PointF(0f, dpToPx(-89f).toFloat()))
        mCirclePointList.add(PointF(dpToPx(50f).toFloat(), dpToPx(-89f).toFloat()))
        mCirclePointList.add(PointF(dpToPx(90f).toFloat(), dpToPx(-49f).toFloat()))
        mCirclePointList.add(PointF(dpToPx(90f).toFloat(), 0f))
        mCirclePointList.add(PointF(dpToPx(90f).toFloat(), dpToPx(50f).toFloat()))
        mCirclePointList.add(PointF(dpToPx(50f).toFloat(), dpToPx(90f).toFloat()))
        mCirclePointList.add(PointF(0f, dpToPx(90f).toFloat()))
        mCirclePointList.add(PointF(dpToPx(-49f).toFloat(), dpToPx(90f).toFloat()))
        mCirclePointList.add(PointF(dpToPx(-89f).toFloat(), dpToPx(50f).toFloat()))
        mCirclePointList.add(PointF(dpToPx(-89f).toFloat(), 0f))
        mCirclePointList.add(PointF(dpToPx(-89f).toFloat(), dpToPx(-49f).toFloat()))
        mCirclePointList.add(PointF(dpToPx(-49f).toFloat(), dpToPx(-89f).toFloat()))

        mCurPointList = ArrayList()
        mCurPointList.add(PointF(0f, dpToPx(-89).toFloat()))
        mCurPointList.add(PointF(dpToPx(50).toFloat(), dpToPx(-89).toFloat()))
        mCurPointList.add(PointF(dpToPx(90).toFloat(), dpToPx(-49).toFloat()))
        mCurPointList.add(PointF(dpToPx(90).toFloat(), 0f))
        mCurPointList.add(PointF(dpToPx(90).toFloat(), dpToPx(50).toFloat()))
        mCurPointList.add(PointF(dpToPx(50).toFloat(), dpToPx(90).toFloat()))
        mCurPointList.add(PointF(0f, dpToPx(90).toFloat()))
        mCurPointList.add(PointF(dpToPx(-49).toFloat(), dpToPx(90).toFloat()))
        mCurPointList.add(PointF(dpToPx(-89).toFloat(), dpToPx(50).toFloat()))
        mCurPointList.add(PointF(dpToPx(-89).toFloat(), 0f))
        mCurPointList.add(PointF(dpToPx(-89).toFloat(), dpToPx(-49).toFloat()))
        mCurPointList.add(PointF(dpToPx(-49).toFloat(), dpToPx(-89).toFloat()))

        mAnimator = ValueAnimator.ofFloat(0f, 1f)
        mAnimator.duration = 3000
        mAnimator.addUpdateListener {
            val x: Float = it.animatedValue as Float
            val factor = 0.15f
            val value = 2.0.pow(-10 * x.toDouble()) * sin((x - factor / 4) * (2 * Math.PI) / factor) + 1

            for (i in mCurPointList.indices) {
                val startPointF = mCirclePointList[i]
                val endPointF = mHeartPointList[i]

                mCurPointList[i].x = startPointF.x + (endPointF.x - startPointF.x) * value.toFloat()
                mCurPointList[i].y = startPointF.y + (endPointF.y - startPointF.y) * value.toFloat()
            }

            postInvalidate()
        }
    }

}