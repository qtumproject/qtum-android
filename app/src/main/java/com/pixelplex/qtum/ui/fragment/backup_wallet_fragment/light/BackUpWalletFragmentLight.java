package com.pixelplex.qtum.ui.fragment.backup_wallet_fragment.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.backup_wallet_fragment.BackUpWalletFragment;

/**
 * Created by kirillvolkov on 24.07.17.
 */

public class BackUpWalletFragmentLight extends BackUpWalletFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_back_up_wallet_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getMainActivity().recolorStatusBar(R.color.title_color_light);
    }
}
