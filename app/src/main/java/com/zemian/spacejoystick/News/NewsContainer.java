package com.zemian.spacejoystick.News;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Oleksandr on 06/06/2017.
 */

public class NewsContainer implements INewsLoaderListener {

    private ArrayList<News> allNews = new ArrayList<News>();
    private INewsContainerListener listener;
    public static NewsContainer instance;
    private boolean downloaded = false;

    public NewsContainer(){
        instance = this;
    }

    public static NewsContainer getInstance(){
        if(instance != null)
            return instance;

        return new NewsContainer();
    }

    public void addNews(News news){
        allNews.add(news);
    }

    public void setListener(INewsContainerListener listener){
        this.listener = listener;
    }

    public int getCount(){
        return allNews.size();
    }

    public News getNews(int index){
        return allNews.get(index);
    }

    public boolean newsDownloaded(){
        return downloaded;
    }

    public void loadNews(){
        NewsLoader loader = new NewsLoader();
        loader.setListener(this);
        loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    @Override
    public void onNewsLoaded(ArrayList<News> news) {
        allNews = news;
        downloaded = true;
        listener.onNewsLoaded(this, true);
    }

    @Override
    public void onLoadingFailed() {
        System.out.println("Failed loading news!");
        listener.onNewsLoaded(this, false);
    }
}
