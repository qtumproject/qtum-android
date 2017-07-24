package com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment.Light;

import com.pixelplex.qtum.model.gson.history.TransactionInfo;
import com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment.Dark.TransactionDetailAdapterDark;
import com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment.TransactionDetailFragment;

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
