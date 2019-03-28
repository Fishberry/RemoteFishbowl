package com.example.shinj.navmain.NotificationSetting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shinj.navmain.DBElement;
import com.example.shinj.navmain.DBHelper;
import com.example.shinj.navmain.NotificationService;
import com.example.shinj.navmain.R;

public class NotificationSettingActivity extends AppCompatActivity implements NotificationSettingPresenter.View{

    final DBHelper dbHelper = new DBHelper(this);
    DBElement dbElement = new DBElement();
    NotificationSettingPresenterImpl notificationSettingPresenterImpl = new NotificationSettingPresenterImpl(this);
    Switch setNotificationSwitch, setTemperSwitch, setPHSwitch;
    EditText setLoopTemperTimeEdit, setLoopPHTimeEdit;
    TextView setNotificationText, setTemperText, setPHText, setLoopTemperTimeText, secondTemperText, setLoopPHTimeText, secondPHText;
    View setNotificationLine, setTemperLine, setPHLine, setLoopTemperTimeLine, setLoopPHTimeLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        setNotificationSwitch = (Switch) findViewById(R.id.set_notification_switch);
        setTemperSwitch = (Switch) findViewById(R.id.set_temper_switch);
        setPHSwitch = (Switch) findViewById(R.id.set_pH_switch);
        setLoopTemperTimeEdit = (EditText) findViewById(R.id.set_loop_temper_time_edit);
        setLoopPHTimeEdit = (EditText) findViewById(R.id.set_loop_pH_time_edit);
        setNotificationText = (TextView) findViewById(R.id.set_notification_text);
        setTemperText = (TextView) findViewById(R.id.set_temper_text);
        setPHText = (TextView) findViewById(R.id.set_pH_text);
        setLoopTemperTimeText = (TextView) findViewById(R.id.set_loop_temper_time_text);
        secondTemperText = (TextView) findViewById(R.id.second_temper_text);
        setLoopPHTimeText = (TextView) findViewById(R.id.set_loop_pH_time_text);
        secondPHText = (TextView) findViewById(R.id.second_pH_text);
        setNotificationLine = (View) findViewById(R.id.set_notification_line);
        setTemperLine = (View) findViewById(R.id.set_temper_line);
        setPHLine = (View) findViewById(R.id.set_pH_line);
        setLoopTemperTimeLine = (View) findViewById(R.id.set_loop_temper_time_line);
        setLoopPHTimeLine = (View) findViewById(R.id.set_loop_pH_time_line);

        notificationSettingPresenterImpl.getDBElement(dbHelper);

        setNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    setTemperText.setVisibility(View.VISIBLE);
                    setTemperSwitch.setVisibility(View.VISIBLE);
                    setTemperLine.setVisibility(View.VISIBLE);
                    setPHText.setVisibility(View.VISIBLE);
                    setPHSwitch.setVisibility(View.VISIBLE);
                    setPHLine.setVisibility(View.VISIBLE);

                    setNotificationSwitch.setChecked(true);
                }
                else {
                    setTemperText.setVisibility(View.GONE);
                    setTemperSwitch.setVisibility(View.GONE);
                    setTemperLine.setVisibility(View.GONE);
                    setPHText.setVisibility(View.GONE);
                    setPHSwitch.setVisibility(View.GONE);
                    setPHLine.setVisibility(View.GONE);
                    setLoopTemperTimeText.setVisibility(View.GONE);
                    setLoopTemperTimeEdit.setVisibility(View.GONE);
                    secondTemperText.setVisibility(View.GONE);
                    setLoopTemperTimeLine.setVisibility(View.GONE);
                    setLoopPHTimeText.setVisibility(View.GONE);
                    setLoopPHTimeEdit.setVisibility(View.GONE);
                    secondPHText.setVisibility(View.GONE);
                    setLoopPHTimeLine.setVisibility(View.GONE);

                    setNotificationSwitch.setChecked(false);
                }
            }
        });

        setTemperSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    setLoopTemperTimeText.setVisibility(View.VISIBLE);
                    setLoopTemperTimeEdit.setVisibility(View.VISIBLE);
                    secondTemperText.setVisibility(View.VISIBLE);
                    setLoopTemperTimeLine.setVisibility(View.VISIBLE);
                }
                else {
                    setLoopTemperTimeText.setVisibility(View.GONE);
                    setLoopTemperTimeEdit.setVisibility(View.GONE);
                    secondTemperText.setVisibility(View.GONE);
                    setLoopTemperTimeLine.setVisibility(View.GONE);
                }
            }
        });

        setPHSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    setLoopPHTimeText.setVisibility(View.VISIBLE);
                    setLoopPHTimeEdit.setVisibility(View.VISIBLE);
                    secondPHText.setVisibility(View.VISIBLE);
                    setLoopPHTimeLine.setVisibility(View.VISIBLE);
                }
                else {
                    setLoopPHTimeText.setVisibility(View.GONE);
                    setLoopPHTimeEdit.setVisibility(View.GONE);
                    secondPHText.setVisibility(View.GONE);
                    setLoopPHTimeLine.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, NotificationService.class);
        stopService(i);
    }

    public void saveNotificationSetting(View view) {
        if (!setNotificationSwitch.isChecked()) {
            dbHelper.updateNotification(0, 0, 0);
            Toast.makeText(this, "알림 설정 저장", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (setTemperSwitch.isChecked() && !setPHSwitch.isChecked()) {
            dbHelper.updateNotification(1, Integer.parseInt(setLoopTemperTimeEdit.getText().toString()) * 1000, 0);
            Intent intent = new Intent(this, NotificationService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startForegroundService(intent);
            else
                startService(intent);

            Toast.makeText(this, "알림 설정 저장", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (!setTemperSwitch.isChecked() && setPHSwitch.isChecked()) {
            dbHelper.updateNotification(2, 0, Integer.parseInt(setLoopPHTimeEdit.getText().toString()) * 1000);
            Intent intent = new Intent(this, NotificationService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startForegroundService(intent);
            else
                startService(intent);

            Toast.makeText(this, "알림 설정 저장", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (setTemperSwitch.isChecked() && setPHSwitch.isChecked()) {
            dbHelper.updateNotification(3, Integer.parseInt(setLoopTemperTimeEdit.getText().toString()) * 1000, Integer.parseInt(setLoopPHTimeEdit.getText().toString()) * 1000);
            Intent intent = new Intent(this, NotificationService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startForegroundService(intent);
            else
                startService(intent);

            Toast.makeText(this, "알림 설정 저장", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (setNotificationSwitch.isChecked() && !setTemperSwitch.isChecked() && !setPHSwitch.isChecked()) {
            Toast.makeText(this, "알림 설정을 완벽하게 해 주십시오.", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelNotificationSetting(View view) {
        finish();
    }

    @Override
    public void freshDBElement(DBElement dbElement) {
        int watchElement = dbElement.getWatchElement();
        Log.d("확인", String.valueOf(watchElement));

        if (watchElement >= 1) {

            setNotificationSwitch.setChecked(true);

            switch (watchElement) {
                case 1:
                    setTemperSwitch.setChecked(true);
                    setTemperText.setVisibility(View.VISIBLE);
                    setTemperSwitch.setVisibility(View.VISIBLE);
                    setTemperLine.setVisibility(View.VISIBLE);
                    setPHText.setVisibility(View.VISIBLE);
                    setPHSwitch.setVisibility(View.VISIBLE);
                    setPHLine.setVisibility(View.VISIBLE);
                    setLoopTemperTimeText.setVisibility(View.VISIBLE);
                    setLoopTemperTimeEdit.setVisibility(View.VISIBLE);
                    secondTemperText.setVisibility(View.VISIBLE);
                    setLoopTemperTimeLine.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    setTemperSwitch.setChecked(true);
                    setTemperText.setVisibility(View.VISIBLE);
                    setTemperSwitch.setVisibility(View.VISIBLE);
                    setTemperLine.setVisibility(View.VISIBLE);
                    setPHText.setVisibility(View.VISIBLE);
                    setPHSwitch.setVisibility(View.VISIBLE);
                    setPHLine.setVisibility(View.VISIBLE);
                    setLoopPHTimeText.setVisibility(View.VISIBLE);
                    setLoopPHTimeEdit.setVisibility(View.VISIBLE);
                    secondPHText.setVisibility(View.VISIBLE);
                    setLoopPHTimeLine.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    setTemperSwitch.setChecked(true);
                    setPHSwitch.setChecked(true);
                    setTemperText.setVisibility(View.VISIBLE);
                    setTemperSwitch.setVisibility(View.VISIBLE);
                    setTemperLine.setVisibility(View.VISIBLE);
                    setPHText.setVisibility(View.VISIBLE);
                    setPHSwitch.setVisibility(View.VISIBLE);
                    setPHLine.setVisibility(View.VISIBLE);
                    setLoopTemperTimeText.setVisibility(View.VISIBLE);
                    setLoopTemperTimeEdit.setVisibility(View.VISIBLE);
                    secondTemperText.setVisibility(View.VISIBLE);
                    setLoopTemperTimeLine.setVisibility(View.VISIBLE);
                    setLoopPHTimeText.setVisibility(View.VISIBLE);
                    setLoopPHTimeEdit.setVisibility(View.VISIBLE);
                    secondPHText.setVisibility(View.VISIBLE);
                    setLoopPHTimeLine.setVisibility(View.VISIBLE);
                    break;
            }
        }
        else {
            setTemperText.setVisibility(View.GONE);
            setTemperSwitch.setVisibility(View.GONE);
            setTemperLine.setVisibility(View.GONE);
            setPHText.setVisibility(View.GONE);
            setPHSwitch.setVisibility(View.GONE);
            setPHLine.setVisibility(View.GONE);
            setLoopTemperTimeText.setVisibility(View.GONE);
            setLoopTemperTimeEdit.setVisibility(View.GONE);
            secondTemperText.setVisibility(View.GONE);
            setLoopTemperTimeLine.setVisibility(View.GONE);
            setLoopPHTimeText.setVisibility(View.GONE);
            setLoopPHTimeEdit.setVisibility(View.GONE);
            secondPHText.setVisibility(View.GONE);
            setLoopPHTimeLine.setVisibility(View.GONE);

            setNotificationSwitch.setChecked(false);
        }
    }
}
