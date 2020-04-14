package com.ts.swipback

import com.tencent.mmkv.MMKV

/**
 * Author:           pwj
 * Date:             2020/4/8 17:31
 * FileName:         MMKVUtils
 */
object MMKVUtils {

    @Volatile
    var mmkv: MMKV? = null

    fun getInstance(): MMKV {
        return mmkv ?: synchronized(this) {
            mmkv ?: MMKV.defaultMMKV()
                .also {
                    mmkv = it
                }
        }
    }

    fun encode(key: String, value: Any) {
        when (value) {
            is Boolean -> mmkv?.encode(key, value)
            is Int -> mmkv?.encode(key, value)
            is Long -> mmkv?.encode(key, value)
            is Float -> mmkv?.encode(key, value)
            is Double -> mmkv?.encode(key, value)
            is String -> mmkv?.encode(key, value)
            is ByteArray -> mmkv?.encode(key, value)
        }
    }

    inline fun <reified T> decode(key: String, t: T): T? {
        return when (t) {
            is Boolean -> getInstance().decodeBool(key, false) as T
            is Int -> getInstance().decodeInt(key, 0) as T
            is Long -> getInstance().decodeLong(key, 0L) as T
            is Float -> getInstance().decodeFloat(key, 0F) as T
            is Double -> getInstance().decodeDouble(key, 0.0) as T
            is String -> getInstance().decodeString(key, "") as T
            is ByteArray -> getInstance().decodeBytes(key, null) as T
            else -> null
        }
    }

}