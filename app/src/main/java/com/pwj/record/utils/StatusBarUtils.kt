package com.pwj.record.utils

import android.app.Activity
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import com.pwj.record.R

/**
 * @Author:          pwj
 * @Date:            2020/10/22 17:16
 * @FileName:        StatuBarUtils
 * @Description:     description
 */
object StatusBarUtils {

    fun setColor(activity: Activity, @ColorInt color: Int) {
        if (isPhone("meizu")) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (isLightColor(activity, color)) {
                    setBarColor(activity, activity.resources.getColor(R.color.colorAccent))
                } else {
                    setBarColor(activity, color)
                }
            } else {
                setBarColor(activity, color)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = activity.window.decorView
            if (isLightColor(activity, color)) {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decor.systemUiVisibility = 0
            }
        } else {
            if (isPhone("xiaomi")) {
                if (isLightColor(activity, color)) {
                    setMiuiStatusBarLightMode(activity, true)
                } else {
                    setMiuiStatusBarLightMode(activity, false)
                }
            } else if (isPhone("meizu")) {
                if (isLightColor(activity, color)) {
                    setFlaymeStatusBarLightMode(activity, true)
                } else {
                    setFlaymeStatusBarLightMode(activity, false)
                }
            }
        }
    }

    fun isPhone(model: String?): Boolean {
        if (!TextUtils.isEmpty(Build.MANUFACTURER)) {
            if (Build.MANUFACTURER.toLowerCase().contains(model!!)) {
                return true
            }
        }
        if (!TextUtils.isEmpty(Build.MODEL)) {
            if (Build.MODEL.toLowerCase().contains(model!!)) {
                return true
            }
        }
        return false
    }

    private fun isLightColor(activity: Activity, color: Int): Boolean {
        val c = activity.resources.getColor(R.color.white)
        val c2 = activity.resources.getColor(R.color.light_gray)
        return c == color || c2 == color
    }


    private fun setBarColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (activity.window.statusBarColor != color) {
                activity.window.statusBarColor = color
            }
        }
    }

    /**
     * 设置miui6+状态栏的字体颜色模式
     *
     * @param activity        context
     * @param isFontColorDark true:dark  false:light
     * @return 是否设置成功
     */
    fun setMiuiStatusBarLightMode(activity: Activity, isFontColorDark: Boolean): Boolean {
        val window = activity.window
        var result = false
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (isFontColorDark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                }
                result = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    /**
     * 设置flyme4+状态栏的字体颜色模式
     *
     * @param activity        context
     * @param isFontColorDark true:dark  false:light
     * @return 是否设置成功
     */
    fun setFlaymeStatusBarLightMode(activity: Activity, isFontColorDark: Boolean): Boolean {
        val window = activity.window
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (isFontColorDark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

}