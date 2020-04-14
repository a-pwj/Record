package com.ts.uidemo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

/**
 * Author:           pwj
 * Date:             2020/3/31 14:45
 * FileName:         ActivityExt
 */
inline fun <reified T : AppCompatActivity> toActivity(context: Context) {
    context.startActivity(Intent(context, T::class.java))
}