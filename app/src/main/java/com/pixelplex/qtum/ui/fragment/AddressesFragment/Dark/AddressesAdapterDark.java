package com.pixelplex.qtum.ui.fragment.AddressesFragment.Dark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.AddressesFragment.AddressHolder;
import com.pixelplex.qtum.ui.fragment.AddressesFragment.AddressesAdapter;
import com.pixelplex.qtum.ui.fragment.AddressesFragment.OnAddressClickListener;
import com.pixelplex.qtum.utils.CurrentNetParams;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class AddressesAdapterDark extends AddressesAdapter {

    public AddressesAdapterDark(List<DeterministicKey> deterministicKeys, OnAddressClickListener listener) {
        super(deterministicKeys, listener);
    }

    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_single_checkable, parent, false);
        return new AddressHolderDark(view, listener);
    }

    @Override
    public void onBindViewHolder(AddressHolder holder, int position) {
        mAddress = mDeterministicKeys.get(position).toAddress(CurrentNetParams.getNetParams()).toString();
        ((AddressHolderDark)holder).bindAddress(mAddress, position);
    }
}
