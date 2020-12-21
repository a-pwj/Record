package com.pwj.record.ui.statusbar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import com.pwj.record.utils.StatusBarUtils

/**
 * @Author:          pwj
 * @Date:            2020-10-22 17:12:19
 * @FileName:        StatusImgActivity
 * @Description:     description
 */
class StatusImgActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_img)

//        方式一：直接给状态栏设置对应的颜色
       getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
       getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
       getWindow().setStatusBarColor(Color.RED);

//        方式二：给状态栏设置透明色并让状态栏浮在视图顶层，配合setFitsSystemWindows（）
       getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
       getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
       getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
       getWindow().setStatusBarColor(Color.TRANSPARENT);

//        toolBar.setFitsSystemWindows(true);//这里根据自己的布局情况
    }

    //
//    private fun setColor() {
//        var color = getColor()
//        if (color == 0) {
//            color = resources.getColor(R.color.white)
//        }
//        StatusBarUtils.setColor(this, color)
//    }
//
//    @ColorInt
//    open fun getColor(): Int {
//        return 0
//    }
}
