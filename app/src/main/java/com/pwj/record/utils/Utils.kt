package com.pwj.record.utils

import android.app.Application

/**
 * @Author:          pwj
 * @Date:            2020/7/23 9:31
 * @FileName:        Utils
 * @Description:     description
 */
object Utils {

    private var uApp: Application? = null

    fun init(application: Application?) {
        if (application == null) return

        if (uApp == null) {
            uApp = application
            return
        }
        if (uApp == application) return
        uApp = application
    }

    fun getApp(): Application {
        if (uApp == null) throw NullPointerException("reflect failed.")
        return uApp!!
    }

}