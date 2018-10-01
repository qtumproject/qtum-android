package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.model.AddressWithTokenBalance;

import java.util.List;

public class TokenAddressesAdapter extends RecyclerView.Adapter<TokenAddressViewHolder> {

    List<AddressWithTokenBalance> items;
    int resId;
    String currency;
    OnAddressTokenClickListener listener;
    int decimalUnits;

    public TokenAddressesAdapter(List<AddressWithTokenBalance> items, int resId, OnAddressTokenClickListener listener, String currency, int decimalUnits) {
        this.items = items;
        this.resId = resId;
        this.listener = listener;
        this.currency = currency;
        this.decimalUnits = decimalUnits;
    }

    @Override
    public TokenAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(resId, parent, false);
        return new TokenAddressViewHolder(view, listener, currency, decimalUnits);
    }

    @Override
    public void onBindViewHolder(TokenAddressViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
