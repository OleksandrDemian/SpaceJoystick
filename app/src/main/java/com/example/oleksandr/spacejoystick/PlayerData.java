package com.example.oleksandr.spacejoystick;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Oleksandr on 15/02/2017.
 */

public class PlayerData {

    private Context context;

    private String name;
    private String health;

    public PlayerData(Context context){
        this.context = context;
    }

    public void loadPlayer(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("player", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "Nameless");
        String health = sharedPreferences.getString("health", "50");
        setName(name, false);
        setHealth(health, false);
    }

    public void saveData(String tag, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("player", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(tag, value);
        editor.commit();
    }

    public String getData(String tag){
        String value = "";
        SharedPreferences sharedPreferences = context.getSharedPreferences("player", Context.MODE_PRIVATE);
        value = sharedPreferences.getString(tag, "Nameless");
        return value;
    }

    public void savePlayer(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("player", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("health", health);
        editor.commit();
    }

    public String getName(){
        System.out.println(name);
        return name;
    }

    public void setName(String name, boolean save){
        this.name = name;
        if(save)
            saveData("name", name);
    }

    public void setHealth(String health, boolean save){
        this.health = health;
        if(save)
            saveData("health", health);
    }
}
