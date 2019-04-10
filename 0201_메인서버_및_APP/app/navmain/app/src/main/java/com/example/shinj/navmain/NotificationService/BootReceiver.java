package com.example.shinj.navmain.NotificationService;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.shinj.navmain.DB.DBElement;
import com.example.shinj.navmain.DB.DBHelper;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("브로드캐스트", "onReceive()");

        String action = intent.getAction();

        //폰 재시작 시, 서비스 등록
        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            Intent serviceLauncher = new Intent(context, NotificationService.class);
            final DBHelper dbHelper = new DBHelper(context);
            DBElement dbElement = dbHelper.getResult();

            if (dbElement.watchElement != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    context.startForegroundService(serviceLauncher);
                else
                    context.startService(serviceLauncher);
            }
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
