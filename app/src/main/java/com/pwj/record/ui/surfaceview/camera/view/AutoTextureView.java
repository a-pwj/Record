package com.pwj.record.ui.surfaceview.camera.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * @Author: pwj
 * @Date: 2020/9/30 14:00
 * @FileName: AutoTextureView
 * @Description: 自定义 正方形预览界面
 */
class AutoTextureView extends TextureView {

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public AutoTextureView(Context context) {
        super(context);
    }

    public AutoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
