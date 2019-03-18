package com.example.shinj.navmain;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;

public class NotificationService extends Service {

    boolean mQuit;
    String address;
    FileInputStream fis = null;
    BufferedReader br = null;
    String ip;
    NotificationManager notificationManager = null;
    NotificationChannel notificationChannel = null;
    PendingIntent pendingIntent = null;
    final DBHelper dbHelper = new DBHelper(this);
    private Context context = null;

    public NotificationService() {}
    public NotificationService(Context applicationContext) {
        super();
        context = applicationContext;
    }


    @Override
    public void onCreate() {

        Log.d("TAG", "Service Create");
        super.onCreate();

        //startForeground(1, new Notification());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationChannel = new NotificationChannel("notification", "notification_channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onDestroy() {

        Log.d("TAG", "Service Destroy");
        super.onDestroy();

        Intent broadcastIntent = new Intent("fishberry.example.com.NotificationService");
        sendBroadcast(broadcastIntent);
        stoptimertask();

        Toast.makeText(this, "알림 서비스가 종료되었습니다.", Toast.LENGTH_SHORT).show();
        mQuit = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("서비스", "onConfigurationChanged()");
        android.os.Debug.waitForDebugger();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        ip = dbHelper.getResult();
        startTimer();

        mQuit = false;
        NotificationThread thread = new NotificationThread(this);
        thread.start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class NotificationThread extends Thread {
        NotificationService mParent;
        Socket socket;
        double temperValue;
        double pHValue;
        int minTemper, maxTemper;
        double minPH, maxPH;

        public NotificationThread(NotificationService parent) {
            mParent = parent;
        }

        @Override
        public void run() {

            try {
                socket = IO.socket("http://" + ip + ":3000/");
                socket.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    socket.emit("reqMsg", "App에서 측정값 받아갑니다");
                    socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                    }).on("serverMsg", (Object... objects) -> {
                        //Notification 하기
                        temperValue = Double.parseDouble(objects[0].toString());
                        pHValue = Double.parseDouble(objects[1].toString());
                        minTemper = Integer.parseInt(objects[2].toString());
                        maxTemper = Integer.parseInt(objects[3].toString());
                        minPH = Double.parseDouble(objects[4].toString());
                        maxPH = Double.parseDouble(objects[5].toString());

                        if(temperValue < minTemper || temperValue > maxTemper) {
                            //온도 Notification 알림
                            pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, new Intent(getApplicationContext(), TemperatureActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                            Notification.Builder builder = new Notification.Builder(NotificationService.this)
                                    .setContentTitle("온도 경고!")
                                    .setContentText("설정한 온도를 벗어났습니다! / 현재 온도 : " + objects[0].toString())
                                    .setSmallIcon(R.drawable.splash_fish)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                builder.setChannelId("notification");
                            }
                            else {
                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            }
                            notificationManager.notify(0, builder.build());
                        }
                        if(pHValue < minPH || pHValue > maxPH) {
                            //수질 Notification 알림
                            pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, new Intent(getApplicationContext(), WaterFragActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                            Notification.Builder builder = new Notification.Builder(NotificationService.this)
                                    .setContentTitle("수질 경고!")
                                    .setContentText("설정한 수질을 벗어났습니다! / 현재 수질 : " + objects[1].toString())
                                    .setSmallIcon(R.drawable.splash_fish)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                builder.setChannelId("notification");
                            }
                            else {
                                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            }
                            notificationManager.notify(0, builder.build());
                        }
                    });
                    Thread.sleep(30000);
                } catch (Exception e) { }
            }
        }
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        timer = new Timer();
        initializeTimerTast();
        timer.schedule(timerTask, 1000, 1000);
    }

    public void initializeTimerTast() {
        timerTask = new TimerTask() {
            @Override
            public void run() {

            }
        };
    }

    public void stoptimertask() {
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("TAG", "NotificationService.onTaskRemoved");

        Intent intent = new Intent(getApplicationContext(), NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
        super.onTaskRemoved(rootIntent);
    }
}
