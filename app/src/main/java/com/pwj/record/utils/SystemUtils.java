package com.pwj.record.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import java.util.Iterator;
import java.util.List;


/**
 * Author:           pwj
 * Date:             2020/1/8 17:48
 * FileName:         SystemUtils
 */
public class SystemUtils {
    public SystemUtils() {
    }

    public static String getCurProcessName(Context context) {
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager)context.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = mActivityManager.getRunningAppProcesses();
        if (runningAppProcessInfos == null) {
            return null;
        } else {
            Iterator var4 = runningAppProcessInfos.iterator();

            ActivityManager.RunningAppProcessInfo appProcess;
            do {
                if (!var4.hasNext()) {
                    return null;
                }

                appProcess = (ActivityManager.RunningAppProcessInfo)var4.next();
            } while(appProcess.pid != pid);

            return appProcess.processName;
        }
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static boolean isInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager)context.getSystemService("activity");
        List runningProcesses;
        if (Build.VERSION.SDK_INT > 20) {
            runningProcesses = am.getRunningAppProcesses();
            if (runningProcesses == null) {
                return true;
            }

            Iterator var4 = runningProcesses.iterator();

            while(true) {
                ActivityManager.RunningAppProcessInfo processInfo;
                do {
                    if (!var4.hasNext()) {
                        return isInBackground;
                    }

                    processInfo = (ActivityManager.RunningAppProcessInfo)var4.next();
                } while(processInfo.importance != 100);

                String[] var6 = processInfo.pkgList;
                int var7 = var6.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    String activeProcess = var6[var8];
                    if (activeProcess.equals(context.getPackageName())) {
                        Log.d("SystemUtils", "the process is in foreground:" + activeProcess);
                        return false;
                    }
                }
            }
        } else {
            runningProcesses = am.getRunningTasks(1);
            ComponentName componentInfo = ((ActivityManager.RunningTaskInfo)runningProcesses.get(0)).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}

