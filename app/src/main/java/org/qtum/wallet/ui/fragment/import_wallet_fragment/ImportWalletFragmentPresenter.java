package org.qtum.wallet.ui.fragment.import_wallet_fragment;


interface ImportWalletFragmentPresenter {
    void onCancelClick();
    void onImportClick(String brainCode);
}
