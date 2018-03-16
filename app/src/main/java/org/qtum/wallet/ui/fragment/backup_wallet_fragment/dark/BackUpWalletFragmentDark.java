package org.qtum.wallet.ui.fragment.backup_wallet_fragment.dark;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.backup_wallet_fragment.BackUpWalletFragment;

import butterknife.BindView;

public class BackUpWalletFragmentDark extends BackUpWalletFragment {

    @BindView(R.id.toolbar)
    View toolbar;
    @BindView(R.id.ibt_back)
    ImageButton btnBack;
    @BindView(R.id.bt_share)
    ImageButton btnShare;
    @BindView(R.id.tv_toolbar_title)
    TextView toolbarTitle;

    @Override
    protected int getLayout() {
        return R.layout.fragment_back_up_wallet;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getMainActivity().recolorStatusBar(R.color.colorPrimary);
        if (getArguments().getBoolean(IS_WALLET_CREATING)) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.background));
            btnBack.setColorFilter(getResources().getColor(R.color.colorPrimary));
            btnShare.setColorFilter(getResources().getColor(R.color.colorPrimary));
            toolbarTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

}
