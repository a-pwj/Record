package com.pwj.record.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import com.pwj.record.ext.showToast
import kotlinx.android.synthetic.main.activity_dialog.*


/**
 * @Author:          pwj
 * @Date:            2020-4-22 15:36:15
 * @FileName:        DialogActivity
 * @Description:     使用Activity用作弹出式Dialog
 */
class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        //点击区域外消失
        setFinishOnTouchOutside(true)
        btnPhone.setOnClickListener {
//            prepareCall(ActivityResultContracts.RequestPermissions()) { result ->
//                if (result.values.first()) {
//                    showToast("拨号")
//                    val intent = Intent()
//                    intent.action = Intent.ACTION_CALL
//                    intent.data = Uri.parse("tel:13866666666")
//                    startActivity(intent)
//                }
//            }.launch(arrayOf(Manifest.permission.CALL_PHONE))

        }
    }
}
