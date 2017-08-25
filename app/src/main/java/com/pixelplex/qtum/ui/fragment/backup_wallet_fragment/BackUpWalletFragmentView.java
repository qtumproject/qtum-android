package com.pixelplex.qtum.ui.fragment.backup_wallet_fragment;


import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

interface BackUpWalletFragmentView extends BaseFragmentView {
    void setBrainCode(String seed);
    String getBrainCode();
    void showToast();
    String getPin();
}
