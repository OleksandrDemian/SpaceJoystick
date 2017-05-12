package com.zemian.spacejoystick;

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
        startTime = lastCheck;
    }

    public void tick(){
        lastCheck = System.nanoTime();
    }

    public boolean passed(){
        System.out.println(deltaTime() + ": " + chekingTime);
        if(deltaTime() > chekingTime){
            if(autoTick)
                tick();
            return true;
        }
        return false;
    }

    public double passedSinceStart(){
        return calculateTime(startTime, System.nanoTime());
    }

    public double deltaTime(){
        return calculateTime(lastCheck, System.nanoTime());
    }

    private double calculateTime(long from, long to){
        long nanoDelta = to - from;
        double seconds = (nanoDelta / 1000000000.0);
        System.out.println("Delta: " + seconds);
        return seconds;
    }

    public void setChekingTime(float chekingTime){
        this.chekingTime = chekingTime;
    }
}
