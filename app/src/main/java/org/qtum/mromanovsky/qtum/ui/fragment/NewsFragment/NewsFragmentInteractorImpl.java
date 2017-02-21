package org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.QtumService;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.News;
import org.qtum.mromanovsky.qtum.datastorage.HistoryList;
import org.qtum.mromanovsky.qtum.datastorage.NewsList;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class NewsFragmentInteractorImpl implements NewsFragmentInteractor{

    private Subscription mSubscriptionNewsList = null;

    NewsFragmentInteractorImpl(){

    }

    @Override
    public void getNewsList(final GetNewsListCallBack callBack){
        mSubscriptionNewsList = QtumService.newInstance().getNews("en")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<News>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<News> newsList) {
                        NewsList.getInstance().setNewsList(newsList);
                        callBack.onSuccess(newsList);
                    }
                });
    }

    @Override
    public List<News> getNewsList() {
        return NewsList.getInstance().getNewsList();
    }

    void unSubscribe(){
        mSubscriptionNewsList.unsubscribe();
    }


    interface GetNewsListCallBack{
        void onSuccess(List<News> newsList);
    }
}
