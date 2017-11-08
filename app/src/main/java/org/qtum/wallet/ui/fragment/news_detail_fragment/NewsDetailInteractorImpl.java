package org.qtum.wallet.ui.fragment.news_detail_fragment;


import android.content.Context;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.qtum.wallet.datastorage.NewsStorage;
import org.qtum.wallet.model.news.News;

import java.lang.ref.WeakReference;


public class NewsDetailInteractorImpl implements NewsDetailInteractor{

    WeakReference<Context> mContext;

    public NewsDetailInteractorImpl(Context context){
        mContext = new WeakReference<Context>(context);
    }

    @Override
    public Elements getElements(int newsPosition) {
        News news = NewsStorage.newInstance().getNews(newsPosition);
        if(news != null) {
            Document document = news.getDocument();
            return document != null ? document.body().children() : null;
        } else {
            return null;
        }
    }
}
