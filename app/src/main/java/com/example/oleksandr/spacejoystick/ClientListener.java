package com.example.oleksandr.spacejoystick;

/**
 * Created by Oleksandr on 12/02/2017.
 */

public interface ClientListener {
    void onMessageReceived(String message);
    void onMessageSendSuccess();
    void onConnectionEvent(ConnectionEvent event);
}
