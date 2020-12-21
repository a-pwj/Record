package com.pwj.record.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule


/**
 * @Author:          pwj
 * @Date:            2020/7/3 9:38
 * @FileName:        AppGlideModule
 * @Description:     description
 */
@GlideModule
class AGlideModule : AppGlideModule() {


    //false 不解析AndroidMenifest文件 true 兼容Glide3
    override fun isManifestParsingEnabled(): Boolean = false

    //修改默认配置，如缓存配置
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val memoryCacheSizeBytes = 1024 * 1024 * 20 // 20mb

        val diskCacheSizeBytes = 1024 * 1024 * 100 //100 MB

        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes.toLong()))
            .setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes.toLong()))

    }

}