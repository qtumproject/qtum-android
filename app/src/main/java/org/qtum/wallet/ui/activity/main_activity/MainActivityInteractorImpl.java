package org.qtum.wallet.ui.activity.main_activity;

import android.content.Context;

import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.NewsList;
import org.qtum.wallet.datastorage.QtumSharedPreference;


class MainActivityInteractorImpl implements MainActivityInteractor {

    private Context mContext;

    MainActivityInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public boolean getKeyGeneratedInstance(Context context) {
        return QtumSharedPreference.getInstance().getKeyGeneratedInstance(context);
    }

    public void clearStatic(){
        KeyStorage.getInstance().clearKeyStorage();
        HistoryList.getInstance().clearHistoryList();
        NewsList.getInstance().clearNewsList();
    }
}
