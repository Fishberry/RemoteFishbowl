package com.example.shinj.navmain.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shinj.navmain.DB.DBElement;
import com.example.shinj.navmain.DB.DBHelper;
import com.example.shinj.navmain.IntentData;
import com.example.shinj.navmain.MainActivity;
import com.example.shinj.navmain.NotificationService.BootReceiver;
import com.example.shinj.navmain.NotificationService.NotificationService;
import com.example.shinj.navmain.R;
import com.example.shinj.navmain.RegisterActivity;
import com.example.shinj.navmain.SplashActivity;

import java.io.Serializable;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class LoginActivity extends AppCompatActivity implements Serializable, LoginPresenter.View {

    private Socket socket;
    private EditText ipEdit;
    private EditText pwEdit;
    private String confirm;
    private CheckBox checkBox;
    private int isChecking = 0;
    IntentData intentData = IntentData.getInstance();
    final DBHelper dbHelper = new DBHelper(this);
    DBElement dbElement;
    LoginPresenterImpl loginPresenterimpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ipEdit = (EditText) findViewById(R.id.ipEdit);
        pwEdit = (EditText) findViewById(R.id.pwEdit);
        checkBox = (CheckBox) findViewById(R.id.remember_login_checkbox);
        dbElement = dbHelper.getResult();
        loginPresenterimpl = new LoginPresenterImpl();

        startSplash();

        loginPresenterimpl.rememberIP(dbElement, socket, intentData, this);
    }

    private void startSplash() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }

    public void pushLoginButton(View v) {
        loginPresenterimpl.connectServer(ipEdit, pwEdit, checkBox, this);
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
                        finish();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    @Override
    public void errorLoginToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "IP 혹은 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
