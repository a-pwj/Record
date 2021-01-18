package com.pwj.record.ui.video

import android.media.*
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import com.pwj.record.ui.video.record.Mp3Encoder
import kotlinx.android.synthetic.main.activity_media_record_two.*
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile

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
        startAudioRecordAudioTrackPlay.setOnClickListener(this)
        stopAudioRecordAudioTrackPlay.setOnClickListener(this)
        startMP3Record.setOnClickListener(this)
        stopMP3Record.setOnClickListener(this)

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
                stopAudioRecord()
            }
            R.id.startAudioRecordAudioTrackPlay -> {
                startAudioRecordAudioTrackPlay()
            }
            R.id.stopAudioRecordAudioTrackPlay -> {
                stopAudioRecordAudioTrackPlay()
            }
            R.id.startMP3Record -> {
                startMP3Record()
            }
            R.id.stopMP3Record -> {
                stopMP3Record()
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // MediaRecord 录制音频
    ///////////////////////////////////////////////////////////////////////////
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

    ///////////////////////////////////////////////////////////////////////////
    // AudioRecord 录制音频
    ///////////////////////////////////////////////////////////////////////////
    //录音状态
    private var isRecording = true;

    private fun startAudioRecord() {
        Thread {
            //音源
            val audioSource = MediaRecorder.AudioSource.MIC
            //采样率
            val sampleRate = 44100
            //声道数 双声道
            val channelConfig = AudioFormat.CHANNEL_IN_STEREO
            //采样位数
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT
            //获取最小缓存区大小
            val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
            //创建录音对象
            val audioRecord = AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, minBufferSize)

            try {
                //创建随机读写流
                val mRandomAccessFile = RandomAccessFile("${cacheDir}+/${System.currentTimeMillis()}.wav", "rw")
                //留出文件头的位置
                mRandomAccessFile.seek(44)
                val buffer = ByteArray(minBufferSize)

                //录音中
                audioRecord.startRecording()
                isRecording = true
                while (isRecording) {
                    val readSize = audioRecord.read(buffer, 0, minBufferSize)
                    mRandomAccessFile.write(buffer, 0, readSize)
                }

                //录音停止
                audioRecord.stop()
                audioRecord.release()

                //写文件头
                writeWaveHeader(mRandomAccessFile, mRandomAccessFile.length(), sampleRate.toLong(), 2, (sampleRate * 16 * 2 / 8).toLong())
                mRandomAccessFile.close()
            } catch (e: Exception) {
            }
        }.start()
    }

    private fun stopAudioRecord() {
        isRecording = false
    }


    /**
     * 为 wav 文件添加文件头，前提是在头部预留了 44字节空间
     *
     * @param raf               随机读写流
     * @param fileLength        文件总长
     * @param sampleRate        采样率
     * @param channels          声道数量
     * @param byteRate          码率 = 采样率 * 采样位数 * 声道数 / 8
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun writeWaveHeader(raf: RandomAccessFile, fileLength: Long, sampleRate: Long, channels: Int, byteRate: Long) {
        val totalDataLen = fileLength + 36
        val header = ByteArray(44)
        header[0] = 'R'.toByte() // RIFF/WAVE header
        header[1] = 'I'.toByte()
        header[2] = 'F'.toByte()
        header[3] = 'F'.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()
        header[8] = 'W'.toByte()
        header[9] = 'A'.toByte()
        header[10] = 'V'.toByte()
        header[11] = 'E'.toByte()
        header[12] = 'f'.toByte() // 'fmt ' chunk
        header[13] = 'm'.toByte()
        header[14] = 't'.toByte()
        header[15] = ' '.toByte()
        header[16] = 16.toByte() // 4 bytes: size of 'fmt ' chunk
        header[17] = 0.toByte()
        header[18] = 0.toByte()
        header[19] = 0.toByte()
        header[20] = 1.toByte() // format = 1
        header[21] = 0.toByte()
        header[22] = channels.toByte()
        header[23] = 0.toByte()
        header[24] = (sampleRate and 0xff).toByte()
        header[25] = (sampleRate shr 8 and 0xff).toByte()
        header[26] = (sampleRate shr 16 and 0xff).toByte()
        header[27] = (sampleRate shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        header[32] = (2 * 16 / 8).toByte() // block align
        header[33] = 0.toByte()
        header[34] = 16.toByte() // bits per sample
        header[35] = 0.toByte()
        header[36] = 'd'.toByte()
        header[37] = 'a'.toByte()
        header[38] = 't'.toByte()
        header[39] = 'a'.toByte()
        header[40] = (fileLength and 0xff).toByte()
        header[41] = (fileLength shr 8 and 0xff).toByte()
        header[42] = (fileLength shr 16 and 0xff).toByte()
        header[43] = (fileLength shr 24 and 0xff).toByte()
        raf.seek(0)
        raf.write(header, 0, 44)
    }


    ///////////////////////////////////////////////////////////////////////////
    // AudioRecord 录制音频  AudioTrack 播放
    ///////////////////////////////////////////////////////////////////////////
    private var isRecordingAudioTrack = true

    private fun startAudioRecordAudioTrackPlay() {
        Thread() {
            //音源
            val audioSource = MediaRecorder.AudioSource.MIC
            //采样率
            val sampleRate = 8000
            //声道数 双声道
            val channelConfig = AudioFormat.CHANNEL_IN_STEREO
            //采样位数
            val audioFormat = AudioFormat.ENCODING_PCM_8BIT
            //获取录音最小缓存区大小
            val recorderBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
            //创建录音对象
            val audioRecord = AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, recorderBufferSize)

            //音频类型
            val streamType = AudioManager.STREAM_MUSIC
            //静态音频还是音频流
            val mode = AudioTrack.MODE_STREAM
            //获取播放最小缓存区大小
            val playerBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat)
            //创建播放对象
            val audioTrack = AudioTrack(streamType, sampleRate, channelConfig, audioFormat, playerBufferSize, mode)
//            AudioTrack(
//                AudioAttributes.Builder().setLegacyStreamType(streamType).build(),
//                AudioFormat.Builder().setChannelMask(channelConfig).setEncoding(audioFormat).setSampleRate(sampleRate).build(),
//                playerBufferSize,
//                mode, AudioManager.AUDIO_SESSION_ID_GENERATE
//            )

            //缓存区
            val buffer = ByteArray(recorderBufferSize)

            //录音中
            audioTrack.play()
            audioRecord.startRecording()
            isRecordingAudioTrack = true
            while (isRecordingAudioTrack) {
                audioRecord.read(buffer, 0, recorderBufferSize)
                audioTrack.write(buffer, 0, buffer.size)
            }

            //停止录音
            audioRecord.stop()
            audioTrack.stop()
            audioRecord.release()
            audioTrack.release()
        }.start()
    }

    private fun stopAudioRecordAudioTrackPlay() {
        isRecordingAudioTrack = false
    }

    ///////////////////////////////////////////////////////////////////////////
    // 录制MP3格式音频
    ///////////////////////////////////////////////////////////////////////////

    private var isRecordingMP3 = true

    private fun startMP3Record() {
        Thread() {
            //音源
            val audioSource = MediaRecorder.AudioSource.MIC
            //采样率
            val sampleRate = 44100
            //声道 单声道
            val channelConfig = AudioFormat.CHANNEL_IN_MONO
            //采样位数
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT
            //录音缓存区大小
            val bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
            val audioRecord = AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, bufferSizeInBytes)
            var fos: FileOutputStream
            try {

                fos = FileOutputStream("${cacheDir}+/${System.currentTimeMillis()}.mp3")
                Mp3Encoder.init(sampleRate, 2, sampleRate, 128, 5)
                val buffer = ShortArray(bufferSizeInBytes)
                val mp3buffer  = ByteArray((7200 + buffer.size * 1.25).toInt())
                audioRecord.startRecording()
                isRecordingMP3 = true
                while (isRecordingMP3 && audioRecord.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    val readSize = audioRecord.read(buffer, 0, bufferSizeInBytes)
                    if (readSize > 0) {
                        val encodeSize = Mp3Encoder.encode(buffer, buffer, readSize, mp3buffer)
                        if (encodeSize>0){
                            try {
                                fos.write(mp3buffer,0,encodeSize)
                            }catch (e:Exception){

                            }
                        }
                    }

                }

            }catch (e:Exception){
                //todo 未完
            }


        }.start()
    }

    private fun stopMP3Record() {

    }
}
