package com.example.oleksandr.spacejoystick;

/**
 * Created by Oleksandr on 18/02/2017.
 */

public class Converter {

    public static String toString(Command command){
        switch (command){
            case TURNLEFT:
                return "cl";
            case TURNRIGHT:
                return "cr";
            case FIRE:
                return "cs";
            case ENGINETRIGGER:
                return "ce";
            case ABILITYTRIGGER:
                return "ca";
        }
        return "unknown";
    }

    public static String toString(Request request){
        switch(request){
            case NAME:
                return "rn";
            case SHIPINFO:
                return "rs";
        }
        return "";
    }
}
