package com.me.component_base.net;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;

import cn.droidlover.xdroidmvp.log.XLog;

/**
 * @author yang
 */
public class NetSpeedService extends Service {
    private Timer timer;

    private Intent receiverIntent;
    public final static String NET_SPEED_RECEIVER_ACTION = "com.ridgepm.network_speed_action";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new NetSpeedDetectTask(this, receiverIntent), 1000L, 1000);
        }
        receiverIntent = new Intent();
        receiverIntent.setAction(NET_SPEED_RECEIVER_ACTION);
        XLog.d("NetSpeedService:onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
        XLog.d("NetSpeedService:onDestroy");
    }
}
