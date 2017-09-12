package org.qtum.wallet.ui.fragment.transaction_fragment.transaction_detail_fragment;

import org.qtum.wallet.model.gson.history.TransactionInfo;

import java.util.List;


interface TransactionDetailFragmentView {
    void setUpRecyclerView(List<TransactionInfo> transactionInfoList);
}
