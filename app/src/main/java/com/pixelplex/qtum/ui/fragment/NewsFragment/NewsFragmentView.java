package com.pixelplex.qtum.ui.fragment.NewsFragment;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.News;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface NewsFragmentView extends BaseFragmentView {
    void updateNews(List<News> newsList);
    void startRefreshAnimation();
    void setAdapterNull();
}