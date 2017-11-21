package org.qtum.wallet.datastorage;

import org.qtum.wallet.model.news.News;

import java.util.List;

public class NewsStorage {

    private static NewsStorage sNewsStorage;
    private List<News> mNewses;

    private NewsStorage(){
    }

    public static NewsStorage newInstance(){
        if(sNewsStorage == null){
            sNewsStorage = new NewsStorage();
        }
        return sNewsStorage;
    }

    public List<News> getNewses() {
        return mNewses;
    }

    public void setNewses(List<News> newses) {
        mNewses = newses;
    }

    public News getNews(int position){
        if(mNewses != null && mNewses.size() > position) {
            return mNewses.get(position);
        } else {
            return null;
        }
    }
}
