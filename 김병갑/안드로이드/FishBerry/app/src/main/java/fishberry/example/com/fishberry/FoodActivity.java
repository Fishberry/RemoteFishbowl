package fishberry.example.com.fishberry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FoodActivity extends AppCompatActivity {

    public static final int QUANTITY_OK = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
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
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
