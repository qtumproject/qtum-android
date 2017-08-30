package com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment.dark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.history.TransactionInfo;
import com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment.TransactionDetailAdapter;
import com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment.TransactionDetailHolder;

import java.util.List;

/**
 * Created by kirillvolkov on 11.07.17.
 */

public class TransactionDetailAdapterDark extends TransactionDetailAdapter {
    TransactionDetailAdapterDark(List<TransactionInfo> transactionInfoList) {
        super(transactionInfoList);
    }

    @Override
    public TransactionDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_transaction_detail, parent, false);
        return new TransactionDetailHolder(view);
    }
}
