package com.pwj.record.ui.tools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwj.record.R
import com.pwj.record.ext.startExtActivity
import com.pwj.record.ui.adpter.ToolsAdapter
import kotlinx.android.synthetic.main.activity_tools.*

/**
 * @Author:          pwj
 * @Date:            2020-5-19 16:38:33
 * @FileName:        ToolsActivity
 * @Description:     description
 */
class ToolsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tools)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ToolsActivity)
            adapter = ToolsAdapter().apply {
                setData(mockData())
                OnItemClickListenr = { position ->
                    when (position) {
                        0 -> startExtActivity<ImOptionsMultiLineActivity>()
                    }
                }
            }
        }
    }

    private fun mockData(): Collection<String> {
        return mutableListOf<String>().apply {
            add("输入框 ImOptions MultiLine示例")
        }
    }
}
