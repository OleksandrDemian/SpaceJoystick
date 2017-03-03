package com.example.oleksandr.spacejoystick;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

        final TextView pointsText = (TextView) view.findViewById(R.id.txtPoints);
        pointsText.setText("Points: " + playerData.getPoints());

        Button clear = (Button)view.findViewById(R.id.btnClear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerData.clearSavedData();
            }
        });

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.shipAttributesLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        int index = 0;

        while (true){
            final Attribute attr = playerData.getAttribute(index);

            if(attr == null)
                break;

            index ++;

            final Button button = new Button(getActivity().getApplicationContext());
            button.setText(attr.toString());
            button.setTextColor(getResources().getColor(R.color.colorAccent));
            button.setBackgroundColor(getResources().getColor(R.color.internalElements));
            button.setLayoutParams(params);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(playerData.getPoints() < 1)
                        return;

                    attr.levelUp();
                    playerData.decresePoints();
                    pointsText.setText("Points: " + playerData.getPoints());

                    button.setText(attr.toString());
                }
            });

            layout.addView(button);
        }

        Button save = (Button)view.findViewById(R.id.btnSavePlayer);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set players data here
                playerData.setName(txtName.getText().toString());

                //save player
                playerData.savePlayer();
            }
        });

        return view;
    }

    public String getName(){
        TextView nameText = (TextView)view.findViewById(R.id.txtName);
        return nameText.getText().toString();
    }
}