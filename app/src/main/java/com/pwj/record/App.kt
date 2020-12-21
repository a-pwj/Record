package com.pwj.record

import android.app.Application
import com.facebook.stetho.Stetho
import com.pwj.fragment.BaseFragmentContext

/**
 * @Author:          pwj
 * @Date:            2020/5/20 14:13
 * @FileName:        App
 * @Description:     description
 */
class App : Application() {


    override fun onCreate() {
        super.onCreate()

        /** Stetho 使用*/
        Stetho.initializeWithDefaults(this)

        BaseFragmentContext.intance.init(this)

    }
}