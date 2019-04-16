package com.me.component_base.net.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import com.me.component_base.R;
import com.me.component_base.utils.UiUtils;


/**
 * @author yang
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!isWifiAvailable(context)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isMobileAvailable(context)) {
                        Toast.makeText(context, UiUtils.getString(R.string.tv_network_disconnected), Toast.LENGTH_SHORT).show();
                    }
                }
            }, 2000);
        }
    }

    private boolean isWifiAvailable(Context context) {
        return checkConnectType(context, ConnectivityManager.TYPE_WIFI);
    }

    private boolean isMobileAvailable(Context context) {
        return checkConnectType(context, ConnectivityManager.TYPE_MOBILE);
    }

    private boolean checkConnectType(Context context, int type) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected() && networkInfo
                    .getType() == type);
        }
        return true;
    }
}
