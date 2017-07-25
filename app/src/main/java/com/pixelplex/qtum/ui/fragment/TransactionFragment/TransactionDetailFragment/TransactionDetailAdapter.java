package com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
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
