package com.pwj.apt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author:          pwj
 * @Date:            2020/6/29 15:01
 * @FileName:        Who.java
 * @Description:     Who.java
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Who {
    String name();

    int age();
}