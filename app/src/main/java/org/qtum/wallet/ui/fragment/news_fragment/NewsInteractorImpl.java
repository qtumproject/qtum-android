package org.qtum.wallet.ui.fragment.news_fragment;

import android.content.Context;

import org.qtum.wallet.dataprovider.medium_rss_feed.MediumService;
import org.qtum.wallet.model.news.RssFeed;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscription;


public class NewsInteractorImpl implements NewsInteractor {

    private WeakReference<Context> mContext;
    private Subscription mSubscriptionNewsList = null;

    public NewsInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public Observable<RssFeed> getMediumRssFeed(String channel) {
        return MediumService.getInstance().getRssFeed(channel);
    }

    @Override
    public void unSubscribe() {
        mSubscriptionNewsList.unsubscribe();
    }

}
