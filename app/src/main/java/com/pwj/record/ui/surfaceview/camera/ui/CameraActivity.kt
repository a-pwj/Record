package com.pwj.record.ui.surfaceview.camera.ui

import android.content.Intent
import android.graphics.*
import android.hardware.Camera
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import com.pwj.record.R
import com.pwj.record.ui.surfaceview.camera.BaseCameraActivity
import com.pwj.record.ui.surfaceview.camera.utils.AppConstant
import com.pwj.record.ui.surfaceview.camera.utils.BitmapUtils
import com.pwj.record.ui.surfaceview.camera.utils.CameraUtils
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * @Author:          pwj
 * @Date:            2020-9-28 13:35:19
 * @FileName:        CameraActivity
 * @Description:     description
 *
 * 首先要继承SurfaceView，实现SurfaceHolder.Callback接口。
 * <p>
 * 重写方法：
 * surfaceChanged：surface大小或格式发生变化时触发，在surfaceCreated调用后该函数至少会被调用一次。
 * surfaceCreated：Surface创建时触发，一般在这个函数开启绘图线程（新的线程，不要再这个线程中绘制Surface）。
 * surfaceDestroyed：销毁时触发，一般不可见时就会销毁。
 * <p>
 * 利用getHolder()获取SurfaceHolder对象，调用SurfaceHolder.addCallback添加回调
 * <p>
 * SurfaceHolder.lockCanvas 获取Canvas对象并锁定画布，调用Canvas绘图，SurfaceHolder.unlockCanvasAndPost 结束锁定画布，提交改变。
 */
class CameraActivity : BaseCameraActivity(), SurfaceHolder.Callback, View.OnClickListener {

    private val TAG = "CameraActivity"
    private var mCamera: Camera? = null
    private var mHolder: SurfaceHolder? = null
    private var cameraInstance: CameraUtils? = null

    /**
     * 屏幕宽高
     */
    private var screenWidth = 0
    private var screenHeight = 0

    /**
     * 图片宽高
     */
    private var picWidth = 0

    /**
     * 是否有界面
     */
    private var isView = true

    /**
     * 拍照id  1： 前摄像头  0：后摄像头
     */
    private var mCameraId = 0

    /**
     * 闪光灯类型 0 ：关闭 1： 打开 2：自动
     */
    private var light_type = 0

    /**
     * 图片高度
     */
    private var picHeight = 0


    override fun getLayoutId(): Int = R.layout.activity_camera

    override fun initView() {
        mHolder = svContent.holder
        mHolder?.addCallback(this)

        ivBack.setOnClickListener(this)
        ivCamera.setOnClickListener(this)
        ivFlash.setOnClickListener(this)
        ivSwitch.setOnClickListener(this)

    }

    override fun initData() {
        cameraInstance = CameraUtils.getInstance()
        val dm = resources.displayMetrics
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels

    }

    override fun onResume() {
        super.onResume()
        if (mCamera==null){
            mCamera = getCamera(mCameraId)
            mHolder?.let {
                startPreview(mCamera!!, mHolder!!)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> finish()
            R.id.ivSwitch -> {
                //切换前后摄像头
                switchCamera()
            }
            R.id.ivFlash -> {
                //切换闪光灯
                if (mCameraId == 1) {
                    Toast.makeText(this, "请切换到后置摄像头", Toast.LENGTH_LONG).show()
                    return
                }
                val parameters = mCamera!!.parameters
                when (light_type) {
                    0 -> {
                        //打开
                        light_type = 1
                        ivFlash.setImageResource(R.mipmap.icon_camera_on)
                        parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH //开启
                        mCamera!!.parameters = parameters
                    }
                    1 -> {
                        //自动
                        light_type = 2
                        parameters.flashMode = Camera.Parameters.FLASH_MODE_AUTO
                        mCamera!!.parameters = parameters
                        ivFlash.setImageResource(R.mipmap.icon_camera_a)
                    }
                    2 -> {
                        //关闭
                        light_type = 0
                        //关闭
                        parameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
                        mCamera!!.parameters = parameters
                        ivFlash.setImageResource(R.mipmap.icon_camera_off)
                    }
                }
            }
            R.id.ivCamera -> {
                // 点击拍照
                when (light_type) {
                    //关闭
                    0 -> cameraInstance?.turnLightOff(mCamera)
                    1 -> cameraInstance?.turnLightOn(mCamera)
                    //自动
                    2 -> cameraInstance?.turnLightAuto(mCamera)
                }
                takePhoto()
            }
        }
    }

    /**
     * 切换前后摄像头
     */
    fun switchCamera() {
        releaseCamera()
        mCameraId = (mCameraId + 1) % Camera.getNumberOfCameras()
        mCamera = getCamera(mCameraId)
        if (mHolder != null) {
            startPreview(mCamera!!, mHolder!!)
        }
    }

    /**
     * 拍照
     */
    private fun takePhoto() {
        mCamera?.takePicture(null, null, { data, camera ->
            isView = false
            //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
            //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            var saveBitmap = cameraInstance!!.setTakePicktrueOrientation(mCameraId, bitmap)
            saveBitmap = Bitmap.createScaledBitmap(saveBitmap, screenWidth, screenHeight, true)
            val imgpath = getExternalFilesDir(Environment.DIRECTORY_DCIM)!!.path.toString() +
                    File.separator + System.currentTimeMillis() + ".jpeg"
            Log.e(TAG, "imgpath: ---  $imgpath")
            BitmapUtils.saveJPGE_After(applicationContext, saveBitmap, imgpath, 100)
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
            if (!saveBitmap.isRecycled) {
                saveBitmap.recycle()
            }
            val intent = Intent()
            intent.putExtra(AppConstant.KEY.IMG_PATH, imgpath)
            intent.putExtra(AppConstant.KEY.PIC_WIDTH, picWidth)
            intent.putExtra(AppConstant.KEY.PIC_HEIGHT, picHeight)
            setResult(AppConstant.RESULT_CODE.RESULT_OK, intent)
            finish()
        })
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        startPreview(mCamera!!, holder!!)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        mCamera!!.stopPreview()
        startPreview(mCamera!!, holder!!)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        releaseCamera()
    }

    /**
     * 释放相机资源
     */
    private fun releaseCamera() {
        mCamera?.let {
            it.setPreviewCallback(null)
            it.stopPreview()
            it.release()
            mCamera = null
        }
    }

    /**
     * 预览相机
     */
    private fun startPreview(camera: Camera, holder: SurfaceHolder) {
        try {
            setupCamera(camera)
            camera.setPreviewDisplay(holder)
            mCamera?.setPreviewCallback { data, camera ->
                val size = camera.parameters.previewSize
                val stream = ByteArrayOutputStream()
                try {
                    // YUV转为RGB
                    val image = YuvImage(data, ImageFormat.NV21, size.width, size.height, null)
                    image.compressToJpeg(Rect(0, 0, size.width / 2, size.height / 2), 20, stream)
                    val bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size())
                    svShow.bitmap = BitmapUtils.rotateMyBitmap(BitmapUtils.ImgaeToNegative(bmp))
                    stream.close()
                } catch (ex: java.lang.Exception) {
                    Log.e("Sys", "Error:" + ex.message)
                }
            }
            cameraInstance!!.setCameraDisplayOrientation(this, mCameraId, camera)
            camera.startPreview()
            isView = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
     * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
     * previewSize.width才是surfaceView的高度
     * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
     */
    private fun setupCamera(camera: Camera) {
//        val parameters = camera.parameters
//        if (parameters.supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//            parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
//        }
//
//        //根据屏幕尺寸获取最佳大小
//        val previewSize = cameraInstance?.getPicPreviewSize(parameters.supportedPreviewSizes, screenHeight, screenWidth)
//        parameters.setPreviewSize(previewSize!!.width, previewSize.height)
//
//        val pictrueSize = cameraInstance?.getPicPreviewSize(parameters.supportedPictureSizes, screenHeight, screenWidth)
//        parameters.setPictureSize(pictrueSize!!.width, pictrueSize.height)
//
//        camera.parameters = parameters
//        picWidth = pictrueSize.width
//        picHeight = pictrueSize.height

        val parameters = camera.parameters
        if (parameters.supportedFocusModes.contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
            )
        ) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        }
        //根据屏幕尺寸获取最佳 大小
        val previewSize = cameraInstance!!.getPicPreviewSize(
            parameters.supportedPreviewSizes,
            screenHeight, screenWidth
        )
        parameters.setPreviewSize(previewSize.width, previewSize.height)

        val pictrueSize = cameraInstance!!.getPicPreviewSize(
            parameters.supportedPictureSizes,
            screenHeight, screenWidth
        )
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height)
        camera.parameters = parameters
        picWidth = pictrueSize.width
        picHeight = pictrueSize.height
    }

    /**
     * 获取Camera实例
     *
     * @return Camera
     */
    private fun getCamera(id: Int): Camera? {
        var camera: Camera? = null
        try {
            camera = Camera.open(id)
        } catch (e: Exception) {

        }
        return camera
    }

}
