package org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.News;

import java.util.List;


interface NewsFragmentInteractor {
    void getNewsList(NewsFragmentInteractorImpl.GetNewsListCallBack callBack);
    List<News> getNewsList();
}
