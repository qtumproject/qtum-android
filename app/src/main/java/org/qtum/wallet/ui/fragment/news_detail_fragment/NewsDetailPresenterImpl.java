package org.qtum.wallet.ui.fragment.news_detail_fragment;


import org.jsoup.select.Elements;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;



public class NewsDetailPresenterImpl extends BaseFragmentPresenterImpl implements NewsDetailPresenter{

    NewsDetailView mNewsDetailView;
    NewsDetailInteractor mNewsDetailInteractor;

    public NewsDetailPresenterImpl(NewsDetailView newsDetailView, NewsDetailInteractor newsDetailInteractor){
        mNewsDetailView = newsDetailView;
        mNewsDetailInteractor = newsDetailInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        int newsPosition = getView().getNewsPosition();
        Elements elements = getInteractor().getElements(newsPosition);
        getView().setupElements(elements);
    }

    @Override
    public NewsDetailView getView() {
        return mNewsDetailView;
    }

    public NewsDetailInteractor getInteractor() {
        return mNewsDetailInteractor;
    }
}
