package com.example.shinj.navmain;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.socket.client.IO;
import io.socket.client.Socket;

public class WaterNowFragment extends Fragment {

    static ProgressBar progressBarWater;
    static Button btnStartWaterNow, btnPauseWaterNow;
    static TextView progressRateWater;
    boolean waterFlag;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water_now, container, false);

        progressBarWater = view.findViewById(R.id.progressBarWater);
        progressRateWater = view.findViewById(R.id.progressRateWater);
        btnStartWaterNow = view.findViewById(R.id.btn_start_water_now);
        btnPauseWaterNow = view.findViewById(R.id.btn_pause_water_now);

        return view;
    }
}
