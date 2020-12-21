package com.pwj.record.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pwj.record.R
import kotlinx.android.synthetic.main.activity_result_contracts.*


/**
 * Author:           pwj
 * Date:             2020/4/15 17:33
 * FileName:         ActivityResultNewApiActivity.kt
 * description:      测试谷歌startActivityForResult 新api
 */
class ActivityResultNewApiActivity : AppCompatActivity(R.layout.activity_result_contracts), View.OnClickListener {

//    private lateinit var takePhotoObserver: TakePhotoObserver
//    private lateinit var takePhotoLiveData: TakePhotoLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        code1.setOnClickListener(this)
        code2.setOnClickListener(this)
        code3.setOnClickListener(this)
        code4.setOnClickListener(this)
        code5.setOnClickListener(this)
        code6.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.code1 -> {
//                /**跳转页面**/
//                val startActivity = prepareCall(ActivityResultContracts.StartActivityForResult()) {
//                    showToast(it?.data?.getStringExtra("prepareCall") ?: "")
//                }
//                startActivity.launch(Intent(this, ActivityForResultNewApiActivity::class.java))
//            }
//            R.id.code2 -> {
//                /**请求权限**/
//                val requestPermission = prepareCall(ActivityResultContracts.RequestPermissions()) { result ->
//                    showToast("request permission $result")
//                }
//                requestPermission.launch(arrayOf(Manifest.permission.READ_PHONE_STATE))
//            }
//            R.id.code3 -> {
//                /**通讯录**/
//                val contact = prepareCall(ActivityResultContracts.PickContact()) { result ->
//                    showToast("dial $result")
//                }
//                contact.launch(null)
//            }
//            R.id.code4 -> {
//                /**获取图片**/
//                prepareCall(ActivityResultContracts.TakePicture()) { result ->
//                    showToast("dial $result")
//                    photo.setImageBitmap(result)
//                }
//            }
//            R.id.code5 -> {
//                /**获取图片,自定义**/
//                prepareCall(TakePicDrawable()) { result ->
//                    showToast("take picture : $result")
//                    result?.let {
//                        photo.setImageDrawable(result)
//                    }
//                }
//            }
//            R.id.code6 -> {
//                /**获取图片,自定义2**/
//                takePhotoObserver = TakePhotoObserver(activityResultRegistry) { bitmap ->
//                    photo.setImageBitmap(bitmap)
//                }
//                lifecycle.addObserver(takePhotoObserver)
//                takePhotoObserver.takePicture()
//            }
//            R.id.code7 -> {
//                /**获取图片,自定义2**/
//                takePhotoLiveData = TakePhotoLiveData(activityResultRegistry)
//                takePhotoLiveData.observeForever(Observer { bitmap ->
//                    photo.setImageBitmap(bitmap)
//                })
//                takePhotoLiveData.takePhotoLauncher
//            }
        }
    }

//    class TakePicDrawable : ActivityResultContract<Void, Drawable>() {
//        override fun createIntent(context: Context, input: Void?): Intent {
//            return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        }
//
//        override fun parseResult(resultCode: Int, intent: Intent?): Drawable? {
//            if (resultCode != Activity.RESULT_OK || intent == null) return null
//            val bitmap = intent.getParcelableExtra<Bitmap>("data")
//            return BitmapDrawable(bitmap)
//        }
//    }
//
//    class TakePhotoObserver(private val registry: ActivityResultRegistry, private val func: (Bitmap) -> Unit) : DefaultLifecycleObserver {
//        private lateinit var takePhoneLauncher:ActivityResultLauncher<Uri>
//
//        override fun onCreate(owner: LifecycleOwner) {
//            takePhoneLauncher = registry.register("key",ActivityResultContracts.TakePicture()) {
//                func(it)
//            }
//        }
//
//        fun takePicture(){
//            takePhoneLauncher
//        }
//    }
//
//    class TakePhotoLiveData(private val registry: ActivityResultRegistry) : LiveData<Bitmap>() {
//
//        lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>
//
//        override fun onActive() {
//            super.onActive()
//            takePhotoLauncher = registry.register(
//                "key",
//                ActivityResultContracts.TakePicture()
//            ) { result ->
//                value = result
//            }
//        }
//
//        override fun onInactive() {
//            super.onInactive()
//            takePhotoLauncher.unregister()
//        }
//
//    }

}
