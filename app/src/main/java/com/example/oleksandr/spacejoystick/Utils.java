package com.example.oleksandr.spacejoystick;

/**
 * Created by Oleksandr on 21/03/2017.
 */

public class Utils {

    public static int toNum(String text){
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exception){
            return -1;
        }
    }

}
