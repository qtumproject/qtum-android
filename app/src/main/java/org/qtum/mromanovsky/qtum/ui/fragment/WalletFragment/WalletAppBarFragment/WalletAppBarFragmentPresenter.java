package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletAppBarFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment.ReceiveFragment;

public class WalletAppBarFragmentPresenter {

    WalletAppBarFragmentView mWalletAppBarFragmentView;

    public WalletAppBarFragmentPresenter(WalletAppBarFragmentView walletAppBarFragmentView){
        mWalletAppBarFragmentView = walletAppBarFragmentView;
    }

    public void onReceiveClick(){
        ReceiveFragment receiveFragment = ReceiveFragment.newInstance();
        getView().openFragment(receiveFragment);
    }

    public WalletAppBarFragmentView getView(){
        return mWalletAppBarFragmentView;
    }

}
