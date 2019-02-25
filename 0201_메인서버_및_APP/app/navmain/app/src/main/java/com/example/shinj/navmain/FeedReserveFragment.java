package com.example.shinj.navmain;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FeedReserveFragment extends Fragment {

    Button eightHourButton, twelveHourButton, twentyfourHourButton, userSettingFeedButton;
    Button oneCircleButton, twoCircleButton, threeCircleButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_reserve, container, false);
        eightHourButton = (Button) view.findViewById(R.id.btn_feedtimer_8h);
        twelveHourButton = (Button) view.findViewById(R.id.btn_feedtimer_12h);
        twentyfourHourButton = (Button) view.findViewById(R.id.btn_feedtimer_24h);
        userSettingFeedButton = (Button) view.findViewById(R.id.btn_feedtimer_userSetting);
        oneCircleButton = (Button) view.findViewById(R.id.feed1);
        twoCircleButton = (Button) view.findViewById(R.id.feed2);
        threeCircleButton = (Button) view.findViewById(R.id.feed3);

        return view;
    }

    public void selectTimer(int id) {

        switch(id) {
            case R.id.btn_feedtimer_8h:
                eightHourButton.setBackgroundResource(R.color.colorMyYellow);
                twelveHourButton.setBackgroundResource(R.color.colorAccent);
                twentyfourHourButton.setBackgroundResource(R.color.colorAccent);
                userSettingFeedButton.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.btn_feedtimer_12h:
                eightHourButton.setBackgroundResource(R.color.colorAccent);
                twelveHourButton.setBackgroundResource(R.color.colorMyYellow);
                twentyfourHourButton.setBackgroundResource(R.color.colorAccent);
                userSettingFeedButton.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.btn_feedtimer_24h:
                eightHourButton.setBackgroundResource(R.color.colorAccent);
                twelveHourButton.setBackgroundResource(R.color.colorAccent);
                twentyfourHourButton.setBackgroundResource(R.color.colorMyYellow);
                userSettingFeedButton.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.btn_feedtimer_userSetting:
                eightHourButton.setBackgroundResource(R.color.colorAccent);
                twelveHourButton.setBackgroundResource(R.color.colorAccent);
                twentyfourHourButton.setBackgroundResource(R.color.colorAccent);
                userSettingFeedButton.setBackgroundResource(R.color.colorMyYellow);
                break;
        }
    }

    public void selectCircle(int id) {

        switch(id) {
            case R.id.feed1:
                oneCircleButton.setBackgroundResource(R.color.colorMyYellow);
                twoCircleButton.setBackgroundResource(R.color.colorAccent);
                threeCircleButton.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.feed2:
                oneCircleButton.setBackgroundResource(R.color.colorAccent);
                twoCircleButton.setBackgroundResource(R.color.colorMyYellow);
                threeCircleButton.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.feed3:
                oneCircleButton.setBackgroundResource(R.color.colorAccent);
                twoCircleButton.setBackgroundResource(R.color.colorAccent);
                threeCircleButton.setBackgroundResource(R.color.colorMyYellow);
                break;
        }
    }
}
