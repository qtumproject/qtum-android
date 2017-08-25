package com.pixelplex.qtum.ui.fragment.profile_fragment;

import android.content.Context;

import com.pixelplex.qtum.datastorage.HistoryList;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.datastorage.NewsList;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.datastorage.TinyDB;


class ProfileFragmentInteractorImpl implements ProfileFragmentInteractor {

    private Context mContext;

    ProfileFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public void clearWallet() {
        QtumSharedPreference.getInstance().clear(mContext);
        KeyStorage.getInstance().clearKeyStorage();
        KeyStorage.getInstance().clearKeyFile(mContext);
        HistoryList.getInstance().clearHistoryList();
        NewsList.getInstance().clearNewsList();
        TinyDB db = new TinyDB(mContext);
        db.clearTokenList();
        db.clearContractList();
        db.clearTemplateList();
    }
}
