package com.pwj.record.ui.adpter

import android.widget.TextView
import com.pwj.record.R

/**
 * @Author:          pwj
 * @Date:            2020/5/19 16:40
 * @FileName:        ToolsAdapter
 * @Description:     description
 */
class ToolsAdapter : BaseAdapter<String>() {

    override fun getLayoutId(): Int = R.layout.item_tools

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val text = holder.getView<TextView>(R.id.text)
        text.text = mData[position]
    }
}