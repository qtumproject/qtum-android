package com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment;

import com.pixelplex.qtum.model.gson.history.TransactionInfo;

import java.util.List;


interface TransactionDetailFragmentView {
    void setUpRecyclerView(List<TransactionInfo> transactionInfoList);
}
