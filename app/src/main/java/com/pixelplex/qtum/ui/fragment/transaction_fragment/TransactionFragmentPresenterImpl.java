package com.pixelplex.qtum.ui.fragment.transaction_fragment;


import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.model.gson.history.Vin;
import com.pixelplex.qtum.model.gson.history.Vout;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.DateCalculator;

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
