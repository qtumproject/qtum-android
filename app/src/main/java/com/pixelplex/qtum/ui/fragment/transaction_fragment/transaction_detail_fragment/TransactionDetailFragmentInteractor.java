package com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment;


import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.datastorage.HistoryList;

class TransactionDetailFragmentInteractor {

    public TransactionDetailFragmentInteractor(){

    }

    public History getHistory(int position){
        return HistoryList.getInstance().getHistoryList().get(position);
    }

}
