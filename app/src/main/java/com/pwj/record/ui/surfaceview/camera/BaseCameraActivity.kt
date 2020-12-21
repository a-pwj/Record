package com.pwj.record.ui.surfaceview.camera

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R

/**
 * @Author:          pwj
 * @Date:            2020/9/27 14:06
 * @FileName:        BaseCameraActivity
 * @Description:     description
 */
abstract class BaseCameraActivity : AppCompatActivity() {

    protected lateinit var activity: BaseCameraActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //去除标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        //去除状态栏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(getLayoutId())
        activity = this
        initStatusColor()
        initView()
        initData()
    }

    /**
     * 设置透明状态栏,这样才能让 ContentView 向上  6.0小米手机设置 tootlbar 会被挤上去
     * window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
     */
    private fun initStatusColor() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val window= activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //设置状态栏颜色
            window.statusBarColor = getColor(R.color.theme)

            val mContentView = activity.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            val mChildView = mContentView.getChildAt(0)
            if (mChildView!=null){
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
                mChildView.fitsSystemWindows = false
            }
        }
    }
    protected abstract fun getLayoutId(): Int
    protected abstract fun initView()
    protected abstract fun initData()

}