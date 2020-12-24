package com.pwj.record.ui.video

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import kotlinx.android.synthetic.main.activity_media_record_two.*
import java.io.IOException

/**
 * @Author:          pwj
 * @Date:            2020-12-22 14:53:40
 * @FileName:        MediaRecordTwoActivity
 * @Description:     description
 */
class MediaRecordTwoActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_record_two)

        startMediaRecord.setOnClickListener(this)
        stopMediaRecord.setOnClickListener(this)
        startAudioRecord.setOnClickListener(this)
        stopAudioRecord.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.startMediaRecord -> {
                startMediaRecord()
            }
            R.id.stopMediaRecord -> {
                stopMediaRecord()
            }
            R.id.startAudioRecord -> {
                startAudioRecord()
            }
            R.id.stopAudioRecord -> {
//                stopAudioRecord()
            }
        }
    }


    /*******************************MediaRecord 录制音频******************************************/
    private val mRecorder by lazy { MediaRecorder() }

    private fun startMediaRecord() {
        //设置声音来源，mic手机麦克风
        mRecorder.run {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            //设置音频格式aac
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            //设置录音文件
            setOutputFile("$filesDir/media_record${System.currentTimeMillis()}.aac")
            //设置编码器
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        }
        try {
            mRecorder.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mRecorder.start()
    }

    private fun stopMediaRecord() {
        mRecorder.stop()
        mRecorder.release()
    }

    /*******************************AudioRecord 录制音频******************************************/
    //录音状态
    private var isRecording = true;

    private fun startAudioRecord() {
        Thread{
            //音源
            val audioSource = MediaRecorder.AudioSource.MIC
            //采样率
            val sampleRate = 44100
            //声道数 双声道
            val channelConfig = AudioFormat.CHANNEL_IN_STEREO
            //采样位数
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT
            //获取最小缓存区大小
            val minBufferSize = AudioRecord.getMinBufferSize(sampleRate,channelConfig,audioFormat)
            //创建录音对象
//            val audioRecord = AudioRecord(audioSource,sampleRate,)


        }.start()

    }

}
