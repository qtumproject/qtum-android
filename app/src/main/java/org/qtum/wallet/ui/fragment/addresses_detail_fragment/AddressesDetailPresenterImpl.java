package org.qtum.wallet.ui.fragment.addresses_detail_fragment;


import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.TransactionInfo;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;

class AddressesDetailPresenterImpl extends BaseFragmentPresenterImpl implements AddressesDetailPresenter {

    private AddressesDetailInteractor mAddressesDetailInteractor;
    private AddressesDetailView mAddressesDetailView;
    private History mHistory;

    AddressesDetailPresenterImpl(AddressesDetailView transactionDetailFragmentView, AddressesDetailInteractor addressesDetailInteractor) {
        mAddressesDetailInteractor = addressesDetailInteractor;
        mAddressesDetailView = transactionDetailFragmentView;
    }

    @Override
    public AddressesDetailView getView() {
        return mAddressesDetailView;
    }

    private AddressesDetailInteractor getInteractor() {
        return mAddressesDetailInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mHistory = getInteractor().getHistory(getView().getPosition());
        if (mHistory != null) {
            List<TransactionInfo> transactionInfoList = new ArrayList<>();

            getView().setUpRecyclerView(mHistory.getVin(), mHistory.getVout());
        }
    }
}
