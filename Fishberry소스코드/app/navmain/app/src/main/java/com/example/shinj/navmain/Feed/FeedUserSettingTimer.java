package com.example.shinj.navmain.Feed;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.shinj.navmain.R;

import java.util.Calendar;


public class FeedUserSettingTimer extends Dialog implements TimePicker.OnTimeChangedListener, View.OnClickListener{
    private static final int LAYOUT = R.layout.feed_user_setting_timer;

    private Context context;
    private TextView feedUserSettingCancel;
    private TextView feedUserSettingOk;
    TimePicker mTimePicker;
    Calendar mCalendar;
    public int hour, minute;
    public FeedUserSettingTimer(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    // 커스텀다이얼로그에서 사용자가 설정한 날짜, 시간정보를 프래그먼트 액티비티로 넘겨주기 위해 설정하는 인터페이스와 리스너
    public interface FeedUserSettingTimerListener{
        public void onOkClicked(int hour, int minute);
    }
    private FeedUserSettingTimerListener feedUserSettingTimerListener;

    public void setFeedUserSettingTimerListener(FeedUserSettingTimerListener feedUserSettingTimerListener){
        this.feedUserSettingTimerListener = feedUserSettingTimerListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mCalendar = Calendar.getInstance();
        mTimePicker = (TimePicker) findViewById(R.id.time_picker);
        feedUserSettingCancel= (TextView) findViewById(R.id.feedUserSettingCancel);
        feedUserSettingOk = (TextView) findViewById(R.id.feedUserSettingOk);
        feedUserSettingCancel.setOnClickListener(this);
        feedUserSettingOk.setOnClickListener(this);
        mTimePicker.setOnTimeChangedListener(this);
        mTimePicker.setIs24HourView(true);  //오후 오전 없애기(false로 하던가, 설정해주지 않으면 디폴트로 false가 되어서 오후 오전이 나온다)
        mTimePicker.setHour(0);             //타임피커 작동 시, 초기화된 시간
        mTimePicker.setMinute(0);           //타임피커 작동 시, 초기화된 분
    }

    // TimePicker에서 시간이 변경될때마다 호출되는 콜백메소드 - 시간값을 프래그먼트 액티비티에 넘겨주려고 변수에 저장만 함
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minuteOfDay){
        hour = hourOfDay;
        minute = minuteOfDay;
    }

    // 다이얼로그 취소버튼이랑 완료버튼 리스너
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.feedUserSettingCancel:
                cancel();
                break;
            case R.id.feedUserSettingOk:
                feedUserSettingTimerListener.onOkClicked(hour, minute);
                dismiss();
                break;
        }
    }
}