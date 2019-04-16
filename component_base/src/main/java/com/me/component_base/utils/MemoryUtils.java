package com.me.component_base.utils;

import android.app.ActivityManager;


import com.me.component_base.BaseApp;

import cn.droidlover.xdroidmvp.log.XLog;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @author yang
 */
public final class MemoryUtils {

    public static void getMemory() {
        ActivityManager activityManager = (ActivityManager) BaseApp.getContext().getSystemService(ACTIVITY_SERVICE);
        //最大分配内存
        if (activityManager != null) {
            int memory = activityManager.getMemoryClass();
            System.out.println("memory: " + memory);
            //最大分配内存获取方法2
            float maxMemory = (float) (Runtime.getRuntime().maxMemory() * 1.0 / (1024 * 1024));
            //当前分配的总内存
            float totalMemory = (float) (Runtime.getRuntime().totalMemory() * 1.0 / (1024 * 1024));
            //剩余内存
            float freeMemory = (float) (Runtime.getRuntime().freeMemory() * 1.0 / (1024 * 1024));
            XLog.d("maxMemory:" + maxMemory);
            XLog.d("totalMemory:" + totalMemory);
            XLog.d("freeMemory:" + freeMemory);
        }
    }
}
