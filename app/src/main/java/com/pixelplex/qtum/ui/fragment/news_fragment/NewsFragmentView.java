package com.pixelplex.qtum.ui.fragment.news_fragment;


import com.pixelplex.qtum.model.gson.News;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

interface NewsFragmentView extends BaseFragmentView {
    void updateNews(List<News> newsList);
    void startRefreshAnimation();
    void setAdapterNull();
}