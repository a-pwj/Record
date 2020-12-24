package com.pwj.record.ui.video

import android.content.Intent
import android.media.AudioFormat
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.BuildConfig
import com.pwj.record.R
import com.pwj.record.ui.video.fftlib.ByteUtils
import com.pwj.record.ui.video.record.RecordHelper.*
import com.pwj.record.ui.video.record.RecordManager
import com.pwj.record.ui.video.utils.WavUtils
import com.pwj.record.utils.log.ZLog
import kotlinx.android.synthetic.main.activity_media_record.*
import java.util.*


/**
 * @Author:          pwj
 * @Date:            2020-12-15 19:55:23
 * @FileName:        MediaRecordActivity
 * @Description:     description
 */
/*
MediaRecorder主要函数：
setAudioChannels(int numChannels) 设置录制的音频通道数
setAudioEncoder(int audio_encoder) 设置audio的编码格式
setAudioEncodingBitRate(int bitRate) 设置录制的音频编码比特率
setAudioSamplingRate(int samplingRate) 设置录制的音频采样率
setAudioSource(int audio_source) 设置用于录制的音源
setAuxiliaryOutputFile(String path) 辅助时间的推移视频文件的路径传递
setAuxiliaryOutputFile(FileDescriptor fd)在文件描述符传递的辅助时间的推移视频
setCamera(Camera c) 设置一个recording的摄像头
setCaptureRate(double fps) 设置视频帧的捕获率
setMaxDuration(int max_duration_ms) 设置记录会话的最大持续时间（毫秒）
setMaxFileSize(long max_filesize_bytes) 设置记录会话的最大大小（以字节为单位）
setOutputFile(FileDescriptor fd) 传递要写入的文件的文件描述符
setOutputFile(String path) 设置输出文件的路径
setOutputFormat(int output_format) 设置在录制过程中产生的输出文件的格式
setPreviewDisplay(Surface sv) 表面设置显示记录媒体（视频）的预览
setVideoEncoder(int video_encoder) 设置视频编码器，用于录制
setVideoEncodingBitRate(int bitRate) 设置录制的视频编码比特率
setVideoFrameRate(int rate) 设置要捕获的视频帧速率
setVideoSize(int width, int height) 设置要捕获的视频的宽度和高度
setVideoSource(int video_source) 开始捕捉和编码数据到setOutputFile（指定的文件）
setLocation(float latitude, float longitude) 设置并存储在输出文件中的地理数据（经度和纬度）
setProfile(CamcorderProfile profile) 指定CamcorderProfile对象
setOrientationHint(int degrees)设置输出的视频播放的方向提示
setOnErrorListener(MediaRecorder.OnErrorListener l)注册一个用于记录录制时出现的错误的监听器
setOnInfoListener(MediaRecorder.OnInfoListener listener)注册一个用于记录录制时出现的信息事件
getMaxAmplitude() 获取在前一次调用此方法之后录音中出现的最大振幅
prepare()准备录制。
release()释放资源
reset()将MediaRecorder设为空闲状态
start()开始录制
stop()停止录制


MediaRecorder主要配置参数：
1.）视频编码格式MediaRecorder.VideoEncoder
default，H263，H264，MPEG_4_SP，VP8
2.）音频编码格式MediaRecorder.AudioEncoder
default，AAC，HE_AAC，AAC_ELD，AMR_NB，AMR_WB，VORBIS
3.）视频资源获取方式MediaRecorder.VideoSource
default，CAMERA，SURFACE
4.）音频资源获取方式MediaRecorder.AudioSource
defalut，camcorder，mic，voice_call，voice_communication,voice_downlink,voice_recognition, voice_uplink
5.）资源输出格式MediaRecorder.OutputFormat
amr_nb，amr_wb,default,mpeg_4,raw_amr,three_gpp，aac_adif， aac_adts， output_format_rtp_avp， output_format_mpeg2ts ，webm

 */
class MediaRecordActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private val TAG: String = MediaRecordActivity::class.java.getSimpleName()
    private var isStart = false
    private var isPause = false
    val recordManager: RecordManager = RecordManager.getInstance()
    private val STYLE_DATA = arrayOf("STYLE_ALL", "STYLE_NOTHING", "STYLE_WAVE", "STYLE_HOLLOW_LUMP")

    init {
        ZLog.w("zlwTest", "TEST-----------------")
        val header1 = WavUtils.generateWavFileHeader(1024, 16000, 1, 16)
        val header2 = WavUtils.generateWavFileHeader(1024, 16000, 1, 16)

        ZLog.d("zlwTest", String.format("Wav1: %s", WavUtils.headerToString(header1)))
        ZLog.d("zlwTest", String.format("Wav2: %s", WavUtils.headerToString(header2)))

        ZLog.w("zlwTest", "TEST-2----------------")

        ZLog.d("zlwTest", String.format("Wav1: %s", ByteUtils.toString(header1)))
        ZLog.d("zlwTest", String.format("Wav2: %s", ByteUtils.toString(header2)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_record)

        initAudioView();
        initEvent();
        initRecord();
        btRecord.setOnClickListener(this)
        btStop.setOnClickListener(this)
        jumpTestActivity.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        doStop()
        initRecordEvent()
    }

    override fun onStop() {
        super.onStop()
        doStop()
    }

    private fun initAudioView() {
        tvState.setVisibility(View.GONE)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, STYLE_DATA)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spUpStyle.setAdapter(adapter)
        spDownStyle.setAdapter(adapter)
        spUpStyle.setOnItemSelectedListener(this)
        spDownStyle.setOnItemSelectedListener(this)
    }

    private fun initEvent() {
        recordManager.changeFormat(RecordFormat.WAV)
        recordManager.changeRecordConfig(recordManager.recordConfig.setSampleRate(16000))
        recordManager.changeRecordConfig(recordManager.recordConfig.setEncodingConfig(AudioFormat.ENCODING_PCM_16BIT))

        rgAudioFormat.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbPcm -> recordManager.changeFormat(RecordFormat.PCM)
                R.id.rbMp3 -> recordManager.changeFormat(RecordFormat.MP3)
                R.id.rbWav -> recordManager.changeFormat(RecordFormat.WAV)
                else -> {
                }
            }
        })
        rgSimpleRate.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb8K -> recordManager.changeRecordConfig(recordManager.recordConfig.setSampleRate(8000))
                R.id.rb16K -> recordManager.changeRecordConfig(recordManager.recordConfig.setSampleRate(16000))
                R.id.rb44K -> recordManager.changeRecordConfig(recordManager.recordConfig.setSampleRate(44100))
                else -> {
                }
            }
        })
        tbEncoding.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb8Bit -> recordManager.changeRecordConfig(recordManager.recordConfig.setEncodingConfig(AudioFormat.ENCODING_PCM_8BIT))
                R.id.rb16Bit -> recordManager.changeRecordConfig(recordManager.recordConfig.setEncodingConfig(AudioFormat.ENCODING_PCM_16BIT))
                else -> {
                }
            }
        })
    }

    private fun initRecord() {
        recordManager.init(application, BuildConfig.DEBUG)
        recordManager.changeFormat(RecordFormat.WAV)
        val recordDir = "$filesDir/record/"
        recordManager.changeRecordDir(recordDir)
        initRecordEvent()
    }

    private fun initRecordEvent() {
        recordManager.setRecordStateListener(object : RecordStateListener {
            override fun onStateChange(state: RecordState) {
                ZLog.i(TAG, String.format("onStateChange %s", state.name));
                when (state) {
                    RecordState.PAUSE -> tvState.setText("暂停中")
                    RecordState.IDLE -> tvState.setText("空闲中")
                    RecordState.RECORDING -> tvState.setText("录音中")
                    RecordState.STOP -> tvState.setText("停止")
                    RecordState.FINISH -> {
                        tvState.setText("录音结束")
                        tvSoundSize.setText("---")
                    }
                    else -> {
                    }
                }
            }

            override fun onError(error: String) {
                ZLog.i(TAG, String.format("onError %s", error))
            }
        })
        recordManager.setRecordSoundSizeListener { soundSize -> tvSoundSize.setText(java.lang.String.format(Locale.getDefault(), "声音大小：%s db", soundSize)) }
        recordManager.setRecordResultListener { result -> Toast.makeText(this, "录音文件： " + result.absolutePath, Toast.LENGTH_SHORT).show() }
        recordManager.setRecordFftDataListener { data -> audioView.setWaveData(data) }
    }


    override fun onClick(view: View?) {
        when (view?.getId()) {
            R.id.btRecord -> doPlay()
            R.id.btStop -> doStop()
            R.id.jumpTestActivity -> startActivity(Intent(this, TestHzActivity::class.java))
            else -> {
            }
        }
    }

    private fun doStop() {
        recordManager.stop()
        btRecord.setText("开始")
        isPause = false
        isStart = false
    }

    private fun doPlay() {
        if (isStart) {
            recordManager.pause()
            btRecord.setText("开始")
            isPause = true
            isStart = false
        } else {
            if (isPause) {
                recordManager.resume()
            } else {
                recordManager.start()
            }
            btRecord.setText("暂停")
            isStart = true
        }
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        when (parent.id) {
            R.id.spUpStyle -> audioView.setStyle(AudioView.ShowStyle.getStyle(STYLE_DATA[position]), audioView.getDownStyle())
            R.id.spDownStyle -> audioView.setStyle(audioView.getUpStyle(), AudioView.ShowStyle.getStyle(STYLE_DATA[position]))
            else -> {
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //nothing
    }

}
