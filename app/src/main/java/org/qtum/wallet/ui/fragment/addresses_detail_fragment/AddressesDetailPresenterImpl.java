package org.qtum.wallet.ui.fragment.addresses_detail_fragment;


import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.TransactionInfo;
import org.qtum.wallet.model.gson.history.Vin;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.model.gson.token_history.TokenHistory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;

class AddressesDetailPresenterImpl extends BaseFragmentPresenterImpl implements AddressesDetailPresenter {

    private AddressesDetailInteractor mAddressesDetailInteractor;
    private AddressesDetailView mAddressesDetailView;

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
        switch (getView().getHistoryType()){
            case History:
                History history = getInteractor().getHistory(getView().getTxHash());
                if (history != null) {
                    getView().setUpRecyclerView(history.getVin(), history.getVout());
                }
                break;
            case Token_History:
                TokenHistory  tokenHistory = getInteractor().getTokenHistory(getView().getTxHash());
                if(tokenHistory!=null){
                    List<Vin> vinList = new ArrayList<>();
                    List<Vout> voutList = new ArrayList<>();
                    vinList.add(new Vin(tokenHistory.getFrom(), tokenHistory.getAmount()));
                    voutList.add(new Vout(tokenHistory.getTo(), tokenHistory.getAmount()));
                    getView().setUpRecyclerView(vinList, voutList);
                }
                break;
        }


    }
}
