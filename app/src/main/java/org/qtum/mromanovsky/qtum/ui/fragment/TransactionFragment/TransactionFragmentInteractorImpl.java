package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;
import org.qtum.mromanovsky.qtum.datastorage.HistoryList;

class TransactionFragmentInteractorImpl implements TransactionFragmentInteractor {

    TransactionFragmentInteractorImpl(){

    }

    @Override
    public History getHistory(int position) {
        return HistoryList.getInstance().getHistoryList().get(position);
    }
}
