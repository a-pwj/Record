package com.pwj.fragment

import android.content.Context

/**
 * @Author:          pwj
 * @Date:            2020/7/9 15:37
 * @FileName:        BaseContext
 * @Description:     description
 */
class BaseFragmentContext private constructor() {

    private lateinit var mContext: Context

    fun init(context: Context) {
        mContext = context
    }

    fun getContext(): Context = mContext

    companion object {
        val intance = Singleton.holder

        object Singleton {
            val holder = BaseFragmentContext()
        }
    }
}