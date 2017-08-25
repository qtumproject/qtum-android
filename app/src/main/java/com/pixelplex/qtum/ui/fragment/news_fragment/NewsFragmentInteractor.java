package com.pixelplex.qtum.ui.fragment.news_fragment;

import com.pixelplex.qtum.model.gson.News;

import java.util.List;


interface NewsFragmentInteractor {
    void getNewsList(NewsFragmentInteractorImpl.GetNewsListCallBack callBack);
    List<News> getNewsList();
}
