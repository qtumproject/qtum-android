package org.qtum.wallet.ui.fragment.start_page_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.NewsList;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;

import java.lang.ref.WeakReference;

/**
 * Created by drevnitskaya on 09.10.17.
 */

public class StartPageInteractorImpl implements StartPageInteractor {

    private WeakReference<Context> mContext;

    public StartPageInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public boolean getGeneratedKey() {
        return QtumSharedPreference.getInstance().getKeyGeneratedInstance(mContext.get());
    }

    @Override
    public void clearWallet() {
        QtumSharedPreference.getInstance().clear(mContext.get());
        KeyStorage.getInstance().clearKeyStorage();
        KeyStorage.getInstance().clearKeyFile(mContext.get());
        HistoryList.getInstance().clearHistoryList();
        NewsList.getInstance().clearNewsList();
        TinyDB db = new TinyDB(mContext.get());
        db.clearTokenList();
        db.clearContractList();
        db.clearTemplateList();
    }
}
