package com.pwj.record.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 * Author:           pwj
 * Date:             2020/4/14 10:59
 * FileName:         ActivityExt
 * description:      Activity启动扩展类
 */
inline fun <reified T : Activity> Activity.startExtActivityForResult(
    requestCode: Int,
    flags: Int? = null,
    extra: Bundle? = null,
    value: Pair<String, Any>? = null,
    values: Collection<Pair<String, Any>>? = null
) {
    val list = ArrayList<Pair<String, Any>>()
    value?.let { list.add(it) }
    values?.let { list.addAll(it) }
    startActivityForResult(getIntent<Activity>(flags, extra, list), requestCode)
}

inline fun <reified T : Activity> Fragment.startExtActivity(
    flags: Int? = null,
    extra: Bundle? = null,
    value: Pair<String, Any>? = null,
    values: Collection<Pair<String, Any>?>? = null
) {
    activity?.let {
        val list = ArrayList<Pair<String, Any>?>()
        value?.let { v -> list.add(v) }
        values?.let { v -> list.addAll(v) }
        startActivity(it.getIntent<T>(flags, extra, list))
    }
}

inline fun <reified T : Activity> Activity.startExtActivity(
    flags: Int? = null,
    extra: Bundle? = null,
    value: Pair<String, Any>? = null,
    values: Collection<Pair<String, Any>?>? = null
) {
    val list = ArrayList<Pair<String, Any>?>()
    value?.let { v -> list.add(v) }
    values?.let { v -> list.addAll(v) }
    startActivity(getIntent<T>(flags, extra, list))
}


inline fun <reified T : Context> Context.getIntent(
    flags: Int? = null,
    extra: Bundle? = null,
    pairs: List<Pair<String, Any>?>? = null
): Intent = Intent(this, T::class.java).apply {
    flags?.let { setFlags(it) }
    extra?.let { putExtras(it) }
    pairs?.let {
        for (pair in pairs) {
            pair?.let {
                val (key, value) = pair
                Log.e("Intent-Value-type", "key=$key,value=$value,type= ${value::class.java}")
                when (value) {
                    is Int -> putExtra(key, value)
                    is Byte -> putExtra(key, value)
                    is Char -> putExtra(key, value)
                    is Short -> putExtra(key, value)
                    is Boolean -> putExtra(key, value)
                    is Long -> putExtra(key, value)
                    is Float -> putExtra(key, value)
                    is Double -> putExtra(key, value)
                    is String -> putExtra(key, value)
                    is CharSequence -> putExtra(key, value)
                    is Parcelable -> putExtra(key, value)
                    is Array<*> -> putExtra(key, value)
                    is ArrayList<*> -> putExtra(key, value)
                    is Serializable -> putExtra(key, value)
                    is BooleanArray -> putExtra(key, value)
                    is ByteArray -> putExtra(key, value)
                    is ShortArray -> putExtra(key, value)
                    is CharArray -> putExtra(key, value)
                    is IntArray -> putExtra(key, value)
                    is LongArray -> putExtra(key, value)
                    is FloatArray -> putExtra(key, value)
                    is DoubleArray -> putExtra(key, value)
                    is Bundle -> putExtra(key, value)
                    else -> {
                    }
                }
            }
        }
    }
}