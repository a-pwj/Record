package com.pwj.record.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author: pwj
 * @Date: 2020/7/2 11:30
 * @FileName: FileManager
 * @Description: description
 */
public class FileManager {

    /**
     * 获取本地文件真实 uri
     *
     * @param contentUri
     * @return
     */
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        String path = null;
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.MediaColumns.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(proj[0]);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } catch (Exception e) {
            path = null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * 用流拷贝文件一份到自己APP目录下
     *
     * @param context
     * @param uri
     * @param fileName
     * @return
     */
    public static String getPathFromInputStreamUri(Context context, Uri uri, String fileName) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                File file = createTemporalFileFrom(context, inputStream, fileName);
                filePath = file.getPath();

            } catch (Exception e) {
                Log.e("e", e.toString());
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    Log.e("e", e.toString());
                }
            }
        }

        return filePath;
    }

    private static File createTemporalFileFrom(Context context, InputStream inputStream, String fileName) throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];
            File saveFile = new File(context.getFilesDir(), "/file");
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            //自己定义拷贝文件路径
            targetFile = new File(saveFile, fileName);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }
}
