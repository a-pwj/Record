package com.pwj.record.bean;

import java.io.Serializable;

/**
 * @Author: pwj
 * @Date: 2020/6/10 17:58
 * @FileName: FileInfo
 * @Description: description
 */
public class FileInfoBean implements Serializable {
    private static final long serialVersionUID = -4830812821556630987L;
    String fileName;
    String filePath;
    long fileSize;
    boolean isDirectory;
    String suffix;

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setDirectory(boolean directory) {
        this.isDirectory = directory;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public FileInfoBean() {
    }
}
