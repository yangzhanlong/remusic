package com.me.component_base.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.me.component_base.R;


/**
 *
 * @author li
 * @date 2017/8/21
 */

public class CustomToast extends Toast {
    private Context context;
    private View view;
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public CustomToast(Context context) {
        super(context);
        this.context = context;
    }

    public CustomToast init(int layoutId, boolean shortDuration){
        if (layoutId == 0){
            layoutId = R.layout.custom_toast;
        }
        view = LayoutInflater.from(context).inflate(layoutId, null);
        this.setView(view);
        this.setGravity(Gravity.CENTER,0,0);
        if (shortDuration) {
            this.setDuration(Toast.LENGTH_SHORT);
        } else {
            this.setDuration(Toast.LENGTH_LONG);
        }
        return this;
    }

    public CustomToast setText(String s){
        ((TextView)view.findViewById(R.id.tv_custom_toast)).setText(s);
        return this;
    }

    public CustomToast setTextView(int tvId, String s){
        ((TextView)view.findViewById(tvId)).setText(s);
        return this;
    }
}
