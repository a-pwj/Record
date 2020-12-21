package com.pwj.record.ui.tools

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwj.record.R
import com.pwj.record.ext.startExtActivity
import com.pwj.record.ui.adpter.SimpleAdapter
import com.pwj.record.ui.tools.comment.CommentListActivity
import com.pwj.record.ui.tools.webp.WebpActivity
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
            adapter = SimpleAdapter().apply {
                setData(mockData())
                OnItemClickListenr = { position ->
                    when (position) {
                        0 -> startExtActivity<ImOptionsMultiLineActivity>()
                        1 -> startExtActivity<StethoActivity>()
                        2 -> startExtActivity<CommentListActivity>()
                        3 -> startExtActivity<WebpActivity>()
                        4 -> startExtActivity<OKIOActivity>()
//                        3 -> startExtActivity<ToolsIocActivity>()
                    }
                }
            }
        }
    }

    private fun mockData(): Collection<String> {
        return mutableListOf<String>().apply {
            add("输入框 ImOptions MultiLine示例")
            add("Stetho 示例")
            add("评论列表")
            add("Glide加载webp")
            add("Okio 使用")
//            add("IOC框架")
        }
    }
}
