package com.pwj.parallaximage.controller

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

/**
 * @Author:          pwj
 * @Date:            2020/4/22 10:43
 * @FileName:        BaseImageController
 * @Description:     图片资源控制器：1,加载本地/res/url.  2,对图片进行缩放操作,生成bitmap 重写loadImage()来实现自己加载的逻辑
 */
abstract class BaseImageController : IController {

    @Volatile
    @JvmField
    var isProcessing = false

    @JvmField
    var callback: ProcessCallback? = null

    @JvmField
    var tBitmap: Bitmap? = null

    override fun process(viewWidth: Int) {
        if (isProcessing) return

        isProcessing = true

        loadImage(viewWidth)
    }

    abstract fun loadImage(viewWidth: Int)

    override fun setProcessCallback(callback: ProcessCallback?) {
        this.callback = callback
    }

    override fun getTargetBitmap(): Bitmap? = tBitmap

    /**
     * 计算重采样倍数
     */
    fun calculateInSampleSize(sourceWidth: Int, sourceHeight: Int, reqWidth: Int, reqHeight: Int): Int {
        var inSampleSize = 1
        if (sourceWidth > reqWidth || sourceHeight > reqHeight) {
            val halfWidth = sourceWidth shr 2
            val halfHeight = sourceHeight shr 2
            while ((halfWidth / inSampleSize > reqWidth) && (halfHeight / inSampleSize > reqHeight)) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * 处理drawable
     */
    fun handleDrawable(viewWidth: Int, resource: Drawable) {
        // 1,get the width/height of resource
        val originWidth = resource.intrinsicWidth
        val originHeight = resource.intrinsicHeight
        // 2,calc the scale factor
        val scale = 1.0f * viewWidth / originWidth
        // 3,calc the target width/height
        val scaleWidth = (scale * originWidth).toInt()
        val scaleHeight = (scale * originHeight).toInt()
        // 4,drawable to bitmap
        var bmp: Bitmap? = drawable2Bitmap(resource) ?: return
        // 5,scaled bitmap
        tBitmap = Bitmap.createScaledBitmap(bmp!!, scaleWidth, scaleHeight, true)
        bmp = null

        callback!!.onProcessFinished(scaleWidth, scaleHeight)
        // 7,reset flag
        isProcessing = false
    }

    /**
     * 处理bitmap
     * @param viewWidth
     * @param bitmap
     */
    protected open fun handleBitmap(viewWidth: Int, bitmap: Bitmap?) {
        // 1,get the width/height of resource
        var bitmap = bitmap
        val originWidth = bitmap!!.width
        val originHeight = bitmap.height
        // 2,calc the scale factor
        val scale = 1.0f * viewWidth / originWidth
        // 3,calc the target width/height
        val scaleWidth = (scale * originWidth).toInt()
        val scaleHeight = (scale * originHeight).toInt()

        // 4,scaled bitmap
        tBitmap = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, true)
        bitmap = null

        // 5,callback
        callback!!.onProcessFinished(scaleWidth, scaleHeight)
        // 6,reset flag
        isProcessing = false
    }

    /**
     * drawable转bitmap
     * @param drawable
     * @return
     */
    private fun drawable2Bitmap(drawable: Drawable): Bitmap? {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            null
        }
    }
}