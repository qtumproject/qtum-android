package org.qtum.wallet.ui.fragment.addresses_detail_fragment;

import android.support.v7.widget.RecyclerView;

import org.qtum.wallet.model.gson.history.TransactionInfo;

import java.util.List;

public abstract class AddressesDetailAdapter<T extends TransactionInfo> extends RecyclerView.Adapter<AddressesDetailHolder> {

    private List<T> mTransactionInfoList;
    private String mSymbol;

    protected AddressesDetailAdapter(List<T> transactionInfoList, String symbol) {
        mTransactionInfoList = transactionInfoList;
        mSymbol = symbol;
    }

    @Override
    public void onBindViewHolder(AddressesDetailHolder holder, int position) {
        holder.bindTransactionDetail(mTransactionInfoList.get(position),mSymbol);
    }

    @Override
    public int getItemCount() {
        return mTransactionInfoList.size();
    }
}
