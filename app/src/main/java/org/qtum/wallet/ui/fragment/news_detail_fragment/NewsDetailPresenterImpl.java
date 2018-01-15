package org.qtum.wallet.ui.fragment.news_detail_fragment;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class NewsDetailPresenterImpl extends BaseFragmentPresenterImpl implements NewsDetailPresenter {

    NewsDetailView mNewsDetailView;
    NewsDetailInteractor mNewsDetailInteractor;

    public NewsDetailPresenterImpl(NewsDetailView newsDetailView, NewsDetailInteractor newsDetailInteractor) {
        mNewsDetailView = newsDetailView;
        mNewsDetailInteractor = newsDetailInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        int newsPosition = getView().getNewsPosition();
        News news = getInteractor().getNews(newsPosition);
        if (news != null) {
            Document document = news.getDocument();
            if(document!=null){
                getView().setupElements(document.body().children());
            }
            getView().setUpTitleAndDate(news.getTitle(), news.getFullFormattedPubDate());
        }
    }

    @Override
    public NewsDetailView getView() {
        return mNewsDetailView;
    }

    public NewsDetailInteractor getInteractor() {
        return mNewsDetailInteractor;
    }
}
