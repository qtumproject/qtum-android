package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;


public interface ImportWalletFragmentInteractor {
    void importWallet(String seed, ImportWalletFragmentInteractorImpl.ImportWalletCallBack callBack);
}
