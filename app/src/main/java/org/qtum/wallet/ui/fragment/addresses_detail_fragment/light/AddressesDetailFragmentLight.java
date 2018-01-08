package org.qtum.wallet.ui.fragment.addresses_detail_fragment.light;

import org.qtum.wallet.model.gson.history.TransactionInfo;
import org.qtum.wallet.ui.fragment.addresses_detail_fragment.AddressesDetailFragment;

import java.util.List;

public class AddressesDetailFragmentLight extends AddressesDetailFragment {
    protected AddressesDetailAdapterLight mTransactionDetailAdapter;

    @Override
    public void setUpRecyclerView(List<TransactionInfo> transactionInfoList) {
        mTransactionDetailAdapter = new AddressesDetailAdapterLight(transactionInfoList);
        mRecyclerView.setAdapter(mTransactionDetailAdapter);
    }
}
