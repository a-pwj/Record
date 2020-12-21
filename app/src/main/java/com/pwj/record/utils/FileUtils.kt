package com.pwj.record.utils

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.*

/**
 * @Author:          pwj
 * @Date:            2020/8/3 16:54
 * @FileName:        FileUtils
 * @Description:     description
 */
object FileUtils {


    /*******************************************************
     *
     * android Q 获取文件路径
     *
     *******************************************************/

    /**
     * 获取本地文件真实 uri
     *
     * @param contentUri
     * @return
     */
    fun getRealPathFromUri(context: Context, contentUri: Uri?): String? {
        var path: String? = null
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.MediaColumns.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(proj[0])
            cursor.moveToFirst()
            path = cursor.getString(column_index)
        } catch (e: java.lang.Exception) {
            path = null
        } finally {
            cursor?.close()
        }
        return path
    }

    /**
     * 用流拷贝文件一份到自己APP目录下
     *
     * @param context
     * @param uri
     * @param fileName
     * @return
     */
    fun getPathFromInputStreamUri(context: Context, uri: Uri, fileName: String): String? {
        var inputStream: InputStream? = null
        var filePath: String? = null
        if (uri.authority != null) {
            try {
                inputStream = context.contentResolver.openInputStream(uri)
                val file = createTemporalFileFrom(context, inputStream, fileName)
                filePath = file!!.path
            } catch (e: java.lang.Exception) {
                Log.e("e", e.toString())
            } finally {
                try {
                    inputStream?.close()
                } catch (e: java.lang.Exception) {
                    Log.e("e", e.toString())
                }
            }
        }
        return filePath
    }

    @Throws(IOException::class)
    private fun createTemporalFileFrom(context: Context, inputStream: InputStream?, fileName: String): File? {
        var targetFile: File? = null
        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)
            val saveFile = File(context.filesDir, "/file")
            if (!saveFile.exists()) {
                saveFile.mkdirs()
            }
            //自己定义拷贝文件路径
            targetFile = File(saveFile, fileName)
            if (targetFile.exists()) {
                targetFile.delete()
            }
            val outputStream: OutputStream = FileOutputStream(targetFile)
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return targetFile
    }


    /*******************************************************
     *
     * 打开文件
     *
     *******************************************************/

    /**
     * 调用自带的视频播放器
     *
     * @param context
     * @param path
     */
    fun openVideo(context: Context, path: String) {
        val mimeType = getMimeType(path)
        val f = File(path)
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            val contentUri: Uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".toolsProvider", f)
            intent.setDataAndType(contentUri, "video/*")
        } else {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setDataAndType(Uri.fromFile(f), "video/*")
        }
        return try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 调用自带的音频播放器
     *
     * @param context
     * @param path
     */
    private fun openAudio(context: Context, path: String) {
        val f = File(path)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setDataAndType(Uri.fromFile(f), "audio/*")
        context.startActivity(intent)
    }

    /**
     * 调用自带的图库
     *
     * @param context
     * @param path
     */
    private fun openPic(context: Context, path: String) {
        val f = File(path)
        val intent = Intent()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.fromFile(f), "image/*")
        context.startActivity(intent)
    }

    /**
     * 调用手机上能打开对应类型文件的程序
     *
     * @param context
     * @param path
     * @return true表示成功找到程序，false表示找不到能成功打开的程序
     */
    fun openFile(context: Context, path: String): Boolean {
        val mimeType = getMimeType(path)
        val f = File(path)
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            val contentUri: Uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".toolsProvider", f)
            intent.setDataAndType(contentUri, mimeType)
        } else {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setDataAndType(Uri.fromFile(f), mimeType)
        }
        return try {
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 根据filename获取 mineType
     */
    fun getMimeType(fileName: String): String? {
        var result: String? = "application/octet-stream"
        val extPos = fileName.lastIndexOf(".")
        if (extPos != -1) {
            val ext = fileName.substring(extPos + 1)
            result = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
        }
        return result
    }
}