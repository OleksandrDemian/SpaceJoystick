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

    public CommandButtonState(char commandChar){
        this.commandChar = commandChar;
        state = CommandState.NOTPRESSED;
    }

    public void press(){
        state = CommandState.CLICKED;
    }

    public void update(){
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
