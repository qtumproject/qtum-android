package com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.TransactionInfo;

import java.util.List;


public interface TransactionDetailFragmentView {
    void setUpRecyclerView(List<TransactionInfo> transactionInfoList);
}
