package com.zemian.spacejoystick;

/**
 * Created by Oleksandr on 01/04/2017.
 */

enum CommandState{
    NOTPRESSED,
    PRESSED,
    CLICKED,
    SENDONCE
}

public class CommandButtonState {

    private CommandState state;
    private char commandChar;
    private boolean sendOnce = false;

    public CommandButtonState(char commandChar){
        this.commandChar = commandChar;
        state = CommandState.NOTPRESSED;
        sendOnce = false;
    }

    public CommandButtonState(char commandChar, boolean sendOnce){
        this.commandChar = commandChar;
        state = CommandState.NOTPRESSED;
        this.sendOnce = sendOnce;
    }

    public void press(){
        state = CommandState.CLICKED;
        System.out.println("left: pressed press!");
    }

    public void update(){
        if(sendOnce){
            state = CommandState.NOTPRESSED;
            return;
        }

        if(state == CommandState.CLICKED)
            state = CommandState.PRESSED;
        if(state == CommandState.SENDONCE)
            state = CommandState.NOTPRESSED;
    }

    public void release(){
        if(state == CommandState.CLICKED)
            state = CommandState.SENDONCE;
        if(state == CommandState.PRESSED)
            state = CommandState.NOTPRESSED;
    }

    public boolean mustBeSended(){
        if(state != CommandState.NOTPRESSED)
            return true;
        return false;
    }

    public char getCommandChar(){
        update();
        return commandChar;
    }
}
