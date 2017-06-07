package com.zemian.spacejoystick;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oleksandr.spacejoystick.R;
import com.zemian.spacejoystick.News.INewsContainerListener;
import com.zemian.spacejoystick.News.NewsContainer;

/**
 * Created by Oleksandr on 14/02/2017.
 */

public class ConnectionScreen extends Fragment implements ClientListener, INewsContainerListener {

    private View view;      //Fragment's view
    private Button connect;
    private Client client;  //Connection
    //private ServersSeracher serversSeracher;
    private NewsContainer newsContainer;

    /**
     * Empty constructor required for fragments
     */
    public ConnectionScreen() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        showNews();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //checkServers(true);
        client = new Client();

        final EditText ipText = (EditText)view.findViewById(R.id.ip);   //Text where you type the game ip
        ipText.setText(getLastIP());                                    //Set the last inserted ip

        connect = (Button)view.findViewById(R.id.btnConnect);    //Start connection to the server when pressed
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ipText.getText().toString();                //Get the ip
                startClient(ip, Client.PORT);                           //Connection to the server
            }
        });
    }

    private void showNews(){
        NewsContainer news = NewsContainer.getInstance();
        if(news.newsDownloaded()) {
            onNewsLoaded(news, true);
            return;
        }

        news.setListener(this);
        news.loadNews();
    }
    /*
    private void checkServers(boolean enabled) {
        final TextView ipListStae = (TextView)view.findViewById(R.id.ipListState);
        if(!enabled){
            ipListStae.setText("Searching disabled");
            return;
        }

        final LinearLayout ipList = (LinearLayout)view.findViewById(R.id.ips);

        serversSeracher = ServersSeracher.getInstance();

        if(serversSeracher != null)
            serversSeracher.stopSearching();

        int views = ipList.getChildCount();
        if(views > 1) {
            for (int i = views - 1; i > 0; i--) {
                ipList.removeViewAt(i);
            }
        }
        String ip = Client.getLocalIP(getContext());

        if(ip == null) {
            Toast.makeText(getContext(), "Can't search servers", Toast.LENGTH_LONG);
            ipListStae.setText("Can't search servers");
            return;
        }

        ipListStae.setText("Searching for servers...");
        serversSeracher = new ServersSeracher (ip, new IOnServerDetected() {
            @Override
            public void onServerDetected(final String ip, final String name) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(ip == null){
                            if(ipList.getChildCount() < 2)
                                ipListStae.setText("There is no available servers");
                            else
                                ipListStae.setText("Available servers: ");
                        }

                        View ipView = getActivity().getLayoutInflater().inflate(R.layout.ip_layout, null);
                        TextView txt = (TextView) ipView.findViewById(R.id.ip_txt);
                        txt.setText(name + ": " + ip);
                        txt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startClient(ip, Client.PORT);
                            }
                        });
                        ipList.addView(ipView);
                    }
                });
            }
        }, true);
        serversSeracher.addServersChecker(0, 100);
        serversSeracher.addServersChecker(100, 200);
        serversSeracher.addServersChecker(200, 255);
        serversSeracher.startChecking();
    }
    */

    /**
     * Starts connection thread
     * @param ip -> server ip
     * @param port -> server port
     */
    private void startClient(final String ip, final int port){
        saveIP(ip);                                             //Save the ip as last ip

        if(client == null)
            client = Client.getInstance();

        if(client.getClientState() == ClientState.CONNECTING)
            return;

        ClientState state = client.getClientState();
        if(state  == ClientState.ISRUNNING || state == ClientState.TERMINATED)
            client = new Client();

        Thread startClientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                client.setClientListener(ConnectionScreen.this);
                client.connect(ip, port);
            }
        });
        Toast.makeText(getContext(), "Connecting to: " + ip, Toast.LENGTH_SHORT).show();
        connect.setEnabled(false);
        startClientThread.start();
    }

    /**
     * @return last saved ip
     */
    private String getLastIP(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("player", Context.MODE_PRIVATE);
        return sharedPreferences.getString("lastIP", "");
    }

    /**
     * Save ip to shared preferences
     * @param ip that must be saved
     */
    private void saveIP(String ip){
        SharedPreferences.Editor spEditor = getContext().getSharedPreferences("player", Context.MODE_PRIVATE).edit();
        spEditor.putString("lastIP", ip);
        spEditor.commit();
    }

    /**
     * Unused but required by ClientListener
     * @param message
     */
    @Override
    public void onMessageReceived(String message) {
        return;
    }

    /**
     * Unused but required by ClientListener
     */
    @Override
    public void onMessageSendSuccess() {
        return;
    }

    @Override
    public void onClientStateChange(ClientState state) {

    }

    /**
     * Is triggered each time connection change it's state
     * @param event
     */
    @Override
    public void onConnectionEvent(ConnectionEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connect.setEnabled(true);
            }
        });
        System.out.println("(Connection screen)Event: " + event.toString());
        switch (event){
            //If connected, starts joystick activity
            case CONNECTED:
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                ActivityConnectionData instance = ActivityConnectionData.getInstance(true);
                /*
                if(serversSeracher != null)
                    serversSeracher.stopSearching();
                */
                startActivity(intent);
                break;
            //If failed, shows error message
            case CONNECTION_FAILED:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    @Override
    public void onNewsLoaded(final NewsContainer container, final boolean success) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout newsLayout = (LinearLayout) view.findViewById(R.id.connection_screen_news);
                if(!success){
                    TextView txt = new TextView(getActivity());
                    txt.setTextColor(Color.WHITE);
                    txt.setText("Error during loading news");
                    newsLayout.addView(txt);
                    return;
                }
                int count = container.getCount();
                for(int i = 0; i < count; i++){
                    newsLayout.addView(container.getNews(i).getNews(getActivity().getLayoutInflater()));
                }
            }
        });
    }
}