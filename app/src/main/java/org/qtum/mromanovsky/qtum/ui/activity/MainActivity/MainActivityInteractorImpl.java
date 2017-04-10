package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.HistoryList;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.NewsList;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.datastorage.QtumToken;


class MainActivityInteractorImpl implements MainActivityInteractor {

    Context mContext;

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
        QtumToken.getInstance().clearQtumToken();
    }
}
