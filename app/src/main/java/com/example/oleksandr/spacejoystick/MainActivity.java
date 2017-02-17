package com.example.oleksandr.spacejoystick;

import android.Manifest;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ClientListener {

    private Client client;
    private boolean engineEnabled = false;
    private PlayerData playerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerData = new PlayerData(getApplicationContext());
        playerData.loadPlayer();

        requestPermission();
        startClient();

        ImageButton left = (ImageButton) findViewById(R.id.btnLeft);
        ImageButton right = (ImageButton) findViewById(R.id.btnRight);
        ImageButton shoot = (ImageButton) findViewById(R.id.btnShoot);
        final ImageButton engine = (ImageButton) findViewById(R.id.btnEngine);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.sendMessage("cl");
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.sendMessage("cr");
            }
        });

        shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.sendMessage("cs");
            }
        });

        engine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engineEnabled = !engineEnabled;
                client.sendMessage("ce" + (engineEnabled ? "1" : "0"));
            }
        });

        /*left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                client.sendMessage("l");
                return false;
            }
        });
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                client.sendMessage("r");
                return false;
            }
        });*/
    }

    private void requestPermission(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);

    }

    private void startClient(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = getIntent().getExtras().getString("ip", "null");
                client = new Client(ip, 6546);
                client.sendMessage("rn" + playerData.getName());
            }
        });
        thread.start();
    }

    @Override
    public void onMessageReceived(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            }
        });
    }
}
