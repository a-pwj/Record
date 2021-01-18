package com.pwj.record.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.Serializable

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


fun Fragment.showToast(msg: String) {
    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
}

fun Fragment.toActivity(clazz: Class<*>) {
    this.activity?.startActivity(Intent(this.activity, clazz))
}

fun AppCompatActivity.toActivity(clazz: Class<*>, a: Serializable) {
    val intent = Intent(this, clazz)
    intent.putExtra("Data", a)
    this.startActivity(intent)
}

fun Fragment.toActivity(clazz: Class<*>, a: Serializable?) {
    val intent = Intent(this.activity, clazz)
    if (a!=null){
        intent.putExtra("Data", a)
    }
    this.activity?.startActivity(intent)
}


fun AppCompatActivity.showToast(msg: String) {
    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.toActivity(clazz: Class<*>) {
    this.startActivity(Intent(this, clazz))
}

fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}
fun Context.getResColor(@ColorRes color: Int): Int = ContextCompat.getColor(this, color)

