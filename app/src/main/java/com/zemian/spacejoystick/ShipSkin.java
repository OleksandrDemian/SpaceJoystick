package com.zemian.spacejoystick;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oleksandr.spacejoystick.R;

/**
 * Created by Oleksandr on 12/06/2017.
 */

public class ShipSkin {

    private String name;
    private int skinIndex = 0;
    private boolean selected = false;
    private View view;

    public ShipSkin(String name, int skinIndex){
        this.name = name;
        this.skinIndex = skinIndex;
    }

    public int getSkinIndex(){
        return skinIndex;
    }

    public boolean isSelected(){
        return selected;
    }

    public void enableView(boolean action){
        selected = action;
        if(action)
            view.setAlpha(1f);
        else
            view.setAlpha(0.6f);
    }

    public String getName(){
        return name;
    }

    public View getView(LayoutInflater inflater){
        view = inflater.inflate(R.layout.ship_layout, null);
        ImageView img = (ImageView) view.findViewById(R.id.ship_image);
        view.setAlpha(0.6f);
        TextView shipName = (TextView) view.findViewById(R.id.ship_name);
        shipName.setText(name);
        img.setImageResource(R.drawable.ship1 + skinIndex);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerData.instance.setShipSkin(ShipSkin.this);
            }
        });

        return view;
    }
}
