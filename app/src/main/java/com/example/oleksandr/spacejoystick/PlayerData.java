package com.example.oleksandr.spacejoystick;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Oleksandr on 15/02/2017.
 */

public class PlayerData {

    private Context context;

    private String name;
    private int shipSkin;
    private int ability;
    private float abilityCoolDown = 5;
    private float fireCoolDown = .5f;

    private int points = 5;

    private ArrayList<Attribute> attributes = new ArrayList<>();

    public PlayerData(Context context){
        this.context = context;
        attributes.add(new Attribute("health", 0, 5, 15));
        attributes.add(new Attribute("damage", 0, 3, 10));
        attributes.add(new Attribute("shield", 0, 1, 2));
        attributes.add(new Attribute("speed", 0, 50, 500));
    }

    public void loadPlayer(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("player", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("name", "Nameless");
        shipSkin = sharedPreferences.getInt("shipSkin", 1);
        ability = sharedPreferences.getInt("ability", 0);
        abilityCoolDown = sharedPreferences.getFloat("abilityCoolDown", 5);
        fireCoolDown = sharedPreferences.getFloat("fireCoolDown", .5f);
        points = sharedPreferences.getInt("points", 5);

        for(int i = 0; i < attributes.size(); i++){
            attributes.get(i).setLevel(sharedPreferences.getInt(attributes.get(i).getName(), 0));
        }
    }

    public void savePlayer(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("player", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", name);
        editor.putInt("shipSkin", shipSkin);
        editor.putInt("ability", ability);
        editor.putFloat("abilityCoolDown", abilityCoolDown);
        editor.putFloat("fireCoolDown", fireCoolDown);

        editor.putInt("points", points);

        for(int i = 0; i < attributes.size(); i++){
            Attribute attr = attributes.get(i);
            editor.putInt(attr.getName(), attr.getLevel());
        }

        editor.commit();
    }

    public void clearSavedData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("player", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public String[] getShipInfoArray(){
        //Dati order: health, damage, shield, speed, shipSkin, abilityType, abilityLevel
        String[] dati = {
                String.valueOf(getAttribute("health").getValue()),
                String.valueOf(getAttribute("damage").getValue()),
                String.valueOf(getAttribute("shield").getValue()),
                String.valueOf(getAttribute("speed").getValue()),
                String.valueOf(shipSkin),
                String.valueOf(ability),
                "1"
        };
        return dati;
    }

    public Attribute getAttribute(String name){
        for(int i = 0; i < attributes.size(); i++){
            if(name.equals(attributes.get(i).getName()))
                return attributes.get(i);
        }
        return null;
    }

    public Attribute getAttribute(int index){
        if(index < attributes.size())
            return attributes.get(index);

        return null;
    }

    public int getAttributesCount(){
        return attributes.size();
    }

    public void decresePoints(){
        points --;
    }

    //--------------------------------GETTERS--------------------------------//
    public float getAbilityCoolDown(){
        return abilityCoolDown;
    }

    public float getFireCoolDown(){
        return fireCoolDown;
    }

    public String getName(){
        return name;
    }

    public int getPoints(){
        return points;
    }

    public int getShipSkin() { return shipSkin; }

    public int getAbility(){ return ability; }

    //--------------------------------SETTERS--------------------------------//
    public void setName(String name) {
        this.name = name;
    }

    public void setShipSkin(int shipSkin) {
        this.shipSkin = shipSkin;
    }

    public void setAbility(int ability) {
        this.ability = ability;
    }

    public void setAbilityCoolDown(float abilityCoolDown) {
        this.abilityCoolDown = abilityCoolDown;
    }

    public void setFireCoolDown(float fireCoolDown) {
        this.fireCoolDown = fireCoolDown;
    }
}
