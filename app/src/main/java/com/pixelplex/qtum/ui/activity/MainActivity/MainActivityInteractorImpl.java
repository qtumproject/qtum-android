package com.pixelplex.qtum.ui.activity.MainActivity;

import android.content.Context;

import com.pixelplex.qtum.datastorage.HistoryList;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.datastorage.NewsList;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;


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
