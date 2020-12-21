package com.pwj.record.utils

import java.io.File
import java.io.IOException

/**
 * @Author:          pwj
 * @Date:            2020/6/5 10:26
 * @FileName:        FileIgnoreUtils
 * @Description:     description
 */
object FileIgnoreUtils {

    fun addIgnore(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) {
            file.mkdirs()
        }
        val ignoreFile = File("$filePath/.nomedia")
        if (ignoreFile.exists() && ignoreFile.isFile) {
            return
        }
        try {
            ignoreFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}