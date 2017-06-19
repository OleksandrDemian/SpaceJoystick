package com.zemian.spacejoystick;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Oleksandr on 15/02/2017.
 */

public class PlayerData {

    public static PlayerData instance;
    private Context context;

    private String name;
    private int shipSkin;
    private int ability;
    private int points = 0;

    private IPlayerListener listener;

    private ArrayList<Attribute> attributes = new ArrayList<>();

    public PlayerData(Context context){
        this.context = context;
        attributes.add(new Attribute("Health", 0, 10, 20));
        attributes.add(new Attribute("Damage", 0, 3, 10));
        attributes.add(new Attribute("Shield", 0, 1, 2));
        attributes.add(new Attribute("Speed", 0, 50, 500));
    }

    public static PlayerData getInstance (Context context){
        if(instance == null){
            PlayerData playerData = new PlayerData(context);
            playerData.loadPlayer();
            instance = playerData;
            return playerData;
        }
        return instance;
    }

    public void setPlayerListener(IPlayerListener listener){
        this.listener = listener;
    }

    public void loadPlayer(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("player", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("name", "Nameless");
        shipSkin = sharedPreferences.getInt("shipSkin", 1);
        ability = sharedPreferences.getInt("ability", 0);
        points = sharedPreferences.getInt("points", 5);

        for(int i = 0; i < attributes.size(); i++){
            int value = sharedPreferences.getInt(attributes.get(i).getName(), 0);
            attributes.get(i).setLevel(value);
        }
    }

    public void attributeChange(Attribute attr){
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(attr.getName(), attr.getLevel());
        editor.commit();
    }

    public void savePlayer(){
        SharedPreferences.Editor editor = getEditor();
        editor.putString("name", name);
        editor.putInt("shipSkin", shipSkin);
        editor.putInt("ability", ability);
        editor.putInt("points", points);
        for(int i = 0; i < attributes.size(); i++){
            Attribute attr = attributes.get(i);
            editor.putInt(attr.getName(), attr.getLevel());
        }
        editor.commit();
    }

    public void clearSavedData(){
        SharedPreferences.Editor editor = getEditor();
        editor.clear();
        editor.commit();
    }

    public SharedPreferences.Editor getEditor(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("player", Context.MODE_PRIVATE);
        return sharedPreferences.edit();
    }

    public String[] getShipInfoArray(){
        //Dati order: health, damage, shield, speed, shipSkin, abilityType, abilityLevel
        String[] dati = {
                String.valueOf(getAttribute("Health").getValue()),
                String.valueOf(getAttribute("Damage").getValue()),
                String.valueOf(getAttribute("Shield").getValue()),
                String.valueOf(getAttribute("Speed").getValue()),
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
        savePoints();
        if(listener != null)
            listener.onPointsValueChange(points);
    }

    //NOT TESTED
    public void incresePoints(){
        points ++;
        savePoints();
        if(listener != null)
            listener.onPointsValueChange(points);
    }

    private void savePoints(){
        SharedPreferences.Editor editor = getEditor();
        editor.putInt("points", points);
        editor.commit();
    }

    //--------------------------------GETTERS--------------------------------//

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

        SharedPreferences.Editor editor = getEditor();
        editor.putString("name", name);
        editor.commit();
    }

    public void setShipSkin(ShipSkin shipSkin) {
        this.shipSkin = shipSkin.getSkinIndex();

        SharedPreferences.Editor editor = getEditor();
        editor.putInt("shipSkin", this.shipSkin);
        editor.commit();

        if(listener != null)
            listener.onSkinChange(shipSkin);
    }

    public void setAbility(int ability) {
        this.ability = ability;

        SharedPreferences.Editor editor = getEditor();
        editor.putInt("ability", ability);
        editor.commit();
    }

    /*
    public void setAbilityCoolDown(float abilityCoolDown) {
        this.abilityCoolDown = abilityCoolDown;
    }

    public void setFireCoolDown(float fireCoolDown) {
        this.fireCoolDown = fireCoolDown;
    }
    */
}
