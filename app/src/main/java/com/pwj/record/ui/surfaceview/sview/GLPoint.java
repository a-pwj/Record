package com.pwj.record.ui.surfaceview.sview;

import android.opengl.GLES20;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @Author: pwj
 * @Date: 2020/9/11 15:26
 * @FileName: GLPoint
 * @Description: 创建一个GLPoint
 * <p>
 * [1] 准备顶点着色代码和片段着色代码
 * [2] 准备顶点和颜色数据
 * [3] 加载着色器代码并初始化程序
 * [4] 绘制逻辑 (添加程序->启用顶点->绘制)
 */
public class GLPoint {

    //顶点着色代码
    final String vsh = "#version 300 es\n" +
            "layout (location = 0) in vec3 aPosition; \n" +
            "layout (location = 1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 color2frag;\n" +
            "\n" +
            "void main(){\n" +
            "    gl_Position = vec4(aPosition.x,aPosition.y, aPosition.z, 1.0);\n" +
            "    color2frag = aColor;\n" +
            "gl_PointSize=10.0;" +
            "}";

    //片段着色代码
    final String fsh = "#version 300 es\n" +
            "precision mediump float;\n" +
            "out vec4 outColor;\n" +
            "in vec4 color2frag;\n" +
            "\n" +
            "void main(){\n" +
            "    outColor = color2frag;\n" +
            "}";

    //顶点数组
    private final float vertexes[] = {   //以逆时针顺序
            0.0f, 0.0f,0.0f,//原点
            0.5f,0.0f,0.0f,
            0.5f,0.5f,0.0f,
            0.0f,0.5f,0.0f,

    };

    // 颜色数组
    private final float colors[] = new float[]{
            1.0f, 1.0f, 1.0f, 1.0f,//白色
            1.0f, 0.0f, 0.0f, 1.0f,//红色
            0.0f, 1.0f, 0.0f, 1.0f,//绿色
            0.0f, 0.0f, 1.0f, 1.0f,//蓝色
    };

    private int program;
    private static final int VERTEX_DIMENSION = 3;
    private static final int COLOR_DIMENSION = 4;

    private FloatBuffer vertBuffer;
    private FloatBuffer colorBuffer;

    private int aPosition = 0;//位置的句柄
    private int aColor = 1;//颜色的句柄

    public GLPoint() {
        program = initProgram();
        vertBuffer = getFloatBuffer(vertexes);
        colorBuffer = getFloatBuffer(colors);
    }

    private int initProgram() {
        int program;
        //顶点shader代码加载
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vsh);
        //片段shader代码加载
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fsh);
        //创建空的opengl es 程序
        program = GLES30.glCreateProgram();
        //加入顶点着色器
        GLES30.glAttachShader(program, vertexShader);
        //加入片元着色器
        GLES30.glAttachShader(program, fragmentShader);
        //创建可执行的opengL es 项目
        GLES30.glLinkProgram(program);
        return program;
    }

    public void draw() {
        //清除颜色缓存和深度缓存
        GLES30.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //将程序添加到opengl es 环境中
        GLES30.glUseProgram(program);
        //启用顶点句柄
        GLES30.glEnableVertexAttribArray(aPosition);
        //启用颜色句柄
        GLES30.glEnableVertexAttribArray(aColor);
        //准备坐标数据
        GLES30.glVertexAttribPointer(
                aPosition,COLOR_DIMENSION,
                GLES30.GL_FLOAT,false,
                VERTEX_DIMENSION*4,vertBuffer
        );
        //准备颜色数据
        GLES30.glVertexAttribPointer(
                aColor,COLOR_DIMENSION,
                GLES30.GL_FLOAT,false,
                COLOR_DIMENSION*4,colorBuffer
        );
        //绘制点
        GLES30.glDrawArrays(GLES30.GL_POINTS,0,vertexes.length/ VERTEX_DIMENSION);

        //禁用顶点数组
        GLES30.glDisableVertexAttribArray(aPosition);
        GLES30.glDisableVertexAttribArray(aColor);


    }


    /**
     * float数组缓冲数据
     *
     * @param vertexs 顶点
     * @return 获取浮点形缓冲数据
     */
    public static FloatBuffer getFloatBuffer(float[] vertexs) {
        FloatBuffer buffer;
        ///每个浮点数:坐标个数* 4字节
        ByteBuffer qbb = ByteBuffer.allocateDirect(vertexs.length * 4);
        //使用本机硬件设备的字节顺序
        qbb.order(ByteOrder.nativeOrder());
        // 从字节缓冲区创建浮点缓冲区
        buffer = qbb.asFloatBuffer();
        // 将坐标添加到FloatBuffer
        buffer.put(vertexs);
        //设置缓冲区以读取第一个坐标
        buffer.position(0);
        return buffer;
    }

    public static int loadShader(int type, String code) {
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, code);
        GLES30.glCompileShader(shader);
        return shader;
    }
}
