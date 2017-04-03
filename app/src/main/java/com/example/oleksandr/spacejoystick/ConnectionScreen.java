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

    private View view;
    private Client client;

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

        final EditText ipText = (EditText)view.findViewById(R.id.ip);
        ipText.setText(getLastIP());
        ipText.setSelected(false);

        client = new Client();

        Button connect = (Button)view.findViewById(R.id.btnConnect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ipText.getText().toString();
                saveIP(ip);
                startClient(ip, Client.PORT);
                /*Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                intent.putExtra("ip", ip);
                startActivity(intent);*/
            }
        });
    }

    private void startClient(final String ip, final int port){
        Thread connect = new Thread(new Runnable() {
            @Override
            public void run() {
                client.setClientListener(ConnectionScreen.this);
                client.connect(ip, port);
                /*client.sendMessage("rn" + playerData.getName());

                //Dati order: health, damage, shield, speed, shipSkin, abilityType, abilityLevel
                //String[] dati = { "50", "15", "3", "500", "1", "3", "2" };
                client.send(Request.SHIPINFO, playerData.getShipInfoArray());*/
            }
        });
        Toast.makeText(getContext(), "Connecting to: " + ip, Toast.LENGTH_SHORT).show();
        connect.start();
    }

    private String getLastIP(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("player", Context.MODE_PRIVATE);
        return sharedPreferences.getString("lastIP", "");
    }

    private void saveIP(String ip){
        SharedPreferences.Editor spEditor = getContext().getSharedPreferences("player", Context.MODE_PRIVATE).edit();
        spEditor.putString("lastIP", ip);
        spEditor.commit();
    }

    @Override
    public void onMessageReceived(String message) {
        return;
    }

    @Override
    public void onMessageSendSuccess() {
        return;
    }

    @Override
    public void onConnectionEvent(ConnectionEvent event) {
        System.out.println("Event: " + event.toString());
        switch (event){
            case CONNECTED:
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
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