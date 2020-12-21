package com.pwj.record.ioc.reflect;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import javax.security.auth.Subject;

/**
 * @Author: pwj
 * @Date: 2020/6/29 11:04
 * @FileName: ReflectUtils
 * @Description: 获取class对象的三种方法
 * 1、第一种方式 通过Class类的静态方法——forName()来实现
 * class1 = Class.forName("com.lvr.reflection.Person");
 * 2、第二种方式 通过类的class属性
 * class1 = Person.class;
 * 3、第三种方式 通过对象getClass方法
 * Person person = new Person();
 * class<?> class1 = person.getClass();
 * <p>
 * 获取对象 ：
 * //第一种方式 Class对象调用newInstance()方法生成，实际上是利用默认构造器来创建该类的实例
 * Object obj = class1.newInstance();
 * //第二种方式 对象获得对应的Constructor对象，再通过该Constructor对象的newInstance()方法生成
 * Constructor<?> constructor = class1.getDeclaredConstructor(String.class);//获取指定声明构造函数
 * obj = constructor.newInstance("hello");
 *
 * <p>
 * 反射获取父类
 * Class<?> superClass = Person.class.getSuperClass();
 * <p>
 * 有无declared的区别
 * //获取所有的属性，但不包括从父类继承下来的属性
 * public Field[] getDeclaredFields() throws SecurityException {}
 * //获取自身的所有的 public 属性，包括从父类继承下来的。
 * public Field[] getFields() throws SecurityException {}
 */
public class ReflectUtils {


    /**
     * 获取class 对象公开方法
     *
     * @param cls
     */
    public static void reflectInfoLog(Class<?> cls) {
        Log.i("reflect", "类名称:" + cls.getName());
        Log.i("reflect", "简单类名称:" + cls.getSimpleName());
        Log.i("reflect", "包名:" + cls.getPackage());
        Log.i("reflect", "是否为接口:" + cls.isInterface());
        Log.i("reflect", "是否为基本类型:" + cls.isPrimitive());
        Log.i("reflect", "是否为数组对象:" + cls.isArray());
        Log.i("reflect", "父类名称:" + cls.getSuperclass().getName());
    }

    public static void reflectConstructors(Class<?> cls) {
        //获取所有构造方法
        cls.getConstructors();
        cls.getDeclaredConstructors();
        //获取单个构造方法
//        cls.getConstructor(Class<?>... parameterTypes)
//        cls.getDeclaredConstructor(Class<?>... parameterTypes)
        //获取所有的方法
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            Class<?> methodReturnType = method.getReturnType();
            Class<?>[] methodParameterTypes = method.getParameterTypes();
        }
//        cls.getDeclaredMethod("name",String.class);
        //获取成员变量
        cls.getDeclaredFields();
//        cls.getDeclaredField("name");

    }

    private void methodT() {
        try {
            Class<?> cls = Class.forName("com.pwj.record.ioc.reflect.PersonReflect");
            //生成新的对象
            PersonReflect personReflect = (PersonReflect) cls.newInstance();
            int age = personReflect.getAge();
            Field field = cls.getDeclaredField("age");
            field.setAccessible(true);
            field.setInt(personReflect, 20);
            //反射访问成员变量值
            int anInt = field.getInt(personReflect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Type[] GenericTest() {
        try {
            Class<?> cls = Class.forName("com.pwj.record.ioc.reflect.PersonReflect");
            //生成新的对象
            PersonReflect personReflect = (PersonReflect) cls.newInstance();
            Field field = cls.getField("getName");
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
                return actualTypeArguments;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    interface Subject{
        void request();
    }

    class RealSubject implements Subject{
        @Override
        public void request(){
            System.out.println("request");
        }
    }

    class ProxyHandler implements InvocationHandler{
        private Subject subject;

        public ProxyHandler(Subject subject) {
            this.subject = subject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //定义预处理的工作，当然你也可以根据 method 的不同进行不同的预处理工作
            System.out.println("====before====");
            Object result = method.invoke(subject, args);
            System.out.println("====after====");
            return result;
        }
    }

    private void test7(){
        RealSubject subject = new RealSubject();
        ProxyHandler handler = new ProxyHandler(subject);
        Subject proxySubject = (Subject) Proxy.newProxyInstance(RealSubject.class.getClassLoader(),RealSubject.class.getInterfaces(),handler);
        proxySubject.request();

    }



}
