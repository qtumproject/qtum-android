package org.qtum.wallet.ui.fragment.news_fragment;

import org.qtum.wallet.datastorage.NewsStorage;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.model.news.RssFeed;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsPresenterImpl extends BaseFragmentPresenterImpl implements NewsPresenter {

    private NewsView mNewsFragmentView;
    private NewsInteractor mNewsFragmentInteractor;
    private final String MEDIUM_QTUM_CHANEL = "@qtum";

    public NewsPresenterImpl(NewsView newsFragmentView, NewsInteractor newsInteractor) {
        mNewsFragmentView = newsFragmentView;
        mNewsFragmentInteractor = newsInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        loadAndUpdateNews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getInteractor().unSubscribe();
    }

    private NewsInteractor getInteractor() {
        return mNewsFragmentInteractor;
    }

    @Override
    public NewsView getView() {
        return mNewsFragmentView;
    }

    @Override
    public void onRefresh() {
        loadAndUpdateNews();
    }

    private void loadAndUpdateNews() {
        getView().startRefreshAnimation();
        getInteractor().getMediumRssFeed(MEDIUM_QTUM_CHANEL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RssFeed>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RssFeed rssFeed) {
                        NewsStorage.newInstance().setNewses(rssFeed.getNewses());

                        List<News> newNews = rssFeed.getNewses();
                        List<News> oldNews = getInteractor().getNewses();
                        if(oldNews.size()==0){
                            oldNews.addAll(newNews);
                        }else {
                            int pos = 0;
                            News lastNews = oldNews.get(0);
                            for (News news : newNews) {
                                if (!news.getPubDate().equals(lastNews.getPubDate())) {
                                    oldNews.add(pos, news);
                                    pos++;
                                } else {
                                    break;
                                }
                            }
                        }
                        getInteractor().setNewses(oldNews);
                        getView().updateNews(oldNews);

                    }
                });
    }

}
