package com.example.testservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btnstart;
    private Button btnStop;
    private EditText editText;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            btnstart = findViewById(R.id.btnStart);
            btnStop = findViewById(R.id.btnStop);
            editText = findViewById(R.id.edtText);




        btnstart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start();
                }
            });
            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stop();
                }
            });

        }
        private void stop() {
           Intent intent = new Intent(this, MyService.class);
           stopService(intent);
        }

        private void start() {
        Song song = new Song("Courtesy Call","Sang đẹp Trai",R.drawable.baseline_music,R.raw.ct);
        Intent intent = new Intent(this,MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",song);
        intent.putExtras(bundle);
        startService(intent);

        }


}

