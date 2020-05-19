package com.pwj.record.utils

import android.util.Log
import java.util.*

/**
 * @Author:          pwj
 * @Date:            2020/4/22 11:15
 * @FileName:        Logger
 * @Description:     description
 */
object LoggerUtils {
    private val DEBUG = true
    val TAG = "wsjLib"

    fun LOGI(tag: String?, msg: String) {
        if (DEBUG) {
            Log.i(tag, buildMessage(msg))
        }
    }

    fun LOGD(tag: String?, msg: String) {
        if (DEBUG) {
            Log.d(tag, buildMessage(msg))
        }
    }

    fun LOGE(tag: String?, msg: String) {
        if (DEBUG) {
            Log.e(tag, buildMessage(msg))
        }
    }

    fun LOGI(msg: String) {
        if (DEBUG) {
            Log.i(TAG, buildMessage(msg))
        }
    }

    fun LOGD(msg: String) {
        if (DEBUG) {
            Log.d(TAG, buildMessage(msg))
        }
    }

    fun LOGE(msg: String) {
        if (DEBUG) {
            Log.e(TAG, buildMessage(msg))
        }
    }

    /**
     * 构建信息，用于创建自定义的Log信息结构
     * @param msg Log信息
     * @return 整个创建好的信息内容
     */
    private fun buildMessage(msg: String): String? {
        val targetStackTraceElement = getTargetStackTraceElement()
        return String.format(
            Locale.US, "%s -->%s",
            "(" + targetStackTraceElement!!.fileName + ":"
                    + targetStackTraceElement.lineNumber + ")", msg
        )
    }

    private fun getTargetStackTraceElement(): StackTraceElement? {
        // find the target invoked method
        var targetStackTrace: StackTraceElement? = null
        var shouldTrace = false
        val stackTrace = Thread.currentThread().stackTrace
        for (stackTraceElement in stackTrace) {
            val isLogMethod = stackTraceElement.className == LoggerUtils::class.java.name
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement
                break
            }
            shouldTrace = isLogMethod
        }
        return targetStackTrace
    }
}