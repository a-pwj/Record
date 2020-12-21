package com.pwj.record.ioc.reflect;

/**
 * @Author: pwj
 * @Date: 2020/6/29 11:22
 * @FileName: PersonReflect
 * @Description: description
 */
public class PersonReflect {

    private String name;
    private int age;

    public PersonReflect() {
    }

    public PersonReflect(String name) {
        this.name = name;
    }

    public PersonReflect(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
