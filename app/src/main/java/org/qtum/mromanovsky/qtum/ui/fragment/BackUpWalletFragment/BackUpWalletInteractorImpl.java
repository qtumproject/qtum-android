package org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;


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
