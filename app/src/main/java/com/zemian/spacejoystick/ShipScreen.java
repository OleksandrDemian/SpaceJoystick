package com.zemian.spacejoystick;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * Created by Oleksandr on 14/02/2017.
 */

public class ShipScreen extends Fragment {

    private View view;
    private EditText txtName;
    private TextView pointsText;
    private PlayerData playerData;

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

        txtName = (EditText)view.findViewById(R.id.txtName);
        txtName.setText(playerData.getName());

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.shipAttributesLayout);
        //LayoutInflater viewInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layout.addView(getSkinChooserView(inflater), layout.getChildCount() - 1);
        layout.addView(getAbilityChooserView(inflater), layout.getChildCount() - 1);

        int attributesCount = playerData.getAttributesCount();
        for(int i = 0; i < attributesCount; i++){
            layout.addView(getAttributeView(playerData.getAttribute(i), inflater), layout.getChildCount() - 1);
        }

        //addAbilitySpinner();

        //final EditText abilityID = (EditText) view.findViewById(R.id.txtAbilityID);
        //final EditText shipSkinID = (EditText) view.findViewById(R.id.txtShipSkinID);

        Button save = (Button)view.findViewById(R.id.btnSavePlayer);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set players data here
                playerData.setName(txtName.getText().toString());
                playerData.savePlayer();
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
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
        Button levelDown = (Button) view.findViewById(R.id.btnAttrDown);
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

        levelDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attribute.getLevel() < 1)
                    return;

                attribute.levelDown();
                playerData.incresePoints();
                value.setText(String.valueOf(attribute.getValue()));
                pointsText.setText("Points: " + playerData.getPoints());
            }
        });
        return view;
    }
    /*
    private void addAbilitySpinner(){
        Spinner abilities = (Spinner) view.findViewById(R.id.abilitySpinner);
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
    }
    */
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

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.ships_container);

        final String[] names = getResources().getStringArray(R.array.ship_skins);

        final TextView curSkin = (TextView) view.findViewById(R.id.ship_skin_name);
        curSkin.setText("Current skin: " + names[playerData.getShipSkin()]);

        for(int i = 0; i < 4; i++){
            final int index = i;
            View shipView = inflater.inflate(R.layout.ship_layout, null);
            ImageView img = (ImageView) shipView.findViewById(R.id.ship_image);
            TextView shipName = (TextView) shipView.findViewById(R.id.ship_name);
            shipName.setText(names[i]);
            img.setImageResource(R.drawable.ship1 + i);
            layout.addView(shipView);
            shipView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playerData.setShipSkin(index);
                    curSkin.setText("Current skin: " + names[index]);
                }
            });
        }

        /*
        Spinner abilities = (Spinner) view.findViewById(R.id.spnSkin);

        final ImageView imageView = (ImageView) view.findViewById(R.id.imgSkin);
        //imageView.setImageResource(R.drawable.ship1 + playerData.getShipSkin());

        ArrayAdapter<CharSequence> strings = ArrayAdapter.createFromResource(getContext(), R.array.ship_skins,
                R.layout.spinner_item);

        strings.setDropDownViewResource(R.layout.spinner_item);
        abilities.setAdapter(strings);
        abilities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageView.setImageResource(R.drawable.ship1 + position);
                playerData.setShipSkin(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        abilities.setSelection(playerData.getShipSkin());
        */
        return view;
    }
}