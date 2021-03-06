package com.zemian.spacejoystick;

/**
 * Created by Oleksandr on 22/05/2017.
 */

public class ActivityConnectionData {

    private static ActivityConnectionData instance;

    public MainActivity.InputThread inputThread = null;
    public boolean gameStarted = false;
    public boolean shipInfoSent = false;
    public int backgroundColor = -1;
    public int kills = 0;
    public int death = 0;
    public int health = 0;
    public int shields = 0;

    public ActivityConnectionData (){
        instance = this;
    }

    public static ActivityConnectionData getInstance(boolean forceNew){
        if(instance == null || forceNew)
            return new ActivityConnectionData();

        return instance;
    }

    public MainActivity.InputThread getInputThread(){
        if(inputThread != null)
            return inputThread;
        return null;
    }

    public void setInputThread(MainActivity.InputThread thread){
        inputThread = thread;
        System.out.println("Set new Input thread: " + (inputThread == null ? "nullo" : "non nullo"));
    }

}
