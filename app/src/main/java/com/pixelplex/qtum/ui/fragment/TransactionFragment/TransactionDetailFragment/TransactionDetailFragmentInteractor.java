package com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import com.pixelplex.qtum.datastorage.HistoryList;

public class TransactionDetailFragmentInteractor {

    public TransactionDetailFragmentInteractor(){

    }

    public History getHistory(int position){
        return HistoryList.getInstance().getHistoryList().get(position);
    }

}
