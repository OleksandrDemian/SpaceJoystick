package com.zemian.spacejoystick;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.content.Context.WIFI_SERVICE;

public class Client extends Thread {

    public static final int PORT = 6546;    //Server's port
    public static Client instance;          //Instance of the client
    private Socket socket;                  //Socket
    private PrintWriter writer;             //Used to write data to server
    private BufferedReader reader;          //Read's data from server
    private ClientListener clientListener;  //Listener
    private boolean isRunning = false;      //Is client running
    private boolean started = false;

    /**
     * Default constructor
     */
    public Client() {
        if (instance != null)
            instance.stopClient();

        instance = this;
    }

    /**
     * Connect to the server
     *
     * @param ip   -> destination address
     * @param port -> destination port
     */
    public void connect(String ip, int port) {
        try {
            //Initialize socket
            isRunning = true;
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 3000);
            //Initialize writer
            writer = new PrintWriter(socket.getOutputStream());
            if(writer == null)
                System.out.println("Whhaaaaaaaat!!!!");
            //Initialize reader
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            instance = this;
            start();
            //Notifies listener about success
            if (clientListener != null) clientListener.onConnectionEvent(ConnectionEvent.CONNECTED);
        } catch (IOException e) {
            //Notifies listener about failure
            if (clientListener != null)
                clientListener.onConnectionEvent(ConnectionEvent.CONNECTION_FAILED);
            e.printStackTrace();
        }
    }

    /**
     * @return the current instance of the client
     */
    public static Client getInstance() {
        //PAY ATTENTION!!!
        if (instance == null)
            return new Client();
        return instance;
    }

    //Set the client listener
    public void setClientListener(ClientListener listener) {
        clientListener = listener;
    }

    //Listens for messages from server
    public void run() {
        started = true;

        while (true) {
            try {
                String message = reader.readLine();
                //System.out.println("Receive: " + message);
                if (clientListener != null) {
                    clientListener.onMessageReceived(message);
                }
                //run();
            } catch (Exception e) {
                System.out.println("ErrorSocket: " + e.getMessage());
                System.out.println("ErrorSocket: " + e.getStackTrace());

                if (clientListener != null)
                    clientListener.onConnectionEvent(ConnectionEvent.CONNECTION_STOPED);
                //e.printStackTrace();
                return;
            }
        }
    }

    /**
     * Sends message via socket
     *
     * @param message - message to send
     */
    public void sendMessage(String message) {
        System.out.println("Invio: " + message);
        try {
            writer.println(message);
            writer.flush();
        } catch (Exception e) {
            clientListener.onConnectionEvent(ConnectionEvent.CONNECTION_STOPED);
            e.printStackTrace();
        }
    }

    /**
     * Sends the command to server
     *
     * @param command
     */
    public void send(Command command) {
        sendMessage(String.valueOf(Converter.toChar(command)));
    }

    /**
     * Sends the command with data to server
     *
     * @param command
     * @param dati
     */
    public void send(Command command, String[] dati) {
        String message = String.valueOf(Converter.toChar(command));

        for (int i = 0; i < dati.length; i++) {
            message += ":" + dati[i];
        }
        sendMessage(message);
    }

    /**
     * Sends the request to server
     * @param request
     */
    /*public void send(Request request){
        sendMessage(Converter.toString(request));
    }*/

    /**
     * Sends the request with data to server
     * @param request
     * @param dati
     */
    /*public void send(Request request, String[] dati){
        String message = Converter.toString(request);
        for(int i = 0; i < dati.length; i++){
            message += ":" + dati[i];
        }
        sendMessage(message);
    }*/

    /**
     * Stops listening
     */
    public void stopClient() {
        if (!isRunning)
            return;

        try {
            isRunning = false;
            interrupt();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning(){
        return isRunning;
    }

    public boolean isStarted(){
        return started;
    }

    public static String getLocalIP(Context context){
        try {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            String ipAddress = Formatter.formatIpAddress(ip);
            return ipAddress;
        } catch (Exception e){
            return null;
        }

    }
}
