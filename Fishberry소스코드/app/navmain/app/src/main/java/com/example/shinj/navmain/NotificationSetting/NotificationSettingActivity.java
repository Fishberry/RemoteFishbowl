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

import com.example.shinj.navmain.NotificationService.BootReceiver;
import com.example.shinj.navmain.DB.DBElement;
import com.example.shinj.navmain.DB.DBHelper;
import com.example.shinj.navmain.NotificationService.NotificationService;
import com.example.shinj.navmain.R;

public class NotificationSettingActivity extends AppCompatActivity implements NotificationSettingPresenter.View{

    final DBHelper dbHelper = new DBHelper(this);
    DBElement dbElement = new DBElement();
    NotificationSettingPresenterImpl notificationSettingPresenterImpl = new NotificationSettingPresenterImpl(this);
    Switch setNotificationSwitch, setTemperSwitch, setPHSwitch;
    EditText setLoopTemperTimeEdit, setLoopPHTimeEdit;
    TextView setNotificationText, setTemperText, setPHText, setLoopTemperTimeText, secondTemperText, setLoopPHTimeText, secondPHText;
    View setNotificationLine, setTemperLine, setPHLine, setLoopTemperTimeLine, setLoopPHTimeLine;
    TextView saveButton, cancelButton, initButton;

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
        saveButton = (TextView) findViewById(R.id.notification_setting_save_button);
        cancelButton = (TextView) findViewById(R.id.notification_setting_cancel_button);
        initButton = (TextView) findViewById(R.id.notification_setting_init_button);

        notificationSettingPresenterImpl.getDBElement(dbHelper);

        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                setTemperSwitch.setChecked(false);
                setPHSwitch.setChecked(false);
                setLoopTemperTimeEdit.setText("0");
                setLoopPHTimeEdit.setText("0");
            }
        });

        //알림 true 시, view
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
                    setTemperSwitch.setChecked(false);
                    setPHSwitch.setChecked(false);
                    setLoopTemperTimeEdit.setText("0");
                    setLoopPHTimeEdit.setText("0");
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
                    setTemperSwitch.setChecked(false);
                    setPHSwitch.setChecked(false);
                    setLoopTemperTimeEdit.setText("0");
                    setLoopPHTimeEdit.setText("0");
                }
            }
        });

        //온도 알림 on 시, view
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

                    setLoopTemperTimeEdit.setText("0");
                }
            }
        });

        //pH 알림 on 시, view
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

                    setLoopTemperTimeEdit.setText("0");
                }
            }
        });

        //저장 버튼
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //알림 설정을 하지 않았을 경우
                if (!setNotificationSwitch.isChecked()) {
                    dbHelper.updateNotification(0, 0, 0);
                    Toast.makeText(getApplicationContext(), "알림 설정 저장", Toast.LENGTH_SHORT).show();
                    finish();
                }
                //온도만 했을 경우
                else if (setTemperSwitch.isChecked() && !(setPHSwitch.isChecked()) && !(Integer.parseInt(setLoopTemperTimeEdit.getText().toString()) == 0) && !(setLoopTemperTimeEdit.getText().equals(""))) {
                    dbHelper.updateNotification(1, Integer.parseInt(setLoopTemperTimeEdit.getText().toString()) * 1000, 0);
                    Intent intent = new Intent(getApplicationContext(), NotificationService.class);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        startForegroundService(intent);
                    else
                        startService(intent);

                    Toast.makeText(getApplicationContext(), "알림 설정 저장", Toast.LENGTH_SHORT).show();
                    finish();
                }
                //수질만 했을 경우
                else if (!setTemperSwitch.isChecked() && setPHSwitch.isChecked() && !(Integer.parseInt(setLoopTemperTimeEdit.getText().toString()) == 0) && !(setLoopPHTimeEdit.getText().equals(""))) {
                    dbHelper.updateNotification(2, 0, Integer.parseInt(setLoopPHTimeEdit.getText().toString()) * 1000);
                    Intent intent = new Intent(getApplicationContext(), NotificationService.class);

                    if (!BootReceiver.isServiceRunning(getApplicationContext(), intent.getClass())) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            startForegroundService(intent);
                        else
                            startService(intent);
                    }

                    Toast.makeText(getApplicationContext(), "알림 설정 저장", Toast.LENGTH_SHORT).show();
                    finish();
                }
                //온도와 수질 둘 다 했을 경우
                else if (setTemperSwitch.isChecked() && setPHSwitch.isChecked() && !(Integer.parseInt(setLoopTemperTimeEdit.getText().toString()) == 0) && !(setLoopTemperTimeEdit.getText().equals("")) && !(Integer.parseInt(setLoopTemperTimeEdit.getText().toString()) == 0) && !(setLoopPHTimeEdit.getText().equals(""))) {
                    dbHelper.updateNotification(3, Integer.parseInt(setLoopTemperTimeEdit.getText().toString()) * 1000, Integer.parseInt(setLoopPHTimeEdit.getText().toString()) * 1000);
                    Intent intent = new Intent(getApplicationContext(), NotificationService.class);

                    if (!BootReceiver.isServiceRunning(getApplicationContext(), intent.getClass())) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            startForegroundService(intent);
                        else
                            startService(intent);
                    }

                    Toast.makeText(getApplicationContext(), "알림 설정 저장", Toast.LENGTH_SHORT).show();
                    finish();
                }
                //알림설정은 클릭했지만, 온도와 수질알림 둘 다 체크를 하지 않았을 경우
                else if (setNotificationSwitch.isChecked() && !setTemperSwitch.isChecked() && !setPHSwitch.isChecked()) {
                    Toast.makeText(getApplicationContext(), "알림 설정을 완벽하게 해 주십시오.", Toast.LENGTH_SHORT).show();
                }
                //온도 혹은 수질 알림을 체크했지만, 시간을 설정하지 않았을 때
                else if ((setTemperSwitch.isChecked() && ((Integer.parseInt(setLoopTemperTimeEdit.getText().toString()) == 0) || (setLoopTemperTimeEdit.getText().equals("")))) || (setPHSwitch.isChecked() && ((Integer.parseInt(setLoopTemperTimeEdit.getText().toString()) == 0) || (setLoopPHTimeEdit.getText().equals(""))))) {
                    Toast.makeText(getApplicationContext(), "알림 설정의 시간을 설정해주십시오.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //취소 버튼
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

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
                    setPHSwitch.setChecked(true);
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