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


public class NewsFragmentInteractorImpl implements NewsFragmentInteractor{

    public NewsFragmentInteractorImpl(){

    }

    @Override
    public void getNewsList(final GetNewsListCallBack callBack){
        QtumService.newInstance().getNews("en")
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
                        callBack.onSuccess(newsList);
                        NewsList.getInstance().setNewsList(newsList);
                    }
                });
    }

    @Override
    public List<News> getNewsList() {
        return NewsList.getInstance().getNewsList();
    }


    public interface GetNewsListCallBack{
        void onSuccess(List<News> newsList);
    }
}
