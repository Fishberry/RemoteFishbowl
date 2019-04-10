package com.example.shinj.navmain.Feed;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shinj.navmain.BaseActivity;
import com.example.shinj.navmain.IntentData;
import com.example.shinj.navmain.R;
import com.example.shinj.navmain.Streaming.StreamingActivity;

import io.socket.client.Socket;


public class FeedFragActivity extends BaseActivity implements View.OnClickListener, FeedFragPresenter.View{

    private Socket socket;
    String address;
    Button btn_feed_frag_now, btn_feed_frag_reserve;
    View view_feed_frag_now, view_feed_frag_reserve;
    FragmentManager fm;
    FragmentTransaction tran;
    FeedNowFragment feedNowFragment;
    FeedReserveFragment feedReserveFragment;
    IntentData intentData = IntentData.getInstance();
    int timerFeed = 0;
    int circleFeed = 0;
    FeedFragPresenterImpl feedFragPresenterimpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        socket = intentData.getSocket();

        btn_feed_frag_now =  findViewById(R.id.btn_feed_frag_now);
        btn_feed_frag_reserve = findViewById(R.id.btn_feed_frag_reserve);
        btn_feed_frag_now.setOnClickListener(this);
        btn_feed_frag_reserve.setOnClickListener(this);

        view_feed_frag_now = (View) findViewById(R.id.view_feed_frag_now);
        view_feed_frag_reserve = (View) findViewById(R.id.view_feed_frag_reserve);

        feedNowFragment = new FeedNowFragment();
        feedReserveFragment = new FeedReserveFragment();

        feedFragPresenterimpl = new FeedFragPresenterImpl();

        setFrag(0);

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
        feedFragPresenterimpl.startFeed(socket);
        Toast.makeText(getApplicationContext(), "먹이급여를 완료하였습니다.", Toast.LENGTH_SHORT).show();
    }

    /* FeedReserveFragment 기능 */
    public void buttonClick(View v) {
        //버튼의 아이디로 버튼 구분
        switch (v.getId()) {
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

    /* 먹이값 저장 */
    public void savefeedButton(View v) {
        feedFragPresenterimpl.saveFeed(socket, timerFeed, circleFeed);
        Toast.makeText(this, "저장하였습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, StreamingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /* 먹이값 초기화 */
    public void resetfeedButton(View view) {
        show();
    }
    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("먹이급여 초기화");
        builder.setMessage("초기화하면 먹이급여예약이 초기화됩니다. 진행하시겠습니까?");
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        timerFeed = -1;
                        circleFeed = -1;
                        Toast.makeText(getApplicationContext(), "먹이급여예약을 초기화하였습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), StreamingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        builder.show();
    }

    /* 먹이값 취소 */
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