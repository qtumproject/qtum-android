package com.pixelplex.qtum.ui.fragment.AddressListFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelplex.qtum.model.DeterministicKeyWithBalance;
import com.pixelplex.qtum.model.gson.UnspentOutput;
import com.pixelplex.qtum.utils.CurrentNetParams;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class AddressesWithBalanceAdapter extends RecyclerView.Adapter<AddressWithBalanceHolder> {

    protected List<DeterministicKeyWithBalance> mDeterministicKeyWithBalance;
    protected OnAddressClickListener listener;
    private int mResID;

    public AddressesWithBalanceAdapter(List<DeterministicKeyWithBalance> deterministicKeys, OnAddressClickListener listener, int resId) {
        mDeterministicKeyWithBalance = deterministicKeys;
        this.listener = listener;
        mResID = resId;
    }

    @Override
    public AddressWithBalanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(mResID, parent, false);
        return new AddressWithBalanceHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(AddressWithBalanceHolder holder, int position) {
        holder.bindDeterministicKeyWithBalance(mDeterministicKeyWithBalance.get(position));
    }

    @Override
    public int getItemCount() {
        return mDeterministicKeyWithBalance.size();
    }
}