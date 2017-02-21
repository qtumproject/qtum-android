package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;


interface ImportWalletFragmentPresenter {
    void cancel();
    void onImportClick(String brainCode);
}
