package com.pixelplex.qtum.ui.fragment.news_fragment;

import com.pixelplex.qtum.model.gson.News;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.List;

class NewsFragmentPresenterImpl extends BaseFragmentPresenterImpl implements NewsFragmentPresenter {

    private NewsFragmentView mNewsFragmentView;
    private NewsFragmentInteractorImpl mNewsFragmentInteractor;

    NewsFragmentPresenterImpl(NewsFragmentView newsFragmentView) {
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
        getInteractor().unSubscribe();
        getView().setAdapterNull();
    }

    private NewsFragmentInteractorImpl getInteractor() {
        return mNewsFragmentInteractor;
    }

    @Override
    public NewsFragmentView getView() {
        return mNewsFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        updateNews();
    }

    @Override
    public void onRefresh() {
        loadAndUpdateNews();
    }

    private void loadAndUpdateNews() {
        getView().startRefreshAnimation();
        getInteractor().getNewsList(new NewsFragmentInteractorImpl.GetNewsListCallBack() {
            @Override
            public void onSuccess(List<News> newsList) {
                updateNews();
            }
        });
    }

    private void updateNews() {
        getView().updateNews(getInteractor().getNewsList());
    }

}
