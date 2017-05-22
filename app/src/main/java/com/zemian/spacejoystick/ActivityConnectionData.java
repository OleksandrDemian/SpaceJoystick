package com.zemian.spacejoystick;

/**
 * Created by Oleksandr on 22/05/2017.
 */

public class ActivityConnectionData {

    private static ActivityConnectionData instance;

    public MainActivity.InputThread inputThread = null;
    public boolean gameStarted = false;
    public int backgroundColor = -1;

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
