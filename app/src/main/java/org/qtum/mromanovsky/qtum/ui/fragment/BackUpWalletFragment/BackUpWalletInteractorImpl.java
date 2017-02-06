package org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;

/**
 * Created by max-v on 2/6/2017.
 */

public class BackUpWalletInteractorImpl implements BackUpWalletInteractor {

    Context mContext;

    public BackUpWalletInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public String getSeed() {
        return QtumSharedPreference.getInstance().getSeed(mContext);
    }
}
