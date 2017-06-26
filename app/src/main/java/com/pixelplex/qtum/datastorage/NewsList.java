package com.pixelplex.qtum.datastorage;

import com.pixelplex.qtum.model.gson.News;

import java.util.ArrayList;
import java.util.List;


public class NewsList {
    private static NewsList sNewsList;

    private List<News> mNewsList;

    public static NewsList getInstance() {
        if (sNewsList == null) {
            sNewsList = new NewsList();
        }
        return sNewsList;
    }

    public void clearNewsList() {
        sNewsList = null;
    }

    private NewsList() {
        mNewsList = new ArrayList<>();
    }

    public List<News> getNewsList() {
        return mNewsList;
    }

    public void setNewsList(List<News> newsList) {
        mNewsList = newsList;
    }
}