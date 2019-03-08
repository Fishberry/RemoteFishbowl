package com.example.shinj.navmain;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class NotificationService extends Service {

    boolean mQuit;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "알림 서비스가 종료되었습니다.", Toast.LENGTH_SHORT).show();
        mQuit = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

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
                socket = IO.socket("http://fishberry.iptime.org:3000/");
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
                        }
                        if(pHValue < minPH || pHValue > maxPH) {
                            //수질 Notification 알림
                        }
                    });
                    Thread.sleep(30000);
                } catch (Exception e) { }
            }
        }


    }
}
