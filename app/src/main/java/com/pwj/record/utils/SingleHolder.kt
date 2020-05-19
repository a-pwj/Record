package com.pwj.record.utils

/**
 * Author:           pwj
 * Date:             2020/4/13 13:32
 * FileName:         SingleHolder
 * description:      封装单例
 *
 * example:
 *  class SomeSingleton private constructor(context: Context) {
 *      init {
 *        // Init using context argument
 *       context.getString(R.string.app_name)
 *      }
 *
 *      companion object : SingletonHolder<SomeSingleton, Context>(::SomeSingleton)
 *  }
 */
open class SingleHolder<out T, in A>(creator: (A) -> T) {

    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }

}