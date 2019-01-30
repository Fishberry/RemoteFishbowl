package fishberry.example.com.fishberry;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {

    WebView webView;            //웹뷰 객체
    WebSettings webSettings;    //웹뷰 세팅 객체
    private Socket socket;
    private TextView tempValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String RaspIP = String.valueOf(R.string.RaspIP);

        tempValue = (TextView) findViewById(R.id.TempValue);

        try {
            socket = IO.socket("http://175.204.79.66:3000");
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {
                    try {
                        socket.emit("reqMsg", "어플에서 온도값 받아갑니다");
                        socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
                        }).on("serverMsg", (Object... objects) -> {
                            runOnUiThread(()->{
                                tempValue.setText(objects[0].toString());
                            });
                        });
                        Thread.sleep(5000);
                    } catch (Exception e) {

                    }
                }
            }
        });
        t.start();

        //웹뷰 객체 레이아웃 아이디와 매칭 및 설정
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webSettings = webView.getSettings();    //웹뷰의 세팅을 웹뷰세팅 객체에게 받는다.
        webSettings.setJavaScriptEnabled(true);     //자바스크립트 사용 가능

        //원하는 URL 됨.
        webView.loadUrl("http://175.204.79.66:8080/?action=stream");
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
                        socket.disconnect();
                        finish();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        socket.connect();
    }
}
