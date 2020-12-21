package com.pwj.apt.compiler;


import com.google.auto.service.AutoService;
import com.pwj.apt.annotation.Who;
import com.pwj.apt.compiler.exception.ProcessingException;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @Author: pwj
 * @Date: 2020/6/29 15:48
 * @FileName: GetModifiersProcessor
 * @Description: 获取元素的可见性修饰符号
 */
@AutoService(Processor.class)
public class GetModifiersProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Who.class);
        try {
            for (Element element : elements) {
                if (element.getKind() == ElementKind.CLASS) {
                    //如果元素是类
                    checkElement(element);
                }
            }
        } catch (ProcessingException e) {
            log(e.getMessage());
        }
        return false;
    }

    /**
     * 判断当前元素是否是类，且当前可见修饰符是否是public，如果不是抛出异常
     *
     * @param element
     */
    private void checkElement(Element element) throws ProcessingException {
        Set<Modifier> modifiers = element.getModifiers();
        if (!modifiers.contains(Modifier.PUBLIC)) {
            //如果当前类不是public
            TypeElement typeElement = (TypeElement) element;
            throw new ProcessingException(typeElement, "This class %s is not public.", typeElement.getQualifiedName().toString());
        }
    }

    /**
     * 调用打印语句而已
     */
    private void log(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
