package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;

/**
 * Created by max-v on 2/6/2017.
 */

public interface ImportWalletFragmentInteractor {
    void importWallet(String seed, ImportWalletFragmentInteractorImpl.ImportWalletCallBack callBack);
}
