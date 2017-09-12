package org.qtum.wallet.ui.fragment.transaction_fragment;



import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.datastorage.HistoryList;

class TransactionFragmentInteractorImpl implements TransactionFragmentInteractor {

    TransactionFragmentInteractorImpl(){

    }

    @Override
    public History getHistory(int position) {
        return HistoryList.getInstance().getHistoryList().get(position);
    }
}
