package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import android.content.Context;

import org.qtum.mromanovsky.qtum.utils.QtumSharedPreference;


public class MainActivityInteractorImpl implements MainActivityInteractor {

    @Override
    public int getWalletPassword(Context context) {
        return QtumSharedPreference.getInstance().getWalletPassword(context);
    }
}
