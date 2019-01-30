package com.example.shinj.navmain;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.socket.client.IO;
import io.socket.client.Socket;


public class FragActivity extends AppCompatActivity implements View.OnClickListener{
    Button bt1,bt2;
    FragmentManager fm;
    FragmentTransaction tran;
    FeedFrag feedFrag;
    TemperatureFragment temperatureFragment;

    /* Feed 프래그먼트 */
    public static final int QUANTITY_OK = 1000;
    private Button feedSettingDone;
    private Socket socket;

//    /* Temper 프래그먼트 */
//    private Spinner tempSpinner1, tempSpinner2, phSpinner1, phSpinner2;
//    private String tempNnumber, phNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        feedFrag = new FeedFrag();
        temperatureFragment = new TemperatureFragment();
        setFrag(0);

        feedSettingDone = (Button) findViewById(R.id.feedSettingDone);
//        tempSpinner1 = (Spinner)findViewById(R.id.tempRange1);
//        tempSpinner2 = (Spinner)findViewById(R.id.tempRange2);
//        phSpinner1 = (Spinner)findViewById(R.id.pHRange1);
//        phSpinner2 = (Spinner)findViewById(R.id.pHRange2);
//
//        final ArrayList<String> tempList = new ArrayList<>(); // 0도부터 40도까지 온도 리스트
//        final ArrayList<String> phList = new ArrayList<>(); // 0도부터 40도까지 ph 리스트
//
//        // 스피너에 온도 설정 범위를 0도부터 40도까지 설정
//        for(int i=0; i<41; i++) {
//            tempNnumber = String.valueOf(i);
//            tempList.add(tempNnumber);
//        }
//
//        // 스피너에 ph 범위를 1.0~14.0 까지 설정
//        for(double i=1.0; i<=14.0; i=i+0.1) {
//            phNumber = String.format("%.1f",i);
//            phList.add(phNumber);
//        }
//
//        ArrayAdapter spinnerAdapter_temp = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, tempList);
//        ArrayAdapter spinnerAdapter_ph = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, phList);
//        tempSpinner1.setAdapter(spinnerAdapter_temp);
//        tempSpinner2.setAdapter(spinnerAdapter_temp);
//        phSpinner1.setAdapter(spinnerAdapter_ph);
//        phSpinner2.setAdapter(spinnerAdapter_ph);
//
//        tempSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        tempSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        phSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        phSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        try {
            socket = IO.socket("http://175.204.79.66:3000");
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt1:
                setFrag(0);
                break;
            case R.id.bt2:
                setFrag(1);
                break;
        }
    }

    public void setFrag(int n){    //프래그먼트를 교체하는 작업을 하는 메소드
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n){
            case 0:
                tran.replace(R.id.frag_frame, feedFrag);
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.frag_frame, temperatureFragment);
                tran.commit();
                break;
        }
    }

    /* Feed 프래그먼트 */
    public void buttonClick(View v) {
        //버튼의 아이디로 버튼 구분
        switch (v.getId()) {
            case R.id.feedSettingDone:
                socket.emit("reqData", "2");
                break;
        }
    }

    public void userSettingfeedQuantity(View v) {
        Intent intent = new Intent(this, FeedQuantityUserSettingActivity.class);
        startActivityForResult(intent, QUANTITY_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == QUANTITY_OK && resultCode == RESULT_OK) {

        }
    }

    public void savefeedButton(View v) {
        Toast.makeText(this, "저장하였습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancelfeedButton(View v) {
        socket.disconnect();
        finish();
    }

//    /* Temper 프래그먼트 */
//
//    public void saveTemperButton(View v) {
//        Toast.makeText(this, "저장하였습니다.", Toast.LENGTH_SHORT).show();
//        finish();
//    }
//
//    public void cancelTemperButton(View v) {
//        finish();
//    }

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