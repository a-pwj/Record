package com.pwj.record.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import java.io.File
import java.io.FileOutputStream


/**
 * @Author:          pwj
 * @Date:            2020/10/22 10:32
 * @FileName:        ViewSaveUtils
 * @Description:     description
 */
object ImgSaveUtils {


    /**
     * 这个方法适用于view 已经显示在界面上了，可以获得view 的宽高实际大小，进而通过DrawingCache 保存为bitmap
     */
    private fun createBitmap(view: View): Bitmap? {
        view.buildDrawingCache()
        return view.drawingCache
    }

    /**
     * 生成View的bitmap
     * @param v
     * @param width
     * @param height
     * @return
     */
    fun createViewBitmap(v: View, width: Int, height: Int): Bitmap? {
        //测量使得view指定大小
        val measuredWidth: Int = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measuredHeight: Int = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        v.measure(measuredWidth, measuredHeight)
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight())
        val bmp: Bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        v.draw(c)
        return bmp
    }

    /**
     * 保存图片到本地
     */
    private fun saveBitmap(bitmap: Bitmap) {
        val fos: FileOutputStream
        try {
            val root: File = Environment.getExternalStorageDirectory()
            val file = File(root, "test.png")
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * rxjava glide 下载图片
     */
//    fun downloadImage(context: Context, imgUrl: String) {
//        val extension = MimeTypeMap.getFileExtensionFromUrl(imgUrl)
//        Observable.create { emitter: ObservableEmitter<File?> ->
//            // Glide提供了一个download() 接口来获取缓存的图片文件，
//            // 但是前提必须要设置diskCacheStrategy方法的缓存策略为
//            // DiskCacheStrategy.ALL或者DiskCacheStrategy.SOURCE，
//            // 还有download()方法需要在子线程里进行
//            val file = Glide.with(context).download(imgUrl).submit().get()
//            val fileParentPath = context.filesDir.absolutePath + "/push/Image"
//            val appDir = File(fileParentPath)
//            if (!appDir.exists()) {
//                appDir.mkdirs()
//            }
//            //获得原文件流
//            val fis = FileInputStream(file)
//            //保存的文件名
//            val fileName = "pushImg_" + System.currentTimeMillis() + "." + extension
//            //目标文件
//            val targetFile = File(appDir, fileName)
//            //输出文件流
//            val fos = FileOutputStream(targetFile)
//            // 缓冲数组
//            val b = ByteArray(1024 * 8)
//            while (fis.read(b) != -1) {
//                fos.write(b)
//            }
//            fos.flush()
//            fis.close()
//            fos.close()
//            //扫描媒体库
////            val mimeTypes = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
////            MediaScannerConnection.scanFile(context, arrayOf(targetFile.absolutePath), arrayOf(mimeTypes), null)
//            emitter.onNext(targetFile)
//        }
//            .subscribeOn(Schedulers.io()) //发送事件在io线程
//            .observeOn(AndroidSchedulers.mainThread()) //最后切换主线程提示结果
//            .subscribe({ result ->
//                Log.e("imgDownload", "下载图片成功")
//                result?.let {
//                    files.add(result)
//                }
//            }) {
//                maps.remove(imgUrl)
//                Log.e("imgDownload", "下载图片失败")
//            }
//    }

    fun saveBitmapToFile(context: Context, bmp: Bitmap?, bitName: String?): String? {
        // 系统相册目录
        val path = context.filesDir.absolutePath + "/push/Image"
//        val galleryPath = File(
//            Environment.getExternalStorageDirectory()
//                .toString() + File.separator + Environment.DIRECTORY_DCIM
//                    + File.separator + "Camera" + File.separator
//        )
        val galleryPath = File(path)
        if (!galleryPath.exists()) {
            galleryPath.mkdir()
        }

        val fileName = bitName.toString() + System.currentTimeMillis() + ".jpg"
        val file = File(galleryPath, fileName)

        try {
            val fos = FileOutputStream(file)
            bmp!!.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return null
        }
        return file.absolutePath
    }

    /**
     * 保存图片到图库
     * @param bmp
     * @param bitName
     */
    fun saveBitmapToGallery(context: Context, bmp: Bitmap?, bitName: String?): String? {
        // 系统相册目录
        /*File galleryPath = new File(Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator);
        if (!galleryPath.exists()) {
            galleryPath.mkdir();
        }

        String fileName = bitName + ".jpg";
        File file = new File(galleryPath, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return file.getAbsolutePath();*/

        //插入到系统图库
        return MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "菜单", bitName)
    }
}