package com.example.shinj.navmain.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.shinj.navmain.DB.DBElement;
import com.example.shinj.navmain.DB.DBHelper;
import com.example.shinj.navmain.IntentData;
import com.example.shinj.navmain.MainActivity;
import com.example.shinj.navmain.NotificationService.BootReceiver;
import com.example.shinj.navmain.NotificationService.NotificationService;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class LoginPresenterImpl implements LoginPresenter {

    LoginPresenter.View view;

    @Override
    public void rememberIP(DBElement dbElement, Socket socket, IntentData intentData, Context context) {
        if(dbElement.getIsRememberIP() == 1) {
            try {
                socket = IO.socket("http://" + dbElement.getIp() + ":3000/");
                socket.connect();
                intentData.setAddress(dbElement.getIp());
                intentData.setSocket(socket);

                if (dbElement.watchElement != 0) {
                    NotificationService notificationService = new NotificationService(context);
                    Intent notificationIntent = new Intent(context, notificationService.getClass());

                    //서비스 실행중인지 확인
                    if (!BootReceiver.isServiceRunning(context, notificationService.getClass())) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            context.startForegroundService(notificationIntent);
                        else
                            context.startService(notificationIntent);
                    }
                }

                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connectServer(EditText ipEdit, EditText pwEdit, CheckBox checkBox, Context context) {

        final DBHelper dbHelper = new DBHelper(context);
        Socket socket;
        DBElement dbe = null;

        try {
            socket = IO.socket("http://" + ipEdit.getText().toString() + ":3000/");
            socket.connect();
            socket.emit("confirmPassword", pwEdit.getText().toString());
            Socket finalSocket = socket;
            socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
            }).on("passwordResult", (Object... objects) -> {

                String confirm;
                int isChecking;
                DBElement dbElement;
                IntentData intentData = IntentData.getInstance();

                Log.d("글자 확인", objects[0].toString());
                confirm = objects[0].toString();
                Log.d("확인", confirm);
                if(confirm.equals("OK")) {
                    if (checkBox.isChecked() == true)
                        isChecking = 1;
                    else
                        isChecking = 0;

                    dbElement = dbHelper.getResult();

                    if (dbElement.getIp().equals("NoValue"))
                        dbHelper.insert(ipEdit.getText().toString(), isChecking);
                    else
                        dbHelper.update(ipEdit.getText().toString(), isChecking);

                    intentData.setAddress(ipEdit.getText().toString());
                    intentData.setSocket(finalSocket);

                    if (dbElement.watchElement != 0) {
                        NotificationService notificationService = new NotificationService(context);
                        Intent notificationIntent = new Intent(context, notificationService.getClass());

                        //서비스 실행중인지 확인
                        if (!BootReceiver.isServiceRunning(context, notificationService.getClass())) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                context.startForegroundService(notificationIntent);
                            else
                                context.startService(notificationIntent);
                        }
                    }

                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }
                else {
                    finalSocket.disconnect();
                    view.errorLoginToast();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
