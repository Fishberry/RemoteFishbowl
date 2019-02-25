package com.example.shinj.navmain;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class WaterFragment extends Fragment {

    DatePicker datePickerWater;
    TimePicker timePickerWater;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water, container, false);

        datePickerWater = (DatePicker) view.findViewById(R.id.date_picker2);
        timePickerWater = (TimePicker) view.findViewById(R.id.time_picker2);

        return view;
    }
}
