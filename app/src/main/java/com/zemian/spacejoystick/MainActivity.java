package com.zemian.spacejoystick;

import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
    private TextView killsView;        //Shows player's health
    private TextView deathView;        //Shows player's shield

    private boolean engineB = false;    //Engine state
    private ActivityConnectionData activityConnectionData;

    //Commands
    private CommandButtonState leftCommand;
    private CommandButtonState rightCommand;
    private CommandButtonState fireCommand;
    private CommandButtonState abilityCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityConnectionData = ActivityConnectionData.getInstance(false);
        initializePlayer();

        leftCommand = new CommandButtonState(Converter.toChar(Command.TURNLEFT));
        rightCommand = new CommandButtonState(Converter.toChar(Command.TURNRIGHT));
        fireCommand = new CommandButtonState(Converter.toChar(Command.FIRE));
        abilityCommand = new CommandButtonState(Converter.toChar(Command.ABILITYTRIGGER));

        //Get buttons
        Button btnStart = (Button) findViewById(R.id.btn_startGame);
        Button btnPause = (Button) findViewById(R.id.btn_pause);
        ImageButton left = (ImageButton) findViewById(R.id.btnLeft);
        ImageButton right = (ImageButton) findViewById(R.id.btnRight);
        ImageButton shoot = (ImageButton) findViewById(R.id.btnShoot);
        ImageButton ability = (ImageButton) findViewById(R.id.btnAbility);
        final ImageButton engine = (ImageButton) findViewById(R.id.btnEngine);

        //Get views(health, shield)
        shieldView = (TextView) findViewById(R.id.joystickShield);
        healthView = (TextView) findViewById(R.id.joystickHealth);
        killsView = (TextView) findViewById(R.id.joystickKills);
        deathView = (TextView) findViewById(R.id.joystickDeath);

        //Set player's health and shield
        setTextToTextView(healthView, "Health: " + String.valueOf(playerData.getAttribute("Health").getValue()));
        setTextToTextView(shieldView, "Shield: " + String.valueOf(playerData.getAttribute("Shield").getValue()));

        //Hide joystick before the game started
        if (!activityConnectionData.gameStarted)
            hideJoystick();
        else
            showJoystick();

        setTextToTextView(killsView, "Kills: " + activityConnectionData.kills);
        setTextToTextView(deathView, "Death: " + activityConnectionData.death);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (client != null)
                    client.send(Command.STARTGAME);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (client != null)
                    client.send(Command.PAUSE);
            }
        });

        //Buttons initialization
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    leftCommand.press();
                if (event.getAction() == MotionEvent.ACTION_UP)
                    leftCommand.release();
                return false;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    rightCommand.press();
                if (event.getAction() == MotionEvent.ACTION_UP)
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
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    fireCommand.press();
                if (event.getAction() == MotionEvent.ACTION_UP)
                    fireCommand.release();
                return false;
            }
        });

        ability.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    abilityCommand.press();
                if (event.getAction() == MotionEvent.ACTION_UP)
                    abilityCommand.release();
                return false;
            }
        });
        initializeInputThread();
    }

    private void initializeInputThread() {
        //Input thread initialization
        inputThread = activityConnectionData.getInputThread();
        if (inputThread != null)
            inputThread.stopThread();

        if(client == null || !(client.getClientState() == ClientState.ISRUNNING))
            return;;

        inputThread = new InputThread();
        inputThread.start();
        activityConnectionData.setInputThread(inputThread);
    }

    /**
     * Used to get player's data and send them to game
     */
    private void initializePlayer() {
        //If client already exist, exit the function
        if (client != null)
            return;

        //Load player's data
        playerData = PlayerData.getInstance(getApplicationContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Client initialization
                client = Client.getInstance();
                client.setClientListener(MainActivity.this);

                //PlayerInitialization
                if (!activityConnectionData.gameStarted) {
                    client.sendMessage("m");
                    client.sendMessage(Converter.toChar(Command.NAME) + playerData.getName());
                    client.send(Command.SHIPINFO, playerData.getShipInfoArray());
                }
            }
        }).start();
    }

    /**
     * Used to set invisible joystick layout
     */
    private void hideJoystick() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout joystick = (RelativeLayout) findViewById(R.id.joystickLayout);
                RelativeLayout waitLayout = (RelativeLayout) findViewById(R.id.waitLayout);
                waitLayout.setVisibility(View.VISIBLE);
                joystick.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Used to set visible joystick layout
     */
    private void showJoystick() {
        activityConnectionData.gameStarted = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout joystick = (RelativeLayout) findViewById(R.id.joystickLayout);
                RelativeLayout waitLayout = (RelativeLayout) findViewById(R.id.waitLayout);
                setBackgroundColor();
                waitLayout.setVisibility(View.INVISIBLE);
                joystick.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * This method is triggered when there are new messages
     *
     * @param message - message received from connection
     */
    @Override
    public void onMessageReceived(final String message) {

        if (message.length() < 1)
            return;
        System.out.println("Message: " + message + ": " + Converter.toCommand(message.charAt(0)));
        switch (Converter.toCommand(message.charAt(0))) {
            //rs - start game
            case STARTGAME:
                showJoystick();
                break;
            case PAUSE:
                playerData.incresePoints();
                break;
            //Set background color here
            case COLOR:
                activityConnectionData.backgroundColor = Integer.parseInt("" + message.charAt(1));
                //TEMP
                showJoystick();
                break;
            case MATCHEND:
                toMainScreen();
                break;
            case SHIELD:
                setTextToTextView(shieldView, "Shield: " + message.substring(1));
                break;
            case HEALTH:
                setTextToTextView(healthView, "Health: " + message.substring(1));
                break;
            case KILL:
                activityConnectionData.kills ++;
                setTextToTextView(killsView, "Kills: " + activityConnectionData.kills);
                break;
            case DEATH:
                activityConnectionData.death ++;
                setTextToTextView(deathView, "Death: " + activityConnectionData.death);
                break;

            case ADDPOINT:
                playerData.incresePoints(true);
                break;
        }
    }

    /**
     * This method is used to set text to textview
     *
     * @param view
     * @param text
     */
    private void setTextToTextView(final TextView view, final String text) {
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
    private void toMainScreen() {
        if(inputThread != null)
            inputThread.stopThread();

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toMainScreen();
    }

    /**
     * Is triggered when the message is sent without errors
     */
    @Override
    public void onMessageSendSuccess() {
        //System.out.println("Inviato senza problemi");
    }

    /**
     * Is triggered when the connection state is changed
     *
     * @param event
     */
    @Override
    public void onConnectionEvent(ConnectionEvent event) {
        switch (event) {
            case CONNECTION_STOPED:
                client.stopClient();
                if(inputThread != null)
                    inputThread.stopThread();
                toMainScreen();
                break;
        }
    }

    /**
     * Sets the background color (color depends on player)
     */
    private void setBackgroundColor() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int index = activityConnectionData.backgroundColor;

                RelativeLayout background = (RelativeLayout) findViewById(R.id.activity_main);
                int color = getResources().getColor(R.color.background);
                switch (index) {
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
    public class InputThread extends Thread {

        //Frequency of sending data per second
        private int frequency = 10;
        private boolean stop = false;

        public void stopThread() {
            System.out.println(">>> Stop input thread");
            stop = true;
        }

        public void run() {
            long mill = System.currentTimeMillis();
            while (true) {
                try {
                    Thread.sleep(1000 / frequency);
                    if (stop)
                        return;

                    //System.out.println("Time: " + ((System.currentTimeMillis() - mill)));
                    //mill = System.currentTimeMillis();

                    String string = "";

                    if (leftCommand.mustBeSended())
                        string += leftCommand.getCommandChar();

                    if (rightCommand.mustBeSended())
                        string += rightCommand.getCommandChar();

                    if (fireCommand.mustBeSended())
                        string += fireCommand.getCommandChar();

                    if (abilityCommand.mustBeSended())
                        string += abilityCommand.getCommandChar();

                    if (engineB) {
                        string += Converter.toChar(Command.ENGINETRIGGER);
                        engineB = false;
                    }
                    client.sendMessage(Converter.toChar(Command.COMMANDSSTRING) + string);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
