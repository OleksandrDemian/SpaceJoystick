package com.example.oleksandr.spacejoystick;

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

    public void levelDown(){
        level --;
    }

    @Override
    public String toString() {
        return name + ": " + getValue();
    }
}
