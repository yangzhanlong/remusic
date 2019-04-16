package com.me.component_base.utils;

import android.support.v4.content.ContextCompat;

import com.me.component_base.BaseApp;


/**
 * @author yang
 */
public class UiUtils {
    public static int getColor(int resId) {
        return ContextCompat.getColor(BaseApp.getContext(), resId);
    }

    public static String getString(int resId) {
        return BaseApp.getContext().getApplicationContext().getResources().getString(resId);
    }
}
