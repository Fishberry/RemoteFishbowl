package com.example.shinj.navmain.Feed;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shinj.navmain.R;

public class FeedReserveFragment extends Fragment {

    TextView eightHourButton, twelveHourButton, twentyfourHourButton, userSettingFeedButton;
    TextView oneCircleButton, twoCircleButton, threeCircleButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_reserve, container, false);
        eightHourButton = view.findViewById(R.id.btn_feedtimer_8h);
        twelveHourButton = view.findViewById(R.id.btn_feedtimer_12h);
        twentyfourHourButton = view.findViewById(R.id.btn_feedtimer_24h);
        userSettingFeedButton = view.findViewById(R.id.btn_feedtimer_userSetting);
        oneCircleButton = view.findViewById(R.id.feed1);
        twoCircleButton = view.findViewById(R.id.feed2);
        threeCircleButton = view.findViewById(R.id.feed3);

        return view;
    }

    public void selectTimer(int id) {

        switch(id) {
            case R.id.btn_feedtimer_8h:
                eightHourButton.setTextColor(Color.parseColor("#303F9F"));
                twelveHourButton.setTextColor(Color.GRAY);
                twentyfourHourButton.setTextColor(Color.GRAY);
                userSettingFeedButton.setTextColor(Color.GRAY);
                eightHourButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                twelveHourButton.setTypeface(Typeface.DEFAULT);
                twentyfourHourButton.setTypeface(Typeface.DEFAULT);
                userSettingFeedButton.setTypeface(Typeface.DEFAULT);
                userSettingFeedButton.setText("사용자 설정");
                break;
            case R.id.btn_feedtimer_12h:
                eightHourButton.setTextColor(Color.GRAY);
                twelveHourButton.setTextColor(Color.parseColor("#303F9F"));
                twentyfourHourButton.setTextColor(Color.GRAY);
                userSettingFeedButton.setTextColor(Color.GRAY);
                eightHourButton.setTypeface(Typeface.DEFAULT);
                twelveHourButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                twentyfourHourButton.setTypeface(Typeface.DEFAULT);
                userSettingFeedButton.setTypeface(Typeface.DEFAULT);
                userSettingFeedButton.setText("사용자 설정");
                break;
            case R.id.btn_feedtimer_24h:
                eightHourButton.setTextColor(Color.GRAY);
                twelveHourButton.setTextColor(Color.GRAY);
                twentyfourHourButton.setTextColor(Color.parseColor("#303F9F"));
                userSettingFeedButton.setTextColor(Color.GRAY);
                eightHourButton.setTypeface(Typeface.DEFAULT);
                twelveHourButton.setTypeface(Typeface.DEFAULT);
                twentyfourHourButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                userSettingFeedButton.setTypeface(Typeface.DEFAULT);
                userSettingFeedButton.setText("사용자 설정");
                break;
            case R.id.btn_feedtimer_userSetting:
                eightHourButton.setTextColor(Color.GRAY);
                twelveHourButton.setTextColor(Color.GRAY);
                twentyfourHourButton.setTextColor(Color.GRAY);
                userSettingFeedButton.setTextColor(Color.parseColor("#303F9F"));
                eightHourButton.setTypeface(Typeface.DEFAULT);
                twelveHourButton.setTypeface(Typeface.DEFAULT);
                twentyfourHourButton.setTypeface(Typeface.DEFAULT);
                userSettingFeedButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                break;
        }
    }

    public void selectCircle(int id) {

        switch(id) {
            case R.id.feed1:
                oneCircleButton.setTextColor(Color.parseColor("#303F9F"));
                twoCircleButton.setTextColor(Color.GRAY);
                threeCircleButton.setTextColor(Color.GRAY);
                oneCircleButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                twoCircleButton.setTypeface(Typeface.DEFAULT);
                threeCircleButton.setTypeface(Typeface.DEFAULT);
                break;
            case R.id.feed2:
                oneCircleButton.setTextColor(Color.GRAY);
                twoCircleButton.setTextColor(Color.parseColor("#303F9F"));
                threeCircleButton.setTextColor(Color.GRAY);
                oneCircleButton.setTypeface(Typeface.DEFAULT);
                twoCircleButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                threeCircleButton.setTypeface(Typeface.DEFAULT);
                break;
            case R.id.feed3:
                oneCircleButton.setTextColor(Color.GRAY);
                twoCircleButton.setTextColor(Color.GRAY);
                threeCircleButton.setTextColor(Color.parseColor("#303F9F"));
                oneCircleButton.setTypeface(Typeface.DEFAULT);
                twoCircleButton.setTypeface(Typeface.DEFAULT);
                threeCircleButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                break;
        }
    }
}
