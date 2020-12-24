package com.pwj.record.ui.video

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwj.record.R
import com.pwj.record.ext.showToast
import com.pwj.record.ext.startExtActivity
import com.pwj.record.ui.adpter.SimpleAdapter
import kotlinx.android.synthetic.main.activity_main_video.*


/**
 * @Author:          pwj
 * @Date:            2020-12-15 9:45:51
 * @FileName:        MainVideoActivity
 * @Description:     description
 */
class MainVideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_video)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainVideoActivity)
            adapter = SimpleAdapter().apply {
                setData(mockData())
                OnItemClickListenr = { position ->
                    resetViewState()
                    when (position) {
                        0 -> {
                            /**
                            1. 把视频文件放到项目工程的 res/raw 文件下。
                            2. 视频文件必须是Android支持视频格式（3gp,wmv,mp4），并且命名必须是小写字母，数据，下划线，例如：my_video_file.mp4
                            3. 当你是在代码里面引用这个资源文件时，你必须使用R statics类，同时去掉文件的后缀：R.raw.my_video_file
                            4. 这个Activity class 应该有一个 helper 方法 getPackageName()，这样便于你在你的代码里面构造正确的URI。
                             */
                            videoView.visibility = View.VISIBLE

                            val uri = "android.resource://" + packageName + "/" + R.raw.local_row
                            videoView.setMediaController(MediaController(this@MainVideoActivity))
                            videoView.setVideoURI(Uri.parse(uri))
                            videoView.start()
                            videoView.requestFocus()
                        }
                        1 -> {
                            videoView.visibility = View.VISIBLE

                            try {
                                val uri = Uri.parse(Environment.getExternalStorageDirectory().getPath().toString() + "/Test_Movie.m4v")
                                //调用系统自带的播放器
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setDataAndType(uri, "video/mp4")
                                startActivity(intent)
                            } catch (e: Exception) {
                                showToast("调用失败")
                            }
                        }
                        2 -> {
                            surfaceVideoView.visibility = View.VISIBLE

                            startSurfaceViewToPlayVideo()
                        }
                        3 -> {
                            textureVideoView.visibility = View.VISIBLE
                            startTextureViewToPlayVideo()
                        }
                        4->startExtActivity<MediaRecordActivity>()
                        5->startExtActivity<MediaRecordActivity>()
                    }
                }
            }
        }
    }


    private fun mockData(): Collection<String> {
        return mutableListOf<String>().apply {
            add("调用 res-raw")
            add("调用其自带的播放器")
            add("SurfaceView")
            add("textureVideoView")
            add("AudioRecord 录制音频")
            add("Media 录制音频")
        }
    }

    private fun resetViewState() {
        videoView.visibility = View.GONE
        surfaceVideoView.visibility = View.GONE
        textureVideoView.visibility = View.GONE
    }


    private fun startTextureViewToPlayVideo() {
        val uri = "android.resource://" + packageName + "/" + R.raw.local_row
        // 防止锁屏
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //TextureView特性
        textureVideoView.alpha = 0.5f
        textureVideoView.rotation = 45.0f
        //textureVideoView.pause();
        textureVideoView.setVideoPath(uri)

        textureVideoView.setOnPreparedListener {
            textureVideoView.setLooping(true)
            textureVideoView.start()
        }

        if (textureVideoView.isPrepared) {
            textureVideoView.setLooping(true)
            textureVideoView.start()
        }
    }

    /**
     * 是否需要回复播放
     */
    private var mNeedResume = false

    private fun startSurfaceViewToPlayVideo() {
        val uri = "android.resource://" + packageName + "/" + R.raw.local_row
        // 防止锁屏
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        surfaceVideoView.setOnPreparedListener {
            surfaceVideoView.setVolume(MySurfaceVideoView.getSystemVolumn(this));
            surfaceVideoView.start();
        }
        surfaceVideoView.setOnPlayStateListener { }
        surfaceVideoView.setOnErrorListener { mp, what, extra ->
            if (!isFinishing) {

            }
            false
        }
        surfaceVideoView.setOnClickListener { }
        surfaceVideoView.setOnInfoListener { mp, what, extra ->
            when (what) {
                MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING -> {
                    //音频和视频数据不正确
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                    if (!isFinishing) {
                        surfaceVideoView.pause()
                    }
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                    if (!isFinishing) {
                        surfaceVideoView.start()
                    }
                }
                MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        surfaceVideoView.background = null
                    } else {
                        surfaceVideoView.setBackgroundDrawable(null)
                    }
                }
            }
            false
        }
        surfaceVideoView.setOnCompletionListener {
            if (!isFinishing) surfaceVideoView.reOpen()
        }

        surfaceVideoView.setVideoPath(uri)
    }

    override fun onResume() {
        super.onResume()
        if (surfaceVideoView.visibility == View.VISIBLE) {
            mNeedResume = false;
            if (surfaceVideoView.isRelease)
                surfaceVideoView.reOpen()
            else
                surfaceVideoView.start();
        }
    }

    override fun onPause() {
        super.onPause()
        if (surfaceVideoView.visibility == View.VISIBLE) {
            if (surfaceVideoView.isPlaying()) {
                mNeedResume = true
                surfaceVideoView.pause()
            }
        }
    }

    override fun onDestroy() {
        if (surfaceVideoView.visibility == View.VISIBLE) {
            surfaceVideoView.release()
        }
        super.onDestroy()
    }

}
