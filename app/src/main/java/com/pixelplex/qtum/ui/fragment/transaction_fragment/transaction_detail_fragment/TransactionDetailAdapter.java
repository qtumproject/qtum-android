package com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment;

import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.model.gson.history.TransactionInfo;

import java.util.List;

/**
 * Created by kirillvolkov on 11.07.17.
 */


public abstract class TransactionDetailAdapter extends RecyclerView.Adapter<TransactionDetailHolder>{

    private List<TransactionInfo> mTransactionInfoList;

    protected TransactionDetailAdapter(List<TransactionInfo> transactionInfoList){
        mTransactionInfoList = transactionInfoList;
    }

    @Override
    public void onBindViewHolder(TransactionDetailHolder holder, int position) {
        holder.bindTransactionDetail(mTransactionInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTransactionInfoList.size();
    }
}
