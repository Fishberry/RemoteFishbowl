package com.example.shinj.navmain.NotificationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class RestartService extends BroadcastReceiver {

    private final static String TAG = RestartService.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "RestartService.onReceive");

        Intent serviceLauncher = new Intent(context, NotificationService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(serviceLauncher);
        else
            context.startService(serviceLauncher);
    }
}
