package com.pwj.parallaximage.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.pwj.parallaximage.controller.BaseImageController;
import com.pwj.parallaximage.utils.LoggerUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


/**
 * 使用Picasso加载图片
 */

public class PicassoImageController extends BaseImageController {

    private Context mContext;

    private String imageUrl;

    public PicassoImageController(Context context, String imageUrl) {
        this.mContext = context;
        this.imageUrl = imageUrl;
    }

    @Override
    public void loadImage(final int viewWidth) {
        if (imageUrl.isEmpty()) {
            return;
        }

        // 利用Picasso获取bitmap
        Picasso.get()
                .load(imageUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // 处理bitmap
                        handleBitmap(viewWidth, bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        LoggerUtils.INSTANCE.LOGE("Picasso加载图片失败");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

}
