package org.qtum.wallet.ui.fragment.start_page_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class StartPagePresenterImpl extends BaseFragmentPresenterImpl implements StartPagePresenter {

    private StartPageView mStartPageView;
    private StartPageInteractor mStartPagerInteractor;

    public StartPagePresenterImpl(StartPageView startPageView, StartPageInteractor startPageInteractor) {
        mStartPageView = startPageView;
        mStartPagerInteractor = startPageInteractor;
    }

    @Override
    public StartPageView getView() {
        return mStartPageView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        boolean isKeyGenerated = getInteractor().getGeneratedKey();
        if (!isKeyGenerated) {
            getView().hideLoginButton();
        }
    }

    @Override
    public void clearWallet() {
        getInteractor().clearWallet();
    }

    public StartPageInteractor getInteractor() {
        return mStartPagerInteractor;
    }
}
