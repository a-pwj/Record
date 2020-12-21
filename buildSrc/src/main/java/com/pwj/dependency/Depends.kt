package com.pwj.dependency

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope


/**
 * @Author:          pwj
 * @Date:            2020/8/3 10:51
 * @FileName:        Depends
 * @Description:     description
 */
object Deps {

    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"

    object Support {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
        const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
        const val design = "com.google.android.material:material:${Versions.material}"
        const val core = "androidx.core:core-ktx:${Versions.core}"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.junit}"
        const val ext_unit = "androidx.test.ext:junit:${Versions.runner}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    }

    object Lifecycle {
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
        const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
        const val common = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    }

    object Navigation {
        const val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigationKtx}"
        const val ui = "androidx.navigation:navigation-ui-ktx:${Versions.navigationKtx}"
    }

    object Glide {
        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

        /** glide动态加载webp*/
        const val webpdecoder = "com.zlc.glide:webpdecoder:${Versions.glideWebpDecoder}"
    }

    object Picasso {
        const val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"
    }

    object Stetho {
        /** 查看数据库*/
        const val stetho = "com.facebook.stetho:stetho:${Versions.stetho}"
        const val stethoOkhttp3 = "com.facebook.stetho:stetho-okhttp3:${Versions.stetho}"
    }

    object Permission {
        /** permission*/
        const val andPermission = "com.yanzhenjie:permission:${Versions.andPermission}"
    }

    object AutoService {
        /** 查看数据库*/
        const val android = "com.google.auto.service:auto-service:${Versions.autoService}"
        const val javapoet = "com.squareup:javapoet:${Versions.javaPoet}"
    }

    object Views {
        const val cardview = "androidx.cardview:cardview:${Versions.cardView}"
        const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
        const val slidingpanelayout =
            "androidx.slidingpanelayout:slidingpanelayout:${Versions.slidingpanelayout}"
    }

    object Others {
        //使用debugImplementation 不要直接使用implementation，使用implementation虽然不会在正式版中弹出内存泄漏的弹窗（因为LeakCanary内部做了限制），但是会增大约1mb的安装包体积
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakAndroid}"

        const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"

        const val BRVH = "com.github.CymChad:BaseRecyclerViewAdapterHelper:${Versions.BRVH}"

        const val gson = "com.google.code.gson:gson:${Versions.gson}"

        const val mmkv = "com.tencent:mmkv:${Versions.mmkv}"

        const val okio = "com.squareup.okio:okio:2.7.0"
    }

    fun addRepos(handler: RepositoryHandler) {
        handler.apply {
            google()
            jcenter()
            maven {
                setUrl("https://jitpack.io")
            }
            mavenCentral()
        }
    }

}

fun DependencyHandlerScope.addCommonDeps() {
    //test
    "testImplementation"(Deps.Test.junit)
    "androidTestImplementation"(Deps.Test.ext_unit)
    "androidTestImplementation"(Deps.Test.espresso)
    //module
//    "implementation"(project(":basemodule"))
    "kapt"(Deps.Glide.compiler)
}

object Versions {
    const val androidGradlePlugin = "4.0.1"
    const val kotlin = "1.3.72"
    const val appCompat = "1.1.0"
    const val core = "1.3.1"
    const val material = "1.1.0"
    const val constraint = "1.1.3"
    const val junit = "4.12"
    const val runner = "1.1.0"
    const val espresso = "3.1.0"
    const val picasso = "2.71828"
    const val glide = "4.11.0"
    const val glideWebpDecoder = "1.8.4.11.0"
    const val navigationKtx = "2.3.0"
    const val lifecycle = "2.2.0"
    const val leakAndroid = "2.4"
    const val lottie = "3.3.0"
    const val BRVH = "3.0.3"
    const val stetho = "1.5.1"
    const val autoService = "1.0-rc7"
    const val javaPoet = "1.10.0"
    const val gson = "2.8.6"
    const val mmkv = "1.0.24"
    const val andPermission = "2.0.3"


    const val cardView = "1.0.0"
    const val recyclerview = "1.1.0"
    const val slidingpanelayout = "1.1.0"

}

object DepBuild {
    const val applicationId = "com.pwj.record"
    const val minSdk = 21
    const val targetSdk = 29
    const val compileVersion = 29
    const val buildVersion = "29.0.3"
    const val versionCode = 1
    const val versionName = "1.0"
}