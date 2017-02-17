package com.example.oleksandr.spacejoystick;

import android.content.Context;
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

public class ShipScreen extends Fragment {

    private View view;
    private PlayerData playerData;

    public ShipScreen() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ship_fragment, container, false);

        playerData = new PlayerData(getActivity().getApplicationContext());
        playerData.loadPlayer();

        final EditText txtName = (EditText)view.findViewById(R.id.txtName);
        txtName.setText(playerData.getName());

        Button save = (Button)view.findViewById(R.id.btnSavePlayer);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString();
                playerData.setName(name, true);

            }
        });

        return view;
    }

    public String getName(){
        TextView nameText = (TextView)view.findViewById(R.id.txtName);
        return nameText.getText().toString();
    }
}