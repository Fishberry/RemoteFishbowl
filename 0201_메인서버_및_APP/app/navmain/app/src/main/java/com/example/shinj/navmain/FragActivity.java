package com.example.shinj.navmain;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.socket.client.IO;
import io.socket.client.Socket;


public class FragActivity extends BaseActivity implements View.OnClickListener{

    Button bt1,bt2;
    FragmentManager fm;
    FragmentTransaction tran;
    FeedFrag feedFrag;
    TemperatureFragment temperatureFragment;
    int timerValue = 0;
    int circleValue = 0;
//    int minTemper = 0;
//    int maxTemper = 0;
//    double minPH = 0.0;
//    double maxPH = 0.0;

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

        bt1 = (Button) findViewById(R.id.btn_frag_feed);
        bt2 = (Button) findViewById(R.id.btn_frag_temperature);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        feedFrag = new FeedFrag();
        temperatureFragment = new TemperatureFragment();
        setFrag(0);

        feedSettingDone = (Button) findViewById(R.id.feedSettingDone);

        try {
            socket = IO.socket("http://fishberry.iptime.org:3000/");
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_frag;
    }

    //프레그먼트 클릭
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_frag_feed:
                setFrag(0);
                break;
            case R.id.btn_frag_temperature:
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
                socket.emit("reqData", "StartServo");
                break;
            case R.id.btn_feedtimer_8h:
                feedFrag.selectTimer(R.id.btn_feedtimer_8h);
                timerValue = 8 * 60 * 60;
                break;
            case R.id.btn_feedtimer_12h:
                feedFrag.selectTimer(R.id.btn_feedtimer_12h);
                timerValue = 12 * 60 * 60;
                break;
            case R.id.btn_feedtimer_24h:
                feedFrag.selectTimer(R.id.btn_feedtimer_24h);
                timerValue = 24 * 60 * 60;
                break;
            case R.id.feed1:
                feedFrag.selectCircle(R.id.feed1);
                circleValue = 1;
                break;
            case R.id.feed2:
                feedFrag.selectCircle(R.id.feed2);
                circleValue = 2;
                break;
            case R.id.feed3:
                feedFrag.selectCircle(R.id.feed3);
                circleValue = 3;
                break;
        }
    }

    // 먹이급여시간 사용자설정 버튼리스너
    public void userSettingFeedTimer(View v) {
        FeedUserSettingTimer feedUserSettingTimerDialog = new FeedUserSettingTimer(this);
        feedUserSettingTimerDialog.setFeedUserSettingTimerListener(new FeedUserSettingTimer.FeedUserSettingTimerListener() {
            @Override
            public void onOkClicked(int year, int month, int day, int hour, int minute) {
                Toast.makeText(FragActivity.this, "설정된 날짜: " + year + "/" + month + "/"  + day + "\n" + "설정된 시간: " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
                feedFrag.selectTimer(R.id.btn_feedtimer_userSetting);
                feedFrag.userSettingFeedButton.setText(hour + "H " + minute + "M");
            }
        });
        feedUserSettingTimerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == QUANTITY_OK && resultCode == RESULT_OK) {

        }
    }

    /* 먹이값 저장취소 */
    public void savefeedButton(View v) {
        socket.emit("insertFeed", timerValue, circleValue);
        socket.disconnect();
        Toast.makeText(this, "저장하였습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancelfeedButton(View v) {
        socket.disconnect();
        finish();
    }

    /* 온도 저장취소 */

    public void saveTemperPHButton(View v) {
        socket.emit("insertTemper", temperatureFragment.minTemper, temperatureFragment.maxTemper);
        socket.emit("insertPH", temperatureFragment.minPH, temperatureFragment.maxPH);
        socket.disconnect();
        Toast.makeText(this, "저장하였습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancelTemperPHButton(View v) {
        socket.disconnect();
        finish();
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