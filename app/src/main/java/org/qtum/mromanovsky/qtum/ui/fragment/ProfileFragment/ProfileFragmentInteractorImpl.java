package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.HistoryList;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.NewsList;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.datastorage.QtumToken;


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
        QtumToken.getInstance().clearQtumToken();
    }
}
