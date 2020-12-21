package com.pwj.record.ui.surfaceview.camera

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwj.record.R
import com.pwj.record.ext.startExtActivity
import com.pwj.record.ui.adpter.SimpleAdapter
import com.pwj.record.view.recyclerView.ParallaxImageActivity
import kotlinx.android.synthetic.main.activity_main_camera.*

/**
 * @Author:          pwj
 * @Date:            2020/9/27 14:13
 * @FileName:        MainCameraActivity.kt
 * @Description:     camera入口
 */
class MainCameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_camera)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainCameraActivity)
            adapter = SimpleAdapter().apply {
                setData(mockData())
                OnItemClickListenr = { position ->
                    when (position) {
                        0 -> startExtActivity<CameraCustomActivity>()
                    }
                }
            }
        }
    }

    private fun mockData(): Collection<String> {
        return mutableListOf<String>().apply {
            add("自定义Camera")
        }
    }
}