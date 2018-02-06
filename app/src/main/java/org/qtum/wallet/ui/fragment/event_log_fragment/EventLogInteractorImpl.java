package org.qtum.wallet.ui.fragment.event_log_fragment;


import android.content.Context;

import org.qtum.wallet.model.gson.history.History;

import java.lang.ref.WeakReference;
import java.util.List;

import io.realm.Realm;

public class EventLogInteractorImpl implements EventLogInteractor{

    WeakReference<Context> mContext;

    EventLogInteractorImpl(Context context){
        mContext = new WeakReference<Context>(context);
    }

    public History getHistory(String txHash) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(History.class)
                .equalTo("txHash", txHash)
                .findFirst();
    }

}
