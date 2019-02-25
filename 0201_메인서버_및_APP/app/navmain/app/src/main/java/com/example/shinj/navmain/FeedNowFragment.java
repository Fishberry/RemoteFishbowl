package com.example.shinj.navmain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FeedNowFragment extends Fragment {

    //Button btnStartFeedNow;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_now, container, false);

        //btnStartFeedNow = view.findViewById(R.id.btn_start_feed_now);

        return view;
    }
}
