package org.qtum.wallet.ui.fragment.news_fragment;

import org.qtum.wallet.model.news.RssFeed;

import rx.Observable;


public interface NewsInteractor {
    Observable<RssFeed> getMediumRssFeed(String channel);

    void unSubscribe();

}
