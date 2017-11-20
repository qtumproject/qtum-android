package org.qtum.wallet.ui.fragment.news_fragment;

import org.qtum.wallet.model.news.News;
import org.qtum.wallet.model.news.RssFeed;

import java.util.List;

import rx.Observable;


public interface NewsInteractor {
    Observable<RssFeed> getMediumRssFeed();
    List<News> getNewses();
    void setNewses(List<News> newses);
    void unSubscribe();

}
