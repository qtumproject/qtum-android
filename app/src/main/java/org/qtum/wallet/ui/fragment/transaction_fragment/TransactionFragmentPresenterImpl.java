package org.qtum.wallet.ui.fragment.transaction_fragment;


import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.Vin;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.utils.DateCalculator;

import java.util.ArrayList;
import java.util.List;

class TransactionFragmentPresenterImpl extends BaseFragmentPresenterImpl implements TransactionFragmentPresenter {

    private TransactionFragmentView mTransactionFragmentView;
    private TransactionFragmentInteractorImpl mTransactionFragmentInteractor;

    TransactionFragmentPresenterImpl(TransactionFragmentView transactionFragmentView) {
        mTransactionFragmentView = transactionFragmentView;
        mTransactionFragmentInteractor = new TransactionFragmentInteractorImpl();
    }

    @Override
    public TransactionFragmentView getView() {
        return mTransactionFragmentView;
    }

    private TransactionFragmentInteractorImpl getInteractor() {
        return mTransactionFragmentInteractor;
    }

    @Override
    public void openTransactionView(int position) {

        String dateString;
        History history = getInteractor().getHistory(position);
        if(history.getBlockTime()!=null) {
            dateString = DateCalculator.getFullDate(history.getBlockTime() * 1000L);
        } else {
            dateString = getView().getContext().getString(R.string.unconfirmed);
        }
        List<String> listTo = new ArrayList<>();
        List<String> listFrom = new ArrayList<>();

        for(Vout vout : history.getVout()){
            listTo.add(vout.getAddress());
        }

        for(Vin vin : history.getVin()){
            listFrom.add(vin.getAddress());
        }

        getView().setUpTransactionData(history.getChangeInBalance().toString(), dateString,
                listFrom, listTo, history.getBlockHeight()>0);
    }
}
