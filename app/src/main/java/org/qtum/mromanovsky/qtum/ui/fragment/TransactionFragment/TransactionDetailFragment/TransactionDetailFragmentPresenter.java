package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment;


import android.os.Bundle;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;

class TransactionDetailFragmentPresenter {

    private TransactionDetailFragmentInteractor mTransactionDetailFragmentInteractor;
    private TransactionDetailFragmentView mTransactionDetailFragmentView;
    private History mHistory;

    TransactionDetailFragmentPresenter(TransactionDetailFragmentView transactionDetailFragmentView){
        mTransactionDetailFragmentInteractor = new TransactionDetailFragmentInteractor();
        mTransactionDetailFragmentView = transactionDetailFragmentView;
    }

    private TransactionDetailFragmentView getView(){
        return mTransactionDetailFragmentView;
    }

    private TransactionDetailFragmentInteractor getInteractor(){
        return mTransactionDetailFragmentInteractor;
    }

    void onViewCreated(Bundle bundle){
        mHistory = getInteractor().getHistory(bundle.getInt(TransactionDetailFragment.POSITION));
        getView().setUpViewPager(mHistory.getVin(), mHistory.getVout());
    }
}
