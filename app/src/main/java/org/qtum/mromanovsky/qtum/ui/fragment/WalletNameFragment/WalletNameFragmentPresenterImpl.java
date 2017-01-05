package org.qtum.mromanovsky.qtum.ui.fragment.WalletNameFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.utils.QtumSharedPreference;


public class WalletNameFragmentPresenterImpl extends BaseFragmentPresenterImpl implements WalletNameFragmentPresenter {

    private WalletNameFragmentView mWalletNameFragmentView;

    public WalletNameFragmentPresenterImpl(WalletNameFragmentView walletNameFragmentView){
        mWalletNameFragmentView = walletNameFragmentView;
    }

    @Override
    public void confirm(String name) {
        QtumSharedPreference.getInstance().saveWalletName(getView().getContext(),name);
        getView().confirm();
    }

    @Override
    public WalletNameFragmentView getView() {
        return mWalletNameFragmentView;
    }
}
