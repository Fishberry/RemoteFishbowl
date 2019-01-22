package com.example.hanmi.test1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {
    private TextView tvMain;
    private EditText etMsg;
    private Button btnSubmit;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMain = findViewById(R.id.tvMain);
        etMsg = findViewById(R.id.etMsg);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener((view)->{
            socket.emit("reqMsg", etMsg.getText().toString());
            etMsg.setText("");
        });

        try {
            socket = IO.socket("http://192.168.0.104:3000");
            socket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
            }).on("serverMessage", (Object... objects) -> {
                runOnUiThread(()->{
                    tvMain.setText(objects[0].toString());
                });
            });
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}