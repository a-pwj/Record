package com.pwj.record.ui.tools.comment

import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import java.util.*

/**
 * @Author:          pwj
 * @Date:            2020-5-25 10:58:07
 * @FileName:        CommentListActivity
 * @Description:     description
 */
class CommentListActivity : AppCompatActivity() {

    private var mCommentListTextView: CommentListTextView? = null
    private var mTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_list)

        mCommentListTextView = findViewById<CommentListTextView>(R.id.commentlist)
        mTextView = findViewById<TextView>(R.id.log)
        test()
    }

    private fun test() {
        mTextView!!.movementMethod = ScrollingMovementMethod.getInstance()
        mCommentListTextView!!.maxlines = 6
        mCommentListTextView!!.moreStr = "查看更多"
        mCommentListTextView!!.nameColor = Color.parseColor("#fe671e")
        mCommentListTextView!!.commentColor = Color.parseColor("#242424")
        mCommentListTextView!!.talkStr = "回复"
        mCommentListTextView!!.talkColor = Color.parseColor("#242424")
        val mCommentInfos: MutableList<CommentListTextView.CommentInfo> = ArrayList<CommentListTextView.CommentInfo>()
        mCommentInfos.add(CommentListTextView.CommentInfo().setID(1111).setComment("今天天气真好啊！11").setNickname("张三").setTonickname("赵四"))
        mCommentInfos.add(CommentListTextView.CommentInfo().setID(2222).setComment("今天天气真好啊！22").setNickname("赵四"))
        mCommentInfos.add(CommentListTextView.CommentInfo().setID(3333).setComment("今天天气真好啊！33").setNickname("王五").setTonickname("小三"))
        mCommentInfos.add(CommentListTextView.CommentInfo().setID(4444).setComment("今天天气真好啊！44").setNickname("小三").setTonickname("王五"))
        mCommentInfos.add(CommentListTextView.CommentInfo().setID(5555).setComment("今天天气真好啊！55").setNickname("李大"))
        mCommentInfos.add(CommentListTextView.CommentInfo().setID(6666).setComment("今天天气真好啊！66").setNickname("小三").setTonickname("王五"))
        mCommentInfos.add(CommentListTextView.CommentInfo().setID(7777).setComment("今天天气真好啊！77").setNickname("李大").setTonickname("张三"))
        mCommentInfos.add(CommentListTextView.CommentInfo().setID(8888).setComment("今天天气真好啊！88").setNickname("小三").setTonickname("王五"))
        mCommentInfos.add(CommentListTextView.CommentInfo().setID(9999).setComment("今天天气真好啊！99").setNickname("李大").setTonickname("张三"))
        mCommentListTextView!!.setData(mCommentInfos)
        mCommentListTextView!!.setonCommentListener(object : CommentListTextView.onCommentListener {
            override fun onNickNameClick(position: Int, mInfo: CommentListTextView.CommentInfo) {
                mTextView!!.append("onNickNameClick  position = [$position], mInfo = [$mInfo]\r\n")
            }

            override fun onToNickNameClick(position: Int, mInfo: CommentListTextView.CommentInfo) {
                mTextView!!.append("onToNickNameClick  position = [$position], mInfo = [$mInfo]\r\n")
            }

            override   fun onCommentItemClick(position: Int, mInfo: CommentListTextView.CommentInfo) {
                mTextView!!.append("onCommentItemClick  position = [$position], mInfo = [$mInfo]\r\n")
            }

            override  fun onOtherClick() {
                mTextView!!.append("""     onOtherClick         """.trimIndent())
            }
        })
    }
}
