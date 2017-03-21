package com.example.oleksandr.spacejoystick;

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
    private TextView pointsText;
    private PlayerData playerData;

    public ShipScreen() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ship_fragment, container, false);

        playerData = new PlayerData(getActivity().getApplicationContext());
        playerData.loadPlayer();

        final EditText txtName = (EditText)view.findViewById(R.id.txtName);
        txtName.setText(playerData.getName());

        pointsText = (TextView) view.findViewById(R.id.txtPoints);
        pointsText.setText("Points: " + playerData.getPoints());

        Button clear = (Button)view.findViewById(R.id.btnClear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerData.clearSavedData();
            }
        });

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.shipAttributesLayout);
        //LayoutInflater viewInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int attributesCount = playerData.getAttributesCount();
        for(int i = 0; i < attributesCount; i++){
            layout.addView(getAttributeView(playerData.getAttribute(i), inflater));
        }

        final EditText abilityID = (EditText) view.findViewById(R.id.txtAbilityID);
        final EditText shipSkinID = (EditText) view.findViewById(R.id.txtShipSkinID);

        Button save = (Button)view.findViewById(R.id.btnSavePlayer);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set players data here
                playerData.setName(txtName.getText().toString());
                int skin = Utils.toNum(shipSkinID.getText().toString());
                int ability = Utils.toNum(abilityID.getText().toString());
                playerData.setShipSkin(skin != -1 ? skin : 0);
                playerData.setAbility(ability == -1 ? 0 : ability);

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

    private View getAttributeView(final Attribute attribute, LayoutInflater inflater){
        View view = inflater.inflate(R.layout.attribute_layout, null);
        final TextView name = (TextView) view.findViewById(R.id.txtAttrName);
        final TextView value = (TextView) view.findViewById(R.id.txtAttrVal);
        Button levelUp = (Button) view.findViewById(R.id.btnAttrUp);
        name.setText(attribute.getName());
        value.setText(String.valueOf(attribute.getValue()));
        levelUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerData.getPoints() < 1)
                    return;
                attribute.levelUp();
                playerData.decresePoints();
                value.setText(String.valueOf(attribute.getValue()));
                pointsText.setText("Points: " + playerData.getPoints());
            }
        });
        return view;
    }
}