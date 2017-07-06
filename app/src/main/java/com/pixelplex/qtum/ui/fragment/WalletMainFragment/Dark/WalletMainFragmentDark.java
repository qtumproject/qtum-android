package com.pixelplex.qtum.ui.fragment.WalletMainFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.WalletMainFragment.WalletMainFragment;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class WalletMainFragmentDark extends WalletMainFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_wallet_main;
    }

    @Override
    public void showOtherTokens(boolean isShow) {
        if(pager.getAdapter() != null) {
            ((FragmentAdapter) pager.getAdapter()).showOtherTokens(isShow);
        }
    }
}
