package com.example.shinj.navmain;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;

public class TemperatureActivity extends BaseActivity {

    private Socket socket;
    Spinner minTemperSpinner, maxTemperSpinner, minPHSpinner, maxPHSpinner;
    int minTemper, maxTemper;
    double minPH, maxPH;
    IntentData intentData = IntentData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        socket = intentData.getSocket();

        final ArrayList<String> temperList = new ArrayList<>();
        final ArrayList<String> phList = new ArrayList<>();

        for(int i = 0; i <= 50; i++)
            temperList.add(String.valueOf(i));

        for(int i = 0; i <= 13; i++) {
            for(int j = 0; j <= 9; j++)
                phList.add(String.valueOf(i + "." + j));
        }

        minTemperSpinner = (Spinner) findViewById(R.id.tempRange1);
        maxTemperSpinner = (Spinner) findViewById(R.id.tempRange2);
        minPHSpinner = (Spinner) findViewById(R.id.pHRange1);
        maxPHSpinner = (Spinner) findViewById(R.id.pHRange2);

        ArrayAdapter temperAdapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, temperList);
        ArrayAdapter phAdapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, phList);

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

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_temperature;
    }

    /* 온도 저장/취소 */
    public void saveTemperPHButton(View v) {
        socket.emit("insertTemper", minTemper, maxTemper);
        socket.emit("insertPH", minPH, maxPH);
        socket.disconnect();
        Toast.makeText(this, "저장하였습니다.", Toast.LENGTH_SHORT).show();
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
