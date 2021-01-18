package com.pwj.record.ui.surfaceview.opengl

import android.graphics.SurfaceTexture

/**
 * @Author:          pwj
 * @Date:            2021/1/13 15:24
 * @FileName:        IDrawer
 * @Description:     渲染器
 */
interface IDrawer {
    fun setVideoSize(videoW: Int, videoH: Int)
    fun setWorldSize(worldW: Int, worldH: Int)
    fun setAlpha(alpha: Float)
    fun draw()
    fun setTextureID(id: Int)
    fun getSurfaceTexture(cb: (st: SurfaceTexture) -> Unit) {}
    fun release()
}