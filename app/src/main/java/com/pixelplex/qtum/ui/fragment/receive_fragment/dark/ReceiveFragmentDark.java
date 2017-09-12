package com.pixelplex.qtum.ui.fragment.receive_fragment.dark;

import android.support.v4.content.ContextCompat;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.receive_fragment.ReceiveFragment;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class ReceiveFragmentDark extends ReceiveFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_receive;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getPresenter().setQrColors(ContextCompat.getColor(getContext(),R.color.colorPrimary), ContextCompat.getColor(getContext(),R.color.background));
    }

}
