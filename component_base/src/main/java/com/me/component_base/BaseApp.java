package com.me.component_base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.me.component_base.utils.SharedPreferencesUtil;


/**
 * @author yang
 */
public class BaseApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        initContext();
        initSpUtil();
    }

    private void initContext() {
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    private void initSpUtil() {
        SharedPreferencesUtil.getInstance(getContext(), "music");
    }
}
