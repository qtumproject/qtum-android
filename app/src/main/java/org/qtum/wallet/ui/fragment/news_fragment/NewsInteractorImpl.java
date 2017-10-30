package org.qtum.wallet.ui.fragment.news_fragment;

import android.content.Context;

import org.qtum.wallet.dataprovider.medium_rss_feed.MediumService;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.model.news.RssFeed;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.internal.util.SubscriptionList;


public class NewsInteractorImpl implements NewsInteractor {

    private WeakReference<Context> mContext;
    private SubscriptionList mSubscriptionList = new SubscriptionList();

    public NewsInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public Observable<RssFeed> getMediumRssFeed(String channel) {
        return MediumService.getInstance().getRssFeed(channel);
    }

    @Override
    public List<News> getNewses() {
        TinyDB tinyDB = new TinyDB(mContext.get());
        return tinyDB.getNewsList();
    }

    @Override
    public void setNewses(List<News> newses) {
        TinyDB tinyDB = new TinyDB(mContext.get());
        tinyDB.putNewsList(newses);
    }

    @Override
    public void unSubscribe() {
        mSubscriptionList.clear();
    }

}
