package org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.News;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface NewsFragmentView extends BaseFragmentView {
    void updateNews(List<News> newsList);
    void startRefreshAnimation();
    void setAdapterNull();
}