package com.pixelplex.qtum.ui.fragment.ImportWalletFragment;


interface ImportWalletFragmentInteractor {
    void importWallet(String seed, ImportWalletFragmentInteractorImpl.ImportWalletCallBack callBack);
}
