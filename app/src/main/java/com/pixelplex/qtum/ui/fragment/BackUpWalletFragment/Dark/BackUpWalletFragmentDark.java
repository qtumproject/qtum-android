package com.pixelplex.qtum.ui.fragment.BackUpWalletFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BackUpWalletFragment.BackUpWalletFragment;

/**
 * Created by kirillvolkov on 24.07.17.
 */

public class BackUpWalletFragmentDark extends BackUpWalletFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_back_up_wallet;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getMainActivity().recolorStatusBar(R.color.colorPrimary);
    }
}
