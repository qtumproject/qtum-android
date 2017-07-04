package com.pixelplex.qtum.ui.fragment.ReceiveFragment;


import android.content.Context;

import com.pixelplex.qtum.datastorage.HistoryList;
import com.pixelplex.qtum.datastorage.KeyStorage;

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