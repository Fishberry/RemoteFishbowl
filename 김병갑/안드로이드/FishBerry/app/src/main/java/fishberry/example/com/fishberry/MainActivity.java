package fishberry.example.com.fishberry;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView webView;            //웹뷰 객체
    WebSettings webSettings;    //웹뷰 세팅 객체

    private Button mainButton01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //웹뷰 객체 레이아웃 아이디와 매칭 및 설정
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webSettings = webView.getSettings();    //웹뷰의 세팅을 웹뷰세팅 객체에게 받는다.
        webSettings.setJavaScriptEnabled(true);     //자바스크립트 사용 가능

//        mainButton01 = (Button) findViewById(R.id.MainButton01);
//        mainButton01.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, TemperActivity.class);
//                startActivity(intent);
//            }
//        });

        //원하는 URL 됨.
        //webView.loadUrl("URL");
    }

    //버튼 클릭 시
    public void buttonClick(View v) {

        Intent intent;

        //버튼의 아이디로 버튼 구분
        switch (v.getId()) {
            case R.id.MainButton01:
                intent = new Intent(this, TemperActivity.class);
                startActivity(intent);
                break;
            case R.id.MainButton02:
                intent = new Intent(this, FoodActivity.class);
                startActivity(intent);
                break;
            case R.id.MainButton03:
                break;
            case R.id.MainButton04:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인화면으로")
                .setMessage("로그인 화면으로 돌아가시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
}
