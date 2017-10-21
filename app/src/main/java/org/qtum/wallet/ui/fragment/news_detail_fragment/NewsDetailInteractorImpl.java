package org.qtum.wallet.ui.fragment.news_detail_fragment;


import android.content.Context;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.qtum.wallet.datastorage.NewsStorage;

import java.lang.ref.WeakReference;
import java.util.List;

public class NewsDetailInteractorImpl implements NewsDetailInteractor{

    WeakReference<Context> mContext;

    public NewsDetailInteractorImpl(Context context){
        mContext = new WeakReference<Context>(context);
    }

    @Override
    public List<Element> getElements(int newsPosition) {
        //Document document = NewsStorage.newInstance().getNews(newsPosition).getDocument();
        return null;
    }
}
