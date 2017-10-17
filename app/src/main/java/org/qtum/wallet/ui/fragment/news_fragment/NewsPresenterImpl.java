package org.qtum.wallet.ui.fragment.news_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class NewsPresenterImpl extends BaseFragmentPresenterImpl implements NewsPresenter {

    private NewsView mNewsFragmentView;
    private NewsInteractor mNewsFragmentInteractor;

    public NewsPresenterImpl(NewsView newsFragmentView, NewsInteractor newsInteractor) {
        mNewsFragmentView = newsFragmentView;
        mNewsFragmentInteractor = newsInteractor;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        loadAndUpdateNews();
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        updateNews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getInteractor().unSubscribe();
    }

    private NewsInteractor getInteractor() {
        return mNewsFragmentInteractor;
    }

    @Override
    public NewsView getView() {
        return mNewsFragmentView;
    }

    @Override
    public void onRefresh() {
        loadAndUpdateNews();
    }

    private void loadAndUpdateNews() {
        getView().startRefreshAnimation();
        getInteractor().getNewsList(getView().getNewsCallback());
    }

    @Override
    public void updateNews() {
        getView().updateNews(getInteractor().getNewsList());
    }

}
