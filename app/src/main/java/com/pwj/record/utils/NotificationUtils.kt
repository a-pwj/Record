package com.pwj.record.utils

import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.annotation.IntDef
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * @Author:          pwj
 * @Date:            2020/7/22 10:18
 * @FileName:        NotificationUtils
 * @Description:     description
 */
object NotificationUtils {

    const val IMPORTANCE_UNSPECIFIED = -1000
    const val IMPORTANCE_NONE = 0
    const val IMPORTANCE_MIN = 1
    const val IMPORTANCE_LOW = 2
    const val IMPORTANCE_DEFAULT = 3
    const val IMPORTANCE_HIGH = 4

    @IntDef(
        NotificationUtils.IMPORTANCE_UNSPECIFIED,
        NotificationUtils.IMPORTANCE_NONE,
        NotificationUtils.IMPORTANCE_MIN,
        NotificationUtils.IMPORTANCE_LOW,
        NotificationUtils.IMPORTANCE_DEFAULT,
        NotificationUtils.IMPORTANCE_HIGH
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Importance

    /**
     * 返回 notification 是否打开
     */
    fun areNotificationEnabled(context: Context) = NotificationManagerCompat.from(context).areNotificationsEnabled()


    fun notify(id: Int, builder: NotificationCompat.Builder) {

    }

    fun notify(tag: String, id: Int, channelConfig: ChannelConfig, builder: NotificationCompat.Builder) {

    }


    class ChannelConfig(id: String, name: CharSequence, @Importance importance: Int) {

        companion object {
            val DEFAULT_CHANNEL_CONFIG = ChannelConfig(Utils.getApp().packageName, Utils.getApp().packageName, IMPORTANCE_DEFAULT)
        }

        private lateinit var mNotificationChannel: NotificationChannel

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationChannel = NotificationChannel(id, name, importance)
            }
        }

        fun getNotificationChannel(): NotificationChannel? {
            return mNotificationChannel
        }

    }
}