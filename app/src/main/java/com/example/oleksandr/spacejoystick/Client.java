package com.example.oleksandr.spacejoystick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Oleksandr on 10/02/2017.
 */

public class Client extends Thread {

    public static final int PORT = 6546;
    public static Client instance;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private ClientListener clientListener;

    public Client(){
        if(instance != null)
            instance.stopClient();

        instance = this;
    }

    public void connect(String ip, int port) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 3000);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            instance = this;
            start();
            if(clientListener != null) clientListener.onConnectionEvent(ConnectionEvent.CONNECTED);
        } catch (IOException e) {
            if(clientListener != null) clientListener.onConnectionEvent(ConnectionEvent.CONNECTION_FAILED);
            e.printStackTrace();
        }
    }

    public static Client getInstance(){
        //PAY ATTENTION!!!
        if(instance == null)
            return new Client();
        return instance;
    }

    public void setClientListener(ClientListener listener){
        clientListener = listener;
    }

    public void run(){
        while (true){
            try {
                String message = reader.readLine();
                System.out.println("Receive: " + message);
                if(clientListener != null){
                    clientListener.onMessageReceived(message);
                }
            } catch (IOException e) {
                if(clientListener != null)
                    clientListener.onConnectionEvent(ConnectionEvent.CONNECTION_STOPED);
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message){
        System.out.println("Invio: " + message);
        try {
            writer.println(message);
            writer.flush();
            if(clientListener != null)
                clientListener.onMessageSendSuccess();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void send(Command command){
        sendMessage(Converter.toString(command));
    }

    public void send(Command command, String[] dati){
        String message = Converter.toString(command);
        for(int i = 0; i < dati.length; i++){
            message += ":" + dati[i];
        }
        sendMessage(message);
    }

    public void send(Request request){
        sendMessage(Converter.toString(request));
    }

    public void send(Request request, String[] dati){
        String message = Converter.toString(request);
        for(int i = 0; i < dati.length; i++){
            message += ":" + dati[i];
        }
        sendMessage(message);
    }

    public void stopClient(){
        try {
            interrupt();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
