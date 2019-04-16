package com.me.component_base.net;

import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;

import java.text.DecimalFormat;
import java.util.TimerTask;

import cn.droidlover.xdroidmvp.log.XLog;

/**
 * @author yang
 */
public class NetSpeedDetectTask extends TimerTask {
    private Handler handler = new Handler();
    private Intent receiverIntent;
    private long rxtxTotal = 0;
    private boolean isNetBad = false;
    private int time;
    private double rxtxSpeed = 1.0f;
    private DecimalFormat showFloatFormat = new DecimalFormat("0.00");
    private boolean isFirst = true;
    private Context context;
    private long totalBytes = TrafficStats.getTotalRxBytes();

    NetSpeedDetectTask(Context context, Intent receiverIntent) {
        this.context = context;
        this.receiverIntent = receiverIntent;
    }

    @Override
    public void run() {
        isNetBad = false;
        long netSpeed = getNetSpeed();
        //XLog.d("NetSpeedDetectTask:" + showFloatFormat.format(netSpeed / 1024d) + "kb/s" );

        if ((netSpeed / 1024d) < 20 && (rxtxSpeed / 1024d) < 20) {
            time += 1;
        } else {
            time = 0;
        }

        rxtxSpeed = netSpeed;
        if (time >= 4) {
            isNetBad = true;
            XLog.d("网速差");
            time = 0;
        }
//        if (isNetBad) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    //receiverIntent.putExtra("is_slow_net_speed", isNetBad);
//                    //context.sendBroadcast(receiverIntent);
//                }
//            });
//        }
    }

    private long getNetSpeed() {
        long speed = TrafficStats.getTotalRxBytes() - totalBytes;
        totalBytes = TrafficStats.getTotalRxBytes();
        return speed;
    }
}
