package com.pwj.record.ui.surfaceview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwj.record.R
import com.pwj.record.ext.startExtActivity
import com.pwj.record.ui.adpter.SimpleAdapter
import com.pwj.record.ui.surfaceview.camera.MainCameraActivity
import com.pwj.record.ui.surfaceview.ui.OpenGL1Activity
import kotlinx.android.synthetic.main.activity_main_surface.*

class MainSurfaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_surface)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainSurfaceActivity)
            adapter = SimpleAdapter().apply {
                setData(mockData())
                OnItemClickListenr = { position ->
                    when (position) {
                        0 -> startExtActivity<MainCameraActivity>()
                        2 -> startExtActivity<OpenGL1Activity>()
//                        2 -> startExtActivity<CommentListActivity>()
//                        3 -> startExtActivity<WebpActivity>()
////                        3 -> startExtActivity<ToolsIocActivity>()
                    }
                }
            }
        }
    }

    private fun mockData(): Collection<String> {
        return mutableListOf<String>().apply {
            add("自定义相机")
            add("绘制三角形")
        }
    }
}