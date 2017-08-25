package com.pixelplex.qtum.ui.fragment.wallet_main_fragment;

import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;


interface WalletMainFragmentView extends BaseFragmentView {
    void showOtherTokens(boolean isShow);
    void showPageIndicator();
    void hidePageIndicator();
}
