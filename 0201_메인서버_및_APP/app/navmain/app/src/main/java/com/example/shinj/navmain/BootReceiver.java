package com.example.shinj.navmain;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    private final static String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if(action.equals("android.intent.action.BOOT_COMPLETED")) {
//            Intent i = new Intent(context, NotificationService.class);
//            context.startService(intent);

            Intent serviceLauncher = new Intent(context, NotificationService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(serviceLauncher);
            else
                context.startService(serviceLauncher);
        }
    }

    public static boolean isServiceRunning(Context context, Class serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d(TAG, "ServiceRunning? = " + true);
                return true;
            }
        }

        Log.d(TAG, "ServiceRunning? = " + false);
        return false;
    }
}
