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
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.socket.client.IO;
import io.socket.client.Socket;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class NotificationService extends Service {

    DBElement dbElement;
    NotificationManager notificationManager = null;
    NotificationChannel notificationChannel = null;
    PendingIntent temperPendingIntent = null;
    PendingIntent pHPendingIntent = null;
    final DBHelper dbHelper = new DBHelper(this);
    private Context context = null;
    public int counter = 0;

    public NotificationService() {}
    public NotificationService(Context applicationContext) {
        super();
        context = applicationContext;
    }

    @Override
    public void onCreate() {
        Log.d("서비스", "onCreate()");
        super.onCreate();

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
        Log.d("서비스", "onDestroy()");
        super.onDestroy();

        if (dbElement.watchElement > 0) {
            Intent broadcastIntent = new Intent("fishberry.example.com.NotificationService");
            sendBroadcast(broadcastIntent);
            stopTimerTask();
        }
        //Toast.makeText(this, "알림 서비스가 종료되었습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("서비스", "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        dbElement = dbHelper.getResult();
        Log.d("감시값", String.valueOf(dbElement.watchElement));

        if (dbElement.watchElement == 0)
            onDestroy();

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

            Notification notification = new Notification.Builder(NotificationService.this)
                    .setContentTitle("")
                    .setContentText("")
                    .setChannelId("notification")
                    .build();

            startForeground(1, notification);
        }
        Log.d("서비스", "onStartCommand()");

        startTimer();

        TemperNotificationThread temperThread = new TemperNotificationThread(this);
        PHNotificationThread pHThread = new PHNotificationThread(this);
        temperThread.start();
        pHThread.start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("서비스", "onBind()");
        return null;
    }

    class PHNotificationThread extends Thread {

        NotificationService mParent;
        Socket socket;
        double pHValue, minPH, maxPH;

        public PHNotificationThread(NotificationService parent) {
            mParent = parent;
        }

        @Override
        public void run() {

            try {
                socket = IO.socket("http://" + dbElement.getIp() + ":3000/");
                socket.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            while (dbElement.watchElement == 2 || dbElement.watchElement == 3) {
                dbElement = dbHelper.getResult();
                try {
                    socket.emit("reqMsg", "App에서 측정값 받아갑니다");
                    socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                    }).on("serverMsg", (Object... objects) -> {
                        //Notification 하기
                        pHValue = Double.parseDouble(objects[1].toString());
                        minPH = Double.parseDouble(objects[4].toString());
                        maxPH = Double.parseDouble(objects[5].toString());
                        Log.d("확인", "현재 수질 : " + pHValue);
                        Log.d("확인", "최저 수질" + minPH);
                        Log.d("확인", "최고 수질" + maxPH);

                        if((pHValue < minPH || pHValue > maxPH) && (dbElement.watchElement == 2 || dbElement.watchElement == 3)) {
                            //수질 Notification 알림
                            pHPendingIntent = PendingIntent.getActivity(NotificationService.this, 0, new Intent(getApplicationContext(), WaterFragActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                            Notification.Builder builder = new Notification.Builder(NotificationService.this)
                                    .setContentTitle("수질 경고!")
                                    .setContentText("설정한 수질을 벗어났습니다! / 현재 수질 : " + objects[1].toString())
                                    .setSmallIcon(R.drawable.splash_fish)
                                    .setContentIntent(pHPendingIntent)
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
                    MILLISECONDS.sleep(dbElement.getpHLoopTime());
                } catch (Exception e) { }
                synchronized (this){
                    dbElement = dbHelper.getResult();
                }

                if (dbElement.watchElement == 0)
                    break;
            }
        }
    }

    class TemperNotificationThread extends Thread {
        NotificationService mParent;
        Socket socket;
        double temperValue;
        int minTemper, maxTemper;

        public TemperNotificationThread(NotificationService parent) {
            mParent = parent;
        }

        @Override
        public void run() {

            try {
                socket = IO.socket("http://" + dbElement.getIp() + ":3000/");
                socket.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            while (dbElement.watchElement == 1 || dbElement.watchElement == 3) {
                dbElement = dbHelper.getResult();
                try {
                    socket.emit("reqMsg", "App에서 측정값 받아갑니다");
                    socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                    }).on("serverMsg", (Object... objects) -> {
                        Log.d("확인", String.valueOf(dbElement.getTemperLoopTime()));
                        //Notification 하기
                        temperValue = Double.parseDouble(objects[0].toString());
                        minTemper = Integer.parseInt(objects[2].toString());
                        maxTemper = Integer.parseInt(objects[3].toString());
                        Log.d("확인", "현재 온도 : " + temperValue);
                        Log.d("확인", "최저 온도 : " + minTemper);
                        Log.d("확인", "최고 온도 : " + maxTemper);

                        // && (dbElement.watchElement == 1 || dbElement.watchElement == 3)
                        if((temperValue < minTemper || temperValue > maxTemper) && (dbElement.watchElement == 1 || dbElement.watchElement == 3)) {
                            //온도 Notification 알림
                            Log.d("확인", "온도 알림 들어간드아!");
                            temperPendingIntent = PendingIntent.getActivity(NotificationService.this, 0, new Intent(getApplicationContext(), TemperatureActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                            Notification.Builder builder = new Notification.Builder(NotificationService.this)
                                    .setContentTitle("온도 경고!")
                                    .setContentText("설정한 온도를 벗어났습니다! / 현재 온도 : " + objects[0].toString())
                                    .setSmallIcon(R.drawable.splash_fish)
                                    .setContentIntent(temperPendingIntent)
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
                    MILLISECONDS.sleep(dbElement.getTemperLoopTime());
                } catch (Exception e) { }
                synchronized (this){
                    dbElement = dbHelper.getResult();
                }

                if (dbElement.watchElement == 0)
                    break;
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

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(getApplicationContext(), NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
        super.onTaskRemoved(rootIntent);
    }
}
