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
    public native static void close();

    public native static int encode(short[] buffer_l, short[] buffer_r, int samples, byte[] mp3buf);

    public native static int flush(byte[] mp3buf);

    public native static void init(int inSampleRate, int outChannel, int outSampleRate, int outBitrate, int quality);

    public static void init(int inSampleRate, int outChannel, int outSampleRate, int outBitrate) {
        init(inSampleRate, outChannel, outSampleRate, outBitrate, 7);
    }
}
