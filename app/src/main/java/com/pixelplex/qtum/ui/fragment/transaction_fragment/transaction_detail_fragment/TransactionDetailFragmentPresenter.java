package com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment;


import android.os.Bundle;

import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.model.gson.history.TransactionInfo;
import com.pixelplex.qtum.model.gson.history.Vin;
import com.pixelplex.qtum.model.gson.history.Vout;

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
