package com.example.shinj.navmain;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shinj.navmain.DB.DBHelper;
import com.example.shinj.navmain.Feed.FeedFragActivity;
import com.example.shinj.navmain.Chart.ChartActivity;
import com.example.shinj.navmain.InfoDevice.InfoDeviceActivity;
import com.example.shinj.navmain.Login.LoginActivity;
import com.example.shinj.navmain.NotificationSetting.NotificationSettingActivity;
import com.example.shinj.navmain.Streaming.StreamingActivity;
import com.example.shinj.navmain.Temperature.TemperatureActivity;
import com.example.shinj.navmain.UserSetting.SettingActivity;
import com.example.shinj.navmain.Water.WaterFragActivity;

import io.socket.client.Socket;

abstract public class BaseActivity extends AppCompatActivity {
    public DrawerLayout mDrawerLayout;
    public Toolbar toolbar;
    public ActionBar actionBar;
    Socket socket;
    String address;
    IntentData intentData = IntentData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        Intent extraIntent = getIntent();

        address = intentData.getAddress();
        socket = intentData.getSocket();

        configureToolbar();
    }

    protected abstract int getLayoutResource();

    private void configureToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            /* 네비게이션바의 메뉴를 클릭했을 때 동작을 지정하는 메소드임! 질문은 제우에게 */
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                Intent intent;
                int id = menuItem.getItemId();
                switch (id) {
                    //  네비게이션바 - 내 어항
                    case R.id.navigation_item_streaming:
                        intent = new Intent(getApplicationContext(), StreamingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    // 네비게이션바 - 먹이급여
                    case R.id.navigation_item_feed:
                        intent = new Intent(getApplicationContext(), FeedFragActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    // 네비게이션바 - 환수
                    case R.id.navigation_item_water:
                        intent = new Intent(getApplicationContext(), WaterFragActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    // 네비게이션바 - 온도/pH
                    case R.id.navigation_item_temperPh:
                        intent = new Intent(getApplicationContext(), TemperatureActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    // 네비게이션바 - 차트
                    case R.id.navigation_item_chart:
                        intent = new Intent(getApplicationContext(), ChartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    //네비게이션바 - 알림설정
                    case R.id.nav_sub_menu_item01:
                        intent = new Intent(getApplicationContext(), NotificationSettingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    //네비게이션바 - 기기 상태
                    case R.id.nav_sub_menu_item02:
                        intent = new Intent(getApplicationContext(), InfoDeviceActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

//                    //네비게이션바 - 로그아웃
//                    case R.id.nav_sub_menu_item03:
//                        final DBHelper dbHelper = new DBHelper(getApplicationContext());
//                        dbHelper.delete();
//                        intent = new Intent(getApplicationContext(), LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        break;

                    //네비게이션바 - 사용자 설정
                    case R.id.nav_sub_menu_item04:
                        intent = new Intent(getApplicationContext(), SettingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;

            /*// 우측 상단위 옵션메뉴
            case R.id.usingGuide:
                intent = new Intent(this, ManualActivity.class);
                startActivity(intent);
                break;
            case R.id.userSetting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.Inquire:
                Toast.makeText(this, "문의 : https://github.com/Fishberry/RemoteFishbowl", Toast.LENGTH_LONG).show();
                break;
             */
        }

        return super.onOptionsItemSelected(item);
    }

}
