package com.zemian.spacejoystick.News;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.oleksandr.spacejoystick.R;

/**
 * Created by Oleksandr on 06/06/2017.
 */

public class News {

    private String title;
    private String date;
    private String content;

    public News(String title, String content, String date){
        this.content = content;
        this.date = date;
        this.title = title;
    }

    public View getNews(LayoutInflater inflater){
        View view = inflater.inflate(R.layout.news_layout, null);
        TextView titleV = (TextView) view.findViewById(R.id.news_title);
        TextView contentV = (TextView) view.findViewById(R.id.news_content);
        TextView dateV = (TextView) view.findViewById(R.id.news_date);

        titleV.setText(title);
        contentV.setText(content);
        dateV.setText(date);

        return view;
    }
}
