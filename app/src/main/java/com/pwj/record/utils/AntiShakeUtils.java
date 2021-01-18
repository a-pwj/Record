package com.pwj.record.utils;

import android.view.View;

public final class AntiShakeUtils {
    private static int CLICK_VIEW_ID = 0;
    private static final long DEFAULT_DURATION = 1000;
    private static final int TAG_KEY = 2130706431;

    private AntiShakeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static boolean isValid(View view) {
        return isValid(view, DEFAULT_DURATION);
    }

    public static boolean isValid(View view, long duration) {
        if (view == null) {
            return false;
        }
        long curTime = System.currentTimeMillis();
        Object tag = view.getTag(TAG_KEY);
        if (!(tag instanceof Long)) {
            view.setTag(TAG_KEY, Long.valueOf(curTime));
            return true;
        }
        long preTime = ((Long) tag).longValue();
        if (view.getId() == CLICK_VIEW_ID && curTime - preTime <= duration) {
            return false;
        }
        view.setTag(TAG_KEY, Long.valueOf(curTime));
        CLICK_VIEW_ID = view.getId();
        return true;
    }
}