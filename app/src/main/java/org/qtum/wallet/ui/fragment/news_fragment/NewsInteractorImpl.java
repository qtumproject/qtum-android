package org.qtum.wallet.ui.fragment.news_fragment;

import android.content.Context;

import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.model.gson.News;
import org.qtum.wallet.datastorage.NewsList;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class NewsInteractorImpl implements NewsInteractor {

    private WeakReference<Context> mContext;
    private Subscription mSubscriptionNewsList = null;

    public NewsInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public void getNewsList(final GetNewsListCallBack callBack) {
        mSubscriptionNewsList = QtumService.newInstance().getNews("en")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<News>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
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

    @Override
    public void unSubscribe() {
        mSubscriptionNewsList.unsubscribe();
    }


    public interface GetNewsListCallBack {
        void onSuccess(List<News> newsList);
    }
}
