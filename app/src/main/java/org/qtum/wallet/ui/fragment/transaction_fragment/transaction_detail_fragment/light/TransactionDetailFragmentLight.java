package org.qtum.wallet.ui.fragment.transaction_fragment.transaction_detail_fragment.light;

import org.qtum.wallet.model.gson.history.TransactionInfo;
import org.qtum.wallet.ui.fragment.transaction_fragment.transaction_detail_fragment.TransactionDetailFragment;

import java.util.List;

public class TransactionDetailFragmentLight extends TransactionDetailFragment {
    protected TransactionDetailAdapterLight mTransactionDetailAdapter;

    @Override
    public void setUpRecyclerView(List<TransactionInfo> transactionInfoList) {
        mTransactionDetailAdapter = new TransactionDetailAdapterLight(transactionInfoList);
        mRecyclerView.setAdapter(mTransactionDetailAdapter);
    }
}
