package com.pwj.fpage.annotation;

import com.pwj.fpage.enums.CoreAnim;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: pwj
 * @Date: 2020/7/30 13:44
 * @FileName: FPage
 * @Description: Fragment页面信息标注
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FPage {
    /**
     * @return 页面的名称
     */
    String name() default "";

    /**
     * @return 界面传递的参数Key
     */
    String[] params() default {""};

    /**
     * @return 页面切换的动画
     */
    CoreAnim anim() default CoreAnim.slide;

    /**
     *
     * @return 拓展字段
     */
    String extra() default "";

}
