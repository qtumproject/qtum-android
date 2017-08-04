package com.pixelplex.qtum.ui.fragment.AddressesListFragmentToken;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pixelplex.qtum.model.DeterministicKeyWithTokenBalance;
import com.pixelplex.qtum.ui.fragment.AddressListFragment.OnAddressClickListener;
import java.util.List;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public class TokenAdressesAdapter extends RecyclerView.Adapter<TokenAddressViewHolder> {

    List<DeterministicKeyWithTokenBalance> items;
    int resId;
    String currency;
    OnAddressTokenClickListener listener;

    public TokenAdressesAdapter(List<DeterministicKeyWithTokenBalance> items, int resId, OnAddressTokenClickListener listener, String currency){
        this.items = items;
        this.resId = resId;
        this.listener = listener;
        this.currency = currency;
    }

    @Override
    public TokenAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(resId, parent, false);
        return new TokenAddressViewHolder(view, listener, currency);
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
