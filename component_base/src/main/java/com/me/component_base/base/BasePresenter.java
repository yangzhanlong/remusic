package com.me.component_base.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.IView;
import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * @author yang
 */
public abstract class BasePresenter<V extends IView> extends XPresent<V> {
    protected Context mContext;
    public void onCleared() {
    }

    public void attachView(Context context, V view) {
        attachV(view);
        mContext = context;
    }

    public void detachView() {
        detachV();
    }

    public void onCreatePresenter(@Nullable Bundle savedState) {
        if (savedState != null) {
            String str = savedState.getString("Activity");
            XLog.d(str);
        }
    }

    public void onDestroyPresenter() {
        detachV();
        mContext = null;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString("Activity", "test");
    }

    public Context getContext() {
        return mContext;
    }
}
