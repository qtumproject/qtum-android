package org.qtum.wallet.ui.fragment.addresses_detail_fragment;

import org.qtum.wallet.model.gson.history.TransactionInfo;

import java.util.List;

interface AddressesDetailView {
    void setUpRecyclerView(List<TransactionInfo> transactionInfoList);
}
