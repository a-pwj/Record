package com.ts.swipback

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import java.lang.reflect.Field


/**
 * Author:           pwj
 * Date:             2020/4/8 14:44
 * FileName:         BaseSlideCloseActivity
 */
open class BaseSlideCloseActivity : AppCompatActivity(), SlidingPaneLayout.PanelSlideListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        initSlideBack()
        super.onCreate(savedInstanceState)
    }

    private fun initSlideBack() {
        if (isSupportSwipeBack()) {
            val slidingPaneLayout = SlidingPaneLayout(this)
            //反射
            try {
                val overhangSize: Field = SlidingPaneLayout::class.java.getDeclaredField("mOverhangSize")
                overhangSize.isAccessible = true
                overhangSize.set(slidingPaneLayout, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            slidingPaneLayout.setPanelSlideListener(this)
            slidingPaneLayout.sliderFadeColor = resources.getColor(android.R.color.transparent)

            // 左侧的透明视图
            val leftView = View(this)
            leftView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            slidingPaneLayout.addView(leftView, 0)

            val decorView = window.decorView as ViewGroup

            // 右侧的内容视图
            val decorChild = decorView.getChildAt(0) as ViewGroup
            decorChild.setBackgroundColor(resources.getColor(android.R.color.transparent))
            decorView.removeView(decorChild)
            decorView.addView(slidingPaneLayout)


            //为SlidingPaneLayout 添加内容视图
            slidingPaneLayout.addView(decorChild, 1)

        }
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {

    }

    override fun onPanelClosed(panel: View) {

    }

    override fun onPanelOpened(panel: View) {
        finish()
    }

    open fun isSupportSwipeBack(): Boolean {
        return true
    }

}