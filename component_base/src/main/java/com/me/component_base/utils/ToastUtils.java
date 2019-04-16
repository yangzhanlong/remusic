package com.me.component_base.utils;

import android.annotation.SuppressLint;

import com.me.component_base.BaseApp;
import com.me.component_base.view.CustomToast;


/**
 * @author yang
 */
public class ToastUtils {
    @SuppressLint("StaticFieldLeak")
    private static CustomToast toast;
    public static void showToast(String s){
        if (toast == null) {
            toast = new CustomToast(BaseApp.getContext());
        }
        toast.init(0, true).setText(s).show();
    }
}
