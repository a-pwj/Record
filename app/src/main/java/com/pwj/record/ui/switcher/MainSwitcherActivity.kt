package com.pwj.record.ui.switcher

import android.graphics.Outline
import android.os.Bundle
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import kotlinx.android.synthetic.main.activity_main_switcher.*

/**
 * @Author:          pwj
 * @Date:            2020-12-23 11:32:13
 * @FileName:        MainSwitcherActivity
 * @Description:     description
 */
class MainSwitcherActivity : AppCompatActivity() {

    private val mCircleOutlineProvider by lazy { CircleOutlineProvider() }
    private val mRectOutlineProvider by lazy { RectOutlineProvider() }
    private var mElevationStep = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_switcher)

        mElevationStep = resources.getDimensionPixelSize(R.dimen.dp_40)
        circle.outlineProvider = mCircleOutlineProvider
        circle.clipToOutline = true

        change.setOnClickListener {
            if (circle.outlineProvider == mCircleOutlineProvider) {
                circle.outlineProvider = mRectOutlineProvider
            } else {
                circle.outlineProvider = mCircleOutlineProvider
            }
            circle.clipToOutline = true
        }
        raise_bt.setOnClickListener {
            mElevationStep += mElevationStep
            circle.elevation = mElevationStep.toFloat()
        }
        lower_bt.setOnClickListener {
            mElevationStep -= mElevationStep
            if (mElevationStep < 0) mElevationStep = 0
            circle.elevation = mElevationStep.toFloat()
        }

    }


    /**
     * 圆形
     */
    inner class CircleOutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            outline?.let {
                view?.let {
                    outline.setOval(0, 0, view.width, view.height)
                }
            }
        }
    }

    /**
     * 矩形
     */
    inner class RectOutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            outline?.let {
                view?.let {
                    val margin = view.width.coerceAtMost(view.height) / 10
                    outline.setRoundRect(margin, margin, view.width - margin, view.height, (margin / 2).toFloat())
                }
            }
        }
    }
}
