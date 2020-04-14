package com.ts.swipback

import java.lang.reflect.Constructor

/**
 * Author:           pwj
 * Date:             2020/4/8 16:59
 * FileName:         EasyReflect
 */
class EasyReflect private constructor(val clazz: Class<*>, private var intstance: Any?) {


    /**
     * 构造方法操作区
     *
     * 使用匹配参数的构造函数创建一个对象实例，并生成新的EasyReflect实例返回
     */
    fun instance(vararg args: Any?): EasyReflect {
        return
    }

    /**
     * 根据传入的参数类型匹配对应的构造器
     */
    fun getConstructor(vararg types:Class<*>):ConstructorReflect{

    }
    co


    class ConstructorReflect(val constructor: Constructor<*> ,val upper :EasyReflect){
        //参数是否可变
         fun newInstanceI(vararg args:Any?):EasyReflect{
            return create(constructor.newInstance(*args))
        }
    }

}