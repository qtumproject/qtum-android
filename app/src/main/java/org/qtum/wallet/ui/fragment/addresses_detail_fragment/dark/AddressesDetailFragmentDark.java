package org.qtum.wallet.ui.fragment.addresses_detail_fragment.dark;

import org.qtum.wallet.model.gson.history.TransactionInfo;
import org.qtum.wallet.ui.fragment.addresses_detail_fragment.AddressesDetailFragment;

import java.util.List;

public class AddressesDetailFragmentDark extends AddressesDetailFragment {

    protected AddressesDetailAdapterDark mTransactionDetailAdapter;

    @Override
    public void setUpRecyclerView(List<TransactionInfo> transactionInfoList) {
        mTransactionDetailAdapter = new AddressesDetailAdapterDark(transactionInfoList);
        mRecyclerView.setAdapter(mTransactionDetailAdapter);
    }
}
