package com.pwj.record.ui.lottie

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieCompositionFactory
import com.pwj.record.R
import kotlinx.android.synthetic.main.activity_lottie.*


/**
 * @Author:          pwj
 * @Date:            2020-5-9 17:13:54
 * @FileName:        LottieActivity
 * @Description:
 *
 * Lottie 支持加载来自 res/raw 或 assets/ 的动画资源。建议使用 res/raw，
 * 因为可以对动画通过 R 文件使用静态引用，而不只是使用字符串名称。这也可以帮助构建静态分析，因为它可以跟踪动画的使用
 */
class LottieActivity : AppCompatActivity(R.layout.activity_lottie) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            val url = "http://cdn.trojx.me/blog_raw/lottie_data_origin.json"
            LottieCompositionFactory.fromUrl(this, url).addListener {
                animationView.setComposition(it)
            }
        }, 10000)

    }

}
