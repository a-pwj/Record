package com.ts.uidemo.bezier.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

/**
 * Author:           pwj
 * Date:             2020/3/31 16:42
 * FileName:         CirlceBezierView
 */
class CirlceBezierView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : BaseView(context, attributeSet, defStyleAttr) {

    private lateinit var mCenterPoint: PointF
    private var mRadius: Float = 0f
    private lateinit var mControlPointList: MutableList<PointF>
    private var mRadio: Float = 0f
    private lateinit var mPath: Path

    private lateinit var mPaint: Paint
    private lateinit var mCirclrPaint: Paint
    private lateinit var mLinePaint: Paint

    private lateinit var mLineColor: IntArray
    private var LINE_WIDTH = 0f//线宽

    /**
     * 设置比例
     *
     * @param ratio 比例，0-1
     */
    fun setRatio(ratio: Float) {
        this.mRadio = ratio
        calculateControlPoint()
        invalidate()
    }


    override fun initView(context: Context) {
        val width = context.resources.displayMetrics.widthPixels
        mRadius = (width / 3).toFloat()

        LINE_WIDTH = dpToPx(2f).toFloat()
        mCenterPoint = PointF(0f, 0f)
        mControlPointList = mutableListOf()

        mPath = Path()
        mPaint = Paint()
        mPaint.run {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.parseColor("#1296db")
        }
        mCirclrPaint = Paint()
        mCirclrPaint.run {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = LINE_WIDTH
            color = Color.RED
        }

        mRadio = 0.55f

        mLineColor = IntArray(4)
        mLineColor[0] = Color.parseColor("#f4ea2a") //黄色
        mLineColor[1] = Color.parseColor("#1afa29") //绿色
        mLineColor[2] = Color.parseColor("#efb336") //橙色
        mLineColor[3] = Color.parseColor("#e89abe") //粉色

        mLinePaint = Paint()
        mLinePaint.run {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = dpToPx(2f).toFloat()
        }
        calculateControlPoint()
    }

    /**
     * 计算⚪的控制点
     */
    private fun calculateControlPoint() {
        val controlWidth = mRadio * mRadius

        mControlPointList.clear()
        //右上
        mControlPointList.add(PointF(0f, -mRadius))
        mControlPointList.add(PointF(controlWidth, -mRadius))
        mControlPointList.add(PointF(mRadius, -controlWidth))

        // 右下
        mControlPointList.add(PointF(mRadius, 0f))
        mControlPointList.add(PointF(mRadius, controlWidth))
        mControlPointList.add(PointF(controlWidth, mRadius))

        // 左下
        mControlPointList.add(PointF(0f, mRadius))
        mControlPointList.add(PointF(-controlWidth, mRadius))
        mControlPointList.add(PointF(-mRadius, controlWidth))

        // 左上
        mControlPointList.add(PointF(-mRadius, 0f))
        mControlPointList.add(PointF(-mRadius, -controlWidth))
        mControlPointList.add(PointF(-controlWidth, -mRadius))

    }

    override fun onDraw(canvas: Canvas) {
        drawCoordinate(canvas)

        canvas.translate(mWidth / 2, mHeight / 2)
        mPath.reset()

        for (i in 0..4) {
            if (i == 0) {
                mPath.moveTo(mControlPointList[i * 3].x, mControlPointList[i * 3].y)
            } else {
                mPath.lineTo(mControlPointList[i * 3].x, mControlPointList[i * 3].y)
            }

            var endPointIndex = if (i == 3) 0 else i * 3 + 3
            mPath.cubicTo(
                mControlPointList[i * 3 + 1].x, mControlPointList[i * 3 + 1].y,
                mControlPointList[i * 3 + 2].x, mControlPointList[i * 3 + 2].y,
                mControlPointList[endPointIndex].x, mControlPointList[endPointIndex].y
            )
        }

        // 绘制贝塞尔曲线
        canvas.drawPath(mPath, mPaint)

        // 绘制圆

        // 绘制圆
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mRadius, mCirclrPaint)

        // 绘制控制线

        // 绘制控制线
        for (i in mControlPointList.indices) {
            // 设置颜色
            mLinePaint.color = mLineColor[i / 3]
            val endPointIndex = if (i == mControlPointList.size - 1) 0 else i + 1
            canvas.drawLine(
                mControlPointList[i].x,
                mControlPointList[i].y,
                mControlPointList[endPointIndex].x,
                mControlPointList[endPointIndex].y,
                mLinePaint
            )
        }

    }
}