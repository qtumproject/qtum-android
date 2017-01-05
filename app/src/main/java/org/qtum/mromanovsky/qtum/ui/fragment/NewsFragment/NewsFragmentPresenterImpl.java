package org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

public class NewsFragmentPresenterImpl extends BaseFragmentPresenterImpl implements NewsFragmentPresenter{

    NewsFragmentView mNewsFragmentView;

    public NewsFragmentPresenterImpl(NewsFragmentView newsFragmentView){
        mNewsFragmentView = newsFragmentView;
    }

    @Override
    public NewsFragmentView getView() {
        return mNewsFragmentView;
    }
}
