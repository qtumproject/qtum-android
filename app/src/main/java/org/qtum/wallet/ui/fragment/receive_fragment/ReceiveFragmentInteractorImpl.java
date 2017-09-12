package org.qtum.wallet.ui.fragment.receive_fragment;


import android.content.Context;

import org.qtum.wallet.datastorage.KeyStorage;

class ReceiveFragmentInteractorImpl implements ReceiveFragmentInteractor{

    private Context mContext;

    ReceiveFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public String getCurrentReceiveAddress() {
        return KeyStorage.getInstance().getCurrentAddress();
    }

}