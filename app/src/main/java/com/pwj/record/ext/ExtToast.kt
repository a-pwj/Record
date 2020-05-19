package com.pwj.record.ext

import android.app.Activity
import android.content.Context
import android.widget.Toast

/**
 * Author:           pwj
 * Date:             2020/4/15 10:40
 * FileName:         ExtToast
 * description:      Toast 扩展类
 */
fun Context.showToast(content:String){
    Toast.makeText(this,content,Toast.LENGTH_SHORT).show()
}

fun Activity.showToastLong(content:String){
    Toast.makeText(this,content,Toast.LENGTH_LONG).show()
}