package org.qtum.wallet.ui.fragment.news_fragment;


import org.qtum.wallet.model.gson.News;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface NewsView extends BaseFragmentView {
    void updateNews(List<News> newsList);

    void startRefreshAnimation();

    void setAdapterNull();

    NewsInteractorImpl.GetNewsListCallBack getNewsCallback();
}