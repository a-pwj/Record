package com.pwj.record.ext

/**
 * @Author:          pwj
 * @Date:            2020/5/8 10:56
 * @FileName:        BooleanExt
 * @Description:     description
 */
//起桥梁作用的中间类，定义成协变
sealed class BooleanExt<out T>

//Nothing是所有类型的子类型，协变的类继承关系和泛型参数类型继承关系一致
object No : BooleanExt<Nothing>()

//data只涉及到了只读的操作
class TransferData<T>(val data: T) : BooleanExt<T>()

//声明成inline函数
inline fun <T> Boolean.yes(block: () -> T): BooleanExt<T> = when {
    this -> {
        TransferData(block.invoke())
    }
    else -> No
}


inline fun <T> BooleanExt<T>.no(block: () -> T): T = when (this) {//T处于函数返回值位置
    is No ->
        block()
    is TransferData ->
        this.data
}