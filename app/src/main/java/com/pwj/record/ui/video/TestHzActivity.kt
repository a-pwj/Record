package com.pwj.record.ui.video

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.BuildConfig
import com.pwj.record.R
import com.pwj.record.ioc.OnClick
import com.pwj.record.ui.video.record.RecordHelper.*
import com.pwj.record.ui.video.record.RecordManager
import com.pwj.record.utils.log.ZLog
import kotlinx.android.synthetic.main.activity_test_hz.*
import java.util.*


/**
 * @Author:          pwj
 * @Date:            2020-12-16 16:13:00
 * @FileName:        TestHzActivity
 * @Description:     description
 */
class TestHzActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val TAG = TestHzActivity::class.java.simpleName

    private var isStart = false
    private var isPause = false
    val recordManager = RecordManager.getInstance()
    private val STYLE_DATA = arrayOf("STYLE_ALL", "STYLE_NOTHING", "STYLE_WAVE", "STYLE_HOLLOW_LUMP")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_test_hz)

        initAudioView();
    }

    override fun onResume() {
        super.onResume()
        initRecord()
    }

    override fun onStop() {
        super.onStop()
        recordManager.stop()
    }

    private fun initAudioView() {
        audioView.setStyle(AudioView.ShowStyle.STYLE_ALL, AudioView.ShowStyle.STYLE_ALL)
        tvState.setVisibility(View.GONE)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, STYLE_DATA)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spUpStyle.setAdapter(adapter)
        spDownStyle.setAdapter(adapter)
        spUpStyle.setOnItemSelectedListener(this)
        spDownStyle.setOnItemSelectedListener(this)
    }

    private fun initRecord() {
        recordManager.init(application, BuildConfig.DEBUG)
        recordManager.changeFormat(RecordFormat.WAV)
        val recordDir: String = java.lang.String.format(
            Locale.getDefault(), "%s/Record/com.zlw.main/",
            Environment.getExternalStorageDirectory().getAbsolutePath()
        )
        recordManager.changeRecordDir(recordDir)
        recordManager.setRecordStateListener(object : RecordStateListener {
            override fun onStateChange(state: RecordState) {
                ZLog.i(TAG, String.format("onStateChange %s", state.name))
                when (state) {
                    RecordState.PAUSE -> tvState.setText("暂停中")
                    RecordState.IDLE -> tvState.setText("空闲中")
                    RecordState.RECORDING -> tvState.setText("录音中")
                    RecordState.STOP -> tvState.setText("停止")
                    RecordState.FINISH -> tvState.setText("录音结束")
                    else -> {
                    }
                }
            }

            override fun onError(error: String) {
                ZLog.i(TAG, String.format("onError %s", error))
            }
        })
        recordManager.setRecordResultListener { result -> Toast.makeText(this@TestHzActivity, "录音文件： " + result.absolutePath, Toast.LENGTH_SHORT).show() }
        recordManager.setRecordFftDataListener { data ->
            val newdata = ByteArray(data.size - 36)
            for (i in newdata.indices) {
                newdata[i] = data[i + 36]
            }
            audioView.setWaveData(data)
        }
    }

    @OnClick(R.id.btRecord, R.id.btStop)
    fun onViewClicked(view: View) {
        when (view.getId()) {
            R.id.btRecord -> if (isStart) {
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
            R.id.btStop -> {
                recordManager.stop()
                btRecord.setText("开始")
                isPause = false
                isStart = false
            }
            else -> {
            }
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
    }
}
