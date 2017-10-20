package org.qtum.wallet.ui.fragment.import_wallet_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

interface ImportWalletPresenter extends BaseFragmentPresenter{
    void onImportClick(String brainCode);
    void onPassphraseChange(String passphrase);
}
