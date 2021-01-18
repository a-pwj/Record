package com.pwj.record.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwj.record.R
import com.pwj.record.ext.startExtActivity
import com.pwj.record.ui.adpter.SimpleAdapter
import com.pwj.record.ui.clock.CustomClockViewActivity
import com.pwj.record.ui.lottie.LottieActivity
import com.pwj.record.ui.motionlayout.MainMotionLayoutActivity
import com.pwj.record.ui.notification.NotificationActivity
import com.pwj.record.ui.scroll.ScrollerActivity
import com.pwj.record.ui.switcher.MainSwitcherActivity
import com.pwj.record.ui.video.MainVideoActivity
import com.pwj.record.utils.ScreenUtils
import com.pwj.record.view.EvaluationDialog
import com.pwj.record.view.recyclerView.ParallaxImageActivity
import kotlinx.android.synthetic.main.activity_main_u_i.*


class MainUIActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_u_i)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainUIActivity)
            adapter = SimpleAdapter().apply {
                setData(mockData())
                OnItemClickListenr = { position ->
                    when (position) {
                        0 -> startExtActivity<ParallaxImageActivity>()
                        1 -> startExtActivity<DialogActivity>()
                        2 -> startExtActivity<LottieActivity>()
                        3 -> startExtActivity<NotificationActivity>()
                        4 -> startExtActivity<ScrollerActivity>()
                        5 -> startExtActivity<UITestActivity>()
                        6 -> startExtActivity<CustomClockViewActivity>()
                        7 -> startExtActivity<MainVideoActivity>()
                        8 -> startExtActivity<MainSwitcherActivity>()
                        9 -> startExtActivity<MainMotionLayoutActivity>()
                        10 -> {
                            val mEvaluationDialog = EvaluationDialog(context)
                            mEvaluationDialog.setPositiveListener { dialog, level, message -> dialog.dismiss() }
                            mEvaluationDialog.setNegativeListener { dialog, level, message -> dialog.dismiss() }
                            mEvaluationDialog.setCancelable(false)
                            mEvaluationDialog.show()

                            val params: WindowManager.LayoutParams = mEvaluationDialog.window!!.attributes
                            if (ScreenUtils.getDensity(this@MainUIActivity) <= 1.5F) {
                                params.width = ScreenUtils.getScreenWidth(this@MainUIActivity) - ScreenUtils.dp2px(this@MainUIActivity, 30F)
                            }else{
                                params.width = ScreenUtils.getScreenWidth(this@MainUIActivity) - ScreenUtils.dp2px(this@MainUIActivity, 50F)
                            }
                            params.height = WindowManager.LayoutParams.WRAP_CONTENT
                            mEvaluationDialog.window!!.attributes = params
                        }
                    }
                }
            }
        }

    }

    private fun mockData(): Collection<String> {
        return mutableListOf<String>().apply {
            add("RecyclerView")
            add("使用Activity 作为Dialog")
            add("Lottie动画使用")
            add("Notification")
            add("View 滑动")
            add("View 杂货箱")
            add("时钟")
            add("video SurfaceView")
            add("ViewOutlineProvider 自定义")
            add("motionlayout")
            add("dialog 分辨率适配")
        }
    }
}