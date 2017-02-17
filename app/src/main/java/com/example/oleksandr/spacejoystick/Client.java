package com.example.oleksandr.spacejoystick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Oleksandr on 10/02/2017.
 */

public class Client extends Thread {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public Client(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        while (true){
            try {
                reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message){
        writer.println(message);
        writer.flush();
    }
}
