package com.pwj.record.ui.tools

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import com.pwj.record.ext.showToast
import kotlinx.android.synthetic.main.activity_o_k_i_o.*
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.nio.charset.Charset

class OKIOActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var fileRoot: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_k_i_o)

//        mkdirs: 创建【多级目录文件夹
//        mkdir：创建【单级目录文件夹】，如果上级文件夹不存在则报错
//        createNewFile：创建【文件】（如.txt）
        fileRoot = File(filesDir.absolutePath, "okio_simple")
        if (!fileRoot.exists()) fileRoot.mkdirs()

        write.setOnClickListener(this)
        read.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.write -> {
                writeFile()
            }
            R.id.read -> {
                readFile()
            }
        }
    }

    /**
     * okio 写文件 示例
     */
    private fun writeFile() {
        //创建文件A
        try {
            val fileA = File(fileRoot, "a.txt")
            fileA.sink().buffer().writeUtf8("adsdasdasdas \n").writeUtf8("中国").close()
            showToast("写入完成")
        } catch (e: Exception) {
            showToast("写文件${e.message.toString()}")
        }

    }

    /**
     * okio 读文件 示例
     */
    private fun readFile() {
        try {
            val fileA = File(fileRoot, "a.txt")
            val str = fileA.source().buffer().readByteString().string(Charset.forName("utf-8"))
              showToast(str)
        } catch (e: Exception) {
            showToast("读文件${e.message.toString()}")
        }
    }


}