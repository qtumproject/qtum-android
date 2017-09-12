package org.qtum.wallet.ui.fragment.backup_wallet_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

interface BackUpWalletFragmentView extends BaseFragmentView {
    void setBrainCode(String seed);
    String getBrainCode();
    void showToast();
    String getPin();
}
