package com.pwj.record

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor

/**
 * @Author:          pwj
 * @Date:            2020/7/17 11:17
 * @FileName:        BaseActivity
 * @Description:     description
 */
open class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    /**
     * 定义View 只能点一次的扩展方法
     */
    fun View.setOnceClick(block: (view: View) -> Unit) {
        /**
         * 定义写成的一个消费者模式
         * actor 谨慎使用 ObsoleteCoroutinesApi 废弃的api
         */
        val eventActor = actor<View>(Dispatchers.Main) {
            // 这里注意，协程 channel 若没有数据，会处于 挂起 状态。直到有数过来才会执行
            for (view in channel) {
                block(view)
                // 500 毫秒 才能接受下一次的点击
                delay(500)
            }
        }
        setOnClickListener {
            /**
             * 发送输出,若消费者,没有消费等待数据,发送数据就会失败
             */
            eventActor.offer(it)
        }
    }
}