package com.pwj.record

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import com.pwj.fragment.MainFragmentActivity
import com.pwj.hencoder.MainHenCoderActivity
import com.pwj.logcat.LogcatDialog
import com.pwj.record.bean.FileInfoBean
import com.pwj.record.bean.Person
import com.pwj.record.ext.showToast
import com.pwj.record.ext.startExtActivity
import com.pwj.record.ui.*
import com.pwj.record.ui.file.FileManagerActivity
import com.pwj.record.ui.statusbar.MainStatusActivity
import com.pwj.record.ui.surfaceview.MainSurfaceActivity
import com.pwj.record.ui.tools.ToolsActivity
import com.pwj.record.utils.FileManager
import com.pwj.record.view.expandable.ExpandableActivity
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_main.*
import java.util.HashSet
import kotlin.properties.Delegates

/**
 * 主入口
 * todo:
 *      注解回顾
 *      jetpack
 *      swipback
 *      notification
 *      surfaceView
 *      logcat dialog floatview
 *      fragment activity 基类
 *      Junit测试
 *
 */
class MainActivity : AppCompatActivity(R.layout.activity_main), View.OnClickListener {

    private val FILERESULTQ = 100
    private val FILERESULT = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        record.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
        record.setOnLongClickListener {
            LogcatDialog(this.applicationContext).show()
            return@setOnLongClickListener true
        }

        code0.setOnClickListener(this)
        code1.setOnClickListener(this)
        code2.setOnClickListener(this)
        code3.setOnClickListener(this)
        code4.setOnClickListener(this)
        code5.setOnClickListener(this)
        code6.setOnClickListener(this)
        code7.setOnClickListener(this)
        code8.setOnClickListener(this)
        code9.setOnClickListener(this)
        code10.setOnClickListener(this)
        code11.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.code0 -> startExtActivity<MainStatusActivity>()
            R.id.code1 -> startExtActivity<ExpandableActivity>()
            R.id.code2 -> {
                val list = ArrayList<Pair<String, Any>>().apply {
                    add(Pair("array<*>", Array<Person>(3) { i: Int -> Person(i.toString()) }))
                    add(Pair("ArrayList<*>", ArrayList<Person>().apply {
                        add(Person("10"))
                        add(Person("11"))
                        add(Person("12"))
                        add(Person("13"))
                    }))
                    add(Pair("bundle", Bundle().putString("bundle", "bundleResult")))
                }
                startExtActivity<ExtTestActivity>(values = list)
            }
            R.id.code3 -> {
                startExtActivity<ActivityResultNewApiActivity>()
            }
            R.id.code4 -> {
                startExtActivity<ViewPagerActivity>()
            }
            R.id.code5 -> {
                startExtActivity<DSLAnimActivity>()
            }
            R.id.code6 -> {
                startExtActivity<MainUIActivity>()
            }
            R.id.code7 -> {
                startExtActivity<ToolsActivity>()
            }
            R.id.code8 -> {
                startExtActivity<MainFragmentActivity>()
            }
            R.id.code9 -> {
                startExtActivity<MainHenCoderActivity>()
            }
            R.id.code10 -> {
                startExtActivity<MainSurfaceActivity>()
            }
            R.id.code11 -> {
                if (VERSION.SDK_INT >= 29 && getApplicationInfo().targetSdkVersion >= 29) {
                    val intent = Intent("android.intent.action.OPEN_DOCUMENT")
                    intent.addCategory("android.intent.category.OPENABLE")
                    intent.type = "*/*"
                    startActivityForResult(intent, FILERESULTQ)
                } else {
                    if (AndPermission.hasPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))) {
                        val intent = Intent(this, FileManagerActivity::class.java)
                        startActivityForResult(intent, FILERESULT)
                    } else {
                        AndPermission.with(this)
                            .runtime()
                            .permission(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
                            .rationale { context, data, executor ->  }//添加拒绝权限回调
                            .onGranted { startActivityForResult(intent, FILERESULT) }
                            .onDenied { }.start();
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FILERESULTQ) {
                val uri = data?.data
                uri?.let {
                    val documentFile = DocumentFile.fromSingleUri(this, uri)
                    if (documentFile == null) {
                        showToast("文件获取出错")
                    } else {
                        if (documentFile.name != null && documentFile.type != null) {
                            val path: String? = FileManager.getPathFromInputStreamUri(this, uri ,documentFile.name ?: "")
//                            val fileLocal = FileData(documentFile.name ?: "", documentFile.type ?: "", documentFile.length(), path ?: "", "")
//                            mPresenter.upFile(fileLocal)
                            showToast("文件名字：${documentFile.name},文件路径：${path?:""}")
                        } else {
                            showToast("文件获取出错")
                        }
                    }
                }
            }
            if (requestCode == FILERESULT) {
                val selectedFileInfos: HashSet<FileInfoBean> = data!!.getSerializableExtra("sendSelectedFiles") as HashSet<FileInfoBean>
                val var1: Iterator<FileInfoBean> = selectedFileInfos.iterator()
                while (var1.hasNext()) {
                    val fileInfo: FileInfoBean = var1.next() as FileInfoBean
//                    val fileLocal = FileData(fileInfo.fileName, fileInfo.suffix, fileInfo.fileSize, fileInfo.filePath, "")
//                    mPresenter.upFile(fileLocal)
                    showToast("文件名字：${fileInfo.fileName},文件路径：${fileInfo.filePath?:""}")
                }
            }
        }
    }


    /**
     * 利用委托完成双击back退出
     */
    private var backPressedTime by Delegates.observable(0L) { pre, old, new ->
        /**
         * 两次时间间隔小于2秒就退出
         */
        if (new - old < 2000) {
            finish()
        } else {
            showToast("再按一次返回键退出")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backPressedTime = System.currentTimeMillis()
    }

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(this@MainActivity, object : GestureDetector.SimpleOnGestureListener() {
            /**
             * 发生确定的单击时执行
             * @param e
             * @return
             */
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean { //单击事件
                showToast("这是单击事件")
                return super.onSingleTapConfirmed(e)
            }

            /**
             * 双击发生时的通知
             * @param e
             * @return
             */
            override fun onDoubleTap(e: MotionEvent?): Boolean { //双击事件
                showToast("这是双击事件")
                return super.onDoubleTap(e)
            }

            /**
             * 双击手势过程中发生的事件，包括按下、移动和抬起事件
             * @param e
             * @return
             */
            override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
                return super.onDoubleTapEvent(e)
            }
        })
    }

    /**
     * 点击外部 输入框消失
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    v?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }

}

