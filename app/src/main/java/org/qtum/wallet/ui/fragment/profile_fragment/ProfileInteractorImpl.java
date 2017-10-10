package org.qtum.wallet.ui.fragment.profile_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.NewsList;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;


class ProfileInteractorImpl implements ProfileInteractor {

    private Context mContext;

    ProfileInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public void clearWallet() {
        QtumSharedPreference.getInstance().clear(mContext);
        KeyStorage.getInstance().clearKeyFile(mContext);
        KeyStorage.getInstance().clearKeyStorage();
        HistoryList.getInstance().clearHistoryList();
        NewsList.getInstance().clearNewsList();
        TinyDB db = new TinyDB(mContext);
        db.clearTokenList();
        db.clearContractList();
        db.clearTemplateList();
    }

    @Override
    public void setupLanguageChangeListener(LanguageChangeListener listener) {
        QtumSharedPreference.getInstance().addLanguageListener(listener);
    }

    @Override
    public void removeLanguageListener(LanguageChangeListener listener) {
        QtumSharedPreference.getInstance().removeLanguageListener(listener);
    }

    @Override
    public boolean isTouchIdEnable() {
        return QtumSharedPreference.getInstance().isTouchIdEnable(mContext);
    }
}
