package com.pixelplex.qtum.ui.fragment.BackUpWalletFragment;

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
