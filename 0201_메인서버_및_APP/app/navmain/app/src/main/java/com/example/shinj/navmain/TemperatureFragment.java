package com.example.shinj.navmain;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class TemperatureFragment extends Fragment {

    Spinner minTemperSpinner, maxTemperSpinner, minPHSpinner, maxPHSpinner;
    int minTemper, maxTemper;
    double minPH, maxPH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temperature, container, false);

        final ArrayList<String> temperList = new ArrayList<>();
        final ArrayList<String> phList = new ArrayList<>();

        for(int i = 0; i <= 50; i++)
            temperList.add(String.valueOf(i));

        for(int i = 0; i <= 13; i++) {
            for(int j = 0; j <= 9; j++)
                phList.add(String.valueOf(i + "." + j));
        }

        minTemperSpinner = (Spinner) view.findViewById(R.id.tempRange1);
        maxTemperSpinner = (Spinner) view.findViewById(R.id.tempRange2);
        minPHSpinner = (Spinner) view.findViewById(R.id.pHRange1);
        maxPHSpinner = (Spinner) view.findViewById(R.id.pHRange2);

        ArrayAdapter temperAdapter = new ArrayAdapter(view.getContext(), R.layout.support_simple_spinner_dropdown_item, temperList);
        ArrayAdapter phAdapter = new ArrayAdapter(view.getContext(), R.layout.support_simple_spinner_dropdown_item, phList);

        minTemperSpinner.setAdapter(temperAdapter);
        maxTemperSpinner.setAdapter(temperAdapter);
        minPHSpinner.setAdapter(phAdapter);
        maxPHSpinner.setAdapter(phAdapter);

        minTemperSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minTemper = Integer.parseInt(temperList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        maxTemperSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                maxTemper = Integer.parseInt(temperList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        minPHSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minPH = Double.parseDouble(phList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        maxPHSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                maxPH = Double.parseDouble(phList.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
}