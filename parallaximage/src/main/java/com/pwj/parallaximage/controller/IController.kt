package com.pwj.parallaximage.controller

import android.graphics.Bitmap

/**
 * @Author:          pwj
 * @Date:            2020/4/22 10:40
 * @FileName:        IController
 * @Description:     description
 */
interface IController {
    fun process(viewWidth: Int)

    fun getTargetBitmap(): Bitmap?

    fun setProcessCallback(callback: ProcessCallback?)
}

interface ProcessCallback {
    fun onProcessFinished(width: Int, height: Int)
}