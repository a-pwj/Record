package com.pwj.parallaximage.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pwj.parallaximage.controller.BaseImageController;
import com.pwj.parallaximage.utils.LoggerUtils;


/**
 * 使用Glide加载图片
 */

public class GlideImageController extends BaseImageController {

    private Context mContext;

    private String imageUrl;

    public GlideImageController(Context context, String imageUrl) {
        this.mContext = context;
        this.imageUrl = imageUrl;
    }

    @Override
    public void loadImage(final int viewWidth) {
        if (imageUrl.isEmpty()) {
            return;
        }

//        // 利用Glide获取drawable
//        Glide.with(mContext).asDrawable().load(imageUrl).into(new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                // 处理drawable
//                handleDrawable(viewWidth, resource);
//            }
//        });

        Glide.with(mContext).load(imageUrl).listener(new RequestListener<Drawable>() {

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                LoggerUtils.INSTANCE.LOGE("Glide加载图片失败");
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                // 处理drawable
                handleDrawable(viewWidth, resource);
                return false;
            }
        }).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }
}
