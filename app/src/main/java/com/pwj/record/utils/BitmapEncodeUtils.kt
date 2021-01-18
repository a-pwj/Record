package com.pwj.record.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URLEncoder


object BitmapEncodeUtils {

    /**
     * 加密
     */
    fun encodeImgToString(bitmap: Bitmap): String {
        val bos = ByteArrayOutputStream()
        val compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        return if (compress) {
            Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT)
        } else {
            ""
        }
    }

    /**
     * 解码
     */
    fun decodeStringToImg(string: String): Bitmap {
        val input = Base64.decode(string, Base64.DEFAULT)
        val bitmap: Bitmap = BitmapFactory.decodeByteArray(input, 0, input.size)
        return bitmap
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    fun getSmallBitmap(filePath: String): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeFile(filePath, options)
    }

    //计算图片的缩放值
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

    fun bitmapToString(filePath: String): String {
       return URLEncoder.encode(bitmapTString(filePath),"UTF-8")
    }

    //把bitmap转换成String
    fun bitmapTString(filePath: String): String {
        val bm = getSmallBitmap(filePath)
        val baos = ByteArrayOutputStream()

        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos)
        val b = baos.toByteArray()
        Log.d("d", "压缩后的大小=" + b.size)
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    fun imageToBase64(path: String): String? {
        if (TextUtils.isEmpty(path)) {
            return null
        }
        var `is`: InputStream? = null
        var data: ByteArray? = null
        var result: String? = null
        try {
            `is` = FileInputStream(path)
            //创建一个字符流大小的数组。
            data = ByteArray(`is`!!.available())
            //写入数组
            `is`.read(data)
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != `is`) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result
    }
}