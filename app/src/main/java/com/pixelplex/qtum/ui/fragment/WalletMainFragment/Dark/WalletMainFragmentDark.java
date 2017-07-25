package com.pixelplex.qtum.ui.fragment.WalletMainFragment.Dark;

import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.WalletFragment.Dark.WalletFragmentDark;
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
    public void showPageIndicator() {
        ((WalletFragmentDark)((FragmentAdapter)pager.getAdapter()).getWalletFragment()).pagerIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePageIndicator() {
        ((WalletFragmentDark)((FragmentAdapter)pager.getAdapter()).getWalletFragment()).pagerIndicator.setVisibility(View.INVISIBLE);
    }

}
