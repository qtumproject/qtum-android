package org.qtum.wallet.ui.fragment.news_fragment;

import org.qtum.wallet.model.gson.News;

import java.util.List;


public interface NewsInteractor {
    void getNewsList(NewsInteractorImpl.GetNewsListCallBack callBack);

    List<News> getNewsList();

    void unSubscribe();

}
