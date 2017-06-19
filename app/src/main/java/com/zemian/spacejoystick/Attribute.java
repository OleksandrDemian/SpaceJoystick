package com.zemian.spacejoystick;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.oleksandr.spacejoystick.R;

import org.w3c.dom.Attr;

/**
 * Created by Oleksandr on 28/02/2017.
 */

public class Attribute {

    private String name;
    private int level;
    private int levelModifier;
    private int baseValue;

    public Attribute(String name, int level, int levelModifier, int baseValue){
        this.name = name;
        this.levelModifier = levelModifier;
        this.level = level * levelModifier;
        this.baseValue = baseValue;
    }

    public String getName(){
        return name;
    }

    public int getValue(){
        return baseValue + (level * levelModifier);
    }

    public int getLevel(){
        return level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public void levelUp(){
        level ++;
    }

    public void levelDown(){ level --; }

    public View getAttributeView(LayoutInflater inflater){
        View view = inflater.inflate(R.layout.attribute_layout, null);
        final TextView name = (TextView) view.findViewById(R.id.txtAttrName);
        final TextView value = (TextView) view.findViewById(R.id.txtAttrVal);
        Button levelUp = (Button) view.findViewById(R.id.btnAttrUp);
        Button levelDown = (Button) view.findViewById(R.id.btnAttrDown);
        name.setText(getName());
        value.setText(String.valueOf(getValue()));

        levelUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerData data = PlayerData.instance;

                if(data.getPoints() < 1)
                    return;

                levelUp();
                data.attributeChange(Attribute.this);
                data.decresePoints();
                value.setText(String.valueOf(getValue()));
                //pointsText.setText("Points: " + playerData.getPoints());
            }
        });

        levelDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getLevel() < 1)
                    return;

                PlayerData data = PlayerData.instance;

                levelDown();
                data.attributeChange(Attribute.this);
                data.incresePoints();
                value.setText(String.valueOf(getValue()));
                //pointsText.setText("Points: " + playerData.getPoints());
            }
        });
        return view;
    }

    @Override
    public String toString() {
        return name + ": " + getValue();
    }
}
