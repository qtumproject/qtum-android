package org.qtum.wallet.ui.fragment.addresses_detail_fragment;

import android.support.v7.widget.RecyclerView;

import org.qtum.wallet.model.gson.history.TransactionInfo;

import java.util.List;

public abstract class AddressesDetailAdapter extends RecyclerView.Adapter<AddressesDetailHolder> {

    private List<TransactionInfo> mTransactionInfoList;

    protected AddressesDetailAdapter(List<TransactionInfo> transactionInfoList) {
        mTransactionInfoList = transactionInfoList;
    }

    @Override
    public void onBindViewHolder(AddressesDetailHolder holder, int position) {
        holder.bindTransactionDetail(mTransactionInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTransactionInfoList.size();
    }
}
