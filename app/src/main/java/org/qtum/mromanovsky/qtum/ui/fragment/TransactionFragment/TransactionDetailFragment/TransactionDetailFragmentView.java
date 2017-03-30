package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.TransactionInfo;

import java.util.List;


public interface TransactionDetailFragmentView {
    void setUpRecyclerView(List<TransactionInfo> transactionInfoList);
}
