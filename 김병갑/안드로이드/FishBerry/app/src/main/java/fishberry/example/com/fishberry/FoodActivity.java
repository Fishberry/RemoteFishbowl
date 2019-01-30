package fishberry.example.com.fishberry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import io.socket.client.IO;
import io.socket.client.Socket;

public class FoodActivity extends AppCompatActivity {

    public static final int QUANTITY_OK = 1000;
    private Button foodSettingDone;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        foodSettingDone = (Button) findViewById(R.id.foodSettingDone);
        String RaspIP = String.valueOf(R.string.RaspIP);

        try {
            socket = IO.socket("http://175.204.79.66:3000");
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buttonClick(View v) {

        //버튼의 아이디로 버튼 구분
        switch (v.getId()) {
            case R.id.foodSettingDone:
                socket.emit("reqData", "2");
                break;
        }
    }

    public void userSettingFoodQuantity(View v) {
        Intent intent = new Intent(this, FoodQuantityUserSettingActivity.class);
        startActivityForResult(intent, QUANTITY_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == QUANTITY_OK && resultCode == RESULT_OK) {

        }
    }

    public void saveFoodButton(View v) {
        Toast.makeText(this, "저장하였습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancelFoodButton(View v) {
        socket.disconnect();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        socket.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        socket.disconnect();
    }
}
