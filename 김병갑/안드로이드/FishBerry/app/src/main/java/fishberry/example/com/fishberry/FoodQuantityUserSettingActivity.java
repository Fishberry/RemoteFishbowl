package fishberry.example.com.fishberry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FoodQuantityUserSettingActivity extends AppCompatActivity {

    private Spinner foodQuantitySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_quantity_user_setting);

        foodQuantitySpinner = (Spinner) findViewById(R.id.foodTimerSpinner);

        final ArrayList<String> timeArrayList = new ArrayList<>();

        for(int i = 1; i <= 24; i++)
            timeArrayList.add(String.format("%d", i));

        ArrayAdapter foodAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, timeArrayList);
        foodQuantitySpinner.setAdapter(foodAdapter);

        foodQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void saveQuantityButton(View v) {
        Toast.makeText(this, "설정하였습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancelQuantityButton(View v) {
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
