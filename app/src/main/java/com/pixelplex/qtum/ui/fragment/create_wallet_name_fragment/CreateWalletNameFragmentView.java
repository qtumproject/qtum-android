package com.pixelplex.qtum.ui.fragment.create_wallet_name_fragment;


import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

interface CreateWalletNameFragmentView extends BaseFragmentView {
    void setErrorText(String errorText);
    void clearError();
    String getPassphrase();
    boolean isCreating();
}
