package com.pwj.apt.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @Author: pwj
 * @Date: 2020/6/29 15:11
 * @FileName: CreateFileByJavaPoetProcessor
 * @Description: 通过JavaPoet生成java文件
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.pwj.apt.annotation.Who")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class CreateFileByJavaPoetProcessor extends AbstractProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        createFileByJavaPoet(annotations, roundEnv);
        return false;
    }

    private void createFileByJavaPoet(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //创建main方法
        MethodSpec main = MethodSpec.methodBuilder("main")
                //设置可见修饰符public static
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                //设置返回值void
                .returns(Void.class)
                //添加参数类型为String数组，且参数名称为args
                .addParameter(String[].class,"args")
                //添加语句
                .addStatement("$T.out.println($s)",System.class,"Hello JavaPoet!")
                .build();
        //创建类
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                //将main方法添加到HelloWord类中
                .addMethod(main)
                .build();
        //创建文件，第一个参数是包名，第二个参数是相关类
        JavaFile file = JavaFile.builder("com.pwj.record", helloWorld).build();
        try {
            file.writeTo(processingEnv.getFiler());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
