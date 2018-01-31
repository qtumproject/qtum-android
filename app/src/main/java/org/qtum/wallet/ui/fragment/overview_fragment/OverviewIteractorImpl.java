package org.qtum.wallet.ui.fragment.overview_fragment;


import android.content.Context;

import org.qtum.wallet.model.gson.history.History;

import java.lang.ref.WeakReference;
import java.util.List;

import io.realm.Realm;

public class OverviewIteractorImpl implements OverviewIteractor{

    WeakReference<Context> mContext;

    OverviewIteractorImpl(Context context){
        mContext = new WeakReference<Context>(context);
    }

    public History getHistory(String txHash) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(History.class)
                .equalTo("txHash", txHash)
                .findFirst();
    }

}
