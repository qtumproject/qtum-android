package org.qtum.wallet.ui.fragment.wallet_main_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;


interface WalletMainFragmentView extends BaseFragmentView {
    void showOtherTokens(boolean isShow);
    void showPageIndicator();
    void hidePageIndicator();
}
