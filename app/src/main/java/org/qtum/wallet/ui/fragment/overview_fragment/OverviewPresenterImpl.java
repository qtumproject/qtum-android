package org.qtum.wallet.ui.fragment.overview_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class OverviewPresenterImpl extends BaseFragmentPresenterImpl implements OverviewPresenter {

    OverviewIteractor mOverviewIteractor;
    OverviewView mOverviewView;

    OverviewPresenterImpl(OverviewView overviewView, OverviewIteractor overviewIteractor){
        mOverviewView = overviewView;
        mOverviewIteractor = overviewIteractor;
    }

    @Override
    public OverviewView getView() {
        return mOverviewView;
    }
}
