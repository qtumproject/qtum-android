package com.pixelplex.qtum.ui.fragment.addresses_fragment.light;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.addresses_fragment.AddressHolder;
import com.pixelplex.qtum.ui.fragment.addresses_fragment.AddressesAdapter;
import com.pixelplex.qtum.ui.fragment.addresses_fragment.OnAddressClickListener;
import com.pixelplex.qtum.utils.CurrentNetParams;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class AddressesAdapterLight extends AddressesAdapter {

    public AddressesAdapterLight(List<DeterministicKey> deterministicKeys, OnAddressClickListener listener) {
        super(deterministicKeys, listener);
    }

    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_single_checkable_light, parent, false);
        return new AddressHolderLight(view, listener);
    }

    @Override
    public void onBindViewHolder(AddressHolder holder, int position) {
        mAddress = mDeterministicKeys.get(position).toAddress(CurrentNetParams.getNetParams()).toString();
        ((AddressHolderLight)holder).bindAddress(mAddress, position);
    }
}