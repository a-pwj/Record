package com.pwj.apt.compiler;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.pwj.apt.annotation.Who;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * @Author: pwj
 * @Date: 2020/6/29 15:41
 * @FileName: TypeKindSpiltProcessor
 * @Description: 通过TypeMirror获取元素类型
 */
@AutoService(Processor.class)
public class TypeKindSpiltProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Who.class);
        for (Element element : elements) {
            if (element.getKind() == ElementKind.INTERFACE) {
                ExecutableElement executableElement = (ExecutableElement) element;
                // 获取返回值类型
                TypeMirror returnType = executableElement.getReturnType();
                TypeKind kind = returnType.getKind();
                System.out.println("print return type----->" + kind.toString());
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(Who.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
