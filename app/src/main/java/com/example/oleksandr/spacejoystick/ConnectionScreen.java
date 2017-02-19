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

/**
 * Created by Oleksandr on 14/02/2017.
 */

public class ConnectionScreen extends Fragment {

    private View view;

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

        Button connect = (Button)view.findViewById(R.id.btnConnect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ipText.getText().toString();
                saveIP(ip);
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                intent.putExtra("ip", ip);
                startActivity(intent);
            }
        });
    }

    private String getLastIP(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("player", Context.MODE_PRIVATE);
        return sharedPreferences.getString("lastIP", "niente");
    }

    private void saveIP(String ip){
        SharedPreferences.Editor spEditor = getContext().getSharedPreferences("player", Context.MODE_PRIVATE).edit();
        spEditor.putString("lastIP", ip);
        spEditor.commit();
    }
}