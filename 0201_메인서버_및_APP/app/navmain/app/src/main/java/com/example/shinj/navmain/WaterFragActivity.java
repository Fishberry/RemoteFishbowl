package com.example.shinj.navmain;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_water_frag;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_water_frag_now:
                setFrag(0);
                break;
            case R.id.btn_water_frag_reserve:
                setFrag(1);
                break;
        }
    }

    public void setFrag(int n){    //프래그먼트를 교체하는 작업을 하는 메소드
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n){
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
        monthWater = waterReserveFragment.datePickerWater.getMonth()+1;
        dayWater = waterReserveFragment.datePickerWater.getDayOfMonth();
        hourWater = waterReserveFragment.timePickerWater.getHour();
        minunteWater = waterReserveFragment.timePickerWater.getMinute();
        timerWater = Integer.toString(yearWater) + Integer.toString(monthWater) + Integer.toString(dayWater) + Integer.toString(hourWater) + Integer.toString(minunteWater) + "00";
        Toast.makeText(getApplicationContext(), yearWater + "년" + monthWater + "월" + dayWater + "일\n"
                + hourWater + ":" + minunteWater, Toast.LENGTH_LONG).show();
        socket.emit("insertWater", timerWater);
        socket.disconnect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        socket.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        socket.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}
