package com.example.shinj.navmain.InfoDevice;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shinj.navmain.IntentData;
import com.example.shinj.navmain.R;

import io.socket.client.Socket;

public class InfoDeviceActivity extends AppCompatActivity implements InfoDevicePresenter.View {

    GridView gridView;
    TextView osVersionText, freeMemoryText, totalMemoryText, infoCPUText;
    InfoDevicePresenterImpl impl = new InfoDevicePresenterImpl(this);
    private Socket socket;
    IntentData intentData = IntentData.getInstance();

    private Integer[] mThumbnail = {
            R.drawable.ic_change_password,
            R.drawable.ic_power_off
    };

    private String[] mTitle = {
            "기기 재시작",
            "기기 종료"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_device);

        socket = intentData.getSocket();

        osVersionText = (TextView) findViewById(R.id.os_version_text);
        freeMemoryText = (TextView) findViewById(R.id.free_memory_text);
        totalMemoryText = (TextView) findViewById(R.id.total_memory_text);
        infoCPUText = (TextView) findViewById(R.id.cpu_info_text);

        gridView = (GridView) findViewById(R.id.info_device_gridview);
        Adapter adapter = new Adapter(this);
        gridView.setAdapter(adapter);

        impl.takeDeviceInfo(socket);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        impl.rebootDevice(socket);
                        break;
                    case 1:
                        impl.shutdownDevice(socket);
                        break;
                }
            }
        });
    }

    @Override
    public void showDeviceInfo(String osVersion, String freeMemory, String totalMemory, String infoCPU) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                osVersionText.setText(osVersion);
                freeMemoryText.setText(freeMemory);
                totalMemoryText.setText(totalMemory);
                infoCPUText.setText(infoCPU);
            }
        });
    }

    @Override
    public void infoResErrorToast() {
        Toast.makeText(this, "기기 정보를 가져오지 못 했습니다.\n연결 확인을 해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
    }

    class Adapter extends BaseAdapter {

        private final Activity context;

        public Adapter(Activity context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mThumbnail.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.gridview_setting_activity, null, true);
            ImageView imageView = (ImageView) item.findViewById(R.id.gridView_setting_image);
            TextView title = (TextView) item.findViewById(R.id.gridView_setting_text);

            title.setText(mTitle[i]);
            imageView.setImageResource(mThumbnail[i]);

            return item;
        }
    }
}
