package org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

interface CreateWalletNameFragmentView extends BaseFragmentView {
    void setErrorText(String errorText);
    void clearError();
}
