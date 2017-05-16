package com.pixelplex.qtum.ui.fragment.CreateWalletNameFragment;


import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

interface CreateWalletNameFragmentView extends BaseFragmentView {
    void setErrorText(String errorText);
    void clearError();
}
