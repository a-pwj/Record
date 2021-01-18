package com.pwj.record.ui.video.record;

/**
 * @Author: pwj
 * @Date: 2020/12/16 14:15
 * @FileName: Mp3Encoder
 * @Description: description
 */
public class Mp3Encoder {

    static {
        System.loadLibrary("lame");
    }

    /**
     * 关闭 lame 编码器，释放资源
     */
    public native static void close();

    /**
     * 编码，把 AudioRecord 录制的 PCM 数据转换成 mp3 格式
     *
     * @param buffer_l 左声道输入数据
     * @param buffer_r 右声道输入数据
     * @param samples  输入数据的size
     * @param mp3buf   输出数据
     * @return 输出到mp3buf的byte数量
     */
    public native static int encode(short[] buffer_l, short[] buffer_r, int samples, byte[] mp3buf);

    /**
     * 刷写
     *
     * @param mp3buf mp3数据缓存区
     * @return 返回刷写的数量
     */
    public native static int flush(byte[] mp3buf);

    /**
     * 初始化 lame编码器
     *
     * @param inSampleRate  输入采样率
     * @param outChannel    声道数
     * @param outSampleRate 输出采样率
     * @param outBitrate    比特率(kbps)
     * @param quality       0~9，0最好
     */
    public native static void init(int inSampleRate, int outChannel, int outSampleRate, int outBitrate, int quality);

    public static void init(int inSampleRate, int outChannel, int outSampleRate, int outBitrate) {
        init(inSampleRate, outChannel, outSampleRate, outBitrate, 7);
    }
}
