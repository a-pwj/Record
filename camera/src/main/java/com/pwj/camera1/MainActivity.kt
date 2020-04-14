package com.pwj.camera1

import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Author:           pwj
 * Date:             2020/3/26 9:34
 * FileName:         MainActivity
 */
class MainActivity : AppCompatActivity(){

    private lateinit var mVideoRecorderUtils: VideoRecorderUtils
    private lateinit var path: String
    private lateinit var name: String
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        mVideoRecorderUtils = VideoRecorderUtils()
        mVideoRecorderUtils.create(mIdSvVideo, VideoRecorderUtils.WH_720X480)
        path = Environment.getExternalStorageDirectory().absolutePath

        mIdIvSnap.setOnClickListener { view: View? ->
            if (!isRecording) {
                mVideoRecorderUtils.startRecord(path, "Video")
            } else {
                mVideoRecorderUtils.stopRecord()
            }
            isRecording = !isRecording
        }

    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        mVideoRecorderUtils.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mVideoRecorderUtils.destroy()
    }
}