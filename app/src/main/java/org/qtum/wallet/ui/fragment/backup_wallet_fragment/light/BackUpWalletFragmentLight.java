package org.qtum.wallet.ui.fragment.backup_wallet_fragment.light;

import org.qtum.wallet.ui.fragment.backup_wallet_fragment.BackUpWalletFragment;

/**
 * Created by kirillvolkov on 24.07.17.
 */

public class BackUpWalletFragmentLight extends BackUpWalletFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_back_up_wallet_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getMainActivity().recolorStatusBar(org.qtum.wallet.R.color.title_color_light);
    }
}
