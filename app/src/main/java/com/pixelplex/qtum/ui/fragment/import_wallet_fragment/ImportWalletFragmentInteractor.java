package com.pixelplex.qtum.ui.fragment.import_wallet_fragment;


interface ImportWalletFragmentInteractor {
    void importWallet(String seed, ImportWalletFragmentInteractorImpl.ImportWalletCallBack callBack);
}
