package org.qtum.wallet.ui.fragment.news_fragment;


import org.qtum.wallet.model.news.News;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface NewsView extends BaseFragmentView {

    void startRefreshAnimation();

    void setAdapterNull();

    void updateNews(List<News> newses);
}