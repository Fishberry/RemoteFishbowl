package com.example.shinj.navmain;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.util.logging.LogRecord;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("브로드캐스트", "onReceive()");

        String action = intent.getAction();

        //폰 재시작 시, 서비스 등록
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceLauncher = new Intent(context, NotificationService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(serviceLauncher);
            else
                context.startService(serviceLauncher);
        }
    }

    public static boolean isServiceRunning(Context context, Class serviceClass) {

        Log.d("브로드캐스트", "isServiceRunning()");

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName()))
                return true;
        }

        return false;
    }
}
