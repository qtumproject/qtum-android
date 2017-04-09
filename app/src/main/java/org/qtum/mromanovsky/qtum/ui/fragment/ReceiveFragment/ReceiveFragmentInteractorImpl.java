package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;


import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.HistoryList;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;

class ReceiveFragmentInteractorImpl implements ReceiveFragmentInteractor{

    private Context mContext;

    ReceiveFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public String getCurrentReceiveAddress() {
        return KeyStorage.getInstance().getCurrentAddress();
    }

    @Override
    public String getBalance() {
        return HistoryList.getInstance().getBalance();
    }
}