package com.zemian.spacejoystick.News;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Oleksandr on 06/06/2017.
 */

public class NewsLoader extends AsyncTask<Void, Void, Void> {

    private INewsLoaderListener listener;

    public void setListener(INewsLoaderListener listener){
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Uri.Builder builder = new Uri.Builder();
        ArrayList<News> news = new ArrayList<News>();

        builder.scheme("http").authority("infinitysasha.altervista.org").appendPath("SpaceShooterParty").appendPath("newsloader.php");
        try {
            URL url = new URL(builder.toString());

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuffer buffer = new StringBuffer();

            for (;;){
                String read = reader.readLine();
                if(read == null)
                    break;
                buffer.append(read);
            }

            String[] parts = buffer.toString().split("<>");
            //System.out.println("Read: " + buffer);

            for(int i = 0; i < parts.length-1; i += 3){
                News element = new News(parts[i], parts[i+1], parts[i+2]);
                news.add(element);
            }
        } catch (MalformedURLException e) {
            if(listener != null)
                listener.onLoadingFailed();

            e.printStackTrace();
            return null;
        } catch (ProtocolException e) {
            if(listener != null)
                listener.onLoadingFailed();

            e.printStackTrace();
            return null;
        } catch (IOException e) {
            if(listener != null)
                listener.onLoadingFailed();

            e.printStackTrace();
            return null;
        }

        if(listener != null)
            listener.onNewsLoaded(news);

        return null;
    }
}
