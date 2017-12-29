package org.qtum.wallet.ui.fragment.news_detail_fragment;

import org.qtum.wallet.model.news.News;

public interface NewsDetailInteractor {
    News getNews(int newsPosition);
}
