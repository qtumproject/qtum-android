package org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

interface CreateWalletNameFragmentView extends BaseFragmentView {
    void incorrectName(String errorText);
    void clearError();
}
