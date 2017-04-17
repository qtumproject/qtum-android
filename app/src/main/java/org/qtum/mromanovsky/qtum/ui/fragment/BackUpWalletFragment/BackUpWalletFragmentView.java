package org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

interface BackUpWalletFragmentView extends BaseFragmentView {
    void setBrainCode(String seed);
    void showToast();
}
