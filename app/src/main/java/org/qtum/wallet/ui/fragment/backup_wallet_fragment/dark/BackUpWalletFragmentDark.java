package org.qtum.wallet.ui.fragment.backup_wallet_fragment.dark;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.backup_wallet_fragment.BackUpWalletFragment;

public class BackUpWalletFragmentDark extends BackUpWalletFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_back_up_wallet;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getMainActivity().recolorStatusBar(R.color.background);
    }
}
