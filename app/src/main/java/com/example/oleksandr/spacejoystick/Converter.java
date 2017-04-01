package com.example.oleksandr.spacejoystick;

import android.graphics.Color;

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

    public static int toColor(int index){
        switch (index){
            case 0:
                return Color.BLUE;
            case 1:
                return Color.GREEN;
            case 2:
                return Color.RED;
            case 3:
                return Color.YELLOW;
        }
        return -1;
    }

    public static char toChar(Command command){
        switch (command){
            case TURNLEFT:
                return 'l';
            case TURNRIGHT:
                return 'r';
            case FIRE:
                return 's';
            case ENGINETRIGGER:
                return 'e';
            case ABILITYTRIGGER:
                return 'a';
        }
        return ' ';
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
