package com.pixelplex.qtum.ui.fragment.NewsFragment;

import com.pixelplex.qtum.model.gson.News;

import java.util.List;


interface NewsFragmentInteractor {
    void getNewsList(NewsFragmentInteractorImpl.GetNewsListCallBack callBack);
    List<News> getNewsList();
}
