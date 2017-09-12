package org.qtum.wallet.ui.fragment.profile_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.NewsList;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;


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
