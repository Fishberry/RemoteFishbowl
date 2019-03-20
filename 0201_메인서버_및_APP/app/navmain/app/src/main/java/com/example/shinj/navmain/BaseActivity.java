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
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    // 네비게이션바 - 먹이급여
                    case R.id.navigation_item_feed:
                        intent = new Intent(getApplicationContext(), FeedFragActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    // 네비게이션바 - 환수
                    case R.id.navigation_item_water:
                        intent = new Intent(getApplicationContext(), WaterFragActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                     // 네비게이션바 - 온도/pH
                    case R.id.navigation_item_temperPh:
                        intent = new Intent(getApplicationContext(), TemperatureActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
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

            /* 우측 상단위 옵션메뉴 */
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
        }

        return super.onOptionsItemSelected(item);
    }

}
