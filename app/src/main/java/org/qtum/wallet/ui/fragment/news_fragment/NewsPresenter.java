package org.qtum.wallet.ui.fragment.news_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface NewsPresenter extends BaseFragmentPresenter {
    void onRefresh();

    void updateNews();

}
