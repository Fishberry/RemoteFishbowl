package com.example.shinj.navmain;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import io.socket.client.IO;
import io.socket.client.Socket;

public class LoginActivity extends AppCompatActivity implements Serializable {

    private Socket socket;
    private EditText ipEdit;
    private EditText pwEdit;
    private String confirm;
    IntentData intentData = IntentData.getInstance();
    FileOutputStream fos = null;
    BufferedWriter bw = null;
    final static String folderName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/connectInfor";
    final static String fileName = "infor.txt";
    final DBHelper dbHelper = new DBHelper(this);
    private Intent notificationServiceIntent;
    private NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ipEdit = (EditText) findViewById(R.id.ipEdit);
        pwEdit = (EditText) findViewById(R.id.pwEdit);

        startSplash();
    }

    private void startSplash() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }

    public void pushLoginButton(View v) {

        try {
            socket = IO.socket("http://" + ipEdit.getText().toString() + ":3000/");
            socket.connect();
            socket.emit("confirmPassword", pwEdit.getText().toString());
            socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
            }).on("passwordResult", (Object... objects) -> {

                Log.d("글자 확인", objects[0].toString());
                confirm = objects[0].toString();
                Log.d("확인", confirm);
                if(confirm.equals("OK")) {
                    String dbValue = dbHelper.getResult();
                    if (dbValue.equals("NoValue"))
                        dbHelper.insert(ipEdit.getText().toString());
                    else
                        dbHelper.update(ipEdit.getText().toString());

                    notificationService = new NotificationService(getApplicationContext());
                    notificationServiceIntent = new Intent(getApplicationContext(), notificationService.getClass());

                    if (!BootReceiver.isServiceRunning(this, notificationService.getClass()))
                        startService(notificationServiceIntent);

                    intentData.setAddress(ipEdit.getText().toString());
                    intentData.setSocket(socket);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    socket.disconnect();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "IP 혹은 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch(item.getItemId()) {
            case R.id.register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("종료")
                .setMessage("종료 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        socket.close();
                        finish();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
}
