package com.pixelplex.qtum.ui.fragment.TransactionFragment;



import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.datastorage.HistoryList;

class TransactionFragmentInteractorImpl implements TransactionFragmentInteractor {

    TransactionFragmentInteractorImpl(){

    }

    @Override
    public History getHistory(int position) {
        return HistoryList.getInstance().getHistoryList().get(position);
    }
}
