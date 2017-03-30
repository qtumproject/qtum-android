package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import org.qtum.mromanovsky.qtum.datastorage.HistoryList;

public class TransactionDetailFragmentInteractor {

    public TransactionDetailFragmentInteractor(){

    }

    public History getHistory(int position){
        return HistoryList.getInstance().getHistoryList().get(position);
    }

}
