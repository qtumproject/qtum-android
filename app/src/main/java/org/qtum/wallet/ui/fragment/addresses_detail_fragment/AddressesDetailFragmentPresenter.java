package org.qtum.wallet.ui.fragment.addresses_detail_fragment;

import android.os.Bundle;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.TransactionInfo;

import java.util.ArrayList;
import java.util.List;

class AddressesDetailFragmentPresenter {

    private AddressesDetailInteractor mAddressesDetailInteractor;
    private AddressesDetailView mAddressesDetailView;
    private History mHistory;

    AddressesDetailFragmentPresenter(AddressesDetailView transactionDetailFragmentView) {
        mAddressesDetailInteractor = new AddressesDetailInteractor();
        mAddressesDetailView = transactionDetailFragmentView;
    }

    private AddressesDetailView getView() {
        return mAddressesDetailView;
    }

    private AddressesDetailInteractor getInteractor() {
        return mAddressesDetailInteractor;
    }

    void onViewCreated(Bundle bundle) {
        mHistory = getInteractor().getHistory(bundle.getInt(AddressesDetailFragment.POSITION));
        if (mHistory != null) {
            List<TransactionInfo> transactionInfoList = new ArrayList<>();
            mHistory.getVin();
            mHistory.getVout();
            getView().setUpRecyclerView(transactionInfoList);
        }
    }
}
