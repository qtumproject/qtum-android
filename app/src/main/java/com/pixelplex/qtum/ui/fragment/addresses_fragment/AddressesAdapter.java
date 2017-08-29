package com.pixelplex.qtum.ui.fragment.addresses_fragment;

import android.support.v7.widget.RecyclerView;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public abstract class AddressesAdapter extends RecyclerView.Adapter<AddressHolder> {
    protected List<DeterministicKey> mDeterministicKeys;
    protected String mAddress;
    protected OnAddressClickListener listener;

    public AddressesAdapter(List<DeterministicKey> deterministicKeys, OnAddressClickListener listener) {
        mDeterministicKeys = deterministicKeys;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mDeterministicKeys.size();
    }
}