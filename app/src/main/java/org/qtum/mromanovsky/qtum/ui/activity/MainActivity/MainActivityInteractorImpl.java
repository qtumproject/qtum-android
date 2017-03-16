package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;


class MainActivityInteractorImpl implements MainActivityInteractor {

    MainActivityInteractorImpl(){

    }

    @Override
    public boolean getKeyGeneratedInstance(Context context) {
        return QtumSharedPreference.getInstance().getKeyGeneratedInstance(context);
    }
}
