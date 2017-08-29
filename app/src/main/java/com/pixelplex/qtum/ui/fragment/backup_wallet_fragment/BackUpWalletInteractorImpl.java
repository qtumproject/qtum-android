package com.pixelplex.qtum.ui.fragment.backup_wallet_fragment;

import android.content.Context;

import com.pixelplex.qtum.datastorage.QtumSharedPreference;


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
