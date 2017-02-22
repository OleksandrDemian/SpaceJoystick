package com.example.oleksandr.spacejoystick;

/**
 * Created by Oleksandr on 22/02/2017.
 */

public class Timer {

    private long startTime;
    private long lastCheck;
    private float chekingTime;
    private boolean autoTick = false;

    public Timer(float chekingTime, boolean autoTick){
        this.chekingTime = chekingTime;
        this.autoTick = autoTick;
        lastCheck = System.nanoTime();
    }

    public void tick(){
        lastCheck = System.nanoTime();
    }

    public boolean passed(){
        if(deltaTime() > chekingTime){
            if(autoTick)
                tick();
            return true;
        }
        return false;
    }

    public double deltaTime(){
        long nanoDelta = System.nanoTime() - lastCheck;
        double seconds = (nanoDelta / 1000000000.0);
        System.out.println("Delta: " + seconds);
        return seconds;
    }

    public void setChekingTime(float chekingTime){
        this.chekingTime = chekingTime;
    }
}
