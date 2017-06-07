package com.zemian.spacejoystick.News;

import java.util.ArrayList;

/**
 * Created by Oleksandr on 06/06/2017.
 */

public interface INewsLoaderListener {
    void onNewsLoaded(ArrayList<News> news);
    void onLoadingFailed();
}
