package org.qtum.wallet.ui.fragment.transaction_fragment.transaction_detail_fragment;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.datastorage.HistoryList;

import java.util.List;

class TransactionDetailFragmentInteractor {

    public TransactionDetailFragmentInteractor() {
    }

    public History getHistory(int position) {
        List<History> historyList = HistoryList.getInstance().getHistoryList();
        if (historyList != null && historyList.size() > position) {
            return historyList.get(position);
        } else {
            return null;
        }
    }
}
