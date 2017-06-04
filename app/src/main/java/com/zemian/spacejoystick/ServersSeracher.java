package com.zemian.spacejoystick;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Oleksandr on 03/06/2017.
 */

public class ServersSeracher {

    private String ip;
    private IOnServerDetected listener;
    private static ServersSeracher instance;
    private boolean search = true;
    private ArrayList<ServersChecker> serversCheckers = new ArrayList<ServersChecker>();
    private int endCount = 0;

    public ServersSeracher(String ip, IOnServerDetected listener, boolean createInstance){
        this.listener = listener;
        this.ip = ip;

        if(createInstance)
            instance = this;
    }

    public void addServersChecker(int from, int to){
        ServersChecker checker = new ServersChecker(from, to);
        serversCheckers.add(checker);
    }

    public void startChecking(){
        for (int i = 0; i < serversCheckers.size(); i++)
            serversCheckers.get(i).start();
    }

    public static ServersSeracher getInstance(){
        return instance;
    }

    public void stopSearching(){
        search = false;
    }

    private void endSearching(){
        endCount ++;
        if(endCount > serversCheckers.size() - 1)
            listener.onServerDetected(null, null);
    }

    /* --------------------------------- ServersCheckers --------------------------------- */
    private class ServersChecker extends Thread {
        private int from;
        private int to;

        public ServersChecker(int from, int to){
            this.from = from;
            this.to = to;
        }

        public void run(){
            /*
            try {
                String prefix = ip.substring(0, ip.lastIndexOf(".") + 1);

                for (int i = from; i < to; i++) {
                    if(!search) {
                        return;
                    }

                    String testIp = prefix + String.valueOf(i);
                    InetAddress name = InetAddress.getByName(testIp);

                    try {
                        Socket s = new Socket();
                        s.connect(new InetSocketAddress(name.getHostAddress(), 6546), 200);

                        PrintWriter writer = new PrintWriter(s.getOutputStream());
                        //BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        writer.println(Converter.toChar(Command.SERVERCHECKER));
                        writer.flush();
                        //String read = reader.readLine();
                        //System.out.println("SC: " + read);
                        listener.onServerDetected(name.toString().substring(1), "");
                        s.close();
                    } catch (Exception e)
                    {

                    }
                }
                endSearching();
            } catch (Exception e) {
                e.printStackTrace();
            }
            */
        }
    }
}
