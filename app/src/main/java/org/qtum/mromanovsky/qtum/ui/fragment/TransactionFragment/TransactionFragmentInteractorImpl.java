package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;
import org.qtum.mromanovsky.qtum.datastorage.HistoryList;

public class TransactionFragmentInteractorImpl implements TransactionFragmentInteractor {
    @Override
    public History getHistory(int position) {
        return HistoryList.getInstance().getHistoryList().get(position);
    }
}
