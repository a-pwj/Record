package com.pwj.record.ui.surfaceview.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.pwj.record.R
import com.pwj.record.ui.surfaceview.camera.ui.CameraActivity
import com.pwj.record.ui.surfaceview.camera.ui.GoogleCameraActivity
import com.pwj.record.ui.surfaceview.camera.utils.AppConstant
import com.yanzhenjie.permission.AndPermission
import kotlinx.android.synthetic.main.activity_camera_custom.*

/**
 * @Author:          pwj
 * @Date:            2020-9-28 11:25:50
 * @FileName:        CameraCustomActivity
 * @Description:
 *
1.创建显示相机画面的布局，Android已经为我们选定好SurfaceView
2.创建预览界面，创建继承自SurfaceView并实现SurfaceHolder接口的拍摄预览类。有了拍摄预览类，即可创建一个布局文件，将预览画面与设计好的用户界面控件融合在一起，实时显示相机的预览图像。
3.设置拍照监听器，给用户界面控件绑定监听器，使其能响应用户操作, 开始拍照过程。
4.拍照并保存文件，将拍摄获得的图像输出保存成各种常用格式的图片。
5.当相机使用完毕后，必须正确地将其释放，以免其它程序访问使用时发生冲突。
 *
 */
class CameraCustomActivity : BaseCameraActivity(), View.OnClickListener {

    private val TAG = CameraCustomActivity::class.java.simpleName

    override fun getLayoutId(): Int = R.layout.activity_camera_custom

    override fun initView() {
        btn_camera.setOnClickListener(this)
        btn_camera2.setOnClickListener(this)
        btn_filter_camera2.setOnClickListener(this)
        btn_camera2_video.setOnClickListener(this)
    }

    override fun initData() {
        requestPermission()
        // 设置关闭 移动网络
        setDataConnectionState(this, false)
    }


    @SuppressLint("WrongConstant")
    fun setDataConnectionState(cxt: Context, state: Boolean) {
        var connectivityManager: ConnectivityManager? = null
        var connectivityManagerClz: Class<*>? = null
        try {
            connectivityManager = cxt
                .getSystemService("connectivity") as ConnectivityManager
            connectivityManagerClz = connectivityManager.javaClass
            val method = connectivityManagerClz.getMethod(
                "setMobileDataEnabled", *arrayOf<Class<*>?>(Boolean::class.javaPrimitiveType)
            )
            method.invoke(connectivityManager, state)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 动态申请  (电话/位置/存储)
     */
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun requestPermission() {
        AndPermission.with(this)
            .runtime()
            .permission(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            ).onDenied {
                Log.e(TAG, "用户同意")
            }.onGranted {
                Log.e(TAG, "用户拒绝权限")
            }.start()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_camera -> {
                val intent = Intent(this, CameraActivity::class.java)
                startActivityForResult(intent, 0)
            }
            R.id.btn_camera2 -> {
                val intent2 = Intent(this, GoogleCameraActivity::class.java)
                startActivityForResult(intent2, 1)
            }
//            R.id.btn_filter_camera2 -> {
//                val intentFilter2 = Intent(this, CameraSurfaceViewActivity::class.java)
//                startActivityForResult(intentFilter2, 0)
//            }
//            R.id.btn_camera2_video -> {
//                val intentVideo = Intent(this, CameraVideoActivity::class.java)
//                startActivity(intentVideo)
//            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != AppConstant.RESULT_CODE.RESULT_OK) {
            return
        }
        if (requestCode == 0) {
//            val imgPath = data!!.getStringExtra(AppConstant.KEY.IMG_PATH)
//            val picWidth = data!!.getIntExtra(AppConstant.KEY.PIC_WIDTH, 0)
//            val picHeight = data!!.getIntExtra(AppConstant.KEY.PIC_HEIGHT, 0)
//            val intent = Intent(activity, ShowPicActivity::class.java)
//            intent.putExtra(AppConstant.KEY.PIC_WIDTH, picWidth)
//            intent.putExtra(AppConstant.KEY.PIC_HEIGHT, picHeight)
//            intent.putExtra(AppConstant.KEY.IMG_PATH, imgPath)
//            startActivity(intent)
        }
    }
}
