package org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


public class CreateWalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements CreateWalletFragmentPresenter {

    CreateWalletFragmentView mCreateWalletFragmentView;

    public CreateWalletFragmentPresenterImpl(CreateWalletFragmentView createWalletFragmentView){
        mCreateWalletFragmentView = createWalletFragmentView;
    }
}
