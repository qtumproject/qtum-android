package org.qtum.wallet.ui.fragment.start_page_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;

import java.lang.ref.WeakReference;

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
        HistoryList.getInstance().clearHistoryList();
        TinyDB db = new TinyDB(mContext.get());
        db.clearTokenList();
        db.clearContractList();
        db.clearTemplateList();
    }
}
