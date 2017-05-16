package com.zemian.spacejoystick;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oleksandr.spacejoystick.R;

//Documented

/**
 * Joystic screen
 */
public class MainActivity extends AppCompatActivity implements ClientListener {

    private Client client;              //Connection to the game
    private PlayerData playerData;      //Player's data
    private InputThread inputThread;    //Thread that sends user's input

    private TextView healthView;        //Shows player's health
    private TextView shieldView;        //Shows player's shield

    private boolean engineB = false;    //Engine state

    //Commands
    private CommandButtonState leftCommand = new CommandButtonState(Converter.toChar(Command.TURNLEFT));
    private CommandButtonState rightCommand = new CommandButtonState(Converter.toChar(Command.TURNRIGHT));
    private CommandButtonState fireCommand = new CommandButtonState(Converter.toChar(Command.FIRE));
    private CommandButtonState abilityCommand = new CommandButtonState(Converter.toChar(Command.ABILITYTRIGGER));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializePlayer();

        //Get buttons
        ImageButton left = (ImageButton) findViewById(R.id.btnLeft);
        ImageButton right = (ImageButton) findViewById(R.id.btnRight);
        ImageButton shoot = (ImageButton) findViewById(R.id.btnShoot);
        ImageButton ability = (ImageButton) findViewById(R.id.btnAbility);
        final ImageButton engine = (ImageButton) findViewById(R.id.btnEngine);

        //Get views(health, shield)
        shieldView = (TextView) findViewById(R.id.joystickShields);
        healthView = (TextView) findViewById(R.id.joystickHealth);

        //Set player's health and shield
        setTextToTextView(healthView, "Health: " + String.valueOf(playerData.getAttribute("health").getValue()));
        setTextToTextView(shieldView, "Shield: " + String.valueOf(playerData.getAttribute("shield").getValue()));

        //Hide joystick before the game started
        hideJoystick();

        //Buttons initialization
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

        //Input thread initialization
        if(inputThread == null) {
            inputThread = new InputThread();
            inputThread.start();
        }
    }

    /**
     * Used to get player's data and send them to game
     */
    private void initializePlayer(){
        //If client already exist, exit the function
        if(client != null)
            return;

        //Load player's data
        playerData = new PlayerData(getApplicationContext());
        playerData.loadPlayer();

        new Thread(new Runnable(){
            @Override
            public void run() {
                //Client initialization
                client = Client.getInstance();
                client.setClientListener(MainActivity.this);

                //PlayerInitialization
                client.sendMessage("rn" + playerData.getName());
                client.send(Request.SHIPINFO, playerData.getShipInfoArray());
            }
        }).start();
    }

    /**
     * Used to set invisible joystick layout
     */
    private void hideJoystick(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout joystick = (RelativeLayout)findViewById(R.id.joystickLayout);
                RelativeLayout waitLayout = (RelativeLayout)findViewById(R.id.waitLayout);
                waitLayout.setVisibility(View.VISIBLE);
                joystick.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Used to set visible joystick layout
     */
    private void showJoystick(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout joystick = (RelativeLayout)findViewById(R.id.joystickLayout);
                RelativeLayout waitLayout = (RelativeLayout)findViewById(R.id.waitLayout);
                waitLayout.setVisibility(View.INVISIBLE);
                joystick.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * This method is triggered when there are new messages
     * @param message - message received from connection
     */
    @Override
    public void onMessageReceived(final String message) {

        if(message.length() < 1)
            return;

        //NOT TESTED
        //Requests
        if(message.charAt(0) == 'r') {
            switch (message.charAt(1)) {
                //rs - start game
                case 's':
                    showJoystick();
                    break;
                case 'p':
                    playerData.incresePoints();
                    break;
                //Set background color here
                case 'c':
                    setBackgroundColor(Integer.parseInt("" + message.charAt(2)));
                    //TEMP
                    showJoystick();
                    break;
                case 'm':
                    toMainScreen();
                    break;
            }
        }

        //Information
        if(message.charAt(0) == 'i') {
            switch (message.charAt(1)) {
                //rs - start game
                case 's':
                    setTextToTextView(shieldView, "Shield: " + message.substring(2));
                    break;
                case 'h':
                    setTextToTextView(healthView, "Health: " + message.substring(2));
                    break;
            }
        }
    }

    /**
     * This method is used to set text to textview
     * @param view
     * @param text
     */
    private void setTextToTextView(final TextView view, final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setText(text);
            }
        });
    }

    /**
     * Close the connection and open the main screen
     */
    private void toMainScreen(){
        client.interrupt();
        finish();
    }

    /**
     * Is triggered when the message is sent without errors
     */
    @Override
    public void onMessageSendSuccess() {
        System.out.println("Inviato senza problemi");
    }

    /**
     * Is triggered when the connection state is changed
     * @param event
     */
    @Override
    public void onConnectionEvent(ConnectionEvent event) {
        switch (event){
            case CONNECTION_STOPED:
                client.stopClient();
                toMainScreen();
                break;
        }
    }

    /**
     * Sets the background color (color depends on player)
     * @param index
     */
    private void setBackgroundColor(final int index){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout background = (RelativeLayout)findViewById(R.id.activity_main);
                int color = getResources().getColor(R.color.background);
                switch (index){
                    case 0:
                        color = getResources().getColor(R.color.bgBlue);
                        break;
                    case 1:
                        color = getResources().getColor(R.color.bgGreen);
                        break;
                    case 2:
                        color = getResources().getColor(R.color.bgRed);
                        break;
                    case 3:
                        color = getResources().getColor(R.color.bgOrange);
                        break;
                }
                background.setBackgroundColor(color);
            }
        });
    }

    /**
     * Thread that sends players's input to the server
     */
    private class InputThread extends Thread {

        //Frequency of sending data per second
        private int frequency = 10;

        public void run(){
            long mill = System.currentTimeMillis();
            while(true){
                try {
                    Thread.sleep(1000/frequency);

                    System.out.println("Time: " + ((System.currentTimeMillis() - mill)));
                    mill = System.currentTimeMillis();

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

                    client.sendMessage(string);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
