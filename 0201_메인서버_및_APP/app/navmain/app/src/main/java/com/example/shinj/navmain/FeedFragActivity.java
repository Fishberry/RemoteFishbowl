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


public class FeedFragActivity extends BaseActivity implements View.OnClickListener{

    private Socket socket;
    String address;
    Button btn_feed_frag_now, btn_feed_frag_reserve, feedSettingDone;
    View view_feed_frag_now, view_feed_frag_reserve;
    FragmentManager fm;
    FragmentTransaction tran;
    FeedNowFragment feedNowFragment;
    FeedReserveFragment feedReserveFragment;
    IntentData intentData = IntentData.getInstance();
    int timerFeed = 0;
    int circleFeed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        address = intentData.getAddress();
        socket = intentData.getSocket();

        btn_feed_frag_now = (Button) findViewById(R.id.btn_feed_frag_now);
        btn_feed_frag_reserve = (Button) findViewById(R.id.btn_feed_frag_reserve);
        btn_feed_frag_now.setOnClickListener(this);
        btn_feed_frag_reserve.setOnClickListener(this);

        view_feed_frag_now = (View) findViewById(R.id.view_feed_frag_now);
        view_feed_frag_reserve = (View) findViewById(R.id.view_feed_frag_reserve);

        feedNowFragment = new FeedNowFragment();
        feedReserveFragment = new FeedReserveFragment();

        setFrag(0);

//        feedSettingDone = (Button) findViewById(R.id.feedSettingDone);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_feed_frag;
    }

    //프레그먼트 클릭
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_feed_frag_now:
                setFrag(0);
                view_feed_frag_now.setBackgroundResource(R.color.colorBlack);
                view_feed_frag_reserve.setBackgroundResource(R.color.white);
                btn_feed_frag_now.setTextColor(Color.BLACK);
                btn_feed_frag_reserve.setTextColor(Color.GRAY);
                break;
            case R.id.btn_feed_frag_reserve:
                setFrag(1);
                view_feed_frag_reserve.setBackgroundResource(R.color.colorBlack);
                view_feed_frag_now.setBackgroundResource(R.color.white);
                btn_feed_frag_reserve.setTextColor(Color.BLACK);
                btn_feed_frag_now.setTextColor(Color.GRAY);
                break;
        }
    }

    public void setFrag(int n){    //프래그먼트를 교체하는 작업을 하는 메소드
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n){
            case 0:
                tran.replace(R.id.frag_frame, feedNowFragment);
                tran.commit();
                break;
            case 1:
                tran.replace(R.id.frag_frame, feedReserveFragment);
                tran.commit();
                break;
        }
    }

    public void onStartFeedNowButton(View v) {
        Toast.makeText(getApplicationContext(), "먹이급여를 완료하였습니다.", Toast.LENGTH_SHORT).show();
        socket.emit("reqFeedNow", "StartServo1");
    }

    /* FeedReserveFragment 기능 */
    public void buttonClick(View v) {
        //버튼의 아이디로 버튼 구분
        switch (v.getId()) {
//            case R.id.feedSettingDone:
//                socket.emit("reqData", "StartServo");
//                break;
            case R.id.btn_feedtimer_8h:
                feedReserveFragment.selectTimer(R.id.btn_feedtimer_8h);
                timerFeed = 8 * 60 * 60;
                break;
            case R.id.btn_feedtimer_12h:
                feedReserveFragment.selectTimer(R.id.btn_feedtimer_12h);
                timerFeed = 12 * 60 * 60;
                break;
            case R.id.btn_feedtimer_24h:
                feedReserveFragment.selectTimer(R.id.btn_feedtimer_24h);
                timerFeed = 24 * 60 * 60;
                break;
            case R.id.feed1:
                feedReserveFragment.selectCircle(R.id.feed1);
                circleFeed = 1;
                break;
            case R.id.feed2:
                feedReserveFragment.selectCircle(R.id.feed2);
                circleFeed = 2;
                break;
            case R.id.feed3:
                feedReserveFragment.selectCircle(R.id.feed3);
                circleFeed = 3;
                break;
        }
    }

    // 먹이급여시간 사용자설정 버튼리스너
    public void userSettingFeedTimer(View v) {
        FeedUserSettingTimer feedUserSettingTimerDialog = new FeedUserSettingTimer(this);
        feedUserSettingTimerDialog.setFeedUserSettingTimerListener(new FeedUserSettingTimer.FeedUserSettingTimerListener() {
            @Override
            public void onOkClicked(int hour, int minute) {
                //Toast.makeText(FeedFragActivity.this, "설정된 날짜: " + year + "/" + month + "/"  + day + "\n" + "설정된 시간: " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
                timerFeed = (hour*3600) + (minute*60);
                feedReserveFragment.selectTimer(R.id.btn_feedtimer_userSetting);
                feedReserveFragment.userSettingFeedButton.setText(hour + "H " + minute + "M");
            }
        });
        feedUserSettingTimerDialog.show();
    }

    /* 먹이값 저장취소 */
    public void savefeedButton(View v) {
        socket.emit("insertFeed", timerFeed, circleFeed);
        Toast.makeText(this, "저장하였습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, StreamingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void cancelfeedButton(View v) {
        finish();
    }

    public void cancelTemperPHButton(View v) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}