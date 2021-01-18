package com.pwj.record.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.core.os.EnvironmentCompat;


import com.pwj.record.R;
import com.pwj.record.bean.FileInfoBean;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


/**
 * @Author: pwj
 * @Date: 2020/6/10 18:09
 * @FileName: FileTypeUtils
 * @Description: description
 */
public class FileTypeUtils {

    public static final FileFilter ALL_FOLDER_AND_FILES_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return !pathname.isHidden();
        }
    };

    public static List<FileInfoBean> getFileInfosFromFileArray(File[] files) {
        List<FileInfoBean> fileInfos = new ArrayList();
        File[] var2 = files;
        int var3 = files.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            File file = var2[var4];
            FileInfoBean fileInfo = getFileInfoFromFile(file);
            fileInfos.add(fileInfo);
        }

        return fileInfos;
    }

    private static FileInfoBean getFileInfoFromFile(File file) {
        FileInfoBean fileInfo = new FileInfoBean();
        fileInfo.setFileName(file.getName());
        fileInfo.setFilePath(file.getPath());
        fileInfo.setDirectory(file.isDirectory());
        if (file.isDirectory()) {
            fileInfo.setFileSize((long)getNumFilesInFolder(fileInfo));
        } else {
            fileInfo.setFileSize(file.length());
        }

        int lastDotIndex = file.getName().lastIndexOf(".");
        if (lastDotIndex > 0) {
            String fileSuffix = file.getName().substring(lastDotIndex + 1);
            fileInfo.setSuffix(fileSuffix);
        }

        return fileInfo;
    }

    public static List<FileInfoBean> getTextFilesInfo(Context context,File fileDir) {
        List<FileInfoBean> textFilesInfo = new ArrayList();
        FileFilter fileFilter = new FileTypeUtils.FileTypeFilter(context.getResources().getStringArray(R.array.rc_file_file_suffix));
        getFileInfos(fileDir, fileFilter, textFilesInfo);
        return textFilesInfo;
    }

    public static List<FileInfoBean> getVideoFilesInfo(Context context,File fileDir) {
        List<FileInfoBean> videoFilesInfo = new ArrayList();
        FileFilter fileFilter = new FileTypeUtils.FileTypeFilter(context.getResources().getStringArray(R.array.rc_video_file_suffix));
        getFileInfos(fileDir, fileFilter, videoFilesInfo);
        return videoFilesInfo;
    }


    public static List<FileInfoBean> getAudioFilesInfo(Context context,File fileDir) {
        List<FileInfoBean> audioFilesInfo = new ArrayList();
        FileFilter fileFilter = new FileTypeUtils.FileTypeFilter(context.getResources().getStringArray(R.array.rc_audio_file_suffix));
        getFileInfos(fileDir, fileFilter, audioFilesInfo);
        return audioFilesInfo;
    }

    public static List<FileInfoBean> getOtherFilesInfo(Context context,File fileDir) {
        List<FileInfoBean> otherFilesInfo = new ArrayList();
        FileFilter fileFilter = new FileTypeUtils.FileTypeFilter(context.getResources().getStringArray(R.array.rc_other_file_suffix));
        getFileInfos(fileDir, fileFilter, otherFilesInfo);
        return otherFilesInfo;
    }

    private static void getFileInfos(File fileDir, FileFilter fileFilter, List<FileInfoBean> fileInfos) {
        File[] listFiles = fileDir.listFiles(fileFilter);
        if (listFiles != null) {
            File[] var4 = listFiles;
            int var5 = listFiles.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                File file = var4[var6];
                if (file.isDirectory()) {
                    getFileInfos(file, fileFilter, fileInfos);
                } else if (file.length() != 0L) {
                    FileInfoBean fileInfo = getFileInfoFromFile(file);
                    fileInfos.add(fileInfo);
                }
            }
        }

    }

    public static class FileNameComparator implements Comparator<FileInfoBean> {
        protected static final int FIRST = -1;
        protected static final int SECOND = 1;

        public FileNameComparator() {
        }

        @Override
        public int compare(FileInfoBean lhs, FileInfoBean rhs) {
            if (!lhs.isDirectory() && !rhs.isDirectory()) {
                return lhs.getFileName().compareToIgnoreCase(rhs.getFileName());
            } else if (lhs.isDirectory() == rhs.isDirectory()) {
                return lhs.getFileName().compareToIgnoreCase(rhs.getFileName());
            } else {
                return lhs.isDirectory() ? -1 : 1;
            }
        }
    }

    public static int getNumFilesInFolder(FileInfoBean fileInfo) {
        if (!fileInfo.isDirectory()) {
            return 0;
        } else {
            File[] files = (new File(fileInfo.getFilePath())).listFiles(ALL_FOLDER_AND_FILES_FILTER);
            return files == null ? 0 : files.length;
        }
    }

    public static int getFileIconResource(Context context,FileInfoBean file) {
        return file.isDirectory() ? R.drawable.rc_ad_list_folder_icon : getFileTypeImageId(file.getFileName(),context);
    }

    private static int getFileTypeImageId(String fileName,Context context) {
        int id;
        if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_file_file_suffix))) {
            id = R.drawable.rc_ad_list_file_icon;
        } else if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_video_file_suffix))) {
            id = R.drawable.rc_ad_list_video_icon;
        } else if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_audio_file_suffix))) {
            id = R.drawable.rc_ad_list_audio_icon;
        } else if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_ppt_file_suffix))) {
            id = R.drawable.rc_ad_list_ppt_icon;
        } else if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_pdf_file_suffix))) {
            id = R.drawable.rc_ad_list_pdf_icon;
        } else if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_image_file_suffix))) {
            id = R.drawable.rc_file_icon_picture;
        } else if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_apk_file_suffix))) {
            id = R.drawable.rc_file_icon_apk;
        } else if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_word_file_suffix))) {
            id = R.drawable.rc_file_icon_word;
        } else if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_excel_file_suffix))) {
            id = R.drawable.rc_file_icon_excel;
        } else if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_key_file_suffix))) {
            id = R.drawable.rc_ad_list_key_icon;
        } else if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_numbers_file_suffix))) {
            id = R.drawable.rc_ad_list_numbers_icon;
        } else if (checkSuffix(fileName, context.getResources().getStringArray(R.array.rc_pages_file_suffix))) {
            id = R.drawable.rc_ad_list_pages_icon;
        } else {
            id = R.drawable.rc_ad_list_other_icon;
        }

        return id;
    }

    private static boolean checkSuffix(String fileName, String[] fileSuffix) {
        String[] var2 = fileSuffix;
        int var3 = fileSuffix.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String suffix = var2[var4];
            if (fileName != null && fileName.toLowerCase().endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }


    public static String formatFileSize(long size) {
        if (size < 1024L) {
            return String.format("%d B", (int)size);
        } else if (size < 1048576L) {
            return String.format("%.2f KB", (float)size / 1024.0F);
        } else {
            return size < 1073741824L ? String.format("%.2f MB", (float)size / 1048576.0F) : String.format("%.2f G", (float)size / 1.07374182E9F);
        }
    }

    public static String[] getExternalStorageDirectories(Context context) {
        List<String> results = new ArrayList();
        if (Build.VERSION.SDK_INT >= 19) {
            File[] externalDirs = context.getExternalFilesDirs((String)null);
            File[] var3 = externalDirs;
            int var4 = externalDirs.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File file = var3[var5];
                if (file != null) {
                    String path = file.getPath().split("/Android")[0];
                    boolean addPath;
                    if (Build.VERSION.SDK_INT >= 21) {
                        addPath = Environment.isExternalStorageRemovable(file);
                    } else {
                        addPath = "mounted".equals(EnvironmentCompat.getStorageState(file));
                    }

                    if (addPath) {
                        results.add(path);
                    }
                }
            }
        }

        if (results.isEmpty()) {
            String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
            String s = "";

            try {
                Process process = (new ProcessBuilder(new String[0])).command("mount").redirectErrorStream(true).start();
                process.waitFor();
                InputStream is = process.getInputStream();

                for(byte[] buffer = new byte[1024]; is.read(buffer) != -1; s = s + new String(buffer)) {
                }

                is.close();
            } catch (Exception var14) {
            }

            String[] lines = s.split("\n");
            String[] var23 = lines;
            int var25 = lines.length;

            for(int var26 = 0; var26 < var25; ++var26) {
                String line = var23[var26];
                if (!line.toLowerCase(Locale.US).contains("asec") && line.matches(reg)) {
                    String[] parts = line.split(" ");
                    String[] var10 = parts;
                    int var11 = parts.length;

                    for(int var12 = 0; var12 < var11; ++var12) {
                        String part = var10[var12];
                        if (part.startsWith("/") && !part.toLowerCase(Locale.US).contains("vold")) {
                            results.add(part);
                        }
                    }
                }
            }
        }

        int i;
        if (Build.VERSION.SDK_INT >= 23) {
            for(i = 0; i < results.size(); ++i) {
                if (!((String)results.get(i)).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
                    results.remove(i--);
                }
            }
        } else {
            for(i = 0; i < results.size(); ++i) {
                if (!((String)results.get(i)).toLowerCase().contains("ext") && !((String)results.get(i)).toLowerCase().contains("sdcard")) {
                    results.remove(i--);
                }
            }
        }

        String[] storageDirectories = new String[results.size()];

        for(int j = 0; j < results.size(); ++j) {
            storageDirectories[j] = (String)results.get(j);
        }

        return storageDirectories;
    }

    public static final class FileTypeFilter implements FileFilter {
        private String[] filesSuffix;

        public FileTypeFilter(String[] fileSuffix) {
            this.filesSuffix = fileSuffix;
        }

        public boolean accept(File pathname) {
            return !pathname.isHidden() && (pathname.isDirectory() || FileTypeUtils.checkSuffix(pathname.getName(), this.filesSuffix));
        }
    }
}
