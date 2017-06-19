package com.zemian.spacejoystick;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oleksandr.spacejoystick.R;

import java.util.ArrayList;

/**
 * Created by Oleksandr on 14/02/2017.
 */

public class ShipScreen extends Fragment implements IPlayerListener {

    private View view;
    private EditText txtName;
    private TextView pointsText;
    private PlayerData playerData;
    private ArrayList<ShipSkin> skins;

    public ShipScreen() {}

    @Override
    public void onResume() {
        super.onResume();
        pointsText = (TextView) view.findViewById(R.id.txtPoints);
        pointsText.setText("Points: " + playerData.getPoints());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ship_fragment, container, false);

        playerData = PlayerData.getInstance(getActivity().getApplicationContext());
        playerData.setPlayerListener(this);

        txtName = (EditText)view.findViewById(R.id.txtName);
        txtName.setText(playerData.getName());

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                playerData.setName(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.shipAttributesLayout);
        //LayoutInflater viewInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layout.addView(getSkinChooserView(inflater), layout.getChildCount());
        layout.addView(getAbilityChooserView(inflater), layout.getChildCount());

        int attributesCount = playerData.getAttributesCount();
        for(int i = 0; i < attributesCount; i++){
            layout.addView(playerData.getAttribute(i).getAttributeView(inflater), layout.getChildCount());
        }

        //addAbilitySpinner();

        //final EditText abilityID = (EditText) view.findViewById(R.id.txtAbilityID);
        //final EditText shipSkinID = (EditText) view.findViewById(R.id.txtShipSkinID);

        return view;
    }

    public String getName(){
        TextView nameText = (TextView)view.findViewById(R.id.txtName);
        return nameText.getText().toString();
    }

    private View getAbilityChooserView(LayoutInflater inflater){
        View view = inflater.inflate(R.layout.ability_chooser, null);

        Spinner abilities = (Spinner) view.findViewById(R.id.spnAbility);

        ArrayAdapter<CharSequence> strings = ArrayAdapter.createFromResource(getContext(), R.array.abilities,
                R.layout.spinner_item);

        strings.setDropDownViewResource(R.layout.spinner_item);
        abilities.setAdapter(strings);
        abilities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                playerData.setAbility(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        abilities.setSelection(playerData.getAbility());

        return view;
    }

    private View getSkinChooserView(LayoutInflater inflater){
        View view = inflater.inflate(R.layout.ship_skin_choser, null);
        skins = new ArrayList<ShipSkin>();

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.ships_container);

        String[] names = getResources().getStringArray(R.array.ship_skins);

        for(int i = 0; i < 4; i++){
            ShipSkin skin = new ShipSkin(names[i], i);
            layout.addView(skin.getView(inflater));
            skins.add(skin);
        }

        TextView curSkin = (TextView) view.findViewById(R.id.ship_skin_name);
        curSkin.setText("Current skin: " + names[playerData.getShipSkin()]);

        skins.get(playerData.getShipSkin()).enableView(true);
        return view;
    }

    @Override
    public void onPointsValueChange(int newValue) {
        pointsText.setText("Points: " + newValue);
    }

    @Override
    public void onSkinChange(ShipSkin skin) {
        TextView curSkin = (TextView) view.findViewById(R.id.ship_skin_name);
        curSkin.setText("Current skin: " + skin.getName());
        skin.enableView(true);

        for(int i = 0; i < skins.size(); i++){
            if(skins.get(i) != skin && skins.get(i).isSelected())
                skins.get(i).enableView(false);
        }
    }
}