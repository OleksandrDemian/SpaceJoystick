package com.zemian.spacejoystick;

/**
 * Created by Oleksandr on 18/02/2017.
 */

public class Converter {

    public static char toChar(Command command){
        switch (command){
            case NONE:
                return 'q';
            case TURNLEFT:
                return 'w';
            case TURNRIGHT:
                return 'e';
            case ENGINETRIGGER:
                return 'r';
            case FIRE:
                return 't';
            case ABILITYTRIGGER:
                return 'y';
            case ABILITYINFO:
                return 'u';
            case NAME:
                return 'i';
            case SHIPINFO:
                return 'o';
            case STARTGAME:
                return 's';
            case PAUSE:
                return 'p';
            case KILL:
                return 'a';
            case DEATH:
                return 'd';
            case ADDPOINT:
                return 'f';
            case MATCHEND:
                return 'g';
            case COLOR:
                return 'h';
            case SHIELD:
                return 'j';
            case HEALTH:
                return 'k';
            case COMMANDSSTRING:
                return 'l';
        }
        return ' ';
    }

    public static Command toCommand(char c){
        switch (c){
            case 'q':
                return Command.NONE;
            case 'w':
                return Command.TURNLEFT;
            case 'e':
                return Command.TURNRIGHT;
            case 'r':
                return Command.ENGINETRIGGER;
            case 't':
                return Command.FIRE;
            case 'y':
                return Command.ABILITYTRIGGER;
            case 'u':
                return Command.ABILITYINFO;
            case 'i':
                return Command.NAME;
            case 'o':
                return Command.SHIPINFO;
            case 's':
                return Command.STARTGAME;
            case 'p':
                return Command.PAUSE;
            case 'a':
                return Command.KILL;
            case 'd':
                return Command.DEATH;
            case 'f':
                return Command.ADDPOINT;
            case 'g':
                return Command.MATCHEND;
            case 'h':
                return Command.COLOR;
            case 'j':
                return Command.SHIELD;
            case 'k':
                return Command.HEALTH;
            case 'l':
                return Command.COMMANDSSTRING;
            default:
                return Command.NONE;
        }
    }
}
