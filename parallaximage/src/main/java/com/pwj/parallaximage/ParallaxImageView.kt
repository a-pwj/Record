package com.pwj.parallaximage

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pwj.parallaximage.controller.IController
import com.pwj.parallaximage.controller.ProcessCallback
import com.pwj.parallaximage.utils.LoggerUtils

/**
 * @Author:          pwj
 * @Date:            2020/4/22 10:28
 * @FileName:        ParallaxImageView
 * @Description:     description
 */
class ParallaxImageView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    /**
     * 图片往上最大偏移量
     */
    private var maxUpOffScreen: Float = 0F

    /**
     * 图片往上偏移量
     */
    private var topOffScreen: Float = 0F

    /**
     * view 相对当前window的位置
     */
    private var viewLocation = IntArray(2)

    /**
     * view的宽高
     */
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    /**
     * 是否已经测量过
     */
    private var isMeasured: Boolean = false

    /**
     * control 是否完成了缩放处理
     */
    private var isScaled = false

    /**
     * 图片资源控制器：1.本地res,url 2.对图片进行缩放操作，生成bitmap
     */
    private var mImageController: IController? = null

    /**
     * 绑定的recyclerview
     */
    private var recyclerView: RecyclerView? = null
    private var rvScrollListener: RecyclerView.OnScrollListener? = null

    /**
     * 缩放因子
     */
    private var scaleFactor = 1.0f

    /**
     * recyclerview位置
     */
    private var rvLocation = IntArray(2)

    /**
     * recyclerview高度
     */
    private var rvHeight = 0

    fun setController(controller: IController) {
        LoggerUtils.LOGE("setController")
        mImageController = controller
        mImageController!!.setProcessCallback(object : ProcessCallback {
            override fun onProcessFinished(width: Int, height: Int) {
                LoggerUtils.LOGE("正常情况下只会走一次processListener:$height")
                isScaled = true
                resetScaleFactor(height)
                getLocationInWindow(viewLocation)
                topOffScreen = -(viewLocation[1] - rvLocation[1]) * scaleFactor
                bindTopOrBottom()
                if (viewLocation[1] == 0) { // view还未显示出来就已经执行了，因此位置计算异常不用刷新
                    return
                }
                // 当前是非ui线程
                postInvalidate()
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        viewHeight = resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(viewWidth, viewHeight)

        isMeasured = true
        LoggerUtils.LOGE("onMeasure")
        mImageController!!.process(viewWidth)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bitmap = mImageController?.getTargetBitmap()
        if (bitmap == null || bitmap?.isRecycled) return
        canvas?.drawBitmap(bitmap, 0F, topOffScreen, null)
    }


    /**
     * 让图片在顶部和底部时随着rv移动
     */
    private fun bindTopOrBottom(): Unit {
        if (topOffScreen > 0) {
            topOffScreen = 0f
        }
        if (topOffScreen < -maxUpOffScreen) {
            topOffScreen = -maxUpOffScreen
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        LoggerUtils.LOGE("onAttachedToWindow")
        if (isMeasured) {
            mImageController?.process(viewWidth)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isScaled = false
    }

    /**
     * 绑定rv
     */
    fun bindRecyclerView(recyclerView: RecyclerView?) {
        if (recyclerView == null || recyclerView == this.recyclerView) return

        unbindRecyclerView()
        this.recyclerView = recyclerView
        rvLocation= IntArray(2)
        rvHeight = recyclerView.layoutManager?.height ?: 0
        recyclerView.getLocationInWindow(rvLocation)
        rvScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val topDistance = getTopDistance()
                LoggerUtils.LOGE("onScrolled-----topDistance:$topDistance")
                if (topDistance > 0 && (topDistance + viewHeight < rvHeight)) {
                    topOffScreen += dy * scaleFactor
                    bindTopOrBottom()
                    if (isMeasured) {
                        invalidate()
                    }
                } else if (topDistance + viewHeight >= rvHeight) {
                    // view还未显示出来就执行process回调，因此会出现view在底部图片置顶的情况
                    if (topOffScreen == 0f) {
                        if (isScaled) {
                            getLocationOnScreen(viewLocation)
                            topOffScreen = -(viewLocation[1] - rvLocation[1]) * scaleFactor
                            bindTopOrBottom()
                            // 当前是非ui线程
                            invalidate()
                        }
                    }
                }
            }
        }
        recyclerView.addOnScrollListener(rvScrollListener!!)
    }

    /**
     * 取消绑定
     */
    private fun unbindRecyclerView() {
        if (recyclerView != null) {
            if (rvScrollListener != null) {
                recyclerView!!.removeOnScrollListener(rvScrollListener!!)
            }
            recyclerView = null
        }
    }

    /**
     * 计算高度的缩放因子
     *
     * @param scaledHeight
     */
    private fun resetScaleFactor(scaledHeight: Int) {
        if (recyclerView != null) {
            maxUpOffScreen = scaledHeight - viewHeight.toFloat()
            scaleFactor = 1.0f * maxUpOffScreen / (rvHeight - viewHeight)
            LoggerUtils.LOGD("resetScaleFactor : $scaleFactor")
        }
    }

    /**
     * 计算当前view到RecyclerView顶部的距离
     *
     * @return
     */
    private fun getTopDistance(): Int {
        getLocationInWindow(viewLocation)
        return viewLocation[1] - rvLocation[1]
    }
}