package com.pwj.record.dsl

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Build
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi

/**
 * @Author:          pwj
 * @Date:            2020/4/21 14:35
 * @FileName:        Anim
 * @Description:     description
 */
abstract class Anim {

    /**
     * 真正运行的动画
     */
    abstract var animator: Animator
    var builder: AnimatorSet.Builder? = null

    /**
     * 时长
     */
    var duration
        get() = 300L
        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
        set(value) {
            animator.duration = value
        }

    /**
     * 插值器
     */
    var interpolator
        get() = LinearInterpolator() as Interpolator
        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
        set(value) {
            animator.interpolator = value
        }

    /**
     * 延迟时长
     */
    var delay
        get() = 0L
        @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
        set(value) {
            animator.startDelay = value
        }

    /**
     * 回调
     */
    var onRepeat: ((Animator) -> Unit)? = null
    var onEnd: ((Animator) -> Unit)? = null
    var onCancel: ((Animator) -> Unit)? = null
    var onStart: ((Animator) -> Unit)? = null

    /**
     * 重置 [ValueAnimator] 的值
     */
    abstract fun reverse()

    /**
     * 开始动画
     */
    abstract fun toBegining()

    internal fun addListener() {
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                animation?.let { onRepeat?.invoke(it) }
            }

            override fun onAnimationEnd(animation: Animator?) {
                animation?.let { onEnd?.invoke(it) }
            }

            override fun onAnimationCancel(animation: Animator?) {
                animation?.let { onCancel?.invoke(it) }
            }

            override fun onAnimationStart(animation: Animator?) {
                animation?.let { onStart?.invoke(it) }
            }

        })
    }
}