package org.qtum.wallet.ui.fragment.receive_fragment.dark;

import android.support.v4.content.ContextCompat;

import org.qtum.wallet.ui.fragment.receive_fragment.ReceiveFragment;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class ReceiveFragmentDark extends ReceiveFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_receive;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getPresenter().setQrColors(ContextCompat.getColor(getContext(), org.qtum.wallet.R.color.colorPrimary), ContextCompat.getColor(getContext(), org.qtum.wallet.R.color.background));
    }

}
