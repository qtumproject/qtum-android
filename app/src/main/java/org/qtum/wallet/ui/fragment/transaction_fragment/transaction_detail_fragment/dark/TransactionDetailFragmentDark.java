package org.qtum.wallet.ui.fragment.transaction_fragment.transaction_detail_fragment.dark;

import org.qtum.wallet.model.gson.history.TransactionInfo;
import org.qtum.wallet.ui.fragment.transaction_fragment.transaction_detail_fragment.TransactionDetailFragment;

import java.util.List;

public class TransactionDetailFragmentDark extends TransactionDetailFragment {

    protected TransactionDetailAdapterDark mTransactionDetailAdapter;

    @Override
    public void setUpRecyclerView(List<TransactionInfo> transactionInfoList) {
        mTransactionDetailAdapter = new TransactionDetailAdapterDark(transactionInfoList);
        mRecyclerView.setAdapter(mTransactionDetailAdapter);
    }
}
