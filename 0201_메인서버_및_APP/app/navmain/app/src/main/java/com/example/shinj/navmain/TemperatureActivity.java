package com.example.shinj.navmain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.socket.client.Socket;

public class TemperatureActivity extends BaseActivity {

    private Socket socket;
    int minTemperValue, maxTemperValue;
    double minPHValue, maxPHValue;
    IntentData intentData = IntentData.getInstance();
    EditText minTemper, maxTemper, minPH, maxPH;
    public static StreamingActivity streamingActivity = new StreamingActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        minTemper = findViewById(R.id.edit_min_temp);
        maxTemper = findViewById(R.id.edit_max_temp);
        minPH = findViewById(R.id.edit_min_pH);
        maxPH = findViewById(R.id.edit_max_pH);


        minTemper.setText(streamingActivity.minTempValue);
        maxTemper.setText(streamingActivity.maxTempValue);
        minPH.setText(streamingActivity.minPHValue);
        maxPH.setText(streamingActivity.maxPHValue);

        socket = intentData.getSocket();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_temperature;
    }

    /* 온도, pH 설정값 저장 */
    public void saveTemperPHButton(View v) {
        minTemperValue = Integer.parseInt(minTemper.getText().toString());
        maxTemperValue = Integer.parseInt(maxTemper.getText().toString());
        minPHValue = Double.parseDouble(minPH.getText().toString());
        maxPHValue = Double.parseDouble(maxPH.getText().toString());
        socket.emit("insertTemper", minTemperValue, maxTemperValue);
        socket.emit("insertPH", minPHValue, maxPHValue);
        Intent intent = new Intent(this, StreamingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /* 온도, pH 설정값 초기화*/

    public void resetTemperPHButton(View view) {
        show();
    }

    void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경고범위 초기화");
        builder.setMessage("초기화하면 경고범위가 초기화됩니다. 진행하시겠습니까?");
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        minTemperValue = -1;
                        maxTemperValue = -1;
                        socket.emit("insertTemper", minTemperValue, maxTemperValue);
                        socket.emit("insertPH", minPHValue, maxPHValue);
                        Toast.makeText(getApplicationContext(), "경고범위를 초기화하였습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), StreamingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        builder.show();
    }

    /* 온도, pH 설정값 취소 */
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
