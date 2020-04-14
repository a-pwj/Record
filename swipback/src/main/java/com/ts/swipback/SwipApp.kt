package com.ts.swipback

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.tencent.mmkv.MMKV
import java.util.*

/**
 * Author:           pwj
 * Date:             2020/4/8 16:14
 * FileName:         SwipApp
 */
class SwipApp : Application() {

    private val mActivityStack = Stack<Activity>()
    companion object {
        lateinit var instance: SwipApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext  as SwipApp

        MMKV.initialize(this)

        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                mActivityStack.add(activity)
            }

            override fun onActivityResumed(activity: Activity) {

            }

        })
    }

    /**
     * 获取倒数第二个 Activity
     *
     * @return
     */
    fun getPenultimateActivity(currentActivity: Activity): Activity? {
        var activity: Activity? = null
        try {
            if (mActivityStack.size > 1) {
                activity = mActivityStack[mActivityStack.size - 1]
            }
        } catch (e: Exception) {
        }
        return activity
    }

    fun addActivity(activity: Activity?) {
        mActivityStack.add(activity)
    }

}