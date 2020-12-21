package com.pwj.fpage.utils;

import com.google.gson.Gson;

/**
 * @Author: pwj
 * @Date: 2020/7/30 13:53
 * @FileName: GsonUtils
 * @Description: description
 */
public class GsonUtils {
    private static final Gson ourInstance = new Gson();

    static Gson getInstance() {
        return ourInstance;
    }

    private GsonUtils() {
    }

    public static String toJson(Object o) {
        return ourInstance.toJson(o);
    }
}
