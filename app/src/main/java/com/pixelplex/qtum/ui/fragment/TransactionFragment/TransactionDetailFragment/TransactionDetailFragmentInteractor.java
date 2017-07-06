package com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment;


import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.datastorage.HistoryList;

class TransactionDetailFragmentInteractor {

    public TransactionDetailFragmentInteractor(){

    }

    public History getHistory(int position){
        return HistoryList.getInstance().getHistoryList().get(position);
    }

}
