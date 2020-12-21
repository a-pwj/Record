package com.pwj.logcat.utils

import android.content.Context

/**
 * @Author:          pwj
 * @Date:            2020/7/24 13:50
 * @FileName:        LogcatContent
 * @Description:     description
 */
object LogcatContent {

    private lateinit var mContext: Context

    fun init(context: Context) {
        mContext = context.applicationContext
    }
}