package org.qtum.wallet.ui.fragment.overview_fragment;


import android.content.Context;

import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.model.gson.history.History;

import java.lang.ref.WeakReference;
import java.util.List;

public class OverviewIteractorImpl implements OverviewIteractor{

    WeakReference<Context> mContext;

    OverviewIteractorImpl(Context context){
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
