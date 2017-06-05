package com.zemian.spacejoystick;

/**
 * Created by Oleksandr on 12/02/2017.
 */

public interface ClientListener {
    void onMessageReceived(String message);
    void onMessageSendSuccess();
    void onClientStateChange(ClientState state);
    void onConnectionEvent(ConnectionEvent event);
}
