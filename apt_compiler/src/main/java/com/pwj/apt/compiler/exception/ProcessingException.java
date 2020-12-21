package com.pwj.apt.compiler.exception;

import javax.lang.model.element.Element;

/**
 * @Author: pwj
 * @Date: 2020/6/29 15:50
 * @FileName: ProcessingException
 * Description:处理器自定义异常
 */
public class ProcessingException extends Exception {

    Element element;

    public ProcessingException(Element element, String msg, Object... args) {
        super(String.format(msg, args));
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}