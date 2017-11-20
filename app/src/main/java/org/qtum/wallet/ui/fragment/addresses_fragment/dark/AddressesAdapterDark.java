package org.qtum.wallet.ui.fragment.addresses_fragment.dark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.ui.fragment.addresses_fragment.AddressHolder;
import org.qtum.wallet.ui.fragment.addresses_fragment.AddressesAdapter;
import org.qtum.wallet.ui.fragment.addresses_fragment.OnAddressClickListener;
import org.qtum.wallet.utils.CurrentNetParams;
import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

public class AddressesAdapterDark extends AddressesAdapter {

    public AddressesAdapterDark(List<DeterministicKey> deterministicKeys, OnAddressClickListener listener) {
        super(deterministicKeys, listener);
    }

    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(org.qtum.wallet.R.layout.item_single_checkable, parent, false);
        return new AddressHolderDark(view, listener);
    }

    @Override
    public void onBindViewHolder(AddressHolder holder, int position) {
        mAddress = mDeterministicKeys.get(position).toAddress(CurrentNetParams.getNetParams()).toString();
        ((AddressHolderDark) holder).bindAddress(mAddress, position);
    }
}
