package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.model.AddressWithBalance;

import java.util.List;

public class AddressesWithBalanceAdapter extends RecyclerView.Adapter<AddressWithBalanceHolder> {

    protected List<AddressWithBalance> mDeterministicKeyWithBalance;
    protected OnAddressClickListener listener;
    private int mResID;

    public AddressesWithBalanceAdapter(List<AddressWithBalance> deterministicKeys, OnAddressClickListener listener, int resId) {
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