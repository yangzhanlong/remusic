package com.me.component_base.manager;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

/**
 * Activity 管理类
 * @author yang
 */
public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
    }

    /**
     * 单一实例
     */
    public static ActivityManager getAppManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public Stack<Activity> getStack() {
        return activityStack;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * finish 当前 Activity
     */
    public void finishActivity() {
        if (activityStack != null) {
            Iterator<Activity> iterator = activityStack.iterator();
            if (iterator.hasNext()) {
                Activity activity = activityStack.lastElement();
                if (activity != null) {
                    activityStack.remove(activity);
                }
            }
        }
    }

    /**
     * finish 指定类名的 Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack != null) {
            Iterator<Activity> iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (activity != null && activity.getClass().equals(cls)) {
                    activity.finish();
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 移除指定 Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * finish 所有activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    public void recreateAllOtherActivity(Activity activity) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && activityStack.get(i) != activity) {
                activityStack.get(i).recreate();
            }
        }
    }
}
