package org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.News;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.util.List;

public class NewsFragmentPresenterImpl extends BaseFragmentPresenterImpl implements NewsFragmentPresenter {

    NewsFragmentView mNewsFragmentView;
    NewsFragmentInteractorImpl mNewsFragmentInteractor;

    public NewsFragmentPresenterImpl(NewsFragmentView newsFragmentView) {
        mNewsFragmentView = newsFragmentView;
        mNewsFragmentInteractor = new NewsFragmentInteractorImpl();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        loadAndUpdateNews();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().setAdapterNull();
    }

    public NewsFragmentInteractorImpl getInteractor() {
        return mNewsFragmentInteractor;
    }

    @Override
    public NewsFragmentView getView() {
        return mNewsFragmentView;
    }

    @Override
    public void onRefresh() {
        loadAndUpdateNews();
    }

    private void loadAndUpdateNews(){
        getView().startRefreshAnimation();
        getInteractor().getNewsList(new NewsFragmentInteractorImpl.GetNewsListCallBack() {
            @Override
            public void onSuccess(List<News> newsList) {
                getView().updateNews(newsList);
            }
        });
    }
}
