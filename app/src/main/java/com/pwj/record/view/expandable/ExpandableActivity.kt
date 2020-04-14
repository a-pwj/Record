package com.pwj.record.view.expandable

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R


class ExpandableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable)

        val expandableTextView: ExpandableTextView = findViewById(R.id.expanded_text)
        val viewWidth: Int = windowManager.defaultDisplay.width - dp2px(this, 20f)
        expandableTextView.initWidth(viewWidth)
        expandableTextView.maxLines = 3
        expandableTextView.setHasAnimation(true)
        expandableTextView.setCloseInNewLine(true)
        expandableTextView.setOpenSuffixColor(resources.getColor(R.color.colorAccent))
        expandableTextView.setCloseSuffixColor(resources.getColor(R.color.colorAccent))
        expandableTextView.setOriginalText(
            "在全球，随着Flutter被越来越多的知名公司应用在自己的商业APP中，" +
                    "Flutter这门新技术也逐渐进入了移动开发者的视野，尤其是当Google在2018年IO大会上发布了第一个" +
                    "Preview版本后，国内刮起来一股学习Flutter的热潮。\n\n为了更好的方便帮助中国开发者了解这门新技术" +
                    "，我们，Flutter中文网，前后发起了Flutter翻译计划、Flutter开源计划，前者主要的任务是翻译" +
                    "Flutter官方文档，后者则主要是开发一些常用的包来丰富Flutter生态，帮助开发者提高开发效率。而时" +
                    "至今日，这两件事取得的效果还都不错！"
        )
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        var res = 0
        val scale: Float = context.getResources().getDisplayMetrics().density
        res = if (dpValue < 0) (-(-dpValue * scale + 0.5f)).toInt() else (dpValue * scale + 0.5f).toInt()
        return res
    }
}
