package com.pixelplex.qtum.ui.fragment.AddressesFragment.Light;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.ui.fragment.AddressesFragment.AddressHolder;
import com.pixelplex.qtum.ui.fragment.AddressesFragment.OnAddressClickListener;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class AddressHolderLight extends AddressHolder {

    protected AddressHolderLight(View itemView, OnAddressClickListener listener) {
        super(itemView, listener);
    }

    public void bindAddress(String address, int position) {
        if (position == KeyStorage.getInstance().getCurrentKeyPosition()) {
            mImageViewCheckIndicator.setVisibility(View.VISIBLE);
            mLinearLayoutAddress.setBackgroundColor(ContextCompat.getColor(mLinearLayoutAddress.getContext(), R.color.qr_code_background));
        } else {
            mImageViewCheckIndicator.setVisibility(View.GONE);
            mLinearLayoutAddress.setBackgroundColor(ContextCompat.getColor(mLinearLayoutAddress.getContext(),android.R.color.transparent));
        }
        mTextViewAddress.setText(address);
    }
}
