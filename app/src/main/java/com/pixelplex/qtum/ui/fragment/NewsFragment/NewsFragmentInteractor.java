package com.pixelplex.qtum.ui.fragment.NewsFragment;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.News;

import java.util.List;


interface NewsFragmentInteractor {
    void getNewsList(NewsFragmentInteractorImpl.GetNewsListCallBack callBack);
    List<News> getNewsList();
}
