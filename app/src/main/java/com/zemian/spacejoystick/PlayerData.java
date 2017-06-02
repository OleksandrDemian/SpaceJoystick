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
    /*
    private float abilityCoolDown = 5;
    private float fireCoolDown = .5f;
    */
    private int points = 0;

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
            System.out.println("Get new player's instance");
            PlayerData playerData = new PlayerData(context);
            playerData.loadPlayer();
            instance = playerData;
            return playerData;
        }
        System.out.println("Get ready player's instance");
        return instance;
    }

    public void loadPlayer(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("player", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("name", "Nameless");
        shipSkin = sharedPreferences.getInt("shipSkin", 1);
        ability = sharedPreferences.getInt("ability", 0);
        /*
        abilityCoolDown = sharedPreferences.getFloat("abilityCoolDown", 5);
        fireCoolDown = sharedPreferences.getFloat("fireCoolDown", .5f);
        */
        points = sharedPreferences.getInt("points", 5);

        for(int i = 0; i < attributes.size(); i++){
            int value = sharedPreferences.getInt(attributes.get(i).getName(), 0);
            attributes.get(i).setLevel(value);
        }
    }

    public void savePlayer(){
        SharedPreferences.Editor editor = getEditor();

        editor.putString("name", name);
        editor.putInt("shipSkin", shipSkin);
        editor.putInt("ability", ability);
        /*
        editor.putFloat("abilityCoolDown", abilityCoolDown);
        editor.putFloat("fireCoolDown", fireCoolDown);
        */
        editor.putInt("points", points);

        for(int i = 0; i < attributes.size(); i++){
            Attribute attr = attributes.get(i);
            editor.putInt(attr.getName(), attr.getLevel());

            /*
            String encName = Utils.encrypt(attr.getName());
            String encVal = Utils.encrypt("" + attr.getLevel());

            System.out.println("Encrypted: " + encName + ": " + encVal);

            String name = Utils.decrypt(encName);
            String val = Utils.decrypt(encVal);

            System.out.println("Decrypted: " + name + ": " + val);
            */
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
    }

    //NOT TESTED
    public void incresePoints(){
        points ++;
    }

    public void incresePoints(boolean mustSave){
        points ++;
        if(!mustSave)
            return;

        SharedPreferences.Editor editor = getEditor();
        editor.putInt("points", points);
        System.out.println("Points: " + points);
        editor.commit();
    }

    //--------------------------------GETTERS--------------------------------//
    /*public float getAbilityCoolDown(){
        return abilityCoolDown;
    }*/

    /*public float getFireCoolDown(){
        return fireCoolDown;
    }*/

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

    /*
    public void setAbilityCoolDown(float abilityCoolDown) {
        this.abilityCoolDown = abilityCoolDown;
    }

    public void setFireCoolDown(float fireCoolDown) {
        this.fireCoolDown = fireCoolDown;
    }
    */
}
