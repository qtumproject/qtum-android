package org.qtum.wallet.ui.fragment.addresses_detail_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.model.gson.history.History;

import java.lang.ref.WeakReference;
import java.util.List;

class AddressesDetailInteractorImpl implements AddressesDetailInteractor {

    WeakReference<Context> mContext;

    public AddressesDetailInteractorImpl(Context context) {
        mContext = new WeakReference<Context>(context);
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