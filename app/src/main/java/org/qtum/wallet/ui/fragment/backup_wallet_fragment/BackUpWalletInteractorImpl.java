package org.qtum.wallet.ui.fragment.backup_wallet_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.QtumSharedPreference;


class BackUpWalletInteractorImpl implements BackUpWalletInteractor {

    private Context mContext;

    BackUpWalletInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public String getSeed() {
        return QtumSharedPreference.getInstance().getSeed(mContext);
    }
}
