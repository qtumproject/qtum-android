package com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment.light;

import com.pixelplex.qtum.model.gson.history.TransactionInfo;
import com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment.TransactionDetailFragment;

import java.util.List;

/**
 * Created by kirillvolkov on 11.07.17.
 */

public class TransactionDetailFragmentLight extends TransactionDetailFragment {
    protected TransactionDetailAdapterLight mTransactionDetailAdapter;

    @Override
    public void setUpRecyclerView(List<TransactionInfo> transactionInfoList) {
        mTransactionDetailAdapter = new TransactionDetailAdapterLight(transactionInfoList);
        mRecyclerView.setAdapter(mTransactionDetailAdapter);
    }
}
