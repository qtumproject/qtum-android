package org.qtum.wallet.ui.fragment.addresses_fragment;

import android.support.v7.widget.RecyclerView;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

public abstract class AddressesAdapter extends RecyclerView.Adapter<AddressHolder> {
    protected List<String> mAddresses;
    protected String mAddress;
    protected OnAddressClickListener listener;

    public AddressesAdapter(List<String> addresses, OnAddressClickListener listener) {
        mAddresses = addresses;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mAddresses.size();
    }
}