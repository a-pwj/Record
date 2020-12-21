package com.pwj.logcat

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.Html
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StyleRes
import com.pwj.logcat.OkEditText.OnClickOkListener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*


/**
 * @Author: pwj
 * @Date: 2020/7/23 17:22
 * @FileName: LogcatDialog
 * @Description: 日志记录对话框
 */
class LogcatDialog : Dialog {
    private var title = "Logcat"
    private var tvLog: TextView? = null
    private val contentList: MutableList<String> = ArrayList() //内容
    private var isAutoFullScroll = true //是否自动拉取到最底部
    private var searchContent: String? = ""
    private var showGrade = 0 //显示级别，0 所有，1 system.out.println，2 警告级别,3 错误级别
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                WHAT_NEXT_LOG -> {
                    val line = msg.obj as String
                    if (!TextUtils.isEmpty(searchTag)) {
                        if (line.contains(searchTag)) {
                            if (!TextUtils.isEmpty(searchContent)) {
                                if (line.contains(searchContent!!)) { //同时搜索
                                    contentList.add(line)
                                    append(line)
                                }
                            } else {
                                contentList.add(line) //只搜索tag
                                append(line)
                            }
                        }
                    } else {
                        if (!TextUtils.isEmpty(searchContent)) {
                            if (line.contains(searchContent!!)) { //只搜索内容
                                contentList.add(line)
                                append(line)
                            }
                        } else {
                            contentList.add(line) //所有
                            append(line)
                        }
                    }
                }
            }
        }
    }
    private var rgGrade: RadioGroup? = null
    private fun init() {
        initView()
        Thread(Runnable {
            var logcatProcess: Process? = null
            var bufferedReader: BufferedReader? = null
            val log = StringBuilder()
            var line: String?
            try {
                while (isRuning) {
                    logcatProcess = Runtime.getRuntime().exec("logcat")
                    bufferedReader = BufferedReader(InputStreamReader(logcatProcess.inputStream))
                    while (bufferedReader.readLine().also { line = it } != null) {
                        log.append(line)
                        val message = mHandler.obtainMessage()
                        message.what = WHAT_NEXT_LOG
                        message.obj = line
                        mHandler.sendMessage(message)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }

    constructor(context: Context, @StyleRes themeResId: Int) : super(context, themeResId) {
        init()
    }

    /**
     * 设置搜索的内容(默认没有)
     *
     * @param searchContent
     */
    fun setSearchContent(searchContent: String?) {
        this.searchContent = searchContent
    }

    /**
     * 设置目标tag（默认没有）
     *
     * @param searchTag
     */
    fun setSearchLogTag(searchTag: String) {
        this.searchTag = searchTag
    }

    /**
     * 设置显示级别
     *
     * @param showGrade
     */
    fun setShowGrade(showGrade: Int) {
        this.showGrade = showGrade
    }

    /**
     * 设置是时候显示级别过滤
     *
     * @param isShowGrade 0 所有，1 System.out.println，2 警告级别,3 错误级别
     */
    fun setShowGrade(isShowGrade: Boolean) {
        if (isShowGrade) {
            rgGrade!!.visibility = View.VISIBLE
        } else {
            rgGrade!!.visibility = View.GONE
        }
    }

    /**
     * 追加内容
     *
     * @param line
     */
    private fun append(line: String) {
        if (showGrade == 0 || showGrade == 1) {
            if (line.contains(" E ")) {
                tvLog!!.append("\n\n")
                showError(line)
            } else if (line.contains(" W ")) {
                tvLog!!.append("\n\n")
                showWarning(line)
            } else {
                tvLog!!.append(
                    """


    $line
    """.trimIndent()
                )
            }
        } else if (showGrade == 2) {
            if (line.contains(" W ")) {
                tvLog!!.append("\n\n")
                showWarning(line)
            }
        } else if (showGrade == 3) {
            if (line.contains(" E ")) {
                tvLog!!.append("\n\n")
                showError(line)
            }
        }
        if (isAutoFullScroll) {
            refreshLogView()
        }
    }

    /**
     * 显示警告级别的日志
     *
     * @param line
     */
    private fun showWarning(line: String) {
        showLine(line, "#ba8a27")
    }

    /**
     * 显示错误级别的信息
     *
     * @param line
     */
    private fun showError(line: String) {
        showLine(line, "red")
    }

    private fun showLine(line: String, color: String) {
        if (line.contains("http://") || line.contains("https://")) {
            val url = line.substring(line.indexOf("http"))
            tvLog!!.append(Html.fromHtml("<font color='" + color + "'>" + line.substring(0, line.indexOf("http")) + "</font>"))
            tvLog!!.append(Html.fromHtml("<a href='$url'>$url</a>"))
        } else {
            tvLog!!.append(Html.fromHtml("<font color='$color'>$line</font>"))
        }
    }

    private var isRuning = true
    private var ivDwon: ImageView? = null
    private var etContent: OkEditText? = null
    var searchTag = "" //过滤tag

    constructor(context: Context) : super(context) {
        init()
    }

    /**
     * 关闭任务
     */
    fun closeTask() {
        isRuning = false
    }

    override fun dismiss() {
        closeTask()
        super.dismiss()
    }

    override fun onBackPressed() {
        dismiss()
        super.onBackPressed()
    }

    fun refreshLogView() {
        val offset = tvLog!!.lineCount * tvLog!!.lineHeight
        if (offset > tvLog!!.height) {
            tvLog!!.scrollTo(0, offset - tvLog!!.height)
        }
    }

    private var y1 = 0f
    private var y2 = 0f
    private fun initView() {
        val view = View.inflate(context, R.layout.dialog_logcat, null)
        setContentView(view)
        val tvTitle = view.findViewById<View>(R.id.tv_title) as TextView
        tvTitle.text = title
        //日志级别
        rgGrade = view.findViewById<View>(R.id.rg_grade) as RadioGroup
        rgGrade!!.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_all) {
                showGrade = 0
                searchContent("")
            } else if (checkedId == R.id.rb_system_out) {
                showGrade = 1
                searchContent("System.out")
            } else if (checkedId == R.id.rb_warming) {
                showGrade = 2
                searchContent("")
            } else if (checkedId == R.id.rb_error) {
                showGrade = 3
                searchContent("")
            }
        }
        etContent = view.findViewById<View>(R.id.et_content) as OkEditText
        etContent!!.setOnClickOkListener(object : OnClickOkListener {
            override fun onOk(content: String?) {
                Toast.makeText(context, "开始搜索 $content", Toast.LENGTH_SHORT).show()
                searchContent(content)
            }
        })
        tvLog = view.findViewById<View>(R.id.tv_consol) as TextView
        tvLog!!.movementMethod = ScrollingMovementMethod.getInstance()
        tvLog!!.movementMethod = LinkMovementMethod.getInstance()
        tvLog!!.setOnTouchListener { v, event -> //继承了Activity的onTouchEvent方法，直接监听点击事件
            if (event.action == MotionEvent.ACTION_DOWN) {
                //当手指按下的时候
                y1 = event.y
            }
            if (event.action == MotionEvent.ACTION_UP) {
                //当手指离开的时候
                y2 = event.y
                if (y2 - y1 > 50) {
                    isAutoFullScroll = false
                    ivDwon!!.visibility = View.VISIBLE
                }
            }
            false
        }
        view.findViewById<View>(R.id.iv_close).setOnClickListener { dismiss() }
        ivDwon = view.findViewById<View>(R.id.iv_down) as ImageView
        ivDwon!!.visibility = View.GONE
        ivDwon!!.setOnClickListener {
            isAutoFullScroll = true
            ivDwon!!.visibility = View.GONE
        }
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置,
         * 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = wm.defaultDisplay.width
        val height = wm.defaultDisplay.height
        val dialogWindow = window
        val lp = dialogWindow!!.attributes
        lp.width = width * 7 / 8 // 宽度
        lp.height = height * 7 / 8 // 高度
        dialogWindow.attributes = lp
    }

    /**
     * 设置控制台的title
     *
     * @param title
     */
    fun setTitle(title: String) {
        this.title = title
    }

    /**
     * 搜索内容
     *
     * @param content
     */
    private fun searchContent(content: String?) {
        searchContent = content
        tvLog!!.text = "--------------search------------\n"
        for (item in contentList) {
            if (item.contains(content!!)) {
                append(item)
            }
        }
    }

    companion object {
        private const val WHAT_NEXT_LOG = 778
    }
}