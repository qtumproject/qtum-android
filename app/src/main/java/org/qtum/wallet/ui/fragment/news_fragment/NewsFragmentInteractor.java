package org.qtum.wallet.ui.fragment.news_fragment;

import org.qtum.wallet.model.gson.News;

import java.util.List;


interface NewsFragmentInteractor {
    void getNewsList(NewsFragmentInteractorImpl.GetNewsListCallBack callBack);
    List<News> getNewsList();
}
