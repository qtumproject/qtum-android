package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment;


import android.os.Bundle;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.TransactionInfo;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.Vin;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.Vout;

import java.util.ArrayList;
import java.util.List;

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
        List<TransactionInfo> transactionInfoList = new ArrayList<>();
        switch (bundle.getInt(TransactionDetailFragment.ACTION)){
            case TransactionDetailFragment.ACTION_FROM:
                for(Vin vin : mHistory.getVin()){
                    transactionInfoList.add(vin);
                }
                break;
            case TransactionDetailFragment.ACTION_TO:
                for(Vout vout : mHistory.getVout()){
                    transactionInfoList.add(vout);
                }
                break;
        }
        getView().setUpRecyclerView(transactionInfoList);

    }
}
