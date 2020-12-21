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
import javax.lang.model.element.TypeElement;

/**
 * @Author: pwj
 * @Date: 2020/6/29 15:34
 * @FileName: SpiltElementProcessor
 * @Description: description
 */
@AutoService(Processor.class)
public class ElementKindSpiltProcessor extends AbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(Who.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //这里通过获取所有包含Who注解的元素set集合
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Who.class);
        for (Element element :elements){
            if (element.getKind()== ElementKind.CLASS) {
                //如果元素是类
            }else if(element.getKind()==ElementKind.INTERFACE){
                //如果元素是接口
            }
        }
        return false;
    }
}
