package com.example.oleksandr.spacejoystick;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ClientListener {

    private Client client;
    private PlayerData playerData;
    //private Vibrator vibrator;
    private InputThread inputThread;
    //private Timer abilityTimer;
    //private Timer fireTimer;

    /*private boolean fireB = false;
    private boolean leftB = false;
    private boolean rightB = false;*/

    private boolean engineB = false;
    //<------------ARRAY???------------------>//
    private CommandButtonState leftCommand = new CommandButtonState(Converter.toChar(Command.TURNLEFT));
    private CommandButtonState rightCommand = new CommandButtonState(Converter.toChar(Command.TURNRIGHT));
    private CommandButtonState fireCommand = new CommandButtonState(Converter.toChar(Command.FIRE));
    private CommandButtonState abilityCommand = new CommandButtonState(Converter.toChar(Command.ABILITYTRIGGER));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerData = new PlayerData(getApplicationContext());
        playerData.loadPlayer();

        //abilityTimer = new Timer(playerData.getAbilityCoolDown(), true);
        //fireTimer = new Timer(playerData.getFireCoolDown(), true);

        //vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        requestPermission();
        startClient();

        ImageButton left = (ImageButton) findViewById(R.id.btnLeft);
        ImageButton right = (ImageButton) findViewById(R.id.btnRight);
        ImageButton shoot = (ImageButton) findViewById(R.id.btnShoot);
        ImageButton ability = (ImageButton) findViewById(R.id.btnAbility);

        final ImageButton engine = (ImageButton) findViewById(R.id.btnEngine);

        /*left.setOnClickListener(new View.OnClickListener() {
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
        });*/

        /*engine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.send(Command.ENGINETRIGGER);
            }
        });

        shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fireTimer.passed())
                    client.send(Command.FIRE);
            }
        });

        ability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Delta: " + abilityTimer.deltaTime());
                if(abilityTimer.passed())
                    client.send(Command.ABILITYTRIGGER);
            }
        });*/

        //<-----------------NEW INPUT------------------------------->
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    leftCommand.press();
                if(event.getAction() == MotionEvent.ACTION_UP)
                    leftCommand.release();
                return false;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    rightCommand.press();
                if(event.getAction() == MotionEvent.ACTION_UP)
                    rightCommand.release();
                return false;
            }
        });

        engine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engineB = true;
            }
        });

        shoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    fireCommand.press();
                if(event.getAction() == MotionEvent.ACTION_UP)
                    fireCommand.release();
                return false;
            }
        });

        ability.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    abilityCommand.press();
                if(event.getAction() == MotionEvent.ACTION_UP)
                    abilityCommand.release();
                return false;
            }
        });

        if(inputThread == null) {
            inputThread = new InputThread();
            inputThread.start();
        }
        //<----------------FINE---------------------------------->
    }

    private void requestPermission(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.VIBRATE}, 1);

    }

    private void startClient(){
        if(client != null)
            return;

        final ClientListener listener = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = getIntent().getExtras().getString("ip", "null");
                client = new Client(ip, 6546);
                client.setClientListener(listener);
                client.sendMessage("rn" + playerData.getName());

                //Dati order: health, damage, shield, speed, shipSkin, abilityType, abilityLevel
                //String[] dati = { "50", "15", "3", "500", "1", "3", "2" };
                client.send(Request.SHIPINFO, playerData.getShipInfoArray());
            }
        });
        thread.start();
    }

    @Override
    public void onMessageReceived(final String message) {

        System.out.println("Receive activity: " + message);
        //NOT TESTED
        if(message.charAt(0) == 'r') {
            switch (message.charAt(1)) {
                //rs - start game
                case 's':
                    break;
                case 'p':
                    playerData.incresePoints();
                    break;
                //Set background color here
                case 'c':
                    setBackgroundColor(Integer.parseInt("" + message.charAt(2)));
                    break;
                case 'm':
                    client.interrupt();
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onMessageSendSuccess() {
        //vibrator.vibrate(25);
    }

    private void setBackgroundColor(final int index){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout background = (RelativeLayout)findViewById(R.id.activity_main);
                int color = Converter.toColor(index);
                if(color == -1)
                    color = getResources().getColor(R.color.background);
                background.setBackgroundColor(color);
            }
        });
    }

    private class InputThread extends Thread {

        private int frequency = 5;

        public void run(){
            while(true){
                try {
                    Thread.sleep(1000/frequency);
                    String string = "c";

                    if(leftCommand.mustBeSended())
                        string += leftCommand.getCommandChar();

                    if(rightCommand.mustBeSended())
                        string += rightCommand.getCommandChar();

                    if(fireCommand.mustBeSended())
                        string += fireCommand.getCommandChar();

                    if(abilityCommand.mustBeSended())
                        string += abilityCommand.getCommandChar();

                    if(engineB) {
                        string += "e";
                        engineB = false;
                    }
                    /*if(fireB)
                        string += "s";
                        //addCommand(Command.FIRE);
                    if(engineB) {
                        string += "e";
                        engineB = false;
                    }
                        //addCommand(Command.ENGINETRIGGER);
                    if(leftB)
                        string += "l";
                    if(rightB)
                        string += "r";
                    if(abilityB)
                        string += "a";*/
                    /*for(int i = 0; i < commands.size(); i++) {
                        System.out.println("Pressed: " + commands.toString());
                        string += Converter.toString(commands.get(i)).charAt(1);
                    }*/

                    client.sendMessage(string);
                    //commands.clear();
                    /*fireB = false;
                    engineB = false;
                    rightB = false;
                    leftB = false;
                    abilityB = false;*/
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /*public void addCommand(Command command){
            commands.add(command);
        }*/

    }
}
