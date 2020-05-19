package com.pwj.record.ui.tools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pwj.record.R
import kotlinx.android.synthetic.main.activity_im_options_multi_line.*

/**
 * @Author:          pwj
 * @Date:            2020-5-19 17:38:51
 * @FileName:        ImOptionsMultiLineActivity
 * @Description:     description
 */
class ImOptionsMultiLineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_im_options_multi_line)


        //示例
        editContent.setHorizontallyScrolling(false)
        editContent.maxLines =5
    }
}
