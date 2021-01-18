package com.pwj.record.ui.file

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import com.pwj.record.bean.FileInfoBean
import com.pwj.record.utils.FileTypeUtils
import kotlinx.android.synthetic.main.activity_comment_list.view.*
import kotlinx.android.synthetic.main.activity_file_manager.*
import kotlinx.android.synthetic.main.toolbar_common.*
import java.util.*

class FileManagerActivity : AppCompatActivity(), View.OnClickListener {

    private val REQUEST_FOR_SELECTED_FILES = 730
    private val ALL_FILE_FILES = 1
    private val ALL_VIDEO_FILES = 2
    private val ALL_AUDIO_FILES = 3
    private val ALL_OTHER_FILES = 4
    private val ALL_RAM_FILES = 5
    private val ALL_SD_FILES = 6
    private val ROOT_DIR = 100
    private val SD_CARD_ROOT_DIR = 101
    private val FILE_TRAVERSE_TYPE_ONE = 200
    private val FILE_TRAVERSE_TYPE_TWO = 201
    private lateinit var mPath: Array<String>
    private var mSDCardPath: String? = null
    private var mSDCardPathOne: String? = null
    private var mSDCardPathTwo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manager)
        initView()
    }

    private fun initView() {
        simple_back.visibility = View.VISIBLE
        simple_title.text = ""
        simple_title.visibility = View.VISIBLE
        simple_back.setOnClickListener(this)

        mFileTextView.setOnClickListener(this)
        mVideoTextView.setOnClickListener(this)
        mAudioTextView.setOnClickListener(this)
        mOtherTextView.setOnClickListener(this)
        mMobileMemoryTextView.setOnClickListener(this)
        mSDCardTextView.setOnClickListener(this)
        mSDCardOneLinearLayout.setOnClickListener(this)
        mSDCardTwoLinearLayout.setOnClickListener(this)

        mPath = FileTypeUtils.getExternalStorageDirectories(this)
        if (mPath.size == 1) {
            mSDCardLinearLayout.visibility = View.VISIBLE
            mSDCardPath = mPath[0]
        }
        if (mPath.size == 2) {
            mSDCardPathOne = mPath[0]
            mSDCardPathTwo = mPath[1]
            mSDCardOneLinearLayout.visibility = View.VISIBLE
            mSDCardTwoLinearLayout.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.simple_back) {
            finish()
        } else {
            val intent = Intent(this, FileListActivity::class.java)
            if (v === mFileTextView) {
                intent.putExtra("rootDirType", ROOT_DIR)
                intent.putExtra("fileFilterType", ALL_FILE_FILES)
                intent.putExtra("fileTraverseType", FILE_TRAVERSE_TYPE_ONE)
            }
            if (v === mVideoTextView) {
                intent.putExtra("rootDirType", ROOT_DIR)
                intent.putExtra("fileFilterType", ALL_VIDEO_FILES)
                intent.putExtra("fileTraverseType", FILE_TRAVERSE_TYPE_ONE)
            }
            if (v === mAudioTextView) {
                intent.putExtra("rootDirType", ROOT_DIR)
                intent.putExtra("fileFilterType", ALL_AUDIO_FILES)
                intent.putExtra("fileTraverseType", FILE_TRAVERSE_TYPE_ONE)
            }
            if (v === mOtherTextView) {
                intent.putExtra("rootDirType", ROOT_DIR)
                intent.putExtra("fileFilterType", ALL_OTHER_FILES)
                intent.putExtra("fileTraverseType", FILE_TRAVERSE_TYPE_ONE)
            }
            if (v === mMobileMemoryTextView) {
                intent.putExtra("rootDirType", ROOT_DIR)
                intent.putExtra("fileFilterType", ALL_RAM_FILES)
                intent.putExtra("fileTraverseType", FILE_TRAVERSE_TYPE_TWO)
            }
            if (v === mSDCardTextView) {
                intent.putExtra("rootDirType", SD_CARD_ROOT_DIR)
                intent.putExtra("fileFilterType", ALL_SD_FILES)
                intent.putExtra("fileTraverseType", FILE_TRAVERSE_TYPE_TWO)
                intent.putExtra("rootDir", mSDCardPath)
            }
            if (v === mSDCardOneLinearLayout) {
                intent.putExtra("rootDirType", SD_CARD_ROOT_DIR)
                intent.putExtra("fileFilterType", ALL_SD_FILES)
                intent.putExtra("fileTraverseType", FILE_TRAVERSE_TYPE_TWO)
                intent.putExtra("rootDir", mSDCardPathOne)
            }
            if (v === mSDCardTwoLinearLayout) {
                intent.putExtra("rootDirType", SD_CARD_ROOT_DIR)
                intent.putExtra("fileFilterType", ALL_SD_FILES)
                intent.putExtra("fileTraverseType", FILE_TRAVERSE_TYPE_TWO)
                intent.putExtra("rootDir", mSDCardPathTwo)
            }
            startActivityForResult(intent, REQUEST_FOR_SELECTED_FILES)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FOR_SELECTED_FILES && data != null) {
            val selectedFileInfos: HashSet<FileInfoBean> = data.getSerializableExtra("selectedFiles") as HashSet<FileInfoBean>
            val intent = Intent()
            intent.putExtra("sendSelectedFiles", selectedFileInfos)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}