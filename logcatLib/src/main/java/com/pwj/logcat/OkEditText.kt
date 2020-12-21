package com.pwj.logcat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.pwj.logcat.R
/**
 * @Author: pwj
 * @Date: 2020/7/23 17:20
 * @FileName: OkEditText
 * @Description: 带确定按钮的输入框
 */
class OkEditText @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle) {
    private val ivSearch: ImageView
    private val ivOk: ImageView
    private val etContent: EditText
    private fun setListener() {
        ivSearch.setOnClickListener {
            ivOk.visibility = View.VISIBLE
            etContent.visibility = View.VISIBLE
        }
        ivOk.setOnClickListener {
            if (onClickOkListener != null) {
                onClickOkListener!!.onOk(etContent.text.toString().trim { it <= ' ' })
            }
        }
    }

    private var onClickOkListener: OnClickOkListener? = null
    fun setOnClickOkListener(onClickOkListener: OnClickOkListener?) {
        this.onClickOkListener = onClickOkListener
    }

    interface OnClickOkListener {
        fun onOk(content: String?)
    }

    init {
        val view = View.inflate(context, R.layout.edit_ok, null)
        ivSearch = view.findViewById<View>(R.id.iv_search) as ImageView
        ivOk = view.findViewById<View>(R.id.iv_ok) as ImageView
        etContent = view.findViewById<View>(R.id.et_content) as EditText
        addView(view)
        setListener()
    }
}