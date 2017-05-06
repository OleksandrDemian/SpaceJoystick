package com.example.oleksandr.spacejoystick;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Oleksandr on 14/02/2017.
 */

public class ConnectionScreen extends Fragment implements ClientListener {

    private View view;      //Fragment's view
    private Client client;  //Connection

    /**
     * Empty constructor required for fragments
     */
    public ConnectionScreen() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final EditText ipText = (EditText)view.findViewById(R.id.ip);   //Text where you type the game ip
        ipText.setText(getLastIP());                                    //Set the last inserted ip

        client = new Client();                                          //Initialize client

        Button connect = (Button)view.findViewById(R.id.btnConnect);    //Start connection to the server when pressed
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ipText.getText().toString();                //Get the ip
                saveIP(ip);                                             //Save the ip as last ip
                startClient(ip, Client.PORT);                           //Connection to the server
            }
        });
    }

    /**
     * Starts connection thread
     * @param ip -> server ip
     * @param port -> server port
     */
    private void startClient(final String ip, final int port){
        Thread connect = new Thread(new Runnable() {
            @Override
            public void run() {
                client.setClientListener(ConnectionScreen.this);
                client.connect(ip, port);
            }
        });
        Toast.makeText(getContext(), "Connecting to: " + ip, Toast.LENGTH_SHORT).show();
        connect.start();
    }

    /**
     * @return last saved ip
     */
    private String getLastIP(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("player", Context.MODE_PRIVATE);
        return sharedPreferences.getString("lastIP", "");
    }

    /**
     * Save ip to shared preferences
     * @param ip that must be saved
     */
    private void saveIP(String ip){
        SharedPreferences.Editor spEditor = getContext().getSharedPreferences("player", Context.MODE_PRIVATE).edit();
        spEditor.putString("lastIP", ip);
        spEditor.commit();
    }

    /**
     * Unused but required by ClientListener
     * @param message
     */
    @Override
    public void onMessageReceived(String message) {
        return;
    }

    /**
     * Unused but required by ClientListener
     */
    @Override
    public void onMessageSendSuccess() {
        return;
    }

    /**
     * Is triggered each time connection change it's state
     * @param event
     */
    @Override
    public void onConnectionEvent(ConnectionEvent event) {
        System.out.println("(Connection screen)Event: " + event.toString());
        switch (event){
            //If connected, starts joystick activity
            case CONNECTED:
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            //If failed, shows error message
            case CONNECTION_FAILED:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Failed to connect", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}