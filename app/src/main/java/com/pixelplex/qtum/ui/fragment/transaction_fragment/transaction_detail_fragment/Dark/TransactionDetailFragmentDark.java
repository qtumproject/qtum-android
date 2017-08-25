package com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment.Dark;

import com.pixelplex.qtum.model.gson.history.TransactionInfo;
import com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment.TransactionDetailFragment;

import java.util.List;

/**
 * Created by kirillvolkov on 11.07.17.
 */

public class TransactionDetailFragmentDark extends TransactionDetailFragment {

    protected TransactionDetailAdapterDark mTransactionDetailAdapter;

    @Override
    public void setUpRecyclerView(List<TransactionInfo> transactionInfoList) {
        mTransactionDetailAdapter = new TransactionDetailAdapterDark(transactionInfoList);
        mRecyclerView.setAdapter(mTransactionDetailAdapter);
    }
}
