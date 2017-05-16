package com.pixelplex.qtum.ui.fragment.ProfileFragment;

import android.content.Context;

import com.pixelplex.qtum.datastorage.HistoryList;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.datastorage.NewsList;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.datastorage.QtumToken;


class ProfileFragmentInteractorImpl implements ProfileFragmentInteractor {

    private Context mContext;

    ProfileFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public void clearWallet() {
        QtumSharedPreference.getInstance().clear(mContext);
        KeyStorage.getInstance().clearKeyStorage();
        HistoryList.getInstance().clearHistoryList();
        NewsList.getInstance().clearNewsList();
        QtumToken.getInstance().clearToken();
    }
}
