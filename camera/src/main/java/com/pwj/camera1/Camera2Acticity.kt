package com.pwj.camera1

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_camera2_acticity.*

class Camera2Acticity : AppCompatActivity(), SurfaceHolder.Callback {

    //    private val handlerThread = HandlerThread("camera2")
//    private val mainHandler = Handler(Looper.getMainLooper())
//    private val chilHandler = Handler(handlerThread.looper)
    private lateinit var mHolder: SurfaceHolder
    private lateinit var mCameraDevice: CameraDevice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2_acticity)

        mHolder = mIdSvVideo.holder
        mHolder.addCallback(this)

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }

    @SuppressLint("MissingPermission")
    override fun surfaceCreated(holder: SurfaceHolder?) {
        val handlerThread = HandlerThread("camera2")
        handlerThread.start()
        val mainHandler = Handler(Looper.getMainLooper())
        val chilHandler = Handler(handlerThread.looper)

        val mCameraId = CameraCharacteristics.LENS_FACING_FRONT.toString()
        val mCameraManager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        var mCameraCharacteristics: CameraCharacteristics

        try {
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val stateCallbacks: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    mCameraDevice = camera
                }

                override fun onDisconnected(camera: CameraDevice) {

                }

                override fun onError(camera: CameraDevice, error: Int) {

                }

            }
            mCameraManager.openCamera(mCameraId, stateCallbacks, mainHandler)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun startPreview(){
        try {
            //创建预览需要的CaptureRequest.Builder
            val reqBuilder=mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            //将SurfaceView的surface作为Capture.Builder的目标
            reqBuilder.addTarget(mHolder.surface)
            reqBuilder.set(
                CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
            )
            reqBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)


        }catch (e:Exception){

        }
    }
}
