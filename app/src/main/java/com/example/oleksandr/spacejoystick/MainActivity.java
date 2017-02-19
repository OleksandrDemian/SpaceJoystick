package com.example.oleksandr.spacejoystick;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.Vibrator;
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
    private PlayerData playerData;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerData = new PlayerData(getApplicationContext());
        playerData.loadPlayer();

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        requestPermission();
        startClient();

        ImageButton left = (ImageButton) findViewById(R.id.btnLeft);
        ImageButton right = (ImageButton) findViewById(R.id.btnRight);
        ImageButton shoot = (ImageButton) findViewById(R.id.btnShoot);
        final ImageButton engine = (ImageButton) findViewById(R.id.btnEngine);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.send(Command.TURNLEFT);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.send(Command.TURNRIGHT);
            }
        });

        shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.send(Command.FIRE);
            }
        });

        engine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.send(Command.ENGINETRIGGER);
            }
        });
    }

    private void requestPermission(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.VIBRATE}, 1);

    }

    private void startClient(){
        final ClientListener listener = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = getIntent().getExtras().getString("ip", "null");
                client = new Client(ip, 6546);
                client.setClientListener(listener);
                client.sendMessage("rn" + playerData.getName());

                //Dati order: health, damage, shield, speed, shipSkin
                String[] dati = { "50", "15", "3", "500", "2" };
                client.send(Request.SHIPINFO, dati);
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

    @Override
    public void onMessageSendSuccess() {
        vibrator.vibrate(50);
    }
}
