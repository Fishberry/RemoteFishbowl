package com.example.shinj.navmain;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.socket.client.IO;
import io.socket.client.Socket;

public class WaterFragActivity extends BaseActivity implements View.OnClickListener {

    private Socket socket;
    Button btn_water_frag_now, btn_water_frag_reserve;
    FragmentManager fm;
    FragmentTransaction tran;
    WaterNowFragment waterNowFragment;
    WaterReserveFragment waterReserveFragment;
    Handler handler = new Handler(); // Thread 에서 화면에 그리기 위해서 필요
    boolean waterFlag;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_water_frag_now = findViewById(R.id.btn_water_frag_now);
        btn_water_frag_reserve = findViewById(R.id.btn_water_frag_reserve);
        btn_water_frag_now.setOnClickListener(this);
        btn_water_frag_reserve.setOnClickListener(this);

        waterNowFragment = new WaterNowFragment();
        waterReserveFragment = new WaterReserveFragment();

        setFrag(0);

        try {
            socket = IO.socket("http://" + address + ":3000/");
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
        bundle.putString("address", address); // key , value
        waterNowFragment.setArguments(bundle);

        socket.emit("reqChanged", "isChanged");
        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
        }).on("resChanged", (Object... objects) -> {
            if(objects[0].toString() == "true")
                waterFlag = true;
            else
                waterFlag = false;
        });

        waterFlag = waterNowFragment.waterFlag;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_water_frag;
    }

    public void onStartWaterNowButton(View v) {
        waterFlag = true;
        if(count == 0)
            socket.emit("reqWaterNow", "StartOUT");
        else
            socket.emit("reqWaterNowRestart", "reqWaterNowRestart");
        Toast.makeText(getApplicationContext(), "지금환수 시작", Toast.LENGTH_SHORT).show();
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        socket.emit("reqPercent", "reqPercent");
                        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                        }).on("resPercent", (Object... objects) -> {
                            count = Integer.parseInt(objects[0].toString());
                        });
                        handler.post(new Runnable() {
                            public void run() {
                                waterNowFragment.progressBarWater.setProgress(count);
                                if (count < 100 && waterFlag == true) {   // 환수 진행중인 상태
                                    waterNowFragment.progressRateWater.setText(count + " %");
                                    waterNowFragment.btnPauseWaterNow.setVisibility(View.VISIBLE);
                                } else if (count >= 100){  // 환수 끝났을 때
                                    Toast.makeText(WaterFragActivity.this, "환수를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                                    waterNowFragment.btnPauseWaterNow.setVisibility(View.INVISIBLE);
                                    count = 0;
                                }
                            }
                        });
                        Thread.sleep(200);
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
    }

    public void onPauseWaterNowButton(View v) {
        waterFlag = false;
        socket.emit("reqWaterNowPause", "WaterPause");
        Toast.makeText(getApplicationContext(), "환수 일시 정지", Toast.LENGTH_SHORT).show();
        waterNowFragment.btnPauseWaterNow.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_water_frag_now:
            setFrag(0);
            break;
        case R.id.btn_water_frag_reserve:
            setFrag(1);
            break;
        }
    }

    public void setFrag(int n) {    //프래그먼트를 교체하는 작업을 하는 메소드
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n) {
            case 0:
                tran.replace(R.id.frag_frame, waterNowFragment);
                tran.commit();
                break;
            case 1:

                tran.replace(R.id.frag_frame, waterReserveFragment);
                tran.commit();
                break;
        }
    }

    public void saveWaterButton(View v) {

        int yearWater, monthWater, dayWater, hourWater, minunteWater;
        String timerWater;
        yearWater = waterReserveFragment.datePickerWater.getYear();
        monthWater = waterReserveFragment.datePickerWater.getMonth() + 1;
        dayWater = waterReserveFragment.datePickerWater.getDayOfMonth();
        hourWater = waterReserveFragment.timePickerWater.getHour();
        minunteWater = waterReserveFragment.timePickerWater.getMinute();
        timerWater = Integer.toString(yearWater) + Integer.toString(monthWater) + Integer.toString(dayWater) + Integer.toString(hourWater) + Integer.toString(minunteWater) + "00";
        Toast.makeText(getApplicationContext(), yearWater + "년" + monthWater + "월" + dayWater + "일\n"
                + hourWater + ":" + minunteWater, Toast.LENGTH_LONG).show();
        socket.emit("insertWater", timerWater);
        //socket.disconnect();
    }

    public void cancelWaterButton(View v) {
        //socket.disconnect();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //socket.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //socket.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //socket.disconnect();
    }
}