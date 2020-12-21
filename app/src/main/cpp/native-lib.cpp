#include <jni.h>
#include <string>
#include "lame/lame.h"

/**
 * JNIEnv*参数：代表Java环境，通过这环境可以调用Java里面的方法
 * object参数：调用C语言的方法对象，this对象表示当前对象，即调用JNI方法所在的类
 *
 *
 *
 * C中调用Java中的String类型为 jstring;
 */


static lame_global_flags *glf = NULL;

unsigned char *convertJByteArrayToChars(JNIEnv *env, jbyteArray bytearray) {
    unsigned char *chars = NULL;
    jbyte *bytes;
    bytes = env->GetByteArrayElements(bytearray, 0);
    int chars_len = env->GetArrayLength(bytearray);
    chars = new unsigned char[chars_len + 1];
    memset(chars, 0, chars_len + 1);
    memcpy(chars, bytes, chars_len);
    chars[chars_len] = 0;
    env->ReleaseByteArrayElements(bytearray, bytes, 0);
    return chars;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_pwj_record_ui_video_record_Mp3Encoder_close(JNIEnv *env, jclass clazz) {
    lame_close(glf);
    glf = NULL;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_pwj_record_ui_video_record_Mp3Encoder_encode(JNIEnv *env, jclass clazz, jshortArray buffer_l, jshortArray buffer_r, jint inSamplerate, jbyteArray mp3buf) {
    jshort *j_buffer_l = env->GetShortArrayElements(buffer_l, 0);

    jshort *j_buffer_r = env->GetShortArrayElements(buffer_r, 0);

    const jsize mp3buf_size = env->GetArrayLength(mp3buf);
    unsigned char *j_mp3buf = convertJByteArrayToChars(env, mp3buf);

    int result = lame_encode_buffer(glf, j_buffer_l, j_buffer_r,
                                    inSamplerate, j_mp3buf, mp3buf_size);

    env->ReleaseShortArrayElements(buffer_l, j_buffer_l, 0);
    env->ReleaseShortArrayElements(buffer_r, j_buffer_r, 0);
    *j_mp3buf = NULL;

    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_pwj_record_ui_video_record_Mp3Encoder_flush(JNIEnv *env, jclass clazz, jbyteArray mp3buf) {
    const jsize mp3buf_size = env->GetArrayLength(mp3buf);
    unsigned char *c_mp3buf = convertJByteArrayToChars(env, mp3buf);

    int result = lame_encode_flush(glf, c_mp3buf, mp3buf_size);

    *c_mp3buf = NULL;
    return result;

}

extern "C"
JNIEXPORT void JNICALL
Java_com_pwj_record_ui_video_record_Mp3Encoder_init(JNIEnv *env, jclass clazz, jint in_sample_rate, jint out_channel, jint out_sample_rate, jint out_bitrate, jint quality) {
    if (glf != NULL) {
        lame_close(glf);
        glf = NULL;
    }
    glf = lame_init();
    lame_set_in_samplerate(glf, in_sample_rate);
    lame_set_num_channels(glf, out_channel);
    lame_set_out_samplerate(glf, out_sample_rate);
    lame_set_brate(glf, out_bitrate);
    lame_set_quality(glf, quality);
    lame_init_params(glf);
}