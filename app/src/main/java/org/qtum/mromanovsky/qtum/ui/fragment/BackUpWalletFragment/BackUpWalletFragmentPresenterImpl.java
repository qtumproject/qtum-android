package org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

public class BackUpWalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements BackUpWalletFragmentPresenter {

    private BackUpWalletFragmentView mBackUpWalletFragmentView;

    public BackUpWalletFragmentPresenterImpl(BackUpWalletFragmentView backUpWalletFragmentView) {
        mBackUpWalletFragmentView = backUpWalletFragmentView;
    }

    @Override
    public BackUpWalletFragmentView getView() {
        return mBackUpWalletFragmentView;
    }
}
